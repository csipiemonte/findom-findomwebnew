/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;

public class ShellSoggettiDto implements Serializable {

	static final long serialVersionUID = 1;
	
	// tabella SHELL_T_SOGGETTI
	
	private int idSoggetto; // impresa o ente
	private String codiceFiscale;
	private String denominazione;
	private int idFormaGiuridica ;
	private String cognome;
	private String nome;
	
	//GETTERS && SETTERS
	public int getIdSoggetto() {
		return idSoggetto;
	}
	public void setIdSoggetto(int idSoggetto) {
		this.idSoggetto = idSoggetto;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public int getIdFormaGiuridica() {
		return idFormaGiuridica;
	}
	public void setIdFormaGiuridica(int idFormaGiuridica) {
		this.idFormaGiuridica = idFormaGiuridica;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

}
