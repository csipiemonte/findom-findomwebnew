/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.integration.extservices.index;

import it.csi.findom.findomwebnew.dto.index.DocumentoIndex;
import it.doqui.index.ecmengine.client.webservices.dto.Node;
import it.doqui.index.ecmengine.client.webservices.dto.engine.search.ResultContent;
import it.doqui.index.ecmengine.client.webservices.exception.InvalidParameterException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.EcmEngineTransactionException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.InvalidCredentialsException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoDataExtractedException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.NoSuchNodeException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.PermissionDeniedException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.DeleteException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.InsertException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.ReadException;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.search.SearchException;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public interface IndexDAO {

	public Node searchNodeRoot() throws InvalidCredentialsException, EcmEngineTransactionException, SearchException, PermissionDeniedException, InvalidParameterException, RemoteException, MalformedURLException, ServiceException;

	public String getUidNode(String nodeName, String parentPath) throws InvalidCredentialsException, NoDataExtractedException, EcmEngineTransactionException, SearchException, InvalidParameterException, RemoteException, MalformedURLException, ServiceException;

	public String createFolder(String uuidPadre, String nomeFolder) throws InvalidCredentialsException, NoSuchNodeException, EcmEngineTransactionException, InsertException, PermissionDeniedException, InvalidParameterException, RemoteException;

	public void deleteNode(String uuid) throws InvalidCredentialsException, DeleteException, NoSuchNodeException, EcmEngineTransactionException, PermissionDeniedException, InvalidParameterException, RemoteException;

	public byte[] getBytes (String uuid) throws ReadException, InvalidCredentialsException, NoSuchNodeException, EcmEngineTransactionException, PermissionDeniedException, InvalidParameterException, RemoteException;

	public byte[] getFileFirmato(String uuid) throws ReadException, InvalidCredentialsException, NoSuchNodeException, EcmEngineTransactionException, PermissionDeniedException, InvalidParameterException, RemoteException;
			
	public Node insertDocumento(DocumentoIndex docInx, boolean allegato) throws InvalidCredentialsException, NoSuchNodeException, EcmEngineTransactionException, InsertException, PermissionDeniedException, InvalidParameterException, RemoteException, MalformedURLException, ServiceException;
	
	public void updateWithTimestampedContent(String uuidNodo, byte[] bytesTimestamped) throws Exception;
	
	public ResultContent getContentMetaData(String uuid) throws Exception;

	public String creaImpronta(Node node, String contentPropertyPrefixedName) throws Exception;

	public String creaImpronta(byte[] contenuto) throws Exception;

	public byte[] sbustaDocumento(byte[] content) throws Exception;
}
