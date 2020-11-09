/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.dosign;

import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.doqui.dosign.dosign.dto.signaturevalidation.SignerDto;
import it.doqui.dosign.dosign.dto.signaturevalidation.VerifyInfoDto;
import it.doqui.dosign.dosign.util.DosignBusinessCode;
import it.doqui.dosign.dosign.util.DosignExceptionCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Classe che contiene il report dei passi di verifica firma
 *
 */
public class EsitoVerificaFirma implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<String> msgError;
	private boolean verificaCorretta;
	private StringBuilder reportVerifica;

	public EsitoVerificaFirma() {
		msgError = new ArrayList<String>();
		verificaCorretta = true;
		reportVerifica = new StringBuilder();
	}
	
	public void checkError(VerifyInfoDto info, List<String> cfLegaleRapps, String cfDelegato) {
					
		setReportVerifica(info);
		this.parseEnvelopeComplianceCode(info.getEnvelopeComplianceCode());		
		this.parseInvalidSignCount(info.getInvalidSignCount());
		this.parseInvalidSign(info.getSigner(), cfLegaleRapps, cfDelegato);
		this.parseErrorCode(info.getError());
		
		return;
	}
	
	public boolean isVerificaCorretta() {
		return verificaCorretta;
	}

	public void setVerificaCorretta(boolean verificaCorretta) {
		this.verificaCorretta = verificaCorretta;
	}

	public String getErrorMessage() {
		String messaggioErrore = "";
		for (String error : msgError) {
			messaggioErrore += "\n"+error;
		}
		return messaggioErrore;
	}
	public String getReportVerifica() {
		return reportVerifica.toString();
	}

	private void parseErrorCode(int error) {
		
      switch (error) {
		
//		Verifica OK o errore di comunicazione fra DoSign server e PkBox server. 
		case DosignExceptionCode.DOSIGN_OK: // (0) 
			break;
			
		case DosignExceptionCode.DOSIGN_CLIENT_HTTP_ERROR: //(1537) 
		case DosignExceptionCode.DOSIGN_CLIENT_INIT_ERROR: //(1538) 
			msgError.add(Constants.ERRORE_SERVIZI_VERIFICA_FIRMA);
			setVerificaCorretta(false);
			break;
				
//		Errori relativi ai processi di verifica dello stato di validita' dei certificati. 

		case DosignExceptionCode.DOSIGN_LDAP_CERTIFICATE_PARSING_ERROR: //(2049) 
		case DosignExceptionCode.DOSIGN_LDAP_CERTIFICATE_SIGN_ERROR: //(2050) 
		case DosignExceptionCode.DOSIGN_LDAP_CERTIFICATE_VERIFY_ERROR: //(2051) 
			setVerificaCorretta(false);
			msgError.add(Constants.ERRORE_VERIFICA_VALIDITA_CERTIFICATO);
			break;
						
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_EXTENSIONS_ERROR: //(1031) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_HTTP_ERROR: //(1051) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_HTTP_GET_CRL_ERROR: //(1052) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_INVALID_URL: //(1025) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_ISSUER_NOT_FOUND: //(1028) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_LDAP_ATTRIBUTES_NOT_FOUND: //(1026) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_LDAP_CRLATTRIBUTE_NOT_FOUND: //(1029) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_LDAP_ERROR: //(1032) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_LDAP_GET_CRL_ERROR: //(1030) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_VERIFY_SIGN_ERROR: //(1027) 
		case DosignExceptionCode.DOSIGN_REVOCATION_CRL_CACERT_NOT_FOUND: //(1033) 
			setVerificaCorretta(false);
			msgError.add(Constants.ERRORE_VERIFICA_LISTA_DI_REVOCA);
			break;
			
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_AUTHORITYINFOACCESS_ERROR: //(1041) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_CERTIFICATE_CHAIN_ERROR: //(1042) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_CONNECTION_ERROR: //(1050) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_INVALID_URL: //(1040) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_PRIVATE_KEY_ERROR: //(1043) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_REQUEST_ENCODING_ERROR: //(1044) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_REQUEST_SIGNING_ERROR: //(1045) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_RESPONSE_CERTIFICATE_ERROR: //(1048) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_RESPONSE_DECODING_ERROR: //(1046) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_RESPONSE_INVALID: //(1047) 
		case DosignExceptionCode.DOSIGN_REVOCATION_OCSP_RESPONSE_KEY_USAGE_ERROR: // (1049) 
			setVerificaCorretta(false);
			msgError.add(Constants.ERRORE_VERIFICA_ENTE_CERTIFICATORE);
			break;
			
	  }		
	}
	
	private void parseInvalidSignCount(int invalidSigns) {
		if (invalidSigns > 0) {
			setVerificaCorretta(false);	
			msgError.add(Constants.ERRORE_VERIFICA_CONSISTENZA_FIRMA);
		}
	}
	
	private void parseEnvelopeComplianceCode(int error) {
		
		switch (error) {
				
			case DosignBusinessCode.DOSIGN_COMPLIANCE_GOOD: // (0) 
				break;
			
			case DosignBusinessCode.DOSIGN_COMPLIANCE_CODE_ENVELOPE_VERSION: // (1) 
				msgError.add(Constants.ERRORE_VERIFICA_BUSTA_CRITTOGRAFATA);
				setVerificaCorretta(false);
				break;
				
			case DosignBusinessCode.DOSIGN_COMPLIANCE_CODE_ENVELOPE_CONTENT_TYPE: // (2) 
			case DosignBusinessCode.DOSIGN_COMPLIANCE_CODE_ENVELOPE_CONTENT: // (3) 
			case DosignBusinessCode.DOSIGN_COMPLIANCE_CODE_ENVELOPE_REVOCATION: // (4)
				msgError.add(Constants.ERRORE_VERIFICA_CONTENUTO_BUSTA_CRITTOGRAFATA);
				setVerificaCorretta(false);
				break;
		}
	}
	
	private void setReportVerifica(VerifyInfoDto verifyInfo) {
		

		reportVerifica.append("\nverifyInfo: "+verifyInfo);
		reportVerifica.append("\nenvelope compliance value : "+verifyInfo.getEnvelopeCompliance());
		reportVerifica.append("\nenvelope compliance code value : "+verifyInfo.getEnvelopeComplianceCode());
		reportVerifica.append("\nenvelope compliance info value : "+verifyInfo.getEnvelopeComplianceInfo());
		reportVerifica.append("\nerror value : "+verifyInfo.getError());
		reportVerifica.append("\nerror msg value : "+verifyInfo.getErrorMsg());
		reportVerifica.append("\nhex error code value : "+verifyInfo.getHexErrorCode());
//		msgErroreDosign="VerifyInfo error: "+verifyInfo.getError()+" ,VerifyInfo errorMsg:"+verifyInfo.getErrorMsg();
//		codiceErroreDosign=verifyInfo.getHexErrorCode();
		reportVerifica.append("\ninvalid sign count value : "+verifyInfo.getInvalidSignCount());
		reportVerifica.append("\nsigner count value : "+verifyInfo.getSignerCount());
		reportVerifica.append("\ndata value : "+verifyInfo.getData());	
		
	}
	
	private void parseInvalidSign(List<SignerDto> listOfSigners, List<String> cfLegaleRapps, String cfDelegato) {
		
		List<String> fiscalCodes=new ArrayList<String>();
		
		if(listOfSigners != null) {
			int numberSigners = listOfSigners.size();
			reportVerifica.append("\nnumberSigners: "+numberSigners);
			for(int indiceSigner = 0; indiceSigner < numberSigners; indiceSigner++) {
				
				SignerDto signer = listOfSigners.get(indiceSigner);
				reportVerifica.append("\nsigner: "+signer);
				reportVerifica.append("\nigner.getSubjectDn(): "+signer.getSubjectDn());
				reportVerifica.append("\nsigner.getCertificateStatus(): "+signer.getCertificateStatus());
				reportVerifica.append("\nsigner.getCertificateStatusCode(): "+signer.getCertificateStatusCode());
				reportVerifica.append("\nsigner.getCertificateStatusDescription(): "+signer.getCertificateStatusDescription());
				reportVerifica.append("\nsigner.getCertificateStatusInfo(): "+signer.getCertificateStatusInfo());
				reportVerifica.append("\nsigner.getError(): "+signer.getError());
				reportVerifica.append("\nsigner.getErrorMsg(): "+signer.getErrorMsg());
				reportVerifica.append("\nsigner.getVerificationDate(): "+signer.getVerificationDate());
				reportVerifica.append("\nsigner.getFiscalCode(): "+signer.getFiscalCode());
				String fiscalCode = signer.getFiscalCode();
				//fiscalCode = fiscalCode.substring(3);
				
				reportVerifica.append("\nfiscalCode --> "+fiscalCode);
				fiscalCodes.add(fiscalCode);
				
				if (signer.getCertificateStatusCode() != DosignBusinessCode.DOSIGN_CERT_VALID) {
					msgError.add(Constants.ERRORE_VERIFICA_VALIDITA_CERTIFICATO);
					setVerificaCorretta(false);
				}
				if (signer.getSignatureCompliance() != DosignBusinessCode.DOSIGN_COMPLIANCE_GOOD ) {
					msgError.add(Constants.ERRORE_VERIFICA_CONSISTENZA_FIRMA);
					setVerificaCorretta(false);
				}
			}	
		}
		
		boolean foundCf=false;
		
		String cfDoSign="";
		String cfLegRappNotFound="";
		List<String> listCfMatched = new ArrayList<String>();

		for (String fiscalCode : fiscalCodes) {
			cfDoSign+=fiscalCode+" ";
			if (!StringUtils.isBlank(cfDelegato)){
				if(fiscalCode.equals(cfDelegato)){
					reportVerifica.append("\n\n--- CF MATCH! --"+fiscalCode);
					listCfMatched.add(fiscalCode);
					foundCf=true;
				}

			} else {
				for (String cfLegaleRapp : cfLegaleRapps) { 
					cfLegRappNotFound+=cfLegaleRapp+" ";
					//if(fiscalCode.equals(cfLegaleRapp)   ){  // verificare sia CF minuscoli che MAIUSCOLI
					if(StringUtils.isNotBlank(fiscalCode) && StringUtils.isNotBlank(cfLegaleRapp) 
							&& StringUtils.equals(fiscalCode.toUpperCase(), cfLegaleRapp.toUpperCase())){
						reportVerifica.append("--- CF MATCH! --"+fiscalCode);
						listCfMatched.add(fiscalCode);
						foundCf=true;
					}
				}
			}
		}


		if(!foundCf){
			
			msgError.add(Constants.ERRORE_VERIFICA_CODICE_FISCALE_FIRMATARIO);

			reportVerifica.append(" Codice fiscale del firmatario non valido: cf legale rappresentante: " + cfLegRappNotFound);
			reportVerifica.append(", cf delegato: "+cfDelegato+", cf restituito/i da doSign: "+cfDoSign);
			
			setVerificaCorretta(false);
		}

	}
	

}
