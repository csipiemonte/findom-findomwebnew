/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDocDematDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeBeneficiariDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VistaDocDematDtoRowMapper extends GenericRowMapper {
	public VistaDocDematDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		VistaDocDematDto dto = new VistaDocDematDto();	
		
		dto.setIdDomanda(rs.getInt("ID_DOMANDA"));
		dto.setFlagRegionale(rs.getString("FLAG_REGIONALE"));
		dto.setFlagFinpiemonte(rs.getString("FLAG_FINPIEMONTE"));
		dto.setDestFileDomanda(rs.getString("DEST_FILE_DOMANDA"));
		dto.setTipoAllegato(rs.getString("TIPO_ALLEGATO"));
		
		return dto;
	}
}
