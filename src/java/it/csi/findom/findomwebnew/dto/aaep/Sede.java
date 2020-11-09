/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;
import java.util.List;

public class Sede implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<AttivitaEconomica> ateco;
    private String codCausaleCessazione;
    private String codSettore;
    private Contatti contatti;
    private String dataAggiornam;
    private String dataCessazione;
    private String dataInizioAttivita;
    private String dataInizioValidita;
    private String dataNumeroDipendenti;
    private String denominazione;
    private String descrCausaleCessazione;
    private String descrSettore;
    private String descrTipoSede;
    private String codiceAteco2007;
    private String descrizioneAteco2007;
    private it.csi.aaep.aaeporch.business.TipologiaFonte fonteDato; // le classi ENUM sono serializzabili
    private String idAzienda;
    private String idSede;
    private String numeroDipendenti;
    private String riferimento;
    private it.csi.aaep.aaeporch.business.TipologiaSede tipoSede; // le classi ENUM sono serializzabili
    private Ubicazione ubicazione;
    
	public List<AttivitaEconomica> getAteco() {
		return ateco;
	}
	public void setAteco(List<AttivitaEconomica> ateco) {
		this.ateco = ateco;
	}
	public String getCodCausaleCessazione() {
		return codCausaleCessazione;
	}
	public void setCodCausaleCessazione(String codCausaleCessazione) {
		this.codCausaleCessazione = codCausaleCessazione;
	}
	public String getCodSettore() {
		return codSettore;
	}
	public void setCodSettore(String codSettore) {
		this.codSettore = codSettore;
	}
	public Contatti getContatti() {
		return contatti;
	}
	public void setContatti(Contatti contatti) {
		this.contatti = contatti;
	}
	public String getDataAggiornam() {
		return dataAggiornam;
	}
	public void setDataAggiornam(String dataAggiornam) {
		this.dataAggiornam = dataAggiornam;
	}
	public String getDataCessazione() {
		return dataCessazione;
	}
	public void setDataCessazione(String dataCessazione) {
		this.dataCessazione = dataCessazione;
	}
	public String getDataInizioAttivita() {
		return dataInizioAttivita;
	}
	public void setDataInizioAttivita(String dataInizioAttivita) {
		this.dataInizioAttivita = dataInizioAttivita;
	}
	public String getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(String dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public String getDataNumeroDipendenti() {
		return dataNumeroDipendenti;
	}
	public void setDataNumeroDipendenti(String dataNumeroDipendenti) {
		this.dataNumeroDipendenti = dataNumeroDipendenti;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getDescrCausaleCessazione() {
		return descrCausaleCessazione;
	}
	public void setDescrCausaleCessazione(String descrCausaleCessazione) {
		this.descrCausaleCessazione = descrCausaleCessazione;
	}
	public String getDescrSettore() {
		return descrSettore;
	}
	public void setDescrSettore(String descrSettore) {
		this.descrSettore = descrSettore;
	}
	public String getDescrTipoSede() {
		return descrTipoSede;
	}
	public void setDescrTipoSede(String descrTipoSede) {
		this.descrTipoSede = descrTipoSede;
	}
	public String getIdAzienda() {
		return idAzienda;
	}
	public void setIdAzienda(String idAzienda) {
		this.idAzienda = idAzienda;
	}
	public String getIdSede() {
		return idSede;
	}
	public void setIdSede(String idSede) {
		this.idSede = idSede;
	}
	public String getNumeroDipendenti() {
		return numeroDipendenti;
	}
	public void setNumeroDipendenti(String numeroDipendenti) {
		this.numeroDipendenti = numeroDipendenti;
	}
	public String getRiferimento() {
		return riferimento;
	}
	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}
	public Ubicazione getUbicazione() {
		return ubicazione;
	}
	public void setUbicazione(Ubicazione ubicazione) {
		this.ubicazione = ubicazione;
	}
	public it.csi.aaep.aaeporch.business.TipologiaFonte getFonteDato() {
		return fonteDato;
	}
	public void setFonteDato(it.csi.aaep.aaeporch.business.TipologiaFonte fonteDato) {
		this.fonteDato = fonteDato;
	}
	public it.csi.aaep.aaeporch.business.TipologiaSede getTipoSede() {
		return tipoSede;
	}
	public void setTipoSede(it.csi.aaep.aaeporch.business.TipologiaSede tipoSede) {
		this.tipoSede = tipoSede;
	}
	public String getDescrizioneAteco2007() {
		return descrizioneAteco2007;
	}
	public void setDescrizioneAteco2007(String descrizioneAteco2007) {
		this.descrizioneAteco2007 = descrizioneAteco2007;
	}
	public String getCodiceAteco2007() {
		return codiceAteco2007;
	}
	public void setCodiceAteco2007(String codiceAteco2007) {
		this.codiceAteco2007 = codiceAteco2007;
	}

}
