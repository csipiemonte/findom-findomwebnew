/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrTModelDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AggrTModelDtoRowMapper extends GenericRowMapper {

	public AggrTModelDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		AggrTModelDto dto = new AggrTModelDto();
		dto.setModelId(rs.getInt("model_id"));
		dto.setTemplateCodeFk(rs.getString("template_code_fk"));
		dto.setModelProgr(rs.getInt("model_progr"));
		dto.setUserId(rs.getString("user_id"));
		dto.setSerializedModel(rs.getString("serialized_model"));
		dto.setModelStateFk(rs.getString("model_state_fk"));
		dto.setModelName(rs.getString("model_name"));
		dto.setModelLastUpdate(rs.getString("model_last_update"));
		return dto;
	}
}
