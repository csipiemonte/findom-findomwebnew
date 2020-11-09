/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;

public class ProcConcors  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String codAtto;
    private String codLiquidazione;
    private String dataAperturaProc;
    private String dataChiusuraLiquidaz;
    private String dataEsecConcordPrevent;
    private String dataFineLiquidaz;
    private String dataRegistroAtto;
    private String dataRevocalLiquidaz;
    private String descIndicatEsecutAtto;
    private String descrAltreIndicazioni;
    private String descrCodAtto;
    private String descrNotaio;
    private String descrTribunale;
    private String idAAEPAzienda;
    private String idAAEPFonteDato;
    private String localRegistroAtto;
    private String numRestistrAtto;
    private String progrLiquidazione;
    private String siglaProvRegAtto;
	
    public String getCodAtto() {
		return codAtto;
	}
	public void setCodAtto(String codAtto) {
		this.codAtto = codAtto;
	}
	public String getCodLiquidazione() {
		return codLiquidazione;
	}
	public void setCodLiquidazione(String codLiquidazione) {
		this.codLiquidazione = codLiquidazione;
	}
	public String getDataAperturaProc() {
		return dataAperturaProc;
	}
	public void setDataAperturaProc(String dataAperturaProc) {
		this.dataAperturaProc = dataAperturaProc;
	}
	public String getDataChiusuraLiquidaz() {
		return dataChiusuraLiquidaz;
	}
	public void setDataChiusuraLiquidaz(String dataChiusuraLiquidaz) {
		this.dataChiusuraLiquidaz = dataChiusuraLiquidaz;
	}
	public String getDataEsecConcordPrevent() {
		return dataEsecConcordPrevent;
	}
	public void setDataEsecConcordPrevent(String dataEsecConcordPrevent) {
		this.dataEsecConcordPrevent = dataEsecConcordPrevent;
	}
	public String getDataFineLiquidaz() {
		return dataFineLiquidaz;
	}
	public void setDataFineLiquidaz(String dataFineLiquidaz) {
		this.dataFineLiquidaz = dataFineLiquidaz;
	}
	public String getDataRegistroAtto() {
		return dataRegistroAtto;
	}
	public void setDataRegistroAtto(String dataRegistroAtto) {
		this.dataRegistroAtto = dataRegistroAtto;
	}
	public String getDataRevocalLiquidaz() {
		return dataRevocalLiquidaz;
	}
	public void setDataRevocalLiquidaz(String dataRevocalLiquidaz) {
		this.dataRevocalLiquidaz = dataRevocalLiquidaz;
	}
	public String getDescIndicatEsecutAtto() {
		return descIndicatEsecutAtto;
	}
	public void setDescIndicatEsecutAtto(String descIndicatEsecutAtto) {
		this.descIndicatEsecutAtto = descIndicatEsecutAtto;
	}
	public String getDescrAltreIndicazioni() {
		return descrAltreIndicazioni;
	}
	public void setDescrAltreIndicazioni(String descrAltreIndicazioni) {
		this.descrAltreIndicazioni = descrAltreIndicazioni;
	}
	public String getDescrCodAtto() {
		return descrCodAtto;
	}
	public void setDescrCodAtto(String descrCodAtto) {
		this.descrCodAtto = descrCodAtto;
	}
	public String getDescrNotaio() {
		return descrNotaio;
	}
	public void setDescrNotaio(String descrNotaio) {
		this.descrNotaio = descrNotaio;
	}
	public String getDescrTribunale() {
		return descrTribunale;
	}
	public void setDescrTribunale(String descrTribunale) {
		this.descrTribunale = descrTribunale;
	}
	public String getIdAAEPAzienda() {
		return idAAEPAzienda;
	}
	public void setIdAAEPAzienda(String idAAEPAzienda) {
		this.idAAEPAzienda = idAAEPAzienda;
	}
	public String getIdAAEPFonteDato() {
		return idAAEPFonteDato;
	}
	public void setIdAAEPFonteDato(String idAAEPFonteDato) {
		this.idAAEPFonteDato = idAAEPFonteDato;
	}
	public String getLocalRegistroAtto() {
		return localRegistroAtto;
	}
	public void setLocalRegistroAtto(String localRegistroAtto) {
		this.localRegistroAtto = localRegistroAtto;
	}
	public String getNumRestistrAtto() {
		return numRestistrAtto;
	}
	public void setNumRestistrAtto(String numRestistrAtto) {
		this.numRestistrAtto = numRestistrAtto;
	}
	public String getProgrLiquidazione() {
		return progrLiquidazione;
	}
	public void setProgrLiquidazione(String progrLiquidazione) {
		this.progrLiquidazione = progrLiquidazione;
	}
	public String getSiglaProvRegAtto() {
		return siglaProvRegAtto;
	}
	public void setSiglaProvRegAtto(String siglaProvRegAtto) {
		this.siglaProvRegAtto = siglaProvRegAtto;
	}

}
