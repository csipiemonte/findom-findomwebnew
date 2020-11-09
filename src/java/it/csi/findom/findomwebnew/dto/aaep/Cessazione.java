/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;

public class Cessazione implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Boolean cessazione;
	private String codCausaleCessazione;
	private String dataCessazione;
	private String dataDenunciaCessazione;
	private String descrCausaleCessazione;
	
	public Boolean getCessazione() {
		return cessazione;
	}
	public void setCessazione(Boolean cessazione) {
		this.cessazione = cessazione;
	}
	public String getCodCausaleCessazione() {
		return codCausaleCessazione;
	}
	public void setCodCausaleCessazione(String codCausaleCessazione) {
		this.codCausaleCessazione = codCausaleCessazione;
	}
	public String getDataCessazione() {
		return dataCessazione;
	}
	public void setDataCessazione(String dataCessazione) {
		this.dataCessazione = dataCessazione;
	}
	public String getDataDenunciaCessazione() {
		return dataDenunciaCessazione;
	}
	public void setDataDenunciaCessazione(String dataDenunciaCessazione) {
		this.dataDenunciaCessazione = dataDenunciaCessazione;
	}
	public String getDescrCausaleCessazione() {
		return descrCausaleCessazione;
	}
	public void setDescrCausaleCessazione(String descrCausaleCessazione) {
		this.descrCausaleCessazione = descrCausaleCessazione;
	}
}
