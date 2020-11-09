/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.DateUtil;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.SegnalazioneValidazione;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * 30/04/2019 Per poter avere segnalazioni visualizzate in un'area separata nella pagina delle segnalazioni (ad esempio 
 * l'eventuale errore in ambito capofila/partner per cui non tutte le domande dei partner sono in stato Inviato) ho adottato 
 * come carattere separatore dei messaggi il #  
 * @author 
 *
 */
public class MostraSegnalazioniValidazioneAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "MostraSegnalazioniValidazioneAction";

	private String dataVerifica;
	private String oraVerifica;
	private List<SegnalazioneValidazione> segnalazioneValidazioneList;
	private List<SegnalazioneValidazione> altreSegnalazioneValidazioneList;
	
	@Override
	public String executeAction() throws SystemException,
			UnrecoverableException, CSIException {

		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + "BEGIN");

		TreeMap<String, Object> context = (TreeMap<String, Object>) getServletRequest().getSession().getAttribute(Constants.CONTEXT_ATTR_NAME);

		if (context != null) {
			// imposta la data e l'ora della verifica
			dataVerifica = DateUtil.getSysDate();
			oraVerifica = DateUtil.getSysHHMM();

			// lista complessiva dei messaggi ristrutturati (errori + warning)
			segnalazioneValidazioneList = new ArrayList<SegnalazioneValidazione>();

			// ristruttura gli errore e li aggiunge alla lista complessiva
			List<String> validationErrors = (List<String>) context.get(Constants.MESSAGE_KEY_VALIDATION_ERROR);
			List<SegnalazioneValidazione> segnalazioneValidazioneErrorList = wrapMessageToSegnalazione(validationErrors, true, Constants.MESSAGE_SEPARATOR_FOR_SPLIT_ALTRI_MSG);
			segnalazioneValidazioneList.addAll(segnalazioneValidazioneErrorList);

			// ristruttura i warning li aggiunge alla lista complessiva
			List<String> validationWarns = (List<String>) context.get(Constants.MESSAGE_KEY_VALIDATION_WARN);
			List<SegnalazioneValidazione> segnalazioneValidazioneWarnList = wrapMessageToSegnalazione(validationWarns, false, Constants.MESSAGE_SEPARATOR_FOR_SPLIT_ALTRI_MSG);
			segnalazioneValidazioneList.addAll(segnalazioneValidazioneWarnList);
			
			//estrae da validationErrors gli eventuali messaggi destinati ad una visualizzazione separata (e contraddistinti 
			//al loro interno dal separatore Constants.MESSAGE_SEPARATOR_FOR_SPLIT_ALTRI_MSG)
			List<SegnalazioneValidazione> tmpAltreSegnalazioneValidazioneList = estraiAltriMEssaggi(validationErrors, true, Constants.MESSAGE_SEPARATOR_FOR_SPLIT_ALTRI_MSG);
			if(tmpAltreSegnalazioneValidazioneList!=null && !tmpAltreSegnalazioneValidazioneList.isEmpty()){
				altreSegnalazioneValidazioneList = new ArrayList<SegnalazioneValidazione>(); 
				altreSegnalazioneValidazioneList.addAll(tmpAltreSegnalazioneValidazioneList);			
			}
		} else {
			log.debug(logprefix + "context NULL");
		}

		log.debug(logprefix + "END");
		return SUCCESS;
	}

	/**
	 * spezza il messaggio originale in al piu' tre parti, ogni parte e'
	 * separata dalla regex Constants.MESSAGE_SEPARATOR_FOR_SPLIT
	 */
	private List<SegnalazioneValidazione> wrapMessageToSegnalazione(List<String> validationMessageList, boolean flag, String SeparatoreAltriMsg) {
		List<SegnalazioneValidazione> segnalazioneValidazioneList = new ArrayList<SegnalazioneValidazione>();

		for (String msg : validationMessageList) {
			String codice;
			String tab1tab2;
			String testo;
			if(!msg.contains(SeparatoreAltriMsg)){ //altrimenti prende in considerazione anche gli altri messaggi
				String split[] = msg.split(Constants.MESSAGE_SEPARATOR_FOR_SPLIT);
				switch (split.length) {
				case 0:
					//
					codice = "";
					tab1tab2 = "";
					testo = "";
					break;
				case 1:
					// testo
					codice = "";
					tab1tab2 = "";
					testo = split[0];
					break;
				case 2:
					// pos : testo
					codice = "";
					tab1tab2 = split[0];
					testo = split[1];
					break;
				case 3:
				default:
					// cod : pos : testo
					codice = split[0];
					tab1tab2 = split[1];
					testo = split[2]; //  se codice != null && != "", ottenere il testo da una tabella di decodifica sul db 
					break;
				}
				//
				SegnalazioneValidazione sv = new SegnalazioneValidazione();
				sv.setCodice(codice);
				sv.setTab1tab2(tab1tab2);
				sv.setTesto(testo);
				sv.setBloccante(flag);
				//
				segnalazioneValidazioneList.add(sv);
			}
		}
		return segnalazioneValidazioneList;
	}

	private List<SegnalazioneValidazione> estraiAltriMEssaggi(List<String> validationMessageList, boolean flag, String separatore) {
		List<SegnalazioneValidazione> segnalazioneValidazioneList = new ArrayList<SegnalazioneValidazione>();

		for (String msg : validationMessageList) {
			String codice;
			String tab1tab2;
			String testo;
			
			// separatore = #
			// devo escludere i casi delle accentate (es &#233;)
			int posSeparatore = msg.indexOf(separatore);
			String charPrecSeparatore = "";
			if (posSeparatore>1){
				charPrecSeparatore = msg.charAt(posSeparatore-1)+"";
			}

			if(msg.contains(separatore) && !StringUtils.equals("&",charPrecSeparatore)){
				String split[] = msg.split(separatore);
				switch (split.length) {
				case 0:
					//
					codice = "";
					tab1tab2 = "";
					testo = "";
					break;
				case 1:
					// testo
					codice = "";
					tab1tab2 = "";
					testo = split[0];
					break;
				case 2:
					// pos : testo
					codice = "";
					tab1tab2 = split[0];
					testo = split[1];
					break;
				case 3:
				default:
					// cod : pos : testo
					codice = split[0];
					tab1tab2 = split[1];
					testo = split[2];
					break;
				}

				//
				SegnalazioneValidazione sv = new SegnalazioneValidazione();
				sv.setCodice(codice);
				sv.setTab1tab2(tab1tab2);
				sv.setTesto(testo);
				sv.setBloccante(flag);
				//
				segnalazioneValidazioneList.add(sv);
			}
		}
		return segnalazioneValidazioneList;
	}

	public String getDataVerifica() {
		return dataVerifica;
	}

	public void setDataVerifica(String dataVerifica) {
		this.dataVerifica = dataVerifica;
	}

	public String getOraVerifica() {
		return oraVerifica;
	}

	public void setOraVerifica(String oraVerifica) {
		this.oraVerifica = oraVerifica;
	}

	public List<SegnalazioneValidazione> getSegnalazioneValidazioneList() {
		return segnalazioneValidazioneList;
	}

	public void setSegnalazioneValidazioneList(
			List<SegnalazioneValidazione> segnalazioneValidazioneList) {
		this.segnalazioneValidazioneList = segnalazioneValidazioneList;
	}

	public List<SegnalazioneValidazione> getAltreSegnalazioneValidazioneList() {
		return altreSegnalazioneValidazioneList;
	}

	public void setAltreSegnalazioneValidazioneList(
			List<SegnalazioneValidazione> altreSegnalazioneValidazioneList) {
		this.altreSegnalazioneValidazioneList = altreSegnalazioneValidazioneList;
	}

}
