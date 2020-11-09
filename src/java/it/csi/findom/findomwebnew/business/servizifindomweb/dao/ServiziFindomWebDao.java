/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.dao;

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
import it.csi.findom.findomwebnew.presentation.vo.UserInfo;
import it.csi.melograno.aggregatore.business.Aggregatore;
import it.csi.melograno.aggregatore.exception.AggregatoreException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ServiziFindomWebDao {

	public ArrayList<StatoDomandaDto> getStatiDomanda() throws ServiziFindomWebException;

	
	public int insertLogAudit(String codApplicativo, String ip, String utente ,String tipoOperazione ,String descrOperazione, String chiaveOperazione) 
				throws ServiziFindomWebException;
	
	public int insertLogFirma(String utente, Integer idDomanda, String metodo, String msg) 
			throws ServiziFindomWebException;
	/**
	 * Estraggo un oggetto ShellSoggettiDto dato un cod_fiscale (tabella shell_t_soggetti)
	 * @param utente
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ShellSoggettiDto getDatiSoggettoByCodiceFiscale(String utente) throws ServiziFindomWebException;
	
	/**
	 * Estraggo dalla vista i dati relativi ai due id passati
	 * Se idSoggettoCreatore == null allora estraggo tutte i dati relativa all'impresa con idSoggBeneficiario
	 * 
	 * @param idSoggettoCreatore
	 * @param idSoggettoBeneficiario
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<VistaDomandeDto> getVistaDomandaByCreatoreBeneficiario(Integer idSoggettoCreatore, Integer idSoggettoBeneficiario)
			throws ServiziFindomWebException;
	

	/**
	 * Estraggo dalla vista i dati relativi ai tre id passati
	 * Se idSoggettoCreatore == null allora estraggo tutte i dati relativa all'impresa con idSoggBeneficiario
	 * 
	 * @param idSoggettoCreatore
	 * @param idSoggettoBeneficiario
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<VistaDomandeDto> getVistaDomandaByCreatoreBeneficiarioDomanda(Integer idSoggettoCreatore, Integer idSoggettoBeneficiario,Integer idDomanda)
		throws ServiziFindomWebException;
		
	/**
	 * Estraggo una lista di ShellSoggettiDto data una lista di id_soggetto (tabella shell_t_soggetti)
	 * @param listaId : lista id da estrarre
	 * @return lista di ShellSoggettiDto
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<ShellSoggettiDto> getDatiSoggettoByIdSoggetto(ArrayList<String> listaId) throws ServiziFindomWebException;

	/**
	 * Recupero le informazioni dalla tabella findom_t_amministratori per il cf dato
	 * 
	 * @param codFisc
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public AmministratoriDto getAmministratoreByCodiceFiscale(String codFisc) throws ServiziFindomWebException;

	/**
	 * Estraggo le domande inserite secondo i parametri di ingresso
	 * 
	 * @param idSoggBeneficiario :id_soggetto Impresa/ente per cui si sta operando
	 * @param idSoggCreatore : id_soggetto che ha creato la domanda == soggetto collegato
	 * @param ruolo
	 * @param normativa
	 * @param descBreveBando
	 * @param bando
	 * @param sportello
	 * @param statoDomanda
	 * @param numDomanda
	 * @return
	 */
	public ArrayList<VistaDomandeDto> getDomandeInserite(Integer idSoggBeneficiario,
			Integer idSoggCreatore, String ruolo, String normativa,
			String descBreveBando, String bando, String sportello,
			String statoDomanda, String numDomanda) throws ServiziFindomWebException;

	/**
	 * Inserisco una entry nella tabella shell_t_soggetti
	 * @param newSoggetto
	 */
	public void insertShellTSoggetto(ShellSoggettiDto newSoggetto) throws ServiziFindomWebException;

	/**
	 * Aggiorno una entry nella tabella shell_t_soggetti
	 * @param soggetto
	 * @throws ServiziFindomWebException
	 */
	public void updateShellTSoggetto(ShellSoggettiDto soggetto)  throws ServiziFindomWebException;
	
	/**
	 * Estraggo i dati dalla vista findom_v_sportelli_attivi
	 * @return 
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<VistaSportelliAttiviDto> getVistaSportelliAttivi() throws ServiziFindomWebException;

	/**
	 * Estraggo i dati da findom_t_sportelli
	 * @return 
	 * @throws ServiziFindomWebException
	 */
	public ProssimoSportelloAttivoDto getProssimoSportelloAttivo() throws ServiziFindomWebException;

	/**
	 * Estraggo i dati dalla tabella findom_d_tipol_beneficiari
	 * @return
	 */
	public ArrayList<TipolBeneficiariDto> getListaTipolBeneficiari() throws ServiziFindomWebException;

	/**
	 *  Estraggo i dati dalla vista findom_v_sportelli_attivi filtrati per id_normativa
	 * @param idNormativa
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<VistaSportelliAttiviDto> getVistaSportelliAttiviByNormativa(int idNormativa) throws ServiziFindomWebException;

	/**
	 *  Estraggo i dati dalla vista findom_v_sportelli_attivi filtrati per id_bando
	 *  
	 * @param idBando
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<VistaSportelliAttiviDto> getVistaSportelliAttiviByIdBando(int idBando) throws ServiziFindomWebException;

	/**
	 * 
	 * @param idBando
	 * @return
	 */
	public ArrayList<TipolBeneficiariDto> getListaTipolBeneficiariByIdBando(int idBando) throws ServiziFindomWebException;

	/**
	 * 
	 * @param idBando
	 * @param idSoggettoBeneficiario
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<VistaDomandeBeneficiariDto> getVistaDomandeBeneficiari(int idBando, int idSoggettoBeneficiario)  throws ServiziFindomWebException;

	/**
	 * Estraggo i principali dati dalla AGGR_T_TEMPLATE in join con la FINDOM_T_BANDI 
	 * @param idBando
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<AggrTTemplateDto> getAggrTemplateWithFindomBandi(int idBando) throws ServiziFindomWebException;

	/**
	 * Estraggo i dati dalla AGGR_T_TMODEL, dati 3 parametri
	 * @param userId
	 * @param templateCode
	 * @param modelProg
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<AggrTModelDto> getAggrTModelByUserTemplateProg(String userId, String templateCode, Integer modelProg) throws ServiziFindomWebException;

	/**
	 * Inserisco una entry nella tabella shell_t_domande
	 * @param idSoggettoCollegato
	 * @param idSoggettoBeneficiario
	 * @param numSportello
	 * @param idSoggettoInvio
	 * @param dataInvioDomanda
	 * @param idModel
	 * @param idTipologiaBeneficiario
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public int insertShellTDomande(Integer idSoggettoCollegato,	Integer idSoggettoBeneficiario, Integer numSportello,
			Integer idSoggettoInvio, String dataInvioDomanda, Integer idModel,	Integer idTipologiaBeneficiario)  throws ServiziFindomWebException;

	/**
	 * Estraggo i dati dalle tabelle AGGR_T_TEMPLATE, AGGR_T_MODEL, data una domanda
	 * 
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<AggrDataDto> getAggrDataByIdDomanda(Integer idDomanda) throws ServiziFindomWebException;

	
	/**
	 * 
	 * @param idDomInt
	 * @param userId
	 * @param templateId
	 * @param modelProgr
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer deleteDomanda(Integer idDomInt, String userId, String templateCode, Integer modelProgr,  Aggregatore aggregatore) 
			throws ServiziFindomWebException, AggregatoreException;

	/**
	 * 
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public String getXMLDomanda(Integer idDomanda) throws ServiziFindomWebException;

	/**
	 * 
	 * @param idDomanda
	 * @param xmlDomanda
	 * @param byteArrayPdf
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer insertTFileDomanda(Integer idDomanda, String xmlDomanda, byte[] byteArrayPdf, String messageDigest) throws ServiziFindomWebException;

	/**
	 * 
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public NumberMaxDto getNumeroMassimoDomandeInviate(Integer idDomanda) throws ServiziFindomWebException;

	/**
	 * 
	 * @param idDomanda
	 * @param idSoggettoCollegato
	 * @param dataTrasmissioneUfficiale 
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer updateShellDomande(Integer idDomanda, Integer idSoggettoCollegato, String statoDomanda,Integer idStatoIstruttoria, Date dataTrasmissioneUfficiale)throws ServiziFindomWebException;

	/**
	 * 
	 * @param idDomanda
	 * @param idSoggettoCollegato
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer updateShellDomandeBandoDemat(Integer idDomanda, Integer idSoggettoCollegato)throws ServiziFindomWebException;

	
	/**
	 * 
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public byte[] getPdfDomanda(Integer idDomanda) throws ServiziFindomWebException;

	
	/**
	 * 
	 * @param idDocumento
	 * @param codFisc
	 * @param numProposta
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ShellDocumentoIndexDto getDocumentoIndex(String idDocumento, String codFisc, Integer numProposta) throws ServiziFindomWebException;
	
	/**
	 * 
	 * @param idDocumento
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ShellDocumentoIndexDto getDocumentoIndex(String idDocumento, Integer idDomanda) throws ServiziFindomWebException;

	/**
	 * 
	 * @param idDomanda
	 * @param uuidIndex
	 * @param repository
	 * @param upFileFileName
	 * @param messageDigest
	 * @param dtVerificaFirma
	 * @param dtMarcaTemporale
	 * @param idStatoDocumentoIndex
	 * @param idTipologiaNewDoc
	 * @param idSoggetto
	 * @return
	 */
	public int insertShellTDocumentoIndex(Integer idDomanda,
			String uuidIndex, String repository, String upFileFileName,
			String messageDigest, String dtVerificaFirma,
			Integer idStatoDocumentoIndex,
			Integer idTipologiaNewDoc, Integer idSoggetto)  throws ServiziFindomWebException;

	/**
	 * 
	 * @param uuid
	 * @return
	 */
	public ShellDocumentoIndexDto getDocumentoIndex(String uuid) throws ServiziFindomWebException;

	/**
	 * 
	 * @param idBando 
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<FindomAllegatiDto> getAllegati(Integer idBando, Integer idDomanada, Integer idSportello) throws ServiziFindomWebException;

	/**
	 * 
	 * @param idDoc id docuemnto da eliminare
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer deleteDocumentoIndex(String idDoc)  throws ServiziFindomWebException;

	/**
	 * 
	 * @param idDomanda
	 * @param nomeFile
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ShellDocumentoIndexDto getDocumentoIndex(Integer idDomanda, String nomeFile)  throws ServiziFindomWebException;

	
	/**
	 * 
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ShellDocumentoIndexDto getDocumentoFirmato(Integer idDomanda) throws ServiziFindomWebException;
	
	/**
	 * Restituisce l'ultimo oggetto inserito nella tabella shell_t_documento_index per la domanda data
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ShellDocumentoIndexDto getLastDocumentoInserito(Integer idDomanda) throws ServiziFindomWebException;
	
	/**
	 * Restituisce una stringa contenente i tidpi doc ammessi per l'upload dei files
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<String> getTipoDocumentoUpload(String parametro) throws ServiziFindomWebException;
	
	/**
	 * 
	 * @param codice
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public String getValoreParametro(String codice) throws ServiziFindomWebException;
	
	/**
	 * Restituisce il message digest del file della domanda
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public String getMessageDigestDomanda(Integer idDomanda) throws ServiziFindomWebException;
	
	
	/**
	 * 
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public String getValueFromXML(String nodo, String campo, Integer idDomanda) throws ServiziFindomWebException;
	
	/**
	 * 	
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<ShellDocumentoIndexDto> getAllDocumentoIndex(Integer idDomanda) throws ServiziFindomWebException;
	
	
	/**
	 * restituisce le regole associate sul db all'idBando passato nel parametro
	 * @param idModel
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<Integer> getRegoleTemplate(int idBando) throws ServiziFindomWebException;
	
	/**
	 * restituisce i dati della regola avente l'id passato nel parametro
	 * @param idRegola
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public RegolaDto getRegola(int idRegola) throws ServiziFindomWebException;
	
	/**
	 * ritorna i parametri e relativi valori associati alla regola avente il codRegola passato per il bando avente il templateId passato
	 * @param codRegola
	 * @param templateId
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<ParametroRegolaBandoDto> getParametriRegola(String codRegola, int templateId) throws ServiziFindomWebException;
	
	
	public RegoleVerificaFirmaDto getRegoleVerificaFirma(Integer idBando) throws ServiziFindomWebException;

	/**
	 * copia il record della aggr_t_model sulla istr_t_model
	 * e i recorda dalla aggr_t_model_index sulla istr_t_model_index
	 * @param idDomanda
	 * @throws ServiziFindomWebException
	 */
	public void copiaDomandaPerIstruttoria(Integer idDomanda) throws ServiziFindomWebException;
	
	/**
	 * legge il valore dell'attributo flag_istruttoria_esterna su findom_t_bandi per il bando avente l'id passato nel parametro
	 * @param idBando
	 * @return il valore del flag (stringa vuota se non valorizzato sul DB)
	 * @throws ServiziFindomWebException
	 */
	public String getFlagIstruttoriaEsterna(Integer idBando) throws ServiziFindomWebException;
	
	/**
	 * 
	 * @param codice
	 * @return il valore dell'id_stato istruttoria corrispondente al codice passato nel parametro, eventualmente null
	 * @throws ServiziFindomWebException
	 */
	public Integer getIdStatoIstruttoriaByCodice(String codice) throws ServiziFindomWebException;
	
	
	public VistaUltimaDomandaDto getDatiUltimaDomandaBeneficiario(String beneficiario) throws ServiziFindomWebException;

	/**
	 * 
	 * @param idDomanda
	 * @return alcuni dati esposti dalla vista FINDOM_V_DOC_DEMAT_BATCH
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<VistaDocDematDto> getVistaDocDemat(Integer idDomanda)throws ServiziFindomWebException;

	public String getIstanzaRoutingBySportello(Integer idSportello) throws ServiziFindomWebException;

	public String getFlagBandoDematerializzatoByIdDomanda(Integer idDomanda) throws ServiziFindomWebException;
		
	public List<DocumentiIntegrativiDto> getDocumentiIntegrativi(Long idDomanda, Integer idAllegato) throws ServiziFindomWebException;

	public ShellDomandeDto findoOneShellTDomanda(Long idDomanda) throws ServiziFindomWebException;

	FindomTBandiDto findoOneFindomTBandi(Long idBando) throws ServiziFindomWebException;

	public FindomTBandiDirezioneDto findInfoDirezioneBando(Long idBando) throws ServiziFindomWebException;

	public FindomDFunzionariDTO findOneFindomDFunzionari(Long idFunzionario) throws ServiziFindomWebException;

	void chiudiIntegrazione(Long idDomanda, UserInfo userInfo) throws ServiziFindomWebException;

	/**
	 * ritorna i dati eventualmente presenti su shell_t_capofila_acronimo non disattivati e relativi alla domanda passata nel parametro
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public CapofilaAcronimoDto getCapofilaAcronimoValidoByIdDomanda(Integer idDomanda) throws ServiziFindomWebException;
	
	/**
	 *  ritorna i dati eventualmente presenti su shell_r_capofila_acronimo_partner non disattivati e relativi alla domanda passata nel parametro
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public CapofilaAcronimoPartnerDto getCapofilaAcronimoPartnerValidoByIdDomanda(Integer idDomanda) throws ServiziFindomWebException;
	
	/**
	 * imposta la data di disattivazione al record individuato dal parametro
	 * @param idCapofilaAcronimo
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer disattivaCapofilaAcronimo(String idCapofilaAcronimo) throws ServiziFindomWebException;
	
	/**
	 * imposta la data di disattivazione al record individuato dal parametro
	 * @param idAcronimoBando
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer disattivaAcronimoBando(String idAcronimoBando) throws ServiziFindomWebException;
	
	/**
	 * ritorna la lista di relazioni capofila/partner non ancora disattivate individuate dal parametro
	 * @param idCapofilaAcronimo
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public ArrayList<CapofilaAcronimoPartnerDto> getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(String idCapofilaAcronimo) throws ServiziFindomWebException;
	
	/**
	 * imposta la data di disattivazione al record individuato dai parametri nella tavola di relazione tra capofila e partner
	 * @param idCapofilaAcronimo
	 * @param idPartner
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer disattivaCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner) throws ServiziFindomWebException;
	
	/**
	 * aggiorna il record individuato dai parametri nella tavola di relazione tra capofila e partner, impostando l'id domanda del partner a null
	 * @param idCapofilaAcronimo
	 * @param idPartner
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer updateNullDomandaInCapofilaAcronimoPartner(String idCapofilaAcronimo, String idPartner) throws ServiziFindomWebException;
	
	/**
	 * aggiorna la domanda individuata da idDomanda impostandone lo stato a statoDomanda
	 * @param idDomanda
	 * @param statoDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public Integer updateStatoDomanda(String idDomanda, String statoDomanda) throws ServiziFindomWebException;
	
	/**
	 * effettua tutti gli aggiornamenti necessari per invalidare la domanda idDomandaCapofila; aggiorna anche la tabella di log audit
	 * @param idCapofilaAcronimo
	 * @param idAcronimoBando
	 * @param idDomandaCapofila
	 * @param codApplicativo
	 * @param utente
	 * @param tipoOperazione
	 * @param descrOperazione
	 * @throws ServiziFindomWebException
	 */
	public void invalidaDomandaCapofila(String idCapofilaAcronimo, String idAcronimoBando, Integer idDomandaCapofila,
			String codApplicativo, String utente, String tipoOperazione, String descrOperazione) throws ServiziFindomWebException;
	
	/**
	 * effettua tutti gli aggiornamenti necessari per invalidare la domanda idDomandaPartner; aggiorna anche la tabella di log audit
	 * @param idCapofilaAcronimo
	 * @param idPartner
	 * @param idDomandaPartner
	 * @param codApplicativo
	 * @param utente
	 * @param tipoOperazione
	 * @param descrOperazione
	 * @throws ServiziFindomWebException
	 */
	public void invalidaDomandaPartner(String idCapofilaAcronimo, String idPartner, Integer idDomandaPartner,
			String codApplicativo, String utente, String tipoOperazione, String descrOperazione) throws ServiziFindomWebException;

	/**
	 * restituisce l'attributo idStato presente nel nodo _operatorePresentatore dell'xml della domanda, se presente
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public String getIdStatoOperatorePresentatore(Integer idDomanda) throws ServiziFindomWebException;
	
	/**
	 * restituisce il codice dello stato della domanda avente id uguale al parametro idDomanda
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public String getStatoDomanda(String idDomanda) throws ServiziFindomWebException;

	/**
	 * Invoco la procedura plsql : fnc_crea_scheda_progetto(integer)
	 * @param idDomanda
	 * @return
	 */
	public String callProcedureCreaSchedaProgetto(Integer idDomanda) throws ServiziFindomWebException;


	/**
	 * Restituisce il codice dello stereotipo associato ad una domanda
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public String getStereotipoDomanda(Integer idDomanda) throws ServiziFindomWebException;

	/**
	 * Restituisce il dato flag_progetti_comuni
	 * @param idDomanda
	 * @return
	 * @throws ServiziFindomWebException
	 */
	public String getFlagProgettiComuniDomanda(Integer idDomanda) throws ServiziFindomWebException;

	/**
	 * Inserisce una entry nella tabella shell_t_documento_index 
	 * @param idDomanda
	 * @param uuidAllegato
	 * @param repository
	 * @param nomeFile
	 * @param messageDigest
	 * @param dtVerificaFirma
	 * @param idStatoDocumentoIndex
	 * @param idTipologiaNewDoc
	 * @param idSoggetto
	 * @param bFileUpload
	 * @return La chiave "id_documento_index" della entry inserita
	 * @throws ServiziFindomWebException
	 */
	public int insertShellTDocumentoIndexDB(Integer idDomanda,
			String uuidAllegato, String repository, String nomeFile,
			String messageDigest, String dtVerificaFirma,
			Integer idStatoDocumentoIndex, Integer idTipologiaNewDoc,
			int idSoggetto, byte[] bFileUpload) throws ServiziFindomWebException;


	public ShellDocumentoIndexDto getAllegatoFromShellTDocumentoIndex(Integer idAllegato) throws ServiziFindomWebException;


	public String getDenominazioneOperatorePresentatore(Integer idDomanda) throws ServiziFindomWebException;
	
}
