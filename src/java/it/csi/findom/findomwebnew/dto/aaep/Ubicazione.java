/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;

public class Ubicazione implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String altreIndicazioni;
    private String cap;
    private String codCatastale;
    private String codISTATComune;
    private String codNazione;
    private String codQuartiere;
    private String coordinataX;
    private String coordinataY;
    private String descrComune;
    private String descrizioneEstesa;
    private String geometriaGJSON;
    private String indirizzo;
    private String nomeNazione;
    private String numeroCivico;
    private String siglaProvincia;
    private String toponimo;
    
	public String getAltreIndicazioni() {
		return altreIndicazioni;
	}
	public void setAltreIndicazioni(String altreIndicazioni) {
		this.altreIndicazioni = altreIndicazioni;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getCodCatastale() {
		return codCatastale;
	}
	public void setCodCatastale(String codCatastale) {
		this.codCatastale = codCatastale;
	}
	public String getCodISTATComune() {
		return codISTATComune;
	}
	public void setCodISTATComune(String codISTATComune) {
		this.codISTATComune = codISTATComune;
	}
	public String getCodNazione() {
		return codNazione;
	}
	public void setCodNazione(String codNazione) {
		this.codNazione = codNazione;
	}
	public String getCodQuartiere() {
		return codQuartiere;
	}
	public void setCodQuartiere(String codQuartiere) {
		this.codQuartiere = codQuartiere;
	}
	public String getCoordinataX() {
		return coordinataX;
	}
	public void setCoordinataX(String coordinataX) {
		this.coordinataX = coordinataX;
	}
	public String getCoordinataY() {
		return coordinataY;
	}
	public void setCoordinataY(String coordinataY) {
		this.coordinataY = coordinataY;
	}
	public String getDescrComune() {
		return descrComune;
	}
	public void setDescrComune(String descrComune) {
		this.descrComune = descrComune;
	}
	public String getDescrizioneEstesa() {
		return descrizioneEstesa;
	}
	public void setDescrizioneEstesa(String descrizioneEstesa) {
		this.descrizioneEstesa = descrizioneEstesa;
	}
	public String getGeometriaGJSON() {
		return geometriaGJSON;
	}
	public void setGeometriaGJSON(String geometriaGJSON) {
		this.geometriaGJSON = geometriaGJSON;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getNomeNazione() {
		return nomeNazione;
	}
	public void setNomeNazione(String nomeNazione) {
		this.nomeNazione = nomeNazione;
	}
	public String getNumeroCivico() {
		return numeroCivico;
	}
	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}
	public String getToponimo() {
		return toponimo;
	}
	public void setToponimo(String toponimo) {
		this.toponimo = toponimo;
	}
    
  }
