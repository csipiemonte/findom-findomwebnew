/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import it.csi.findom.findomwebnew.business.servizifindomweb.ServiziFindomWeb;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.NumberMaxDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeBeneficiariDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.util.ActionUtil;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.csi.util.performance.StopWatch;

public class DomandaAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "DomandaAction";

	String sportelloOpen;  // attributo per differenziare il testo della modale di conferma invio domanda in base al fatto che lo soprtello sia aperto o meno
	String numMaxDomandeBandoPresentate;// attributo per differenziare il testo della modale di conferma invio domanda in base al fatto che l'utente abbia inviato il numero massimo di domande o meno
	String numMaxDomandeSportelloPresentate;// attributo per differenziare il testo della modale di conferma invio domanda in base al fatto che l'utente abbia inviato il numero massimo di domande o meno
	String bandoMaterializzatoSenzaPEC;// attributo per differenziare il testo della modale di conferma invio domanda per bandi non Dematerializzati che non devono inviare PEC
	
	@Override
	public String executeAction()  {
		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.info(logprefix + "BEGIN");
		StopWatch stopWatch = new StopWatch(Constants.APPLICATION_CODE);

		stopWatch.start();
		
		// verifico se lo sportello e' aperto
		// questo serve per cambiare il messaggio nella modale di conferma invio domanda
		
		StatusInfo state = getStatus();
		if(state==null){
			log.info(logprefix + " state NULL");
		}else{
			log.info(logprefix + " state.getNumProposta()="+state.getNumProposta());
		}
		
		if(state==null || (state!=null && state.getNumProposta()==null )){
			log.info(logprefix + " state.toString()="+state.toString());
			log.error(logprefix +" oggetto statusInfo in sessione incompleto.");
			stopWatch.stop();
			stopWatch.dumpElapsed(CLASS_NAME, methodName, "Renderizzazione action con oggetto in sessione incompleto", "msg");
			return "sessionExpired";
		}
		AggrDataDto aggrDomanda = new AggrDataDto();
		aggrDomanda.setDataAperturaSportello(state.getAperturaSportelloDa());
		aggrDomanda.setDataChiusuraSportello(state.getAperturaSportelloA());
		log.info(logprefix + " lette date sportello da status");
		
		// verifico che lo sportello sia aperto e passo il risultato alla aggregatoreUtilImpl.jsp in modo da gestire un messaggio nella modale di conferma invio
		// questo controllo e' bloccante
		boolean isOpen = ActionUtil.isSportelloOpen (aggrDomanda);
		log.info(logprefix + " isOpen="+isOpen);
		if(isOpen)
			setSportelloOpen("true");
		else
			setSportelloOpen("false");
		
		// verifico il numero di domande inviate e passo il risultato alla aggregatoreUtilImpl.jsp in modo da gestire un messaggio nella modale di conferma invio
		// questo controllo non e' bloccante
		setNumMaxDomandeBandoPresentate("false");
		setNumMaxDomandeSportelloPresentate("false");
		setBandoMaterializzatoSenzaPEC("false");
		
		try {

// TODO verificare se usare:
//			if (ActionUtil.isMaxNumDomandeInviatePerBando(state, getServiziFindomWeb())) {
			if (isMaxNumDomandeInviatePerBando(state, getServiziFindomWeb())) {
				setNumMaxDomandeBandoPresentate("true");
				log.warn(logprefix + " il numero delle domande del bando e' >= al numero massimo consentito");
			}
			
			if (isMaxNumDomandeInviatePerSportello(state, getServiziFindomWeb())) {
				setNumMaxDomandeSportelloPresentate("true");
				log.warn(logprefix + " il numero delle domande dello sportello e' >= al numero massimo consentito");
			}
			
			String flagBandoDematerializzato = state.getFlagBandoDematerializzato();
			if(flagBandoDematerializzato!=null){
				flagBandoDematerializzato = flagBandoDematerializzato.toUpperCase();
			}
			log.info(logprefix + " flagBandoDematerializzato: " + flagBandoDematerializzato);
			
			Boolean usoIndex = (Boolean) getServletRequest().getSession().getAttribute("flagUploadIndex_bando"+state.getTemplateId());
			if(usoIndex == null){
				FindomTBandiDto band = getServiziFindomWeb().findoOneFindomTBandi(new Long(state.getTemplateId()));
				log.info(logprefix + "bando="+state.getTemplateId()+", file su Index="+band.getFlagUploadIndex());
				getServletRequest().getSession().setAttribute("flagUploadIndex_bando" + state.getTemplateId(), band.getFlagUploadIndex());
				usoIndex = band.getFlagUploadIndex();
			}else{
				log.info(logprefix + "leggo da sessione, bando=" + state.getTemplateId() + ", file su Index="+usoIndex);
			}
			
			if (!StringUtils.equals(Constants.FLAG_BANDO_DEMATERIALIZZATO, flagBandoDematerializzato) && !usoIndex){
				log.info(logprefix + "imposto setBandoMaterializzatoSenzaPEC = true");
				setBandoMaterializzatoSenzaPEC("true");
			}
			
		} catch (ServiziFindomWebException e) {
			stopWatch.stop();
			stopWatch.dumpElapsed(CLASS_NAME, methodName, "Renderizzazione action con problema nel numMax domande", "msg");
			log.error(logprefix +" errore nella verifica del numero di domande inviate "+e);
			return ERROR;
		}

		stopWatch.stop();
		stopWatch.dumpElapsed(CLASS_NAME, methodName, "Renderizzazione action success", "msg");
		log.info(logprefix + "END");
		return SUCCESS;
	}


	private boolean isMaxNumDomandeInviatePerSportello(StatusInfo state,
			ServiziFindomWeb serviziFindomWeb) throws ServiziFindomWebException {

		final String methodName = "isMaxNumDomandeInviateSportello";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		boolean massimoDomandePresentate = false;
		
		String nmdps = (String)getServletRequest().getSession().getAttribute("numMaxDomandeSportello_domanda"+state.getNumProposta());
		log.debug(logprefix + " numMaxDomandeSportello_domanda" + state.getNumProposta()+"="+nmdps);
		
		if(!StringUtils.equals(nmdps, "0")){
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
					getServletRequest().getSession().setAttribute("numMaxDomandeSportello_domanda"+state.getNumProposta(),"0");
				}
				
			}else{
				log.warn(logprefix + " numeroMaxDomande NULLA ");
				getServletRequest().getSession().setAttribute("numMaxDomandeSportello_domanda"+state.getNumProposta(),"0");
			}

		} else {
			log.debug(logprefix + " massimoDomandePresentate letto da sessione" );
		}
		log.debug(logprefix + " massimoDomandePresentate sportello" + massimoDomandePresentate);
		log.info(logprefix + " END");
		return massimoDomandePresentate;
	}


	private boolean isMaxNumDomandeInviatePerBando(StatusInfo state,
			ServiziFindomWeb serviziFindomWeb) throws ServiziFindomWebException{
		final String methodName = "isMaxNumDomandeInviatePerBando";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		
		boolean massimoDomandePresentate = false;
		
		String nmdpb = (String)getServletRequest().getSession().getAttribute("numMaxDomandeBando_domanda"+state.getNumProposta());
		log.debug(logprefix + " numMaxDomandeBando_domanda" + state.getNumProposta() + " = " +nmdpb);
		
		if(!StringUtils.equals(nmdpb, "0")){
			
			// estraggo dal database il numero massimo di domande per lo sportello in corso e per il bando in corso
			NumberMaxDto numeroMaxDomande =  getServiziFindomWeb().getNumeroMassimoDomandeInviate(state.getNumProposta());
			
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
					getServletRequest().getSession().setAttribute("numMaxDomandeBando_domanda"+state.getNumProposta(),"0");
				}
				
			}else{
				log.warn(logprefix + " numeroMaxDomande NULLA ");
				getServletRequest().getSession().setAttribute("numMaxDomandeBando_domanda"+state.getNumProposta(),"0");
			}
		
		} else {
			log.debug(logprefix + " massimoDomandePresentate letto da sessione" );
		}
		
		log.debug(logprefix + " massimoDomandePresentate bando = " + massimoDomandePresentate);
		log.info(logprefix + " END");
		return massimoDomandePresentate;
	}


	// GETTERS && SETTERS
	public String getNumMaxDomandeBandoPresentate() {
		return numMaxDomandeBandoPresentate;
	}

	public void setNumMaxDomandeBandoPresentate(String numMaxDomandeBandoPresentate) {
		this.numMaxDomandeBandoPresentate = numMaxDomandeBandoPresentate;
	}

	public String getNumMaxDomandeSportelloPresentate() {
		return numMaxDomandeSportelloPresentate;
	}

	public void setNumMaxDomandeSportelloPresentate(
			String numMaxDomandeSportelloPresentate) {
		this.numMaxDomandeSportelloPresentate = numMaxDomandeSportelloPresentate;
	}

	public String getSportelloOpen() {
		return sportelloOpen;
	}

	public void setSportelloOpen(String sportelloOpen) {
		this.sportelloOpen = sportelloOpen;
	}

	public String getBandoMaterializzatoSenzaPEC() {
		return bandoMaterializzatoSenzaPEC;
	}

	public void setBandoMaterializzatoSenzaPEC(String bandoMaterializzatoSenzaPEC) {
		this.bandoMaterializzatoSenzaPEC = bandoMaterializzatoSenzaPEC;
	}

}
