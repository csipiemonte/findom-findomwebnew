/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;
import java.util.Date;

public class DocumentiIntegrativiDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nomeFile;
	private Long idDocumentoIndex;
	private Long idStatoDocumentoIndex;
	private String numProtocollo;
	private Date dtProtocollo;
	private String uuidNodo;

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public Long getIdDocumentoIndex() {
		return idDocumentoIndex;
	}

	public void setIdDocumentoIndex(Long idDocumentoIndex) {
		this.idDocumentoIndex = idDocumentoIndex;
	}

	public Long getIdStatoDocumentoIndex() {
		return idStatoDocumentoIndex;
	}

	public void setIdStatoDocumentoIndex(Long idStatoDocumentoIndex) {
		this.idStatoDocumentoIndex = idStatoDocumentoIndex;
	}

	public String getNumProtocollo() {
		return numProtocollo;
	}

	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}

	public Date getDtProtocollo() {
		return dtProtocollo;
	}

	public void setDtProtocollo(Date dtProtocollo) {
		this.dtProtocollo = dtProtocollo;
	}

	public String getUuidNodo() {
		return uuidNodo;
	}

	public void setUuidNodo(String uuidNodo) {
		this.uuidNodo = uuidNodo;
	}

}
