/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.integration.extservices.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import it.csi.findom.findomwebnew.presentation.util.Constants;

/**
 * @author riccardo.bova
 * @date 21 giu 2018
 */
public class MailDAOImpl implements MailDAO, InitializingBean {

	protected static final Logger logger = Logger.getLogger(Constants.APPLICATION_CODE + ".integration.extservices.mail");

	protected static final String DEFAULT_MAIL_FARM = "serverMail";
	protected static final String DEFAULT_MAIL_FROM = "sender";
	private ResourceBundle rB;

	public void send(String to, String oggetto, String testo, String soggettoBeneficiario) throws MessagingException, UnsupportedEncodingException {
		send(rB.getString(DEFAULT_MAIL_FARM), rB.getString(DEFAULT_MAIL_FROM), to, oggetto, testo, soggettoBeneficiario);
	}

	private void send(String mailFarm, String from, String to, String oggetto, String testo, String soggettoBeneficiario) throws MessagingException, UnsupportedEncodingException {
		logger.info("[MailDAOImpl::send] BEGIN");
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", mailFarm);

			Session session = Session.getDefaultInstance(props, null);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from, StringUtils.trimToEmpty(soggettoBeneficiario)));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(oggetto);
			message.setSentDate(new Date());
			message.setText(testo);

			Transport.send(message);

		} catch (MessagingException messagingException) {
			logger.error("[MailDAOImpl::send] MessagingException ", messagingException);
			throw messagingException;
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
			logger.error("[MailDAOImpl::send] UnsupportedEncodingException ", unsupportedEncodingException);
			throw unsupportedEncodingException;
		} finally {
			logger.info("[MailDAOImpl::send] END");
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			rB = ResourceBundle.getBundle("mail");
			logger.info("[MailDAOImpl::afterPropertiesSet] lette properties email");
		} catch (Throwable ex) {
			logger.error("[MailDAOImpl::afterPropertiesSet] ERRORE ", ex);
			throw new RuntimeException("[MailDAOImpl::afterPropertiesSet] ERRORE ", ex);
		}
		
	}
}
