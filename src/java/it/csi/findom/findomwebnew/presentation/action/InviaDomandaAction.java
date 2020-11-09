/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.findom.findomwebnew.business.helper.IndexHelper;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.TipolBeneficiariDto;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.Domanda;
import it.csi.findom.findomwebnew.presentation.vo.SelItem;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.csi.melograno.aggregatore.business.AggregatoreFactory;
import it.csi.melograno.aggregatore.business.DataModel;
import it.csi.melograno.aggregatore.exception.AggregatoreException;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;


public class InviaDomandaAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "InviaDomandaAction";

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
	
	private IndexHelper indexHelper;
	
	// Pochettino 14/9/2018 date ufficiali da usare dove serve
	Date dataTrasmissioneUfficiale = null;
	
	@Override
	
	public String executeAction() throws Throwable {

		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		dataTrasmissioneUfficiale = new Date();
		log.info(logprefix + " dataTrasmissioneUfficiale="+dataTrasmissioneUfficiale);
		
		// l'id della domanda non arriva dalla request perche' passa attraverso una modale di "conferma invio proposta"
		// lo devo ricavare dallo statusInfo
		
		StatusInfo state = getStatus(); 
		Integer idDomanda = state.getNumProposta();
		log.info(logprefix + " idDomanda="+idDomanda);
		log.info(logprefix + " state.getTemplateId()="+state.getTemplateId());
		log.info(logprefix + " state.getFlagBandoDematerializzato()="+state.getFlagBandoDematerializzato());
		log.info(logprefix + " state.getTipoFirma()="+state.getTipoFirma());
		
		try {
	
			// se il bando e' dematerializzato non dobbiamo fare 
			// l'aggiornamento dei dati di trasmissione in questo momento
			// aggiorneremo i dati di trasmissione al momneto del cambio stato in INVIATA
			String flagBandoDematerializzato = state.getFlagBandoDematerializzato();
			log.info(logprefix + " flagBandoDematerializzato: " + flagBandoDematerializzato);
			
			if(idDomanda == null){
				log.warn(logprefix + " idDomanda NULLO, impossibile inviare la domanda ");
				if (Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(flagBandoDematerializzato)){				
					setRisultatoKO(Constants.CONCLUSIONE_DOMANDA_FAILED);
				} else {
					setRisultatoKO(Constants.INVIO_DOMANDA_FAILED);
				}
				return ERROR;
				
			}
			
			///////////////////////////////////////////
			// 1. carico i dati necessari della domanda
			
			// carico i dati dell'aggregatore per la domanda selezionata
			ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(idDomanda);
			
			if(listaAggData==null || (listaAggData!=null && listaAggData.size()!=1)){
				log.warn(logprefix + " listaAggData NULLA o di dimenzione errata");
				setRisultatoKO(Constants.CARICA_DOMANDA_FAILED);
				return ERROR;
			}
			log.info(logprefix + " caricata listaAggData");
					
			AggrDataDto aggrDomanda = listaAggData.get(0);
			log.info(logprefix + " caricata aggrDomanda");
			
			Integer modelProg = aggrDomanda.getModelProgr();
			log.info(logprefix + " modelProg="+modelProg);
			
			// templateCode
			String templateCode = aggrDomanda.getTemplateCode();
			log.info(logprefix + " templateCode="+templateCode);
			
			int templateId = aggrDomanda.getTemplateId();
			log.info(logprefix + " templateId="+templateId);
			
			String userId = aggrDomanda.getUserId(); 
			log.info(logprefix + " userId="+userId);
			
			Integer modelId = aggrDomanda.getModelId(); 
			log.info(logprefix + " modelId="+modelId);
			
			String cf = getUserInfo().getCodFisc();
			log.info(logprefix + " cf=["+cf+"]");
			
			String ruolo = getUserInfo().getRuolo();
			log.info(logprefix + " ruolo="+ruolo);
			
			String cfBenefic = state.getCodFiscaleBeneficiario(); // presente denominazione
			log.info(logprefix + " cfBenefic="+cfBenefic);
			
			///////////////////////////////////////////
			// 2. verifico che l'utente collegato abbia i permessi per inviare la domanda in questione
			// utente deve essere amministratore oppure il codice fiscale del beneficiario deve corrispondere
			// al valore in aggr_t_model.userId
			
			if((Constants.RUOLO_AMM.equals(ruolo)) || (Constants.RUOLO_LR.equals(ruolo)) ||
					(StringUtils.isNotBlank(cfBenefic) && StringUtils.isNotBlank(userId) && StringUtils.equals(userId,cfBenefic))){
				// utente ha i permessi per visualizzare la domanda
				log.info(logprefix+" utente valido");
			}else{
				log.warn(logprefix + " utente non ha i permessi per visualizzare la domanda numero:"+idDomanda);
				setRisultatoKO(Constants.INVIO_DOMANDA_NOTALLOWED);
				return ERROR;
			}
	

			///////////////////////////////////////////
			// 4. aggiorno nel XML la dataInvioDOmanda o la data di conclusione della domanda, lo stato della stampa e .....
			
			//Pochettino 14/9/2018 : passo la data da usare altrimenti prende quella del DB server che potrebbe non essere allineata con l'application server
			boolean aggOK = aggiornaDatiTrasmissione(aggrDomanda, userId, flagBandoDematerializzato, dataTrasmissioneUfficiale);
			log.info(logprefix + " aggOK="+aggOK);
			if(!aggOK){
				log.warn(logprefix + " aggiornamento dati nell'XML fallito. ");
				if (Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(flagBandoDematerializzato)){				
					addActionError(Constants.CONCLUSIONE_DOMANDA_FAILED);
				} else {
					addActionError(Constants.INVIO_DOMANDA_FAILED);
				}
				return ERROR;
			}

			///////////////////////////////////////////
			// 5. genero il file PDF definitivo
			String draftStr = "";
			ByteArrayOutputStream pdf = new ByteArrayOutputStream();
			try {
	
				AggregatoreFactory.create().printPDF(userId, templateCode, modelProg, draftStr, pdf);
				log.info(logprefix + " generato il file PDF definitivo");
				
			} catch (AggregatoreException e) {
				//  se qui si e' rotto dovrei fare il roll-back dei dati messi nell'XML
				log.error(logprefix + " creazione PDF fallita. "+e);
				if (Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(flagBandoDematerializzato)){				
					setRisultatoKO(Constants.CONCLUSIONE_DOMANDA_FAILED);
				} else {
					setRisultatoKO(Constants.INVIO_DOMANDA_FAILED);
				}
				return ERROR;
			}
			
			///////////////////////////////////////////
			// 6. salvo sul db il file PDF
			String xmlDomanda =  getServiziFindomWeb().getXMLDomanda(modelId);
			log.info(logprefix + " xmlDomanda caricato="+xmlDomanda);
			
			byte[] byteArrayPdf = pdf.toByteArray();
			if (byteArrayPdf != null && byteArrayPdf.length > 0) {
				
				// Message-digest
				String messageDigest = indexHelper.creaImpronta(byteArrayPdf);
				
				log.info(logprefix + " messageDigest=" + messageDigest);
				Integer res = getServiziFindomWeb().insertTFileDomanda(modelId, xmlDomanda, byteArrayPdf, messageDigest);
				log.info(logprefix + " res="+res);
				
			}else{
				
				log.error(logprefix + "  PDF nullo per la domanda "+modelId);
				if (Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(flagBandoDematerializzato)){				
					setRisultatoKO(Constants.CONCLUSIONE_DOMANDA_FAILED);
				} else {
					setRisultatoKO(Constants.INVIO_DOMANDA_FAILED);
				}
				return ERROR;
			}
		
			
			///////////////////////////////////////////
			// 7. cambio lo stato della domanda in INVIATO
			String statoDom = Constants.STATO_INVIATA;
			String msgInvio = Constants.INVIO_DOMANDA;
			
			String msgInvioLOG = "Inviata domanda numero=";
					
			// se il bando e' dematerializzato lo stato della domanda diventa CONCLUSA e non INVIATA
			if (Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(flagBandoDematerializzato)){
				statoDom = Constants.STATO_CONCLUSA;
				log.info(logprefix + "tipo firma = "+state.getTipoFirma());
				if(Constants.FIRMA_OLOGRAFA.equalsIgnoreCase(state.getTipoFirma())){
					msgInvio = Constants.CONCLUSIONE_DOMANDA_OLOGRAFA;
				}else{
					msgInvio = Constants.CONCLUSIONE_DOMANDA;
				}

				msgInvioLOG = "Conclusa domanda numero=";

			} else {

				// per i bandi non Dematerializzati
				Boolean usoIndex = (Boolean) getServletRequest().getSession().getAttribute("flagUploadIndex_bando"+templateId);
				if(usoIndex == null){
					FindomTBandiDto band = getServiziFindomWeb().findoOneFindomTBandi(new Long(templateId));
					log.info(logprefix + "bando="+templateId+", file su Index="+band.getFlagUploadIndex());
					getServletRequest().getSession().setAttribute("flagUploadIndex_bando" + templateId, band.getFlagUploadIndex());
					usoIndex = band.getFlagUploadIndex();
				}else{
					log.info(logprefix + "leggo da sessione, bando=" + templateId + ", file su Index="+usoIndex);
				}

				if (!usoIndex){
					log.info(logprefix + "impostato messaggio per bandi non dematerializzati senza PEC");
					msgInvio = Constants.INVIO_DOMANDA_MAT;
				}
			}
				
			// Questo changeState ha volte fallisce..... Sostituirlo con una UPDATE secca				
			Integer idUpd = getServiziFindomWeb().updateStatoDomanda(idDomanda+"", statoDom);
			log.info(logprefix + "cambiato stato idDomanda="+idDomanda+", nuovo stato = "+statoDom);					

			state.setStatoProposta(statoDom);

			if (state.getTemplateId() == null) {
				state.setTemplateId(aggrDomanda.getTemplateId());
			}

			//verifico se il bando prevede l'istruttoria su findom e se si ottengo il codice stato istruttoria da mettere su shell_t_domande
			Integer idStatoIstruttoriaRI = getIdStatoIstruttoria(state.getTemplateId(),Constants.CODICE_STATO_ISTRUTTORIA_DA_PRENDERE_IN_CARICO);		
		
			
			///////////////////////////////////////////
			// 8. aggiorno shell_t_domande impostando la data invio e l'id soggetto invio
			// sulla shell_t_domande verrano aggiornate dt_invio e soggetto_invio oppure dt_conclusione e id_soggetto_conclusione 
			// a seconda dello stato della domanda
			
			//Pochettino 14/9/2018 : gli passo le date/ore da usare altrimenti prende quelle del DB server che potrebbero non essere allineate con l'application server
			Integer val = getServiziFindomWeb().updateShellDomande(idDomanda, state.getIdSoggettoCollegato(), statoDom,idStatoIstruttoriaRI, dataTrasmissioneUfficiale ); 
			log.info(logprefix + " aggiornato data invio");


			///////////////////////////////////////////
			// 9 aggiorno l'oggetto satus in sessione 
			state.setDataTrasmissione(dataTrasmissioneUfficiale);
			state.setNumProposta(aggrDomanda.getModelId());
			setStatus(state); 

			// salva data e ora nello statusInfo nel contesto per visualizzazione a fianco del bottone Invia
			@SuppressWarnings("unchecked")
			TreeMap<String, Object> context = (TreeMap<String, Object>) this.getServletRequest().getSession().getAttribute(Constants.CONTEXT_ATTR_NAME);
			context.put(Constants.STATUS_INFO, state);
			log.info(logprefix + " valori dello stato aggiornati.....");

			
			///////////////////////////////////////////
			// 10 Scheda progetto - richiamo procedura di ribaltamento xml in fase di invio domanda
			// Controllo se il bando prevede la sezione "Scheda Progetto" e se prevede istruttoria esterna
			
			FindomTBandiDto pbandi = getServiziFindomWeb().findoOneFindomTBandi(new Long(templateId));
			
			if(pbandi!=null){
				log.info(logprefix + " getflagSchedaProgetto="+pbandi.getFlagSchedaProgetto());
				log.info(logprefix + " getFlagIstruttoriaEsterna="+pbandi.getFlagIstruttoriaEsterna());
			}
			if(pbandi!=null 
					&& pbandi.getFlagSchedaProgetto() 
					&& !StringUtils.equals("S", pbandi.getFlagIstruttoriaEsterna())
					&& !Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(flagBandoDematerializzato)	
					){
				// Invoco la procedura fnc_crea_scheda_progetto che travasa i dati dell'XML _criteri nelle tabella shell_t_valut_domande
				String creaSP = getServiziFindomWeb().callProcedureCreaSchedaProgetto(idDomanda);
				log.info(logprefix + " creaSP="+creaSP);
				
				if(!StringUtils.equals("0", creaSP)){
					log.error(logprefix + " La function 'fnc_crea_scheda_progetto' ha sollevato la seguente eccezione: "+creaSP);
				}
			}
			
			// copiamo la domanda INVIATA dalla aggr_t_model alla istr_t_model 
			// e i record sulla aggr_t_model_index sulla istr_t_model_index
			getServiziFindomWeb().copiaDomandaPerIstruttoria(idDomanda);

			predisponiListe(false);

			log.info(logprefix + " END");
			setRisultatoOK(msgInvio);

			getServiziFindomWeb().insertLogAudit(Constants.CSI_LOG_IDAPPL, "", 
					getUserInfo().getCodFisc()+" - "+ getUserInfo().getCognome() + " " + getUserInfo().getNome(), 
					Constants.CSI_LOG_OPER_INVIO_DOMANDA, msgInvioLOG + idDomanda, "");

			return SUCCESS;

		}catch(Exception e){
			
			log.error(logprefix+"Errore Invio domanda numero="+idDomanda);
			throw new Throwable(e);
			
		}

	}


	private boolean aggiornaDatiTrasmissione(AggrDataDto aggrDomanda, String userId, String flagBandoDematerializzato, Date dataTxUfficiale) throws Throwable {
		final String methodName = "aggiornaDatiTrasmissione";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		boolean res = true;
		
		DataModel mod;
		try {
			
			log.info(logprefix + " userId="+userId);
			
			// templateCode
			String xformId = aggrDomanda.getTemplateCode();
			log.info(logprefix + " xformId="+xformId);			
			
			Integer modelProgr = aggrDomanda.getModelProgr();
			log.info(logprefix + " modelProgr="+modelProgr);	
			
			String xformName = aggrDomanda.getModelName();
			log.info(logprefix + " xformName="+xformName);	
			
					
			// 1) legge l'xml della domanda  
			mod = AggregatoreFactory.create().readModel(userId, xformId, modelProgr);
			TreeMap<String, String> t2 = mod.getSerializedModel();
			
			// passo le date/ore da usare altrimenti prende quelle del DB server che potrebbero non essere allineate con l'application server
			if (!Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(flagBandoDematerializzato)){
				// 2) imposta nell'XML i dati che vanno valorizzato all'atto della trasmissione
				log.info(logprefix + Constants.XML_FIELD_DATATX + " presente nell'XML:" + t2.get(Constants.XML_FIELD_DATATX));

				String dataTXstr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(dataTxUfficiale);
				log.info(logprefix + " dataTX :" + dataTXstr);

				// aggiorna la data di trasmissione
				t2.put(Constants.XML_FIELD_DATATX, dataTXstr);
				log.info(logprefix + Constants.XML_FIELD_DATATX +" aggiornata nell'XML");
			} else {
				// aggiorniamo la data CONCLUSIONE
				log.info(logprefix + Constants.XML_FIELD_DATACO + " presente nell'XML:" + t2.get(Constants.XML_FIELD_DATACO));

				String dataCostr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(dataTxUfficiale);
				log.info(logprefix + " dataCostr :" + dataCostr);

				// aggiorna la data di trasmissione
				t2.put(Constants.XML_FIELD_DATACO, dataCostr);
				log.info(logprefix + Constants.XML_FIELD_DATACO +" aggiornata nell'XML");
			}

			// aggiorna lo stato per la stampa
			log.info(logprefix + Constants.XML_FIELD_STATOSTAMPA + " presente nell'XML:" + t2.get(Constants.XML_FIELD_STATOSTAMPA));
			t2.put(Constants.XML_FIELD_STATOSTAMPA, Constants.XML_FIELD_VALUE_STATOSTAMPA);
			log.info(logprefix + Constants.XML_FIELD_STATOSTAMPA + " aggiornato nell'XML");
			
			/*
			 *  inserire qui i campi del model che devono essere cambiati prima di inviare la domanda
			 */
			
			// aggiorna il DataModel
			mod.setSerializedModel(t2);
			log.info(logprefix + " model serializzato");
			
			StatusInfo stato = getStatus(); // TODO: dati presenti cf e denominazione: descrImpresaEnte
			
			// 3) salva l'XML della domanda nel DB
			int intModelProg = AggregatoreFactory.create().saveModel(userId, xformId, modelProgr, xformName, mod, null, stato.getStatoProposta());
			log.info(logprefix + " Salvataggio eseguito, intModelProg:" + intModelProg);

			
		} catch (AggregatoreException e) {
			log.error(logprefix + " errore nella modifica di dati del model, e=" + e.getMessage());
			res = false;
			
		} finally {
			log.info(logprefix + " END");
		}
		
		return res;
	}

	
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
	
	
	public void setRisultatoKO(String risultato) {
		getServletRequest().getSession().setAttribute("risultatoKO", risultato);
	}

	public void setRisultatoOK(String risultato) {
		getServletRequest().getSession().setAttribute("risultatoOK", risultato);
	}
	
	
	// GETTERS && SETTERS
	public IndexHelper getIndexHelper() {
		return indexHelper;
	}
	public void setIndexHelper(IndexHelper indexHelper) {
		this.indexHelper = indexHelper;
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

}
