/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;

public class DettagliAlboArtigiano implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String dataDeliberaIscrizione;
	private String descrIterIscrizione;
	private String flgIterIscrizione;
	private String numeroIscrizione;
	private String provinciaIscrizione;
	
	public String getDataDeliberaIscrizione() {
		return dataDeliberaIscrizione;
	}
	public void setDataDeliberaIscrizione(String dataDeliberaIscrizione) {
		this.dataDeliberaIscrizione = dataDeliberaIscrizione;
	}
	public String getDescrIterIscrizione() {
		return descrIterIscrizione;
	}
	public void setDescrIterIscrizione(String descrIterIscrizione) {
		this.descrIterIscrizione = descrIterIscrizione;
	}
	public String getFlgIterIscrizione() {
		return flgIterIscrizione;
	}
	public void setFlgIterIscrizione(String flgIterIscrizione) {
		this.flgIterIscrizione = flgIterIscrizione;
	}
	public String getNumeroIscrizione() {
		return numeroIscrizione;
	}
	public void setNumeroIscrizione(String numeroIscrizione) {
		this.numeroIscrizione = numeroIscrizione;
	}
	public String getProvinciaIscrizione() {
		return provinciaIscrizione;
	}
	public void setProvinciaIscrizione(String provinciaIscrizione) {
		this.provinciaIscrizione = provinciaIscrizione;
	}
}
