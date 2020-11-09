/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDocumentoIndexDto;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.TreeMap;

public class ViewDBFilesAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "ViewDBFilesAction";
	
	private InputStream pdfStream;
	private String contentDisposition;
	private String contentType;
	private String uuidFile;
	private String nomeFile;
	private Integer idAllegato;
	
	private String updsectid; // 7000
	private String updpageid; // 7150
	private String updsectxp; // S3_P3
	private String updpagexp; // S3
	private String templateCode; // BANDO00
	private String formProg; // aggr_t_model.model_progr
	
	
	@Override
	public String executeAction() 
	{
		
		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.debug(logprefix + "  BEGIN");
		log.debug(logprefix + "  uuidFile = [" + uuidFile + "]");
		log.debug(logprefix + "  nomeFile = [" + nomeFile + "]");
		log.debug(logprefix + "  idAllegato = [" + idAllegato + "]");
		
		byte[] file;
		boolean hasError = false;
		StatusInfo status;
		//leggo l'oggetto StatusInfo dalla sessione
		TreeMap<String, Object> context = (TreeMap<String, Object>) getServletRequest().getSession().getAttribute(Constants.CONTEXT_ATTR_NAME);
		if (context != null){
			status = (StatusInfo) context.get(Constants.STATUS_INFO);
			log.debug(logprefix + "letto STATUS dalla sessione");
			
			// recupero i parametri per la pagina di ritorno.

			updpagexp = (String)context.get("xpageXp");
			log.debug(logprefix + "updpagexp="+updpagexp); // '//*[@id='S3_P3']'

			updsectxp = (String)context.get("xsectXp");
			log.debug(logprefix + "updsectxp="+updsectxp); // '//*[@id='S3']'
			
			updpageid = (Integer)context.get("xpageId")+"";
			log.debug(logprefix + "updpageid="+updpageid); // 7150
			
			updsectid = (Integer)context.get("xsectId")+"";
			log.debug(logprefix + "updsectid="+updsectid); // 7000
			
			templateCode = (String)context.get("xformId"); 
			log.debug(logprefix + "templateCode="+templateCode); // BANDO00
			
			Integer fpr = (Integer)context.get("xformProg"); 
			if(fpr!=null)
				formProg = fpr+"";
			else
				formProg = null;
			log.debug(logprefix + "formProg="+formProg);
		}
		
		try {
			
			ShellDocumentoIndexDto doc = getServiziFindomWeb().getAllegatoFromShellTDocumentoIndex(idAllegato);
			file = doc.getFileAllegato();
			
			String nomeFIle = doc.getNomeFile();
			log.debug(logprefix +" nomeFIle= "+nomeFIle);
			
			int r = nomeFIle.lastIndexOf(".");
			if(r>0){
				contentType = nomeFIle.substring(nomeFIle.lastIndexOf(".")+1, nomeFIle.length());
				log.debug(logprefix +" contentType= "+contentType);
			}
			
			if(file!=null && file.length>0){
				
				log.debug(logprefix + " filePdf.length="+file.length);
			}else{
				log.debug(logprefix + " filePdf NULL");
			}
			
			contentDisposition = "attachment;filename=\""+nomeFile;
			pdfStream = new ByteArrayInputStream(file);

		} catch (Exception e) {
			log.error(logprefix + " Exception: "+e.getMessage());
			hasError = true;
		}finally{
			
			if(hasError){
				
				// metto in sessione un errore che gestisco nella commonality
				getServletRequest().getSession().setAttribute(Constants.INDEX_ERROR, Constants.INDEX_ERROR_READ_MSG);
				
				// REDIRIGO SULLA PAGINA CHIAMANTE
				getServletRequest().setAttribute("#xcommId", Constants.ACCESS_FORM_PAGE);
				getServletRequest().setAttribute("#xformId", templateCode);
				getServletRequest().setAttribute("#xformProg", formProg );
				getServletRequest().setAttribute("#xformState", getStatus().getStatoProposta());
				getServletRequest().setAttribute("#xsectId", updsectid);
				getServletRequest().setAttribute("#xsectXp", updsectxp);
				getServletRequest().setAttribute("#xpageId", updpageid);
				getServletRequest().setAttribute("#xpageXp", updpagexp);
				
				log.debug(logprefix + "  END");
				return ERROR;
			}
			log.debug(logprefix + "  END");
		}

		return SUCCESS;
	}
	
	
	// GETTERS && SETTERS
	public InputStream getPdfStream() {
		return pdfStream;
	}
	public void setPdfStream(InputStream pdfStream) {
		this.pdfStream = pdfStream;
	}
	public String getContentDisposition() {
		return contentDisposition;
	}
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getUuidFile() {
		return uuidFile;
	}
	public void setUuidFile(String uuidFile) {
		this.uuidFile = uuidFile;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public Integer geIdAllegato() {
		return idAllegato;
	}
	public void setIdAllegato(Integer idAllegato) {
		this.idAllegato = idAllegato;
	}
}
