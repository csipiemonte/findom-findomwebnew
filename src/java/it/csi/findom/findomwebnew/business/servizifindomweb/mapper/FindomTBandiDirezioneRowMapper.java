/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDirezioneDto;

public class FindomTBandiDirezioneRowMapper extends GenericRowMapper {
	public FindomTBandiDirezioneDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		FindomTBandiDirezioneDto dto = new FindomTBandiDirezioneDto();
		dto.setDescBreveBando(rs.getString("descr_breve_bando"));
		dto.setDescrizioneBando(rs.getString("descrizione_bando"));
		dto.setDescrizioneSettore(rs.getString("descrizioneSettore"));
		dto.setIdBando(rs.getLong("id_bando"));
		dto.setIdDirezione(rs.getLong("id_direzione"));
		dto.setIdSettore(rs.getLong("id_settore"));
		dto.setDescrizioneDirezione(rs.getString("descrizione_direzione"));

		return dto;
	}
}
