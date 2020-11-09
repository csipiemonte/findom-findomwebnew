/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;

public class AggrTTemplateDto implements Serializable {

	static final long serialVersionUID = 1;
	
	// tabella AGGR_T_TEMPLATE 
	
	private int templateId;
	private String templateCode ;
	private String template;
	private String pdfTemplate;
	private String modelValidationRules ;
	private String templateDescription;
	private String templateName;
	private String templateLastUpdate;  // Date
	private String templateValidFromDate; // Date
	private String templateValidToDate; // Date
	private String templateState;
	private String commandValidationRules;
	private String globalValidationRules;
	private String templateSplitted ;
	
	// GETTERS && SETTERS
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getPdfTemplate() {
		return pdfTemplate;
	}

	public void setPdfTemplate(String pdfTemplate) {
		this.pdfTemplate = pdfTemplate;
	}

	public String getModelValidationRules() {
		return modelValidationRules;
	}

	public void setModelValidationRules(String modelValidationRules) {
		this.modelValidationRules = modelValidationRules;
	}

	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateLastUpdate() {
		return templateLastUpdate;
	}

	public void setTemplateLastUpdate(String templateLastUpdate) {
		this.templateLastUpdate = templateLastUpdate;
	}

	public String getTemplateValidFromDate() {
		return templateValidFromDate;
	}

	public void setTemplateValidFromDate(String templateValidFromDate) {
		this.templateValidFromDate = templateValidFromDate;
	}

	public String getTemplateValidToDate() {
		return templateValidToDate;
	}

	public void setTemplateValidToDate(String templateValidToDate) {
		this.templateValidToDate = templateValidToDate;
	}

	public String getTemplateState() {
		return templateState;
	}

	public void setTemplateState(String templateState) {
		this.templateState = templateState;
	}

	public String getCommandValidationRules() {
		return commandValidationRules;
	}

	public void setCommandValidationRules(String commandValidationRules) {
		this.commandValidationRules = commandValidationRules;
	}

	public String getGlobalValidationRules() {
		return globalValidationRules;
	}

	public void setGlobalValidationRules(String globalValidationRules) {
		this.globalValidationRules = globalValidationRules;
	}

	public String getTemplateSplitted() {
		return templateSplitted;
	}

	public void setTemplateSplitted(String templateSplitted) {
		this.templateSplitted = templateSplitted;
	}
	
}
