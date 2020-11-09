/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;

public class FindomTBandiDirezioneDto implements Serializable {

	static final long serialVersionUID = 1;

	private Long idBando;
	private String descrizioneBando;
	private String descBreveBando;
	private Long idSettore;
	private String descrizioneSettore;
	private Long idDirezione;
	private String descrizioneDirezione;

	public String getDescrizioneDirezione() {
		return descrizioneDirezione;
	}

	public void setDescrizioneDirezione(String descrizioneDirezione) {
		this.descrizioneDirezione = descrizioneDirezione;
	}

	public Long getIdBando() {
		return idBando;
	}

	public void setIdBando(Long idBando) {
		this.idBando = idBando;
	}

	public String getDescrizioneBando() {
		return descrizioneBando;
	}

	public void setDescrizioneBando(String descrizioneBando) {
		this.descrizioneBando = descrizioneBando;
	}

	public String getDescBreveBando() {
		return descBreveBando;
	}

	public void setDescBreveBando(String descBreveBando) {
		this.descBreveBando = descBreveBando;
	}

	public Long getIdSettore() {
		return idSettore;
	}

	public void setIdSettore(Long idSettore) {
		this.idSettore = idSettore;
	}

	public String getDescrizioneSettore() {
		return descrizioneSettore;
	}

	public void setDescrizioneSettore(String descrizioneSettore) {
		this.descrizioneSettore = descrizioneSettore;
	}

	public Long getIdDirezione() {
		return idDirezione;
	}

	public void setIdDirezione(Long idDirezione) {
		this.idDirezione = idDirezione;
	}

}
