/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.integration.extservices.mail;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

/**
 * @author riccardo.bova
 * @date 21 giu 2018
 */
public interface MailDAO {

	void send(String to, String oggetto, String testo, String soggettoBeneficiario) throws MessagingException, UnsupportedEncodingException;
}
