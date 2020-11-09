/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.findom.findomwebnew.presentation.util.Constants;

public class StampaDomandaDaIndiceAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "StampaDomandaDaIndiceAction";
	
	private Integer idDomanda;
	
	@Override
	public String executeAction() 
	{
		final String DOMANDA_INVIATA = "domanda_inviata";
		final String DOMANDA_NON_INVIATA = "domanda_non_inviata";
	
		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.debug(logprefix + " BEGIN");
		
		idDomanda = getStatus().getNumProposta();
		String stato = getStatus().getStatoProposta();
		log.debug(logprefix + "  idDomanda = [" + idDomanda + "]");
		
		if ((stato != null)&&
			(Constants.STATO_CONCLUSA.equals(stato) || Constants.STATO_INVIATA.equals(stato))) {
			return DOMANDA_INVIATA;
		} 
		
		return DOMANDA_NON_INVIATA;
	}
	
	public Integer getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Integer idDomanda) {
		this.idDomanda = idDomanda;
	}
}
