/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;

// dto della vista findom_v_doc_demat_batch
public class VistaDocDematDto implements Serializable{

	static final long serialVersionUID = 1;	
	
	private int idDomanda;
	private String tipoAllegato;
	private String flagRegionale;
	private String flagFinpiemonte; 
	private String destFileDomanda;
	
		
	//GETTERS && SETTERS	
	
	public int getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(int idDomanda) {
		this.idDomanda = idDomanda;
	}
	public String getTipoAllegato() {
		return tipoAllegato;
	}
	public void setTipoAllegato(String tipoAllegato) {
		this.tipoAllegato = tipoAllegato;
	}
	public String getFlagRegionale() {
		return flagRegionale;
	}
	public void setFlagRegionale(String flagRegionale) {
		this.flagRegionale = flagRegionale;
	}
	public String getFlagFinpiemonte() {
		return flagFinpiemonte;
	}
	public void setFlagFinpiemonte(String flagFinpiemonte) {
		this.flagFinpiemonte = flagFinpiemonte;
	}
	public String getDestFileDomanda() {
		return destFileDomanda;
	}
	public void setDestFileDomanda(String destFileDomanda) {
		this.destFileDomanda = destFileDomanda;
	}
	
}
