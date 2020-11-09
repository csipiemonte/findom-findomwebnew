/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrTModelDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrTTemplateDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ParametroRegolaBandoDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.RegolaDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.TipolBeneficiariDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaSportelliAttiviDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.Domanda;
import it.csi.findom.findomwebnew.presentation.vo.SelItem;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.csi.melograno.aggregatore.business.AggregatoreFactory;
import it.csi.melograno.aggregatore.business.DataModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import bsh.EvalError;
import bsh.Interpreter;

public class CreateNewDomandaAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;

	private static final String CLASS_NAME = "CreateNewDomandaAction";

	// liste del form di inserimento nuova domanda
	SelItem[] listaNormativeINS;
	SelItem[] listadescBreveBandoINS;
	SelItem[] listaBandiINS;
	SelItem[] listaSportelliINS;
	TipolBeneficiariDto[] listaTipologieBeneficiariINS;
	
	// campi che vengono postati per l'inserimetno di una nuova domanda
	String normativaINS;
	String descBreveBandoINS;
	String bandoINS;
	String sportelloINS;
	String tipologiaBeneficiarioINS;
	
	// campi che vengono postati per la ricerca  (sono nel form di inserimento, quindi li azzero)
	String normativa;
	String descBreveBando;
	String bando;
	String sportello;
	String statoDomanda;
	String numDomanda;
	
	// liste del form di ricerca  (sono nel form di inserimento, quindi li azzero)
	SelItem[] listaNormative;
	SelItem[] listadescBreveBando;
	SelItem[] listaBandi;
	SelItem[] listaSportelli;
	SelItem[] listaStati;
	
	// lista delle domande trovate (sono nel form di inserimento, quindi li azzero)
	ArrayList<Domanda> listaDomande ;
			
	String numMaxDomandeBandoPresentate;// attributo per differenziare il testo della modale di conferma invio domanda in base al fatto che l'utente abbia inviato il numero massimo di domande o meno
	String numMaxDomandeSportelloPresentate;// attributo per differenziare il testo della modale di conferma invio domanda in base al fatto che l'utente abbia inviato il numero massimo di domande o meno
	String sportelloOpen;  // attributo per differenziare il testo della modale di conferma invio domanda in base al fatto che lo sportello sia aperto o meno
	
	String showPopup;
	
	@Override
	public String executeAction() throws SystemException, UnrecoverableException, CSIException {

		/*
		 * Considerazioni
		 *  - nella findom_t_bandi non ci possono essere piu' bandi per lo stesso template, e' stata fatta la scelta    1 Bando == 1 template
		 *  - un bando == un template vuol dire findom_t_bandi.id_bando  = aggr_t_template.template_id
		 * 
		 */
		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		log.info(logprefix + " normativaINS=["+normativaINS+"]");
		log.info(logprefix + " descBreveBandoINS=["+descBreveBandoINS+"]");
		log.info(logprefix + " bandoINS=["+bandoINS+"]");
		log.info(logprefix + " sportelloINS=["+sportelloINS+"]");
		log.info(logprefix + " tipologiaBeneficiarioINS=["+tipologiaBeneficiarioINS+"]");
		
		verificaSportelliAttivi();

		// VERIFICHE SUI DATI postati
		boolean datiErrati = false;
		setShowPopup("false");

		if(StringUtils.isBlank(normativaINS) || StringUtils.equals("-1", normativaINS)){
			log.warn(logprefix + " errore nel campo Normativa");
			addFieldError("normativaINS", Constants.ERR_MESSAGE_FIELD_OBBL);
			datiErrati = true;
		}
		
		if(StringUtils.isBlank(descBreveBandoINS) || StringUtils.equals("-1", descBreveBandoINS)){
			log.warn(logprefix + " errore nel campo Descrizione Breve Bando");
			addFieldError("descBreveBandoINS", Constants.ERR_MESSAGE_FIELD_OBBL);
			datiErrati = true;
		}
		
		if(StringUtils.isBlank(bandoINS) || StringUtils.equals("-1", bandoINS)){
			log.warn(logprefix + " errore nel campo Bando");
			addFieldError("bandoINS", Constants.ERR_MESSAGE_FIELD_OBBL);
			datiErrati = true;
		}
		
		if(StringUtils.isBlank(sportelloINS) || StringUtils.equals("-1", sportelloINS)){
			log.warn(logprefix + " errore nel campo Sportello");
			addFieldError("sportelloINS", Constants.ERR_MESSAGE_FIELD_OBBL);
			datiErrati = true;
		}
		
		if(StringUtils.isBlank(tipologiaBeneficiarioINS) || StringUtils.equals("-1", tipologiaBeneficiarioINS)){
			log.warn(logprefix + " errore nel campo Tipologia Beneficiario");
			addFieldError("tipologiaBeneficiarioINS", Constants.ERR_MESSAGE_FIELD_OBBL);
			datiErrati = true;
		}
		
		if(datiErrati){
			log.info(logprefix + " errori nei dati del form di inserimento");
			addActionError(Constants.ERR_MESSAGE_PARAM_ERROR);
			// popola le liste
			loadListe();
			return INPUT;
		}
		
		// fine VERIFICHE SUI DATI postati
		///////////////////////////////////
		// eseguo l'algoritmo A02 (findom_v_domande_beneficiari) e conto le domande presentate su quel bando per quel beneficiario
		
		StatusInfo state = getStatus();
		Integer idSoggBen = state.getIdSoggettoBeneficiario();
		log.info(logprefix + " idSoggBen="+idSoggBen);
		
		// verificato con Laura basta id_bando ( non devo cercare con i 3 ID )
		ArrayList<VistaSportelliAttiviDto> listaSportelliAttivi =  getServiziFindomWeb().getVistaSportelliAttiviByIdBando(Integer.parseInt(bandoINS));
		
		// la lista degli sportelli non dovrebbe mai essere nulla
		if(listaSportelliAttivi==null || ( listaSportelliAttivi!=null && listaSportelliAttivi.size()==0)){
			log.error(logprefix + " nessuna entry trovata nella lista degli sportelli attivi");
			return ERROR;
		}
		
		// metto nello statusInfo alcune informazioni
		int numSportello = listaSportelliAttivi.get(0).getIdSportelloBando();
		String aperturaSportelloDa = listaSportelliAttivi.get(0).getDtApertura();
		String aperturaSportelloA = listaSportelliAttivi.get(0).getDtChiusura();
		
		log.info(logprefix + " numSportello="+numSportello);
		log.info(logprefix + " aperturaSportelloDa="+aperturaSportelloDa);
		log.info(logprefix + " aperturaSportelloA="+aperturaSportelloA);
		
		state.setAperturaSportelloA(aperturaSportelloA);
		state.setAperturaSportelloDa(aperturaSportelloDa);
		state.setNumSportello(numSportello);
				
		// controlli superati
		// salvo i dati della nuova domanda su DB
	

		// PASSO 1 :recupero i dati dalla AGGR_T_TEMPLATE
		ArrayList<AggrTTemplateDto> listaTemplate = getServiziFindomWeb().getAggrTemplateWithFindomBandi(Integer.parseInt(bandoINS));
		if(listaTemplate==null || (listaTemplate!=null && listaTemplate.size()!=1)){
			log.warn(logprefix + " nessun template trovato, impossibile proseguire");
			addActionError(Constants.ERR_MESSAGE_NEWDOMANDA_DATIERROR);
			// popola le liste
			loadListe();
			return INPUT;
		}else{
			log.info(logprefix + " listaTemplate.size()="+listaTemplate.size());
		}
		
		String templateCode = listaTemplate.get(0).getTemplateCode();
		log.info(logprefix + " templateCode="+templateCode);
		
		String templateName = listaTemplate.get(0).getTemplateName();
		log.info(logprefix + " templateName="+templateName);
		
		String templateDescr = listaTemplate.get(0).getTemplateDescription();
		log.info(logprefix + " templateDescr="+templateDescr);
		
		// metto nello statusInfo alcune informazioni
		state.setStatoProposta(Constants.STATO_BOZZA);
		int templateId = listaTemplate.get(0).getTemplateId();
		log.info(logprefix + " templateId="+templateId);
		state.setTemplateId(templateId);
		

		//in questo punto applico le eventuali regole presenti sul DB per il bando corrente		
		boolean regoleValidazione = validaPossibilitaCreazioneNuovaDomanda(templateId, Integer.parseInt(bandoINS));
		if (!regoleValidazione) {
			log.warn(logprefix + " VERIFICATA INCONGRUENZA IN VALIDAZIONE REGOLE DOMANDA ; templateId = " +templateId + "; idBando = " +bandoINS);
			log.info(logprefix + " templateId="+templateId);	
			addActionError(Constants.ERROR_CREAZIONE_DOMANDA);
			
			// popola le liste
			loadListe();
			return INPUT;			
		}


		// PASSO 2 - inserisco una entry in AGGR_T_MODEL
		Integer intModelProg = null;
		Integer modelProgr = null; //usa sequence seq_model
		DataModel postedModel = new DataModel();
		String statoDomanda = Constants.STATO_BOZZA;
		String nomeDomanda = "Nuova Domanda";
		
		String userId = state.getCodFiscaleBeneficiario();
		
		try {
			
			intModelProg = AggregatoreFactory.create().saveModel(userId, templateCode, modelProgr, nomeDomanda, postedModel, null, statoDomanda);
			log.info(logprefix + "inserimento avvenuta con successo, intModelProg=" + intModelProg);

		} catch (Throwable e) {
			log.warn(logprefix + " impossibile salvare in aggrTModel "+e);
			addActionError(Constants.ERR_MESSAGE_NEWDOMANDA_DATIERROR);
			// popola le liste
			loadListe();
			return INPUT;
		}
		
		
		// PASSO 3 - salva dettaglio domanda in SHELL_T_DOMANDE
		// servizio per recuperare l'idModel appena inserito (inserito in tabella AGGR_T_MODEL) 
		ArrayList<AggrTModelDto> listaModel =  getServiziFindomWeb().getAggrTModelByUserTemplateProg(userId, templateCode, intModelProg);
		
		Integer idModel = null;
		if(listaModel!=null ){
			log.info(logprefix + " listaModel.size()="+listaModel.size());
			idModel = listaModel.get(0).getModelId();
			log.info(logprefix + " idModel="+idModel);
			
			state.setNumProposta(idModel);
		}
		
		int oo = getServiziFindomWeb().insertShellTDomande(state.getIdSoggettoCollegato(),
															idSoggBen,
															state.getNumSportello(),
															null,null,
															idModel,
															Integer.parseInt(tipologiaBeneficiarioINS));
		
		log.info(logprefix + " inserito record in tabella SHELL_T_DOMANDE");
		
		// TODO TRANSAZIONE !!!!!
		
		try {
			getServiziFindomWeb().insertLogAudit(Constants.CSI_LOG_IDAPPL, "", 
					getUserInfo().getCodFisc()+" - "+ getUserInfo().getCognome() + " " + getUserInfo().getNome(),
					Constants.CSI_LOG_OPER_INSERT_DOMANDA, "creata nuova domanda numero:"+idModel, "");
		} catch (ServiziFindomWebException e) {
			log.error(logprefix +" impossibile scrivere CSI_LOG_AUDIT: "+e);
		}
			

		// recupero i dati della domanda da visualizzare in alcune sezioni della pagina
		int idSoggettoCreatore = state.getIdSoggettoCollegato();
		
		//TODO : qui domanda nuova.... non ha senso usare query con UNION
		ArrayList<VistaDomandeDto> datiDomanda = getServiziFindomWeb().getVistaDomandaByCreatoreBeneficiarioDomanda(idSoggettoCreatore, idSoggBen, idModel);
		state.setDescrNormativa(datiDomanda.get(0).getNormativa());
		state.setCodiceAzione(datiDomanda.get(0).getCodiceAzione());
		state.setDescrBando(datiDomanda.get(0).getDescrBando());
		state.setDescrBreveBando(datiDomanda.get(0).getDescrBreveBando());
		state.setDescrTipolBeneficiario(datiDomanda.get(0).getDescrizioneTipolBeneficiario());
		state.setFlagBandoDematerializzato(datiDomanda.get(0).getFlagBandoDematerializzato());
		state.setTipoFirma(datiDomanda.get(0).getTipoFirma());
		state.setFlagProgettiComuni(datiDomanda.get(0).getFlagProgettiComuni());
		//in questo caso la sigla nazione presa da shell_t_soggetti va bene perche' la domanda e' in fase di creazione e non c'e'
		//l'xml (in particolare nodo _operatorePresentatore) in cui ci potrebbe essere un'altro stato, che sarebbe quello corretto da associare alla domanda 
		state.setSiglaNazione(datiDomanda.get(0).getSiglaNazione());  
		setStatus(state);
		
		TreeMap<String, Object> context = (TreeMap<String, Object>) this.getServletRequest().getSession().getAttribute(Constants.CONTEXT_ATTR_NAME);
		context.put(Constants.STATUS_INFO, state);
		log.info(logprefix + " valori dello stato inseriti in REQUEST");
		context.put(Constants.URL_BASE_LOCATION, getUrlRouting());
		log.info(logprefix + " valore dell'url di routing inserito in CONTEXT_ATTR_NAME:" + getUrlRouting());

		
		// PASSO 4 - apre pagina della domanda con il nuovo modello appena salvato.
		log.info(logprefix + " imposta param per l'anagrafica ");
		
		String xformId = templateCode; // sarebbe aggr_t_template.template_code
		String xformType =templateName; // sarebbe aggr_t_template.template_name
		String xformDesc =templateDescr; // sarebbe aggr_t_template.template_description

		getServletRequest().setAttribute("#xcommId", "ACCESS_RW_FORM");
		getServletRequest().setAttribute("#xformId", xformId);
		getServletRequest().setAttribute("#xformType", xformType);
		getServletRequest().setAttribute("#xformDesc", xformDesc);
		getServletRequest().setAttribute("#xformProg", intModelProg.toString());
		getServletRequest().setAttribute("#xformName", nomeDomanda);
		getServletRequest().setAttribute("#xformState", Constants.STATO_BOZZA);
		getServletRequest().setAttribute("#xdraftStr", "BOZZA");
	
		return SUCCESS;
	}

	private boolean loadListe() {
		final String methodName = "loadListe";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN LOADLISTE VUOTO.....");
		boolean result = true;
		log.info(logprefix + " END");
		return result;
	}
	
	
	/**
	 * Metodo per utilizzo del motore delle regole  : BEANSHELL
	 *  
	 * @param templateId
	 * @param idBando
	 * @return TRUE se non c'e' regola associato o se regola associata e' soddisfatta
	 * @throws ServiziFindomWebException
	 */
	private boolean validaPossibilitaCreazioneNuovaDomanda(int templateId, int idBando) throws ServiziFindomWebException { //, NumberFormatException, SystemException, UnrecoverableException, CSIException {
		final String methodName = "validaPossibilitaCreazioneNuovaDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		boolean validazione = true;		
		String scritpBeanShell = "";
		RegolaDto regola = null;
		ArrayList<ParametroRegolaBandoDto> paramRegolaBandoList = null;
		ArrayList<Integer> idRegolaList = null;
 
		//metodo per ottenere le key delle regole da verificare per la domanda (findom_r_bandi_regole)
		idRegolaList = getServiziFindomWeb().getRegoleTemplate(idBando);

		//ciclo sull'array delle key per ottenere la regola e sostituire dinamicamente i parametri, se presenti
		if (null != idRegolaList) {
			for (Integer idRegola : idRegolaList) {
				regola = getServiziFindomWeb().getRegola(idRegola);

				// Controllo che sia una regola BeanShell,
				// se non lo e' la salto (le regola alla creazione di una domanda vengono eseguite 
				// nel router in  CreateNewDomandaAction::verificaDomandeInviate )
				
				String corpoRegola = regola.getCorpoRegola();
				
				if(corpoRegola!=null && !corpoRegola.startsWith("//JAVARULE::it.csi.findom")){
					log.debug(logprefix + " Regola BeanShell ");
					//recupero l'array dei parametri da sostituire alla regola (shell_r_parametri_regole)
					paramRegolaBandoList = getServiziFindomWeb().getParametriRegola(regola.getCodRegola(),templateId);
					
					if (null != paramRegolaBandoList) {
						for (ParametroRegolaBandoDto parametro : paramRegolaBandoList) {
							String codParametro = parametro.getCodParametro();
							String valoreParametro = parametro.getValoreParametro();						
							corpoRegola = corpoRegola.replaceAll(codParametro, valoreParametro);
						}
					}
					//concateno le regole
					scritpBeanShell = scritpBeanShell + corpoRegola;
				}else{
					log.debug(logprefix + " Regola Java, skippo ");
				}
			}
		}
		log.info(logprefix + " scritpBeanShell caricato. dimensione="+scritpBeanShell.length());

		// invoco interprete per valutare le regole
		if (!"".equalsIgnoreCase(scritpBeanShell)) {
			log.info(logprefix + " scritpBeanShell = " + scritpBeanShell);			
			validazione = evalBeanShell(scritpBeanShell);
		}

		log.info(logprefix + " END");
		return validazione;
	}
	
	
	private boolean evalBeanShell(String scritpBeanShell) {
		final String methodName = "evalBeanShell";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.info(logprefix + " BEGIN");
		boolean validazione = false;
		Map<String, String> messages = new HashMap<String, String>();

		Interpreter i = new Interpreter();
		try {
			i.set(Constants.MESSAGES, messages);
			i.set(Constants.USER_INFO, getUserInfo());
			i.set(Constants.STATUS_INFO, getStatus()); // dichiarato static dalla BaseAction, viene inizializzato dalla stessa e vari step procedurali lo completano dei valori necessari
			//
			i.eval(scritpBeanShell);
			String msgRisultatoKO = "";
			if (messages != null && messages.size() > 0) {
				// ciclo i messaggi e li aggiungo...
				for (Map.Entry<String, String> entry : messages.entrySet()) {
					log.info(logprefix +" messaggi/o errori, key:" + entry.getKey() + ", value:" + entry.getValue());
					addActionError(entry.getValue());					
					msgRisultatoKO = msgRisultatoKO + entry.getValue() + " ";
				}
				validazione = false;
				getServletRequest().getSession().setAttribute("risultatoKO",msgRisultatoKO); 
			} else {
				validazione = true;
			}

		} catch (EvalError e) {
			log.error(logprefix +" errore validazione beanShell: " + e.getMessage());
			addActionError(Constants.ERROR_CREAZIONE_DOMANDA);
			validazione = false;
			e.printStackTrace();

		} finally {
			log.info(logprefix +" END");
		}

		return validazione;
	}

	
	// GETTERS && SETTERS
	
	public SelItem[] getListaNormativeINS() {
		return listaNormativeINS;
	}
	public void setListaNormativeINS(SelItem[] listaNormativeINS) {
		this.listaNormativeINS = listaNormativeINS;
	}
	public SelItem[] getListaBandiINS() {
		return listaBandiINS;
	}
	public void setListaBandiINS(SelItem[] listaBandiINS) {
		this.listaBandiINS = listaBandiINS;
	}
	public SelItem[] getListaSportelliINS() {
		return listaSportelliINS;
	}
	public void setListaSportelliINS(SelItem[] listaSportelliINS) {
		this.listaSportelliINS = listaSportelliINS;
	}
	public String getNormativaINS() {
		return normativaINS;
	}
	public void setNormativaINS(String normativaINS) {
		this.normativaINS = normativaINS;
	}
	public String getBandoINS() {
		return bandoINS;
	}
	public void setBandoINS(String bandoINS) {
		this.bandoINS = bandoINS;
	}
	public String getSportelloINS() {
		return sportelloINS;
	}
	public void setSportelloINS(String sportelloINS) {
		this.sportelloINS = sportelloINS;
	}
	public String getTipologiaBeneficiarioINS() {
		return tipologiaBeneficiarioINS;
	}
	public void setTipologiaBeneficiarioINS(String tipologiaBeneficiarioINS) {
		this.tipologiaBeneficiarioINS = tipologiaBeneficiarioINS;
	}
	public String getDescBreveBandoINS() {
		return descBreveBandoINS;
	}
	public void setDescBreveBandoINS(String descBreveBandoINS) {
		this.descBreveBandoINS = descBreveBandoINS;
	}
	public String getNormativa() {
		return normativa;
	}
	public void setNormativa(String normativa) {
		this.normativa = normativa;
	}
	public String getDescBreveBando() {
		return descBreveBando;
	}
	public void setDescBreveBando(String descBreveBando) {
		this.descBreveBando = descBreveBando;
	}
	public String getBando() {
		return bando;
	}
	public void setBando(String bando) {
		this.bando = bando;
	}
	public String getSportello() {
		return sportello;
	}
	public void setSportello(String sportello) {
		this.sportello = sportello;
	}
	public String getStatoDomanda() {
		return statoDomanda;
	}
	public void setStatoDomanda(String statoDomanda) {
		this.statoDomanda = statoDomanda;
	}
	public String getNumDomanda() {
		return numDomanda;
	}
	public void setNumDomanda(String numDomanda) {
		this.numDomanda = numDomanda;
	}
	public SelItem[] getListaNormative() {
		return listaNormative;
	}
	public void setListaNormative(SelItem[] listaNormative) {
		this.listaNormative = listaNormative;
	}
	public SelItem[] getListadescBreveBando() {
		return listadescBreveBando;
	}
	public void setListadescBreveBando(SelItem[] listadescBreveBando) {
		this.listadescBreveBando = listadescBreveBando;
	}
	public SelItem[] getListaBandi() {
		return listaBandi;
	}
	public void setListaBandi(SelItem[] listaBandi) {
		this.listaBandi = listaBandi;
	}
	public SelItem[] getListaSportelli() {
		return listaSportelli;
	}
	public void setListaSportelli(SelItem[] listaSportelli) {
		this.listaSportelli = listaSportelli;
	}
	public SelItem[] getListaStati() {
		return listaStati;
	}
	public void setListaStati(SelItem[] listaStati) {
		this.listaStati = listaStati;
	}
	public ArrayList<Domanda> getListaDomande() {
		return listaDomande;
	}
	public void setListaDomande(ArrayList<Domanda> listaDomande) {
		this.listaDomande = listaDomande;
	}
	public SelItem[] getListadescBreveBandoINS() {
		return listadescBreveBandoINS;
	}
	public void setListadescBreveBandoINS(SelItem[] listadescBreveBandoINS) {
		this.listadescBreveBandoINS = listadescBreveBandoINS;
	}
	public TipolBeneficiariDto[] getListaTipologieBeneficiariINS() {
		return listaTipologieBeneficiariINS;
	}
	public void setListaTipologieBeneficiariINS(
			TipolBeneficiariDto[] listaTipologieBeneficiariINS) {
		this.listaTipologieBeneficiariINS = listaTipologieBeneficiariINS;
	}
	public String getNumMaxDomandeBandoPresentate() {
		return numMaxDomandeBandoPresentate;
	}

	public void setNumMaxDomandeBandoPresentate(String numMaxDomandeBandoPresentate) {
		this.numMaxDomandeBandoPresentate = numMaxDomandeBandoPresentate;
	}

	public String getNumMaxDomandeSportelloPresentate() {
		return numMaxDomandeSportelloPresentate;
	}

	public void setNumMaxDomandeSportelloPresentate(
			String numMaxDomandeSportelloPresentate) {
		this.numMaxDomandeSportelloPresentate = numMaxDomandeSportelloPresentate;
	}
	public String getSportelloOpen() {
		return sportelloOpen;
	}

	public void setSportelloOpen(String sportelloOpen) {
		this.sportelloOpen = sportelloOpen;
	}

	public String getShowPopup() {
		return showPopup;
	}

	public void setShowPopup(String showPopup) {
		this.showPopup = showPopup;
	}
}
