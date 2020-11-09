/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;
import java.util.List;

public class Persona  implements Serializable{

	private static final long serialVersionUID = 1L;

	private String codiceFiscale;
    private String cognome;
    private String descrTipoPersona;
    private String idAzienda;
    private String idFonteDato;
    private String idPersona;
    private List<Carica> listaCariche;
    private String nome;
    private String tipoPersona;
    
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getDescrTipoPersona() {
		return descrTipoPersona;
	}
	public void setDescrTipoPersona(String descrTipoPersona) {
		this.descrTipoPersona = descrTipoPersona;
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
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public List<Carica> getListaCariche() {
		return listaCariche;
	}
	public void setListaCariche(List<Carica> listaCariche) {
		this.listaCariche = listaCariche;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
    
}
