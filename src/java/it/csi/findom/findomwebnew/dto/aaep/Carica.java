/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;

public class Carica  implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private String codCarica;
    private String codDurataCarica;
    private String codFiscaleAzienda;
    private String codFiscalePersona;
    private String dataFineCarica;
    private String dataInizioCarica;
    private String dataPresentazCarica;
    private String descrAzienda;
    private String descrCarica;
    private String descrDurataCarica;
    private String flagRappresentanteLegale;
    private String idAzienda;
    private String idFonteDato;
    private String numAnniEsercCarica;
    private String progrCarica;
    private String progrPersona;
    
	public String getCodCarica() {
		return codCarica;
	}
	public void setCodCarica(String codCarica) {
		this.codCarica = codCarica;
	}
	public String getCodDurataCarica() {
		return codDurataCarica;
	}
	public void setCodDurataCarica(String codDurataCarica) {
		this.codDurataCarica = codDurataCarica;
	}
	public String getCodFiscaleAzienda() {
		return codFiscaleAzienda;
	}
	public void setCodFiscaleAzienda(String codFiscaleAzienda) {
		this.codFiscaleAzienda = codFiscaleAzienda;
	}
	public String getCodFiscalePersona() {
		return codFiscalePersona;
	}
	public void setCodFiscalePersona(String codFiscalePersona) {
		this.codFiscalePersona = codFiscalePersona;
	}
	public String getDataFineCarica() {
		return dataFineCarica;
	}
	public void setDataFineCarica(String dataFineCarica) {
		this.dataFineCarica = dataFineCarica;
	}
	public String getDataInizioCarica() {
		return dataInizioCarica;
	}
	public void setDataInizioCarica(String dataInizioCarica) {
		this.dataInizioCarica = dataInizioCarica;
	}
	public String getDataPresentazCarica() {
		return dataPresentazCarica;
	}
	public void setDataPresentazCarica(String dataPresentazCarica) {
		this.dataPresentazCarica = dataPresentazCarica;
	}
	public String getDescrAzienda() {
		return descrAzienda;
	}
	public void setDescrAzienda(String descrAzienda) {
		this.descrAzienda = descrAzienda;
	}
	public String getDescrCarica() {
		return descrCarica;
	}
	public void setDescrCarica(String descrCarica) {
		this.descrCarica = descrCarica;
	}
	public String getDescrDurataCarica() {
		return descrDurataCarica;
	}
	public void setDescrDurataCarica(String descrDurataCarica) {
		this.descrDurataCarica = descrDurataCarica;
	}
	public String getFlagRappresentanteLegale() {
		return flagRappresentanteLegale;
	}
	public void setFlagRappresentanteLegale(String flagRappresentanteLegale) {
		this.flagRappresentanteLegale = flagRappresentanteLegale;
	}
	public String getIdAzienda() {
		return idAzienda;
	}
	public void setIdAzienda(String idAzienda) {
		this.idAzienda = idAzienda;
	}
	public String getIdFonteDato() {
		return idFonteDato;
	}
	public void setIdFonteDato(String idFonteDato) {
		this.idFonteDato = idFonteDato;
	}
	public String getNumAnniEsercCarica() {
		return numAnniEsercCarica;
	}
	public void setNumAnniEsercCarica(String numAnniEsercCarica) {
		this.numAnniEsercCarica = numAnniEsercCarica;
	}
	public String getProgrCarica() {
		return progrCarica;
	}
	public void setProgrCarica(String progrCarica) {
		this.progrCarica = progrCarica;
	}
	public String getProgrPersona() {
		return progrPersona;
	}
	public void setProgrPersona(String progrPersona) {
		this.progrPersona = progrPersona;
	}

}
