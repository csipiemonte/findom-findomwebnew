/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp;

import java.io.Serializable;

public class OperatorePresentatoreDto implements Serializable {

	private static final long serialVersionUID = -7843699216089254766L;

	private String denominazione;
	private String codiceFiscale;
	private String descrizioneFormaGiuridica;
	private String codiceFormaGiuridica;
	private String partitaIva;
	private String indirizzoPec;
	private String idFormaGiuridica;
	private String codicePrevalenteAteco2007;
	private String descrizioneAteco2007;
	private String idAteco2007;
	private String idAttivitaEconomica;
	private String codiceAttivitaEconomica;
	private String descrizioneAttivitaEconomica;	
	private String idStato;
	private String descrStato;
		
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getDescrizioneFormaGiuridica() {
		return descrizioneFormaGiuridica;
	}
	public void setDescrizioneFormaGiuridica(String descrizioneFormaGiuridica) {
		this.descrizioneFormaGiuridica = descrizioneFormaGiuridica;
	}
	public String getCodiceFormaGiuridica() {
		return codiceFormaGiuridica;
	}
	public void setCodiceFormaGiuridica(String codiceFormaGiuridica) {
		this.codiceFormaGiuridica = codiceFormaGiuridica;
	}
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	public String getIndirizzoPec() {
		return indirizzoPec;
	}
	public void setIndirizzoPec(String indirizzoPec) {
		this.indirizzoPec = indirizzoPec;
	}
	public String getIdFormaGiuridica() {
		return idFormaGiuridica;
	}
	public void setIdFormaGiuridica(String idFormaGiuridica) {
		this.idFormaGiuridica = idFormaGiuridica;
	}
	public String getCodicePrevalenteAteco2007() {
		return codicePrevalenteAteco2007;
	}
	public void setCodicePrevalenteAteco2007(String codicePrevalenteAteco2007) {
		this.codicePrevalenteAteco2007 = codicePrevalenteAteco2007;
	}
	public String getDescrizioneAteco2007() {
		return descrizioneAteco2007;
	}
	public void setDescrizioneAteco2007(String descrizioneAteco2007) {
		this.descrizioneAteco2007 = descrizioneAteco2007;
	}
	public String getIdAteco2007() {
		return idAteco2007;
	}
	public void setIdAteco2007(String idAteco2007) {
		this.idAteco2007 = idAteco2007;
	}
	public String getIdAttivitaEconomica() {
		return idAttivitaEconomica;
	}
	public void setIdAttivitaEconomica(String idAttivitaEconomica) {
		this.idAttivitaEconomica = idAttivitaEconomica;
	}
	public String getCodiceAttivitaEconomica() {
		return codiceAttivitaEconomica;
	}
	public void setCodiceAttivitaEconomica(String codiceAttivitaEconomica) {
		this.codiceAttivitaEconomica = codiceAttivitaEconomica;
	}
	public String getDescrizioneAttivitaEconomica() {
		return descrizioneAttivitaEconomica;
	}
	public void setDescrizioneAttivitaEconomica(String descrizioneAttivitaEconomica) {
		this.descrizioneAttivitaEconomica = descrizioneAttivitaEconomica;
	}	
	public String getIdStato() {
		return idStato;
	}
	public void setIdStato(String idStato) {
		this.idStato = idStato;
	}
	public String getDescrStato() {
		return descrStato;
	}
	public void setDescrStato(String descrStato) {
		this.descrStato = descrStato;
	}	

}
