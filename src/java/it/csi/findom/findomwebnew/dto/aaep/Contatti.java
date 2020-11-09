/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;

public class Contatti implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String email;
    private String fax;
    private String numeroVerde;
    private String sitoWeb;
    private String telefono;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getNumeroVerde() {
		return numeroVerde;
	}
	public void setNumeroVerde(String numeroVerde) {
		this.numeroVerde = numeroVerde;
	}
	public String getSitoWeb() {
		return sitoWeb;
	}
	public void setSitoWeb(String sitoWeb) {
		this.sitoWeb = sitoWeb;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
