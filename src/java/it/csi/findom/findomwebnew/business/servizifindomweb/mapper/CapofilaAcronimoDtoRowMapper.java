/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.CapofilaAcronimoDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CapofilaAcronimoDtoRowMapper extends GenericRowMapper {
	public CapofilaAcronimoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		CapofilaAcronimoDto dto = new CapofilaAcronimoDto();
		dto.setIdCapofilaAcronimo(Objects.toString(rs.getInt("idCapofilaAcronimo"),""));
		dto.setIdAcronimoBando(Objects.toString(rs.getInt("idAcronimoBando"),""));
		dto.setIdDomanda(Objects.toString(rs.getInt("idDomanda"),""));
		dto.setDtAttivazione(rs.getString("dtAttivazione"));
		dto.setDtDisattivazione(rs.getString("dtDisattivazione"));
		return dto;
	}
}
