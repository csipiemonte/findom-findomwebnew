/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.integration.extservices.dosign;


import it.csi.findom.findomwebnew.dto.dosign.EsitoVerificaFirma;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.RegoleVerificaFirmaDto;

import java.rmi.RemoteException;
import java.util.List;


public interface DosignDAO {
	
	public EsitoVerificaFirma verify(List<String> cfLegaleRapps, String cfDelegato, byte[] bytesSignedFile, RegoleVerificaFirmaDto regoleVerificaFirma ) throws Exception, RemoteException;
	public byte[] timeStamp(byte[] bytesSignedFile) throws Exception, RemoteException;
}
