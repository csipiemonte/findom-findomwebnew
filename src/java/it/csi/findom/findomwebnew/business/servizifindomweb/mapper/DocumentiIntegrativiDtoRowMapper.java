/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.DocumentiIntegrativiDto;

public class DocumentiIntegrativiDtoRowMapper extends GenericRowMapper {

	public DocumentiIntegrativiDto mapRow(ResultSet rs, int rowNum) throws SQLException {

		DocumentiIntegrativiDto dto = new DocumentiIntegrativiDto();
		dto.setDtProtocollo(rs.getDate("dt_protocollo"));
		dto.setIdDocumentoIndex(rs.getLong("id_documento_index"));
		dto.setNomeFile(rs.getString("nome_file"));
		dto.setIdStatoDocumentoIndex(rs.getLong("id_stato_documento_index"));
		dto.setNumProtocollo(rs.getString("num_protocollo"));
		dto.setUuidNodo(rs.getString("uuid_nodo"));
		return dto;
	}

}
