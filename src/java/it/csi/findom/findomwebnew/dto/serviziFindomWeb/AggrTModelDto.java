/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb;

import java.io.Serializable;

public class AggrTModelDto implements Serializable {

	static final long serialVersionUID = 1;

	// tabella AGGR_T_MODEL
	
	private int modelId;
	private String templateCodeFk;
	private int modelProgr;
	private String userId;
	private String serializedModel;
	private String modelStateFk;
	private String modelName ;
	private String modelLastUpdate;
	
	// GETTERS && SETTERS 
	
	public int getModelId() {
		return modelId;
	}
	public void setModelId(int modelId) {
		this.modelId = modelId;
	}
	public String getTemplateCodeFk() {
		return templateCodeFk;
	}
	public void setTemplateCodeFk(String templateCodeFk) {
		this.templateCodeFk = templateCodeFk;
	}
	public int getModelProgr() {
		return modelProgr;
	}
	public void setModelProgr(int modelProgr) {
		this.modelProgr = modelProgr;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSerializedModel() {
		return serializedModel;
	}
	public void setSerializedModel(String serializedModel) {
		this.serializedModel = serializedModel;
	}
	public String getModelStateFk() {
		return modelStateFk;
	}
	public void setModelStateFk(String modelStateFk) {
		this.modelStateFk = modelStateFk;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelLastUpdate() {
		return modelLastUpdate;
	}
	public void setModelLastUpdate(String modelLastUpdate) {
		this.modelLastUpdate = modelLastUpdate;
	}
	
}
