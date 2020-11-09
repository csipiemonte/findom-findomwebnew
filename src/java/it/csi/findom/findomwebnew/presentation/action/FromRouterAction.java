/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.aaep.aaeporch.business.AAEPException_Exception;
import it.csi.aaep.aaeporch.business.CSIException_Exception;
import it.csi.aaep.aaeporch.business.ImpresaINFOC;
import it.csi.aaep.aaeporch.business.SystemException_Exception;
import it.csi.aaep.aaeporch.business.UnrecoverableException_Exception;
import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.findom.findomwebnew.dto.aaep.Carica;
import it.csi.findom.findomwebnew.dto.aaep.Impresa;
import it.csi.findom.findomwebnew.dto.aaep.Persona;
import it.csi.findom.findomwebnew.dto.aaep.Sede;
import it.csi.findom.findomwebnew.dto.ipa.Ipa;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.VistaUltimaDomandaDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.integration.extservices.aaep.AaepDAO;
import it.csi.findom.findomwebnew.integration.extservices.ipa.IpaDAO;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.ImpresaEnte;
import it.csi.findom.findomwebnew.presentation.vo.StatoDomanda;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.csi.findom.findomwebnew.presentation.vo.UserInfo;
import it.csi.findom.findomwebnew.util.TrasformaClassiAAEP;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.exceptions.MalformedIdTokenException;
import it.csi.util.performance.StopWatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ValidationAware;

public class FromRouterAction extends BaseAction implements ValidationAware {

	private static final long serialVersionUID = 1L;
	
	private static final String CLASS_NAME = "FromRouterAction";

	public ArrayList<ImpresaEnte> listaImprese = null;

	private AaepDAO aaepDAO;
	private IpaDAO ipaDAO;
	
	
	@Override
	public String executeAction() throws SystemException, UnrecoverableException, CSIException {
		
		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		
		String result = new String();
		try {
			result = super.execute();
			
		} catch (SystemException e){
			log.error(logprefix + " SystemException: " + e);
			return ERROR;
		} catch (UnrecoverableException e){
			log.error(logprefix + " UnrecoverableException: " + e);
			return ERROR;
		} catch (CSIException e){
			log.error(logprefix + " CSIException: " + e);
			return ERROR;
		} catch (Exception e){
			log.error(logprefix + " Exception: " + e);
			return ERROR;
		} catch (Throwable e) {
			log.error(logprefix + " Throwable: " + e);
			return ERROR;
		} finally {
			log.debug(logprefix + " END");
		}
		return result;
		
	}
	
	@Override
	public String execute() throws SystemException,	UnrecoverableException, CSIException {
		
		final String methodName = "execute";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.debug(logprefix + " BEGIN");
		StopWatch stopWatch = new StopWatch(Constants.APPLICATION_CODE);
		stopWatch.start();
		
		getSessionParametersAppendedToUrl();
		
		log.debug(logprefix + " getUserInfo().getCodFisc()="+getUserInfo().getCodFisc());
		
		// recupero i dati dalla SHELL_T_SOGGETTI relativi al CF dell'utente loggato
		ShellSoggettiDto soggetto = getServiziFindomWeb().getDatiSoggettoByCodiceFiscale(getUserInfo().getCodFisc());
		log.debug(logprefix + " dati del soggetto collegato al CF loggato : caricati");
		
		// recupero le domande presentate dal soggetto collegato
		if(soggetto!=null){
			// soggetto collegato esiste nella shell_t_soggetti
			log.debug(logprefix + "trovato soggetto con CF ="+soggetto.getCodiceFiscale());
				
			int idSogg = soggetto.getIdSoggetto();
			log.debug(logprefix + " arrSoggetti.idSoggetto="+idSogg);
			log.debug(logprefix + " arrSoggetti.CF="+soggetto.getCodiceFiscale());
			
			if(StringUtils.isBlank(soggetto.getNome()) || StringUtils.isBlank(soggetto.getCognome())){
				// campi nome e/o cognome nulli, li aggiorno
				aggiornaSoggetto(soggetto);
			}
			getStatus().setIdSoggettoCollegato(idSogg);
			log.debug(logprefix + " impostato nello status l'id del soggetto collegato");
			
		} else {
			// soggetto collegato NON esiste nella shell_t_soggetti, 
			// devo inserire il nuovo soggetto nella shell_t_soggetti
			// e fargli inserire una nuova impresa/ente
			log.debug(logprefix + " nessun soggetto trovato");

			// visualizzo la sezione per inserire una nuova impresa/ente
			
			ShellSoggettiDto newSoggetto = new ShellSoggettiDto();
			newSoggetto.setCodiceFiscale(getUserInfo().getCodFisc());
			newSoggetto.setCognome(getUserInfo().getCognome()); 
			newSoggetto.setNome(getUserInfo().getNome()); 

			
			getServiziFindomWeb().insertShellTSoggetto(newSoggetto);
			log.debug(logprefix + " inserito nuovo soggetto sul db");
			
			getServiziFindomWeb().insertLogAudit(Constants.CSI_LOG_IDAPPL, "", 
					getUserInfo().getCodFisc()+" - "+ getUserInfo().getCognome() + " " + getUserInfo().getNome(), 
					Constants.CSI_LOG_OPER_INS_SOGGETTO, "inserito nuovo utente CF="+getUserInfo().getCodFisc(), "");
			
			// recupero l'idSoggetto appena creato
			ShellSoggettiDto soggInserito = getServiziFindomWeb().getDatiSoggettoByCodiceFiscale(getUserInfo().getCodFisc());
			log.debug(logprefix + " soggInserito.getIdSoggetto()="+soggInserito.getIdSoggetto());
			
			getStatus().setIdSoggettoCollegato(soggInserito.getIdSoggetto());
			
		}
				
		stopWatch.stop();
		stopWatch.dumpElapsed(CLASS_NAME, methodName+"()", "test", "test");
		
		//aggiorno l'oggetto status nel CONTEXT
		TreeMap<String, Object> context = new TreeMap<String, Object>();
		context.put(Constants.USER_INFO, getUserInfo());
		context.put(Constants.STATUS_INFO, getStatus());
			
		if (getServletRequest().getParameter("idDomanda")!= null) {
			verificaBeneficiario(getServletRequest().getParameter("idDomanda"));
		}
		
		getServletRequest().removeAttribute("datiIpa");
		
		getServletRequest().getSession().setAttribute(Constants.CONTEXT_ATTR_NAME, context);
		log.debug(logprefix + " creato contesto " + Constants.CONTEXT_ATTR_NAME);
			
		log.debug(logprefix + "  END");
		return SUCCESS;
	}
	
	private void verificaBeneficiario(String idDomanda) throws ServiziFindomWebException, NumberFormatException {
		
		final String methodName = "verificaBeneficiario";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.info(logprefix + "BEGIN");

		if ((getStatus().getIdSoggettoBeneficiario() == null)||(getStatus().getCodFiscaleBeneficiario() == null)){
		
		// 1. carico i dati necessari della domanda
			
			// carico i dati dell'aggregatore per la domanda selezionata
			ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(Integer.parseInt(idDomanda));
			if(listaAggData==null || (listaAggData!=null && listaAggData.size()!=1)){
				log.debug(logprefix + " listaAggData NULLA o di dimenzione errata");
				addActionError(Constants.CARICA_DOMANDA_FAILED);
				return;
			}
		
			AggrDataDto aggrDomanda = listaAggData.get(0);
			Integer idSoggettoBeneficiario = aggrDomanda.getIdSoggettoBeneficiario();
	
			getStatus().setIdSoggettoBeneficiario(idSoggettoBeneficiario);
			
			String cfBenefic = getStatus().getCodFiscaleBeneficiario();
			if (cfBenefic == null) {
				ArrayList<String> listaId = new ArrayList<String>();
				listaId.add(idSoggettoBeneficiario.toString());
				ArrayList<ShellSoggettiDto> listaSoggetti = getServiziFindomWeb().getDatiSoggettoByIdSoggetto(listaId);
				if (listaSoggetti.size()>0 && listaSoggetti.get(0)!=null) {
					cfBenefic = listaSoggetti.get(0).getCodiceFiscale();
					getStatus().setCodFiscaleBeneficiario(cfBenefic);
				}
			}
			log.debug(logprefix + " cfBenefic="+cfBenefic);
		}
		log.info(logprefix + "END");
	}

	private void aggiornaSoggetto(ShellSoggettiDto soggetto) throws ServiziFindomWebException {
		final String methodName = "aggiornaSoggetto";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		UserInfo us = getUserInfo();
		ShellSoggettiDto newSogg = new ShellSoggettiDto();
		newSogg.setIdSoggetto(soggetto.getIdSoggetto());
		newSogg.setCognome(us.getCognome());
		newSogg.setNome(us.getNome());
		newSogg.setCodiceFiscale(soggetto.getCodiceFiscale());
		newSogg.setDenominazione(soggetto.getDenominazione());
		newSogg.setIdFormaGiuridica(soggetto.getIdFormaGiuridica());
		
		getServiziFindomWeb().updateShellTSoggetto(newSogg);
		log.debug(logprefix + " aggiornato soggetto sul db");
		getServiziFindomWeb().insertLogAudit(Constants.CSI_LOG_IDAPPL, "", soggetto.getCodiceFiscale()+" - "+ soggetto.getCognome() + " " + soggetto.getNome()
				, Constants.CSI_LOG_OPER_INS_IMPRESA, "aggiornato soggetto CF="+soggetto.getCodiceFiscale(), "");
		log.debug(logprefix + " END");
	}
		

	public void caricaDatiImpresa() {
		final String methodName = "caricaDatiImpresa";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.debug(logprefix + " BEGIN");

		StatusInfo state = getStatus(); // denominazione null
		UserInfo us = getUserInfo();

		// invoco AAEP e recupero le info dell'impresa
		Impresa azienda = null;
		Sede sedeLegale = null;
		try {
			log.debug(logprefix + "recupero info azienda da AAEP con getDettaglioImpresa(INFOC,"+state.getCodFiscaleBeneficiario()+")");
			
			// Se entra Monitoraggio non chiamo AAEP (Pochettino 7 set 2018) 
			// perche' altrimenti AAEP restituisce "UserException:findByCodiceFiscaleINFOCAMERE2: Nessun record trovato"
			// ed intasa i log di non errori
			if (StringUtils.equals("SYMGNT80A01L219B", state.getCodFiscaleBeneficiario())){
				log.debug(logprefix + "Utente monitoraggio, NON accedo ad AAEP");
			}else{
			
				ImpresaINFOC aziendaINFOC = aaepDAO.getDettaglioImpresa2("INFOC", state.getCodFiscaleBeneficiario(), "", "", "");
				log.debug(logprefix + "recuperate info azienda da AAEP");
				// trasformo l'oggett orestituito in una interno per poterlo serializzare e mettere in sessione
				azienda = TrasformaClassiAAEP.impresaINFOC2ImpresaI(aziendaINFOC);
				//  valorizzare ul RUOLO LR 
				// se AAEP non risponde cosa faccio?????
				String lr = verificaLegaleRappresentante(azienda, state.getOperatore());
				if (StringUtils.isNotBlank(lr) && lr.equals("S"))	{			
					us.setRuolo(Constants.RUOLO_LR);
				}
				if (azienda!=null) {
					log.debug(logprefix + "Cerco la sede legale per idAzienda = ["+aziendaINFOC.getIdAzienda()+"]" );
					it.csi.aaep.aaeporch.business.Sede sede = aaepDAO.getDettaglioSedeLegale(aziendaINFOC.getIdAzienda(),determinaIdSedeLegale(aziendaINFOC.getSedi().iterator()) , "INFOC");
					sedeLegale = TrasformaClassiAAEP.sedeINFOC2Sede(sede);
					if(sedeLegale!=null){
						log.info(logprefix + "Ho trovato la sede legale ["+sedeLegale.getDenominazione()+"]" );
						log.debug(logprefix + "LA SEDE HA ATECO 2007 = ["+sedeLegale.getCodiceAteco2007()+"]" );
					}
				}
			}

		} catch (UnrecoverableException_Exception e) {
			log.error(logprefix +" UnrecoverableException_Exception: "+e);

		} catch (CSIException_Exception e) {
			
			if(e.getMessage()!=null & e.getMessage().contains("Nessun record trovato")){
				log.warn(logprefix + "Nessun record trovato per "+state.getCodFiscaleBeneficiario());
				log.warn(logprefix +" CSIException_Exception: "+e);
			}else{
				log.error(logprefix +" CSIException_Exception: "+e);
			}

		} catch (SystemException_Exception e) {
			log.error(logprefix +" SystemException_Exception: "+e);

		} catch (AAEPException_Exception e) {
			log.error(logprefix +" AAEPException_Exception: "+e);

		} catch (Exception e){
			// entro qui se il WS non risponde correttamente 
			// Exception: javax.xml.ws.soap.SOAPFaultException: Fault occurred while processing.
			log.error(logprefix +" Exception: "+e);

		}

		//  se non trova i dati su AAEP salva in sessione null
		getServletRequest().getSession().setAttribute(Constants.SESSION_ENTEIMPRESA, azienda);
		log.debug(logprefix + "messo in sessione EnteImpresa");
		getServletRequest().getSession().setAttribute(Constants.SESSION_SEDE_LEGALE, sedeLegale);						
		log.debug(logprefix + "messo in sessione sedeLegale");
		
		//  svuoto comunque il contenuto in sessione del rappresentante legale di AAEP e salva in sessione null
		getServletRequest().getSession().setAttribute(Constants.SESSION_MAPPA_DATILR, null);						
		log.debug(logprefix + "messo in sessione null per mappaDatiConsLRAAEP");
		
		log.debug(logprefix + "  END");
	}


	private String determinaIdSedeLegale(Iterator<it.csi.aaep.aaeporch.business.Sede> sediIter) {
		while (sediIter.hasNext()) {
			it.csi.aaep.aaeporch.business.Sede sede = sediIter.next();
			if (sede.getDescrTipoSede().equalsIgnoreCase("SEDE LEGALE")) {
				return sede.getIdSede(); 
			}
		}
		throw new UnsupportedOperationException("Attenzione! l'azienda individuata non presenta la sede legale su AAEP ");
	}
	
	private String verificaLegaleRappresentante(Impresa azienda, String codFisc) {
		final String methodName = "verificaLegaleRappresentante";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.debug(logprefix + " BEGIN");
		log.debug(logprefix + " codFisc=" + codFisc);
		String lr = "";
			
		if (azienda != null) {
			log.debug(logprefix + "azienda trovata su AAEP, IdAzienda=" + azienda.getIdAzienda());

			// verifica su AAEP se utente loggato e' Legale Rappresentante
			List<Persona> listaPersone = azienda.getListaPersone();
			if (listaPersone != null) {
				log.debug(logprefix + "trovata listaPersone=" + listaPersone.size());

				for (Iterator itr = listaPersone.iterator(); itr.hasNext();) {
					Persona persona = (Persona) itr.next();
					log.debug(logprefix + "persona.getCodiceFiscale()=" + persona.getCodiceFiscale());

					if (persona != null && persona.getCodiceFiscale() != null
							&& persona.getCodiceFiscale().equals(codFisc)) {
						log.debug(logprefix + "trovata persona loggata in elenco listaPersone");
						List<Carica> listaCariche = persona.getListaCariche();
						if (listaCariche != null) {
							log.debug(logprefix + "trovata listaCariche=" + listaCariche.size());
							for (Iterator itr2 = listaCariche.iterator(); itr2.hasNext();) {
								Carica carica = (Carica) itr2.next();
								if (carica != null && "S".equals(carica.getFlagRappresentanteLegale())) {
									log.debug(logprefix + "FOUNDED carica.getFlagRappresentanteLegale()="
											+ carica.getFlagRappresentanteLegale());

									//  utente loggato e' LR
									lr = "S";

									break;
								}
							}
						} else {
							log.debug(logprefix + "trovata listaCariche NULLA");
						}
						break;
					}
				}
			} else {
				log.debug(logprefix + "trovata listaPersone NULLA");
			}

		} else {
			log.debug(logprefix + "azienda NON trovata su AAEP");
		}
		
		log.debug(logprefix + " END");
		return lr;
	}

	
	public void caricaDatiUltimaDomandaBeneficiario() {
		final String methodName = "caricaDatiUltimaDomandaBeneficiario";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";

		log.debug(logprefix + " BEGIN");

		StatusInfo state = getStatus();

		// invoco AAEP e recupero le info dell'impresa
		VistaUltimaDomandaDto vistaUltimaDomandaDto = null;

		try {
			log.info(logprefix + "recupero info azienda da AAEP con getDettaglioImpresa(INFOC,"+state.getCodFiscaleBeneficiario()+")");
			
			// Se entra Monitoraggio non chiamo AAEP (Pochettino 7 set 2018) 
			// perche' altrimenti AAEP restituisce "UserException:findByCodiceFiscaleINFOCAMERE2: Nessun record trovato"
			// ed intasa i log di non errori
			if (StringUtils.equals("SYMGNT80A01L219B", state.getCodFiscaleBeneficiario())){
				log.debug(logprefix + "Utente monitoraggio, NON carico dati ultima domanda");
			}else{
			
				vistaUltimaDomandaDto = getServiziFindomWeb().getDatiUltimaDomandaBeneficiario(state.getCodFiscaleBeneficiario());
				log.info(logprefix + "recuperate info beneficiario da ultima domanda inserita");
				log.info(logprefix + " vistaUltimaDomandaDto="+vistaUltimaDomandaDto);
			}

		} catch (ServiziFindomWebException e) {
			
			if(e.getMessage()!=null & e.getMessage().contains("Nessun record trovato")){
				log.warn(logprefix + "Nessun record trovato per "+state.getCodFiscaleBeneficiario());
				log.warn(logprefix +" ServiziFindomWebException: "+e);
			}else{
				log.error(logprefix +" ServiziFindomWebException: "+e);
			}

		} catch (Exception e){
			// entro qui se il WS non risponde correttamente 
			// Exception: javax.xml.ws.soap.SOAPFaultException: Fault occurred while processing.
			log.error(logprefix +" Exception: "+e);

		}
		if(vistaUltimaDomandaDto!=null){
	 		getServletRequest().getSession().setAttribute(Constants.SESSION_DATIULTIMOBENEFICIARIO, vistaUltimaDomandaDto);						
			log.debug(logprefix + "messo in sessione datiUltimoBeneficiario");
		}else{
			
			VistaUltimaDomandaDto vubSess = (VistaUltimaDomandaDto) getServletRequest().getSession().getAttribute(Constants.SESSION_DATIULTIMOBENEFICIARIO);
			log.debug(logprefix + "  leggo datiUltimoBeneficiario da sessione="+vubSess);
			
			// se l'oggetto in sessione non corrisponde al beneficiario selezionato, lo elimino
			if(vubSess!=null && vubSess.getOperatorePresentatore()!=null 
					&& !StringUtils.equals(vubSess.getOperatorePresentatore().getCodiceFiscale(), state.getCodFiscaleBeneficiario())){
				
				log.debug(logprefix + "  rimosso attributo "+Constants.SESSION_DATIULTIMOBENEFICIARIO+" dalla sessione");
				getServletRequest().getSession().removeAttribute(Constants.SESSION_DATIULTIMOBENEFICIARIO);
			}
		}
		log.debug(logprefix + "  END");
	}

	private void getSessionParametersAppendedToUrl() throws ServiziFindomWebException {
		
		final String methodName = "getSessionParametersAppendedToUrl";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + "BEGIN");
		
		Gson gson = new Gson();
		
		StatusInfo statusInfo = new StatusInfo();
		
		String datiIpa =  getServletRequest().getParameter(Constants.SESSION_DATIIPA);
		Ipa ipa = (Ipa)gson.fromJson(datiIpa, Ipa.class);
		getServletRequest().getSession().setAttribute(Constants.SESSION_DATIIPA, ipa);
		
		UserInfo ui = new UserInfo();
		try {
			Identita identita = new Identita((String)getServletRequest().getParameter("idIride"));
					
			ui.setRuolo((String)getServletRequest().getParameter("ruolo"));
			ui.setCodFisc(identita.getCodFiscale());
			ui.setCognome(identita.getCognome());
			ui.setIdIride((String)getServletRequest().getParameter("idIride"));
			ui.setNome(identita.getNome());
		
		} catch (MalformedIdTokenException e) {
			log.error(logprefix + " MalformedIdTokenException:" + e.toString(), e);
		}
		getServletRequest().getSession().setAttribute(Constants.USERINFO_SESSIONATTR, ui);
		
		String idSoggettoBeneficiario = getServletRequest().getParameter("isbSI");
		if (idSoggettoBeneficiario != null) {
			statusInfo.setIdSoggettoBeneficiario(Integer.parseInt(idSoggettoBeneficiario));
		}
		String idSoggettoCollegato = getServletRequest().getParameter("iscSI");
		if (idSoggettoCollegato != null) {
			statusInfo.setIdSoggettoCollegato(Integer.parseInt(idSoggettoCollegato));
		}
		statusInfo.setCodFiscaleBeneficiario(getServletRequest().getParameter("cfbSI"));
		String denominazioneBeneficiario= getServletRequest().getParameter("dieSI"); // denominazione presente
		if( denominazioneBeneficiario != null && !denominazioneBeneficiario.isEmpty()){
			statusInfo.setDescrImpresaEnte(getServletRequest().getParameter("dieSI")); // recupera cf da findomrouter
		}
		
		Map<String, String> statoPropostaMap = new TreeMap<String, String>();
		
		ArrayList<StatoDomanda> elencoStatiD = getServiziFindomWeb().getStatiDomanda();

		if(elencoStatiD!=null){
			log.info(logprefix + " elencoStatiD.length=" + elencoStatiD.size());
			for (StatoDomanda statoDomanda : elencoStatiD) {
				log.debug(logprefix + " codice=[" + statoDomanda.getCodice()+"], descr=["+statoDomanda.getDescrizione()+"]");
				if(statoDomanda.getDataFineValidita()==null){
					statoPropostaMap.put(statoDomanda.getCodice(), statoDomanda.getDescrizione());
				}else{
					log.debug(logprefix + " stato con fine valida impostat, model_state=["+statoDomanda.getCodice()+"]");
				}
			}
		}else{
			log.debug(logprefix + " elencoStatiD NULL");
		}
		
		String idDomanda = getServletRequest().getParameter("idDomanda");
		if (idDomanda != null) {
			statusInfo.setNumProposta(Integer.parseInt(idDomanda));
			
			statusInfo.setFlagBandoDematerializzato( getServiziFindomWeb()
					.getFlagBandoDematerializzatoByIdDomanda(Integer.parseInt(idDomanda)));

			// inserisco nello statusInfo i dati legati alla domanda selezionata
			
			// carico i dati per la domanda selezionata
			ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(Integer.parseInt(idDomanda));
			
			AggrDataDto aggrDomanda = listaAggData.get(0);
			log.info(logprefix + " caricato dati domanda:"+idDomanda);
			
			statusInfo.setTemplateId(aggrDomanda.getTemplateId());
			
			FindomTBandiDto pbandi = getServiziFindomWeb().findoOneFindomTBandi(new Long(aggrDomanda.getTemplateId()));
			statusInfo.setTipoFirma(pbandi.getTipoFirma());
			
		}
		statusInfo.setStatoPropostaMap(statoPropostaMap);
		statusInfo.setOperatore(getUserInfo().getCodFisc());
		statusInfo.setDescrizioneOperatore(getUserInfo().getCognome() + " " + getUserInfo().getNome());
		
		getServletRequest().getSession().setAttribute(Constants.STATUS_INFO, statusInfo);
		getServletRequest().getSession().setAttribute(Constants.SESSION_KEY_INIT_CP, Constants.FLAG_TRUE);

		// cerco i dati dell'impresa si AAEP
		caricaDatiImpresa();

		// cerco i dati da eventuale ultima domanda
		caricaDatiUltimaDomandaBeneficiario();

		log.info(logprefix + "END");
	}

	//GETTERS && SETTERS
	public ArrayList<ImpresaEnte> getListaImprese() {
		return listaImprese;
	}
	public void setListaImprese(ArrayList<ImpresaEnte> listaImprese) {
		this.listaImprese = listaImprese;
	}

	public AaepDAO getAaepDAO() {
		return aaepDAO;
	}

	public IpaDAO getIpaDAO() {
		return ipaDAO;
	}

	public void setAaepDAO(AaepDAO aaepDAO) {
		this.aaepDAO = aaepDAO;
	}

	public void setIpaDAO(IpaDAO ipaDAO) {
		this.ipaDAO = ipaDAO;
	}
}
