/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDomandeDto;

public class ShellDomandeDtoRowMapper extends GenericRowMapper {
	public ShellDomandeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		ShellDomandeDto dto = new ShellDomandeDto();
		dto.setIdDomanda(rs.getInt("id_domanda"));
		dto.setIdSoggettoCreatore(rs.getInt("id_soggetto_creatore"));
		dto.setIdSoggettoBeneficiario(rs.getInt("id_soggetto_beneficiario"));
		dto.setIdSportelloBando(rs.getInt("id_sportello_bando"));
		dto.setIdSoggettoInvio(rs.getInt("id_soggetto_invio"));
		dto.setDataCreazione(rs.getString("dt_creazione"));
		dto.setIdTipolBeneficiario(rs.getInt("id_tipol_beneficiario"));
		dto.setDataInvioDomanda(rs.getString("dt_invio_domanda"));
		dto.setFascicolo_acta(rs.getString("fascicolo_acta"));
		dto.setDtConclusione(rs.getDate("dt_conclusione"));
		dto.setIdSoggettoConclusione(rs.getLong("id_soggetto_conclusione"));
		dto.setIdMotivazioneEsito(rs.getLong("id_motivazione_esito"));
		dto.setIdIstruttoreAmm(rs.getLong("id_istruttore_amm"));
		dto.setNoteEsitoIstruttoria(rs.getString("note_esito_istruttoria"));
		dto.setIdStatoIstr(rs.getLong("id_stato_istr"));
		dto.setDtRichiestaIntegrazione(rs.getDate("dt_richiesta_integrazione"));
		dto.setDtUploadDocumenti(rs.getDate("dt_upload_documenti"));
		dto.setNoteRichiestaIntegrazione(rs.getString("note_richiesta_integrazione"));
		dto.setIdDecisore(rs.getLong("id_decisore"));
		dto.setDtRimodulazioneIstruttore(rs.getDate("dt_rimodulazione_istruttore"));
		dto.setDtRimodulazioneDecisore(rs.getDate("dt_rimodulazione_decisore"));
		dto.setDtTermineIntegrazione(rs.getDate("dt_termine_integrazione"));
		dto.setFlagAbilitaIntegrazione(rs.getBoolean("flag_abilita_integrazione"));
		dto.setIdFunzionarioIntegrazione(rs.getLong("id_funzionario_integrazione"));

		return dto;
	}
}
