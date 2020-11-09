/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;
import java.util.List;

public class Impresa  implements Serializable{

	private static final long serialVersionUID = 1L;

	// attributi di it.csi.aaep.aaeporch.business.Impresa;
	private Cessazione cessazione;
	private String codNaturaGiuridica;
	private String codiceFiscale;
	private List<AttivitaEconomica> codiciATECO;
	private String dataAggiornamento;
	private String dataInizioValidita;
	private String descrFonte;
	private String descrNaturaGiuridica;
	private DettagliAlboArtigiano dettagliAlboArtigiano;
    private DettagliCameraCommercio dettagliCameraCommercio;
    private String idAzienda;
    private String idFonte;
    private String idNaturaGiuridica;
    private String idSede;
    private String partitaIva;
    private String postaElettronicaCertificata;
    private String ragioneSociale;
    private List<Sede> sedi;
	
	// attributi di it.csi.aaep.aaeporch.business.ImpresaINFOC;
	private String annoDenunciaAddetti;
	private Cessazione cessazioneFunzioneSedeLegale;
	private String codFonte;
	private DatoCostitutivo datoCostitutivo;
	private String descrIndicStatoAttiv;
	private String descrIndicTrasfSede;
	private String flagLocalizzazionePiemonte;
	private String flgAggiornamento;
	private String impresaCessata;
	private String indicStatoAttiv;
	private String indicTrasfSede;
	private List<Persona> listaPersone;
	private List<ProcConcors> listaProcConcors;
	private List<SezioneSpeciale> listaSezSpecInfoc;
	private String numAddettiFam;
	private String numAddettiSubord;
	private List<String> testoOggettoSociale;
	
	public Cessazione getCessazione() {
		return cessazione;
	}
	public void setCessazione(Cessazione cessazione) {
		this.cessazione = cessazione;
	}
	public String getCodNaturaGiuridica() {
		return codNaturaGiuridica;
	}
	public void setCodNaturaGiuridica(String codNaturaGiuridica) {
		this.codNaturaGiuridica = codNaturaGiuridica;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public List<AttivitaEconomica> getCodiciATECO() {
		return codiciATECO;
	}
	public void setCodiciATECO(List<AttivitaEconomica> codiciATECO) {
		this.codiciATECO = codiciATECO;
	}
	public String getDescrNaturaGiuridica() {
		return descrNaturaGiuridica;
	}
	public void setDescrNaturaGiuridica(String descrNaturaGiuridica) {
		this.descrNaturaGiuridica = descrNaturaGiuridica;
	}
	public String getIdAzienda() {
		return idAzienda;
	}
	public void setIdAzienda(String idAzienda) {
		this.idAzienda = idAzienda;
	}
	public String getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public String getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(String dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public String getDescrFonte() {
		return descrFonte;
	}
	public void setDescrFonte(String descrFonte) {
		this.descrFonte = descrFonte;
	}
	public DettagliAlboArtigiano getDettagliAlboArtigiano() {
		return dettagliAlboArtigiano;
	}
	public void setDettagliAlboArtigiano(DettagliAlboArtigiano dettagliAlboArtigiano) {
		this.dettagliAlboArtigiano = dettagliAlboArtigiano;
	}
	public DettagliCameraCommercio getDettagliCameraCommercio() {
		return dettagliCameraCommercio;
	}
	public void setDettagliCameraCommercio(
			DettagliCameraCommercio dettagliCameraCommercio) {
		this.dettagliCameraCommercio = dettagliCameraCommercio;
	}
	public String getIdFonte() {
		return idFonte;
	}
	public void setIdFonte(String idFonte) {
		this.idFonte = idFonte;
	}
	public String getIdNaturaGiuridica() {
		return idNaturaGiuridica;
	}
	public void setIdNaturaGiuridica(String idNaturaGiuridica) {
		this.idNaturaGiuridica = idNaturaGiuridica;
	}
	public String getIdSede() {
		return idSede;
	}
	public void setIdSede(String idSede) {
		this.idSede = idSede;
	}
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	public String getPostaElettronicaCertificata() {
		return postaElettronicaCertificata;
	}
	public void setPostaElettronicaCertificata(String postaElettronicaCertificata) {
		this.postaElettronicaCertificata = postaElettronicaCertificata;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public List<Sede> getSedi() {
		return sedi;
	}
	public void setSedi(List<Sede> sedi) {
		this.sedi = sedi;
	}
	public String getAnnoDenunciaAddetti() {
		return annoDenunciaAddetti;
	}
	public void setAnnoDenunciaAddetti(String annoDenunciaAddetti) {
		this.annoDenunciaAddetti = annoDenunciaAddetti;
	}
	public Cessazione getCessazioneFunzioneSedeLegale() {
		return cessazioneFunzioneSedeLegale;
	}
	public void setCessazioneFunzioneSedeLegale(
			Cessazione cessazioneFunzioneSedeLegale) {
		this.cessazioneFunzioneSedeLegale = cessazioneFunzioneSedeLegale;
	}
	public String getCodFonte() {
		return codFonte;
	}
	public void setCodFonte(String codFonte) {
		this.codFonte = codFonte;
	}
	public DatoCostitutivo getDatoCostitutivo() {
		return datoCostitutivo;
	}
	public void setDatoCostitutivo(DatoCostitutivo datoCostitutivo) {
		this.datoCostitutivo = datoCostitutivo;
	}
	public String getDescrIndicStatoAttiv() {
		return descrIndicStatoAttiv;
	}
	public void setDescrIndicStatoAttiv(String descrIndicStatoAttiv) {
		this.descrIndicStatoAttiv = descrIndicStatoAttiv;
	}
	public String getDescrIndicTrasfSede() {
		return descrIndicTrasfSede;
	}
	public void setDescrIndicTrasfSede(String descrIndicTrasfSede) {
		this.descrIndicTrasfSede = descrIndicTrasfSede;
	}
	public String getFlagLocalizzazionePiemonte() {
		return flagLocalizzazionePiemonte;
	}
	public void setFlagLocalizzazionePiemonte(String flagLocalizzazionePiemonte) {
		this.flagLocalizzazionePiemonte = flagLocalizzazionePiemonte;
	}
	public String getFlgAggiornamento() {
		return flgAggiornamento;
	}
	public void setFlgAggiornamento(String flgAggiornamento) {
		this.flgAggiornamento = flgAggiornamento;
	}
	public String getImpresaCessata() {
		return impresaCessata;
	}
	public void setImpresaCessata(String impresaCessata) {
		this.impresaCessata = impresaCessata;
	}
	public String getIndicStatoAttiv() {
		return indicStatoAttiv;
	}
	public void setIndicStatoAttiv(String indicStatoAttiv) {
		this.indicStatoAttiv = indicStatoAttiv;
	}
	public String getIndicTrasfSede() {
		return indicTrasfSede;
	}
	public void setIndicTrasfSede(String indicTrasfSede) {
		this.indicTrasfSede = indicTrasfSede;
	}
	public List<Persona> getListaPersone() {
		return listaPersone;
	}
	public void setListaPersone(List<Persona> listaPersone) {
		this.listaPersone = listaPersone;
	}
	public List<ProcConcors> getListaProcConcors() {
		return listaProcConcors;
	}
	public void setListaProcConcors(List<ProcConcors> listaProcConcors) {
		this.listaProcConcors = listaProcConcors;
	}
	public List<SezioneSpeciale> getListaSezSpecInfoc() {
		return listaSezSpecInfoc;
	}
	public void setListaSezSpecInfoc(List<SezioneSpeciale> listaSezSpecInfoc) {
		this.listaSezSpecInfoc = listaSezSpecInfoc;
	}
	public String getNumAddettiFam() {
		return numAddettiFam;
	}
	public void setNumAddettiFam(String numAddettiFam) {
		this.numAddettiFam = numAddettiFam;
	}
	public String getNumAddettiSubord() {
		return numAddettiSubord;
	}
	public void setNumAddettiSubord(String numAddettiSubord) {
		this.numAddettiSubord = numAddettiSubord;
	}
	public List<String> getTestoOggettoSociale() {
		return testoOggettoSociale;
	}
	public void setTestoOggettoSociale(List<String> testoOggettoSociale) {
		this.testoOggettoSociale = testoOggettoSociale;
	}
}
