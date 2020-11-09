/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp;

import static it.csi.melograno.aggregatore.business.javaengine.commonality.MapTarget.INHERIT;
import it.csi.melograno.aggregatore.business.javaengine.commonality.CommonalityVO;
import it.csi.melograno.aggregatore.business.javaengine.commonality.MapTo;

public class CostituzioneImpresaDto extends CommonalityVO {
			 
  
	
  @MapTo(target = INHERIT, name = "costituzioneInCorso")
  String costituzioneInCorso;

  @MapTo(target = INHERIT, name = "dataCostituzioneImpresa")
  String dataCostituzioneImpresa;
  
  @MapTo(target = INHERIT, name = "iscrizioneInCorso")
  String iscrizioneInCorso;

  @MapTo(target = INHERIT, name = "provincia")
  String provincia;

  @MapTo(target = INHERIT, name = "provinciaDescrizione")
  String provinciaDescrizione;
  
  @MapTo(target = INHERIT, name = "idStatoCostImpresa")
  String idStatoCostImpresa;
  
  @MapTo(target = INHERIT, name = "descrStatoCostImpresa")
  String descrStatoCostImpresa;

  @MapTo(target = INHERIT, name = "dataIscrizioneRegistroImprese")
  String dataIscrizioneRegistroImprese;
  
  
	public String getCostituzioneInCorso() {
		return costituzioneInCorso;
	}

	public void setCostituzioneInCorso(String costituzioneInCorso) {
		this.costituzioneInCorso = costituzioneInCorso;
	}
	
	
	public String getDataCostituzioneImpresa() {
		return dataCostituzioneImpresa;
	}
	
	public void setDataCostituzioneImpresa(String dataCostituzioneImpresa) {
		this.dataCostituzioneImpresa = dataCostituzioneImpresa;
	}

	public String getIscrizioneInCorso() {
		return iscrizioneInCorso;
	}
	
	public void setIscrizioneInCorso(String iscrizioneInCorso) {
		this.iscrizioneInCorso = iscrizioneInCorso;
	}
	
	public String getProvincia() {
		return provincia;
	}
	
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	public String getProvinciaDescrizione() {
		return provinciaDescrizione;
	}

	public void setProvinciaDescrizione(String provinciaDescrizione) {
		this.provinciaDescrizione = provinciaDescrizione;
	}

	public String getIdStatoCostImpresa() {
		return idStatoCostImpresa;
	}

	public void setIdStatoCostImpresa(String idStatoCostImpresa) {
		this.idStatoCostImpresa = idStatoCostImpresa;
	}

	public String getDescrStatoCostImpresa() {
		return descrStatoCostImpresa;
	}

	public void setDescrStatoCostImpresa(String descrStatoCostImpresa) {
		this.descrStatoCostImpresa = descrStatoCostImpresa;
	}

	public String getDataIscrizioneRegistroImprese() {
		return dataIscrizioneRegistroImprese;
	}

	public void setDataIscrizioneRegistroImprese(
			String dataIscrizioneRegistroImprese) {
		this.dataIscrizioneRegistroImprese = dataIscrizioneRegistroImprese;
	}
	

}
