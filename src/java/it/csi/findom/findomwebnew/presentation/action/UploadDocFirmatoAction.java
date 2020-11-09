/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.findom.findomwebnew.business.helper.IndexHelper;
import it.csi.findom.findomwebnew.dto.dosign.EsitoVerificaFirma;
import it.csi.findom.findomwebnew.dto.index.DocumentoIndex;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.RegoleVerificaFirmaDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDocumentoIndexDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDocDematDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeDto;
import it.csi.findom.findomwebnew.integration.extservices.dosign.DosignDAO;
import it.csi.findom.findomwebnew.presentation.exception.FindomwebException;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.DateUtil;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.csi.melograno.aggregatore.business.AggregatoreFactory;
import it.csi.melograno.aggregatore.business.DataModel;
import it.csi.melograno.aggregatore.exception.AggregatoreException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.InsertException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.opensymphony.xwork2.ValidationAware;

public class UploadDocFirmatoAction extends BaseAction implements ValidationAware{

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "UploadDocFirmatoAction";
	
	// parametri che arrivano dal form di caricamento del file
	private Integer idTipologiaNewDoc; // id Tipologia doc da inserire
	private String docDifferibile; //
	private File upFile;
	private String upFileContentType;
	private String upFileFileName;
	
	private String deletedoc; 	// id del documento da cancellare
	private String viewedoc; 	// id del documento da visualizzare
	
	private DosignDAO dosignDAO; 	
	private IndexHelper indexHelper;
	
	// parametri per l'azione di visualizzazione
	private String uuidFile;
	private String nomeFile;
	VistaDomandeDto domanda;	
	ArrayList<ShellDocumentoIndexDto> listaDocFirmati;
	
	public void validate(){
		final String methodName = "validate";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.info(logprefix + " BEGIN");

		// se ho un errore : "the request was rejected because its size (2697115) exceeds the configured maximum (2097152)>
		// non ho nulla nel FieldErrors
		
		// verifico se sono state sollevate delle eccezioni dall'Interceptor:
		// struts.messages.error.uploading = A general error that occurs when the file could not be uploaded
		// struts.messages.error.file.too.large = Occurs when the uploaded file is too large as specified by maximumSize.
		// struts.messages.error.content.type.not.allowed = Occurs when the uploaded file does not match the expected content types specified
		// struts.messages.error.file.extension.not.allowed = Occurs when uploaded file has disallowed extension
		// struts.messages.upload.error.SizeLimitExceededException = Occurs when the upload request (as a whole) exceed configured struts.multipart.maxSize
		// struts.messages.upload.error.<Exception class SimpleName> = Occurs when any other exception took place during file upload process
		Collection collErr = getActionErrors();
		if(collErr!=null){
			boolean exceeds = false;
			log.warn(logprefix + " collErr size 1 = "+collErr.size());	
			for (Iterator itr = collErr.iterator(); itr.hasNext();) {
				String err = (String) itr.next();				
				log.error(logprefix + " Riscontrato errore = "+err);	
				
				if (err.contains("exceed")){
					exceeds = true;
					itr.remove();					
				}
			}
			log.warn(logprefix + " collErr size 2 = "+collErr.size());	
			if (exceeds){
				log.error(logprefix + " Aggiungo errore = "+Constants.ERR_MSG_MAX_SIZE_SIGNED_FILE);	
				collErr.add(Constants.ERR_MSG_MAX_SIZE_SIGNED_FILE);
				setActionErrors(collErr);
			}
			log.warn(logprefix + " collErr size 3 = "+collErr.size());	
		}

		log.info(logprefix + " nomeFile = "+getUpFileFileName());
		log.info(logprefix + " ContentType = "+getUpFileContentType());
		log.info(logprefix + " END");
	}
	
	@Override
	public String executeAction() throws Throwable {

		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.info(logprefix + " BEGIN");
		
		log.info(logprefix + " deletedoc="+deletedoc);
		log.info(logprefix + " viewedoc="+viewedoc);
		
		String templateCode; // BANDO00
		String cfBeneficiario; // cf del beneficiario della domanda
		
		
		////////////////////////////////////////////////////////////
		// controllo dei parametri postati dal form
		//boolean paramOK = true;
		
		/////////////////////////////////////////////////////////////
		// valorizzo alcuni dati della domanda su cui si sta lavorando
		StatusInfo status;
		//leggo l'oggetto StatusInfo dalla sessione
		TreeMap<String, Object> context = (TreeMap<String, Object>) getServletRequest().getSession().getAttribute(Constants.CONTEXT_ATTR_NAME);
		if (context != null){
			status = (StatusInfo) context.get(Constants.STATUS_INFO);
			log.info(logprefix + "letto STATUS dalla sessione");
					
			cfBeneficiario = status.getCodFiscaleBeneficiario(); //"XTBFNS58B08F813D";
			log.info(logprefix + "cfBeneficiario="+cfBeneficiario);
			
		}else{
			
			log.error(logprefix + "context NULLO"); 
			return ERROR;
		}
			
		Integer idDomanda = status.getNumProposta();
		/////////////////////////////////////////////////////////
		// azione di visualizzazione di un allegato
		if(StringUtils.isNotBlank(viewedoc)){
			
			/////////////////////////////////////////
			// VISUALIZZAZIONE DI UN FILE
			log.info(logprefix + "VISUALIZZAZIONE DI UN FILE");
			
			// verifico che l'utente loggato abbia i permessi per lavorare sul file indicato
			ShellDocumentoIndexDto documen = null;
			try{
				documen = getServiziFindomWeb().getDocumentoIndex(viewedoc, status.getNumProposta());
				log.info(logprefix + " documen recuperato ");
			
			}catch(Exception e){
				// qui entro perche non ho trovato nulla o perche ne ho trovati troppi
				log.error(logprefix + " documen NON recuperato, Exception e="+e.getMessage());
			}
			
			if(documen==null){
				log.warn(logprefix + " verifica accesso fallita");
				// metto in sessione un errore che gestisco nella commonality
				getServletRequest().getSession().setAttribute(Constants.UPLOAD_ERROR, Constants.INDEX_ERROR_READ_MSG);
			
			}else{
			
				nomeFile = documen.getNomeFile();
				log.info(logprefix + "nomeFile="+nomeFile);
				
				// valorizzo l'identificativo del documento da visualizzare nella prossima Action
				uuidFile = documen.getUuidNodo();
				log.info(logprefix + "uuidFile="+uuidFile);
				log.info(logprefix + "END");
				
				// redirigo sulla action che carica il file
				return "viewFile";
			}
		
		} else {
			
			String errorMsg = "";
			/////////////////////////////////////////
			//UPLOAD DI UN FILE
			log.info(logprefix + "UPLOAD DI UN FILE");

			// file selezionato dall'utente
			File file = getUpFile();

			boolean hasError = false;

			// ottengo array di byte
			byte[] bFileUpload = null;
						
			log.info(logprefix + " idDomanda="+idDomanda);
			
			String dtVerificaFirma = null;		// valorizzare in futuro
			String messageDigestSignedFile = null;
			
			if (file != null) {
				
				log.info(logprefix + " file.length()="+file.length());
				bFileUpload = new byte[(int) file.length()];

				try {

					bFileUpload = readFile(new FileInputStream(file), bFileUpload);
					log.info(logprefix + " readFile OK");

					// nome file lo uso per controllare l'estensione se serve...
					log.info(logprefix + " nomeFile="+getUpFileFileName());
					log.info(logprefix + " ContentType="+getUpFileContentType());

					String nomeFile = getUpFileFileName();
					
					String estensione = getEstensioneFile();
					log.info(logprefix + " estensione="+estensione);

					String tipoFirma = status.getTipoFirma();
					log.info(logprefix + " tipoFirma: " + tipoFirma);
					
					String estensioneConsentita = "";
					
					if (Constants.FIRMA_DIGITALE.equalsIgnoreCase(tipoFirma)){
						estensioneConsentita = getServiziFindomWeb().getValoreParametro(Constants.SIGNEDFILE_EXTENSION); //.p7m
						if (!estensioneConsentita.equals(estensione)) {

							getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.VERIFICA, Constants.ERR_MSG_SIGNEDFILE_EXTENSION + " ["+estensione+"]");
							log.error(logprefix + Constants.ERR_MSG_SIGNEDFILE_EXTENSION + " ["+estensione+"]" + ", domanda="+idDomanda);
							throw new FindomwebException(Constants.ERR_MSG_SIGNEDFILE_EXTENSION);
						}
					}
					log.info(logprefix + " estensioneConsentita: " + estensioneConsentita);
					
					String dimensioneMassima = getServiziFindomWeb().getValoreParametro(Constants.MAX_SIZE_SIGNED_FILE);
					if (StringUtils.isNotBlank(dimensioneMassima)){
						long dimMax = Long.parseLong(dimensioneMassima);
						if (file.length()>dimMax) {
							getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.VERIFICA, Constants.ERR_MSG_MAX_SIZE_SIGNED_FILE + " ["+dimensioneMassima+"]");
							log.error(logprefix + Constants.ERR_MSG_MAX_SIZE_SIGNED_FILE + " ["+dimensioneMassima+"]" + ", domanda="+idDomanda);
							throw new FindomwebException(Constants.ERR_MSG_MAX_SIZE_SIGNED_FILE);
						}
					}
					log.info(logprefix + " dimensioneMassima: " + dimensioneMassima);
					
					// controlliamo che il nome del file non sia stato modificato
					String nomeOriginario = "DomandaNumero["+idDomanda+"]-["+status.getCodFiscaleBeneficiario()+"].pdf"; 
					String nomeOriginarioPDF = "DomandaNumero["+idDomanda+"]-["+status.getCodFiscaleBeneficiario()+"].PDF"; 
					nomeOriginario += estensioneConsentita;
					nomeOriginarioPDF += estensioneConsentita;
					log.info(logprefix + " nomeOriginario="+nomeOriginario);
					log.info(logprefix + " nomeOriginarioPDF="+nomeOriginarioPDF);
					log.info(logprefix + " nomeFile="+nomeFile);
					
					if (nomeFile.equals(nomeOriginario) || nomeFile.equals(nomeOriginarioPDF)){
						log.info(logprefix + " nomeFile corretto");
					}else{
						// nome file modificato --> msg errore
						errorMsg = Constants.ERR_MSG_NOME_FILE_MODIFICATO;
						getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.VERIFICA, Constants.ERR_MSG_NOME_FILE_MODIFICATO + "nomeFile="+nomeFile);;
						log.error(logprefix + errorMsg + ", domanda="+idDomanda+", nomeOriginario="+nomeOriginario + ", nomeFile="+nomeFile);
						throw new FindomwebException(errorMsg);
					}

					// cerco su shell_t_documento_index se esiste un doc con quel nome
					ShellDocumentoIndexDto shelDoc = getServiziFindomWeb().getDocumentoIndex(idDomanda, getUpFileFileName());
					log.info(logprefix + " shelDoc="+shelDoc);

					if(shelDoc!=null){
						// il doc esiste gia, fermo tutto e mostro messaggio 
						log.error(logprefix + " documento gia presente su Index");
						addActionError(Constants.ERR_MSG_INDEX_INSERT_EXSITING_MSG);
						getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.VERIFICA, Constants.ERR_MSG_INDEX_INSERT_EXSITING_MSG + "filename="+getUpFileFileName());
						log.error(logprefix + Constants.ERR_MSG_INDEX_INSERT_EXSITING_MSG + "uuid="+shelDoc.getUuidNodo());
						return ERROR;
					} 

					String stereotipo = "";
					
					if (Constants.FIRMA_DIGITALE.equalsIgnoreCase(tipoFirma)){

						List<String> cfLegRapp = new ArrayList<String>();
						
						stereotipo = getServiziFindomWeb().getStereotipoDomanda(idDomanda);
						log.info(logprefix + " stereotipo = "+stereotipo);
						if(StringUtils.isBlank(stereotipo)){
							getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.UPLOAD, "errore nel reperimento dei dati della domanda dalla vista");
							log.error(logprefix + " errore nel reperimento dei dati della domanda dalla vista, idDomanda="+idDomanda);
							throw new Exception();
						}
						
						//SE il beneficiario della domanda ha stereotipo PF 
						if(Constants.STEREOTIPO_PERSONA_FISICA.equals(stereotipo)){
							//in fase di upload del documento firmato digitalmente, deve essere controllato che il codice fiscale 
							// del firmatario della domanda	corrisponda a quello del beneficiario persona fisica
							String cfBeneficiarioPF = status.getCodFiscaleBeneficiario();
							log.info(logprefix + " cfBeneficiarioPF: " + cfBeneficiarioPF);
							cfLegRapp.add(cfBeneficiarioPF);
							
						} else {
						
							String cfLegaleRappresentante = status.getCodiceFiscaleLegaleRappresentante();
							log.info(logprefix + " cfLegaleRappresentante: " + cfLegaleRappresentante);
							cfLegRapp.add(cfLegaleRappresentante);
							
						}

						
						String cfDelegato = status.getCodiceFiscaleDelegato();
						log.info(logprefix + " cfDelegato: " + cfDelegato);
						
						// Determina i parametri per la verifica firma
						Integer idBando = status.getTemplateId();
						RegoleVerificaFirmaDto regoleVerificaFirma = getServiziFindomWeb().getRegoleVerificaFirma(idBando);

						// i due hash sono diversi..... almeno per la domanda 10789 sono diversi
						// verificare se e' sensato paragonare gli hash come fatto
						// o se l'hash del doc firmato va calcolato diversamente.....
						// commento "controllo dell'impronta" ...
						
						String messageDigestSuDB = getServiziFindomWeb().getMessageDigestDomanda(idDomanda);
						log.info(logprefix + " messageDigestSuDB::: " + messageDigestSuDB);
						
//						 sbusta e estrae impronta con servizi index
						messageDigestSignedFile = indexHelper.creaImpronta(indexHelper.sbustaDocumento(bFileUpload));						
						log.info(logprefix + " messageDigestSignedFile::: " + messageDigestSignedFile);
						
						// da commentare per escludere il controllo dell'impronta
						if(messageDigestSuDB!=null && 
						   !messageDigestSuDB.equalsIgnoreCase(messageDigestSignedFile)){
							errorMsg = Constants.ERR_MSG_MESSAGE_DIGEST;
							getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.VERIFICA, errorMsg);
							log.error(logprefix + Constants.ERR_MSG_MESSAGE_DIGEST + "idDomanda="+idDomanda);
							throw new FindomwebException(errorMsg);
						}
						
						EsitoVerificaFirma esitoVerificaFirma = dosignDAO.verify(cfLegRapp, cfDelegato, bFileUpload, regoleVerificaFirma);
						
						if (esitoVerificaFirma.isVerificaCorretta() ){
							
							dtVerificaFirma = DateUtil.getDataAttuale();
							log.info(logprefix + " verificaFirma::: " + esitoVerificaFirma.isVerificaCorretta());

							getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.VERIFICA, Constants.VERIFICA_OK);

						} else {	
							
							// Errore verifica firma ...
							errorMsg = Constants.ERR_MSG_VERIFICA_FIRMA;
							errorMsg += esitoVerificaFirma.getErrorMessage();
							getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.VERIFICA, errorMsg);
							log.error(logprefix + Constants.ERR_MSG_VERIFICA_FIRMA + "idDomanda="+idDomanda);
							throw new FindomwebException(errorMsg);								
							
						}

					}
					
					// carico i dati dell'aggregatore per la domanda selezionata
					ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(idDomanda);
					
					if(listaAggData==null || (listaAggData!=null && listaAggData.size()!=1)){
						log.warn(logprefix + " listaAggData NULLA o di dimenzione errata");
						addActionError(Constants.CARICA_DOMANDA_FAILED);
						getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.UPLOAD, Constants.CARICA_DOMANDA_FAILED);
						log.error(logprefix + Constants.CARICA_DOMANDA_FAILED + " idDomanda="+idDomanda);
						return ERROR;
					}
					log.info(logprefix + " caricata listaAggData");
					
					AggrDataDto aggrDomanda = listaAggData.get(0);
					log.info(logprefix + " caricata aggrDomanda");
					
					Integer modelProg = aggrDomanda.getModelProgr();
					log.info(logprefix + " modelProg="+modelProg);
					
					String userId = aggrDomanda.getUserId(); // aggr_t_model.user_id
					log.info(logprefix + " userId="+userId);
					
					Integer modelId = aggrDomanda.getModelId(); // aggr_t_model.model_id
					log.info(logprefix + " modelId="+modelId);
					
					templateCode = aggrDomanda.getTemplateCode(); // aggr_t_model.model_id
					log.info(logprefix + " templateCode="+templateCode);
					
					String cf = getUserInfo().getCodFisc();
					log.info(logprefix + " cf=["+cf+"]");
					
					String ruolo = getUserInfo().getRuolo();
					log.info(logprefix + " ruolo="+ruolo);
					
					log.info(logprefix + " cfBenefic="+cfBeneficiario);
					
					// valorizzo Oggetto DocumentoIndex
					String fileName = "--";
					if(bFileUpload!=null){
						fileName = getUpFileFileName();
					}
					String contentType = getUpFileContentType();
					
					if(StringUtils.isBlank(status.getDescrImpresaEnte())){
						String denSoggetto = getServiziFindomWeb().getDenominazioneOperatorePresentatore(idDomanda);
						log.warn(logprefix + " denSoggetto="+denSoggetto );
						status.setDescrImpresaEnte(denSoggetto);
					}
					DocumentoIndex docInx = indexHelper.valorizzaOggettoDocumentoIndex(status, bFileUpload, templateCode, docDifferibile, stereotipo, fileName, contentType);
					
					// individuo l'anno di apertura dello sportello per usarlo come nodo radice su Index
					String annoApertSport = impostaAnnoAperturaSportello();
					log.info(logprefix + "annoApertSport="+annoApertSport);
					
					// carico il documento dentro index (solo se non e' differibile, file=null)
					String uuidDocumento = "";
					if(file != null ){
						uuidDocumento = indexHelper.insertDocSuIndex(docInx, annoApertSport, idDomanda, templateCode, false);
					}
					log.info(logprefix + "uuidDocumento="+uuidDocumento);
					
				
					// se non ho avuto errori su Index proseguo e valorizzo il DB
					if(uuidDocumento!="ERROR_ON_INDEX"){
						
						/////////////////////////////////////////////////////////////////
						// MODIFICHE SU DB
					    // recupero i dati dell'utente loggato
					    ShellSoggettiDto utenteLoggato = getServiziFindomWeb().getDatiSoggettoByCodiceFiscale(getUserInfo().getCodFisc());
					    log.info(logprefix + " recuperati dati Operatore");
						
					    Integer idStatoDocumentoIndex = Constants.ID_STATO_DOC_INDEX_VALIDATO;	// valorizzare in futuro
					    String repository = Constants.INDEX_REPOSITORY;
												
						// salva in shell_t_documento_index
						int id = getServiziFindomWeb().insertShellTDocumentoIndex(idDomanda, uuidDocumento, repository, nomeFile,
								messageDigestSignedFile,	dtVerificaFirma, idStatoDocumentoIndex, idTipologiaNewDoc, utenteLoggato.getIdSoggetto());
						log.info(logprefix + " tabella  shell_t_documento_index scritta , id= "+id);
						
						int idSoggettoCreatore = 0;
						// recupero i dati della domanda da visualizzare nei titoli della pagina
						if(Constants.RUOLO_AMM.equals(ruolo) || Constants.RUOLO_LR.equals(ruolo)){
							idSoggettoCreatore = aggrDomanda.getIdSoggettoCreatore();
						}else{
							idSoggettoCreatore = status.getIdSoggettoCollegato();
						}
						int idSoggettoBeneficiario = status.getIdSoggettoBeneficiario();
						
						// cambio lo stato della domanda in INVIATO
						try {
							// se il bando e' dematerializzato lo stato della domanda diventa CONCLUSA e non INVIATA
							String statoDomanda = Constants.STATO_INVIATA;							
							AggregatoreFactory.create().changeState(userId, templateCode, modelProg, statoDomanda);
												
						} catch (AggregatoreException e) {
							log.error(logprefix + " change state dell'aggregatore fallito. "+e);
							// AA rivedere il codice d'errore
							getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.UPLOAD, Constants.INVIO_DOMANDA_FAILED);
							addActionError(Constants.INVIO_DOMANDA_FAILED);
							
							log.error(logprefix + Constants.INVIO_DOMANDA_FAILED + " idDomanda="+idDomanda);
							return ERROR;
						}
						
						//verifico se il bando prevede l'istruttoria su findom e se si ottengo il codice stato istruttoria da mettere su shell_t_domande
						Integer idStatoIstruttoriaRI = getIdStatoIstruttoria(status.getTemplateId(),Constants.CODICE_STATO_ISTRUTTORIA_DA_PRENDERE_IN_CARICO);		
						
						// Aggiornamento data invio domanda
//						Integer val = getServiziFindomWeb().updateShellDomandeBandoDemat(idDomanda, status.getIdSoggettoCollegato());
						Integer val = getServiziFindomWeb().updateShellDomande(idDomanda, status.getIdSoggettoCollegato(), Constants.STATO_INVIATA,idStatoIstruttoriaRI, new Date());
						log.info(logprefix + " aggiornato data invio");
						
						// copiamo la domanda INVIATA dalla aggr_t_model alla istr_t_model 
						// e i record sulla aggr_t_model_index sulla istr_t_model_index
						getServiziFindomWeb().copiaDomandaPerIstruttoria(idDomanda);
						
						
						
						///////////////////////////////////////////
						// Scheda progetto - richiamo procedura di ribaltamento xml in fase di invio domanda
						// Controllo se il bando prevede la sezione "Scheda Progetto" e se prevede istruttoria esterna
						
						FindomTBandiDto pbandi = getServiziFindomWeb().findoOneFindomTBandi(new Long(status.getTemplateId()));
						
						if(pbandi!=null){
							log.info(logprefix + " getflagSchedaProgetto="+pbandi.getFlagSchedaProgetto());
							log.info(logprefix + " getFlagIstruttoriaEsterna="+pbandi.getFlagIstruttoriaEsterna());
						}
						
						if(pbandi!=null 
								&& pbandi.getFlagSchedaProgetto() 
								&& !StringUtils.equals("S", pbandi.getFlagIstruttoriaEsterna())
								){
							// Invoco la procedura fnc_crea_scheda_progetto che travasa i dati dell'XML _criteri nelle tabella shell_t_valut_domande
							String creaSP = getServiziFindomWeb().callProcedureCreaSchedaProgetto(idDomanda);
							log.info(logprefix + " creaSP="+creaSP);
							
							if(!StringUtils.equals("0", creaSP)){
								log.error(logprefix + " La function 'fnc_crea_scheda_progetto' ha sollevato la seguente eccezione: "+creaSP);
							}
						}
						
						
						ArrayList<VistaDomandeDto> datiDomanda = getServiziFindomWeb().getVistaDomandaByCreatoreBeneficiarioDomanda(idSoggettoCreatore, idSoggettoBeneficiario, idDomanda);
						domanda = datiDomanda.get(0);
						
					}else{
						log.error(logprefix + " si e' verificato un qualche errore su INDEX, mi fermo");

						getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.UPLOAD, Constants.ERR_MSG_INDEX_INSERT_DOMANDA);
						log.error(logprefix + Constants.ERR_MSG_INDEX_INSERT_DOMANDA + " idDomanda="+idDomanda);
						addActionError(Constants.ERR_MSG_INDEX_INSERT_DOMANDA);
						return ERROR;
					}

				} catch (FindomwebException e) {
					log.warn(logprefix + " FindomwebException " + e.getMessage());
					getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.UPLOAD, e.getMessage());
					log.error(logprefix +" FindomwebException idDomanda="+idDomanda);
					
					addActionError(e.getMessage());
					return ERROR;
									
				} catch (InsertException e) {
					log.warn(logprefix + " InsertException, documento gia presente su Index");
					getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.UPLOAD, Constants.ERR_MSG_INDEX_INSERT_EXSITING_MSG);
					addActionError(Constants.ERR_MSG_INDEX_INSERT_EXSITING_MSG);
					log.error(logprefix + Constants.ERR_MSG_INDEX_INSERT_EXSITING_MSG +" idDomanda="+idDomanda);
					return ERROR;
					
				} catch (FileNotFoundException e) {
					log.error(logprefix + " FileNotFoundException="+e.getMessage());
					hasError = true;
					
				} catch (IOException e) {
					log.error(logprefix + " IOException="+e.getMessage());
					hasError = true;
				
				} catch (Exception e) {
					log.error(logprefix + " Exception="+e.getMessage());
					hasError = true;
					
				}finally {
					
					if(hasError){
						getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.UPLOAD, Constants.ERR_MSG_INDEX_INSERT_DOMANDA);
						addActionError(Constants.ERR_MSG_INDEX_INSERT_DOMANDA);
						log.error(logprefix + Constants.ERR_MSG_INDEX_INSERT_DOMANDA +" idDomanda="+idDomanda);
						return ERROR;
					}
				}
				
			}else {
				
				log.info(logprefix + " file null");
				getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.UPLOAD, Constants.ERR_MSG_INDEX_ERROR_FILE_NULL_MSG);
				addActionError(Constants.ERR_MSG_INDEX_ERROR_FILE_NULL_MSG);
				log.error(logprefix + Constants.ERR_MSG_INDEX_ERROR_FILE_NULL_MSG +" idDomanda="+idDomanda);
				return ERROR;
			}
		}
		ShellDocumentoIndexDto shelDoc = getServiziFindomWeb().getDocumentoFirmato(idDomanda);
		log.info(logprefix + " shelDoc="+shelDoc);
		
		
		if (shelDoc!=null){
			log.info("shelDoc idDocumentoIndex: " + shelDoc.getIdDocumentoIndex());
			log.info("shelDoc nomeFile: " + shelDoc.getNomeFile());
			// il doc firmato e' gia stato uploadato
			listaDocFirmati = new ArrayList<ShellDocumentoIndexDto>();			
			listaDocFirmati.add(shelDoc);
			log.info(logprefix + " settiamo in session listaDocFirmati");
			getServletRequest().getSession().setAttribute("listaDocFirmati", listaDocFirmati);
		}
		// Aggiornare  lo status....
		status.setStatoProposta(Constants.STATO_INVIATA);
						
		boolean aggOK = aggiornaDatiTrasmissione(idDomanda);
		log.info(logprefix + " aggOK="+aggOK);
		if(!aggOK){
			log.warn(logprefix + " aggiornamento data trasmissione nell'XML fallito. ");								
			addActionError(Constants.INVIO_DOMANDA_FAILED);
			return ERROR;
		}

		getServiziFindomWeb().insertLogFirma(getUserInfo().getCodFisc(), idDomanda, Constants.UPLOAD, Constants.UPLOAD_OK);

		ArrayList<VistaDocDematDto> listaDocDematDto = getServiziFindomWeb().getVistaDocDemat(idDomanda);
		String destinatarioDomanda = getDestinatarioDomanda(listaDocDematDto);
		// destinatarioDomanda vale F o S a seconda della vista FINDOM_V_DOC_DEMAT_BATCH  
		if(destinatarioDomanda.equals(Constants.DESTINATARIO_DOMANDA_FINPIEMONTE)){
			addActionMessage(Constants.UPLOAD_DOMANDA_OK_FINPIEMONTE);
		}else{
			addActionMessage(Constants.UPLOAD_DOMANDA_OK);
		}
		
		log.info(logprefix + " END");
		return SUCCESS;
	}

	

	/**
	 * Restituisce l'anno di apertura dello sportello oppure l'anno corrente
	 * @return
	 */
	private String impostaAnnoAperturaSportello() {
		final String methodName = "impostaAnnoAperturaSportello";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		String ret = "";
		
		String dataAperturaSportello = getStatus().getAperturaSportelloDa(); //aperturaSportelloDa:"2015-03-23 00:01:00"
		// Modificato il formato della data.. ora   questo: DD/MM/YYYY HH24:MI
		//
		log.info(logprefix + "dataAperturaSportello="+dataAperturaSportello); 

		if(StringUtils.isNotBlank(dataAperturaSportello)){
			ret = dataAperturaSportello.substring(6, 10);
		}else{
			// se non e' valorizzata la data di apertura dello sportello prendo l'anno in corso
			int anno = Calendar.getInstance().get(Calendar.YEAR);
			log.info(logprefix + "anno="+anno);
			ret = anno+"";
		}
		log.info(logprefix + " END");
		return ret;
	}
	
	public static byte[] readFile(InputStream is, byte[] bytes)	throws IOException {
		final String methodName = "readFile";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Close the input stream and return bytes
		is.close();
		log.info(logprefix + " END");
		return bytes;
	}

	private boolean aggiornaDatiTrasmissione(Integer idDomanda) throws Throwable {
		final String methodName = "aggiornaDatiTrasmissione";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		boolean res = true;
		
		DataModel mod;
		try {
			
			ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(idDomanda);
			if(listaAggData==null || (listaAggData!=null && listaAggData.size()!=1)){
				log.warn(logprefix + " listaAggData NULLA o di dimensione errata");								
				return false;
			}
			AggrDataDto aggrDomanda = listaAggData.get(0);
			String userId = aggrDomanda.getUserId();
			
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
			
			Date dataTX = null;

			// 2) imposta nell'XML la data trasmissione
			log.info(logprefix + Constants.XML_FIELD_DATATX + " presente nell'XML:" + t2.get(Constants.XML_FIELD_DATATX));
			dataTX = new Date();
			String dataTXstr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(dataTX);
			log.info(logprefix + " dataTX :" + dataTXstr);

			// aggiorna la data di trasmissione
			t2.put(Constants.XML_FIELD_DATATX, dataTXstr);
			log.info(logprefix + Constants.XML_FIELD_DATATX +" aggiornata nell'XML");			
			
			// aggiorna il DataModel
			mod.setSerializedModel(t2);
			log.info(logprefix + " model serializzato");
			
			StatusInfo stato = getStatus();
			
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
		
	private String getDestinatarioDomanda(ArrayList<VistaDocDematDto> listaDocDematDto) {
		String retVal = Constants.DESTINATARIO_DOMANDA_REGIONE;  //default
		if(listaDocDematDto !=null && !listaDocDematDto.isEmpty()){
			for (VistaDocDematDto vistaDocDematDto : listaDocDematDto) {
				if(StringUtils.isNotBlank(vistaDocDematDto.getTipoAllegato()) && vistaDocDematDto.getTipoAllegato().equals(Constants.TIPO_ALLEGATO_DOMANDA)){
					if(	StringUtils.isNotBlank(vistaDocDematDto.getFlagFinpiemonte()) 
							&& vistaDocDematDto.getFlagFinpiemonte().equals(Constants.FLAG_TRUE) 
							&& StringUtils.isNotBlank(vistaDocDematDto.getFlagRegionale()) 
							&& vistaDocDematDto.getFlagRegionale().equals(Constants.FLAG_FALSE)){
						retVal=Constants.DESTINATARIO_DOMANDA_FINPIEMONTE; //sta per destinatario finpiemonte
						break;
					}
				}
			}
		}
		return retVal;
	}
		
	// GETTERS && SETTERS

	public File getUpFile() {
		return upFile;
	}
	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}
	public String getUpFileContentType() {
		return upFileContentType;
	}
	public void setUpFileContentType(String upFileContentType) {
		this.upFileContentType = upFileContentType;
	}
	public String getUpFileFileName() {
		return upFileFileName;
	}
	public void setUpFileFileName(String upFileFileName) {
		this.upFileFileName = upFileFileName;
	}
	public String getDeletedoc() {
		return deletedoc;
	}
	public void setDeletedoc(String deletedoc) {
		this.deletedoc = deletedoc;
	}
	public String getViewedoc() {
		return viewedoc;
	}
	public void setViewedoc(String viewedoc) {
		this.viewedoc = viewedoc;
	}

	public IndexHelper getIndexHelper() {
		return indexHelper;
	}
	public void setIndexHelper(IndexHelper indexHelper) {
		this.indexHelper = indexHelper;
	}
	public String getUuidFile() {
		return uuidFile;
	}
	public void setUuidFile(String uuidFile) {
		this.uuidFile = uuidFile;
	}
	public Integer getIdTipologiaNewDoc() {
		return idTipologiaNewDoc;
	}
	public void setIdTipologiaNewDoc(Integer idTipologiaNewDoc) {
		this.idTipologiaNewDoc = idTipologiaNewDoc;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getDocDifferibile() {
		return docDifferibile;
	}

	public void setDocDifferibile(String docDifferibile) {
		this.docDifferibile = docDifferibile;
	}
	
	public DosignDAO getDosignDAO() {
		return dosignDAO;
	}
	public void setDosignDAO(DosignDAO dosignDAO) {
		this.dosignDAO = dosignDAO;
	}
	
	public String getEstensioneFile(){
		String estensione = "";
		String nomeFile = getUpFileFileName();
		if (StringUtils.isNotEmpty(nomeFile)){
			int index = nomeFile.lastIndexOf(".");
			estensione = nomeFile.substring(index);
		}
		return estensione;
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

	
}
