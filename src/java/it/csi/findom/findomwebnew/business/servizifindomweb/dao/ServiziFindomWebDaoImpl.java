/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.dao;

//import it.csi.findom.blocchetti.util.StringUtils;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.AggrDataDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.AggrTModelDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.AggrTTemplateDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.AmministratoriDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.CapofilaAcronimoDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.CapofilaAcronimoPartnerDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.DocumentiIntegrativiDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.FileDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.FindomAllegatiDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.FindomDFunzionariRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.FindomTBandiDirezioneRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.FindomTBandiRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.IntegerDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.NumberMaxDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.ParametroRegolaBandoDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.ProssimoSportelloAttivoDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.RegolaDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.RegoleVerificaFirmaDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.ShellDocumentoIndexDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.ShellDomandeDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.ShellSoggettiDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.StatoDomandaDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.StringDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.TipiDocRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.TipolBeneficiariDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.VistaDocDematDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.VistaDomandeBeneficiariDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.VistaDomandeDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.VistaSportelliAttiviDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.VistaUltimaDomandaDtoRowMapper;
import it.csi.findom.findomwebnew.business.servizifindomweb.mapper.XMLDtoRowMapper;
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
import it.csi.findom.findomwebnew.integration.extservices.index.IndexDAO;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.DateUtil;
import it.csi.findom.findomwebnew.presentation.vo.UserInfo;
import it.csi.melograno.aggregatore.business.Aggregatore;
import it.csi.melograno.aggregatore.exception.AggregatoreException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.transaction.annotation.Transactional;

public class ServiziFindomWebDaoImpl implements ServiziFindomWebDao {

	protected static final Logger LOGGER = Logger.getLogger(Constants.APPLICATION_CODE + ".business.servizifindomweb.dao");
	private static final String CLASS_NAME = "ServiziFindomWebDaoImpl";
	private NamedParameterJdbcTemplate template;
	private IndexDAO indexDAO; 
	private DataSource ds;
	
	public ServiziFindomWebDaoImpl() throws ServiziFindomWebException {
	}
	
		
	@Override
	public ArrayList<StatoDomandaDto> getStatiDomanda() throws ServiziFindomWebException {
		
		final String methodName = "getStatiDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ArrayList<StatoDomandaDto> listaStati = new ArrayList<StatoDomandaDto>();
		
		MapSqlParameterSource params = new MapSqlParameterSource();

		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_STATI_DOMANDA");
			LOGGER.info(logprefix + " Query == " + query);

			listaStati = (ArrayList<StatoDomandaDto>) template.query(query, params, new StatoDomandaDtoRowMapper());

		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);

			throw new ServiziFindomWebException("Exception while execute query getStatiDomanda", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return listaStati;
	}

	/**
	 *  String codApplicativo : <codice prodotto>_<codice linea cliente>_<codice ambiente>_<codice Unita' di Installazione>
	 *  String ip : ip della pdl di chi accede
	 *  String utente :  Identificativo univoco dell'utente che ha effettuato l'operazione
	 *  String tipoOperazione : login / logout / read / insert / update / delete / merge
	 *  String descrOperazione : descrizione dell'operazione (max 150 char)
	 *  String chiaveOperazione : ulteriori informazioni sull'operazione (max 500 char)
	 *  
	 */
	@Override
	public int insertLogAudit(String codApplicativo, String ip, String utente ,String tipoOperazione ,String descrOperazione, String chiaveOperazione) 
			throws ServiziFindomWebException { 
		
		final String methodName = "insertLogAudit";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		int ris = 0;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_LOGAUDIT");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idApp", codApplicativo , java.sql.Types.VARCHAR);
			params.addValue("ipAddress", ip, java.sql.Types.VARCHAR);
			params.addValue("utente", utente , java.sql.Types.VARCHAR);
			params.addValue("operazione", tipoOperazione, java.sql.Types.VARCHAR);
			params.addValue("oggOper", descrOperazione, java.sql.Types.VARCHAR);
			params.addValue("keyOper", chiaveOperazione, java.sql.Types.VARCHAR);
			
			ris = template.update(query, params);
			LOGGER.info(logprefix + " inserita ["+ris+"] entry");
			
		// TODO : Pochettino 29-01-2016 : se la sequence si scassa non traccia nulla nei log
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Exception : Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query insertLogAudit", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}

	/**
	 *  String idDomanda : Id domanda in verifica
	 *  String utente :  Identificativo univoco dell'utente che ha effettuato l'operazione
	 *  String metodo : descrizione dell'operazione (max 150 char)
	 *  String msg : ulteriori informazioni sull'operazione (max 500 char)
	 *  
	 */
	@Override
	public int insertLogFirma(String utente, Integer idDomanda, String metodo, String msg) 
			throws ServiziFindomWebException { 
		
		final String methodName = "insertLogAudit";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		int ris = 0;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_LOG_FIRMA");
			LOGGER.info(logprefix + " Query == " + query);
						 
			params.addValue("idDomanda", idDomanda , java.sql.Types.NUMERIC);
			params.addValue("msg", msg, java.sql.Types.VARCHAR);
			params.addValue("utente", utente , java.sql.Types.VARCHAR);
			params.addValue("metodo", metodo, java.sql.Types.VARCHAR);
			
			ris = template.update(query, params);
			LOGGER.info(logprefix + " inserita ["+ris+"] entry");
			
		// TODO : Pochettino 29-01-2016 : se la sequence si scassa non traccia nulla nei log
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Exception : Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query insertLogFirma", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}
	
	@Override
	public ShellSoggettiDto getDatiSoggettoByCodiceFiscale(String cf)
			throws ServiziFindomWebException {
		final String methodName = "getDatiSoggettoByCodiceFiscale";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ShellSoggettiDto soggetto = null; 
		ArrayList<ShellSoggettiDto> listaDS = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();

		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_SHELL_SOGGETTI_BYCF");
			LOGGER.info(logprefix + " cf == " + cf);
			LOGGER.info(logprefix + " Query == " + query);

			params.addValue("cfUtente", cf , java.sql.Types.VARCHAR);
			
			listaDS = ((ArrayList<ShellSoggettiDto>) template.query(query, params, new ShellSoggettiDtoRowMapper()));
			
			if(listaDS!=null && listaDS.size()>0){
				soggetto = listaDS.get(0);
				LOGGER.info(logprefix + " trovato un soggetto");
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);

			throw new ServiziFindomWebException("Exception while execute query getDatiSoggettoByCodiceFiscale", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}
		
		return soggetto;
	} 
	
	@Override
	public ArrayList<ShellSoggettiDto> getDatiSoggettoByIdSoggetto(ArrayList<String> listaId) throws ServiziFindomWebException {
		final String methodName = "getDatiSoggettoByIdSoggetto";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ArrayList<ShellSoggettiDto> listaDS = new ArrayList<ShellSoggettiDto>();
		
		if(listaId != null && listaId.size()>0){
			
			MapSqlParameterSource params = new MapSqlParameterSource();
	
			// costruisco la condizione (x, y, z)
			String condizione = "(";
			
			int j = 0;
			for (Iterator itr = listaId.iterator(); itr.hasNext();) {
				String val = (String) itr.next();
				if(j==0){
					// solo per il primo elemento non metto la virgola
					condizione += val;
				}else{
					condizione += " ,"+val;
				}
				j++;
			}
			
			condizione += ")";
			LOGGER.info(logprefix + " condizione="+condizione);
			
			try {
				String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_SHELL_SOGGETTI_BYID");
				LOGGER.info(logprefix + " Query == " + query);
	
				String qu = query.replace("#condizione#", condizione);
				LOGGER.info(logprefix + " Query == " + qu);
				
				listaDS = ((ArrayList<ShellSoggettiDto>) template.query(qu, params, new ShellSoggettiDtoRowMapper()));
	
			} catch (Exception e) {
				LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
	
				throw new ServiziFindomWebException("Exception while execute query getDatiSoggettoByIdSoggetto", e);
			} finally {
				LOGGER.info(logprefix + " - END");
			}
		}
		return listaDS;
	}

	@Override
	public ArrayList<VistaDomandeDto> getVistaDomandaByCreatoreBeneficiario(Integer idSoggettoCreatore, Integer idSoggettoBeneficiario)
			throws ServiziFindomWebException {
		final String methodName = "getVistaDomandaByCreatoreBeneficiario";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idSoggettoCreatore="+idSoggettoCreatore+" ,idSoggettoBeneficiario="+idSoggettoBeneficiario);
		
		ArrayList<VistaDomandeDto> listaDomande = new ArrayList<VistaDomandeDto>();
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_VISTA_DOMANDE_BY_FILTRO_SEARCH");
//			LOGGER.info(logprefix + " Query == " + query);
			
			String condizione = " id_soggetto_beneficiario = "+idSoggettoBeneficiario;
			if(idSoggettoCreatore!=null)
					condizione += " AND id_soggetto_creatore = " + idSoggettoCreatore;
			
			String q = query.replace("#condizione#", condizione);
			LOGGER.info(logprefix + " Query == " + q);
			
			listaDomande = (ArrayList<VistaDomandeDto>) template.query(q, params, new VistaDomandeDtoRowMapper());

		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getVistaDomandaByCreatoreBeneficiario", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}
		
		return listaDomande;
	}

	@Override
	public ArrayList<VistaDomandeDto> getVistaDomandaByCreatoreBeneficiarioDomanda(Integer idSoggettoCreatore, Integer idSoggettoBeneficiario,
			Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getVistaDomandaByCreatoreBeneficiarioDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idSoggettoCreatore="+idSoggettoCreatore+" ,idSoggettoBeneficiario="+idSoggettoBeneficiario+" ,idDomanda="+idDomanda);
		
		ArrayList<VistaDomandeDto> listaDomande = new ArrayList<VistaDomandeDto>();
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_VISTA_DOMANDE_BY_FILTRO_SEARCH");
//			LOGGER.info(logprefix + " Query == " + query);
			
			String condizione = " id_soggetto_beneficiario = "+idSoggettoBeneficiario;
			if(idSoggettoCreatore!=null)
					condizione += " AND id_soggetto_creatore = " + idSoggettoCreatore;
			
			condizione += "  AND id_domanda = " + idDomanda;
			
			String q = query.replace("#condizione#", condizione);
			LOGGER.info(logprefix + " Query == " + q);
			
			listaDomande = (ArrayList<VistaDomandeDto>) template.query(q, params, new VistaDomandeDtoRowMapper());

		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getVistaDomandaByCreatoreBeneficiarioDomanda", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}
		
		return listaDomande;
	}

	@Override
	public AmministratoriDto getAmministratoreByCodiceFiscale(String codFisc)
			throws ServiziFindomWebException {
		final String methodName = "getAmministratoreByCodiceFiscale";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ArrayList<AmministratoriDto> listaAmministratori = new ArrayList<AmministratoriDto>();
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		AmministratoriDto amministratore = null;
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_FINDOM_AMMINISTRATORI_BY_COD_FISCALE");
			LOGGER.info(logprefix + " Query == " + query);
		
			params.addValue("codiceFiscale", codFisc , java.sql.Types.VARCHAR);
			
			listaAmministratori = (ArrayList<AmministratoriDto>) template.query(query, params, new AmministratoriDtoRowMapper());
			
			if(listaAmministratori!=null && listaAmministratori.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + listaAmministratori.size());
				amministratore = listaAmministratori.get(0);
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getAmministratoreByCodiceFiscale", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}
			
		return amministratore;
	}
	
	
	@Override
	public ArrayList<VistaDomandeDto> getDomandeInserite(Integer idSoggBeneficiario,
			Integer idSoggCreatore, String ruolo, String normativa,
			String descBreveBando, String bando, String sportello,
			String statoDomanda, String numDomanda) throws ServiziFindomWebException {

		final String methodName = "getDomandeInserite";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ArrayList<VistaDomandeDto> lista = new ArrayList<VistaDomandeDto>();
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_VISTA_DOMANDE_BY_FILTRO_SEARCH");
			LOGGER.info(logprefix + " Query == " + query);
		
			String q = ServiziFindomWebDaoUtil.composeSearchDomandeQuery(query, idSoggBeneficiario, idSoggCreatore , ruolo,
				normativa, descBreveBando, bando,sportello,statoDomanda, numDomanda);
			
			lista = (ArrayList<VistaDomandeDto>) template.query(q, params, new VistaDomandeDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());		
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getDomandeInserite", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return lista;
	}
	
	@Override
	public void insertShellTSoggetto(ShellSoggettiDto newSoggetto)
			throws ServiziFindomWebException {
		final String methodName = "insertShellTSoggetto";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		int ris;
		
		String query = "";
		try {
			String cf = newSoggetto.getCodiceFiscale();
			if(cf!=null){
				cf = cf.toUpperCase();
			}
			params.addValue("codFiscale", cf, java.sql.Types.VARCHAR);
			params.addValue("denominazione", newSoggetto.getDenominazione(), java.sql.Types.VARCHAR);
			if(newSoggetto.getIdFormaGiuridica()>0){
				params.addValue("idFormaGiuridica", newSoggetto.getIdFormaGiuridica(), java.sql.Types.NUMERIC);
			}
			params.addValue("cognome", newSoggetto.getCognome(), java.sql.Types.VARCHAR);
			params.addValue("nome", newSoggetto.getNome(), java.sql.Types.VARCHAR);
			
			// inserisco record in SHELL_T_DETTAGLIO_DOMANDA
			if(newSoggetto.getIdFormaGiuridica()>0){
				query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_SHELL_SOGGETTI");
			}else{
				query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_SHELL_SOGGETTI_BIS");
			}
			LOGGER.info(logprefix + " Query == " + query);
			
			ris = template.update(query, params);
			
		}catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query insertShellTSoggetto", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}	
	}
	
	@Override
	public void updateShellTSoggetto(ShellSoggettiDto soggetto) throws ServiziFindomWebException {
		final String methodName = "updateShellTSoggetto";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		int ris;
		
		String query = "";
		try {
			
			params.addValue("idSoggetto", soggetto.getIdSoggetto(), java.sql.Types.NUMERIC);
			params.addValue("denominazione", soggetto.getDenominazione(), java.sql.Types.VARCHAR);
			params.addValue("idFormaGiuridica", soggetto.getIdFormaGiuridica(), java.sql.Types.NUMERIC);
			params.addValue("cognome", soggetto.getCognome(), java.sql.Types.VARCHAR);
			params.addValue("nome", soggetto.getNome(), java.sql.Types.VARCHAR);
			
			// aggiorno record in SHELL_T_SOGGETTI
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_SHELL_SOGGETTI");
			LOGGER.info(logprefix + " Query == " + query);
			
			ris = template.update(query, params);
			
		}catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query updateShellTSoggetto", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}	
		
	}
	
	@Override
	public ArrayList<VistaSportelliAttiviDto> getVistaSportelliAttivi() throws ServiziFindomWebException {

		final String methodName = "getVistaSportelliAttivi";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ArrayList<VistaSportelliAttiviDto> lista = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_VISTA_SPORTELLI_ATTIVI");
			LOGGER.info(logprefix + " Query == " + query);
			
			lista = (ArrayList<VistaSportelliAttiviDto>) template.query(query, params, new VistaSportelliAttiviDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getVistaSportelliAttivi", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return lista;
	}
	
	@Override
	public ArrayList<VistaSportelliAttiviDto> getVistaSportelliAttiviByNormativa(int idNormativa) throws ServiziFindomWebException {

		final String methodName = "getVistaSportelliAttiviByNormativa";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idNormativa="+idNormativa);
		
		ArrayList<VistaSportelliAttiviDto> lista = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_VISTA_SPORTELLI_ATTIVI_BY_IDNORMATIVA");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idNormativa",idNormativa, java.sql.Types.INTEGER);
			lista = (ArrayList<VistaSportelliAttiviDto>) template.query(query, params, new VistaSportelliAttiviDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getVistaSportelliAttiviByNormativa", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return lista;
	}
	
	@Override
	public ArrayList<VistaSportelliAttiviDto> getVistaSportelliAttiviByIdBando(int idBando) throws ServiziFindomWebException {
		final String methodName = "getVistaSportelliAttiviByIdBando";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idBando="+idBando);
		
		ArrayList<VistaSportelliAttiviDto> lista = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_VISTA_SPORTELLI_ATTIVI_BY_IDBANDO");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idBando", idBando, java.sql.Types.INTEGER);
			lista = (ArrayList<VistaSportelliAttiviDto>) template.query(query, params, new VistaSportelliAttiviDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getVistaSportelliAttiviByIdBando", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return lista;
	}
	
	@Override
	public ProssimoSportelloAttivoDto getProssimoSportelloAttivo() throws ServiziFindomWebException {
		final String methodName = "getProssimoSportelloAttivo";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ProssimoSportelloAttivoDto sportello = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_PPROSSIMO_SPORTELLO_ATTIVO");
			LOGGER.info(logprefix + " Query == " + query);
			
			sportello = (ProssimoSportelloAttivoDto) template.queryForObject(query, params, new ProssimoSportelloAttivoDtoRowMapper());
			
			if(sportello!=null){
				LOGGER.info(logprefix + " trovato prossimo bando:" + sportello.getDescrizioneBando());
			}
		} catch (EmptyResultDataAccessException e){
			LOGGER.info(logprefix + " nessun bando prossimo all'apertura trovato.");
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return sportello;
	}
	
	@Override
	public ArrayList<TipolBeneficiariDto> getListaTipolBeneficiari() throws ServiziFindomWebException {
		final String methodName = "getListaTipolBeneficiari";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ArrayList<TipolBeneficiariDto> lista = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_TIPOL_BENEFICIARI");
			LOGGER.info(logprefix + " Query == " + query);
			
			lista = (ArrayList<TipolBeneficiariDto>) template.query(query, params, new TipolBeneficiariDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getListaTipolBeneficiari", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return lista;
	}
	
	
	@Override
	public ArrayList<TipolBeneficiariDto> getListaTipolBeneficiariByIdBando(int idBando) throws ServiziFindomWebException {
		final String methodName = "getListaTipolBeneficiariByIdBando";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idBando="+idBando);
		
		ArrayList<TipolBeneficiariDto> lista = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_TIPOL_BENEFICIARI_BY_IDBANDO");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idBando", idBando, java.sql.Types.INTEGER);
			lista = (ArrayList<TipolBeneficiariDto>) template.query(query, params, new TipolBeneficiariDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getListaTipolBeneficiariByIdBando", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return lista;
	}
	
	@Override
	public ArrayList<VistaDomandeBeneficiariDto> getVistaDomandeBeneficiari(int idBando, int idSoggettoBeneficiario)
			throws ServiziFindomWebException {
		final String methodName = "getVistaDomandeBeneficiari";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idBando="+idBando+" , idSoggettoBeneficiario="+idSoggettoBeneficiario);
		
		ArrayList<VistaDomandeBeneficiariDto> lista = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_VISTA_DOMANDE_BENEFICIARI");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idBando", idBando, java.sql.Types.INTEGER);
			params.addValue("idSoggettoBeneficiario", idSoggettoBeneficiario, java.sql.Types.INTEGER);
			
			lista = (ArrayList<VistaDomandeBeneficiariDto>) template.query(query, params, new VistaDomandeBeneficiariDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getVistaDomandeBeneficiari", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return lista;
	}
	
	@Override
	public ArrayList<AggrTTemplateDto> getAggrTemplateWithFindomBandi(int idBando) throws ServiziFindomWebException {
		final String methodName = "getAggrTemplateWithFindomBandi";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idBando="+idBando);
		
		ArrayList<AggrTTemplateDto> lista = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_AGGR_TEMPLATE_WITH_FINDOM_BANDI");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idBando", idBando, java.sql.Types.INTEGER);
			lista = (ArrayList<AggrTTemplateDto>) template.query(query, params, new AggrTTemplateDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getAggrTemplateWithFindomBandi", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return lista;
	}
	
	@Override
	public ArrayList<AggrTModelDto> getAggrTModelByUserTemplateProg(String userId, String templateCode, Integer modelProg)
			throws ServiziFindomWebException {
		final String methodName = "getAggrTModelByUserTemplateProg";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " userId="+userId);
		LOGGER.info(logprefix + " templateCode="+templateCode);
		LOGGER.info(logprefix + " modelProg="+modelProg);
		
		ArrayList<AggrTModelDto> lista = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_AGGR_MODEL_BY_USTPPR");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("userId", userId, java.sql.Types.VARCHAR);
			params.addValue("templateCode", templateCode, java.sql.Types.VARCHAR);
			params.addValue("modelProg", modelProg, java.sql.Types.INTEGER);
			
			lista = (ArrayList<AggrTModelDto>) template.query(query, params, new AggrTModelDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getAggrTModelByUserTemplateProg", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}

		return lista;
	}

	@Override
	public int insertShellTDomande(Integer idSoggettoCollegato,
			Integer idSoggettoBeneficiario, Integer numSportello,
			Integer idSoggettoInvio, String dataInvioDomanda, Integer idModel,
			Integer idTipologiaBeneficiario) throws ServiziFindomWebException {
		
		final String methodName = "getAggrTModelByUserTemplateProg";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idSoggettoCollegato="+idSoggettoCollegato);
		LOGGER.info(logprefix + " idSoggettoBeneficiario="+idSoggettoBeneficiario);
		LOGGER.info(logprefix + " numSportello="+numSportello);
		LOGGER.info(logprefix + " idSoggettoInvio="+idSoggettoInvio);
		LOGGER.info(logprefix + " dataInvioDomanda="+dataInvioDomanda);
		LOGGER.info(logprefix + " idModel="+idModel);
		LOGGER.info(logprefix + " idTipologiaBeneficiario="+idTipologiaBeneficiario);
		
		int ris = 0;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_SHELLDOMANDE");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idSoggettoCollegato", idSoggettoCollegato , java.sql.Types.INTEGER);
			params.addValue("idSoggettoBeneficiario", idSoggettoBeneficiario, java.sql.Types.INTEGER);
			params.addValue("numSportello", numSportello , java.sql.Types.INTEGER);
			params.addValue("idSoggettoInvio", idSoggettoInvio, java.sql.Types.INTEGER);
			params.addValue("dataInvioDomanda", dataInvioDomanda, java.sql.Types.VARCHAR);
			params.addValue("idModel", idModel, java.sql.Types.INTEGER);
			params.addValue("idTipologiaBeneficiario", idTipologiaBeneficiario, java.sql.Types.INTEGER);
			
			ris = template.update(query, params);
			
		} catch (ServiziFindomWebException e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query insertShellTDomande", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
		
	}
	

	@Override
	public ArrayList<AggrDataDto> getAggrDataByIdDomanda(Integer idDomanda)
			throws ServiziFindomWebException {
			
			final String methodName = "getAggrDataByIdDomanda";
			final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
			LOGGER.info(logprefix + " BEGIN");
			LOGGER.info(logprefix + " idDomanda="+idDomanda);
			
			ArrayList<AggrDataDto> lista = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			try {
				String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_AGGR_DATA_BY_IDDOMANDA");
				LOGGER.info(logprefix + " Query == " + query);
				
				params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
				
				lista = (ArrayList<AggrDataDto>) template.query(query, params, new AggrDataDtoRowMapper());
				
				if(lista!=null && lista.size()>0){
					LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
				}
				
			} catch (Exception e) {
				LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
				throw new ServiziFindomWebException("Exception while execute query getAggrDataByIdDomanda", e);
			} finally {
				LOGGER.info(logprefix + " - END");
			}

			return lista;
			
	}
	
	
	@Override	
	@Transactional(rollbackFor={ServiziFindomWebException.class,AggregatoreException.class})
	public Integer deleteDomanda(Integer idDomanda, String userId, String templateCode, Integer modelProgr,  Aggregatore aggregatore) 
			throws ServiziFindomWebException, AggregatoreException {
		
		final String methodName = "deleteDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		LOGGER.info(logprefix + " userId="+userId);
		LOGGER.info(logprefix + " templateCode="+templateCode);
		LOGGER.info(logprefix + " modelProgr="+modelProgr);	
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		int ris1 = 0;
		boolean ris2;
		try {			
			//verifico la presenza di allegati			
			LOGGER.info(logprefix + "sto per effettuare la lettura degli allegati della domanda = " + idDomanda);
			ArrayList<ShellDocumentoIndexDto> listaDoc = getAllDocumentoIndex(idDomanda);
			LOGGER.info(logprefix + "la domanda ha  = " + (listaDoc == null ? "0" : listaDoc.size()) + " elementi " );
			
			if(listaDoc!=null && !listaDoc.isEmpty()){
				for (int i = 0; i < listaDoc.size(); i++) {
					// ciclo sui doc trovati nella shell_t_documento_index
					ShellDocumentoIndexDto curDoc = (ShellDocumentoIndexDto) listaDoc.get(i);
					if(curDoc==null){
						continue;
					}
					int idDocumentoIndex = curDoc.getIdDocumentoIndex(); //identificativo del record da cancellare 	
					int idAllegato = curDoc.getIdAllegato();             //identificativo dell'allegato riferito dal record da cancellare
					String uuidDoc = curDoc.getUuidNodo();               //identificativo index dell'allegato riferito dal record da cancellare
					LOGGER.info(logprefix + " curDoc = "+idAllegato+ ", uuid=["+uuidDoc+"]");
            		LOGGER.info(logprefix + "sto per effettuare la cancellazione da tabella SHELL_T_DOCUMENTO_INDEX con idDocumentoIndex = " + idDocumentoIndex);
					int ris0 =  deleteDocumentoIndex(idDocumentoIndex+"");
					LOGGER.info(logprefix + "cancellazione effettuata da tabella SHELL_T_DOCUMENTO_INDEX; numero di record cancellati = " + ris0);
				}
			}
			
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("DELETE_SHELL_T_DOMANDA");

			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			
			ris1 = template.update(query, params);
			LOGGER.info(logprefix + " SHELL_T_TDOMANDE, cancellazione avvenuta, ris1= " + ris1);
			
			ris2 = aggregatore.deleteModel(userId, templateCode, modelProgr);
			LOGGER.info(logprefix + " AGGR_T_MODEL, cancellazione avvenuta, ris2= " + ris2);
			
			// solo per bando 87 "Bonus Piemonte", verifico se devo aggiornare una entry in findom_t_soggetti_bonus_covid
			if(StringUtils.equals(templateCode, "BONUS")){
				// verifico quante domande ha fatto il beneficiario
				
				LOGGER.info(logprefix + " codFiscale == " + userId);
				
				String queryCountCovid = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("COUNT_SOGGETTI_BONUS_COVID");
				LOGGER.info(logprefix + " bando BONUS, queryCountCovid == " + queryCountCovid);
				
				MapSqlParameterSource params2 = new MapSqlParameterSource();
				params2.addValue("codFiscale", userId, java.sql.Types.VARCHAR);
				params2.addValue("partIva", userId, java.sql.Types.VARCHAR);
				
				Integer numPresenze = template.queryForObject(queryCountCovid, params2, Integer.class);
				LOGGER.info(logprefix + " numPresenze == " + numPresenze);
				
				if(numPresenze==0){
					String queryUpdateCovid = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_SOGGETTI_BONUS_COVID");
					LOGGER.info(logprefix + " queryUpdateCovid == " + queryUpdateCovid);
					MapSqlParameterSource params3 = new MapSqlParameterSource();
					params3.addValue("codFiscale", userId, java.sql.Types.VARCHAR);
					
					Integer ris3 = template.update(queryUpdateCovid, params3);
					LOGGER.info(logprefix + " findom_t_soggetti_bonus_covid, update avvenuto, ris3= " + ris3);
				}
			}
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query deleteDomanda", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}
		
		return ris1;
	}
	
	@Override
	public String getXMLDomanda(Integer idDomanda) 	throws ServiziFindomWebException {
		final String methodName = "getXMLDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String xmlDomanda = null;
		
		if(idDomanda!=null){
			try {
				String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_XMLDOMANDA");
				LOGGER.info(logprefix + " Query == " + query);
				
				params.addValue("modelId", idDomanda, java.sql.Types.INTEGER);
				xmlDomanda = (String) template.queryForObject(query, params, new XMLDtoRowMapper());
				
			} catch (Exception e) {
				LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
				LOGGER.info(logprefix + " - END");
				throw new ServiziFindomWebException("Exception while execute query getXMLDomanda", e);
			} 
		}
		
		LOGGER.info(logprefix + " - END");
		return xmlDomanda;
	}

	@Override
	public Integer insertTFileDomanda(Integer idDomanda, String xmlDomanda,	byte[] pdfFile, String messageDigest) throws ServiziFindomWebException {
		final String methodName = "insertTFileDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		Integer ris;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";

		try{

			// inserisco record in SHELL_T_FILE_DOMANDE
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_SHELL_T_FILE_DOMANDE");
			LOGGER.info(logprefix + " query == " + query);
			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			params.addValue("xmlDomanda", xmlDomanda, java.sql.Types.SQLXML);
			params.addValue("pdfFile", pdfFile, java.sql.Types.LONGVARBINARY);
			params.addValue("messageDigest", messageDigest, java.sql.Types.VARCHAR);

			ris = template.update(query, params);
			LOGGER.info(logprefix + " righe inserite=" + ris);

		} catch (Exception e) {
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query insertTFileDomanda", e);

		} finally {
			LOGGER.info(logprefix + " - END");
		}
		return ris;
	}

	@Override
	public NumberMaxDto getNumeroMassimoDomandeInviate(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getNumeroMassimoDomandeInviate";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		NumberMaxDto nMax = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		if(idDomanda!=null){
			try {
				String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_NUMMAXDOMANDE");
				LOGGER.info(logprefix + " Query == " + query);
				
				params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
				nMax = (NumberMaxDto) template.queryForObject(query, params, new NumberMaxDtoRowMapper());
				
			} catch (Exception e) {
				LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
				LOGGER.info(logprefix + " - END");
				throw new ServiziFindomWebException("Exception while execute query getNumeroMassimoDomandeInviate", e);
			} 
		}
		
		LOGGER.info(logprefix + " - END");
		return nMax;
	}
		
	@Override
	public Integer updateShellDomande(Integer idDomanda, Integer idSoggettoCollegato, String statoDomanda, Integer idStatoIstruttoria, Date dataUfficiale) throws ServiziFindomWebException {
		final String methodName = "updateShellDomande";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		LOGGER.info(logprefix + " idSoggettoCollegato="+idSoggettoCollegato);
		
		int ris = 0;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";

		try {
			if (statoDomanda.equalsIgnoreCase(Constants.STATO_CONCLUSA))
				query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_SHELLDOMANDE_STATO_CO"); 
			else{				
				 query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_SHELLDOMANDE");				
			}
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda , java.sql.Types.INTEGER);
			params.addValue("idSoggettoCollegato", idSoggettoCollegato, java.sql.Types.INTEGER);
			params.addValue("dataTx", dataUfficiale, java.sql.Types.TIMESTAMP);
			
			if (!statoDomanda.equalsIgnoreCase(Constants.STATO_CONCLUSA)){
				String condizione = "";
				if(statoDomanda.equalsIgnoreCase(Constants.STATO_INVIATA) && idStatoIstruttoria!=null){					
					condizione= " , id_stato_istr = "+idStatoIstruttoria;						
				}
				query = query.replace("#condizione#", condizione);
			}
			
			ris = template.update(query, params);
			
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query updateShellDomande", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}
	
	@Override
	public Integer updateShellDomandeBandoDemat(Integer idDomanda, Integer idSoggettoCollegato) throws ServiziFindomWebException {
		final String methodName = "updateShellDomandeBandoDemat";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		LOGGER.info(logprefix + " idSoggettoCollegato="+idSoggettoCollegato);
		
		int ris = 0;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_SHELLDOMANDE_BANDO_DEMAT");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda , java.sql.Types.INTEGER);
			params.addValue("idSoggettoCollegato", idSoggettoCollegato, java.sql.Types.INTEGER);
			
			ris = template.update(query, params);
			
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query updateShellDomande", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}
		
	@Override
	public byte[] getPdfDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getPdfDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		byte[] pdfArray = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_PDF_DOMANDA");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			pdfArray = (byte[]) template.queryForObject(query, params, new FileDtoRowMapper());
			
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getPdfDomanda", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return pdfArray;
	}
	
	@Override
	public ShellDocumentoIndexDto getDocumentoIndex(String idDocumento, String codFisc, Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDocumento="+idDocumento);
		LOGGER.info(logprefix + " codFisc="+codFisc);
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		ShellDocumentoIndexDto doc = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_DOCUMENTO_INDEX");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDocumento", idDocumento, java.sql.Types.INTEGER);
			params.addValue("codFisc", codFisc, java.sql.Types.VARCHAR);
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			
			doc = (ShellDocumentoIndexDto) template.queryForObject(query, params, new ShellDocumentoIndexDtoRowMapper());
		
		} catch (EmptyResultDataAccessException e){
			LOGGER.warn(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " continuo ");

		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getDocumentoIndex", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		
		return doc;
	}
	
	@Override
	public ShellDocumentoIndexDto getDocumentoIndex(String idDocumento, Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDocumento="+idDocumento);		
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		ShellDocumentoIndexDto doc = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_DOCUMENTO_INDEX_DOMANDA");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDocumento", idDocumento, java.sql.Types.INTEGER);			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			
			doc = (ShellDocumentoIndexDto) template.queryForObject(query, params, new ShellDocumentoIndexDtoRowMapper());
		
		} catch (EmptyResultDataAccessException e){
			LOGGER.warn(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " continuo ");

		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getDocumentoIndex", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		
		return doc;
	}
	
	@Override
	public ShellDocumentoIndexDto getDocumentoIndex(Integer idDomanda,	String nomeFile) throws ServiziFindomWebException {
		final String methodName = "getDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		LOGGER.info(logprefix + " nomeFile="+nomeFile);
		
		ShellDocumentoIndexDto doc = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_DOCUMENTO_INDEX_BYIDDOM_NOMEFILE");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			params.addValue("nomeFile", nomeFile, java.sql.Types.VARCHAR);
			
			doc =  (ShellDocumentoIndexDto)template.queryForObject(query, params, new ShellDocumentoIndexDtoRowMapper());
//			LOGGER.info(logprefix + " Query fatta;  docTmp=" + docTmp);
//			
//			if(docTmp!=null){
//				doc = (ShellDocumentoIndexDto)docTmp;
//			}
			
		} catch (EmptyResultDataAccessException e){
			LOGGER.warn(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " continuo ");
			
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getDocumentoIndex", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		
		return doc;
	}

	public ShellDocumentoIndexDto getDocumentoFirmato(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getDocumentoFirmato";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);		
		
		ShellDocumentoIndexDto doc = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_DOCUMENTO_FIRMATO");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
						
			doc =  (ShellDocumentoIndexDto)template.queryForObject(query, params, new ShellDocumentoIndexDtoRowMapper());

			
		} catch (EmptyResultDataAccessException e){
			LOGGER.warn(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " continuo ");
			
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getDocumentoFirmato", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		
		return doc;
	}
	
	@Override
	public ShellDocumentoIndexDto getDocumentoIndex(String uuid) throws ServiziFindomWebException {
		final String methodName = "getDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " uuid="+uuid);
		
		ShellDocumentoIndexDto doc = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_DOCUMENTO_INDEX_BYUUID");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("uuid", uuid, java.sql.Types.VARCHAR);
			
			doc = (ShellDocumentoIndexDto) template.queryForObject(query, params, new ShellDocumentoIndexDtoRowMapper());
			
		} catch (EmptyResultDataAccessException e){
			LOGGER.warn(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " continuo ");
			
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getDocumentoIndex", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		
		return doc;
	}
	
	@Override
	public int insertShellTDocumentoIndex(Integer idDomanda, String uuidIndex, String repository, String upFileFileName,
			String messageDigest, String dtVerificaFirma, Integer idStatoDocumentoIndex,
			Integer idTipologiaNewDoc, Integer idSoggetto)	throws ServiziFindomWebException {

		final String methodName = "insertShellTDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		LOGGER.info(logprefix + " uuidIndex="+uuidIndex);
		LOGGER.info(logprefix + " repository="+repository);
		LOGGER.info(logprefix + " upFileFileName="+upFileFileName);
		LOGGER.info(logprefix + " messageDigest="+messageDigest);
		LOGGER.info(logprefix + " dtVerificaFirma="+dtVerificaFirma);
		LOGGER.info(logprefix + " idStatoDocumentoIndex="+idStatoDocumentoIndex);
		LOGGER.info(logprefix + " idTipologiaNewDoc="+idTipologiaNewDoc);
		LOGGER.info(logprefix + " idSoggetto="+idSoggetto);
		
		int ris = 0;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_DOCUMENTO_INDEX");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda , java.sql.Types.INTEGER);
			params.addValue("uuidIndex", uuidIndex, java.sql.Types.VARCHAR);
			params.addValue("repositoryValue", repository , java.sql.Types.VARCHAR);
			params.addValue("nomeFile", upFileFileName, java.sql.Types.VARCHAR);
			params.addValue("messageDigest", messageDigest, java.sql.Types.VARCHAR);			
			params.addValue("dtVerificaFirma", DateUtil.getDataDaStringa(dtVerificaFirma), java.sql.Types.TIMESTAMP);
			params.addValue("idStatoDocumentoIndex", idStatoDocumentoIndex, java.sql.Types.INTEGER);
			params.addValue("idAllegato", idTipologiaNewDoc, java.sql.Types.INTEGER);
			params.addValue("idSoggetto", idSoggetto, java.sql.Types.INTEGER);
			
			ris = template.update(query, params);
			LOGGER.info(logprefix + " inserita ["+ris+"] entry");
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " - Errore insertShellTDocumentoIndex su idDomanda="+idDomanda+" ,uuidIndex="+uuidIndex+" ,nomeFile="+upFileFileName); 
			LOGGER.error(logprefix + " - Exception : Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query insertShellTDocumentoIndex", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}
	
	@Override
	public ArrayList<FindomAllegatiDto> getAllegati(Integer idBando, Integer idDomanda, Integer idSportello) throws ServiziFindomWebException {
		final String methodName = "getAllegati";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		ArrayList<FindomAllegatiDto> lista = null; 
		
		try {
			if(idBando != null && idDomanda != null && idSportello!= null){
				query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_FINDOM_ALLEGATI1");
				params.addValue("idBando", idBando , java.sql.Types.INTEGER);
				params.addValue("idDomanda", idDomanda , java.sql.Types.INTEGER);
				params.addValue("idSportello", idSportello , java.sql.Types.INTEGER);
			}else{
				query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_FINDOM_ALLEGATI");
				params.addValue("idBando", idBando , java.sql.Types.INTEGER);
			}
			LOGGER.info(logprefix + " Query == " + query);
			
			lista = (ArrayList<FindomAllegatiDto>) template.query(query, params, new FindomAllegatiDtoRowMapper());
			
			if(lista!=null && lista.size()>0){
				LOGGER.info(logprefix + " trovata una lista di dimensione " + lista.size());
			}
					
		}catch (Exception e) {
			
			LOGGER.info(logprefix + " - Exception : Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getAllegati", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return lista;
	}
	
	

	@Override
	public Integer deleteDocumentoIndex(String idDoc) throws ServiziFindomWebException {
		final String methodName = "deleteDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDoc="+idDoc);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		int ris1 = 0;
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("DELETE_DOCUMENTO_INDEX");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDoc", idDoc, java.sql.Types.INTEGER);
			
			ris1 = template.update(query, params);
			LOGGER.info(logprefix + " cancellazione avvenuta, ris1= " + ris1);
			
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query deleteDocumentoIndex", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}
		
		return ris1;
	}
	
	
	@Override
	public ShellDocumentoIndexDto getLastDocumentoInserito(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getLastDocumentoInserito";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		ShellDocumentoIndexDto doc = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_LAST_DOCUMENTO_INDEX");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			
			doc = (ShellDocumentoIndexDto) template.queryForObject(query, params, new ShellDocumentoIndexDtoRowMapper());
			
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getLastDocumentoInserito", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		
		return doc;
	}
	
	@Override
	public ArrayList<String> getTipoDocumentoUpload(String parametro) throws ServiziFindomWebException {
		final String methodName = "getTipoDocumentoUpload";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");

		MapSqlParameterSource params = new MapSqlParameterSource();
		String tipiDocumento = null;
		ArrayList<String> estensioni = new ArrayList<String>();

		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_PARAMETRO");
			LOGGER.info(logprefix + " Query == " + query);

			params.addValue("codice", parametro, java.sql.Types.VARCHAR);
			tipiDocumento = (String) template.queryForObject(query, params, new TipiDocRowMapper());
			
			StringTokenizer st = new StringTokenizer(tipiDocumento, ",");

			while (st.hasMoreElements()) {
				estensioni.add((String)st.nextElement());
			}
		

		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Exception while execute query getTipoDocumentoUpload", e);
		} 


		LOGGER.info(logprefix + " - END");
		return estensioni;
	}
	
	@Override
	public String getValoreParametro(String codice) throws ServiziFindomWebException {
		final String methodName = "getValoreParametro";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");

		MapSqlParameterSource params = new MapSqlParameterSource();
		String valore = "";
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_VALORE_PARAMETRO");
			LOGGER.info(logprefix + " Query == " + query);

			params.addValue("codice", codice, java.sql.Types.VARCHAR);
			valore = (String) template.queryForObject(query, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(logprefix + " EmptyResultDataAccessException - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Exception while execute query getValoreParametro", e);
        
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Exception while execute query getValoreParametro", e);
		} 

		LOGGER.info(logprefix + " - END");
		return valore;
	}
	
	@Override
	public String getMessageDigestDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getMessageDigestDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		String messageDigest = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_MESSAGE_DIGEST_DOMANDA");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			messageDigest = (String) template.queryForObject(query, params, String.class);
			
		} catch (EmptyResultDataAccessException e){
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.error(logprefix + " continuo ");

		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getMessageDigestDomanda", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return messageDigest;
	}
	
	@Override
	public String getValueFromXML(String nodo, String campo, Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getValueFromXML";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " nodo="+nodo);
		LOGGER.info(logprefix + " campo="+campo);
		
		String valore = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = "SELECT " +
   					"UNNEST(XPATH ('/tree-map/" + nodo + "/map/" + campo +"/text()'::text,aggr_t_model.serialized_model))::text AS VALORE " +
					"FROM AGGR_T_MODEL  " +
					"WHERE AGGR_T_MODEL.MODEL_ID = :idDomanda ";
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			List<String> strLst = template.query(query, params, new StringDtoRowMapper());
			if ( strLst.isEmpty() ){
				return null;
			}else if ( strLst.size() == 1 ) { // list contains exactly 1 element
				return strLst.get(0);
			}else{  // list contains more than 1 elements
				//your wish, you can either throw the exception or return 1st element.   
				return null;
			}
						
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getValueFromXML", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
	}
	
	@Override
	public ArrayList<ShellDocumentoIndexDto> getAllDocumentoIndex(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getAllDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		ArrayList<ShellDocumentoIndexDto> listaDoc = new ArrayList<ShellDocumentoIndexDto>();
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_ALL_DOCUMENTO_INDEX_BY_ID_DOMANDA"); 
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
						
			listaDoc = (ArrayList<ShellDocumentoIndexDto>)template.query(query, params, new ShellDocumentoIndexDtoRowMapper());			
			
		} catch (EmptyResultDataAccessException e){
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.error(logprefix + " continuo ");
			
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getAllDocumentoIndex", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		
		return listaDoc;
	}	
	
	public ArrayList<Integer> getRegoleTemplate(int idBando) throws ServiziFindomWebException{
		final String methodName = "getRegoleTemplate";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");		
		ArrayList<Integer> lista = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_KEY_REGOLE"); 
			LOGGER.info(logprefix + " Query == " + query);			
			params.addValue("idBando", idBando, java.sql.Types.NUMERIC);
			LOGGER.info(logprefix + " idBando="+idBando);
			
			//NB: StringDtoRowMapper considera rs.getString(1); se la query cambiasse verificare che continui a funzionare
			lista = ((ArrayList<Integer>) template.query(query, params, new IntegerDtoRowMapper()));

			if ((lista != null) && (lista.size() > 0)) {
				LOGGER.info(logprefix + " Trovata una lista di regole di dimensione = " + lista.size());
			}
				
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);		
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getRegoleTemplate", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}	    
		return lista;
	}
	
	public RegolaDto getRegola(int idRegola) throws ServiziFindomWebException {
		final String methodName = "getRegola";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		RegolaDto regola = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {			
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_REGOLA"); 
			LOGGER.info(logprefix + " Query == " + query);			
			params.addValue("idRegola", idRegola, java.sql.Types.NUMERIC);
			LOGGER.info(logprefix + " idRegola="+idRegola);
			regola = (RegolaDto) template.queryForObject(query, params, new RegolaDtoRowMapper());
			
        } catch (Exception e) {			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getRegola", e);			
		} finally {
			LOGGER.info(logprefix + " END");
		}		
		return regola;
	}
	
	
	public ArrayList<ParametroRegolaBandoDto> getParametriRegola(String codRegola, int templateId) throws ServiziFindomWebException {
		final String methodName = "getParametriRegola";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ArrayList<ParametroRegolaBandoDto> lista = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_ELENCO_PARAMETRI_REGOLA"); 
			LOGGER.info(logprefix + " Query == " + query);		
			params.addValue("templateId", templateId, java.sql.Types.NUMERIC);
			params.addValue("codRegola", codRegola, java.sql.Types.VARCHAR);
			LOGGER.info(logprefix + " templateId="+templateId);
			LOGGER.info(logprefix + " codRegola="+codRegola);
			
			//NB: IntegerDtoRowMapper considera rs.getString(1); se la query cambiasse verificare che continui a funzionare
			lista = ((ArrayList<ParametroRegolaBandoDto>) template.query(query, params, new ParametroRegolaBandoDtoRowMapper()));
			
			if ((lista != null) && (lista.size() > 0)) {
				LOGGER.info(logprefix + " Trovata una lista di regole di dimensione = " + lista.size());
			}
				
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);		
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getParametriRegola", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}	    
		return lista;
	}
	
	public RegoleVerificaFirmaDto getRegoleVerificaFirma(Integer idBando) throws ServiziFindomWebException{
		final String methodName = "getRegoleVerificaFirma";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		RegoleVerificaFirmaDto regola = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {			
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_REGOLE_VERIFICA_FIRMA"); 
			LOGGER.info(logprefix + " Query == " + query);			
			params.addValue("idBando", idBando, java.sql.Types.NUMERIC);
			LOGGER.info(logprefix + " idBando="+idBando);
			regola = (RegoleVerificaFirmaDto) template.queryForObject(query, params, new RegoleVerificaFirmaDtoRowMapper());
			
        } catch (Exception e) {			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getRegola", e);			
		} finally {
			LOGGER.info(logprefix + " END");
		}		
		return regola;
	}
	
	public void copiaDomandaPerIstruttoria(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "copiaDomandaPerIstruttoria";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		int ris;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_MODEL_ISTRUTTORIA"); 
			LOGGER.info(logprefix + " Query == " + query);		
			params.addValue("idDomanda", idDomanda, java.sql.Types.NUMERIC);
			params.addValue("idStatoDomandaIstr", Constants.STATO_RICEVUTA);
			params.addValue("idStatoDomanda", Constants.STATO_INVIATA);
			LOGGER.info(logprefix + " idDomanda="+idDomanda);			
			
			ris = template.update(query, params);
			LOGGER.info(logprefix + " inserita ["+ris+"] entry");
			
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_MODEL_INDEX_ISTRUTTORIA"); 
			LOGGER.info(logprefix + " Query == " + query);		
			params.addValue("idDomanda", idDomanda, java.sql.Types.NUMERIC);

			ris = template.update(query, params);
			LOGGER.info(logprefix + " inserita ["+ris+"] entry");
			
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);		
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo copiaDomandaPerIstruttoria", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}	    		
	}

	public String getFlagIstruttoriaEsterna(Integer idBando)  throws ServiziFindomWebException {
		final String methodName = "getFlagIstruttoriaEsterna";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");

		MapSqlParameterSource params = new MapSqlParameterSource();
		String valore = "";
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_FLAG_ISTRUTTORIA_ESTERNA");
			LOGGER.info(logprefix + " Query == " + query);

			params.addValue("idBando", idBando, java.sql.Types.INTEGER);
			valore = (String) template.queryForObject(query, params, String.class);			
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(logprefix + " EmptyResultDataAccessException - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Exception while execute query getFlagIstruttoriaEsterna", e);
        
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Exception while execute query getFlagIstruttoriaEsterna", e);
		} 

		LOGGER.info(logprefix + " - END");
		return (valore == null ? "" : valore);
	}
	
	
	public Integer getIdStatoIstruttoriaByCodice(String codice) throws ServiziFindomWebException {
		final String methodName = "getIdStatoIstruttoriaByCodice";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");

		MapSqlParameterSource params = new MapSqlParameterSource();		
		Integer idStatoIstruttoria = null;		
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_ID_STATO_ISTRUTTORIA");
			LOGGER.info(logprefix + " Query == " + query);

			params.addValue("codice", codice, java.sql.Types.VARCHAR);			
			String idStatoIstruttoriaStr = (String) template.queryForObject(query, params, String.class);
			
			if (idStatoIstruttoriaStr!=null) {
				idStatoIstruttoria = new Integer(idStatoIstruttoriaStr);
			}
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(logprefix + " EmptyResultDataAccessException - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Exception while execute query getIdStatoIstruttoriaByCodice", e);
        
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Exception while execute query getIdStatoIstruttoriaByCodice", e);
		} 

		LOGGER.info(logprefix + " - END");
		return idStatoIstruttoria;	
	}
	// GETTERS && SETTERS
	
	@Override
	public String getIstanzaRoutingBySportello(Integer idSportello) throws ServiziFindomWebException {
		final String methodName = "getIstanzaRoutingBySportello";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idSportello="+idSportello);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String istanzaRoutingDomanda = null;
		
		if(idSportello!=null){
			try {
				String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_ISTANZA_BY_SPORTELLO");
				LOGGER.info(logprefix + " Query == " + query);
				
				params.addValue("idSportello", idSportello, java.sql.Types.INTEGER);
				istanzaRoutingDomanda = (String) template.queryForObject(query, params, String.class);
			
			} catch (DataAccessException e) {
				LOGGER.info(logprefix + " Istanza sportello "+idSportello+" non trovata.");
			} 				
		}
		
		LOGGER.info(logprefix + " - END");
		return istanzaRoutingDomanda;
	}

	@Override
	public String getFlagBandoDematerializzatoByIdDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getFlagBandoDematerializzatoByIdDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String flagBandoDematerializzato = null;
		
		if(idDomanda!=null){
			try {
				String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_FLAG_BANDO_DEMAT_BY_DOMANDA");
				LOGGER.info(logprefix + " Query == " + query);
				
				params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
				flagBandoDematerializzato = (String) template.queryForObject(query, params, String.class);
			
			} catch (DataAccessException e) {
				LOGGER.info(logprefix + " Domanda "+idDomanda+" non trovata.");
			} 				
		}
		
		LOGGER.info(logprefix + " - END");
		return flagBandoDematerializzato;
	}
	
	public IndexDAO getIndexDAO() {
		return indexDAO;
	}
	public void setIndexDAO(IndexDAO indexDAO) {
		this.indexDAO = indexDAO;
	}
	
	public ArrayList<VistaDocDematDto> getVistaDocDemat(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getVistaDocDemat";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		ArrayList<VistaDocDematDto> lista = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_V_DOC_DEMAT"); 
			LOGGER.info(logprefix + " Query == " + query);		
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);			
			LOGGER.info(logprefix + " idDomanda="+idDomanda);
		
			lista = ((ArrayList<VistaDocDematDto>) template.query(query, params, new VistaDocDematDtoRowMapper()));
			
			if ((lista != null) && (lista.size() > 0)) {
				LOGGER.info(logprefix + " Trovata una lista di dati doc demat di dimensione = " + lista.size());
			}
				
		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);		
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getVistaDocDemat", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}	    
		return lista;
	}

	@Override
	public List<DocumentiIntegrativiDto> getDocumentiIntegrativi(Long idDomanda, Integer idAllegato) throws ServiziFindomWebException {
		List<DocumentiIntegrativiDto> lista = new ArrayList<DocumentiIntegrativiDto>();
		final String logprefix = "[" + ServiziFindomWebDaoImpl.class.getName() + "::" + "getDocumentiIntegrativi" + "] ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_ELENCO_DOCUMENTI_INTEGRATIVI");
			LOGGER.info(logprefix + " Query == " + query);
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			params.addValue("idAllegato", idAllegato, java.sql.Types.INTEGER);
			LOGGER.info(logprefix + " idDomanda=" + idDomanda);

			lista = ((List<DocumentiIntegrativiDto>) template.query(query, params, new DocumentiIntegrativiDtoRowMapper()));

		} catch (Exception e) {

			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getVistaDocDemat", e);

		} finally {
			LOGGER.info(logprefix + " END");
		}

		return lista;
	}

	@Override
	public ShellDomandeDto findoOneShellTDomanda(Long idDomanda) throws ServiziFindomWebException {
		ShellDomandeDto shellDomandeDto = null;
		final String logprefix = "[" + ServiziFindomWebDaoImpl.class.getName() + "::" + "findoOne" + "] ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_SHELL_T_DOMANDA");
			LOGGER.info(logprefix + " Query == " + query);
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			LOGGER.info(logprefix + " idDomanda=" + idDomanda);

			shellDomandeDto = (ShellDomandeDto) template.queryForObject(query, params, new ShellDomandeDtoRowMapper());

		} catch (Exception e) {
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getVistaDocDemat", e);

		} finally {
			LOGGER.info(logprefix + " END");
		}

		return shellDomandeDto;
	}

	@Override
	public FindomTBandiDto findoOneFindomTBandi(Long idBando) throws ServiziFindomWebException {
		FindomTBandiDto findomTBandiDto = null;
		final String logprefix = "[" + ServiziFindomWebDaoImpl.class.getName() + "::" + "findoOne" + "] ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_FINDOM_T_BANDI");
			LOGGER.info(logprefix + " Query == " + query);
			params.addValue("idBando", idBando, java.sql.Types.INTEGER);
			LOGGER.info(logprefix + " idBando=" + idBando);

			findomTBandiDto = (FindomTBandiDto) template.queryForObject(query, params, new FindomTBandiRowMapper());

		} catch (Exception e) {
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getVistaDocDemat", e);

		} finally {
			LOGGER.info(logprefix + " END");
		}

		return findomTBandiDto;
	}

	@Override
	public FindomTBandiDirezioneDto findInfoDirezioneBando(Long idBando) throws ServiziFindomWebException {
		FindomTBandiDirezioneDto findomTBandiDirezioneDto = null;
		final String logprefix = "[" + ServiziFindomWebDaoImpl.class.getName() + "::" + "findInfoDirezioneBando" + "] ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_BANDI_DIREZIONE_SETTORE");
			LOGGER.info(logprefix + " Query == " + query);
			params.addValue("idBando", idBando, java.sql.Types.INTEGER);
			LOGGER.info(logprefix + " idDomanda=" + idBando);

			findomTBandiDirezioneDto = (FindomTBandiDirezioneDto) template.queryForObject(query, params, new FindomTBandiDirezioneRowMapper());

		} catch (Exception e) {
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getVistaDocDemat", e);

		} finally {
			LOGGER.info(logprefix + " END");
		}

		return findomTBandiDirezioneDto;
	}

	@Override
	public FindomDFunzionariDTO findOneFindomDFunzionari(Long idFunzionario) throws ServiziFindomWebException {
		List<FindomDFunzionariDTO> findomDFunzionariDTO = null;
		final String logprefix = "[" + ServiziFindomWebDaoImpl.class.getName() + "::" + "findFindomDFunzionariByCodFiscale" + "] ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_FUNZIONARIO_BY_ID");
			LOGGER.info(logprefix + " Query == " + query);
			params.addValue("idFunzionario", idFunzionario, java.sql.Types.INTEGER);
			LOGGER.info(logprefix + " idFunzionario=" + idFunzionario);

			findomDFunzionariDTO = (List<FindomDFunzionariDTO>) template.query(query, params, new FindomDFunzionariRowMapper());
			if (findomDFunzionariDTO == null || findomDFunzionariDTO.isEmpty())
				return null;

		} catch (Exception e) {
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getVistaDocDemat", e);

		} finally {
			LOGGER.info(logprefix + " END");
		}

		return findomDFunzionariDTO.get(0);
	}

	@Override
	public VistaUltimaDomandaDto getDatiUltimaDomandaBeneficiario(String beneficiario) throws ServiziFindomWebException {
		List<VistaUltimaDomandaDto> vistaUltimaDomandaDto = null;
		final String logprefix = "[" + ServiziFindomWebDaoImpl.class.getName() + "::" + "getDatiUltimaDomandaBeneficiario" + "] ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_ULTIMA_DOMANDA_BY_CF");
			LOGGER.info(logprefix + " Query == " + query);
			params.addValue("beneficiario", beneficiario, java.sql.Types.VARCHAR);
			LOGGER.info(logprefix + " beneficiario=" + beneficiario);

			vistaUltimaDomandaDto = (List<VistaUltimaDomandaDto>) template.query(query, params, new VistaUltimaDomandaDtoRowMapper());
			if (vistaUltimaDomandaDto == null || vistaUltimaDomandaDto.isEmpty())
				return null;

		} catch (Exception e) {
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getDatiUltimaDomandaBeneficiario", e);

		} finally {
			LOGGER.info(logprefix + " END");
		}

		return vistaUltimaDomandaDto.get(0);
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public void chiudiIntegrazione(Long idDomanda, UserInfo userInfo) throws ServiziFindomWebException {
		final String logprefix = "[" + ServiziFindomWebDaoImpl.class.getName() + "::" + "chiudiIntegrazione" + "] ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("CHIUDI_INTEGRAZIONE");
			LOGGER.info(logprefix + " Query == " + query);
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			LOGGER.info(logprefix + " idDomanda=" + idDomanda);
			template.update(query, params);

			insertLogAudit(Constants.CSI_LOG_IDAPPL, // codApplicativo
					"", // ip
					userInfo.getCodFisc() + " - " + userInfo.getCognome() + " " + userInfo.getNome(), // utente
					Constants.CSI_LOG_OPER_CHIUSURA_INTEGRAZIONE, // tipoOperazione
					"chiusura della richiesta integrazione per la domanda numero: " + idDomanda, // descOperazione
					"");// chiave

		} catch (Exception e) {
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("si e' verificata una eccezione durante l'esecuzione del metodo getVistaDocDemat", e);

		} finally {
			LOGGER.info(logprefix + " END");
			LOGGER.info(logprefix + " END");
		}

	}
	
	//PROGETTI COMUNI (capofila/partner) inizio
	@Override
	public CapofilaAcronimoDto getCapofilaAcronimoValidoByIdDomanda(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getCapofilaAcronimoValidoByIdDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);		
		CapofilaAcronimoDto capofilaAcronimoDto = null;		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_CAPOFILA_ACRONIMO_VALIDO_BY_ID_DOMANDA"); 
			LOGGER.info(logprefix + " Query == " + query);			
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);						
			ArrayList<CapofilaAcronimoDto> capofilaAcronimoDtoList = (ArrayList<CapofilaAcronimoDto>)template.query(query, params, new CapofilaAcronimoDtoRowMapper());
			LOGGER.info(logprefix + " trovata una lista di dimensione " + (capofilaAcronimoDtoList == null ? "0" :capofilaAcronimoDtoList.size()));
			if(capofilaAcronimoDtoList!=null && capofilaAcronimoDtoList.size()>0){				
				capofilaAcronimoDto = capofilaAcronimoDtoList.get(0);
			}		
		} catch (EmptyResultDataAccessException e){
			LOGGER.error(logprefix + " - Errore (EmptyResultDataAccessException) occorso durante l'esecuzione del metodo: ", e);
			throw new ServiziFindomWebException("Errore (EmptyResultDataAccessException) occorso durante l'esecuzione del metodo getCapofilaAcronimoValidoByIdDomanda", e);			
		} catch (Exception e) {			
			LOGGER.error(logprefix + " - Errore (Exception) occorso durante l'esecuzione del metodo: ", e);
			throw new ServiziFindomWebException("Errore (Exception) occorso durante l'esecuzione del metodo getCapofilaAcronimoValidoByIdDomanda", e);			
		} finally {
			LOGGER.info(logprefix + " END");
		}		
		return capofilaAcronimoDto;
	}
	@Override
	public	CapofilaAcronimoPartnerDto getCapofilaAcronimoPartnerValidoByIdDomanda(Integer idDomanda) throws ServiziFindomWebException{
		final String methodName = "getCapofilaAcronimoPartnerValidoByIdDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);		
		CapofilaAcronimoPartnerDto capofilaAcronimoPartnerDto = null;		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_CAPOFILA_ACRONIMO_PARTNER_VALIDO_BY_ID_DOMANDA"); 
			LOGGER.info(logprefix + " Query == " + query);			
			params.addValue("idDomandaPartner", idDomanda, java.sql.Types.INTEGER);						
			ArrayList<CapofilaAcronimoPartnerDto> capofilaAcronimoPartnerDtoList = (ArrayList<CapofilaAcronimoPartnerDto>)template.query(
					query, params, new CapofilaAcronimoPartnerDtoRowMapper());
			LOGGER.info(logprefix + " trovata una lista di dimensione " + (capofilaAcronimoPartnerDtoList == null ? "0" :capofilaAcronimoPartnerDtoList.size()));
			if(capofilaAcronimoPartnerDtoList!=null && capofilaAcronimoPartnerDtoList.size()>0){				
				capofilaAcronimoPartnerDto = capofilaAcronimoPartnerDtoList.get(0);
			}		
		} catch (EmptyResultDataAccessException e){
			LOGGER.error(logprefix + " - Errore (EmptyResultDataAccessException) occorso durante l'esecuzione del metodo: ", e);
			throw new ServiziFindomWebException("Errore (EmptyResultDataAccessException) occorso durante l'esecuzione del metodo getCapofilaAcronimoValidoByIdDomanda", e);
			
		} catch (Exception e) {			
			LOGGER.error(logprefix + " - Errore (Exception) occorso durante l'esecuzione del metodo: ", e);
			throw new ServiziFindomWebException("Errore (Exception) occorso durante l'esecuzione del metodo getCapofilaAcronimoValidoByIdDomanda", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}		
		return capofilaAcronimoPartnerDto;
	}
	
	@Override	
	public Integer disattivaCapofilaAcronimo(String idCapofilaAcronimo) throws ServiziFindomWebException {
		final String methodName = "updateShellDomande";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idCapofilaAcronimo="+idCapofilaAcronimo);
		
		int ris = 0;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_DISATTIVA_CAPOFILA_ACRONIMO");		
			LOGGER.info(logprefix + " Query == " + query);			
			int idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);			
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt , java.sql.Types.INTEGER);			
			ris = template.update(query, params);
			
		} catch (Exception e) {			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new ServiziFindomWebException("Errore occorso durante l'esecuzione del metodo ", e);			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}	
	
	@Override	
	public Integer disattivaAcronimoBando(String idAcronimoBando) throws ServiziFindomWebException {
		final String methodName = "disattivaAcronimoBando";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idAcronimoBando="+idAcronimoBando);		
		int ris = 0;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_DISATTIVA_ACRONIMO_BANDO"); 		
			LOGGER.info(logprefix + " Query == " + query);			
			int idAcronimoBandoInt = Integer.parseInt(idAcronimoBando);			
			params.addValue("idAcronimoBando", idAcronimoBandoInt, java.sql.Types.INTEGER);
			ris = template.update(query, params);			
		} catch (Exception e) {			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new ServiziFindomWebException("Errore occorso durante l'esecuzione del metodo ", e);			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}
	
	@Override
	public	ArrayList<CapofilaAcronimoPartnerDto> getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(String idCapofilaAcronimo) throws ServiziFindomWebException{		                                          
		final String methodName = "getCapofilaAcronimoPartnerListByIdCapofilaAcronimo";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idCapofilaAcronimo="+idCapofilaAcronimo);		
		ArrayList<CapofilaAcronimoPartnerDto> capDtoList = new ArrayList<>();		
		MapSqlParameterSource params = new MapSqlParameterSource();		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_CAPOFILA_ACRONIMO_PARTNER_BY_ID_CAPOFILA_ACRONIMO");
			LOGGER.info(logprefix + " Query == " + query);			
			int idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);		
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt , java.sql.Types.INTEGER);			
			LOGGER.info(logprefix + " Query == " + query);			
			capDtoList = (ArrayList<CapofilaAcronimoPartnerDto>) template.query(query, params, new CapofilaAcronimoPartnerDtoRowMapper());		
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getShellDomandeByCreatoreBeneficiario", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}			
		return capDtoList;
	}
	@Override	
	public Integer disattivaCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner) throws ServiziFindomWebException {
		final String methodName = "disattivaAcronimoBando";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idCapofilaAcronimo="+idCapofilaAcronimo + ", idPartner=" + idPartner);		
		int ris = 0;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_DISATTIVA_CAPOFILA_ACRONIMO_PARTNER"); 		
			LOGGER.info(logprefix + " Query == " + query);			
			int idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);			
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);			
			int idPartnerInt = Integer.parseInt(idPartner);			
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);			
			ris = template.update(query, params);			
		} catch (Exception e) {			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new ServiziFindomWebException("Errore occorso durante l'esecuzione del metodo ", e);			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}
	
	@Override	
	public Integer updateNullDomandaInCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner) throws ServiziFindomWebException {
		final String methodName = "updateNullDomandaInCapofilaAcronimoPartner";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idCapofilaAcronimo="+idCapofilaAcronimo + ", idPartner=" + idPartner);		
		int ris = 0;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_DOMANDA_NULL_CAPOFILA_ACRONIMO_PARTNER"); 		
			LOGGER.info(logprefix + " Query == " + query);			
			int idCapofilaAcronimoInt = Integer.parseInt(idCapofilaAcronimo);			
			params.addValue("idCapofilaAcronimo", idCapofilaAcronimoInt, java.sql.Types.INTEGER);			
			int idPartnerInt = Integer.parseInt(idPartner);			
			params.addValue("idPartner", idPartnerInt, java.sql.Types.INTEGER);			
			ris = template.update(query, params);			
		} catch (Exception e) {			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new ServiziFindomWebException("Errore occorso durante l'esecuzione del metodo ", e);			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}
	
	@Override	
	public Integer updateStatoDomanda(String idDomanda, String statoDomanda) throws ServiziFindomWebException {
		final String methodName = "updateStatoDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda + ", statoDomanda=" + statoDomanda);		
		int ris = 0;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("UPDATE_STATO_DOMANDA"); 		
			LOGGER.info(logprefix + " Query == " + query);
			params.addValue("statoDomanda", statoDomanda, java.sql.Types.VARCHAR);			
			int idDomandaInt = Integer.parseInt(idDomanda);			
			params.addValue("idDomanda", idDomandaInt, java.sql.Types.INTEGER);							
			ris = template.update(query, params);			
		} catch (Exception e) {			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo: ", e);
			throw new ServiziFindomWebException("Errore occorso durante l'esecuzione del metodo ", e);			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}
	
	@Override	
	@Transactional(rollbackFor={ServiziFindomWebException.class})
	public void invalidaDomandaCapofila(String idCapofilaAcronimo, String idAcronimoBando, Integer idDomandaCapofila,
			String codApplicativo, String utente, String tipoOperazione, String descrOperazione) throws ServiziFindomWebException {
		
		final String methodName = "invalidaDomandaCapofila";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idCapofilaAcronimo = "+idCapofilaAcronimo + ", idAcronimoBando = " + idAcronimoBando + ", idDomandaCapofila = " + idDomandaCapofila);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		int ris1 = 0;
		boolean ris2;
		try {	
			//disattivo il record su shell_t_capofila_acronimo 
			disattivaCapofilaAcronimo(idCapofilaAcronimo);
			LOGGER.info(logprefix + " disattivato record su shell_t_capofila_acronimo ");
			//disattivo il record su shell_t_acronimo_bandi
			disattivaAcronimoBando(idAcronimoBando);
			LOGGER.info(logprefix + " disattivato record su shell_t_capofila_acronimo ");
			
			//accedo a shell_r_capofila_acronimo_partner e disattivo ciascun record; le eventuali domande dei partner vengono messe in stato NV su aggr_t_model 
			ArrayList<CapofilaAcronimoPartnerDto> capDtoList = getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(idCapofilaAcronimo);
			LOGGER.info(logprefix + " ho recuperato da shell_r_capofila_acronimo_partner una lista di " + (capDtoList == null ? "0" : capDtoList.size()) + " elementi " );
			
			if(capDtoList!=null && !capDtoList.isEmpty()){
				for (CapofilaAcronimoPartnerDto curCapDto : capDtoList) {
					if(curCapDto==null || StringUtils.isBlank(curCapDto.getIdPartner())){
						continue;
					}
					String idPartner = curCapDto.getIdPartner();
					String idDomandaPartner = Objects.toString(curCapDto.getIdDomandaPartner(), "");
					disattivaCapofilaAcronimoPartner(idCapofilaAcronimo, idPartner);
					LOGGER.info(logprefix + " disattivato record su shell_r_capofila_acronimo_partner relativo a idPartner " + idPartner);
					
					if(StringUtils.isNotBlank(idDomandaPartner) && !idDomandaPartner.equals("0")){
						LOGGER.info(logprefix + " il partner corrente su shell_r_capofila_acronimo_partner ha una domanda " + idDomandaPartner);
						updateStatoDomanda(idDomandaPartner, Constants.STATO_INVALIDATA);
						LOGGER.info(logprefix + " invalidato la domanda " + idDomandaPartner + " del partner "+idPartner+ " su aggr_t_model ");
					}
				}
			}
			LOGGER.info(logprefix + " terminato l'aggiornamento di shell_r_capofila_acronimo_partner e delle eventuali domande riferite in quella tabella ");			
			
			updateStatoDomanda(idDomandaCapofila+"", Constants.STATO_INVALIDATA);
			LOGGER.info(logprefix + " invalidato la domanda " + idDomandaCapofila + "del capofila su aggr_t_model ");
			
			insertLogAudit( codApplicativo, "", utente , tipoOperazione , descrOperazione, "");
			LOGGER.info(logprefix + " tracciato l'operazione su log audit ");
				
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo ", e);
			throw new ServiziFindomWebException("Errore occorso durante l'esecuzione del metodo ", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}	
	}
	
	@Override
	@Transactional(rollbackFor={ServiziFindomWebException.class})
	public void invalidaDomandaPartner(String idCapofilaAcronimo,String idPartner, Integer idDomandaPartner,
			String codApplicativo, String utente, String tipoOperazione, String descrOperazione ) throws ServiziFindomWebException {
		final String methodName = "invalidaDomandaPartner";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idCapofilaAcronimo = "+idCapofilaAcronimo + ", idPartner = " + idPartner);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		int ris1 = 0;
		boolean ris2;
		try {	
			//metto a null l'id domanda partner su shell_r_capofila_acronimo_partner 
			updateNullDomandaInCapofilaAcronimoPartner(idCapofilaAcronimo, idPartner);
			LOGGER.info(logprefix + " disattivato record su shell_r_capofila_acronimo_partner");
			
			updateStatoDomanda(idDomandaPartner+"", Constants.STATO_INVALIDATA);
			LOGGER.info(logprefix + " invalidato la domanda " + idDomandaPartner + "del partner su aggr_t_model ");
			
			insertLogAudit( codApplicativo, "", utente , tipoOperazione , descrOperazione, "");
			LOGGER.info(logprefix + " tracciato l'operazione su log audit ");
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo ", e);
			throw new ServiziFindomWebException("Errore occorso durante l'esecuzione del metodo ", e);
		} finally {
			LOGGER.info(logprefix + " - END");
		}	
		
	}
	//PROGETTI COMUNI (capofila/partner) fine
	
	//gestione beneficiario estero
	public String getIdStatoOperatorePresentatore(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getIdStatoOperatorePresentatore";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");

		MapSqlParameterSource params = new MapSqlParameterSource();
		String valore = "";
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("QUERY_ID_STATO_OPERATORE_PRESENTATORE");
			LOGGER.info(logprefix + " Query == " + query);

			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			valore = (String) template.queryForObject(query, params, String.class);			
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(logprefix + " EmptyResultDataAccessException -  Errore occorso durante l'esecuzione del metodo:", e);			
			throw new ServiziFindomWebException( "EmptyResultDataAccessException -  Errore occorso durante l'esecuzione del metodo getIdStatoOperatorePresentatore ", e);
        
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:", e);			
			throw new ServiziFindomWebException("Exception - Errore occorso durante l'esecuzione del metodo getIdStatoOperatorePresentatore", e);
		} 

		LOGGER.info(logprefix + " - END");
		return (valore == null ? "" : valore);
	}
	
	public String getStatoDomanda(String idDomanda) throws ServiziFindomWebException {
		final String methodName = "getStatoDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
        if(StringUtils.isBlank(idDomanda)){
        	return "";
        }
        	
		MapSqlParameterSource params = new MapSqlParameterSource();
		String stato = "";
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_STATO_DOMANDA");
			LOGGER.info(logprefix + " Query == " + query);
            Integer idDomandaInt = Integer.parseInt(idDomanda);
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			stato = (String) template.queryForObject(query, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(logprefix + "Errore occorso durante l'esecuzione del metodo:", e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Si e' verificata una EmptyResultDataAccessException durante l'esecuzione di getStatoDomanda", e);
        
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" , e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Si e' verificata una Exception durante l'esecuzione di getStatoDomanda", e);
		} 

		LOGGER.info(logprefix + " - END");
		return stato;
	}

	
	@Override
	public String callProcedureCreaSchedaProgetto(Integer idDomanda)
			throws ServiziFindomWebException {
		final String methodName = "callProcedureCreaSchedaProgetto";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		
		String res = null;
		
		try {
			
//			MapSqlParameterSource params = new MapSqlParameterSource();
//			res = template.queryForObject("SELECT fnc_crea_scheda_progetto ("+idDomanda+")", params, String.class);
//			LOGGER.info(logprefix + " res="+res);
			
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(ds);
			jdbcCall.withoutProcedureColumnMetaDataAccess();
		    jdbcCall.withFunctionName("fnc_crea_scheda_progetto");
		    SqlParameter parameterIN = new SqlParameter("p_id_domanda",java.sql.Types.INTEGER);
		    SqlOutParameter parameterOut = new SqlOutParameter("res", java.sql.Types.VARCHAR);
			jdbcCall.declareParameters(parameterIN, parameterOut);
			
		    SqlParameterSource in = new MapSqlParameterSource().addValue("p_id_domanda", idDomanda);
		    res = jdbcCall.executeFunction(String.class, in);

		    LOGGER.info(logprefix + " res="+res);
			
		} catch (Exception e) {
			LOGGER.warn(logprefix + " eccezione Exception");
//			e.printStackTrace();
			throw new ServiziFindomWebException("Si e' verificata una Exception durante l'esecuzione di callProcedureCreaSchedaProgetto", e);
	    } finally{
	    	LOGGER.info(logprefix + " END");
	    }
		return res;
	}
	
	
	
	// GETTERS && SETTERS
	
	public void setTemplate(NamedParameterJdbcTemplate template) {
		this.template = template;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}


	@Override
	public String getStereotipoDomanda(Integer idDomanda)
			throws ServiziFindomWebException {
		final String methodName = "getStereotipoDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
        if(idDomanda == null){
        	return "";
        }

		MapSqlParameterSource params = new MapSqlParameterSource();
		String stereotipo = "";
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_STEREOTIPO_DOMANDA");
			LOGGER.info(logprefix + " Query == " + query);
            
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			stereotipo = (String) template.queryForObject(query, params, String.class);
			
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(logprefix + "Errore occorso durante l'esecuzione del metodo:", e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Si e' verificata una EmptyResultDataAccessException durante l'esecuzione di getStereotipoDomanda", e);
        
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" , e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Si e' verificata una Exception durante l'esecuzione di getStereotipoDomanda", e);
		} 

		LOGGER.info(logprefix + " - END");
		return stereotipo;
	}


	@Override
	public String getFlagProgettiComuniDomanda(Integer idDomanda)
			throws ServiziFindomWebException {
		final String methodName = "getFlagProgettiComuniDomanda";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
        if(idDomanda == null){
        	return "";
        }

		MapSqlParameterSource params = new MapSqlParameterSource();
		String flg = "";
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_FLAGPROGETTICOMUNI_DOMANDA");
			LOGGER.info(logprefix + " Query == " + query);
            
			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			flg = (String) template.queryForObject(query, params, String.class);
			
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(logprefix + "Errore occorso durante l'esecuzione del metodo:", e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Si e' verificata una EmptyResultDataAccessException durante l'esecuzione di getFlagProgettiComuniDomanda", e);
        
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:" , e);
			LOGGER.info(logprefix + " - END");
			throw new ServiziFindomWebException("Si e' verificata una Exception durante l'esecuzione di getFlagProgettiComuniDomanda", e);
		} 

		LOGGER.info(logprefix + " - END");
		return flg;
	}


	@Override
	public int insertShellTDocumentoIndexDB(Integer idDomanda,
			String uuidIndex, String repository, String upFileFileName,
			String messageDigest, String dtVerificaFirma,
			Integer idStatoDocumentoIndex, Integer idTipologiaNewDoc,
			int idSoggetto, byte[] bFileUpload) throws ServiziFindomWebException {
		
		final String methodName = "insertShellTDocumentoIndexDB";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idDomanda="+idDomanda);
		LOGGER.info(logprefix + " uuidIndex="+uuidIndex);
		LOGGER.info(logprefix + " repository="+repository);
		LOGGER.info(logprefix + " upFileFileName="+upFileFileName);
		LOGGER.info(logprefix + " messageDigest="+messageDigest);
		LOGGER.info(logprefix + " dtVerificaFirma="+dtVerificaFirma);
		LOGGER.info(logprefix + " idStatoDocumentoIndex="+idStatoDocumentoIndex);
		LOGGER.info(logprefix + " idTipologiaNewDoc="+idTipologiaNewDoc);
		LOGGER.info(logprefix + " idSoggetto="+idSoggetto);
		
		int ris = 0;
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("INSERT_DOCUMENTO_INDEX_DB");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idDomanda", idDomanda , java.sql.Types.INTEGER);
			params.addValue("uuidIndex", uuidIndex, java.sql.Types.VARCHAR);
			params.addValue("repositoryValue", repository , java.sql.Types.VARCHAR);
			params.addValue("nomeFile", upFileFileName, java.sql.Types.VARCHAR);
			params.addValue("messageDigest", messageDigest, java.sql.Types.VARCHAR);			
			params.addValue("dtVerificaFirma", DateUtil.getDataDaStringa(dtVerificaFirma), java.sql.Types.TIMESTAMP);
			params.addValue("idStatoDocumentoIndex", idStatoDocumentoIndex, java.sql.Types.INTEGER);
			params.addValue("idAllegato", idTipologiaNewDoc, java.sql.Types.INTEGER);
			params.addValue("idSoggetto", idSoggetto, java.sql.Types.INTEGER);
			params.addValue("file", bFileUpload, java.sql.Types.BINARY);

			ris = template.queryForObject(query, params, Integer.class);
			
			LOGGER.info(logprefix + " inserita ["+ris+"] entry");
			
		} catch (Exception e) {
			LOGGER.error(logprefix + " - Errore insertShellTDocumentoIndex su idDomanda="+idDomanda+" ,uuidIndex="+uuidIndex+" ,nomeFile="+upFileFileName); 
			LOGGER.error(logprefix + " - Exception : Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query insertShellTDocumentoIndex", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		return ris;
	}


	@Override
	public ShellDocumentoIndexDto getAllegatoFromShellTDocumentoIndex(Integer idAllegato) throws ServiziFindomWebException {
		final String methodName = "getAllegatoFromShellTDocumentoIndex";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");
		LOGGER.info(logprefix + " idAllegato="+idAllegato);
		
		ShellDocumentoIndexDto doc = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "";
		
		try {
			query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("SELECT_DOCUMENTO_INDEX_DB");
			LOGGER.info(logprefix + " Query == " + query);
			
			params.addValue("idAllegato", idAllegato, java.sql.Types.INTEGER);
			
			doc = (ShellDocumentoIndexDto) template.queryForObject(query, params, new ShellDocumentoIndexDtoRowMapper());
		
		} catch (EmptyResultDataAccessException e){
			LOGGER.warn(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			LOGGER.info(logprefix + " continuo ");

		} catch (Exception e) {
			
			LOGGER.error(logprefix + " - Errore occorso durante l'esecuzione del metodo:" + e, e);
			throw new ServiziFindomWebException("Exception while execute query getAllegatoFromShellTDocumentoIndex", e);
			
		} finally {
			LOGGER.info(logprefix + " END");
		}
		
		return doc;
	}
	
	public String getDenominazioneOperatorePresentatore(Integer idDomanda) throws ServiziFindomWebException {
		final String methodName = "getDenominazioneOperatorePresentatore";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		LOGGER.info(logprefix + " BEGIN");

		MapSqlParameterSource params = new MapSqlParameterSource();
		String valore = "";
		
		try {
			String query = ServiziFindomWebDaoUtil.getSelectStatements().getProperty("QUERY_DENOMINAZIONE_OPERATORE_PRESENTATORE");
			LOGGER.info(logprefix + " Query == " + query);

			params.addValue("idDomanda", idDomanda, java.sql.Types.INTEGER);
			valore = (String) template.queryForObject(query, params, String.class);			
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(logprefix + " EmptyResultDataAccessException -  Errore occorso durante l'esecuzione del metodo:", e);			
			throw new ServiziFindomWebException( "EmptyResultDataAccessException -  Errore occorso durante l'esecuzione del metodo getIdStatoOperatorePresentatore ", e);
        
		} catch (Exception e) {
			LOGGER.error(logprefix + " Errore occorso durante l'esecuzione del metodo:", e);			
			throw new ServiziFindomWebException("Exception - Errore occorso durante l'esecuzione del metodo getIdStatoOperatorePresentatore", e);
		} 

		LOGGER.info(logprefix + " - END");
		return (valore == null ? "" : valore);
	}
	
}
