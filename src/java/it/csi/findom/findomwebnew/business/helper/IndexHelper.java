/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.helper;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import it.csi.findom.findomwebnew.dto.index.DocumentoIndex;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.integration.extservices.index.IndexDAO;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.doqui.index.ecmengine.client.webservices.dto.Node;
import it.doqui.index.ecmengine.client.webservices.exception.InvalidParameterException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.EcmEngineTransactionException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.InvalidCredentialsException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoDataExtractedException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoSuchNodeException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.PermissionDeniedException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.DeleteException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.InsertException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.search.SearchException;

public class IndexHelper {
	
	private IndexDAO indexDAO;
	
	protected static final Logger log = Logger.getLogger(Constants.APPLICATION_CODE + ".helper");
	private static final String CLASS_NAME = "IndexHelper";
	
	public IndexDAO getIndexDAO() {
		return indexDAO;
	}
	public void setIndexDAO(IndexDAO indexDAO) {
		this.indexDAO = indexDAO;
	}
	/**
	 * Cerca un nodo su Index, se non lo trova lo crea.
	 * 
	 * @param indexPath : path dentro cui cercare il nodo
	 * @param uuidNodoParent : uuid del nodo padre
	 * @param nomeNewNodo : nome del nodo da cercare
	 * @return
	 * @throws InvalidCredentialsException
	 * @throws NoSuchNodeException
	 * @throws EcmEngineTransactionException
	 * @throws SearchException
	 * @throws InsertException
	 * @throws InvalidParameterException
	 * @throws PermissionDeniedException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public String getUuidNode(String indexPath, String uuidNodoParent, String nomeNewNodo) throws InvalidCredentialsException, 
			NoSuchNodeException, EcmEngineTransactionException, SearchException, InsertException, InvalidParameterException, 
			PermissionDeniedException, RemoteException, MalformedURLException, ServiceException {
		
		final String methodName = "getUuidNode";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		log.debug(logprefix + " indexPath=["+indexPath+"]");
		log.debug(logprefix + " uuidNodoParent=["+uuidNodoParent+"]");
		log.debug(logprefix + " nomeNewNodo=["+nomeNewNodo+"]");
		
		String uuid = "";
	
		try {
			
			uuid = indexDAO.getUidNode(nomeNewNodo, indexPath);
			
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException e="+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (NoDataExtractedException e) {
			log.error(logprefix + " NoDataExtractedException e="+e.getMessage());
			//throw new NoDataExtractedException(e.getMessage());
			// folder non trovato, non alzo una eccezione ma creo il nodo
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + " EcmEngineTransactionException e="+e.getMessage());
			throw new EcmEngineTransactionException(e.getMessage());
		} catch (SearchException e) {
			log.error(logprefix + " SearchException e="+e.getMessage());
			throw new SearchException(e.getMessage());
		} catch (InvalidParameterException e) {
			log.error(logprefix + " InvalidParameterException e="+e.getMessage());
			throw new InvalidParameterException(e.getMessage());
		} catch (RemoteException e) {
			log.error(logprefix + " RemoteException e="+e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (MalformedURLException e) {
			log.error(logprefix + " MalformedURLException e="+e.getMessage());
			throw new MalformedURLException(e.getMessage());
		} catch (ServiceException e) {
			log.error(logprefix + " ServiceException e="+e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		
		log.debug(logprefix + "  uuid = ["+uuid+"]");
		if(StringUtils.isBlank(uuid)){
		
			try{
				log.debug(logprefix + "  creo nuovo folder");
				uuid = indexDAO.createFolder(uuidNodoParent, nomeNewNodo);
				
			}catch (InvalidCredentialsException e1){
				log.error(logprefix + " InvalidCredentialsException e1="+e1.getMessage());
				throw new InvalidCredentialsException(e1.getMessage());
			}catch (NoSuchNodeException e1){
				log.error(logprefix + " NoSuchNodeException e1="+e1.getMessage());
				throw new NoSuchNodeException(e1.getMessage());
			}catch (EcmEngineTransactionException e1){
				log.error(logprefix + " EcmEngineTransactionException e1="+e1.getMessage());
				throw new EcmEngineTransactionException(e1.getMessage());
			}catch (InsertException e1){
				log.error(logprefix + " InsertException e1="+e1.getMessage());
				throw new InsertException(e1.getMessage());
			}catch (PermissionDeniedException e1){
				log.error(logprefix + " PermissionDeniedException e1="+e1.getMessage());
				throw new PermissionDeniedException(e1.getMessage());
			}catch (InvalidParameterException e1){
				log.error(logprefix + " InvalidParameterException e1="+e1.getMessage());
				throw new InvalidParameterException(e1.getMessage());
			}catch (RemoteException e1){
				log.error(logprefix + " RemoteException e1="+e1.getMessage());
				throw new RemoteException(e1.getMessage());
			}
		}
		
		log.debug(logprefix + " END uuid = ["+uuid+"]");
		return uuid;
	}
	
	
	/**
	 * Se non ci sono eccezioni su Index, restituisce il numero seriale del documento
	 * altrimenti restituisce null
	 * 
	 * @param docInx
	 * @param anno
	 * @param idDomanda
	 * @return
	 */
	public String insertDocSuIndex(DocumentoIndex docInx, String anno, Integer idDomanda, String templateCode, boolean allegato) {
		final String methodName = "insertDocSuIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		// numero seriale UUID dell'allegato su index
		String uuid = "ERROR_ON_INDEX";
		
		///////////////////////////////////////////////////
		// Alberatura INDEX : 
		//    /app:company_home/cm:findom/cm:ANNO/cm:BANDO/cm:DOMANDA
		// dove:
		//  ANNO : anno di apertura dello sportello (es: 2016)
		//  BANDO : templateId (es: BANDO00)
		//  DOMANADA : modelId (es: 2096)
		String rootAnno = Constants.INDEX_ROOTPATH + Constants.INDEX_PREFIX + anno;
		String rootTemplate = rootAnno + Constants.INDEX_PREFIX + templateCode;
		String rootDomanda =  rootTemplate + Constants.INDEX_PREFIX + idDomanda;
		
		log.debug(logprefix + "rootAnno = ["+rootAnno+"]");
		log.debug(logprefix + "rootTemplate = ["+rootTemplate+"]");
		log.debug(logprefix + "rootDomanda = ["+rootDomanda+"]");
		
		// numeri seriali UUID delle cartelle su index
		String uuidRoot = "";
		String uuidAnno = "";
		String uuidBando = "";
		String uuidDomanda = "";
		
		try {
			uuidRoot = indexDAO.searchNodeRoot().getUid();
			log.debug(logprefix + "uuidRoot = ["+uuidRoot+"]");
			
			// verifico che esista folder ANNO, se non esiste la creo
			uuidAnno = getUuidNode(Constants.INDEX_ROOTPATH, uuidRoot, anno );
			log.debug(logprefix + "uuidAnno = ["+uuidAnno+"]");
			
			// verifico che esista folder BANDO, se non esiste la creo
			uuidBando = getUuidNode(rootAnno, uuidAnno, templateCode );
			log.debug(logprefix + "uuidBando = ["+uuidBando+"]");
			
			// verifico che esista folder DOMANDA, se non esiste la creo
			uuidDomanda = getUuidNode(rootTemplate, uuidBando, idDomanda+"" );
			log.debug(logprefix + "uuidDomanda = ["+uuidDomanda+"]");
			
			// imposto il nodo contenitore del documento da caricare
			docInx.setUidNode(uuidDomanda);
			
			log.debug(logprefix + "docInx ="+docInx);
			
			Node newNode = indexDAO.insertDocumento(docInx, allegato);
			log.debug(logprefix + "allegato caricato su index ");
			
			if(newNode!=null){
				uuid = newNode.getUid();
			}
			log.debug(logprefix + "uuid= ["+uuid+"]");
			
		} catch (Exception e) { 
			log.error(logprefix + " errore nella creazione di qualche folder o dell'upload del file su Index " + e.getMessage());
		}finally{
			log.debug(logprefix + " END");
		}
		return uuid;
	}

	public String insertDocIntegrativoSuIndex(DocumentoIndex docInx, String anno, Integer idDomanda, String templateCode, boolean allegato) {
		final String methodName = "insertDocIntegrativoSuIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");

		// numero seriale UUID dell'allegato su index
		String uuid = "ERROR_ON_INDEX";

		///////////////////////////////////////////////////
		// Alberatura INDEX :
		// /app:company_home/cm:findom/cm:ANNO/cm:BANDO/cm:DOMANDA/cm:integrazioni
		// dove:
		// ANNO : anno di apertura dello sportello (es: 2016)
		// BANDO : templateId (es: BANDO00)
		// DOMANADA : modelId (es: 2096)
		String rootAnno = Constants.INDEX_ROOTPATH + Constants.INDEX_PREFIX + anno;
		String rootTemplate = rootAnno + Constants.INDEX_PREFIX + templateCode;
		String rootDomanda = rootTemplate + Constants.INDEX_PREFIX + idDomanda;
		String rootIntegrazioni = rootDomanda + Constants.INDEX_PREFIX + Constants.INDEX_INTEGRAZIONI;

		log.debug(logprefix + "rootAnno = [" + rootAnno + "]");
		log.debug(logprefix + "rootTemplate = [" + rootTemplate + "]");
		log.debug(logprefix + "rootDomanda = [" + rootDomanda + "]");
		log.debug(logprefix + "rootDomanda = [" + rootIntegrazioni + "]");

		// numeri seriali UUID delle cartelle su index
		String uuidRoot = "";
		String uuidAnno = "";
		String uuidBando = "";
		String uuidDomanda = "";
		String uuidIntegrazioni = "";

		try {
			uuidRoot = indexDAO.searchNodeRoot().getUid();
			log.debug(logprefix + "uuidRoot = [" + uuidRoot + "]");

			// verifico che esista folder ANNO, se non esiste la creo
			uuidAnno = getUuidNode(Constants.INDEX_ROOTPATH, uuidRoot, anno);
			log.debug(logprefix + "uuidAnno = [" + uuidAnno + "]");

			// verifico che esista folder BANDO, se non esiste la creo
			uuidBando = getUuidNode(rootAnno, uuidAnno, templateCode);
			log.debug(logprefix + "uuidBando = [" + uuidBando + "]");

			// verifico che esista folder DOMANDA, se non esiste la creo
			uuidDomanda = getUuidNode(rootTemplate, uuidBando, idDomanda + "");
			log.debug(logprefix + "uuidDomanda = [" + uuidDomanda + "]");

			// verifico che esista folder integrazioni
			uuidIntegrazioni = getUuidNode(rootDomanda, uuidDomanda, Constants.INDEX_INTEGRAZIONI);
			log.debug(logprefix + "uuidIntegrazioni = [" + uuidIntegrazioni + "]");

			// imposto il nodo contenitore del documento da caricare
			docInx.setUidNode(uuidIntegrazioni);

			Node newNode = indexDAO.insertDocumento(docInx, allegato);
			log.debug(logprefix + "allegato caricato su index ");

			if (newNode != null) {
				uuid = newNode.getUid();
			}
			log.debug(logprefix + "uuid= [" + uuid + "]");

		} catch (Exception e) {
			log.error(logprefix + " errore nella creazione di qualche folder o dell'upload del file su Index " + e.getMessage());
		} finally {
			log.debug(logprefix + " END");
		}
		return uuid;
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
	public DocumentoIndex valorizzaOggettoDocumentoIndex(StatusInfo stato, byte[] bFileUpload, String templateCode, String docDifferibile, String stereotipo, String fileName, String contentType) throws ServiziFindomWebException, Exception {
		final String methodName = "valorizzaOggettoDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		DocumentoIndex docInx = new DocumentoIndex();
		docInx.setFilename(fileName);
		docInx.setBytes(bFileUpload);
		//docInx.setUidNode(uidNode); 
		docInx.setCodiceBando(templateCode);
		docInx.setDescrizioneBando(stato.getDescrBando());
		docInx.setIdBando(stato.getTemplateId());
		docInx.setIdDomanda(stato.getNumProposta());
		docInx.setCFUtenteUploader(stato.getOperatore());
		docInx.setDescrizioneUtenteUploader(stato.getDescrizioneOperatore());
		docInx.setCodiceImpresaEnte(stato.getCodFiscaleBeneficiario());
		docInx.setDescrizioneImpresaEnte(stato.getDescrImpresaEnte());
		docInx.setIdTipologiaBeneficiario(stato.getIdSoggettoBeneficiario()) ; // ??
		docInx.setDescrizioneBeneficiario(stato.getDescrTipolBeneficiario());
		docInx.setStereotipoBeneficiario(stereotipo);
		docInx.setContentType(contentType);
		
		log.debug(logprefix + " END");
		return docInx;
	}
	
	public void updateWithTimestampedContent(String uuidNodo,
			byte[] bytesTimestamped) throws Exception {
		indexDAO.updateWithTimestampedContent(uuidNodo, bytesTimestamped);
	}
	
	public void deleteNode(String uuid) throws InvalidCredentialsException, DeleteException, NoSuchNodeException, EcmEngineTransactionException, PermissionDeniedException, InvalidParameterException, RemoteException {
		final String methodName = "deleteNode";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.debug(logprefix + " BEGIN, uuid="+uuid);
		
		Node nodo = new Node();
		nodo.setUid(uuid);
		
		long start=System.currentTimeMillis();
	
		try {
			indexDAO.deleteNode(uuid);
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException e="+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (DeleteException e) {
			log.error(logprefix + " DeleteException e1="+e.getMessage());
			throw new DeleteException(e.getMessage());
		} catch (NoSuchNodeException e) {
			log.error(logprefix + " NoSuchNodeException e1="+e.getMessage());
			throw new NoSuchNodeException(e.getMessage());
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + " EcmEngineTransactionException e1="+e.getMessage());
			throw new EcmEngineTransactionException(e.getMessage());
		} catch (PermissionDeniedException e) {
			log.error(logprefix + " PermissionDeniedException e1="+e.getMessage());
			throw new PermissionDeniedException(e.getMessage());
		} catch (InvalidParameterException e) {
			log.error(logprefix + " InvalidParameterException e1="+e.getMessage());
			throw new InvalidParameterException(e.getMessage());
		} catch (RemoteException e) {
			log.error(logprefix + " RemoteException e1="+e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	
		
		log.debug(logprefix + " nodo cancellato in ms: "+(System.currentTimeMillis()-start));
		log.debug(logprefix + " END");
	}
	
	public  byte[] sbustaDocumento(byte[] content) throws Exception {
		final String methodName = "valorizzaOggettoDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		long start=System.currentTimeMillis();
		try {
			return indexDAO.sbustaDocumento(content);
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException e="+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (DeleteException e) {
			log.error(logprefix + " DeleteException e1="+e.getMessage());
			throw new DeleteException(e.getMessage());
		} catch (NoSuchNodeException e) {
			log.error(logprefix + " NoSuchNodeException e1="+e.getMessage());
			throw new NoSuchNodeException(e.getMessage());
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + " EcmEngineTransactionException e1="+e.getMessage());
			throw new EcmEngineTransactionException(e.getMessage());
		} catch (PermissionDeniedException e) {
			log.error(logprefix + " PermissionDeniedException e1="+e.getMessage());
			throw new PermissionDeniedException(e.getMessage());
		} catch (InvalidParameterException e) {
			log.error(logprefix + " InvalidParameterException e1="+e.getMessage());
			throw new InvalidParameterException(e.getMessage());
		} catch (RemoteException e) {
			log.error(logprefix + " RemoteException e1="+e.getMessage());
			throw new RemoteException(e.getMessage());
		} 
		finally {
			log.debug(logprefix + " sbustato documento in ms: "+(System.currentTimeMillis()-start));
			log.debug(logprefix + " END");
		}
	}
	
	public String creaImpronta(Node node, String contentPropertyPrefixedName) throws Exception{
		final String methodName = "creaImpronta";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		long start=System.currentTimeMillis();
		try {
			return indexDAO.creaImpronta(node, contentPropertyPrefixedName);
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException e="+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (DeleteException e) {
			log.error(logprefix + " DeleteException e1="+e.getMessage());
			throw new DeleteException(e.getMessage());
		} catch (NoSuchNodeException e) {
			log.error(logprefix + " NoSuchNodeException e1="+e.getMessage());
			throw new NoSuchNodeException(e.getMessage());
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + " EcmEngineTransactionException e1="+e.getMessage());
			throw new EcmEngineTransactionException(e.getMessage());
		} catch (PermissionDeniedException e) {
			log.error(logprefix + " PermissionDeniedException e1="+e.getMessage());
			throw new PermissionDeniedException(e.getMessage());
		} catch (InvalidParameterException e) {
			log.error(logprefix + " InvalidParameterException e1="+e.getMessage());
			throw new InvalidParameterException(e.getMessage());
		} catch (RemoteException e) {
			log.error(logprefix + " RemoteException e1="+e.getMessage());
			throw new RemoteException(e.getMessage());
		} finally {

			log.debug(logprefix + " impronta creata in ms: "+(System.currentTimeMillis()-start));
			log.debug(logprefix + " END");
		}
	}
	public String creaImpronta(byte[] contenuto) throws Exception{
		final String methodName = "creaImpronta";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		long start=System.currentTimeMillis();
		try {
			return indexDAO.creaImpronta(contenuto);
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException e="+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (DeleteException e) {
			log.error(logprefix + " DeleteException e1="+e.getMessage());
			throw new DeleteException(e.getMessage());
		} catch (NoSuchNodeException e) {
			log.error(logprefix + " NoSuchNodeException e1="+e.getMessage());
			throw new NoSuchNodeException(e.getMessage());
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + " EcmEngineTransactionException e1="+e.getMessage());
			throw new EcmEngineTransactionException(e.getMessage());
		} catch (PermissionDeniedException e) {
			log.error(logprefix + " PermissionDeniedException e1="+e.getMessage());
			throw new PermissionDeniedException(e.getMessage());
		} catch (InvalidParameterException e) {
			log.error(logprefix + " InvalidParameterException e1="+e.getMessage());
			throw new InvalidParameterException(e.getMessage());
		} catch (RemoteException e) {
			log.error(logprefix + " RemoteException e1="+e.getMessage());
			throw new RemoteException(e.getMessage());
		} finally {

			log.debug(logprefix + " impronta creata in ms: "+(System.currentTimeMillis()-start));
			log.debug(logprefix + " END");
		}
	}
}
