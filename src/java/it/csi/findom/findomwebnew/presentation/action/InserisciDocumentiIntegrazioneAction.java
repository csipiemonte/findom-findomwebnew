/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.DocumentiIntegrativiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomDFunzionariDTO;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDirezioneDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDomandeDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.integration.extservices.mail.MailDAO;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.vo.UserInfo;

public class InserisciDocumentiIntegrazioneAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	static public final Long IDALLEGATOINTEGRATIVO = 0L;
	private static final String CLASS_NAME = "InserisciDocumentiIntegrazioneAction";

	// input
	private Long idDomanda;
	private Long idBando;
	private String statoRichiesta;
	private List<String> errorsList;
	// output
	private List<DocumentiIntegrativiDto> listaDocumenti;
	private ShellDomandeDto shellDomandeDto;
	private FindomTBandiDirezioneDto findomTBandiDirezioneDto;
	private FindomDFunzionariDTO findomDFunzionariDTO;

	private MailDAO mailDAO;

	public String executeAction() throws ServiziFindomWebException {

		if (idDomanda == null)
			throw new ServiziFindomWebException("idDomanda Non valorizzato");
		if (idBando == null)
			throw new ServiziFindomWebException("idBando Non valorizzato");
		if (statoRichiesta == null)
			throw new ServiziFindomWebException("statoRichiesta Non valorizzato");

		setOutputList();
		if (errorsList != null && !errorsList.isEmpty())
			for (String entry : errorsList)
				addActionError(entry);

		return SUCCESS;
	}

	public static String OGGETTO_MAIL = "Bando %s –Integrazione documenti completata";
	public static String TESTO_MAIL = "L’ente %s ha completato l’integrazione dei documenti richiesti per la domanda %s del bando %s .\n \n Attenzione! Non rispondere a questa mail.";
	public static final String ERROR_MAIL = "Non è stato possibile inviare una mail di conferma all’Istruttore amministrativo, contattarlo altrimenti.";

	public String chiudiIntegrazione() throws ServiziFindomWebException, MessagingException {

		final String methodName = "chiudiIntegrazione";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		log.debug(logprefix + " idDomanda=" + idDomanda);

		if (idDomanda == null)
			throw new ServiziFindomWebException("idDomanda Non valorizzato");
		UserInfo userInfo = getUserInfo();
		getServiziFindomWeb().chiudiIntegrazione(idDomanda, userInfo);

		statoRichiesta = Constants.RICHIESTA_CHIUSA;
		setOutputList();
		String descBreveBando = "";
		String soggettoBeneficiario = "";

		try {
			// get dati for mail
			descBreveBando = StringUtils.trimToEmpty(findomTBandiDirezioneDto.getDescBreveBando());
			ArrayList<String> sogg = new ArrayList<String>();
			sogg.add(String.valueOf(shellDomandeDto.getIdSoggettoBeneficiario()));
			ArrayList<ShellSoggettiDto> soggettiBeneficiari = getServiziFindomWeb().getDatiSoggettoByIdSoggetto(sogg);

			if (soggettiBeneficiari != null && !soggettiBeneficiari.isEmpty())
				soggettoBeneficiario = soggettiBeneficiari.get(0).getDenominazione();

		} catch (Exception e) {
			addActionError(ERROR_MAIL);
			log.debug(logprefix + "Errore reperimento dati soggetti per invio mail");
		}

		String testo = String.format(TESTO_MAIL, soggettoBeneficiario, shellDomandeDto.getIdDomanda(), descBreveBando);
		String to = findomDFunzionariDTO != null ? findomDFunzionariDTO.getEmail() : "";
		String oggetto = String.format(OGGETTO_MAIL, descBreveBando);

		if (StringUtils.isEmpty(to)) {
			log.debug(logprefix + "Errore manca destinatario invio mail");
			addActionError(ERROR_MAIL);
		} else {
			try {
				mailDAO.send(to, oggetto, testo.toString(), soggettoBeneficiario);
			} catch (Exception e) {
				addActionError(ERROR_MAIL);
				log.debug(logprefix + "Errore invio mail");
				log.error(e.getMessage(), e);
			}
		}

		addActionMessage("Chiusura integrazione effettuata con successo");
		log.debug(logprefix + " END");

		return SUCCESS;
	}

	private void setOutputList() throws ServiziFindomWebException {
		// recupero documenti integrativi
		listaDocumenti = getServiziFindomWeb().getDocumentiIntegrativi(idDomanda, Constants.ID_ALLEGATO_DOCUMENTO_INTEGRATIVO);
		shellDomandeDto = getServiziFindomWeb().findoOneShellTDomanda(idDomanda);
		findomTBandiDirezioneDto = getServiziFindomWeb().findInfoDirezioneBando(idBando);
		findomDFunzionariDTO = getServiziFindomWeb().findOneFindomDFunzionari(shellDomandeDto.getIdFunzionarioIntegrazione());
	}

	public String getStatoRichiesta() {
		return statoRichiesta;
	}

	public void setStatoRichiesta(String statoRichiesta) {
		this.statoRichiesta = statoRichiesta;
	}

	public FindomDFunzionariDTO getFindomDFunzionariDTO() {
		return findomDFunzionariDTO;
	}

	public void setFindomDFunzionariDTO(FindomDFunzionariDTO findomDFunzionariDTO) {
		this.findomDFunzionariDTO = findomDFunzionariDTO;
	}

	public Long getIdBando() {
		return idBando;
	}

	public void setIdBando(Long idBando) {
		this.idBando = idBando;
	}

	public ShellDomandeDto getShellDomandeDto() {
		return shellDomandeDto;
	}

	public void setShellDomandeDto(ShellDomandeDto shellDomandeDto) {
		this.shellDomandeDto = shellDomandeDto;
	}

	public FindomTBandiDirezioneDto getFindomTBandiDirezioneDto() {
		return findomTBandiDirezioneDto;
	}

	public void setFindomTBandiDirezioneDto(FindomTBandiDirezioneDto findomTBandiDirezioneDto) {
		this.findomTBandiDirezioneDto = findomTBandiDirezioneDto;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public List<DocumentiIntegrativiDto> getListaDocumenti() {
		return listaDocumenti;
	}

	public List<String> getErrorsList() {
		return errorsList;
	}

	public void setErrorsList(List<String> errorsList) {
		this.errorsList = errorsList;
	}

	public void setListaDocumenti(List<DocumentiIntegrativiDto> listaDocumenti) {
		this.listaDocumenti = listaDocumenti;
	}

	public MailDAO getMailDAO() {
		return mailDAO;
	}

	public void setMailDAO(MailDAO mailDAO) {
		this.mailDAO = mailDAO;
	}

}
