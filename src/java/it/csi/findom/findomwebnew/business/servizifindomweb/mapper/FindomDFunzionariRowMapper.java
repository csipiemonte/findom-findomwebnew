/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomDFunzionariDTO;

/**
 * @author riccardo.bova
 * @date 15 giu 2018
 */
public class FindomDFunzionariRowMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		FindomDFunzionariDTO dto = new FindomDFunzionariDTO();
		dto.setCodFiscale(rs.getString("cod_fiscale"));
		dto.setCognome(rs.getString("cognome"));
		dto.setDtFine(rs.getDate("dt_fine"));
		dto.setDtInizio(rs.getDate("dt_inizio"));
		dto.setEmail(rs.getString("email"));
		dto.setIdFunzionario(rs.getLong("id_funzionario"));
		dto.setIdRuolo(rs.getLong("id_ruolo"));
		dto.setNome(rs.getString("nome"));
		return dto;
	}

}
