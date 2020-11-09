/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb;

import it.csi.findom.findomwebnew.business.servizifindomweb.dao.ServiziFindomWebDao;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrTModelDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrTTemplateDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AmministratoriDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.CapofilaAcronimoDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.CapofilaAcronimoPartnerDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.DocumentiIntegrativiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomAllegatiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomDFunzionariDTO;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDirezioneDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.NumberMaxDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ParametroRegolaBandoDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ProssimoSportelloAttivoDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.RegolaDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.RegoleVerificaFirmaDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDocumentoIndexDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDomandeDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.StatoDomandaDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.TipolBeneficiariDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDocDematDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeBeneficiariDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaSportelliAttiviDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.VistaUltimaDomandaDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.vo.Domanda;
import it.csi.findom.findomwebnew.presentation.vo.StatoDomanda;
import it.csi.findom.findomwebnew.presentation.vo.UserInfo;
import it.csi.findom.findomwebnew.util.TrasformaClassi;
import it.csi.melograno.aggregatore.business.Aggregatore;
import it.csi.melograno.aggregatore.exception.AggregatoreException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ServiziFindomWeb {

	protected static final Logger LOGGER = Logger.getLogger(Constants.APPLICATION_CODE + ".DaoImpl");
	
	private static final String CLASS_NAME = "ServiziFindomWeb";
	private ServiziFindomWebDao findomWebDao;

	
	public ArrayList<StatoDomanda> getStatiDomanda() throws ServiziFindomWebException {
		final String methodName = "getStatiDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		
		ArrayList<StatoDomandaDto> lista = findomWebDao.getStatiDomanda();
		return TrasformaClassi.statoDomandaDto2StatoDomanda(lista);
	}
	
	public int insertLogAudit(String codApplicativo, String ip, String utente ,String tipoOperazione ,String descrOperazione, String chiaveOperazione)
			throws ServiziFindomWebException {
		final String methodName = "insertLogAudit";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.insertLogAudit(codApplicativo, ip, utente, tipoOperazione, descrOperazione, chiaveOperazione);
	}
	
	public int insertLogFirma(String utente, Integer idDomanda, String metodo, String msg) 
			throws ServiziFindomWebException {
		final String methodName = "insertLogFirma";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.insertLogFirma(utente, idDomanda, metodo, msg);
	}
	
	public ShellSoggettiDto getDatiSoggettoByCodiceFiscale(String utente) throws ServiziFindomWebException {
		final String methodName = "getDatiSoggettoByCodiceFiscale";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDatiSoggettoByCodiceFiscale(utente);
	}
	
	public ArrayList<ShellSoggettiDto> getDatiSoggettoByIdSoggetto(ArrayList<String> listaId) throws ServiziFindomWebException {
		final String methodName = "getDatiSoggettoByIdSoggetto";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDatiSoggettoByIdSoggetto(listaId);
	}
	
	public ArrayList<VistaDomandeDto> getVistaDomandaByCreatoreBeneficiario(Integer idSoggettoCreatore, Integer idSoggettoBeneficiario) 
			throws ServiziFindomWebException {
		final String methodName = "getVistaDomandaByCreatoreBeneficiario";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getVistaDomandaByCreatoreBeneficiario(idSoggettoCreatore,idSoggettoBeneficiario);
	}

	public ArrayList<VistaDomandeDto> getVistaDomandaByCreatoreBeneficiarioDomanda(Integer idSoggettoCreatore, Integer idSoggettoBeneficiario,
			Integer idDomanda) throws ServiziFindomWebException {
				final String methodName = "getVistaDomandaByCreatoreBeneficiarioDomanda";
				final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
				LOGGER.debug(logprefix + " BEGIN-END");
				return findomWebDao.getVistaDomandaByCreatoreBeneficiarioDomanda(idSoggettoCreatore, idSoggettoBeneficiario, idDomanda);
	}
	
	public AmministratoriDto getAmministratoreByCodiceFiscale(String codFisc) throws ServiziFindomWebException {
		final String methodName = "getAmministratoreByCodiceFiscale";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getAmministratoreByCodiceFiscale(codFisc);
	}

	public ArrayList<Domanda> getDomandeInserite(Integer idSoggBeneficiario,
			Integer idSoggCreatore, String ruolo, String normativa,
			String descBreveBando, String bando, String sportello,
			String statoDomanda, String numDomanda) throws ServiziFindomWebException {
		final String methodName = "getDomandeInserite";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		
		ArrayList<VistaDomandeDto> lista = findomWebDao.getDomandeInserite(idSoggBeneficiario, idSoggCreatore , ruolo,
				normativa, descBreveBando, bando,sportello,statoDomanda, numDomanda);
		
		return TrasformaClassi.vistaDomandaDto2Domanda(lista);
	}
	
	public void insertShellTSoggetto(ShellSoggettiDto newSoggetto) throws ServiziFindomWebException {
		final String methodName = "insertShellTSoggetto";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN");
		findomWebDao.insertShellTSoggetto(newSoggetto);
		LOGGER.debug(logprefix + " END");
	}
	
	public void updateShellTSoggetto(ShellSoggettiDto soggetto) throws ServiziFindomWebException {
		final String methodName = "updateShellTSoggetto";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN");
		findomWebDao.updateShellTSoggetto(soggetto);
		LOGGER.debug(logprefix + " END");		
	}
	
	public ArrayList<VistaSportelliAttiviDto> getVistaSportelliAttivi()  throws ServiziFindomWebException {
		final String methodName = "getVistaSportelliAttivi";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getVistaSportelliAttivi();
	}
	
	public ArrayList<VistaSportelliAttiviDto> getVistaSportelliAttiviByNormativa(int idNormativa)  throws ServiziFindomWebException {
		final String methodName = "getVistaSportelliAttiviByNormativa";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getVistaSportelliAttiviByNormativa(idNormativa);
	}
	
	public ArrayList<VistaSportelliAttiviDto> getVistaSportelliAttiviByIdBando(int idBando) throws ServiziFindomWebException {
				final String methodName = "getVistaSportelliAttiviByIdBando";
				final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
				LOGGER.debug(logprefix + " BEGIN-END");
				return findomWebDao.getVistaSportelliAttiviByIdBando(idBando);
	}
	
	public ProssimoSportelloAttivoDto getProssimoSportelloAttivo() throws ServiziFindomWebException {
		final String methodName = "getProssimoSportelloAttivo";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getProssimoSportelloAttivo();
	}
	
	public ArrayList<TipolBeneficiariDto> getListaTipolBeneficiari() throws ServiziFindomWebException {
		final String methodName = "getListaTipolBeneficiari";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getListaTipolBeneficiari();
	}

	public ArrayList<TipolBeneficiariDto> getListaTipolBeneficiariByIdBando(int idBando) throws ServiziFindomWebException {
		final String methodName = "getListaTipolBeneficiariByIdBando";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getListaTipolBeneficiariByIdBando(idBando);
	}
	
	public ArrayList<VistaDomandeBeneficiariDto> getVistaDomandeBeneficiari(int idBando, int idSoggettoBeneficiario) 
			throws ServiziFindomWebException {
		final String methodName = "getVistaDomandeBeneficiari";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getVistaDomandeBeneficiari(idBando, idSoggettoBeneficiario);
	}

	public ArrayList<AggrTTemplateDto> getAggrTemplateWithFindomBandi(int idBando) throws ServiziFindomWebException {
		final String methodName = "getAggrTemplateWithFindomBandi";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getAggrTemplateWithFindomBandi(idBando);
	}
	
	public ArrayList<AggrTModelDto> getAggrTModelByUserTemplateProg(String userId, String templateCode, Integer modelProg) throws ServiziFindomWebException {
		final String methodName = "getAggrTModelByUserTemplateProg";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getAggrTModelByUserTemplateProg(userId, templateCode, modelProg);
	}
	
	public int insertShellTDomande(Integer idSoggettoCollegato,	Integer idSoggettoBeneficiario, Integer numSportello,
			Integer IdSoggettoInvio, String DataInvioDomanda, Integer idModel, Integer idTipologiaBeneficiario) throws ServiziFindomWebException {
		final String methodName = "insertShellTDomande";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.insertShellTDomande( idSoggettoCollegato, idSoggettoBeneficiario, numSportello,
				IdSoggettoInvio, DataInvioDomanda, idModel, idTipologiaBeneficiario);
	}

	public ArrayList<AggrDataDto> getAggrDataByIdDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getAggrDataByIdDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getAggrDataByIdDomanda(idDomanda);
	}
	
	public Integer deleteDomanda(Integer idDomInt, String userId,
			String templateCode, Integer modelProgr, Aggregatore aggregatore)  throws ServiziFindomWebException, AggregatoreException {
		final String methodName = "deleteDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.deleteDomanda(idDomInt, userId, templateCode, modelProgr, aggregatore);
	}
	
	public String getXMLDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getXMLDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getXMLDomanda(idDomanda);
	}

	public Integer insertTFileDomanda(Integer idDomInt, String xmlDomanda, byte[] byteArrayPdf, String messageDigest) throws ServiziFindomWebException {
		final String methodName = "insertTFileDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.insertTFileDomanda(idDomInt, xmlDomanda, byteArrayPdf, messageDigest);
	}
	
	public NumberMaxDto getNumeroMassimoDomandeInviate(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getNumeroMassimoDomandeInviate";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getNumeroMassimoDomandeInviate(idDomanda);
	}

	public Integer updateShellDomande(Integer idDomanda, Integer idSoggettoCollegato,String statoDomanda, Integer idStatoIstruttoria, Date dataTrasmissioneUfficiale)  throws ServiziFindomWebException {
		final String methodName = "updateShellDomande";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.updateShellDomande(idDomanda, idSoggettoCollegato, statoDomanda,idStatoIstruttoria, dataTrasmissioneUfficiale);
	}
	
	public Integer updateShellDomandeBandoDemat(Integer idDomanda, Integer idSoggettoCollegato)  throws ServiziFindomWebException {
		final String methodName = "updateShellDomandeBandoDemat";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.updateShellDomandeBandoDemat(idDomanda, idSoggettoCollegato);
	}
	
	public byte[] getPdfDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getPdfDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getPdfDomanda(idDomanda);
	}

	public ShellDocumentoIndexDto getDocumentoIndex(String idDocumento, String codFisc, Integer numProposta) throws ServiziFindomWebException{
		final String methodName = "getDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDocumentoIndex(idDocumento, codFisc, numProposta);
	}
	
	public ShellDocumentoIndexDto getDocumentoIndex(String idDocumento, Integer numProposta) throws ServiziFindomWebException{
		final String methodName = "getDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDocumentoIndex(idDocumento, numProposta);
	}
	
	public int insertShellTDocumentoIndex(Integer idDomanda, String uuidIndex,
			String repository, String upFileFileName, String messageDigest,	String dtVerificaFirma, 
			Integer idStatoDocumentoIndex, Integer idTipologiaNewDoc, Integer idSoggetto)  throws ServiziFindomWebException{
		final String methodName = "insertShellTDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.insertShellTDocumentoIndex(idDomanda, uuidIndex, repository, upFileFileName, messageDigest,
				dtVerificaFirma, idStatoDocumentoIndex, idTipologiaNewDoc, idSoggetto);
	}
	
	public ShellDocumentoIndexDto getDocumentoIndex(String uuid) throws ServiziFindomWebException{
		final String methodName = "getDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDocumentoIndex(uuid);
	}

	public HashMap getMappaAllegati(Integer idBando, Integer idDomanda, Integer idSportello) throws ServiziFindomWebException{
		final String methodName = "getMappaAllegati";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");

		ArrayList<FindomAllegatiDto> listaAllegati = findomWebDao.getAllegati(idBando, idDomanda, idSportello);
		
		HashMap<Integer, FindomAllegatiDto> mappa = new HashMap<Integer, FindomAllegatiDto>();
		for (FindomAllegatiDto allegato : listaAllegati) {
			mappa.put(allegato.getIdAllegato(), allegato);
		}
		LOGGER.info(logprefix + " END");
		return mappa;
	}
	
	public Integer deleteDocumentoIndex(String idDoc) throws ServiziFindomWebException{
		final String methodName = "deleteDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.deleteDocumentoIndex(idDoc);
	}

	public ShellDocumentoIndexDto getDocumentoIndex(Integer idDomanda, String nomeFile)  throws ServiziFindomWebException{
		final String methodName = "getDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDocumentoIndex(idDomanda, nomeFile);
	}
	
	public ShellDocumentoIndexDto getDocumentoFirmato(Integer idDomanda)  throws ServiziFindomWebException{
		final String methodName = "getDocumentoFirmato";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDocumentoFirmato(idDomanda);
	}
	
	public ShellDocumentoIndexDto getLastDocumentoInserito(Integer idDomanda)  throws ServiziFindomWebException{
		final String methodName = "getLastDocumentoInserito";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getLastDocumentoInserito(idDomanda);
	}
	
	public ArrayList<String> getTipoDocumentoUpload(String parametro)  throws ServiziFindomWebException {
		final String methodName = "getTipoDocumentoUpload";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getTipoDocumentoUpload(parametro);
	}
	
	public String getValoreParametro(String codice)  throws ServiziFindomWebException {
		final String methodName = "getValoreParametro";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getValoreParametro(codice);
	}
	
	public String getMessageDigestDomanda(Integer idDomanda)  throws ServiziFindomWebException {
		final String methodName = "getMessageDigestDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getMessageDigestDomanda(idDomanda);
	}
	
	public String getValueFromXML(String nodo, String campo, Integer idDomanda)  throws ServiziFindomWebException {
		final String methodName = "getValueFromXML";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getValueFromXML(nodo, campo, idDomanda);
	}
	
	public ArrayList<ShellDocumentoIndexDto> getAllDocumentoIndex(Integer idDomanda) throws ServiziFindomWebException{
		final String methodName = "getAllDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getAllDocumentoIndex(idDomanda);
	}	
	
	public ArrayList<Integer> getRegoleTemplate(int idBando) throws ServiziFindomWebException{
		final String methodName = "getRegoleTemplate";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getRegoleTemplate(idBando);
	}
	
	public RegolaDto getRegola(int idRegola) throws ServiziFindomWebException{
		final String methodName = "getRegola";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getRegola(idRegola);
	}
	
	public RegoleVerificaFirmaDto getRegoleVerificaFirma(Integer idBando) throws ServiziFindomWebException{
		final String methodName = "getRegoleVerificaFirma";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getRegoleVerificaFirma(idBando);
	}
		
	public ArrayList<ParametroRegolaBandoDto> getParametriRegola(String codRegola, int templateId) throws ServiziFindomWebException{
		final String methodName = "getParametriRegola";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getParametriRegola(codRegola, templateId);
	}	

	public void copiaDomandaPerIstruttoria(Integer idDomanda) throws ServiziFindomWebException{
		final String methodName = "copiaDomandaPerIstruttoria";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		findomWebDao.copiaDomandaPerIstruttoria(idDomanda);
	}
	
	public String getFlagIstruttoriaEsterna(Integer idBando) throws ServiziFindomWebException{
		final String methodName = "getFlagIstruttoriaEsterna";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getFlagIstruttoriaEsterna(idBando);
	}
	public Integer getIdStatoIstruttoriaByCodice(String codice) throws ServiziFindomWebException{
		final String methodName = "getIdStatoIstruttoriaByCodice";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getIdStatoIstruttoriaByCodice(codice);
	}

	public String getIstanzaRoutingBySportello(Integer idSportello) throws ServiziFindomWebException{
		final String methodName = "getIstanzaRoutingBySportello";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getIstanzaRoutingBySportello(idSportello);
	}
	
	public String getFlagBandoDematerializzatoByIdDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getFlagBandoDematerializzatoByIdDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getFlagBandoDematerializzatoByIdDomanda(idDomanda);
	}
	
	public ArrayList<VistaDocDematDto> getVistaDocDemat(Integer idDomanda) throws ServiziFindomWebException{
		final String methodName = "getVistaDatiDocDemat";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getVistaDocDemat(idDomanda);
	}
	
	public List<DocumentiIntegrativiDto> getDocumentiIntegrativi(Long idDomanda, Integer idAllegato) throws ServiziFindomWebException {
		final String methodName = "getDocumentiIntegrativi";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDocumentiIntegrativi(idDomanda, idAllegato);
	}

	public ShellDomandeDto findoOneShellTDomanda(Long idDomanda) throws ServiziFindomWebException {
		final String methodName = "findoOneShellTDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.findoOneShellTDomanda(idDomanda);
	}

	public FindomTBandiDto findoOneFindomTBandi(Long idBando) throws ServiziFindomWebException {
		final String methodName = "findoOneFindomTBandi";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.findoOneFindomTBandi(idBando);
	}

	public FindomTBandiDirezioneDto findInfoDirezioneBando(Long idBando) throws ServiziFindomWebException {
		final String methodName = "findInfoDirezioneBando";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.findInfoDirezioneBando(idBando);
	}

	public FindomDFunzionariDTO findOneFindomDFunzionari(Long idFunzionario) throws ServiziFindomWebException {
		final String methodName = "findOneFindomDFunzionari";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.findOneFindomDFunzionari(idFunzionario);
	}

	public void chiudiIntegrazione(Long idDomanda, UserInfo userInfo) throws ServiziFindomWebException {
		final String methodName = "chiudiIntegrazione";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		findomWebDao.chiudiIntegrazione(idDomanda, userInfo);

	}
	
	public VistaUltimaDomandaDto getDatiUltimaDomandaBeneficiario(String beneficiario) throws ServiziFindomWebException {
		final String methodName = "getDatiUltimaDomandaBeneficiario";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDatiUltimaDomandaBeneficiario(beneficiario);
	}
	
	public CapofilaAcronimoDto getCapofilaAcronimoValidoByIdDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getCapofilaAcronimoValidoByIdDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getCapofilaAcronimoValidoByIdDomanda(idDomanda);
	}
	
	public CapofilaAcronimoPartnerDto getCapofilaAcronimoPartnerValidoByIdDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getCapofilaAcronimoPartnerValidoByIdDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getCapofilaAcronimoPartnerValidoByIdDomanda(idDomanda);
	}
	
	public void invalidaDomandaCapofila(String idCapofilaAcronimo, String idAcronimoBando, Integer idDomandaCapofila,
			String codApplicativo, String utente, String tipoOperazione, String descrOperazione) throws ServiziFindomWebException {
		final String methodName = "invalidaDomandaCapofila";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		findomWebDao.invalidaDomandaCapofila(idCapofilaAcronimo, idAcronimoBando, idDomandaCapofila, codApplicativo, utente, tipoOperazione, descrOperazione);
	}
	
	public void invalidaDomandaPartner(String idCapofilaAcronimo, String idPartner, Integer idDomandaPartner,
			String codApplicativo, String utente, String tipoOperazione, String descrOperazione) throws ServiziFindomWebException {
		final String methodName = "invalidaDomandaPartner";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		findomWebDao.invalidaDomandaPartner(idCapofilaAcronimo,idPartner,idDomandaPartner, codApplicativo, utente, tipoOperazione, descrOperazione);
	}
	
	public String getIdStatoOperatorePresentatore(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getIdStatoOperatorePresentatore";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getIdStatoOperatorePresentatore(idDomanda);
	}
	
	public ArrayList<CapofilaAcronimoPartnerDto> getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(String idCapofilaAcronimo) throws ServiziFindomWebException{
		final String methodName = "getCapofilaAcronimoPartnerListByIdCapofilaAcronimo";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(idCapofilaAcronimo);
	}
	
	public String getStatoDomanda(String idDomanda) throws ServiziFindomWebException{
		final String methodName = "getStatoDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getStatoDomanda(idDomanda);
	}
	
	public String callProcedureCreaSchedaProgetto(Integer idDomanda)  throws ServiziFindomWebException {
		final String methodName = "getFlagSchedaProgetto";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.callProcedureCreaSchedaProgetto(idDomanda);
	}

	public String getStereotipoDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getStereotipoDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getStereotipoDomanda(idDomanda);
	}
	
	public String getFlagProgettiComuniDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getFlagProgettiComuniDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getFlagProgettiComuniDomanda(idDomanda);
	}

	public int insertShellTDocumentoIndexDB(Integer idDomanda,
			String uuidAllegato, String repository, String nomeFile,
			String messageDigest, String dtVerificaFirma, Integer idStatoDocumentoIndex,
			Integer idTipologiaNewDoc, int idSoggetto, byte[] bFileUpload)  throws ServiziFindomWebException {
		final String methodName = "insertShellTDocumentoIndexDB";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.insertShellTDocumentoIndexDB(idDomanda, uuidAllegato, repository, nomeFile, messageDigest,
				dtVerificaFirma, idStatoDocumentoIndex, idTipologiaNewDoc, idSoggetto, bFileUpload);
	}
	
	public ShellDocumentoIndexDto getAllegatoFromShellTDocumentoIndex(Integer idAllegato)  throws ServiziFindomWebException {
		final String methodName = "getAllegatoFromShellTDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getAllegatoFromShellTDocumentoIndex(idAllegato);
	}
	
	public Integer updateStatoDomanda(String idDomanda, String statoDomanda) throws ServiziFindomWebException {
		final String methodName = "updateStatoDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.updateStatoDomanda(idDomanda, statoDomanda);
	}
	
	public String getDenominazioneOperatorePresentatore(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getDenominazioneOperatorePresentatore";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.debug(logprefix + " BEGIN-END");
		return findomWebDao.getDenominazioneOperatorePresentatore(idDomanda);
	}
	
	// GETTERS && SETTERS
	public ServiziFindomWebDao getFindomWebDao() {
		return findomWebDao;
	}

	public void setFindomWebDao(ServiziFindomWebDao findomWebDao) {
		this.findomWebDao = findomWebDao;
	}

}
