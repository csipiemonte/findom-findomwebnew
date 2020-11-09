/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;
import java.util.Date;

public class ShellDomandeDto implements Serializable {

	static final long serialVersionUID = 1;
	
	// tabella SHELL_T_DOMANDE 
	
	private int idDomanda;
	private int idSoggettoCreatore;
	private int idSoggettoBeneficiario;
	private int idSportelloBando;
	private Integer idSoggettoInvio;
	private String dataCreazione; // Date
	private int idTipolBeneficiario;
	private String dataInvioDomanda; // Date
	private String fascicolo_acta;
	private Date dtConclusione;
	private Long idSoggettoConclusione;
	private Long idMotivazioneEsito;
	private Long idIstruttoreAmm;
	private String noteEsitoIstruttoria;
	private Long idStatoIstr;
	private Date dtRichiestaIntegrazione;
	private Date dtUploadDocumenti;
	private String noteRichiestaIntegrazione;
	private Long idDecisore;
	private Date dtRimodulazioneIstruttore;
	private Date dtRimodulazioneDecisore;
	private Date dtTermineIntegrazione;
	private Boolean flagAbilitaIntegrazione;
	private Long idFunzionarioIntegrazione;

	public int getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(int idDomanda) {
		this.idDomanda = idDomanda;
	}
	public int getIdSoggettoCreatore() {
		return idSoggettoCreatore;
	}
	public void setIdSoggettoCreatore(int idSoggettoCreatore) {
		this.idSoggettoCreatore = idSoggettoCreatore;
	}
	public int getIdSoggettoBeneficiario() {
		return idSoggettoBeneficiario;
	}
	public void setIdSoggettoBeneficiario(int idSoggettoBeneficiario) {
		this.idSoggettoBeneficiario = idSoggettoBeneficiario;
	}
	public int getIdSportelloBando() {
		return idSportelloBando;
	}
	public void setIdSportelloBando(int idSportelloBando) {
		this.idSportelloBando = idSportelloBando;
	}
	public Integer getIdSoggettoInvio() {
		return idSoggettoInvio;
	}
	public void setIdSoggettoInvio(Integer idSoggettoInvio) {
		this.idSoggettoInvio = idSoggettoInvio;
	}
	public String getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(String dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public int getIdTipolBeneficiario() {
		return idTipolBeneficiario;
	}
	public void setIdTipolBeneficiario(int idTipolBeneficiario) {
		this.idTipolBeneficiario = idTipolBeneficiario;
	}
	public String getDataInvioDomanda() {
		return dataInvioDomanda;
	}
	public void setDataInvioDomanda(String dataInvioDomanda) {
		this.dataInvioDomanda = dataInvioDomanda;
	}

	public String getFascicolo_acta() {
		return fascicolo_acta;
	}

	public void setFascicolo_acta(String fascicolo_acta) {
		this.fascicolo_acta = fascicolo_acta;
	}

	public Date getDtConclusione() {
		return dtConclusione;
	}

	public void setDtConclusione(Date dtConclusione) {
		this.dtConclusione = dtConclusione;
	}

	public Long getIdSoggettoConclusione() {
		return idSoggettoConclusione;
	}

	public void setIdSoggettoConclusione(Long idSoggettoConclusione) {
		this.idSoggettoConclusione = idSoggettoConclusione;
	}

	public Long getIdMotivazioneEsito() {
		return idMotivazioneEsito;
	}

	public void setIdMotivazioneEsito(Long idMotivazioneEsito) {
		this.idMotivazioneEsito = idMotivazioneEsito;
	}

	public Long getIdIstruttoreAmm() {
		return idIstruttoreAmm;
	}

	public void setIdIstruttoreAmm(Long idIstruttoreAmm) {
		this.idIstruttoreAmm = idIstruttoreAmm;
	}

	public String getNoteEsitoIstruttoria() {
		return noteEsitoIstruttoria;
	}

	public void setNoteEsitoIstruttoria(String noteEsitoIstruttoria) {
		this.noteEsitoIstruttoria = noteEsitoIstruttoria;
	}

	public Long getIdStatoIstr() {
		return idStatoIstr;
	}

	public void setIdStatoIstr(Long idStatoIstr) {
		this.idStatoIstr = idStatoIstr;
	}

	public Date getDtRichiestaIntegrazione() {
		return dtRichiestaIntegrazione;
	}

	public void setDtRichiestaIntegrazione(Date dtRichiestaIntegrazione) {
		this.dtRichiestaIntegrazione = dtRichiestaIntegrazione;
	}

	public Date getDtUploadDocumenti() {
		return dtUploadDocumenti;
	}

	public void setDtUploadDocumenti(Date dtUploadDocumenti) {
		this.dtUploadDocumenti = dtUploadDocumenti;
	}

	public String getNoteRichiestaIntegrazione() {
		return noteRichiestaIntegrazione;
	}

	public void setNoteRichiestaIntegrazione(String noteRichiestaIntegrazione) {
		this.noteRichiestaIntegrazione = noteRichiestaIntegrazione;
	}

	public Long getIdDecisore() {
		return idDecisore;
	}

	public void setIdDecisore(Long idDecisore) {
		this.idDecisore = idDecisore;
	}

	public Date getDtRimodulazioneIstruttore() {
		return dtRimodulazioneIstruttore;
	}

	public void setDtRimodulazioneIstruttore(Date dtRimodulazioneIstruttore) {
		this.dtRimodulazioneIstruttore = dtRimodulazioneIstruttore;
	}

	public Date getDtRimodulazioneDecisore() {
		return dtRimodulazioneDecisore;
	}

	public void setDtRimodulazioneDecisore(Date dtRimodulazioneDecisore) {
		this.dtRimodulazioneDecisore = dtRimodulazioneDecisore;
	}

	public Date getDtTermineIntegrazione() {
		return dtTermineIntegrazione;
	}

	public void setDtTermineIntegrazione(Date dtTermineIntegrazione) {
		this.dtTermineIntegrazione = dtTermineIntegrazione;
	}

	public Boolean getFlagAbilitaIntegrazione() {
		return flagAbilitaIntegrazione;
	}

	public void setFlagAbilitaIntegrazione(Boolean flagAbilitaIntegrazione) {
		this.flagAbilitaIntegrazione = flagAbilitaIntegrazione;
	}

	public Long getIdFunzionarioIntegrazione() {
		return idFunzionarioIntegrazione;
	}

	public void setIdFunzionarioIntegrazione(Long idFunzionarioIntegrazione) {
		this.idFunzionarioIntegrazione = idFunzionarioIntegrazione;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
