/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class FindomTBandiDto implements Serializable {

	static final long serialVersionUID = 1;

	private Long idBando;
	private String descrizione;
	private String descBreve;
	private Long idClassificazione;
	private Long templateId;
	private Long idEnteDestinatario;
	private Long numMaxDomande;
	private BigDecimal percMaxContributoErogabile;
	private String flagDemat;
	private String parolaChiaveActa;
	private String feedbackActa;
	private String tipoFirma;
	private BigDecimal percQuotaParte;
	private Long idSettore;
	private BigDecimal importoMassimoErogabile;
	private BigDecimal importoMinimoErogabile;
	private Long punteggioMinValut;
	private String flagValutazioneEsterna;
	private String flagValutazioneMassiva;
	private BigDecimal budgetDisponibile;
	private String tipologiaVerificaFirma;
	private String flagVerificaFirma;
	private Boolean flagNuovaGestione;
	private String flagIstruttoriaEsterna;
	private Boolean flagRimodulazione;
	private BigDecimal idTipoMetadati;
	private Date dataInizioProgetto;
	private Date dataFineProgetto;
	private String flagDuplParolaChiaveActa;
	private Boolean flagProgettiComuni;
	private Date dataMaxInizioProgetto;
	private String codNodoOperativo;
	private Boolean flagAmmAziendeEstere;
	private Long idAreaTematica;
	private Boolean flagSchedaProgetto;
	private Boolean flagUploadIndex;
	
	public Long getIdBando() {
		return idBando;
	}

	public void setIdBando(Long idBando) {
		this.idBando = idBando;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDescBreve() {
		return descBreve;
	}

	public void setDescBreve(String descBreve) {
		this.descBreve = descBreve;
	}

	public Long getIdClassificazione() {
		return idClassificazione;
	}

	public void setIdClassificazione(Long idClassificazione) {
		this.idClassificazione = idClassificazione;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getIdEnteDestinatario() {
		return idEnteDestinatario;
	}

	public void setIdEnteDestinatario(Long idEnteDestinatario) {
		this.idEnteDestinatario = idEnteDestinatario;
	}

	public Long getNumMaxDomande() {
		return numMaxDomande;
	}

	public void setNumMaxDomande(Long numMaxDomande) {
		this.numMaxDomande = numMaxDomande;
	}

	public BigDecimal getPercMaxContributoErogabile() {
		return percMaxContributoErogabile;
	}

	public void setPercMaxContributoErogabile(BigDecimal percMaxContributoErogabile) {
		this.percMaxContributoErogabile = percMaxContributoErogabile;
	}

	public String getFlagDemat() {
		return flagDemat;
	}

	public void setFlagDemat(String flagDemat) {
		this.flagDemat = flagDemat;
	}

	public String getParolaChiaveActa() {
		return parolaChiaveActa;
	}

	public void setParolaChiaveActa(String parolaChiaveActa) {
		this.parolaChiaveActa = parolaChiaveActa;
	}

	public String getFeedbackActa() {
		return feedbackActa;
	}

	public void setFeedbackActa(String feedbackActa) {
		this.feedbackActa = feedbackActa;
	}

	public String getTipoFirma() {
		return tipoFirma;
	}

	public void setTipoFirma(String tipoFirma) {
		this.tipoFirma = tipoFirma;
	}

	public BigDecimal getPercQuotaParte() {
		return percQuotaParte;
	}

	public void setPercQuotaParte(BigDecimal percQuotaParte) {
		this.percQuotaParte = percQuotaParte;
	}

	public Long getIdSettore() {
		return idSettore;
	}

	public void setIdSettore(Long idSettore) {
		this.idSettore = idSettore;
	}

	public BigDecimal getImportoMassimoErogabile() {
		return importoMassimoErogabile;
	}

	public void setImportoMassimoErogabile(BigDecimal importoMassimoErogabile) {
		this.importoMassimoErogabile = importoMassimoErogabile;
	}

	public BigDecimal getImportoMinimoErogabile() {
		return importoMinimoErogabile;
	}

	public void setImportoMinimoErogabile(BigDecimal importoMinimoErogabile) {
		this.importoMinimoErogabile = importoMinimoErogabile;
	}

	public Long getPunteggioMinValut() {
		return punteggioMinValut;
	}

	public void setPunteggioMinValut(Long punteggioMinValut) {
		this.punteggioMinValut = punteggioMinValut;
	}

	public String getFlagValutazioneEsterna() {
		return flagValutazioneEsterna;
	}

	public void setFlagValutazioneEsterna(String flagValutazioneEsterna) {
		this.flagValutazioneEsterna = flagValutazioneEsterna;
	}

	public String getFlagValutazioneMassiva() {
		return flagValutazioneMassiva;
	}

	public void setFlagValutazioneMassiva(String flagValutazioneMassiva) {
		this.flagValutazioneMassiva = flagValutazioneMassiva;
	}

	public BigDecimal getBudgetDisponibile() {
		return budgetDisponibile;
	}

	public void setBudgetDisponibile(BigDecimal budgetDisponibile) {
		this.budgetDisponibile = budgetDisponibile;
	}

	public String getTipologiaVerificaFirma() {
		return tipologiaVerificaFirma;
	}

	public void setTipologiaVerificaFirma(String tipologiaVerificaFirma) {
		this.tipologiaVerificaFirma = tipologiaVerificaFirma;
	}

	public String getFlagVerificaFirma() {
		return flagVerificaFirma;
	}

	public void setFlagVerificaFirma(String flagVerificaFirma) {
		this.flagVerificaFirma = flagVerificaFirma;
	}

	public Boolean getFlagNuovaGestione() {
		return flagNuovaGestione;
	}

	public void setFlagNuovaGestione(Boolean flagNuovaGestione) {
		this.flagNuovaGestione = flagNuovaGestione;
	}

	public String getFlagIstruttoriaEsterna() {
		return flagIstruttoriaEsterna;
	}

	public void setFlagIstruttoriaEsterna(String flagIstruttoriaEsterna) {
		this.flagIstruttoriaEsterna = flagIstruttoriaEsterna;
	}

	public Boolean getFlagRimodulazione() {
		return flagRimodulazione;
	}

	public void setFlagRimodulazione(Boolean flagRimodulazione) {
		this.flagRimodulazione = flagRimodulazione;
	}

	public BigDecimal getIdTipoMetadati() {
		return idTipoMetadati;
	}

	public void setIdTipoMetadati(BigDecimal idTipoMetadati) {
		this.idTipoMetadati = idTipoMetadati;
	}

	public Date getDataInizioProgetto() {
		return dataInizioProgetto;
	}

	public void setDataInizioProgetto(Date dataInizioProgetto) {
		this.dataInizioProgetto = dataInizioProgetto;
	}

	public Date getDataFineProgetto() {
		return dataFineProgetto;
	}

	public void setDataFineProgetto(Date dataFineProgetto) {
		this.dataFineProgetto = dataFineProgetto;
	}

	public String getFlagDuplParolaChiaveActa() {
		return flagDuplParolaChiaveActa;
	}

	public void setFlagDuplParolaChiaveActa(String flagDuplParolaChiaveActa) {
		this.flagDuplParolaChiaveActa = flagDuplParolaChiaveActa;
	}

	public Boolean getFlagProgettiComuni() {
		return flagProgettiComuni;
	}

	public void setFlagProgettiComuni(Boolean flagProgettiComuni) {
		this.flagProgettiComuni = flagProgettiComuni;
	}

	public Date getDataMaxInizioProgetto() {
		return dataMaxInizioProgetto;
	}

	public void setDataMaxInizioProgetto(Date dataMaxInizioProgetto) {
		this.dataMaxInizioProgetto = dataMaxInizioProgetto;
	}

	public String getCodNodoOperativo() {
		return codNodoOperativo;
	}

	public void setCodNodoOperativo(String codNodoOperativo) {
		this.codNodoOperativo = codNodoOperativo;
	}

	public Boolean getFlagAmmAziendeEstere() {
		return flagAmmAziendeEstere;
	}

	public void setFlagAmmAziendeEstere(Boolean flagAmmAziendeEstere) {
		this.flagAmmAziendeEstere = flagAmmAziendeEstere;
	}

	public Long getIdAreaTematica() {
		return idAreaTematica;
	}

	public void setIdAreaTematica(Long idAreaTematica) {
		this.idAreaTematica = idAreaTematica;
	}

	public Boolean getFlagSchedaProgetto() {
		return flagSchedaProgetto;
	}

	public void setFlagSchedaProgetto(Boolean flagSchedaProgetto) {
		this.flagSchedaProgetto = flagSchedaProgetto;
	}

	public Boolean getFlagUploadIndex() {
		return flagUploadIndex;
	}

	public void setFlagUploadIndex(Boolean flagUploadIndex) {
		this.flagUploadIndex = flagUploadIndex;
	}

}
