/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.integration.extservices.dosign;

import it.csi.findom.findomwebnew.dto.dosign.EsitoVerificaFirma;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.RegoleVerificaFirmaDto;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.doqui.dosign.dosign.business.session.dosign.signaturevalidation.DosignSignatureValidation;
import it.doqui.dosign.dosign.business.session.dosign.timestamping.DosignTimestamping;
import it.doqui.dosign.dosign.client.DosignServiceClient;
import it.doqui.dosign.dosign.dto.publish.DosignServiceDto;
import it.doqui.dosign.dosign.dto.signaturevalidation.VerifyDto;
import it.doqui.dosign.dosign.dto.signaturevalidation.VerifyInfoDto;
import it.doqui.dosign.dosign.dto.signaturevalidation.VerifyReportDto;
import it.doqui.dosign.dosign.dto.timestamping.TimestampingParameterDto;
import it.doqui.dosign.dosign.dto.timestamping.TimestampingValueDto;
import it.doqui.dosign.dosign.util.DosignConstants;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class DosignDAOImpl implements DosignDAO {

	private static final String CLASS_NAME = "DosignDAOImpl";
	private static final String TIMESTAMP = "TIMESTAMP";

	protected static final Logger log = Logger.getLogger(Constants.APPLICATION_CODE + ".integration.extservices.dosign");
	
	private ResourceBundle rB;

	protected static DosignServiceDto dosignService = null;
	protected static DosignServiceDto dosignTimestampingService = null;
	
	public void init() {
		final String methodName = "init";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
			
		try {
			ResourceBundle rB = ResourceBundle.getBundle("dosign");
			dosignService = initializeDosignService();
			dosignTimestampingService = initializeTimeStampingService();
			log.debug(logprefix + " dosignService inizializzato");
		} catch(Exception e){
			log.error(logprefix + " Exception " + e.getMessage());
		}

		log.debug(logprefix + " END");
	}
	
	

	public DosignDAOImpl() {
		final String methodName = "DosignDAOImpl";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		rB = ResourceBundle.getBundle("dosign");
		
		log.debug(logprefix + " END, rB="+rB);
	}
	
	public EsitoVerificaFirma verify(List<String> cfLegaleRapps, String cfDelegato, byte[] bytesSignedFile, 
						RegoleVerificaFirmaDto regoleVerificaFirma ) throws Exception {

		//http://tst-applogic.reteunitaria.piemonte.it/dosignmanager/DosignSignatureValidation/DosignSignatureValidationBean?wsdl
		final String methodName = "verify";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
				
//		FR - getione livello errore
		EsitoVerificaFirma verificaErroreDosign = new EsitoVerificaFirma();
//		-----
		
		try {

			log.debug(logprefix + "dosignService= "+dosignService);
			
			DosignSignatureValidation dosignSignatureValidation = DosignServiceClient.getSignatureValidationService(dosignService);
			
			log.debug(logprefix + "dosignSignatureValidation= "+dosignSignatureValidation);		
					
			//byte[] bytes = testReadFile();
			VerifyDto verifyDto = initializeVerifyDTO(regoleVerificaFirma.getTipologiaVerificaFirma(), bytesSignedFile);

			log.info(logprefix + "verifyDto OK ");		
			
			VerifyReportDto verifyReport = dosignSignatureValidation.verify(verifyDto);
		 
			log.debug(logprefix + "verifyReport OK ");		
			
			List<VerifyInfoDto> listOfVerifyInfo = verifyReport.getVerifyInfo();
			
			if(listOfVerifyInfo!=null){
				
				log.debug(logprefix + "numberVerifyInfo: "+listOfVerifyInfo.size());

				
				for (VerifyInfoDto verifyInfo : listOfVerifyInfo) {
	
	//				FR - getione livello errore
					verificaErroreDosign.checkError(verifyInfo, cfLegaleRapps, cfDelegato);
	//				-----
							
	//				String messageDigestSignedFile = DigestUtils.shaHex(verifyInfo.getData());
	//				log.debug(logprefix + " messageDigestSignedFile: " + messageDigestSignedFile);					
					
					/*ResultCheckMessageDigest resultCheckMessageDigest= checkMessageDigest(idDocIndex, verifyInfo.getData());
					if(!resultCheckMessageDigest.isResult()){
						return false;
					}*/
	
	
				}
			}else{
				log.debug(logprefix + "numberVerifyInfo: nullo");
			}
		
			
			log.debug(logprefix + " rapporto verifica: " + verificaErroreDosign.getReportVerifica());
			
		}catch(Exception ex) {
			log.error("Exception in verify: " + ex.getMessage());
			ex.printStackTrace();
			//throw new DigitalSignException("");
			verificaErroreDosign.setVerificaCorretta(false);
			return verificaErroreDosign;
		}
 		
		log.debug(logprefix + " END");
		
		return verificaErroreDosign;

	}
	
	
	private DosignServiceDto initializeDosignService() {
		DosignServiceDto serviceDosignService = new DosignServiceDto();
		
		String server = rB.getString("server");
		String port = rB.getString("port");
		String context = rB.getString("context");
	
		serviceDosignService.setServer(server);
		serviceDosignService.setContext(context);
		serviceDosignService.setPort(new Integer(port));
		return serviceDosignService;
	}
	
	private DosignServiceDto initializeTimeStampingService() {
		DosignServiceDto serviceDosignService = new DosignServiceDto();
		
		String port= rB.getString("port");
		String server= rB.getString("server");
		log.debug("initializing doSign service with server "+server + " port: "+port);
		serviceDosignService.setServer(server);
		serviceDosignService.setContext("/dosignmanager/DosignTimestamping");
		serviceDosignService.setPort(new Integer(port));
		return serviceDosignService;
	}

	private VerifyDto initializeVerifyDTO(String tipologiaVerificaFirma, byte[] bytesSignedFile) {
		VerifyDto verifyDto = new VerifyDto();
		verifyDto.setSignedDataMimeType(DosignConstants.OCTET_STREAM_MIMETYPE);
		
		if (Constants.VERIFICA_FORTE.equals(tipologiaVerificaFirma)) {
			verifyDto.setProfileType(DosignConstants.STRONG_SIGNATURE);
			verifyDto.setVerificationType(DosignConstants.CRL_VERIFICATION);

		} else {
			verifyDto.setProfileType(DosignConstants.WEAK_SIGNATURE);			
		}
		
		verifyDto.setExtractData(true);// to check message_digest against original file
		verifyDto.setEnvelopeArray(bytesSignedFile);
		return verifyDto;
	}
	
	/*private ResultCheckMessageDigest checkMessageDigest(String idDocIndex, byte[] bytesFromSignedFile) {
		
		final String methodName = "checkMessageDigest";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");

		log.debug(logprefix + "checkMessageDigest for idDocIndex"+idDocIndex);
		
		ResultCheckMessageDigest resultCheckMessageDigest=new ResultCheckMessageDigest();
		
		// RECUPERARE DA DB IL MESSAGE DIGEST DEL NOSTRO DOCUMENTO 
		String messageDigestDoSign = DigestUtils.shaHex(bytesFromSignedFile);
		String messageDigestDB = "";
		log.debug(logprefix + "messageDigest from original file on db: "+messageDigestDB+"\nDigestUtils.shaHex(bytesFromSignedFile) ----> "+messageDigestDoSign);
		if(messageDigestDB!=null && messageDigestDB.equalsIgnoreCase(messageDigestDoSign)){
			log.debug(logprefix + "checkMessageDigest for idDocIndex: "+idDocIndex+" ---> OK\n\n\n\n");
			resultCheckMessageDigest.setResult(true);
			return resultCheckMessageDigest;
		} else{
			log.debug(logprefix + "checkMessageDigest for idDocIndex: "+idDocIndex+" ---> KO\n\n\n\n");
			resultCheckMessageDigest.setResult(false);
			resultCheckMessageDigest.setMessage("message_digest non corretto.originale:"+messageDigestDB+" , da doSign:"+messageDigestDoSign);
			return resultCheckMessageDigest;
		}
	}*/
	
	public byte[] timeStamp(byte[] bytesSignedFile)   {
		   long start=System.currentTimeMillis();
		   byte[] bytesTimeStamped=null;
        DosignServiceDto dosignService = initializeTimeStampingService();
        try {
			   DosignTimestamping timestampingService = DosignServiceClient.getTimestampingService(dosignService);
			   log.debug("DosignServiceClient.getTimestampingService ms: "+(System.currentTimeMillis()-start));
			   TimestampingParameterDto timestampingParameter=new TimestampingParameterDto();
			   timestampingParameter.setData(bytesSignedFile);
			   timestampingParameter.setHashAlgorithm(5);
			   timestampingParameter.setEncoding(1);
			   timestampingParameter.setVerificationType(1);
			   timestampingParameter.setProfileType(0);
			   timestampingParameter.setCustomerInformation(null);
			   timestampingParameter.setCustomerTsa("TsaInfocert");
			   timestampingParameter.setHashProtected(false);
			   timestampingParameter.setFileName(null);
			   timestampingParameter.setMediaType(null);
			   TimestampingValueDto timeStampedData = timestampingService.createTimeStampedData(timestampingParameter);
			   bytesTimeStamped=timeStampedData.getEnvelopeArray();
			   log.debug("bytesTimeStamped returned from createTimeStampedData : "+bytesTimeStamped);
			   //logDoSign(idUtente, idDocIndex, "S", TIMESTAMP, (System.currentTimeMillis()-start),"OK TIMESTAMP" ,null,null);
        }catch(Exception ex){
     	   log.error("ERROR doSignManager.timeStamp",ex);
     	   //TODO RIMAPPARE CODICE DI ERRORE DOSIGN SERVICE
     	   //logDoSign(idUtente,idDocIndex,"N",TIMESTAMP,(System.currentTimeMillis()-start),(ex.getMessage()==null?""+ex.getClass():"Error: "+ex.getMessage()),"",ERR_GENERIC);
        }
		   return bytesTimeStamped;
	}

//	private class ResultCheckMessageDigest{
//		private boolean result;
//		private String message;
//		public boolean isResult() {
//			return result;
//		}
//		public void setResult(boolean result) {
//			this.result = result;
//		}
//		public String getMessage() {
//			return message;
//		}
//		public void setMessage(String message) {
//			this.message = message;
//		}
//	}
	

}
