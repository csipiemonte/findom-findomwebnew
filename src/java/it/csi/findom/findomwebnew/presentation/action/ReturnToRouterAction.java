/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.findom.findomwebnew.presentation.util.Constants;
import java.net.URLEncoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.Action;

public class ReturnToRouterAction extends BaseAction {
			
	public String executeAction() {
		
		final String method = "executeAction";
		String actionToRet = Action.SUCCESS;
		
		log.debug("[" + getClass().getName()+ "::" + method + "]  "+ "BEGIN");
		log.debug("[" + getClass().getName()+ "::" + method + "]  "+
					"urlRouting=" + getUrlRouting());
		log.debug("[" + getClass().getName()+ "::" + method + "]  "+ " END return success");
		
		return actionToRet;
	}
	
	@SuppressWarnings("deprecation")
	public String cercaDomande() throws Exception {
		
		final String method = "cercaDomande";
		log.debug("[" + getClass().getName()+ "::" + method + "]  BEGIN");
		String result = SUCCESS;
		
		StringBuffer url = new StringBuffer();
		url.append("/");
		url.append(getUrlRouting());
		url.append("/cercaDomandeRouter.do");
		appendSessionParametersToUrl(url);
	
		log.info("[" + getClass().getName()+ "::" + method + "]  "+ "nextUrl= " + url.toString());

		if (url != null) {
			getResponse().sendRedirect(
					getResponse().encodeRedirectURL(url.toString()));
		} 
		
		return result;
	}

	@SuppressWarnings("deprecation")
	public String cambiaProfilo() throws Exception {
		final String method = "cambiaProfilo";
		log.debug("[" + getClass().getName()+ "::" + method + "]  ");
		String result = SUCCESS;
		
		StringBuffer url = new StringBuffer();
		url.append("/");
		url.append(getUrlRouting());
		url.append("/cambiaProfiloRouter.do");
		appendSessionParametersToUrl(url);

		log.info("[" + getClass().getName()+ "::" + method + "]  "+ "nextUrl= " + url.toString());

		if (url != null) {
			getResponse().sendRedirect(
					getResponse().encodeRedirectURL(url.toString()));
		}
		return result;	
	}
	@SuppressWarnings("deprecation")
	public String home() throws Exception {
		final String method = "home";
		log.debug("[" + getClass().getName()+ "::" + method + "]  ");
		String result = SUCCESS;
		StringBuffer url = new StringBuffer();
		url.append("/");
		url.append(getUrlRouting());
		url.append("/homeRouter.do");
		
		appendSessionParametersToUrl(url);
		
		log.info("[" + getClass().getName()+ "::" + method + "]  "+ "nextUrl= " + url.toString());

		if (url != null) {
			getResponse().sendRedirect(
					getResponse().encodeRedirectURL(url.toString()));
		}
		return result;	
	}
	
	@SuppressWarnings("deprecation")
	public String esci() throws Exception {
		final String method = "esci";
		log.debug("[" + getClass().getName()+ "::" + method + "]  ");
		String result = SUCCESS;
		StringBuffer url = new StringBuffer();
		url.append("/");
		url.append(getUrlRouting());
		url.append("/esci.do");
		
		log.info("[" + getClass().getName()+ "::" + method + "]  "+ "nextUrl= " + url.toString());

		if (url != null) {
			getResponse().sendRedirect(
					getResponse().encodeRedirectURL(url.toString()));
		}
		return result;	
	}
	
	private void appendSessionParametersToUrl(StringBuffer url) throws Exception {
		StringBuffer param = new StringBuffer();
		
		Gson gson = new GsonBuilder()
	    .disableHtmlEscaping()
	    .create();
		
		if (getServletRequest().getSession().getAttribute("risultatoKO")!=null) {
			if (param.length() > 0) {
				param.append("&");
			}
			param.append("risultatoKO").append("=").append(URLEncoder.encode((String)getServletRequest().getSession().getAttribute("risultatoKO"), Constants.UTF8));			
		}
		if (getServletRequest().getSession().getAttribute("risultatoOK")!=null) {
			if (param.length() > 0) {
				param.append("&");
			}
			param.append("risultatoOK").append("=").append(URLEncoder.encode((String)getServletRequest().getSession().getAttribute("risultatoOK"), Constants.UTF8));
		}		
		if ( getActionErrors()!=null && getActionErrors().size() > 0) {
			String errori =  gson.toJson(getActionErrors());
			if (param.length() > 0) {
				param.append("&");
			}
			param.append("errori").append("=").append(URLEncoder.encode(errori, Constants.UTF8));
		}
		
		url.append("?"+param);
		resetSessionAttributes();
	}
	
	@SuppressWarnings("unchecked")
	private void resetSessionAttributes() {
		
		getServletRequest().getSession().removeAttribute("risultatoOK");
		getServletRequest().getSession().removeAttribute("risultatoKO");
		getServletRequest().getSession().removeAttribute("errori");
		getServletRequest().getSession().removeAttribute("vociDiSpesaConsList");

		// rimuovo gli indici messi in sessione dall'aggregatore
		getServletRequest().getSession().setAttribute(Constants.INDEXTREE_NAME, null);

	}
}
