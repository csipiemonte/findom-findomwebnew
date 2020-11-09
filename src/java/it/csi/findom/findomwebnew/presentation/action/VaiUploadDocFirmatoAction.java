/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDocumentoIndexDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.TipolBeneficiariDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeDto;
import it.csi.findom.findomwebnew.presentation.util.ActionUtil;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.Domanda;
import it.csi.findom.findomwebnew.presentation.vo.SelItem;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import java.util.ArrayList;
import java.util.TreeMap;

public class VaiUploadDocFirmatoAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "VaiUploadDocFirmatoAction";

	String idDomanda;
	
	// lista delle domande trovate
	ArrayList<ShellDocumentoIndexDto> listaDocFirmati;
	
	VistaDomandeDto domanda;
	
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

	@Override
	public String executeAction() throws SystemException, UnrecoverableException, CSIException {
		
		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.info(logprefix + " BEGIN");
		
		log.info(logprefix + " idDomanda="+idDomanda);
		Integer idDomInt = Integer.parseInt(idDomanda);
		
		if(StringUtils.isBlank(idDomanda)){			
			log.warn(logprefix + " idDomanda NULLO, impossibile effettuare l'upload ");
			// TODO Cambiare messaggio d'errore
			addActionMessage(Constants.UPLOAD_DOMANDA_FAILED); 
			return ERROR;
		}
		
		///////////////////////////////////////////
		// 1. carico i dati necessari della domanda
		
		// carico i dati dell'aggregatore per la domanda selezionata
		ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(idDomInt);
		if(listaAggData==null || (listaAggData!=null && listaAggData.size()!=1)){
			log.info(logprefix + " listaAggData NULLA o di dimenzione errata");			
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
			log.info(logprefix + " utente non ha i permessi per fare l'upload della domanda numero:"+idDomanda);
			// TODO Cambiare messaggio
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
		
		//se lo sportello è chiuso non si può effettuare l'upload
		String statoProposta = getStatus().getStatoProposta();
		if (statoProposta.equalsIgnoreCase(Constants.STATO_CONCLUSA)){
			boolean isOpen = ActionUtil.isSportelloOpen(aggrDomanda);
			log.info(logprefix + " isOpen="+isOpen);
			if (!isOpen){			
				log.info(logprefix + " Attenzione! Sportello chiuso");

				predisponiListe();		
				addActionError(Constants.ERR_MESSAGE_SPORTELLO_CHIUSO);
				// popola le liste			
				return ERROR;
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

		domanda = datiDomanda.get(0);
		state.setDescrNormativa(datiDomanda.get(0).getNormativa());
		state.setCodiceAzione(datiDomanda.get(0).getCodiceAzione());
		state.setDescrBando(datiDomanda.get(0).getDescrBando());
		state.setDescrBreveBando(datiDomanda.get(0).getDescrBreveBando());
		state.setDescrTipolBeneficiario(datiDomanda.get(0).getDescrizioneTipolBeneficiario());
		state.setFlagBandoDematerializzato(datiDomanda.get(0).getFlagBandoDematerializzato());
		state.setTipoFirma(datiDomanda.get(0).getTipoFirma());
		
		String codiceFiscaleLegaleRappresentante = getServiziFindomWeb().getValueFromXML(Constants.NODO_LEGALERAPPRESENTANTE, Constants.CAMPO_LEGALERAPPRESENTANTE_CF, idDomInt);
		String codiceFiscaleDelegato = getServiziFindomWeb().getValueFromXML(Constants.NODO_SOGGETTODELEGATO, Constants.CAMPO_SOGGETTODELEGATO_CF, idDomInt);
				
		state.setCodiceFiscaleDelegato(codiceFiscaleDelegato);
		state.setCodiceFiscaleLegaleRappresentante(codiceFiscaleLegaleRappresentante);

		
		setStatus(state);
		log.info(logprefix + " State aggiornato");
		
		TreeMap<String, Object> context = (TreeMap<String, Object>) this.getServletRequest().getSession().getAttribute(Constants.CONTEXT_ATTR_NAME);
		context.put(Constants.STATUS_INFO, state);
		log.info(logprefix + " valori dello stato inseriti in REQUEST");

		getServletRequest().getSession().removeAttribute("listaDocFirmati");
		// recuperiamo il documento firmato eventualmente uploadato
		// PROVVISORIO -- Da sostituire il metodo getDocumentoIndex quando sarà chiaro in base a quale criterio recuperare il file firmato
		String nomeDocFirmato = "DomandaNumero["+idDomanda+"]-["+getStatus().getCodFiscaleBeneficiario()+"].pdf.p7m";
		//ShellDocumentoIndexDto shelDoc = getServiziFindomWeb().getDocumentoIndex(idDomInt, nomeDocFirmato);
		ShellDocumentoIndexDto shelDoc = getServiziFindomWeb().getDocumentoFirmato(idDomInt);
		log.info(logprefix + " shelDoc="+shelDoc);
		
		if (shelDoc!=null){
			log.info("shelDoc idDocumentoIndex: " + shelDoc.getIdDocumentoIndex());
			log.info("shelDoc nomeFile: " + shelDoc.getNomeFile());
			// il doc firmato è già stato uploadato
			listaDocFirmati = new ArrayList<ShellDocumentoIndexDto>();			
			listaDocFirmati.add(shelDoc);
			log.info(logprefix + " settiamo in session listaDocFirmati");
			getServletRequest().getSession().setAttribute("listaDocFirmati", listaDocFirmati);
		}


		log.info(logprefix + " END");
		return SUCCESS;
	}
	
	private void predisponiListe() {
		final String methodName = "predisponiListe";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		////////////////////////////////////////
		// Combo di ricerca 
		////////////////////////////////////////

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
	
	public String getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(String idDomanda) {
		this.idDomanda = idDomanda;
	}
	
	public ArrayList<ShellDocumentoIndexDto> getListaDocFirmati() {
		return listaDocFirmati;
	}
	public void setListaDocFirmati(ArrayList<ShellDocumentoIndexDto> listaDocFirmati) {
		this.listaDocFirmati = listaDocFirmati;
	}
	
	public VistaDomandeDto getDomanda() {
		return domanda;
	}
	public void setDomanda(VistaDomandeDto domanda) {
		this.domanda = domanda;
	}
	
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
	
}
