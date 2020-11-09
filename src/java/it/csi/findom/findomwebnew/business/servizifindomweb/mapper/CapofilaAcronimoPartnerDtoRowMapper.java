/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.CapofilaAcronimoPartnerDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CapofilaAcronimoPartnerDtoRowMapper extends GenericRowMapper {
	public CapofilaAcronimoPartnerDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		CapofilaAcronimoPartnerDto dto = new CapofilaAcronimoPartnerDto();		
		dto.setIdCapofilaAcronimo(Objects.toString(rs.getInt("idCapofilaAcronimo"),""));
		dto.setIdPartner(Objects.toString(rs.getInt("idPartner"),""));
		dto.setIdDomandaPartner(Objects.toString(rs.getInt("idDomandaPartner"),""));
		dto.setDtAttivazione(rs.getString("dtAttivazione"));
		dto.setDtDisattivazione(rs.getString("dtDisattivazione"));
		return dto;
	}
}
