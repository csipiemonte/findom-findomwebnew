/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDto;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindomTBandiRowMapper extends GenericRowMapper {
	public FindomTBandiDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		FindomTBandiDto dto = new FindomTBandiDto();
		dto.setBudgetDisponibile(rs.getBigDecimal("budget_disponibile"));
		dto.setIdBando(rs.getLong("id_bando"));
		dto.setDescrizione(rs.getString("descrizione"));
		dto.setDescBreve(rs.getString("descr_breve"));
		dto.setIdClassificazione(rs.getLong("id_classificazione"));
		dto.setTemplateId(rs.getLong("template_id"));
		dto.setIdEnteDestinatario(rs.getLong("id_ente_destinatario"));
		dto.setNumMaxDomande(rs.getLong("num_max_domande"));
		dto.setPercMaxContributoErogabile(rs.getBigDecimal("perc_max_contributo_erogabile"));
		dto.setFlagDemat(rs.getString("flag_demat"));
		dto.setParolaChiaveActa(rs.getString("parola_chiave_acta"));
		dto.setFeedbackActa(rs.getString("feedback_acta"));
		dto.setTipoFirma(rs.getString("tipo_firma"));
		dto.setPercQuotaParte(rs.getBigDecimal("perc_quota_parte"));
		dto.setIdSettore(rs.getLong("id_settore"));
		dto.setImportoMassimoErogabile(rs.getBigDecimal("importo_massimo_erogabile"));
		dto.setImportoMinimoErogabile(rs.getBigDecimal("importo_minimo_erogabile"));
		dto.setPunteggioMinValut(rs.getLong("punteggio_min_valut"));
		dto.setFlagValutazioneEsterna(rs.getString("flag_valutazione_esterna"));
		dto.setFlagValutazioneMassiva(rs.getString("flag_valutazione_massiva"));
		dto.setTipologiaVerificaFirma(rs.getString("tipologia_verifica_firma"));
		dto.setFlagVerificaFirma(rs.getString("flag_verifica_firma"));
		dto.setFlagNuovaGestione(rs.getBoolean("flag_nuova_gestione"));
		dto.setFlagIstruttoriaEsterna(rs.getString("flag_istruttoria_esterna"));
		dto.setFlagRimodulazione(rs.getBoolean("flag_rimodulazione"));
		dto.setIdTipoMetadati(rs.getBigDecimal("id_tipo_metadati"));
		dto.setDataInizioProgetto(rs.getDate("dt_inizio_progetto"));
		dto.setDataFineProgetto(rs.getDate("dt_fine_progetto"));
		dto.setFlagDuplParolaChiaveActa(rs.getString("flag_dupl_parola_chiave_acta"));
		dto.setFlagProgettiComuni(rs.getBoolean("flag_progetti_comuni"));
		dto.setDataMaxInizioProgetto(rs.getDate("dt_max_inizio_progetto"));
		dto.setCodNodoOperativo(rs.getString("cod_nodo_operativo"));
		dto.setFlagAmmAziendeEstere(rs.getBoolean("flag_amm_aziende_estere"));
		dto.setIdAreaTematica(rs.getLong("id_area_tematica"));
		dto.setFlagSchedaProgetto(rs.getBoolean("flag_scheda_progetto"));
		dto.setFlagUploadIndex(rs.getBoolean("flag_upload_index"));

		return dto;
	}
}
