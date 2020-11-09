/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.findom.findomwebnew.business.LoginHelper;
import it.csi.findom.findomwebnew.business.servizifindomweb.ServiziFindomWeb;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AmministratoriDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ProssimoSportelloAttivoDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaSportelliAttiviDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.util.VerificaParametri;
import it.csi.findom.findomwebnew.presentation.vo.LayoutInclude;
import it.csi.findom.findomwebnew.presentation.vo.StatoDomanda;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.csi.findom.findomwebnew.presentation.vo.UserInfo;
import it.csi.findom.findomwebnew.util.SessionUtil;
import it.csi.iride2.policy.entity.Identita;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	
	protected static final Logger log = Logger.getLogger(Constants.APPLICATION_CODE + ".presentation");
	private static final String CLASS_NAME = "BaseAction";
	
	protected static LayoutInclude layoutInclude; // Singleton
	protected static String urlRouting; // Singleton
	private HttpServletRequest request;
	private HttpServletResponse response;

	// accesso al DB 
	private ServiziFindomWeb serviziFindomWeb;
	private String showNuovaDomanda = "true";
	private String prossimoSportelloAttivo = null;
	
	@Override
	public String execute() throws Exception {
		
		final String methodName = "execute";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.debug(logprefix + " BEGIN");
		
		LoginHelper loginHelper = new LoginHelper();

		// inizializzo gli include per le parti comuni delle JSP
		if (layoutInclude == null) {
			ServletContext context = ServletActionContext.getServletContext();
			log.debug(logprefix + " layoutInclude null, inizializzaione in corso...");
			setLayoutInclude(new LayoutInclude());
			layoutInclude.setPortalHead(context.getInitParameter("portalHead"));
			layoutInclude.setPortalHeader(context.getInitParameter("portalHeader"));
			layoutInclude.setApplicationHeader(context.getInitParameter("applicationHeader"));
			layoutInclude.setApplicationLinkHelpContact(context.getInitParameter("applicationLinkHelpContact"));
			layoutInclude.setPortalFooter(context.getInitParameter("portalFooter"));
		} else {
			log.debug(logprefix + " layoutInclude gia' inizializzato");
		}
		
		if (urlRouting == null) {
			ServletContext context = ServletActionContext.getServletContext();
			setUrlRouting(context.getInitParameter("urlRouting"));
						
		} else {
			log.debug(logprefix + " urlRouting gia' inizializzato a:" +urlRouting);
		}
	
		
		boolean lavoroInLocale = new Boolean(ServletActionContext.getServletContext().getInitParameter("is_local_deploy")).booleanValue();
		log.debug("lavoroInLocale = " + lavoroInLocale);
		
		
		// per Vulnerability TEST , parsifico parametri in request per evidenziare eventuali anomalie
		long start = System.currentTimeMillis();
		boolean exitWithError = VerificaParametri.controllaParamInReq(getServletRequest());
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		
		log.debug(logprefix + "Parametri in request sospetti : exitWithError="+exitWithError + ", elapsedTimeMillis="+elapsedTimeMillis+" ms");
		if(exitWithError){
			log.error(logprefix + "riscontrati errori nei parametri dell'aggregatore");

			try {
				getServiziFindomWeb().insertLogAudit(Constants.CSI_LOG_IDAPPL, "", getUserInfo().getCodFisc()+" - "+ getUserInfo().getCognome() + " " + getUserInfo().getNome(), 
						Constants.CSI_LOG_WRONGPARAM, "probabile manomissione parametri in request", "");
				
			} catch (ServiziFindomWebException e) {
				log.error(logprefix +" impossibile scrivere CSI_LOG_AUDIT: "+e);
			}

			return "badRequestParam";
		}
		
		if (!SessionUtil.existsKey(getServletRequest(), Constants.SESSION_KEY_INIT_CP, Constants.FLAG_TRUE, false)) {
			
			if (!lavoroInLocale) {
				
				// recupera l'oggetto dalla sessione (ce lo ha messo il filtro IrideIdAdapterFilter)
				log.debug(logprefix + " userInfo in sessione=[" + getUserInfo() + "]");
				
				try{
					serviziFindomWeb.insertLogAudit(Constants.CSI_LOG_IDAPPL, "", 
													getUserInfo().getCodFisc()+" - "+ getUserInfo().getCognome() + " " + getUserInfo().getNome(),
													Constants.CSI_LOG_OPER_LOGIN, "accesso al sistema", "");
				} catch (ServiziFindomWebException e) {
					log.error(logprefix +" impossibile scrivere CSI_LOG_AUDIT:"+e);
				}
	
				//PRTMTR80A01L219F/OPERATORI/MONITORAGGIO
				boolean isMonitoraggio = loginHelper.isUtenteMonitoraggio(getUserInfo());
				
				if(isMonitoraggio){
					log.debug(logprefix + " END - monitoraggio");
					getUserInfo().setRuolo(Constants.RUOLO_MON);
					return "monitoraggio";
				}
				
				// verifica RUOLO
				verificaRuolo(getUserInfo().getCodFisc());
				
				
				getServletRequest().getSession().setAttribute(Constants.SESSION_KEY_INIT_CP, Constants.FLAG_TRUE);
				
			} else {
				// LAVORO In locale
				log.debug(logprefix + " LAVORO In locale");
				//
				if(getServletRequest().getSession() != null){
					getServletRequest().getSession().setAttribute(Constants.SESSION_KEY_INIT_CP, Constants.FLAG_TRUE);
				}else{
					log.debug(logprefix + " getSession NULL");
				}
				log.debug(logprefix + " fakeCurrentUser");
				Identita fakeCurrentUser = accessoSimulatoIride(getServletRequest().getSession());
				getServletRequest().getSession().setAttribute("IRIDE_ID_SESSIONATTR", fakeCurrentUser);

				// verifica RUOLO
				verificaRuolo(getUserInfo().getCodFisc());
				
			}

			//setta informazioni iniziali necessarie al template
			initStatusInfo();
			
		} else {
			log.debug(logprefix + " utilizzo la sessione in request .");
			log.debug(logprefix + "userInfo= "+getUserInfo());
			
			log.debug(logprefix + "status= "+getStatus().toString());
			
			if(getStatus()==null){
				log.debug(logprefix + "status NULL "); // questo non dovrebbe succedere mai...
				return "sessionError";
			}else{
				log.debug(logprefix + "status IdSoggettoCollegato = "+getStatus().getIdSoggettoCollegato());
				if(getStatus().getIdSoggettoCollegato()==null ){
					// sessione scaduta!!!
					log.debug(logprefix + "status IdSoggettoCollegato NULL ");
					return "sessionError";
				} 
			}
		}
		
		verificaSportelliAttivi();
		aggiornaContextSportello();
		
		String result = new String();
		try {
			result = executeAction();
		} catch (SystemException e){
			log.error(logprefix + " SystemException: " + e);
			return ERROR;
		} catch (UnrecoverableException e){
			log.error(logprefix + " UnrecoverableException: " + e);
			return ERROR;
		} catch (CSIException e){
			log.error(logprefix + " CSIException: " + e);
			return ERROR;
		} catch (Exception e){
			log.error(logprefix + " Exception: " + e);
			return ERROR;
		} catch (Throwable e) {
			log.error(logprefix + " Throwable: " + e);
			return ERROR;
		} finally {
			log.debug(logprefix + " END");
		}
		return result;
	}
	
	
	/**
	 * Verifico quale ruolo ha l'utente collegato
	 * @param codFisc
	 * @throws ServiziFindomWebException
	 */
	private void verificaRuolo(String codFisc) throws ServiziFindomWebException {
		final String methodName = "verificaRuolo";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.debug(logprefix + " BEGIN");
		
		// verifico se l'utete collegato e' Amministratore
		AmministratoriDto amministratore = serviziFindomWeb.getAmministratoreByCodiceFiscale(getUserInfo().getCodFisc());
		if(amministratore!=null){
			// l'utente collegato e' un amministratore
			log.debug(logprefix + " l'utente e' un amministratore, IdAmministratore() =["+amministratore.getIdAmministratore()+"]");
			getUserInfo().setRuolo(Constants.RUOLO_AMM);
	
		} else {
		
			log.debug(logprefix + " l'utente non e' un amministratore");
			//l'utente non e' un amministratore, cerco le domande dell'impresa selezionata indipendentemente dal creatore
			getUserInfo().setRuolo(Constants.RUOLO_NOR);
		
		}
		log.debug(logprefix + " END");
	}

	/**
	 * parte delle info verranno verranno settate/sovrascritte dalla classe
	 * DomandaAction in fase di modifica della proposta
	 * e dalla classe GetTemplateAction.java in fase di inserimento proposta
	 * 
	 * @param session
	 * @throws ServiziFindomWebException 
	 */
	private void initStatusInfo() throws ServiziFindomWebException {
		final String methodName = "initStatusInfo";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.debug(logprefix + " BEGIN");

		// N.B: tutti gli attributi dello StatusInfo qui sono nulli (a meno di quelli che dipendono dall'utente loggato)
		// i vari attributi verranno valorizzati in seguito
		
		setStatus(new StatusInfo());

		Map<String, String> statoPropostaMap = new TreeMap<String, String>();
		ArrayList<StatoDomanda> elencoStatiD = getServiziFindomWeb().getStatiDomanda();

		if(elencoStatiD!=null){
			log.debug(logprefix + " elencoStatiD.length=" + elencoStatiD.size());
			for (StatoDomanda statoDomanda : elencoStatiD) {
				log.debug(logprefix + " codice=[" + statoDomanda.getCodice()+"], descr=["+statoDomanda.getDescrizione()+"]");
				if(statoDomanda.getDataFineValidita()==null){
					statoPropostaMap.put(statoDomanda.getCodice(), statoDomanda.getDescrizione());
				}else{
					log.debug(logprefix + " stato con fine valida impostat, model_state=["+statoDomanda.getCodice()+"]");
				}
			}
		}else{
			log.debug(logprefix + " elencoStatiD NULL");
		}
		getStatus().setStatoPropostaMap(statoPropostaMap);
		
		getStatus().setOperatore(getUserInfo().getCodFisc());
		getStatus().setDescrizioneOperatore(getUserInfo().getCognome() + " " + getUserInfo().getNome());
		
		
		log.debug(logprefix + " status iniziale:" + getStatus().toString());

		TreeMap<String, Object> context = new TreeMap<String, Object>();
		context.put(Constants.USER_INFO, getUserInfo());
		context.put(Constants.STATUS_INFO, getStatus());
			
		getServletRequest().getSession().setAttribute(Constants.CONTEXT_ATTR_NAME, context);
		log.debug(logprefix + " creato contesto " + Constants.CONTEXT_ATTR_NAME);
		log.debug(logprefix + " END");
	}
	
	public abstract String executeAction() throws  SystemException, UnrecoverableException, CSIException, Throwable;
	
	private Identita accessoSimulatoIride(HttpSession session) {
		log.debug("[BaseAction::accessoSumulatoIride] BEGIN");

		Identita utenteFake = new Identita();

		//Demo27
		utenteFake.setCodFiscale("AAAAAA00A11H000P");
		utenteFake.setCognome("CSI PIEMONTE");
		utenteFake.setIdProvider("ACTALIS_EU");
		utenteFake.setLivelloAutenticazione(16);
		utenteFake.setMac("rCWechbOPKN7c+aDy+MkFw==");
		utenteFake.setTimestamp("20200515134107");
		utenteFake.setNome("DEMO 27");
				
		getUserInfo().setCodFisc(utenteFake.getCodFiscale());
		getUserInfo().setNome(utenteFake.getNome());
		getUserInfo().setRuolo("-");
		getUserInfo().setCognome(utenteFake.getCognome());
		getUserInfo().setIdIride("AAAAAA00A11H000P/CSI PIEMONTE/DEMO 27/ACTALIS_EU/20200515134107/16/rCWechbOPKN7c+aDy+MkFw==");
		
		session.setAttribute(Constants.IRIDE_ID_SESSIONATTR, getUserInfo().getIdIride());
		log.debug("[BaseAction::accessoSumulatoIride] END");
		return utenteFake;
	}
	
	protected void verificaSportelliAttivi() {
		
		final String methodName = "verificaSportelliAttivi";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.info(logprefix + " BEGIN");
		setShowNuovaDomanda("true");

		List<VistaSportelliAttiviDto> listaSportelli = new ArrayList<VistaSportelliAttiviDto>();
		try {
			listaSportelli = getServiziFindomWeb().getVistaSportelliAttivi();
			
			if(listaSportelli!=null && listaSportelli.size()>0){
				setShowNuovaDomanda("true");
				log.info(logprefix + " Esistono sportelli attivi: " + listaSportelli.size());

			} else {
				setShowNuovaDomanda("false");
				log.info(logprefix + " Non esistono sportelli attivi." );
				
				determinaProssimoSportelloAttivo();
			}
		} catch (ServiziFindomWebException ex){	
			log.info(logprefix + " Errore verifica sportelli attivi ", ex);
		} 
		
		log.info(logprefix + " END");

	}
	
	private void determinaProssimoSportelloAttivo() {
		
		final String methodName = "determinaProssimoSportelloAttivo";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.info(logprefix + " BEGIN");
		
		try {
			setProssimoSportelloAttivo(null);				

			ProssimoSportelloAttivoDto sportello = getServiziFindomWeb().getProssimoSportelloAttivo();
			
			if (sportello != null) {
				StringBuffer sb = new StringBuffer();
				
				sb.append("Il prossimo sportello aprirà il ")
				.append(sportello.getDtApertura())
				.append(" alle ore ")
				.append(sportello.getOraApertura())
				.append(" per il bando: ")
				.append(sportello.getDescrizioneBando());
				
				setProssimoSportelloAttivo(sb.toString());
				
				log.info(logprefix + " Messaggio sportelli prossimo sportello attivo: " + sb.toString());

			}
			
		} catch (ServiziFindomWebException ex){	
			setProssimoSportelloAttivo(null);
		} 
		log.info(logprefix + " END");

	}
	
	public Integer getIdStatoIstruttoria(Integer idBando,String codice) throws ServiziFindomWebException {
		final String methodName = "getIdStatoIstruttoria";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.info(logprefix + " BEGIN");
		Integer idStatoIstruttoriaRI = null;			
		String flagIstruttoriaEsternaFindom = getServiziFindomWeb().getFlagIstruttoriaEsterna(idBando);
		log.info(logprefix +" flagIstruttoriaEsternaFindom vale " + flagIstruttoriaEsternaFindom);
		if (StringUtils.isEmpty(flagIstruttoriaEsternaFindom) || !flagIstruttoriaEsternaFindom.equals("S")){
			//se il bando prevede l'istruttoria su findom recupero l'id dello stato avente codice 'RI'
			idStatoIstruttoriaRI = getServiziFindomWeb().getIdStatoIstruttoriaByCodice(codice);				
		}
		log.info(logprefix +" idStatoIstruttoriaRI vale " + (idStatoIstruttoriaRI == null ? "" : idStatoIstruttoriaRI));
		log.info(logprefix + " END");
		return idStatoIstruttoriaRI;
	}
	
	
	/**
	 * Verifico quale ruolo ha l'utente collegato
	 * @param codFisc
	 * @throws ServiziFindomWebException
	 */
	private void aggiornaContextSportello() throws ServiziFindomWebException {
		final String methodName = "aggiornaContextSportello";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.debug(logprefix + " BEGIN");
		
		if (getStatus().getNumProposta() != null) {
			String contextSportello = serviziFindomWeb.getIstanzaRoutingBySportello(getStatus().getNumSportello());
			if(contextSportello!=null){
				getStatus().setContextSportello(contextSportello);
			} else {
				ServletContext context = ServletActionContext.getServletContext();
				getStatus().setContextSportello(context.getInitParameter("defaultUrlRouting"));
			}

			log.debug(logprefix + " context sportello =["+contextSportello+"]");
			
		} 
		log.debug(logprefix + " END");
	}

	// GETTERS && SETTERS

	public static LayoutInclude getLayoutInclude() {
		return layoutInclude;
	}

	public static void setLayoutInclude(LayoutInclude layoutInclude) {
		BaseAction.layoutInclude = layoutInclude;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getServletRequest() {
		return this.request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public StatusInfo getStatus() {
		return (StatusInfo) getServletRequest().getSession().getAttribute(Constants.STATUS_INFO);
	}

	public void setStatus(StatusInfo stato) {
		getServletRequest().getSession().setAttribute(Constants.STATUS_INFO, stato);
	}

	public UserInfo getUserInfo() {
		return (UserInfo) getServletRequest().getSession().getAttribute(Constants.USERINFO_SESSIONATTR);
	}

	public void setUserInfo(UserInfo userInfo) {
		getServletRequest().getSession().setAttribute(Constants.USERINFO_SESSIONATTR, userInfo);
	}
	
	public ServiziFindomWeb getServiziFindomWeb() {		
		return serviziFindomWeb;
	}

	public void setServiziFindomWeb(ServiziFindomWeb serviziFindomWeb) {
		this.serviziFindomWeb = serviziFindomWeb;
	}

	public String getShowNuovaDomanda() {
		return showNuovaDomanda;
	}

	public void setShowNuovaDomanda(String showNuovaDomanda) {
		this.showNuovaDomanda = showNuovaDomanda;
	}

	public String getProssimoSportelloAttivo() {
		return prossimoSportelloAttivo;
	}

	public void setProssimoSportelloAttivo(String prossimoSportelloAttivo) {
		this.prossimoSportelloAttivo = prossimoSportelloAttivo;
	}

	public static String getUrlRouting() {
		return urlRouting;
	}

	public static void setUrlRouting(String urlRouting) {
		BaseAction.urlRouting = urlRouting;
	}
}
