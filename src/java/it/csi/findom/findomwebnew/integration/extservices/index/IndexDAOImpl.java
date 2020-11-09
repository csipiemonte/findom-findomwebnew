/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.integration.extservices.index;

import it.csi.findom.findomwebnew.dto.index.DocumentoIndex;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.doqui.index.ecmengine.client.webservices.dto.Node;
import it.doqui.index.ecmengine.client.webservices.dto.NodeInfo;
import it.doqui.index.ecmengine.client.webservices.dto.OperationContext;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Aspect;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Content;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Property;
import it.doqui.index.ecmengine.client.webservices.dto.engine.search.NodeResponse;
import it.doqui.index.ecmengine.client.webservices.dto.engine.search.ResultContent;
import it.doqui.index.ecmengine.client.webservices.dto.engine.search.SearchParams;
import it.doqui.index.ecmengine.client.webservices.dto.engine.security.DigestInfo;
import it.doqui.index.ecmengine.client.webservices.dto.engine.security.Document;
import it.doqui.index.ecmengine.client.webservices.dto.engine.security.EnvelopedContent;
import it.doqui.index.ecmengine.client.webservices.engine.EcmEngineWebServiceDelegate;
import it.doqui.index.ecmengine.client.webservices.engine.EcmEngineWebServiceDelegateServiceLocator;
import it.doqui.index.ecmengine.client.webservices.exception.EcmEngineException;
import it.doqui.index.ecmengine.client.webservices.exception.InvalidParameterException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.EcmEngineTransactionException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.InvalidCredentialsException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoDataExtractedException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoSuchNodeException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.PermissionDeniedException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.DeleteException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.InsertException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.ReadException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.UpdateException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.search.SearchException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.search.TooManyResultsException;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

public class IndexDAOImpl implements IndexDAO {

	private static final String APP_PREFIX ="findom:";
	private static final String UTF_8 = "UTF-8";
	private static final String CLASS_NAME = "IndexDAOImpl";
	
	protected static final Logger log = Logger.getLogger(Constants.APPLICATION_CODE + ".integration.extservices.index");
	
	private ResourceBundle rB;
	private OperationContext operationContext;
	
	private EcmEngineWebServiceDelegateServiceLocator ecmengineLocator ;
	private EcmEngineWebServiceDelegate ecmengineDelegate ;
	

	private static final String APPLICATION_PKCS7_MIME = "application/pkcs7-mime";
	private static final String APPLICATION_TSD_MIME = "application/timestamped-data";
	private static final String APPLICATION_PDF = "application/pdf";

	public void init() {
		final String methodName = "init";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
	
		log.debug(logprefix + "file caricato: " + rB+",userName ---> "   + rB.getString("userName")  +" ,url: "+ rB.getString("urlIndexWs"));
		
		// inizializzo il context per Index
		operationContext = getOperationContext();
		log.debug(logprefix + "operationContext OK " );
		
		// inizializzo il locator dei WS di Index
		ecmengineLocator = new EcmEngineWebServiceDelegateServiceLocator();
		log.debug(logprefix + "ecmengineLocator OK " );
		
		try {
//			log.debug(logprefix + "urlIndexWs=["+rB.getString("urlIndexWs")+"]");
			
			ecmengineDelegate = ecmengineLocator.getEcmEngineManagement(new URL(rB.getString("urlIndexWs")));
			log.debug(logprefix + " ecmengineDelegate inizializzato");
			
		} catch (MalformedURLException e) {
			log.error(logprefix + " MalformedURLException "+e.getMessage());
		} catch (ServiceException e) {
			log.error(logprefix + " ServiceException "+e.getMessage());
		}
		
		log.debug(logprefix + " END");
	}
	
	

	public IndexDAOImpl() {
		final String methodName = "IndexDAOImpl";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		rB = ResourceBundle.getBundle("ecmEngine");
		
		log.debug(logprefix + " END, rB="+rB);
	}
	
	private OperationContext getOperationContext() {
		OperationContext operationContext = new OperationContext();
		operationContext.setUsername(rB.getString("userName"));
		operationContext.setPassword(rB.getString("password"));
		operationContext.setNomeFisico(rB.getString("nomeFisico"));
		operationContext.setFruitore(rB.getString("fruitore"));
		return operationContext;
	}
	
	private EcmEngineWebServiceDelegate getEcmengineDelegate() throws MalformedURLException, ServiceException{
		final String methodName = "getEcmengineDelegate";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN-END");
//		if(ecmengineDelegate==null){
//			log.debug(logprefix + " ecmengineDelegate NULL");
//			init();
//		}
//		log.debug(logprefix + " END");
		return ecmengineDelegate;
	}

	////////// Operazioni
	
	@Override
	public Node searchNodeRoot() throws InvalidCredentialsException,
			EcmEngineTransactionException, SearchException,
			PermissionDeniedException, InvalidParameterException,
			RemoteException, MalformedURLException, ServiceException {
				
		final String methodName = "searchNodeRoot";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		Node toReturn = null;
		
		SearchParams searchParams = new SearchParams();
		String xPathQuery = rB.getString("path_root");
		log.debug(logprefix + " xPathQuery=["+xPathQuery+"]");
		
		log.debug(logprefix + " userName=["+rB.getString("userName")+"]");
		log.debug(logprefix + " password=["+rB.getString("password")+"]");
		
		searchParams.setXPathQuery(xPathQuery);
		
		try {
			
			NodeResponse response = getEcmengineDelegate().xpathSearchNoMetadata(searchParams, operationContext);
			log.debug(logprefix + " Numero Nodi trovati= " + response.getTotalResults());
			
			if (response.getTotalResults() > 0) {
				toReturn = (response.getNodeArray())[0];
				log.debug(logprefix + "Node UID: " + toReturn.getUid());
			}
			
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException e="+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + " EcmEngineTransactionException e="+e.getMessage());
			throw new EcmEngineTransactionException(e.getMessage());
		} catch (SearchException e) {
			log.error(logprefix + " SearchException e="+e.getMessage());
			throw new SearchException(e.getMessage());
		} catch (PermissionDeniedException e) {
			log.error(logprefix + " PermissionDeniedException e="+e.getMessage());
			throw new PermissionDeniedException(e.getMessage());
		} catch (TooManyResultsException e) {
			log.error(logprefix + " TooManyResultsException e="+e.getMessage());
			throw new TooManyResultsException(e.getMessage());
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
		
		log.debug(logprefix + " END");
		return toReturn;
	}



	@Override
	public byte[] getFileFirmato(String uuid) throws ReadException, InvalidCredentialsException, NoSuchNodeException, EcmEngineTransactionException, PermissionDeniedException, InvalidParameterException, RemoteException {
		final String methodName = "getFileFirmato";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN, uuid="+uuid);
		
		
		Node node = new Node(uuid);
		Content content = new Content();
		//content.setModelPrefixedName("cm:contentmodel");
		content.setContentPropertyPrefixedName("findom:fileFirmato");
		//content.setTypePrefixedName("cm:allegato");
		//content.setParentAssocTypePrefixedName("cm:contains");		
		content.setEncoding(UTF_8);
		
		log.debug(logprefix + " END");
		return ecmengineDelegate.retrieveContentData(node, content, operationContext);
	}
	
	@Override
	public byte[] getBytes(String uuid) throws ReadException, InvalidCredentialsException, NoSuchNodeException, EcmEngineTransactionException, PermissionDeniedException, InvalidParameterException, RemoteException {
		final String methodName = "getBytes";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN, uuid="+uuid);
		
		
		Node node = new Node(uuid);
		Content content = new Content();
		//content.setModelPrefixedName("cm:contentmodel");
		content.setContentPropertyPrefixedName("cm:content");
		//content.setTypePrefixedName("cm:allegato");
		//content.setParentAssocTypePrefixedName("cm:contains");	
		content.setEncoding(UTF_8);
		
		log.debug(logprefix + " END");
		
		return ecmengineDelegate.retrieveContentData(node, content, operationContext);
	}



	@Override
	public void deleteNode(String uuid) throws InvalidCredentialsException, DeleteException, NoSuchNodeException, EcmEngineTransactionException, PermissionDeniedException, InvalidParameterException, RemoteException {
		final String methodName = "deleteNode";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.debug(logprefix + " BEGIN, uuid="+uuid);
		
		Node nodo = new Node();
		nodo.setUid(uuid);
		
		long start=System.currentTimeMillis();
		
		ecmengineDelegate.deleteContent(nodo, operationContext);
		
		log.debug(logprefix + " nodo cancellato in ms: "+(System.currentTimeMillis()-start));
		log.debug(logprefix + " END");
	}



	@Override
	public String getUidNode(String nodeName, String parentPath) throws InvalidCredentialsException,
	   NoDataExtractedException, EcmEngineTransactionException, SearchException, InvalidParameterException,
	   RemoteException, MalformedURLException, ServiceException {
	
		final String methodName = "getUidNode";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		log.debug(logprefix + " nodeName=["+nodeName+"]");
		log.debug(logprefix + " parentPath=["+parentPath+"]");
		
		String uuidNodo = null;
		SearchParams searchParams = new SearchParams();
		searchParams.setXPathQuery(parentPath+"/cm:"+nodeName);
		
		try {
			
			uuidNodo = getEcmengineDelegate().nodeExists(searchParams, operationContext);
			
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException e="+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (NoDataExtractedException e) {
			log.error(logprefix + " NoDataExtractedException e="+e.getMessage());
			throw new NoDataExtractedException(e.getMessage());
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
		log.debug(logprefix + " uuidNodo = ["+uuidNodo+"]");

		log.debug(logprefix + " END");
		return uuidNodo;
	}



	@Override
	public String createFolder(String uuidPadre, String nomeFolder) throws InvalidCredentialsException, NoSuchNodeException, 
	EcmEngineTransactionException, InsertException, PermissionDeniedException, InvalidParameterException, RemoteException{ 
			
		final String methodName = "createFolder";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		log.debug(logprefix + " uuidPadre=["+uuidPadre+"]");
		log.debug(logprefix + " nomeFolder=["+nomeFolder+"]");
		
		String uidNodo = "";

		Node parentNode = new Node();
		parentNode.setUid(uuidPadre);
		
		log.debug(logprefix + " impostato nodo padre");
		
		Content folder = new Content();
		folder.setTypePrefixedName("cm:folder");
		folder.setModelPrefixedName("cm:contentmodel");
		folder.setParentAssocTypePrefixedName("cm:contains");
		folder.setPrefixedName("cm:" + nomeFolder);
		
		log.debug(logprefix + " impostata FOLDER");
		
		Property [] props = new Property[1];
		props[0] = new Property();
		props[0].setPrefixedName("cm:name");
		props[0].setDataType("d:text");
		props[0].setMultivalue(false);
		props[0].setValues(new String [] { nomeFolder });
		folder.setProperties(props);
		
		log.debug(logprefix + " impostata PROPERTY");
		
		Node folderNode=ecmengineDelegate.createContent(parentNode, folder, operationContext );
		
		if(folderNode!=null){
			uidNodo = folderNode.getUid();
			log.debug(logprefix + " nodo creato, UID="+uidNodo);
		} else{
			log.debug(logprefix + " nodo NON creato"); 
		}
		
		log.debug(logprefix + " END");
		return uidNodo;
	}



	@Override
	public Node insertDocumento(DocumentoIndex docInx, boolean allegato) throws InvalidCredentialsException, NoSuchNodeException, EcmEngineTransactionException,
	InsertException, PermissionDeniedException, InvalidParameterException, RemoteException, MalformedURLException, ServiceException{
		final String methodName = "insertDocumento";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		Node allegatoNode = null;
		
		long start=System.currentTimeMillis();
		
		Content content = null;
		if (allegato) 
			content = createContentAllegato(docInx);
		else
			content = createContentDomanda(docInx);
		Node nuovoNodo = new Node();
		nuovoNodo.setUid(docInx.getUidNode());
		
		try {
			
			allegatoNode = getEcmengineDelegate().createContent(nuovoNodo, content, operationContext);
			log.info("node with  uid: "+allegatoNode.getUid()+" creato; total time ms: "+(System.currentTimeMillis()-start));
			
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException "+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (NoSuchNodeException e) {
			log.error(logprefix + " NoSuchNodeException "+e.getMessage());
			throw new NoSuchNodeException(e.getMessage());
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + " EcmEngineTransactionException "+e.getMessage());
			throw new EcmEngineTransactionException(e.getMessage());
		} catch (InsertException e) {
			log.error(logprefix + " InsertException "+e.getMessage());
			e.printStackTrace();
			
			// TODO : file esistente in quel nodo.....
			
			throw new InsertException(e.getMessage());
		} catch (PermissionDeniedException e) {
			log.error(logprefix + " PermissionDeniedException "+e.getMessage());
			throw new PermissionDeniedException(e.getMessage());
		} catch (InvalidParameterException e) {
			log.error(logprefix + " InvalidParameterException "+e.getMessage());
			throw new InvalidParameterException(e.getMessage());
		} catch (RemoteException e) {
			log.error(logprefix + " RemoteException "+e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (MalformedURLException e) {
			log.error(logprefix + " MalformedURLException "+e.getMessage());
			throw new MalformedURLException(e.getMessage());
		} catch (ServiceException e) {
			log.error(logprefix + " ServiceException "+e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		
		log.debug(logprefix + " END");
		return allegatoNode;
	}


	private Content createContentAllegato(DocumentoIndex dto) {
		final String methodName = "createContentAllegato";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		Content content = new Content();
		
		String tipoFile = dto.getFilename().substring(dto.getFilename().lastIndexOf("."), dto.getFilename().length());
		log.debug(logprefix + " tipoFile="+tipoFile);

		content.setPrefixedName(APP_PREFIX + dto.getFilename());
		content.setParentAssocTypePrefixedName("cm:contains");
		content.setModelPrefixedName("findom:model");
		content.setTypePrefixedName("findom:allegato");
		content.setContentPropertyPrefixedName("cm:content");

		//content.setMimeType(tipoFile);
		//content.setMimeType(APPLICATION_PDF);
		content.setMimeType(dto.getContentType());
		log.debug(logprefix + " mime type:::::::::::::::::::: " + dto.getContentType());
		
		content.setEncoding(UTF_8);
		content.setContent(dto.getBytes());

		Property props[] = createPropertiesAllegato(dto);
		log.debug(logprefix + " properties settate");
		content.setProperties(props);
		
		log.debug(logprefix + " END");
		return content;
	}
	
	private Content createContentDomanda(DocumentoIndex dto) {
		final String methodName = "createContentDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		Content content = new Content();
		
		String tipoFile = dto.getFilename().substring(dto.getFilename().lastIndexOf("."), dto.getFilename().length());
		log.debug(logprefix + " tipoFile="+tipoFile);

		content.setPrefixedName(APP_PREFIX + dto.getFilename());
		content.setParentAssocTypePrefixedName("cm:contains");
		content.setModelPrefixedName("findom:model");
		content.setTypePrefixedName("findom:domanda");
		content.setContentPropertyPrefixedName("cm:content");

		//content.setMimeType(APPLICATION_PKCS7_MIME);
		content.setMimeType(dto.getContentType());
		content.setContentPropertyPrefixedName("findom:fileFirmato");
		
		//content.setMimeType(tipoFile);
		content.setEncoding(UTF_8);
		content.setContent(dto.getBytes());
		
		//Aspect[] aspects = getAspectsFileFirmato();
		//content.setAspects(aspects);

		Property props[] = createPropertiesDomanda(dto);
		log.debug(logprefix + " properties settate");
		content.setProperties(props);
		
		log.debug(logprefix + " END");
		return content;
	}
		
	private List<Property> createPropertiesDocumento(DocumentoIndex dto) {
		final String methodName = "createPropertiesDocumento";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		List<Property> properties = new ArrayList<Property>();
		
		Property codiceBando = new Property();
		codiceBando.setPrefixedName("findom:codiceBando");
		codiceBando.setDataType("d:text");		
		codiceBando.setValues(new String[] { dto.getCodiceBando() });
		properties.add(codiceBando);
		
		Property descrizioneBando = new Property();
		descrizioneBando.setPrefixedName("findom:descrizioneBando");
		descrizioneBando.setDataType("d:text");		
		descrizioneBando.setValues(new String[] { dto.getDescrizioneBando() });
		properties.add(descrizioneBando);
		
		Property idBando = new Property();
		idBando.setPrefixedName("findom:idBando");
		idBando.setDataType("d:long");		
		idBando.setValues(new String[] { dto.getIdBando().toString() });
		properties.add(idBando);
		
		Property idDomanda = new Property();
		idDomanda.setPrefixedName("findom:idDomanda");
		idDomanda.setDataType("d:long");		
		idDomanda.setValues(new String[] { dto.getIdDomanda().toString() });
		properties.add(idDomanda);
		
		Property CFUtenteUploader = new Property();
		CFUtenteUploader.setPrefixedName("findom:CFUtenteUploader");
		CFUtenteUploader.setDataType("d:text");
		CFUtenteUploader.setValues(new String[] { dto.getCFUtenteUploader() });
		properties.add(CFUtenteUploader);
		
		Property descrizioneUtenteUploader = new Property();
		descrizioneUtenteUploader.setPrefixedName("findom:descrizioneUtenteUploader");
		descrizioneUtenteUploader.setDataType("d:text");
		descrizioneUtenteUploader.setValues(new String[] { dto.getDescrizioneUtenteUploader() });
		properties.add(descrizioneUtenteUploader);

		Property codiceImpresaEnte = new Property();
		codiceImpresaEnte.setPrefixedName("findom:codiceImpresaEnte");
		codiceImpresaEnte.setDataType("d:text");
		codiceImpresaEnte.setValues(new String[] { dto.getCodiceImpresaEnte() });
		properties.add(codiceImpresaEnte);
		
		Property descrizioneImpresaEnte = new Property();
		descrizioneImpresaEnte.setPrefixedName("findom:descrizioneImpresaEnte");
		descrizioneImpresaEnte.setDataType("d:text");
		descrizioneImpresaEnte.setValues(new String[] { dto.getDescrizioneImpresaEnte() });
		properties.add(descrizioneImpresaEnte);
		
		Property idTipologiaBeneficiario = new Property();
		idTipologiaBeneficiario.setPrefixedName("findom:idTipologiaBeneficiario");
		idTipologiaBeneficiario.setDataType("d:long");		
		idTipologiaBeneficiario.setValues(new String[] { dto.getIdTipologiaBeneficiario().toString() });
		properties.add(idTipologiaBeneficiario);
		
		Property descrizioneBeneficiario = new Property();
		descrizioneBeneficiario.setPrefixedName("findom:descrizioneBeneficiario");
		descrizioneBeneficiario.setDataType("d:text");
		descrizioneBeneficiario.setValues(new String[] { dto.getDescrizioneBeneficiario() });
		properties.add(descrizioneBeneficiario);
		
		Property stereotipoBeneficiario = new Property();
		stereotipoBeneficiario.setPrefixedName("findom:stereotipoBeneficiario");
		stereotipoBeneficiario.setDataType("d:text");
		stereotipoBeneficiario.setValues(new String[] { dto.getStereotipoBeneficiario() });
		properties.add(stereotipoBeneficiario);
		
		log.debug(logprefix + " END");
		return properties;
		//return properties.toArray(new Property[properties.size()]);
	}
	
	private Property[] createPropertiesAllegato(DocumentoIndex dto) {
		final String methodName = "createPropertiesAllegato";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		List<Property> properties = createPropertiesDocumento(dto);

		Property idTipologiaAllegato = new Property();
		idTipologiaAllegato.setPrefixedName("findom:idTipologiaAllegato");
		idTipologiaAllegato.setDataType("d:long");		
		idTipologiaAllegato.setValues(new String[] { dto.getIdTipologiaAllegato().toString() });
		properties.add(idTipologiaAllegato);
		
		Property descrizioneTpologiaAllegato = new Property();
		descrizioneTpologiaAllegato.setPrefixedName("findom:descrizioneTipologiaAllegato");
		descrizioneTpologiaAllegato.setDataType("d:text");
		descrizioneTpologiaAllegato.setValues(new String[] { dto.getDescrizioneTipologiaAllegato() });
		properties.add(descrizioneTpologiaAllegato);
		
		log.debug(logprefix + " END");
		return properties.toArray(new Property[properties.size()]);
	}
	
	private Property[] createPropertiesDomanda(DocumentoIndex dto) {
		final String methodName = "createPropertiesAllegato";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		List<Property> properties = createPropertiesDocumento(dto);

		Property idTipologiaAllegato = new Property();
		idTipologiaAllegato.setPrefixedName("findom:documentoConAllegati");
		idTipologiaAllegato.setDataType("d:boolean");		
		idTipologiaAllegato.setValues(new String[] { "true" });
		properties.add(idTipologiaAllegato);
		
		Property tipoDocumento = new Property();
		tipoDocumento.setPrefixedName("findom:tipoDocumento");
		tipoDocumento.setDataType("d:text");
		tipoDocumento.setValues(new String[] { "SEMPLICE" });
		properties.add(tipoDocumento);
		
		Property formaDigitale = new Property();
		formaDigitale.setPrefixedName("findom:formaDigitale");
		formaDigitale.setDataType("d:text");
		formaDigitale.setValues(new String[] { "PDF" });
		properties.add(formaDigitale);
		
		log.debug(logprefix + " END");
		return properties.toArray(new Property[properties.size()]);
	}
	
	private Aspect[]  getAspectsMarcatoTemporalmente() {
		Aspect[] aspects=new Aspect[3];
		aspects[0]=new Aspect();
		aspects[0].setPrefixedName("cm:auditable" );
		aspects[1]=new Aspect();
		aspects[1].setPrefixedName("sys:referenceable" );
		aspects[2]=new Aspect();
		aspects[2].setPrefixedName("ecm-sys:modified" );
		/*aspects[3]=new Aspect();
		aspects[3].setPrefixedName("findom:marcaTemporale" );*/
		return aspects;
	}
	
	private Aspect[]  getAspectsFileFirmato() {
		Aspect[] aspects=new Aspect[3];
		aspects[0]=new Aspect();
		aspects[0].setPrefixedName("cm:auditable" );
		aspects[1]=new Aspect();
		aspects[1].setPrefixedName("sys:referenceable" );
		aspects[2]=new Aspect();
		aspects[2].setPrefixedName("ecm-sys:modified" );
		aspects[3]=new Aspect();
		aspects[3].setPrefixedName("findom:fileFirmato" );
		return aspects;
	}
	
	public void updateWithTimestampedContent(String uuidNodo,
			byte[] bytesTimestamped) throws Exception {

		final String methodName = "updateWithTimestampedContent";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		log.debug("updateWithTimestampedContent uuidNodo: " + uuidNodo);
		Node node=new Node();
		node.setUid(uuidNodo);
		Aspect[] aspects = getAspectsMarcatoTemporalmente();
		Content content=new Content();
		content.setAspects(aspects);
		log.debug("logprefixcalling updateMetadata...");
		ecmengineDelegate.updateMetadata(node, content, operationContext);
		log.debug("logprefix updateMetadata for timestamped content ok");
		content = new Content();
		content.setContentPropertyPrefixedName("findom:marcaTemporale");
		content.setMimeType(APPLICATION_TSD_MIME);

		content.setEncoding(UTF_8);
		content.setContent(bytesTimestamped);
		log.debug("calling updateContentData for timestamped content...");
		try {
		ecmengineDelegate.updateContentData(node, content,
				operationContext);
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException "+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (NoSuchNodeException e) {
			log.error(logprefix + " NoSuchNodeException "+e.getMessage());
			throw new NoSuchNodeException(e.getMessage());
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + " EcmEngineTransactionException "+e.getMessage());
			throw new EcmEngineTransactionException(e.getMessage());
		} catch (UpdateException e) {
			log.error(logprefix + " InsertException "+e.getMessage());
			e.printStackTrace();
		} catch (PermissionDeniedException e) {
			log.error(logprefix + " PermissionDeniedException "+e.getMessage());
			throw new PermissionDeniedException(e.getMessage());
		} catch (InvalidParameterException e) {
			log.error(logprefix + " InvalidParameterException "+e.getMessage());
			throw new InvalidParameterException(e.getMessage());
		} catch (RemoteException e) {
			log.error(logprefix + " RemoteException "+e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (Exception e) {
			log.error(logprefix + " Exception "+e.getMessage());
			throw new Exception(e.getMessage());
		}
		log.debug(logprefix + " END");
	}
	
	public ResultContent getContentMetaData(String uuid) throws Exception {
		final String methodName = "getContentMetaData";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		try {

			Node node = new Node(uuid);
			ResultContent rContent = ecmengineDelegate.getContentMetadata(node, operationContext);
			return rContent;
		} catch (InvalidCredentialsException e) {
			log.error(logprefix + " InvalidCredentialsException "+e.getMessage());
			throw new InvalidCredentialsException(e.getMessage());
		} catch (ReadException e) {
			log.error(logprefix + " ReadException "+e.getMessage());
			throw new ReadException(e.getMessage());
		} catch (NoSuchNodeException e) {
			log.error(logprefix + " NoSuchNodeException "+e.getMessage());
			throw new NoSuchNodeException(e.getMessage());
		} catch (EcmEngineTransactionException e) {
			log.error(logprefix + " EcmEngineTransactionException "+e.getMessage());
			throw new EcmEngineTransactionException(e.getMessage());
		} catch (PermissionDeniedException e) {
			log.error(logprefix + " PermissionDeniedException "+e.getMessage());
			throw new PermissionDeniedException(e.getMessage());
		} catch (InvalidParameterException e) {
			log.error(logprefix + " InvalidParameterException "+e.getMessage());
			throw new InvalidParameterException(e.getMessage());
		}

	}
	
	public String creaImpronta(Node node, String contentPropertyPrefixedName) throws Exception {

		final String methodName = "creaImpronta";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		try {

	         NodeInfo nodeInfo = new NodeInfo();

	         nodeInfo.setPrefixedName(contentPropertyPrefixedName);
	 		 //content.setContentPropertyPrefixedName("cm:content");

	         nodeInfo.setEnveloped(false);  // se imposti a true... esegue il calcolo dopo aver eseguito extractDocumentFromEnvelope del nodo (se contenuto del nodo firmato..)
	         DigestInfo digestInfo = new DigestInfo();
	         digestInfo.setAlgorithm("SHA-256");
	         digestInfo = ecmengineDelegate.generateDigestFromUID(node, nodeInfo, digestInfo, operationContext);

	         return digestInfo.getDigest();
	         
	    } catch (Exception e) {
	    	 
	        e.printStackTrace();
			log.error(logprefix + " Errore durante creaImpronta "+e.getMessage());
	        throw e;
	    }
		
		finally {
			log.debug(logprefix + " END");

		}

	}
		 
	public String creaImpronta(byte[] contenuto) throws Exception {

		final String methodName = "creaImpronta";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
     	try {

	         Content content = new Content();

	         content.setContent(contenuto);

	         content.setEncoding("UTF-8");

	         DigestInfo digestInfo = new DigestInfo();

	         digestInfo.setAlgorithm("SHA-256");

	         digestInfo = ecmengineDelegate.generateDigestFromContent(content, digestInfo, operationContext);
	         return digestInfo.getDigest();

	    } catch (Exception e) {
	    	 
	        e.printStackTrace();
			log.error(logprefix + " Errore durante creaImpronta "+e.getMessage());
	        throw e;
	    }
		
		finally {
			log.debug(logprefix + " END");

		}

	}
		 
	public byte[] sbustaDocumento(byte[] content) throws Exception {

		    final String methodName = "sbustaDocumento";
			final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
			log.debug(logprefix + " BEGIN");
			
			EnvelopedContent env = new EnvelopedContent();
			env.setData(content);
		    env.setStore(Boolean.FALSE);  //(se true crea nodo temp)
		    
	    	Document document;
			try {
					
					document = ecmengineDelegate.extractDocumentFromEnvelope(env, operationContext);
			    	return document.getBuffer();
			    	
		    } catch (Exception e) {
		    	 
		        e.printStackTrace();
				log.error(logprefix + " Errore durante creaImpronta "+e.getMessage());
		        throw e;
		    }
			
			finally {
				log.debug(logprefix + " END");

			}

		}
}
