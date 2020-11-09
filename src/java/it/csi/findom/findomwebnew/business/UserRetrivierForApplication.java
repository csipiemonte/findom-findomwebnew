/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business;

import static it.csi.findom.findomwebnew.util.Utils.quote;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.csi.findom.findomwebnew.presentation.vo.UserInfo;
import it.csi.melograno.aggregatore.exception.AggregatoreException;
import it.csi.melograno.aggregatoreutil.util.UserRetrivier;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class UserRetrivierForApplication implements UserRetrivier {

	private static final Logger log = Logger.getLogger(Constants.APPLICATION_CODE + ".business");
	private static final String CLASS_NAME = "UserRetrivierForApplication";
	
	
	public String[] getUser(HttpSession session) throws AggregatoreException {
		final String methodName = "getUser";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.debug(logprefix + "BEGIN");
		
		// userId e userDesc per aggregatoreUtil
		UserInfo userInfo = getUserInfo(session, logprefix);
		StatusInfo statusInfo = (StatusInfo)session.getAttribute(Constants.STATUS_INFO);
		
		String[] user = new String[2];
		String userId = statusInfo.getCodFiscaleBeneficiario();

		user[0] = userId;
		user[1] = userInfo.getCognome() + " " + userInfo.getNome();
		log.info(logprefix + "user[0]:" + quote(user[0]));
		log.info(logprefix + "user[1]:" + quote(user[1]));
		
		log.debug(logprefix + "END");
		return user;
	}
	
	

	public String getUserId(HttpSession session) throws AggregatoreException {
		final String methodName = "getUserId";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.debug(logprefix + "BEGIN");

		StatusInfo statusInfo = (StatusInfo)session.getAttribute(Constants.STATUS_INFO);
		String userId = statusInfo.getCodFiscaleBeneficiario();
		log.info(logprefix + "userId:" + quote(userId));

		log.debug(logprefix + "END");
		return userId;
	}

	public String getUserDesc(HttpSession session) throws AggregatoreException {
		final String methodName = "getUserDesc";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.debug(logprefix + "BEGIN");

		// userDesc per aggregatoreUtil
		UserInfo userInfo = getUserInfo(session, logprefix);
		String userDesc = userInfo.getCognome() + " " + userInfo.getNome();
		log.info(logprefix + "userDesc:" + quote(userDesc));

		log.debug(logprefix + "END");
		return userDesc;
	}

	private UserInfo getUserInfo(HttpSession session, final String logprefix) throws AggregatoreException {
		// ottiene l'identita digitale come salvata in sessione
		String richUserId = session.getAttribute(Constants.IRIDE_ID_SESSIONATTR).toString();
		log.debug(logprefix + "richUserId:" + quote(richUserId));
		if (richUserId == null) {
			throw new AggregatoreException("Identita digitale non presente in sessione.");
		}

		// ottiene l'utente corrente come salvato in sessione
		UserInfo userInfo = (UserInfo) session.getAttribute(Constants.USERINFO_SESSIONATTR);
		log.debug(logprefix + "userInfo:" + quote(userInfo));
		if (userInfo == null) {
			throw new AggregatoreException("CurrentUser non presente in sessione per identitaIdParamname:" + richUserId);
		}

		return userInfo;
	}
	
}
