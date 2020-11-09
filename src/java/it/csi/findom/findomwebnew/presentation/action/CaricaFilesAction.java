/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.findom.findomwebnew.business.helper.IndexHelper;
import it.csi.findom.findomwebnew.dto.index.DocumentoIndex;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomAllegatiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDocumentoIndexDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.exception.FindomwebException;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.csi.melograno.aggregatore.business.AggregatoreFactory;
import it.csi.melograno.aggregatore.business.DataModel;
import it.csi.melograno.aggregatore.exception.AggregatoreException;
import it.doqui.index.ecmengine.client.webservices.exception.InvalidParameterException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.EcmEngineTransactionException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.InvalidCredentialsException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoSuchNodeException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.PermissionDeniedException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.DeleteException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.InsertException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.opensymphony.xwork2.ValidationAware;

public class CaricaFilesAction extends BaseAction implements ValidationAware{

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "CaricaFilesAction";
	
	// parametri che arrivano dal form di caricamento del file
	private Integer idTipologiaNewDoc; // id Tipologia doc da inserire
	private String docDifferibile; //
	private File upFile;
	private String upFileContentType;
	private String upFileFileName;
	
	private String deletedoc; 	// id del documento da cancellare
	private String viewedoc; 	// id del documento da visualizzare
	
	private IndexHelper indexHelper;
	
	// parametri per l'azione di visualizzazione
	private String uuidFile;
	private String nomeFile;
	private String idAllegato;
	
	
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
			for (Iterator itr = collErr.iterator(); itr.hasNext();) {
				String err = (String) itr.next();
				log.error(logprefix + " Riscontrato errore = "+err);
				getServletRequest().getSession().setAttribute(Constants.INDEX_ACTION_ERROR, err);
			}
		}
		
//		image/jpeg,
//		image/gif,
//		text/plain,
//        application/pdf,
//        application/word,
//        application/msword,             
//        application/vnd.ms-excel,
//        application/msexcel
        
		// TODO : leggere questa lista da DB
		
		// TODO : fare qui il controllo sul type e alzare un errore o farlo nella executeAction ?
		
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
		log.info(logprefix + " idTipologiaNewDoc="+idTipologiaNewDoc);
		log.info(logprefix + " docDifferibile="+docDifferibile);
		
		String updsectid; // 7000
		String updpageid; // 7150
		String updsectxp; // S3_P3
		String updpagexp; // S3
		String templateCode; // BANDO00
		String formName; // Nuova Domanda
		String formProg; // aggr_t_model.model_progr
		String cfBeneficiario; // cf del beneficiario della domanda
		
		
		////////////////////////////////////////////////////////////
		// controllo dei parametri postati dal form
		boolean paramOK = true;
		boolean param1 = verificaParametroNumeric(deletedoc, "deletedoc");
		boolean param2 = verificaParametroNumeric(viewedoc, "viewedoc");
		boolean param4 = verificaParametroCheckBox(docDifferibile, "docDifferibile");
		boolean param3 = true;
		
		if(idTipologiaNewDoc!=null){
			param3 = verificaParametroNumeric(idTipologiaNewDoc + "", "idTipologiaNewDoc");
			if(param3 && idTipologiaNewDoc==9999){
				log.warn(logprefix + "Tipologia documento non selezionata");
				param3 = false;
			}
		}
		
		if(param1 && param2 && param3 && param4){
			log.warn(logprefix + " verificaParametri avvenuta con successo");
		}else{
			
			log.warn(logprefix + " verificaParametri fallita");
			// metto in sessione un errore che gestisco nella commonality
			
			//  Messaggio specifico in caso di tipologia doc non slezionata..
			if (idTipologiaNewDoc==9999)
				getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_PARAM_NULL);
			else
				getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_PARAM_MSG);
			paramOK = false;
		}

		/////////////////////////////////////////////////////////////
		// valorizzo alcuni dati della domanda su cui si sta lavorando
		
		// StatusInfo status;
		StatusInfo status = getStatus();
		log.info(logprefix + "status= " + status.getDescrImpresaEnte());
		
		//leggo l'oggetto StatusInfo dalla sessione
		TreeMap<String, Object> context = (TreeMap<String, Object>) getServletRequest().getSession().getAttribute(Constants.CONTEXT_ATTR_NAME);
		if (context != null)
		{
			log.info(logprefix + "letto STATUS dalla sessione");
			
			updpagexp = (String)context.get("xpageXp");
			log.info(logprefix + "updpagexp="+updpagexp); // '//*[@id='S3_P3']'

			updsectxp = (String)context.get("xsectXp");
			log.info(logprefix + "updsectxp="+updsectxp); // '//*[@id='S3']'
			
			updpageid = (Integer)context.get("xpageId")+"";
			log.info(logprefix + "updpageid="+updpageid); // 7150
			
			updsectid = (Integer)context.get("xsectId")+"";
			log.info(logprefix + "updsectid="+updsectid); // 7000
		
			templateCode = (String)context.get("xformId"); 
			log.info(logprefix + "templateCode="+templateCode); // BANDO00
			
			formName = (String)context.get("xformName"); 
			log.info(logprefix + "formName="+formName); // BANDO00
			
			cfBeneficiario = status.getCodFiscaleBeneficiario(); //"04971390010";
			log.info(logprefix + "cfBeneficiario="+cfBeneficiario);
			
			Integer fpr = (Integer)context.get("xformProg"); 
			if(fpr!=null)
				formProg = fpr+"";
			else
				formProg = null;
			
			log.info(logprefix + "formProg="+formProg);
			
		}else{
			
			log.error(logprefix + "context NULLO"); 
			return ERROR;
		}
		
		
		// verifico se il devo leggere il documento da Index
		Boolean usoIndex = (Boolean) getServletRequest().getSession().getAttribute("flagUploadIndex_bando"+status.getTemplateId());
		if(usoIndex == null){
			FindomTBandiDto band = getServiziFindomWeb().findoOneFindomTBandi(new Long(status.getTemplateId()));
			log.info(logprefix + "bando="+status.getTemplateId()+", file su Index="+band.getFlagUploadIndex());
			getServletRequest().getSession().setAttribute("flagUploadIndex_bando"+status.getTemplateId(),band.getFlagUploadIndex());
			usoIndex = band.getFlagUploadIndex();
		}else{
			log.info(logprefix + "leggo da sessione, bando="+status.getTemplateId()+", file su Index="+usoIndex);
		}

		/////////////////////////////////////////////////////////
		// azione di visualizzazione di un allegato
		if(paramOK && StringUtils.isNotBlank(viewedoc)){
			
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
				getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_READ_MSG);
			}else{
			
				nomeFile = documen.getNomeFile();
				log.info(logprefix + "nomeFile="+nomeFile);
				
				// valorizzo l'identificativo del documento da visualizzare nella prossima Action
				uuidFile = documen.getUuidNodo();
				log.info(logprefix + "uuidFile="+uuidFile);
				log.info(logprefix + "END");
				
				if(usoIndex){
					// redirigo sulla action che carica il file
					idAllegato = documen.getIdAllegato()+"";
					return "viewFile";
				} else {
					// redirigo sulla action che carica il file
					idAllegato = documen.getIdDocumentoIndex()+"";
					return "viewDBFile";
				}
			}
		
		/////////////////////////////////////////////////////////
		// azione di cancellazione di un allegato
		}else if(paramOK && StringUtils.isNotBlank(deletedoc)){
			
			/////////////////////////////////////////
			// CANCELLAZIONE DI UN FILE
			log.info(logprefix + "CANCELLAZIONE DI UN FILE");
			
			// verifico che l'utente loggato abbia i permessi per lavorare sul file indicato
			ShellDocumentoIndexDto documen = null;
			try{
				documen = getServiziFindomWeb().getDocumentoIndex(deletedoc, status.getNumProposta());
				log.info(logprefix + " documento recuperato ");
				
			}catch(Exception e){
				// qui entro perche non ho trovato nulla o perche ne ho trovati troppi
				log.warn(logprefix + " Exception e="+e.getMessage());
			}
			
			if(documen==null){
				log.warn(logprefix + " doc non trovato sul DB, verifica accesso fallita"); // 
				// metto in sessione un errore che gestisco nella commonality
				getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_DELETE_MSG);
				
			} else {
			
				boolean hasError = false;
				
				String uuid = documen.getUuidNodo();
				log.info(logprefix + "uuid="+uuid);
				
				try {
					// se uuid=='-' allora il doc e' sul DB
					if(StringUtils.isNotBlank(uuid) && !StringUtils.equals(uuid, "-")){ // uuid NON nullo, quindi esiste doc su Index
						// rimuovo il documento su Index
						indexHelper.deleteNode(uuid);
						log.info(logprefix + "cancellazione effettuata su Index");
					}
					
					// rimuovo i nodi dal file XML
					deleteAllegatiFromModel(cfBeneficiario, templateCode, formProg, formName, deletedoc);
					log.info(logprefix + "cancellazione effettuata dall'XML");

					// rimuovo la entry nella tabella shell_t_documento_index
					getServiziFindomWeb().deleteDocumentoIndex(deletedoc);
					log.info(logprefix + "cancellazione effettuata da tabella");

				} catch (InvalidCredentialsException e) {
					log.error(logprefix + "InvalidCredentialsException: "+e.getMessage());
					hasError = true;
				} catch (DeleteException e) {
					log.error(logprefix + "DeleteException: "+e.getMessage());
					hasError = true;
				} catch (NoSuchNodeException e) {
					log.error(logprefix + "NoSuchNodeException: "+e.getMessage());
					hasError = true;
				} catch (EcmEngineTransactionException e) {
					log.error(logprefix + "EcmEngineTransactionException: "+e.getMessage());
					hasError = true;
				} catch (PermissionDeniedException e) {
					log.error(logprefix + "PermissionDeniedException: "+e.getMessage());
					hasError = true;
				} catch (InvalidParameterException e) {
					log.error(logprefix + "InvalidParameterException: "+e.getMessage());
					hasError = true;
				} catch (RemoteException e) {
					log.error(logprefix + "RemoteException: "+e.getMessage());
					hasError = true;
				} catch (AggregatoreException e) {
					log.error(logprefix + "AggregatoreException: "+e.getMessage());
					hasError = true;
				} catch (ServiziFindomWebException e) {
					log.error(logprefix + "ServiziFindomWebException: "+e.getMessage());
					hasError = true;
				} catch (Exception e) {
					log.error(logprefix + "Exception: "+e.getMessage());
					hasError = true;
				} finally {
					log.info(logprefix + "hasError="+hasError);
					if(hasError){
						// metto in sessione un errore che gestisco nella commonality
						getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_DELETE_MSG);
					}else{
						// cancellazione riuscita, metto in sessione un messaggio
						getServletRequest().getSession().setAttribute(Constants.INDEX_OK, Constants.INDEX_OK_DELETE_MSG);
					}
				}
			}

		/////////////////////////////////////////////////////////
		// azione di caricamento di un allegato
		} else if (paramOK){

			/////////////////////////////////////////
			//UPLOAD DI UN FILE
			log.info(logprefix + "UPLOAD DI UN FILE");

			// file selezionato dall'utente
			File file = getUpFile();

			boolean hasError = false;

			// ottengo array di byte
			byte[] bFileUpload = null;
			
			Integer idDomanda = status.getNumProposta();
			log.info(logprefix + " idDomanda="+idDomanda);
			log.info(logprefix + " stato domanda="+status.getStatoProposta());

			Integer idSportello = status.getNumSportello();
			
			if(Constants.STATO_INVIATA.equals(status.getStatoProposta()) ){
				// non si possono aggiungere allegati su domande inviate 
				log.error(logprefix + " domanda Inviata, non si possono aggiungere allegati");
				throw new InsertException();
			} 
			
			if (file != null || StringUtils.equals(docDifferibile,"on")) {
				
				if(file != null ){
					log.info(logprefix + " file.length()="+file.length());
					bFileUpload = new byte[(int) file.length()];
				}
				try {
					HashMap mappaAllegati = new HashMap();
					// recupero la mappa di tutti i documenti allegabili
					
					mappaAllegati = getServiziFindomWeb().getMappaAllegati(status.getTemplateId(), idDomanda, idSportello);
					
					if(file != null ){
						
						bFileUpload = readFile(new FileInputStream(file), bFileUpload);
						log.info(logprefix + " readFile OK");
						
						// nome file lo uso per controllare l'estensione se serve...
						log.info(logprefix + " nomeFile="+getUpFileFileName());
						log.info(logprefix + " ContentType="+getUpFileContentType());
						String contentType = getUpFileContentType();
						
						String estensione = getEstensioneFile();
						log.info(logprefix + " estensione="+estensione);
						
						String signedFileExtensions =  getServiziFindomWeb().getValoreParametro(Constants.SIGNEDFILE_EXTENSION);
						log.info(logprefix + " signedFileExtensions="+signedFileExtensions);
						
						if(StringUtils.isNotBlank(signedFileExtensions)){					
							ArrayList<String> signedFileExtensionsList = new ArrayList<String>();
							StringTokenizer st = new StringTokenizer(signedFileExtensions, ",");
							while (st.hasMoreElements()) {
								signedFileExtensionsList.add((String)st.nextElement());
							}
							if (!signedFileExtensionsList.isEmpty() && signedFileExtensionsList.contains(estensione)) {

								String nomeFile = getUpFileFileName();
								if (StringUtils.isNotBlank(nomeFile)){
									int indiceUltimaEstensione = nomeFile.lastIndexOf(".");
									String nomeFileOrig = nomeFile.substring(0, indiceUltimaEstensione);

									int indicePenultimaEstensione = nomeFileOrig.lastIndexOf(".");
									String estensioneOrig = nomeFileOrig.substring(indicePenultimaEstensione);
									if(StringUtils.isNotBlank(estensioneOrig)){
										estensione = estensioneOrig;
									}
								}
							}	
						}
						log.info(logprefix + " estensione 2 ="+estensione);
						
						FindomAllegatiDto doc = (FindomAllegatiDto)mappaAllegati.get(idTipologiaNewDoc);
						String flagFirmaDigitale = doc.getFlagFirmaDigitale();
						log.info(logprefix + " flagFirmaDigitale="+flagFirmaDigitale);
						
						String par = Constants.PARAMETRO_UPLOAD_FILE;
						if ("S".equalsIgnoreCase(flagFirmaDigitale)){
							par = Constants.PARAMETRO_UPLOAD_FILE_FIRMA_DIGITALE;
						}
						
						ArrayList<String> tipiDoc = getServiziFindomWeb().getTipoDocumentoUpload(par);
						for (String tipoDoc : tipiDoc) {
							log.info(logprefix + "Estensione consentita: " + tipoDoc);
						}
						
						if (!tipiDoc.contains(estensione)) {
							// il file che si sta caricando non ha estensione consentita
							throw new FindomwebException();
						}
						
						// cerco su shell_t_documento_index se esiste un doc con quel nome
						ShellDocumentoIndexDto shelDoc = getServiziFindomWeb().getDocumentoIndex(idDomanda, getUpFileFileName());
						log.info(logprefix + " shelDoc="+shelDoc);
						
						if(shelDoc!=null){
							// il doc esiste gia, fermo tutto e mostro messaggio 
							log.error(logprefix + " documento gia presente su Index");
							throw new InsertException();
						} 
						
					}else{
						log.info(logprefix + " file nullo, e' un documento differibile");
					}
					
					// se la denominazione dell'azienda non e' nello status lo recupero da XML
					if(StringUtils.isBlank(status.getDescrImpresaEnte())){
						String denSoggetto = getServiziFindomWeb().getDenominazioneOperatorePresentatore(idDomanda);
						log.warn(logprefix + " denSoggetto="+denSoggetto );
						status.setDescrImpresaEnte(denSoggetto);
					}
					
					String uuidAllegato = "";
					
					DocumentoIndex docInx = valorizzaOggettoDocumentoIndex(status, bFileUpload, templateCode, docDifferibile);					
					log.info(logprefix + "docInx="+docInx);
						
					log.info(logprefix + "usoIndex="+usoIndex);
					
					// carico il documento dentro index (solo se non e' differibile, file=null)
					if(usoIndex){
						
						// individuo l'anno di apertura dello sportello per usarlo come nodo radice su Index
						String annoApertSport = impostaAnnoAperturaSportello();
						log.info(logprefix + "annoApertSport="+annoApertSport);

						if(file != null ){
							uuidAllegato = indexHelper.insertDocSuIndex(docInx, annoApertSport, idDomanda, templateCode, true);
						}
					} 
					log.info(logprefix + "uuidAllegato="+uuidAllegato);

					// se non ho avuto errori su Index proseguo e valorizzo il DB
					if(uuidAllegato!="ERROR_ON_INDEX"){
						
						/////////////////////////////////////////////////////////////////
						// MODIFICHE SU DB
					    // recupero i dati dell'utente loggato
					    ShellSoggettiDto utenteLoggato = getServiziFindomWeb().getDatiSoggettoByCodiceFiscale(getUserInfo().getCodFisc());
					    log.info(logprefix + " recuperati dati Operatore");

					    // valorizzo alcuni dati della tabella  shell_t_documento_index
						String messageDigest = "";  		// valorizzare in futuro
//						String dtVerificaFirma = null;		// valorizzare in futuro
//						String dtMarcaTemporale = null;		// valorizzare in futuro
					    Integer idStatoDocumentoIndex = 1;	// valorizzare in futuro
					    
					    String nomeFile = "-";
					    String repository = "-";
					    if(file != null ){
					    	nomeFile = getUpFileFileName();
					    	idStatoDocumentoIndex = Constants.ID_STATO_DOC_INDEX_GENERATO;
					    	if (StringUtils.equals(docDifferibile,"on"))
					    		idStatoDocumentoIndex = Constants.ID_STATO_DOC_INDEX_DIFFERITO;
					    	
					    }
					    
					    String idDoc = "";
					    
					    if(usoIndex){
					    	repository = Constants.INDEX_REPOSITORY;
					    	
							// salva in shell_t_documento_index
						    int id = getServiziFindomWeb().insertShellTDocumentoIndex(idDomanda, uuidAllegato, repository, nomeFile,
									messageDigest,	null, idStatoDocumentoIndex, idTipologiaNewDoc, utenteLoggato.getIdSoggetto());
							log.info(logprefix + " tabella  shell_t_documento_index scritta , id= "+id);
							
							ShellDocumentoIndexDto documen = null;
							// recupero l'id del doc appena inserito
							if(file != null ){
								documen = getServiziFindomWeb().getDocumentoIndex( uuidAllegato );
							}else{
								log.info(logprefix + " documento differito, non esiste sul DB");
								documen = getServiziFindomWeb().getLastDocumentoInserito(idDomanda);
							}
							
							if(documen!=null){
								idDoc = documen.getIdDocumentoIndex()+"";
								log.info(logprefix + " documen idDoc = "+idDoc);
							}else{
								log.error(logprefix + " documento non trovato sul DB");
								throw new Exception();
							}
						
						}else{
							
							// salva in shell_t_documento_index l'allegato
						    int idDocumentoIndex = getServiziFindomWeb().insertShellTDocumentoIndexDB(idDomanda, uuidAllegato, repository, nomeFile,
									messageDigest,	null, idStatoDocumentoIndex, idTipologiaNewDoc, utenteLoggato.getIdSoggetto(), bFileUpload);
							log.info(logprefix + " tabella  shell_t_documento_index scritta , idDocumentoIndex= "+idDocumentoIndex);
							
							idDoc = idDocumentoIndex+"";
						}
						
						/////////////////////////////////////////////////////////////////
						// modifico XML domanda aggiungendo un nuovo nodo dentro _allegati

						// aggiungo il nuovo nodo dal file XML
						insertAllegatiFromModel(cfBeneficiario, templateCode, Integer.parseInt(formProg), formName, docInx, 
											uuidAllegato, idDoc, mappaAllegati, status);
						
						getServletRequest().getSession().setAttribute(Constants.INDEX_OK, Constants.INDEX_OK_INSERT_MSG);
						
						// TODO : se inserimento dati su DB fallisce , elimino doc da INDEX ??
						
					}else{
						log.error(logprefix + " si e' verificato un qualche errore su INDEX, mi fermo");
						// metto in sessione un errore che gestisco nella commonality
						getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_INSERT_MSG);
					}

				} catch (FindomwebException e) {
					log.warn(logprefix + " FindomwebException, estensione documento non consentita");
					// metto in sessione un errore che gestisco nella commonality
					getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.ERROR_ESTENSIONE_FILE);
				} catch (InsertException e) {
					log.warn(logprefix + " InsertException, documento gia presente su Index");
					// metto in sessione un errore che gestisco nella commonality
					getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_INSERT_EXSITING_MSG);
				} catch (AggregatoreException e) {
					log.error(logprefix + " AggregatoreException :"+e.getMessage());
					hasError = true;
					
				} catch (FileNotFoundException e) {
					log.info(logprefix + " FileNotFoundException="+e.getMessage());
					hasError = true;
					
				} catch (IOException e) {
					log.info(logprefix + " IOException="+e.getMessage());
					hasError = true;
				
				} catch (Exception e) {
					log.info(logprefix + " Exception="+e.getMessage());
					e.printStackTrace();
					hasError = true;
					
				}finally {
					
					if(hasError){
						// metto in sessione un errore che gestisco nella commonality
						getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_INSERT_MSG);
					}
				}
				
			}else {
				log.error(logprefix + " tutti i parametri nulli (file NULL && docDifferibile != 'on'), non facco nulla");
				if (file == null) {
					log.info(logprefix + " file null");
					// metto in sessione un errore specifico per  che gestisco nella commonality
					getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_FILE_NULL_MSG);
				} else {
					// metto in sessione un errore che gestisco nella commonality
					getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_INSERT_MSG);
				}
			}
		}
	
		// REDIRIGO SULLA PAGINA CHIAMANTE
		
		getServletRequest().setAttribute("#xcommId",  Constants.ACCESS_FORM_PAGE);  // ACCESS_PAGE
		getServletRequest().setAttribute("#xformId", templateCode);  // BANDO00
		getServletRequest().setAttribute("#xformProg", formProg); 
		getServletRequest().setAttribute("#xformState", status.getStatoProposta());   // BZ
		
		getServletRequest().setAttribute("#xsectId", updsectid);
		getServletRequest().setAttribute("#xsectXp", updsectxp);
		getServletRequest().setAttribute("#xpageId", updpageid);
		getServletRequest().setAttribute("#xpageXp", updpagexp);

		log.info(logprefix + " END");
		return SUCCESS;
	}
	

	

	/**
	 * Tramite l'aggregatore modifico l'xml del model aggiungendo il nodo in questione
	 * 
	 * @param cfBeneficiario
	 * @param templateCode
	 * @param formProg
	 * @param formName
	 * @param docInx
	 * @param uuidAllegato
	 * @param idDocIndex
	 * @param mappaAllegati 
	 * @param stato
	 * @throws Throwable 
	 */
	private void insertAllegatiFromModel(String cfBeneficiario, String templateCode, int formProg, String formName,	
			DocumentoIndex docInx, String uuidAllegato, String idDocIndex, HashMap mappaAllegati, StatusInfo stato) throws Throwable {
		final String methodName = "insertAllegatiFromModel";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		log.info(logprefix + " cfBeneficiario=["+cfBeneficiario+"]");
		log.info(logprefix + " templateCode=["+templateCode+"]");
		log.info(logprefix + " formProg=["+formProg+"]");
		log.info(logprefix + " formName=["+formName+"]");
		log.info(logprefix + " uuidAllegato=["+uuidAllegato+"]");
		log.info(logprefix + " idDocIndex=["+idDocIndex+"]");
		
		// 1) legge l'xml della domanda  
		DataModel datamodel = AggregatoreFactory.create().readModel(cfBeneficiario, templateCode, formProg);
		log.info(logprefix + " letto datamodel");
		
		TreeMap<String, String> mapSerializedModel = datamodel.getSerializedModel();

		// conto gli elementi nella lista ed inserire il +1
		int numDocAllegati = contaDocAllegati(mapSerializedModel);

		// per poter fare il controllo (nella global validation) che l'utente abbia inserito tutti i doc obbligatori 	
		// aggiungo un nodo nel XML contenente tutti gli ID dei doc obbligatori	
		String elencoAllObbl = "";
		Iterator it = mappaAllegati.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        log.info(logprefix + " " + pair.getKey() + " = " + pair.getValue());
	        FindomAllegatiDto doc = (FindomAllegatiDto)pair.getValue();
	        if("S".equals(doc.getFlagObbligatorio())){
	        	elencoAllObbl = elencoAllObbl + "," + pair.getKey() ;
	        }
	    }
	    log.info(logprefix + " elencoAllObbl:: " + elencoAllObbl);
	    mapSerializedModel.put("_allegati.elencoIdAllegatiObbligatori",elencoAllObbl);

		// aggiorna i campi....
		mapSerializedModel.put("_allegati.allegatiList[" + numDocAllegati + "].documento.descrTipologia", docInx.getDescrizioneTipologiaAllegato());
		mapSerializedModel.put("_allegati.allegatiList[" + numDocAllegati + "].documento.idTipologia", idTipologiaNewDoc+"");
		mapSerializedModel.put("_allegati.allegatiList[" + numDocAllegati + "].documento.obbligatorio", docInx.getFlagObbligatorio());
		mapSerializedModel.put("_allegati.allegatiList[" + numDocAllegati + "].documento.nomeFile",getUpFileFileName());
		mapSerializedModel.put("_allegati.allegatiList[" + numDocAllegati + "].documento.idFile", idDocIndex);
		mapSerializedModel.put("_allegati.allegatiList[" + numDocAllegati + "].documento.contentType", getUpFileContentType());
		mapSerializedModel.put("_allegati.allegatiList[" + numDocAllegati + "].documento.indexUuid", uuidAllegato);
		if(StringUtils.equals(docInx.getDocDifferibile(),"on")){
			mapSerializedModel.put("_allegati.allegatiList[" + numDocAllegati + "].documento.differito", "true");
		}else{
			mapSerializedModel.put("_allegati.allegatiList[" + numDocAllegati + "].documento.differito", "false");
		}
		
		// aggiorna il DataModel
		datamodel.setSerializedModel(mapSerializedModel);
		log.info(logprefix + " model serializzato");
		// salva l'XML della domanda nel DB
		//int intModelProg = AggregatoreFactory.create().saveModel(cfBeneficiario, templateCode, formProg, formName, datamodel, null, stato.getStatoProposta());
		int intModelProg = AggregatoreFactory.create().saveModel(cfBeneficiario, templateCode, formProg, formName, datamodel, null, Constants.STATO_BOZZA);
		log.info(logprefix + " END - Salvataggio eseguito, intModelProg:" + intModelProg);
	}


	/**
	 * Valorizzo un oggetto che servira' per popolare il contentModel di Index
	 * 
	 * @param stato
	 * @param bFileUpload
	 * @param templateCode
	 * @param docDifferibile 
	 * @return
	 * @throws ServiziFindomWebException
	 * @throws Exception
	 */
	private DocumentoIndex valorizzaOggettoDocumentoIndex(StatusInfo stato, byte[] bFileUpload, String templateCode, String docDifferibile) throws ServiziFindomWebException, Exception {
		final String methodName = "valorizzaOggettoDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		DocumentoIndex docInx = new DocumentoIndex();
		
		// recupero informazioni sulla tipologia del documento allegato
		Integer idDomanda = stato.getNumProposta();
		log.info(logprefix + " idDomanda="+idDomanda);
		log.info(logprefix + " stato domanda="+stato.getStatoProposta());
		Integer idSportello = stato.getNumSportello(); // TODO: numero sportello bando 65 da testare
		log.info(logprefix + "idSportello= "+idSportello);
		HashMap<Integer, FindomAllegatiDto> mappaAllegati = getServiziFindomWeb().getMappaAllegati(stato.getTemplateId(), idDomanda, idSportello);
		
		FindomAllegatiDto allegatoDto = null;
		String descrTipologiaAllegato = "";
		String flagObbl = "";
		if(mappaAllegati!=null && !mappaAllegati.isEmpty()){
			allegatoDto = (FindomAllegatiDto)mappaAllegati.get(idTipologiaNewDoc);
			
			if(allegatoDto!=null){
				descrTipologiaAllegato = allegatoDto.getDescrizione();
				flagObbl = allegatoDto.getFlagObbligatorio();
			}else{
				log.info(logprefix + " allegatoDto NULL");
			}
		
		}else{
			log.info(logprefix + " mappaAllegati NULL");
		}
		
		log.info(logprefix + " descrTipologiaAllegato = "+descrTipologiaAllegato);
		log.info(logprefix + " flagObbl = "+flagObbl);

		String stereotipo = getServiziFindomWeb().getStereotipoDomanda(stato.getNumProposta());
		log.info(logprefix + " stereotipo = "+stereotipo);
		if(StringUtils.isBlank(stereotipo)){
			log.error(logprefix + " errore nel reperimento dello stereotipo della domanda");
			throw new Exception();
		}
		
		log.info(logprefix + "stato= "+stato.toString());
		log.info(logprefix + "stato.getDescrImpresaEnte= "+stato.getDescrImpresaEnte());
		log.info(logprefix + "stato.getDescrTipolBeneficiario= "+stato.getDescrTipolBeneficiario());
		
		// valorizzo Oggetto DocumentoIndex
		if(bFileUpload!=null){
			docInx.setFilename(getUpFileFileName());
		}else{
			docInx.setFilename("--");
		}
		docInx.setBytes(bFileUpload);
		docInx.setCodiceBando(templateCode); // LR_58_1978_bando_cultura_BIS
		docInx.setDescrizioneBando(stato.getDescrBando()); // Invito alla presentazione di progetti per la promozione della cultura
		docInx.setIdBando(stato.getTemplateId()); // 3
		docInx.setIdDomanda(stato.getNumProposta()); // 28738
		docInx.setCFUtenteUploader(stato.getOperatore()); // AAAAAA00A11D000L
		docInx.setDescrizioneUtenteUploader(stato.getDescrizioneOperatore()); // CSI PIEMONTE DEMO 23
		docInx.setCodiceImpresaEnte(stato.getCodFiscaleBeneficiario()); // MNTLGU80A01L219T
		docInx.setDescrizioneImpresaEnte(stato.getDescrImpresaEnte()); // Progettazione software ***
		docInx.setIdTipologiaBeneficiario(stato.getIdSoggettoBeneficiario()) ; // 2335
		docInx.setDescrizioneBeneficiario(stato.getDescrTipolBeneficiario()); // Enti privati/Soggetti Privati
		docInx.setStereotipoBeneficiario(stereotipo); // IM
		docInx.setIdTipologiaAllegato(idTipologiaNewDoc); // 22
		docInx.setDescrizioneTipologiaAllegato(descrTipologiaAllegato); // Fotocopia documento d'identita...
		docInx.setContentType(getUpFileContentType()); // application/pdf
		
		docInx.setDocDifferibile(docDifferibile); // null
		docInx.setFlagObbligatorio(flagObbl); // S
		
		log.info(logprefix + " END");
		return docInx;
	}


	/**
	 * Tramite l'aggregatore modifico l'xml del model eliminando il nodo in questione
	 * 
	 * @param cfBeneficiario
	 * @param templateCode
	 * @param formProg
	 * @param formName
	 * @param idDoc
	 * @throws Throwable 
	 */
	private void deleteAllegatiFromModel(String cfBeneficiario, String templateCode, String formProg, String formName, String idDoc) throws Throwable {
		final String methodName = "deleteAllegatiFromModel";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		log.info(logprefix + " cfBeneficiario=["+cfBeneficiario+"]");
		log.info(logprefix + " templateCode=["+templateCode+"]");
		log.info(logprefix + " formProg=["+formProg+"]");
		log.info(logprefix + " formName=["+formName+"]");
		log.info(logprefix + " idDoc=["+idDoc+"]");
		
		// 1) legge l'xml della domanda  
		DataModel mod = AggregatoreFactory.create().readModel(cfBeneficiario, templateCode, Integer.parseInt(formProg));
		log.info(logprefix + " letto mod");
		
		TreeMap<String, String> mapSerializedModel = mod.getSerializedModel();

		// trovo la posizione dell'elemento da rimuovere...
		Integer posizione = getPosizioneDocAllegato(mapSerializedModel, idDoc);
		log.info(logprefix + " posizione="+posizione);
		
		if(posizione!=null){
			
			// imposto "DELETED" l'elemento, al salvataggio ci pensa l'aggregatore ad eliminare l'xml
			mapSerializedModel.put("_allegati.allegatiList[" + posizione + "]", "DELETED");
			log.info(logprefix + " elemento rimosso da mapSerializedModel");
			
		}else{
			
			log.error(logprefix + "elemento non trovato all'interno dell'XML");
			throw new Exception();
		}
		
		// aggiorna il DataModel
		mod.setSerializedModel(mapSerializedModel);
		log.info(logprefix + " model serializzato");
		
		// salva l'XML della domanda nel DB
		int intModelProg = AggregatoreFactory.create().saveModel(cfBeneficiario, templateCode, Integer.parseInt(formProg), formName, mod, null, Constants.STATO_BOZZA);
		log.info(logprefix + " END - Salvataggio eseguito, intModelProg:" + intModelProg);
	}


	/**
	 * Conto il numero delle occorrenze del nodo "_allegati.allegatiList[X].documento.idFile"
	 * nell'xml della domanda serializzato
	 * @param t2
	 * @return
	 */
	private int contaDocAllegati(TreeMap<String, String> mappa) {
		final String methodName = "contaDocAllegati";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		int num = 0;
		
		while(true){
			String x =  mappa.get("_allegati.allegatiList["+num+"].documento.idFile");
			if(StringUtils.isBlank(x)){
				break;
			}else{
				num++;
			}
		}
		log.info(logprefix + "trovati "+num+" docuemnti allegati");
		log.info(logprefix + " END");
		return num;
	}

	private Integer getPosizioneDocAllegato(TreeMap<String, String> mappa, String idDoc) {
		final String methodName = "getPosizioneDocAllegato";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		Integer posizione = null;
		
		int numAllegati = contaDocAllegati(mappa);
		log.info(logprefix + " numAllegati="+numAllegati);
		
		for (int j = 0; j < numAllegati; j++) {
			String x =  mappa.get("_allegati.allegatiList["+j+"].documento.idFile");
			if (StringUtils.equals(x, idDoc)){
				posizione = j;
				log.info(logprefix + "trovata posizione ="+j);
				break;
			}
		}
		log.info(logprefix + " END");
		return posizione;
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
		// Modificato il formato della data.. ora e' questo: DD/MM/YYYY HH24:MI
		log.info(logprefix + "dataAperturaSportello="+dataAperturaSportello); // BANDO00

		if(StringUtils.isNotBlank(dataAperturaSportello)){
			//ret = dataAperturaSportello.substring(0, 4);
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


	/**
	 * Verifico che il parametro passato, se non nullo, sia di tipo numerico 
	 * @param param
	 * @param paramName
	 * @return
	 */
	private boolean verificaParametroNumeric(String param, String paramName){
		final String methodName = "verificaParametroNumeric";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		boolean res = true;

		if(StringUtils.isNotBlank(param) ){
			if(StringUtils.isNumeric(param)){
				log.info(logprefix + " parametro ["+paramName+"] valido");
			} else {
				log.warn(logprefix + " parametro ["+paramName+"] NON valido");
				res = false;
			}
		}else{
			log.info(logprefix + " parametro ["+paramName+"] NULL");
		}
		log.info(logprefix + " END, verificaParametro="+res);
		return res;
	}

	private boolean verificaParametroCheckBox(String param,	String paramName) {
		final String methodName = "verificaParametroCheckBox";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		boolean res = true;

		if(StringUtils.isNotBlank(param) ){
			if(StringUtils.equals("on",param)){
				log.info(logprefix + " parametro ["+paramName+"] valido");
			} else {
				log.warn(logprefix + " parametro ["+paramName+"] NON valido");
				res = false;
			}
		}else{
			log.info(logprefix + " parametro ["+paramName+"] NULL");
		}
		log.info(logprefix + " END, verificaParametro="+res);
		return res;
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
	public String getIdAllegato() {
		return idAllegato;
	}
	public void setIdAllegato(String idAllegato) {
		this.idAllegato = idAllegato;
	}
	public String getDocDifferibile() {
		return docDifferibile;
	}
	public void setDocDifferibile(String docDifferibile) {
		this.docDifferibile = docDifferibile;
	}
	public IndexHelper getIndexHelper() {
		return indexHelper;
	}
	public void setIndexHelper(IndexHelper indexHelper) {
		this.indexHelper = indexHelper;
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

}
