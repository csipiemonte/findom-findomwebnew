/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.TipolBeneficiariDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.util.ActionUtil;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.Domanda;
import it.csi.findom.findomwebnew.presentation.vo.SelItem;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeMap;

public class LoadDomandaAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "LoadDomandaAction";

	String idDomanda;

	// liste del form di ricerca
	SelItem[] listaNormative;
	SelItem[] listadescBreveBando;
	SelItem[] listaBandi;
	SelItem[] listaSportelli;
	SelItem[] listaStati;
	
	// liste del form di inserimento
	SelItem[] listaNormativeINS;
	SelItem[] listadescBreveBandoINS;
	SelItem[] listaBandiINS;
	SelItem[] listaSportelliINS;
	TipolBeneficiariDto[] listaTipologieBeneficiariINS;
	
	// lista delle domande trovate
	ArrayList<Domanda> listaDomande ;
	
	// campi che vengono postati per la ricerca
	String normativa;
	String descBreveBando;
	String bando;
	String sportello;
	String statoDomanda;
	String numDomanda;
	
	String sportelloOpen; // attributo per differenziare il testo della modale di conferma invio domanda in base al fatto che lo soprtello sia aperto o meno
	String numMaxDomandeBandoPresentate;// attributo per differenziare il testo della modale di conferma invio domanda in base al fatto che l'utente abbia inviato il numero massimo di domande o meno
	String numMaxDomandeSportelloPresentate;// attributo per differenziare il testo della modale di conferma invio domanda in base al fatto che l'utente abbia inviato il numero massimo di domande o meno
	String bandoMaterializzatoSenzaPEC;// attributo per differenziare il testo della modale di conferma invio domanda per bandi non Dematerializzati che non devono inviare PEC
	
	@Override
	public String executeAction() throws SystemException, UnrecoverableException, CSIException {
		
		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.info(logprefix + " BEGIN");
		
		log.info(logprefix + " idDomanda="+idDomanda);
		Integer idDomInt = Integer.parseInt(idDomanda);
		
		if(StringUtils.isBlank(idDomanda)){
			predisponiListe(true);
			log.warn(logprefix + " idDomanda NULLO, impossibile caricare la domanda ");
			addActionMessage(Constants.ELIMINA_DOMANDA_FAILED);
			return ERROR;
		}
		
		///////////////////////////////////////////
		// 1. carico i dati necessari della domanda
		
		// carico i dati dell'aggregatore per la domanda selezionata
		ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(idDomInt);
		if(listaAggData==null || (listaAggData!=null && listaAggData.size()!=1)){
			log.info(logprefix + " listaAggData NULLA o di dimenzione errata");
			predisponiListe(true);
			addActionError(Constants.CARICA_DOMANDA_FAILED);
			return ERROR;
		}
		
		AggrDataDto aggrDomanda = listaAggData.get(0);
		
		Integer modelProg = aggrDomanda.getModelProgr();
		log.info(logprefix + " modelProg="+modelProg);
		
		String cf = getUserInfo().getCodFisc();
		log.info(logprefix + " cf=["+cf+"]");
		
		String ruolo = getUserInfo().getRuolo();
		log.info(logprefix + " ruolo="+ruolo);
		
		StatusInfo state = getStatus();
		String cfBenefic = state.getCodFiscaleBeneficiario();
		log.info(logprefix + " cfBenefic="+cfBenefic);
		
		String userId = aggrDomanda.getUserId();
		log.info(logprefix + " userId="+userId);
		log.info(logprefix + " getIdSoggettoCreatore="+aggrDomanda.getIdSoggettoCreatore());
		
		///////////////////////////////////////////
		// 2. verifico che l'utente collegato abbia i permessi per visualizzare la domanda in questione
		// utente deve essere amministratore oppure il codice fiscale del beneficiario deve corrispondere
		// al valore in aggr_t_model.userId
		
		if((Constants.RUOLO_AMM.equals(ruolo)) || (Constants.RUOLO_LR.equals(ruolo)) ||
				(StringUtils.isNotBlank(cfBenefic) && StringUtils.isNotBlank(userId) && StringUtils.equals(userId,cfBenefic))){
			// utente ha i permessi per visualizzare la domanda
			log.info(logprefix+" utente valido");
		}else{
			log.info(logprefix + " utente non ha i permessi per visualizzare la domanda numero:"+idDomanda);
			predisponiListe(true);
			addActionError(Constants.CARICA_DOMANDA_NOTALLOWED);
			return ERROR;
		}
		
		///////////////////////////////////////////
		// 3. imposto eventuali valori nello STATE
		
		state.setTemplateId(aggrDomanda.getTemplateId());
		state.setModelName(aggrDomanda.getModelName());
		state.setNumProposta(aggrDomanda.getModelId());  
		state.setStatoProposta(aggrDomanda.getModelStateFk());
		state.setNumSportello(aggrDomanda.getIdSportelloBando());
		state.setAperturaSportelloDa(aggrDomanda.getDataAperturaSportello());
		state.setAperturaSportelloA(aggrDomanda.getDataChiusuraSportello());
		
		if(StringUtils.equals(aggrDomanda.getModelStateFk(), Constants.STATO_INVIATA)){
			
			String dataTx = aggrDomanda.getDataInvioDomanda();
			log.info(logprefix + " DataInvioDomanda ="+dataTx);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			if(StringUtils.isNotBlank(dataTx)){
				try {
					state.setDataTrasmissione(df.parse(dataTx));
				} catch (ParseException e) {
					log.error(logprefix+" Errore nel parsing della data di trasmissione della domanda, dataTx=["+dataTx+"]");
				}
			}
		}
		
		
		int idSoggettoCreatore = 0;
		// recupero i dati della domanda da visualizzare nei titoli della pagina
		if(Constants.RUOLO_AMM.equals(ruolo) || Constants.RUOLO_LR.equals(ruolo)){
			idSoggettoCreatore = aggrDomanda.getIdSoggettoCreatore();
		}else{
			idSoggettoCreatore = state.getIdSoggettoCollegato();
		}
		
		int idSoggettoBeneficiario = state.getIdSoggettoBeneficiario();
		ArrayList<VistaDomandeDto> datiDomanda = getServiziFindomWeb().getVistaDomandaByCreatoreBeneficiarioDomanda(idSoggettoCreatore, idSoggettoBeneficiario, idDomInt);

		state.setDescrNormativa(datiDomanda.get(0).getNormativa());
		state.setCodiceAzione(datiDomanda.get(0).getCodiceAzione());
		state.setDescrBando(datiDomanda.get(0).getDescrBando());
		state.setDescrBreveBando(datiDomanda.get(0).getDescrBreveBando());
		state.setDescrTipolBeneficiario(datiDomanda.get(0).getDescrizioneTipolBeneficiario());
		state.setFlagBandoDematerializzato(datiDomanda.get(0).getFlagBandoDematerializzato());
		state.setTipoFirma(datiDomanda.get(0).getTipoFirma());		
		state.setFlagProgettiComuni(datiDomanda.get(0).getFlagProgettiComuni());
		
		//gestione beneficiario estero inizio
		String idStatoOperatorePresentatore = getServiziFindomWeb().getIdStatoOperatorePresentatore(idDomInt);
		if(StringUtils.isNotBlank(idStatoOperatorePresentatore)){
			state.setSiglaNazione(idStatoOperatorePresentatore);  //lo stato (estero o Italia) dell'xml della domanda, se presente, e' il valore corretto
			log.info(logprefix + " SiglaNazione = idStatoOperatorePresentatore="+idStatoOperatorePresentatore);
		}else{
			//se nell'xml non c'e' ancora lo stato (quindi l'operatore presentatore non e' ancora stato salvato), lo stato da usare e' quello di shell_t_soggetti
			state.setSiglaNazione(datiDomanda.get(0).getSiglaNazione());
			log.info(logprefix + " SiglaNazione = datiDomanda.get(0).getSiglaNazione()="+datiDomanda.get(0).getSiglaNazione());
		}
		//gestione beneficiario estero fine
		
		setStatus(state);
		log.info(logprefix + " State aggiornato");
		
		TreeMap<String, Object> context = (TreeMap<String, Object>) this.getServletRequest().getSession().getAttribute(Constants.CONTEXT_ATTR_NAME);
		context.put(Constants.STATUS_INFO, state);
		log.info(logprefix + " valori dello stato inseriti in REQUEST");
		context.put(Constants.URL_BASE_LOCATION, getUrlRouting());
		log.info(logprefix + " valore dell'url di routing inserito in CONTEXT_ATTR_NAME:" + getUrlRouting());
		
		
		try {
			getServiziFindomWeb().insertLogAudit(Constants.CSI_LOG_IDAPPL, "", 
					getUserInfo().getCodFisc()+" - "+ getUserInfo().getCognome() + " " + getUserInfo().getNome(),
					Constants.CSI_LOG_OPER_SEL_DOMANDA, "selezionata domanda numero:"+aggrDomanda.getModelId(), "");
		} catch (ServiziFindomWebException e) {
			log.error(logprefix +" impossibile scrivere CSI_LOG_AUDIT: "+e);
		}
		
		///////////////////////////////////////////
		// 4. imposto valori per l'aggregatore nella request
		
		
		// PASSO 4 - apre pagina della domanda con il nuovo modello appena salvato.
		log.info(logprefix + " imposta param per l'anagrafica ");
		
		String xformId = aggrDomanda.getTemplateCode();  			// sarebbe aggr_t_template.template_code
		String xformType = aggrDomanda.getTemplateName();  			// sarebbe aggr_t_template.template_name
		String xformDesc = aggrDomanda.getTemplateDescription();  	// sarebbe aggr_t_template.template_description
		String nomeDomanda = aggrDomanda.getModelName();  			// sarebbe aggr_t_model.model_name
		String modelState =  aggrDomanda.getModelStateFk();		 	// sarebbe aggr_t_model.model_state_fk
		// prendo la descrizione dello stato dalla Mappa presente nell'oggetto StatusInfo
		String modelStateDesc = state.getStatoPropostaMap().get(modelState);
		
		String xcommId = "";
		if((Constants.STATO_BOZZA).equals(modelState) || 
			(Constants.STATO_VALIDATAKO).equals(modelState) ||
			(Constants.STATO_VALIDATAOK).equals(modelState) ||
			(Constants.STATO_VALIDATAWO).equals(modelState) ){
			xcommId = Constants.ACCESS_FORM_RW;  // per accesso RW
		}
		if((Constants.STATO_INVIATA).equals(modelState) || (Constants.STATO_CONCLUSA).equals(modelState) || (Constants.STATO_INVALIDATA).equals(modelState)){
			xcommId = Constants.ACCESS_FORM_RO; // per accesso RO
		}
		
		
		log.info(logprefix + " xcommId="+xcommId);
		log.info(logprefix + " xformId="+xformId);
		log.info(logprefix + " xformType="+xformType);
		log.info(logprefix + " xformDesc="+xformDesc);
		log.info(logprefix + " modelProg="+modelProg);
		log.info(logprefix + " nomeDomanda="+nomeDomanda);
		log.info(logprefix + " modelState="+modelState);
		log.info(logprefix + " modelStateDesc="+modelStateDesc);
		
		
		getServletRequest().setAttribute("#xcommId", xcommId);
		getServletRequest().setAttribute("#xformId", xformId);
		getServletRequest().setAttribute("#xformType", xformType);
		getServletRequest().setAttribute("#xformDesc", xformDesc);
		getServletRequest().setAttribute("#xformProg", modelProg.toString());
		getServletRequest().setAttribute("#xformName", nomeDomanda);
		getServletRequest().setAttribute("#xformState", modelState);
		getServletRequest().setAttribute("#xdraftStr", modelStateDesc);
		
		// Tutto e' andato bene
		predisponiListe(false);
		getServletRequest().getSession().removeAttribute("listaDomande");
		
		// verifico che lo sportello sia aperto e passo il risultao alla aggregatoreUtilImpl.jsp in modo da gestire un messaggio nella modale di conferma invio
		boolean isOpen = ActionUtil.isSportelloOpen(aggrDomanda);
		log.info(logprefix + " isOpen="+isOpen);
		if(isOpen)
			setSportelloOpen("true");
		else
			setSportelloOpen("false");

		// verifico il numero di domande inviate e passo il risultato alla aggregatoreUtilImpl.jsp in modo da gestire un messaggio nella modale di conferma invio
		// questo controllo non e' bloccante 
		setNumMaxDomandeBandoPresentate("false");
		setNumMaxDomandeSportelloPresentate("false");
		setBandoMaterializzatoSenzaPEC("false");
		
		try {
			
			if (ActionUtil.isMaxNumDomandeInviatePerBando(state, getServiziFindomWeb())) {
				setNumMaxDomandeBandoPresentate("true");
				log.warn(logprefix + " il numero delle domande del bando e' >= al numero massimo consentito");
			}
			
			if (ActionUtil.isMaxNumDomandeInviatePerSportello(state, getServiziFindomWeb())) {
				setNumMaxDomandeSportelloPresentate("true");
				log.warn(logprefix + " il numero delle domande dello sportello e' >= al numero massimo consentito");
			}
			
			String flagBandoDematerializzato = state.getFlagBandoDematerializzato();
			if(flagBandoDematerializzato!=null){
				flagBandoDematerializzato = flagBandoDematerializzato.toUpperCase();
			}
			log.info(logprefix + " flagBandoDematerializzato: " + flagBandoDematerializzato);
			
			Boolean usoIndex = (Boolean) getServletRequest().getSession().getAttribute("flagUploadIndex_bando"+state.getTemplateId());
			if(usoIndex == null){
				FindomTBandiDto band = getServiziFindomWeb().findoOneFindomTBandi(new Long(state.getTemplateId()));
				log.info(logprefix + "bando="+state.getTemplateId()+", file su Index="+band.getFlagUploadIndex());
				getServletRequest().getSession().setAttribute("flagUploadIndex_bando" + state.getTemplateId(), band.getFlagUploadIndex());
				usoIndex = band.getFlagUploadIndex();
			}else{
				log.info(logprefix + "leggo da sessione, bando=" + state.getTemplateId() + ", file su Index="+usoIndex);
			}
			
			if (!StringUtils.equals(Constants.FLAG_BANDO_DEMATERIALIZZATO, flagBandoDematerializzato) && !usoIndex){
				log.info(logprefix + "imposto setBandoMaterializzatoSenzaPEC = true");
				setBandoMaterializzatoSenzaPEC("true");
			}
			
		} catch (ServiziFindomWebException e) {
			log.error(logprefix +" errore nella verifica del numero di domande inviate "+e);
			addActionError(Constants.CARICA_DOMANDA_FAILED);
			return ERROR;
		}
				
		log.info(logprefix + " END");
		return SUCCESS;
	}
	
	/**
	 * Popolo le combo box del form di ricerca e del form di creazione
	 *  
	 * @param tutte : true : prendo tutte le liste dalla request
	 * 				  false : prendo solo le liste del form di inserimento dalla request, annullo quelle del form di ricerca 
	 * @return
	 */
	private void predisponiListe(boolean tutte) {
		final String methodName = "predisponiListe";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		
		////////////////////////////////////////
		// Combo di ricerca 
		////////////////////////////////////////
		if(tutte){ 
			// c'e' stato un errore, ricarico tutto com'era
			log.info(logprefix + "listaNormative presa da request="+getServletRequest().getSession().getAttribute("listaNormative"));
			log.info(logprefix + "listadescBreveBando presa da request="+getServletRequest().getSession().getAttribute("listadescBreveBando"));
			log.info(logprefix + "listaBandi presa da request="+getServletRequest().getSession().getAttribute("listaBandi"));
			log.info(logprefix + "listaSportelli presa da request="+getServletRequest().getSession().getAttribute("listaSportelli"));
			log.info(logprefix + "listaStati presa da request="+getServletRequest().getSession().getAttribute("listaStati"));
			
			// popolo le combo del form di inserimento
			listaNormative = (SelItem[]) getServletRequest().getSession().getAttribute("listaNormative");
			listadescBreveBando = (SelItem[]) getServletRequest().getSession().getAttribute("listadescBreveBando");
			listaBandi = (SelItem[]) getServletRequest().getSession().getAttribute("listaBandi");
			listaSportelli = (SelItem[]) getServletRequest().getSession().getAttribute("listaSportelli");
			listaStati = (SelItem[]) getServletRequest().getSession().getAttribute("listaStati");
			log.info(logprefix + " ricaricate liste delle combo del form di ricerca dalla sessione");
			
			listaDomande = (ArrayList<Domanda>) getServletRequest().getSession().getAttribute("listaDomande");
			log.info(logprefix + " ricaricata lista delle domande trovate dalla sessione");
			
			//  ricaricavo anche i valori del form postati...
			normativa = (String)getServletRequest().getSession().getAttribute("normativa");
			descBreveBando = (String)getServletRequest().getSession().getAttribute("descBreveBando");
			bando = (String)getServletRequest().getSession().getAttribute("bando");
			sportello = (String)getServletRequest().getSession().getAttribute("sportello");
			statoDomanda = (String)getServletRequest().getSession().getAttribute("statoDomanda");
			numDomanda = (String)getServletRequest().getSession().getAttribute("numDomanda");
			log.info(logprefix + " ricaricati parametri del form di ricerca dalla sessione");
			
		}else{
			
			// non ho avuto errori, svuoto le liste del form di ricerca, forzandone il ricaricamento nella CercaDomandeAction
			// popolo le combo del form di inserimento
			listaNormative = (SelItem[]) getServletRequest().getSession().getAttribute("listaNormative");
			listadescBreveBando = (SelItem[]) getServletRequest().getSession().getAttribute("listadescBreveBando");
			listaBandi = (SelItem[]) getServletRequest().getSession().getAttribute("listaBandi");
			listaSportelli = (SelItem[]) getServletRequest().getSession().getAttribute("listaSportelli");
			listaStati = (SelItem[]) getServletRequest().getSession().getAttribute("listaStati");
			log.info(logprefix + " ricaricate liste delle combo del form di ricerca dalla sessione");

			setListaDomande(null);
			log.info(logprefix + " eliminata lista delle domande trovate dalla sessione");
			
		}
		
		////////////////////////////////////////
		// Combo di inserimento
		// le prendo dalla request.
		////////////////////////////////////////
		
		log.info(logprefix + "listaNormativeINS presa da request="+getServletRequest().getSession().getAttribute("listaNormativeINS"));
		log.info(logprefix + "listaBandiINS presa da request="+getServletRequest().getSession().getAttribute("listaBandiINS"));
		log.info(logprefix + "listadescBreveBandoINS presa da request="+getServletRequest().getSession().getAttribute("listadescBreveBandoINS"));
		log.info(logprefix + "listaSportelliINS presa da request="+getServletRequest().getSession().getAttribute("listaSportelliINS"));
		log.info(logprefix + "listaTipologieBeneficiariINS presa da request="+getServletRequest().getSession().getAttribute("listaTipologieBeneficiariINS"));
		
		// popolo le combo del form di inserimento
		listaNormativeINS = (SelItem[]) getServletRequest().getSession().getAttribute("listaNormativeINS");
		listadescBreveBandoINS = (SelItem[]) getServletRequest().getSession().getAttribute("listadescBreveBandoINS");
		listaBandiINS = (SelItem[]) getServletRequest().getSession().getAttribute("listaBandiINS");
		listaSportelliINS = (SelItem[]) getServletRequest().getSession().getAttribute("listaSportelliINS");
		listaTipologieBeneficiariINS = (TipolBeneficiariDto[]) getServletRequest().getSession().getAttribute("listaTipologieBeneficiariINS");
		
		log.info(logprefix + " ricaricate liste delle combo del form di inserimento dalla sessione");
		log.info(logprefix + " END");
	}

	// GETTERS && SETTERS
	
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

	public SelItem[] getListaNormativeINS() {
		return listaNormativeINS;
	}

	public void setListaNormativeINS(SelItem[] listaNormativeINS) {
		this.listaNormativeINS = listaNormativeINS;
	}

	public SelItem[] getListadescBreveBandoINS() {
		return listadescBreveBandoINS;
	}

	public void setListadescBreveBandoINS(SelItem[] listadescBreveBandoINS) {
		this.listadescBreveBandoINS = listadescBreveBandoINS;
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

	public TipolBeneficiariDto[] getListaTipologieBeneficiariINS() {
		return listaTipologieBeneficiariINS;
	}

	public void setListaTipologieBeneficiariINS(
			TipolBeneficiariDto[] listaTipologieBeneficiariINS) {
		this.listaTipologieBeneficiariINS = listaTipologieBeneficiariINS;
	}

	public String getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(String idDomanda) {
		this.idDomanda = idDomanda;
	}

	public ArrayList<Domanda> getListaDomande() {
		return listaDomande;
	}

	public void setListaDomande(ArrayList<Domanda> listaDomande) {
		this.listaDomande = listaDomande;
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

	public String getSportelloOpen() {
		return sportelloOpen;
	}

	public void setSportelloOpen(String sportelloOpen) {
		this.sportelloOpen = sportelloOpen;
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

	public String getBandoMaterializzatoSenzaPEC() {
		return bandoMaterializzatoSenzaPEC;
	}

	public void setBandoMaterializzatoSenzaPEC(String bandoMaterializzatoSenzaPEC) {
		this.bandoMaterializzatoSenzaPEC = bandoMaterializzatoSenzaPEC;
	}
	
}
