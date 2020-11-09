/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.integration.extservices.ipa;

import it.csi.findom.findomwebnew.dto.ipa.Ipa;
import it.csi.findom.findomwebnew.presentation.util.Constants;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

public class IpaDAOImpl implements IpaDAO {

	private static final String CLASS_NAME = "IpaDAOImpl";

	protected static final Logger log = Logger
			.getLogger(Constants.APPLICATION_CODE
					+ ".integration.extservices.ipa");

	private ResourceBundle rB;
	private Properties properties;

	public void init() {
		final String methodName = "init";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");

		try {
			ResourceBundle rB = ResourceBundle.getBundle("ipa");

			String url = rB.getString("ldapUrl");
			String user = rB.getString("ldapUser");
			String pwd = rB.getString("ldapPwd");

			log.debug(logprefix + " url=" + url + ", user=[" + user
					+ "], pwd=[" + pwd + "]");
			log.debug(logprefix + " LDAP IPA inizializzato");

		} catch (Exception e) {
			log.error(logprefix + " Exception " + e.getMessage());
		}

		log.debug(logprefix + " END");
	}

	public IpaDAOImpl() {
		final String methodName = "IpaDAOImpl";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");

		rB = ResourceBundle.getBundle("ipa");
		log.debug(logprefix + " letto file configurazione");

		properties = initProps();
		log.debug(logprefix + " lette properties ipa");
		log.debug(logprefix + "END");
	}

	private Properties initProps() {
		final String methodName = "initProps";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");

		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, rB.getString("ldapUrl"));
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, rB.getString("ldapUser"));
		env.put(Context.SECURITY_CREDENTIALS, rB.getString("ldapPwd"));

		log.debug(logprefix + " END");
		return env;
	}

	public Ipa searchEnteOnIPA(String codFiscale) {
		final String methodName = "searchEnteOnIPA";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN, codFiscale=" + codFiscale);

		Ipa ipa = null;

		try {
			DirContext ctx = new InitialDirContext(properties);

			log.debug(logprefix + " ctx OK");

			SearchControls ctls = new SearchControls();
			log.debug(logprefix + " ctls OK");

			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String filter = "(&((objectclass=amministrazione) (codiceFiscaleAmm=" + codFiscale + ")))";
			log.debug(logprefix + " filter=["+filter+"]");
			
			NamingEnumeration answer = ctx.search("c=it", filter, ctls);

			if (answer != null && !answer.hasMoreElements())
				log.debug(logprefix + " nessun elemento trovato su IPA");

			while (answer.hasMoreElements()) {

				SearchResult a = (SearchResult) answer.nextElement();

				Attributes result = a.getAttributes();

				if (result == null) {
					log.debug(logprefix + "Attributi non presenti");
				} else {

					ipa = new Ipa();
					ipa.setCodiceIPA(getIpaValue( result.get("o")));
					log.debug(logprefix + "IPA="+getIpaValue( result.get("o")));

					ipa.setCodiceFiscaleAmm(getIpaValue( result.get("codiceFiscaleAmm")));
					ipa.setCognomeResp(getIpaValue( result.get("cognomeResp")));
			    	ipa.setContatti(getIpaValue( result.get("contatti")));
					ipa.setDataVerifica(getIpaValue( result.get("dataVerifica")));
					ipa.setDescrizione(getIpaValue( result.get("description")));
					ipa.setElle(getIpaValue( result.get("l")));
					ipa.setMacroTipoAmm(getIpaValue( result.get("macroTipoAmm")));
					ipa.setMail(getIpaValue( result.get("mail")));
					ipa.setNomeResp(getIpaValue( result.get("nomeResp")));
					ipa.setCodicePostale(getIpaValue( result.get("postalCode")));
					ipa.setProvincia(getIpaValue( result.get("provincia")));
					ipa.setRegione(getIpaValue( result.get("regione")));
					ipa.setSitoIstituzionale(getIpaValue( result.get("sitoIstituzionale")));
					ipa.setSt(getIpaValue( result.get("st")));
					ipa.setVia(getIpaValue( result.get("street")));
					ipa.setTipoAmm(getIpaValue( result.get("tipoAmm")));
					ipa.setTitoloResp(getIpaValue( result.get("titoloResp")));
				}
			}

		} catch (NamingException e) {
			log.error(logprefix + "NamingException e=" + e.getMessage());
			e.printStackTrace();
		}

		log.debug(logprefix + " END");
		return ipa;
	}

	private String getIpaValue(Attribute attr) {
		String r = null;
		if(attr!=null){
    		try {
				r= (String)attr.get();
			} catch (NamingException e) {
				log.error("[IpaDAOImpl::getIpaValue ]NamingException e=" + e.getMessage());
			}
    	}
		return r;
	}
	
}
