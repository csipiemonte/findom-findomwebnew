/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;

public class CapofilaAcronimoDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	String idCapofilaAcronimo;  
	String idDomanda;
	String idAcronimoBando;
	String dtAttivazione;
	String dtDisattivazione;

	public String getIdCapofilaAcronimo() {
		return idCapofilaAcronimo;
	}

	public void setIdCapofilaAcronimo(String idCapofilaAcronimo) {
		this.idCapofilaAcronimo = idCapofilaAcronimo;
	}

	public String getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(String idDomanda) {
		this.idDomanda = idDomanda;
	}

	public String getIdAcronimoBando() {
		return idAcronimoBando;
	}

	public void setIdAcronimoBando(String idAcronimoBando) {
		this.idAcronimoBando = idAcronimoBando;
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
