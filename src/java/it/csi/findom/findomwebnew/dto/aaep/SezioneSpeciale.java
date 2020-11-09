/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;

public class SezioneSpeciale implements Serializable{

	private static final long serialVersionUID = 1L;

	private String codAlbo;
    private String codSezione;
    private String codiceSezSpec;
    private String dataFine;
    private String dataInizio;
    private String flColtDir;
    private String idAzienda;
    private String idFonteDato;
    private String idSezioneSpeciale;
    
	public String getCodAlbo() {
		return codAlbo;
	}
	public void setCodAlbo(String codAlbo) {
		this.codAlbo = codAlbo;
	}
	public String getCodSezione() {
		return codSezione;
	}
	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
	}
	public String getCodiceSezSpec() {
		return codiceSezSpec;
	}
	public void setCodiceSezSpec(String codiceSezSpec) {
		this.codiceSezSpec = codiceSezSpec;
	}
	public String getDataFine() {
		return dataFine;
	}
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	public String getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}
	public String getFlColtDir() {
		return flColtDir;
	}
	public void setFlColtDir(String flColtDir) {
		this.flColtDir = flColtDir;
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
	public String getIdSezioneSpeciale() {
		return idSezioneSpeciale;
	}
	public void setIdSezioneSpeciale(String idSezioneSpeciale) {
		this.idSezioneSpeciale = idSezioneSpeciale;
	}
}
