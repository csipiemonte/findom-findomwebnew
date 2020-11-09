/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp;

import java.io.Serializable;

public class VistaUltimaDomandaDto implements Serializable {

	static final long serialVersionUID = 1;
	
	private OperatorePresentatoreDto operatorePresentatore;
	private CostituzioneImpresaDto costituzioneImpresa;
	private SedeLegaleDto sedeLegale;
	private LegaleRappresentanteDto legaleRappresentante;

	public OperatorePresentatoreDto getOperatorePresentatore() {
		return operatorePresentatore;
	}
	public CostituzioneImpresaDto getCostituzioneImpresa() {
		return costituzioneImpresa;
	}
	public SedeLegaleDto getSedeLegale() {
		return sedeLegale;
	}
	public LegaleRappresentanteDto getLegaleRappresentante() {
		return legaleRappresentante;
	}
	public void setOperatorePresentatore(
			OperatorePresentatoreDto operatorePresentatore) {
		this.operatorePresentatore = operatorePresentatore;
	}
	public void setCostituzioneImpresa(CostituzioneImpresaDto costituzioneImpresa) {
		this.costituzioneImpresa = costituzioneImpresa;
	}
	public void setSedeLegale(SedeLegaleDto sedeLegale) {
		this.sedeLegale = sedeLegale;
	}
	public void setLegaleRappresentante(LegaleRappresentanteDto legaleRappresentante) {
		this.legaleRappresentante = legaleRappresentante;
	}
}
