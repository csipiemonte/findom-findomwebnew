/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShellSoggettiDtoRowMapper extends GenericRowMapper {
	public ShellSoggettiDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		ShellSoggettiDto dto = new ShellSoggettiDto();
		dto.setIdSoggetto(rs.getInt("id_soggetto"));
		dto.setCodiceFiscale(rs.getString("cod_fiscale"));
		dto.setDenominazione(rs.getString("denominazione"));
		dto.setIdFormaGiuridica(rs.getInt("id_forma_giuridica"));
		dto.setCognome(rs.getString("cognome"));
		dto.setNome(rs.getString("nome"));
		return dto;
	}
}
