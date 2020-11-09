/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;

public class CapofilaAcronimoPartnerDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	String idCapofilaAcronimo;	
	String idPartner;	
	String idDomandaPartner;	
	String dtAttivazione;	
	String dtDisattivazione;

	public String getIdCapofilaAcronimo() {
		return idCapofilaAcronimo;
	}

	public void setIdCapofilaAcronimo(String idCapofilaAcronimo) {
		this.idCapofilaAcronimo = idCapofilaAcronimo;
	}

	public String getIdPartner() {
		return idPartner;
	}

	public void setIdPartner(String idPartner) {
		this.idPartner = idPartner;
	}

	public String getIdDomandaPartner() {
		return idDomandaPartner;
	}

	public void setIdDomandaPartner(String idDomandaPartner) {
		this.idDomandaPartner = idDomandaPartner;
	}

	public String getDtAttivazione() {
		return dtAttivazione;
	}

	public void setDtAttivazione(String dtAttivazione) {
		this.dtAttivazione = dtAttivazione;
	}

	public String getDtDisattivazione() {
		return dtDisattivazione;
	}

	public void setDtDisattivazione(String dtDisattivazione) {
		this.dtDisattivazione = dtDisattivazione;
	}
	
}
