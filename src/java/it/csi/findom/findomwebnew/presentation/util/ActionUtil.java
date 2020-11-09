/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.util;

import it.csi.findom.findomwebnew.business.servizifindomweb.ServiziFindomWeb;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FormeGiuridicheDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.NumberMaxDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeBeneficiariDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.vo.Domanda;
import it.csi.findom.findomwebnew.presentation.vo.ImpresaEnte;
import it.csi.findom.findomwebnew.presentation.vo.SelItem;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class ActionUtil {

	private static final Logger log = Logger.getLogger(Constants.APPLICATION_CODE + ".presentation.util");
	private static final String CLASS_NAME = "ActionUtil";
	
	public static ArrayList<Domanda> getElencoDomande(Integer idSoggBeneficiario, Integer idSoggCreatore, String ruolo, String normativa, 
			String descBreveBando, String bando, String sportello, String statoDomanda, 
			String numDomanda, ServiziFindomWeb serviziFindomWeb) throws ServiziFindomWebException {
		
		final String methodName = "getElencoDomande";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		

		ArrayList<Domanda> listaDomandeInserite = null;
		
		if(idSoggBeneficiario==null || idSoggCreatore==null || StringUtils.isBlank(ruolo)){
			log.warn(logprefix + " dati non valorizzati, impossibile trovare le domande inserite.");
			
		}else{
			// annullo i valori di default (-1)
			if("-1".equals(normativa))
				normativa=null;
			
			if("-1".equals(descBreveBando))
				descBreveBando=null;
			
			if("-1".equals(bando))
				bando=null;
			
			if("-1".equals(sportello))
				sportello=null;
			
			if("-1".equals(statoDomanda))
				statoDomanda=null;
			
			listaDomandeInserite = serviziFindomWeb.getDomandeInserite(idSoggBeneficiario, idSoggCreatore , ruolo,
								normativa, descBreveBando, bando, sportello,statoDomanda, numDomanda);
	
			if(listaDomandeInserite!=null)
				log.info(logprefix + " listaDomandeInserite.size=="+listaDomandeInserite.size());
			else
				log.info(logprefix + " listaDomandeInserite NULL");
			
		}
		log.info(logprefix + " END");
		return listaDomandeInserite;
	}
		
	public static String checkCF(String cf) {
		final String methodName = "checkCF";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		String result = null;
		log.info(logprefix + " BEGIN");
		log.info(logprefix + "codiceFiscale:" + cf);
		
		String codice = cf;
		if(cf!=null){
			codice = cf.toUpperCase();
		}
		ControlCodFisc ctrlCF = new ControlCodFisc(codice);
		
		if (StringUtils.isBlank(codice)) {
			log.warn(logprefix + "codiceFiscale non valorizzato");
			result = Constants.ERR_MESSAGE_FIELD_VOID; //"valorizzare il campo";
			
		} else {
			if (codice.length() == 16 || codice.length() == 11) {
				if (codice.length() == 16) {
					if (!StringUtils.isAlphanumeric(codice)) {
						log.warn(logprefix + "codiceFiscale composto da caratteri proibiti");
						result = Constants.ERR_MESSAGE_FIELD_NOTALFA; //"inserire solo caratteri alfanumerici";
					}else if(!ctrlCF.controllaCheckDigit()) {
					    log.warn(logprefix + "codiceFiscale formalmente non corretto");
					    result = Constants.ERR_MESSAGE_FIELD_CFINVALID; //"codice fiscale formalmente non corretto";
					}
				} else {
					 //controllo cod fiscale (partita iva)
					 if (!ControlPartitaIVA.controllaPartitaIVA(codice)){
						 log.warn(logprefix + "controllo check digit su cod fiscale (partita iva) fallito");
						 result = Constants.ERR_MESSAGE_FIELD_CFINVALID; 
					 }
				}
			} else {
				log.warn(logprefix + "codice inserito non valido");
				result = Constants.ERR_MESSAGE_FIELD_CFINVALID; //"codice inserito non valido";
			}
		}
		log.info(logprefix + "result:" + result);
		log.info(logprefix + "  END");
		return result;
	}
	
	
	public static ArrayList<ImpresaEnte> popolaListaImpreseEntiToView(
			ArrayList<ShellSoggettiDto> listaImpreseEnti,
			HashMap<Integer, FormeGiuridicheDto> mappaFormeGiuridiche) {
		
		final String methodName = "popolaListaImpreseEntiToView";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		ArrayList<ImpresaEnte> lista = new ArrayList<ImpresaEnte>();
		
		for (Iterator itr = listaImpreseEnti.iterator(); itr.hasNext();) {
			ShellSoggettiDto sogg = (ShellSoggettiDto) itr.next();
			
			ImpresaEnte impEnte = new ImpresaEnte();
			impEnte.setIdSoggetto(sogg.getIdSoggetto());
			impEnte.setCodiceFiscale(sogg.getCodiceFiscale());
			impEnte.setDenominazione(sogg.getDenominazione());
			impEnte.setIdFormaGiuridica(sogg.getIdFormaGiuridica());
			impEnte.setCognome(sogg.getCognome());
			impEnte.setNome(sogg.getNome());
			
			FormeGiuridicheDto fg = mappaFormeGiuridiche.get(sogg.getIdFormaGiuridica());
			if(fg!=null){
				impEnte.setCodFormaGiuridica(fg.getCodFormaGiuridica());
				impEnte.setDescrFormaGiuridica(fg.getDescrFormaGiuridica());
				impEnte.setFlagPubblicoPrivato(fg.getFlagPubblicoPrivato());
			}
				
			lista.add(impEnte);
		}

		log.info(logprefix + " END");
		return lista;
	}
	
	/**
	 * Valido per mappe (Integer,String)
	 * @param mappa
	 * @return
	 */
	public static SelItem[] popolaArrayForCombo(Map mappa) {
		// trasformo le mappe in liste
			
		final String methodName = "popolaArrayForCombo";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		log.info(logprefix + " BEGIN");
		
		SelItem[] array = null;
		
		class MyComparator implements Comparator<SelItem>{
			@Override
	        public int compare(SelItem item1, SelItem item2)
	        {
	            return  item1.getValue().compareTo(item2.getValue());
	        }
		}
		
		if(mappa != null && mappa.size()>0){
			
			List<SelItem> lista = new ArrayList<SelItem>();
			Iterator itr = mappa.entrySet().iterator();
			
			while (itr.hasNext()) {
				Entry t = (Entry) itr.next();
				Integer chiave = (Integer)t.getKey();
				String valore = (String)t.getValue();
				log.info(logprefix + " chiave="+chiave + " , valore="+valore);
				
				SelItem s = new SelItem();
				s.setKey(chiave+"");
				s.setValue(valore);
				
				lista.add(s);
			}
	
			Collections.sort(lista, new MyComparator());
						
			if(lista!=null && lista.size()>0){
				log.info(logprefix + " lista size="+lista.size());
				array = new SelItem[lista.size()];
				lista.toArray(array);
			}else{
				log.info(logprefix + " lista NULL o vuota");
			}
		}
		
		log.info(logprefix + " END");
		return array;
	}
	
	/**
	 * Come popolaArrayForCombo ma la chiave della mappa e' String
	 * Valido per mappe (String,String)
	 * 
	 * @param mappa
	 * @return
	 */
	public static SelItem[] popolaArrayStatiForCombo(Map mappa) {
		// trasformo le mappe in liste
			
		final String methodName = "popolaArrayForCombo";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		SelItem[] array = null;
		
		class MyComparator implements Comparator<SelItem>{
			@Override
	        public int compare(SelItem item1, SelItem item2)
	        {
	            return  item1.getValue().compareTo(item2.getValue());
	        }
		}
		
		if(mappa != null && mappa.size()>0){
			
			List<SelItem> lista = new ArrayList<SelItem>();
			Iterator itr = mappa.entrySet().iterator();
			
			while (itr.hasNext()) {
				Entry t = (Entry) itr.next();
				String chiave = (String)t.getKey();
				String valore = (String)t.getValue();
				log.info(logprefix + " chiave="+chiave + " , valore="+valore);
	
				SelItem s = new SelItem();
				s.setKey(chiave);
				s.setValue(valore);
				
				lista.add(s);
			}
	
			Collections.sort(lista, new MyComparator());

			if(lista!=null && lista.size()>0){
				log.info(logprefix + " lista size="+lista.size());
				array = new SelItem[lista.size()];
				lista.toArray(array);
			}else{
				log.info(logprefix + " lista NULL o vuota");
			}
		}
		
		log.info(logprefix + " END");
		return array;
	}


	/**
	 * Data una domanda, verifica che la data odierna sia compresa nelle date di inzio-fine sportello
	 * @param aggrDomanda
	 * @return
	 */
	public static boolean isSportelloOpen(AggrDataDto aggrDomanda) {
		final String methodName = "isSportelloOpen";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		boolean res = false;
		String dataApSp = aggrDomanda.getDataAperturaSportello();
		String dataChSp = aggrDomanda.getDataChiusuraSportello();
		log.info(logprefix + " dataApSp="+dataApSp);
		log.info(logprefix + " dataChSp="+dataChSp);
		
		Date dataApertura;
		Date dataChiusura;
		
		try {
			//dataApertura = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dataApSp);
			dataApertura = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataApSp);
			log.info(logprefix + " dataApertura="+dataApertura);
			
			// potrebbero esserci date di fine sportello NULLE
			if(StringUtils.isNotBlank(dataChSp)){
				//dataChiusura = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dataChSp);
				dataChiusura = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataChSp);
				log.info(logprefix + " dataChiusura="+dataChiusura);
				
				Date oggi = new Date();
				log.info(logprefix + " oggi="+oggi);
				
				if(oggi.after(dataApertura) && oggi.before(dataChiusura)){
					log.info(logprefix + " la data odierna e' interna all'intervallo dello sportello");
					res = true;
				}
			}else{
				log.info(logprefix + " data fine sportello NULLA");
				res = true;
			}
		} catch (ParseException e) {
			log.error(logprefix +" errore nel parsing delle date: "+e);
		}
		
		log.info(logprefix + " END");
		return res;
	}
	
	/**
	 * Verifico se l'utente ha gia' INVIATO il numero massimo di domande consentito // algoritmo A01
	 */
	public static boolean isMaxNumDomandeInviatePerBando(StatusInfo state, ServiziFindomWeb serviziFindomWeb) throws ServiziFindomWebException{
		final String methodName = "isMaxNumDomandeInviatePerBando";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		boolean massimoDomandePresentate = false;
		
		// estraggo dal database il numero massimo di domande per lo sportello in corso e per il bando in corso
		NumberMaxDto numeroMaxDomande =  serviziFindomWeb.getNumeroMassimoDomandeInviate(state.getNumProposta());

		if(numeroMaxDomande!=null){
			Integer nMaxDomB = numeroMaxDomande.getNumMaxDomandeBando(); // se sul DB findom_t_bandi.num_max_domande e' nullo, qui vale 0
			log.info(logprefix + " nMaxDomB="+nMaxDomB);
			
			if(nMaxDomB>0){
				
				// verifica che esistano delle domande inviate associate al bando per il beneficiario
				// l'array trovato deve avere al massimo 1 elemento
				ArrayList<VistaDomandeBeneficiariDto> listaDomandeInviate = serviziFindomWeb.getVistaDomandeBeneficiari(state.getTemplateId(), state.getIdSoggettoBeneficiario());
				
				if(listaDomandeInviate!=null && listaDomandeInviate.size()>0){
					
					// il beneficiario ha inviato almeno una domanda
					int nDomBando = listaDomandeInviate.get(0).getNumDomandeBando();
					log.info(logprefix + " nDomBando="+nDomBando);
					
					if((nMaxDomB>0 && nDomBando < nMaxDomB) || nMaxDomB==0 ){
						// limiti massimi nulli o numero domande presentate inferiore al massimo previsto. Proseguo con l'INVIO
						log.info(logprefix + " limiti massimi nulli o numero domande presentate inferiore al massimo previsto. Proseguo con l'INVIO");
					}else{
						massimoDomandePresentate = true;
					}
				}else{
					// non ci sono domande in stato INVIATA a carico del beneficiario per quel bando.
					log.info(logprefix + " non ci sono domande in stato INVIATA a carico del beneficiario per quel bando. Proseguo con l'INVIO");
				}
			}else{
				// entrambi i limiti massimi sono nulli. 
				log.info(logprefix + " entrambi i limiti massimi sono nulli. Posso proseguire con l'invio.");
			}
			
		}else{
			log.warn(logprefix + " numeroMaxDomande NULLA ");
		}
		log.warn(logprefix + " massimoDomandePresentate bando = " + massimoDomandePresentate);

		log.info(logprefix + " END");
		return massimoDomandePresentate;
	}
	
	/**
	 * Verifico se l'utente ha gia' INVIATO il numero massimo di domande consentito // algoritmo A01
	 */
	public static boolean isMaxNumDomandeInviatePerSportello(StatusInfo state, ServiziFindomWeb serviziFindomWeb) throws ServiziFindomWebException{
		final String methodName = "isMaxNumDomandeInviateSportello";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		boolean massimoDomandePresentate = false;
		
		// estraggo dal database il numero massimo di domande per lo sportello in corso e per il bando in corso
		NumberMaxDto numeroMaxDomande =  serviziFindomWeb.getNumeroMassimoDomandeInviate(state.getNumProposta());
		
		if(numeroMaxDomande!=null){
			Integer nMaxDomS = numeroMaxDomande.getNumMaxDomandeSportello();// se sul DB findom_t_sportelli_bandi.num_max_domande e' nullo, qui vale 0
			log.info(logprefix + " nMaxDomS="+nMaxDomS);
			
			if(nMaxDomS>0){
				
				// verifica che esistano delle domande inviate associate al bando per il beneficiario
				// l'array trovato deve avere al massimo 1 elemento
				ArrayList<VistaDomandeBeneficiariDto> listaDomandeInviate = serviziFindomWeb.getVistaDomandeBeneficiari(state.getTemplateId(), state.getIdSoggettoBeneficiario());
				
				if(listaDomandeInviate!=null && listaDomandeInviate.size()>0){
					
					// il beneficiario ha inviato almeno una domanda
					int nDomSport = listaDomandeInviate.get(0).getNumDomandeSportello();
					log.info(logprefix + " nDomSport="+nDomSport);
					
					if((nMaxDomS>0 && nDomSport < nMaxDomS) || nMaxDomS==0){
						// limiti massimi nulli o numero domande presentate inferiore al massimo previsto. Proseguo con l'INVIO
						log.info(logprefix + " limiti massimi nulli o numero domande presentate inferiore al massimo previsto. Proseguo con l'INVIO");
					}else{
						massimoDomandePresentate = true;
					}
				}else{
					// non ci sono domande in stato INVIATA a carico del beneficiario per quel bando.
					log.info(logprefix + " non ci sono domande in stato INVIATA a carico del beneficiario per quel bando. Proseguo con l'INVIO");
				}
			}else{
				// entrambi i limiti massimi sono nulli. 
				log.info(logprefix + " entrambi i limiti massimi sono nulli. Posso proseguire con l'invio.");
			}
			
		}else{
			log.warn(logprefix + " numeroMaxDomande NULLA ");
		}
		log.warn(logprefix + " massimoDomandePresentate sportello" + massimoDomandePresentate);

		log.info(logprefix + " END");
		return massimoDomandePresentate;
	}
}
