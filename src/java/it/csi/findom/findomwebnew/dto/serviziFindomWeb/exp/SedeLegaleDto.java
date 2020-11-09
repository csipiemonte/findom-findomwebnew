/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class SedeLegaleDto extends CommonalityVO {

	@MapTo(target = INHERIT)
	String stato;
	@MapTo(target = INHERIT)
	String statoEstero;
	@MapTo(target = INHERIT)
	String statoEsteroDescrizione;
	@MapTo(target = INHERIT)
	String cittaEstera;
	@MapTo(target = INHERIT)
	String provincia;
	@MapTo(target = INHERIT)
	String provinciaDescrizione;
	@MapTo(target = INHERIT)
	String provinciaSigla;
	@MapTo(target = INHERIT)
	String comune;
	@MapTo(target = INHERIT)
	String comuneDescrizione;
	@MapTo(target = INHERIT)
	String indirizzo;
	@MapTo(target = INHERIT)
	String numCivico;
	@MapTo(target = INHERIT)
	String cap;
	@MapTo(target = INHERIT)
	String telefono;
	@MapTo(target = INHERIT)
	String pec;
	@MapTo(target = INHERIT)
	String email;
	@MapTo(target = INHERIT)
	String personaRifSL;
	@MapTo(target = INHERIT)
	String cellulare;
	
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getStatoEstero() {
		return statoEstero;
	}
	public void setStatoEstero(String statoEstero) {
		this.statoEstero = statoEstero;
	}
	public String getStatoEsteroDescrizione() {
		return statoEsteroDescrizione;
	}
	public void setStatoEsteroDescrizione(String statoEsteroDescrizione) {
		this.statoEsteroDescrizione = statoEsteroDescrizione;
	}
	public String getCittaEstera() {
		return cittaEstera;
	}
	public void setCittaEstera(String cittaEstera) {
		this.cittaEstera = cittaEstera;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getProvinciaDescrizione() {
		return provinciaDescrizione;
	}
	public void setProvinciaDescrizione(String provinciaDescrizione) {
		this.provinciaDescrizione = provinciaDescrizione;
	}
	public String getProvinciaSigla() {
		return provinciaSigla;
	}
	public void setProvinciaSigla(String provinciaSigla) {
		this.provinciaSigla = provinciaSigla;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getComuneDescrizione() {
		return comuneDescrizione;
	}
	public void setComuneDescrizione(String comuneDescrizione) {
		this.comuneDescrizione = comuneDescrizione;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getNumCivico() {
		return numCivico;
	}
	public void setNumCivico(String numCivico) {
		this.numCivico = numCivico;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getPec() {
		return pec;
	}
	public void setPec(String pec) {
		this.pec = pec;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPersonaRifSL() {
		return personaRifSL;
	}
	public void setPersonaRifSL(String personaRifSL) {
		this.personaRifSL = personaRifSL;
	}
	public String getCellulare() {
		return cellulare;
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
		
	
}
