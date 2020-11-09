/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrTTemplateDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AggrTTemplateDtoRowMapper extends GenericRowMapper {

	public AggrTTemplateDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		// non contemplo le colonne col pdf e quelle con le regole di validazione 
		AggrTTemplateDto dto = new AggrTTemplateDto();
		dto.setTemplateId(rs.getInt("template_id"));
		dto.setTemplateCode(rs.getString("template_code"));
//		dto.setTemplate(rs.getString("template"));
//		dto.setPdfTemplate(rs.getString("pdf_template"));
//		dto.setModelValidationRules(rs.getString("model_validation_rules"));
		dto.setTemplateDescription(rs.getString("template_description"));
		dto.setTemplateName(rs.getString("template_name"));
		dto.setTemplateLastUpdate(rs.getString("template_last_update"));
		dto.setTemplateValidFromDate(rs.getString("template_valid_from_date"));
		dto.setTemplateValidToDate(rs.getString("template_valid_to_date"));
//		dto.setCommandValidationRules(rs.getString("command_validation_rules"));
//		dto.setGlobalValidationRules(rs.getString("global_validation_rules"));
		dto.setTemplateSplitted(rs.getString("template_splitted"));
		return dto;
	}
	
}
