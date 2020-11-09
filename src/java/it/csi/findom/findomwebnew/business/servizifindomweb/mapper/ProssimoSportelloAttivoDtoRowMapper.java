/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ProssimoSportelloAttivoDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProssimoSportelloAttivoDtoRowMapper extends GenericRowMapper {
	public ProssimoSportelloAttivoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProssimoSportelloAttivoDto dto = new ProssimoSportelloAttivoDto();
		dto.setDtApertura(rs.getString("dt_apertura"));
		dto.setOraApertura(rs.getString("ora_apertura"));
		dto.setDescrizioneBando(rs.getString("descrizione_bando"));
		return dto;
	}
}
