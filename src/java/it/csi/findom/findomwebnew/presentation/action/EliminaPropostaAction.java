/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.findom.findomwebnew.business.helper.IndexHelper;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDocumentoIndexDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.TipolBeneficiariDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.Domanda;
import it.csi.findom.findomwebnew.presentation.vo.SelItem;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.csi.melograno.aggregatore.business.AggregatoreFactory;
import it.doqui.index.ecmengine.client.webservices.exception.InvalidParameterException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.EcmEngineTransactionException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.InvalidCredentialsException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoSuchNodeException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.PermissionDeniedException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.DeleteException;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class EliminaPropostaAction  extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "EliminaPropostaAction";	
	private IndexHelper indexHelper; 
	
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
	
	@Override
	public String executeAction() throws SystemException, UnrecoverableException, CSIException {
		
		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		log.debug(logprefix + " idDomanda="+idDomanda);
		
		if(StringUtils.isBlank(idDomanda)){
			log.warn(logprefix + " idDomanda NULLO, impossibile effettuare la cancellazione della domanda ");
			setRisultatoKO(Constants.ELIMINA_DOMANDA_FAILED);
			return ERROR;
		}
		
		Integer idDomInt = Integer.parseInt(idDomanda);

		// 0. carico i dati dell'aggregatore per la domanda selezionata
		ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(idDomInt);
		if(listaAggData==null || (listaAggData!=null && listaAggData.size()!=1)){
			log.debug(logprefix + " listaAggData NULLA o di dimenzione errata");
			setRisultatoKO(Constants.ELIMINA_DOMANDA_FAILED);
			return ERROR;
		}
		
		AggrDataDto aggrDomanda = listaAggData.get(0);
		
		Integer modelProg = aggrDomanda.getModelProgr();
		log.debug(logprefix + " modelProg="+modelProg);

		// 1. verifico che la domanda in questione sia cancellabile (stato BZ, OK, KO, OW)
		String statoDomanda = aggrDomanda.getModelStateFk();
		log.debug(logprefix + " statoDomanda="+statoDomanda);
		
		if(StringUtils.equals(Constants.STATO_INVIATA, statoDomanda)){
			log.warn(logprefix + " impossibile cancellare la domanda, stato = "+statoDomanda);
			setRisultatoKO(Constants.ELIMINA_DOMANDA_FAILED_4STATE);
			return ERROR;
		}
		
		// 2. verifico che l'utente collegato abbia i permessi per cancellare la domanda in questione.
		// Utente deve essere amministratore oppure il codice fiscale del beneficiario deve corrispondere
		// al valore in aggr_t_model.userId
		
		String ruolo = getUserInfo().getRuolo();
		log.debug(logprefix + " ruolo="+ruolo);
		
		StatusInfo state = getStatus();
		log.debug(logprefix + " state="+state);
		
		String cfBenefic = state.getCodFiscaleBeneficiario();
		log.debug(logprefix + " CodFiscaleBeneficiario="+cfBenefic);
		log.debug(logprefix + " IdSoggettoBeneficiario="+state.getIdSoggettoBeneficiario());
		
		//  se l'utente non ha aperto la domanda da cancellare lo status non contiene i dati del beneficiario
		if(cfBenefic==null || state.getIdSoggettoBeneficiario()==null){
		
			Integer idSoggettoBeneficiario = aggrDomanda.getIdSoggettoBeneficiario();
			getStatus().setIdSoggettoBeneficiario(idSoggettoBeneficiario);
			
			log.debug(logprefix + " IdSoggettoBeneficiario ricaricato="+idSoggettoBeneficiario);
			
			if (cfBenefic == null) {
				ArrayList<String> listaId = new ArrayList<String>();
				listaId.add(idSoggettoBeneficiario.toString());
				ArrayList<ShellSoggettiDto> listaSoggetti = getServiziFindomWeb().getDatiSoggettoByIdSoggetto(listaId);
				if (listaSoggetti.size()>0 && listaSoggetti.get(0)!=null) {
					cfBenefic = listaSoggetti.get(0).getCodiceFiscale();
					getStatus().setCodFiscaleBeneficiario(cfBenefic);
				}
			}
			log.debug(logprefix + " CodFiscaleBeneficiario ricaricato ="+cfBenefic);
		}
		
		String userId = aggrDomanda.getUserId();
		log.debug(logprefix + " userId="+userId);
		
		if((Constants.RUOLO_AMM.equals(ruolo)) || (Constants.RUOLO_LR.equals(ruolo)) ||
				(StringUtils.isNotBlank(cfBenefic) && StringUtils.isNotBlank(userId) && StringUtils.equals(userId,cfBenefic))){
			// utente ha i permessi per visualizzare la domanda
			log.debug(logprefix+" utente valido");
		}else{
			log.debug(logprefix + " utente non ha i permessi per eliminare la domanda numero:"+idDomanda);
			setRisultatoKO(Constants.ELIMINA_DOMANDA_NOTALLOWED);
			return ERROR;
		}
		
		//in vista della successiva cancellazione degli eventuali allegati su Index salvo in docList i dati degli allegati, prima che vengano cancellati in deleteDomanda()
		ArrayList<ShellDocumentoIndexDto> docList = getServiziFindomWeb().getAllDocumentoIndex(idDomInt);
		
		// 2. elimino domanda da tabella SHELL_T_DOMANDE e dalla AGGR_T_MODEL (ed elimino gli eventuali allegati su shell_t_documento_index nel metodo transazionale deleteDomanda)
		Integer esito = null;
		try {
			esito = getServiziFindomWeb().deleteDomanda(idDomInt, userId, aggrDomanda.getTemplateCode(), aggrDomanda.getModelProgr(), AggregatoreFactory.create() );
			log.info(logprefix + "cancellazione avvenuta con successo, esito="+esito);

		} catch (Throwable e) {
			log.warn(logprefix + " impossibile eliminare la domanda dalla shell_t_domande: "+e);
			setRisultatoKO(Constants.ELIMINA_DOMANDA_FAILED);
			return ERROR;
		}
		
		//Se la cancellazione sul db di findom e' andata bene cancello gli eventuali allegati presenti su Index fuori dalla transazione (tanto il rollback su index 
		//non verrebbe fatto). In questo modo:
		//1) se la cencellazione sul db di findom va male scatta il rollback, e la cancellazione su Index che viene fatta qua sotto non viene neanche iniziata  
		//2) se la cancellazione sul db di findom va a buon fine e la cancellazione su Index va male, il risultato e' che ci saranno dei documenti su 
		//   Index senza la corrispondente domanda su findom
		log.info(logprefix + " inizio gestione cancellazione su index degli allegati ");
		boolean eliminazioneAllegatiOK = false;
		try{
			if(docList!=null && !docList.isEmpty()){
				
				// verifico se il devo leggere il documento da Index
				Boolean usoIndex = (Boolean) getServletRequest().getSession().getAttribute("flagUploadIndex_bando"+state.getTemplateId());
				if(usoIndex == null){
					FindomTBandiDto band = getServiziFindomWeb().findoOneFindomTBandi(new Long(state.getTemplateId()));
					log.info(logprefix + "bando="+state.getTemplateId()+", file su Index="+band.getFlagUploadIndex());
					getServletRequest().getSession().setAttribute("flagUploadIndex_bando"+state.getTemplateId(),band.getFlagUploadIndex());
					usoIndex = band.getFlagUploadIndex();
				}else{
					log.info(logprefix + "leggo da sessione, bando="+state.getTemplateId()+", file su Index="+usoIndex);
				}
				
				if(usoIndex){
					for (int i = 0; i < docList.size(); i++) {
						// ciclo sui doc trovati nella shell_t_documento_index
						ShellDocumentoIndexDto curDoc = (ShellDocumentoIndexDto) docList.get(i);
						if(curDoc==null){
							continue;
						}					
						int idAllegato = curDoc.getIdAllegato(); // numero dell'allegato da cancellare
						String uuidDoc = curDoc.getUuidNodo();
						log.info(logprefix + " curDoc = "+idAllegato+ ", uuid=["+uuidDoc+"]");
	
						// elimino il doc da Index	
						if(StringUtils.isNotBlank(uuidDoc)){
							// uuid NON nullo, quindi esiste doc su Index;  rimuovo il documento su Index
							log.info(logprefix + " sto per cancellare su Index il documento con idAllegato = "+idAllegato+ " e uuid=["+uuidDoc+"]");						
							indexHelper.deleteNode(uuidDoc);
							log.info(logprefix + " cancellazione effettuata su Index del documento con idAllegato = "+idAllegato+ " e uuid=["+uuidDoc+"]");						
						}
					}
				}else{
					log.info(logprefix + " non utilizzo index");
				}
			}
			eliminazioneAllegatiOK = true;
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + "InvalidCredentialsException: "+e.getMessage());
		} catch (DeleteException e) {
			log.error(logprefix + "DeleteException: "+e.getMessage());
		} catch (NoSuchNodeException e) {
			log.error(logprefix + "NoSuchNodeException: "+e.getMessage());
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + "EcmEngineTransactionException: "+e.getMessage());
		} catch (PermissionDeniedException e) {
			log.error(logprefix + "PermissionDeniedException: "+e.getMessage());
		} catch (InvalidParameterException e) {
			log.error(logprefix + "InvalidParameterException: "+e.getMessage());
		} catch (RemoteException e) {
			log.error(logprefix + "RemoteException: "+e.getMessage());
		} catch (Exception e) {
			log.error(logprefix + "Exception: "+e.getMessage());
		} 

        if(!eliminazioneAllegatiOK){
        	log.info(logprefix + " si e' verificato un errore durante la cancellazione degli allegati su Index della domanda :"+idDomanda);
			setRisultatoKO(Constants.ELIMINA_DOMANDA_ALLEGATI_INDEX_FAILED);
			return ERROR;
        }
        log.info(logprefix + " fine gestione cancellazione su index degli allegati ");

		try {
			getServiziFindomWeb().insertLogAudit(Constants.CSI_LOG_IDAPPL, "", 
					getUserInfo().getCodFisc()+" - "+ getUserInfo().getCognome() + " " + getUserInfo().getNome(),
					Constants.CSI_LOG_OPER_DELETE_DOMANDA, "eliminata domanda numero:"+idDomInt, "");
		} catch (ServiziFindomWebException e) {
			log.error(logprefix +" impossibile scrivere CSI_LOG_AUDIT: "+e);
		}
		
		getServletRequest().getSession().removeAttribute("listaDomande");
		
		// le combo di inserimento sono in getServletRequest() e non vanno modificate
		// le combo di ricerca devono essere svuotate per forzarne il ricaricamento nella Action "cercaDomande"
		predisponiListe();
		
		log.debug(logprefix + " END");
		setRisultatoOK(Constants.ELIMINA_DOMANDA);
		return SUCCESS;
	}
	
	/**
	 * Popolo le combo box del form di ricerca e del form di creazione
	 * 
	 * @return
	 */
	private void predisponiListe() {
		final String methodName = "predisponiListe";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		
		/////////////////////////////////////////
		// Combo di ricerca 
		// svuoto le liste delle combo forzandone il ricaricamento nella Action "cercaDomande"
		/////////////////////////////////////////
		
		// popolo le combo del form di inserimento
		listaNormative = (SelItem[]) getServletRequest().getSession().getAttribute("listaNormative");
		listadescBreveBando = (SelItem[]) getServletRequest().getSession().getAttribute("listadescBreveBando");
		listaBandi = (SelItem[]) getServletRequest().getSession().getAttribute("listaBandi");
		listaSportelli = (SelItem[]) getServletRequest().getSession().getAttribute("listaSportelli");
		listaStati = (SelItem[]) getServletRequest().getSession().getAttribute("listaStati");
		log.debug(logprefix + " ricaricate liste delle combo del form di ricerca dalla sessione");

		setListaDomande(null);
		log.debug(logprefix + " eliminata lista delle domande trovate dalla sessione");
		
		
		/////////////////////////////////////////
		// Combo di inserimento
		// le prendo dalla getServletRequest().
		/////////////////////////////////////////
		
		log.debug(logprefix + "listaNormativeINS presa da request="+getServletRequest().getSession().getAttribute("listaNormativeINS"));
		log.debug(logprefix + "listaBandiINS presa da request="+getServletRequest().getSession().getAttribute("listaBandiINS"));
		log.debug(logprefix + "listadescBreveBandoINS presa da request="+getServletRequest().getSession().getAttribute("listadescBreveBandoINS"));
		log.debug(logprefix + "listaSportelliINS presa da request="+getServletRequest().getSession().getAttribute("listaSportelliINS"));
		log.debug(logprefix + "listaTipologieBeneficiariINS presa da request="+getServletRequest().getSession().getAttribute("listaTipologieBeneficiariINS"));
		
		// popolo le combo del form di inserimento
		listaNormativeINS = (SelItem[]) getServletRequest().getSession().getAttribute("listaNormativeINS");
		listadescBreveBandoINS = (SelItem[]) getServletRequest().getSession().getAttribute("listadescBreveBandoINS");
		listaBandiINS = (SelItem[]) getServletRequest().getSession().getAttribute("listaBandiINS");
		listaSportelliINS = (SelItem[]) getServletRequest().getSession().getAttribute("listaSportelliINS");
		listaTipologieBeneficiariINS = (TipolBeneficiariDto[]) getServletRequest().getSession().getAttribute("listaTipologieBeneficiariINS");
		
		log.debug(logprefix + " ricaricate liste delle combo del form di inserimento dalla sessione");
		
		log.debug(logprefix + " END");
	}

	
	public void setRisultatoKO(String risultato) {
		getServletRequest().getSession().setAttribute("risultatoKO", risultato);
	}

	public void setRisultatoOK(String risultato) {
		getServletRequest().getSession().setAttribute("risultatoOK", risultato);
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
	public IndexHelper getIndexHelper() {
		return indexHelper;
	}
	public void setIndexHelper(IndexHelper indexHelper) {
		this.indexHelper = indexHelper;
	}
}
