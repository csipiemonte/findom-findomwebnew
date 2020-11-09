/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.findom.findomwebnew.business.helper.IndexHelper;
import it.csi.findom.findomwebnew.dto.index.DocumentoIndex;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.DocumentiIntegrativiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomAllegatiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomDFunzionariDTO;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.FindomTBandiDirezioneDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDocumentoIndexDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDomandeDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.VistaDomandeDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.exception.FindomwebException;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import it.doqui.index.ecmengine.client.webservices.exception.publishing.engine.management.InsertException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class UploadDocumentoIntegrazioneAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	// input
	private Long idDomanda;
	private Long idBando;
	private String statoRichiesta;

	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	private IndexHelper indexHelper;
	private List<String> errorsList;

	// output
	private List<DocumentiIntegrativiDto> listaDocumenti;
	private ShellDomandeDto shellDomandeDto;
	private FindomTBandiDirezioneDto findomTBandiDirezioneDto;
	private FindomDFunzionariDTO findomDFunzionariDTO;

	public void validate() {
		if (errorsList == null)
			errorsList = new ArrayList<String>();
		else
			errorsList.clear();

		if (fileUpload == null) {
			errorsList.add("Il file deve essere selezionato");
		} else {

			// CONTROLLO DIMENSIONE FILE
			String dimensioneMassima = "0";
			try {
				dimensioneMassima = getServiziFindomWeb().getValoreParametro(Constants.MAX_SIZE_SIGNED_FILE);
			} catch (ServiziFindomWebException e) {
				errorsList.add("Problemi nel contattare il server");
			}
			if (StringUtils.isNotBlank(dimensioneMassima)) {
				long dimMax = Long.parseLong(dimensioneMassima);
				if (fileUpload.length() > dimMax) {
					errorsList.add("La dimensione del file non può essere superiore a 5 Mb");
				}
			}
			// CONTROLLO ESTENSIONE
			
			// controllo estensione che prevede anche il caso di file firmati
			String estensione = getEstensioneFileSenzaFirma();		
			
			ArrayList<String> tipiDoc = new ArrayList<String>();
			try {
				tipiDoc = getServiziFindomWeb().getTipoDocumentoUpload(Constants.PARAMETRO_UPLOAD_FILE);
			} catch (ServiziFindomWebException e) {

			}
			if (!tipiDoc.contains(estensione)) {
				errorsList.add("Estensione del file non consentita");
			}

			// 1. verifico se il documento e' gia' sul db
			ShellDocumentoIndexDto shelDoc = null;
			try {
				shelDoc = getServiziFindomWeb().getDocumentoIndex(Integer.valueOf((int) this.idDomanda.longValue()), getFileUploadFileName());
			} catch (ServiziFindomWebException e) {
			}

			if (shelDoc != null) {
				errorsList.add("Il documento con il nome " + getFileUploadFileName() + " esiste per questa domanda");
			}
		}
		if (!errorsList.isEmpty())
			addActionError("vari errori in input");
	}

	

	public String executeAction() throws Exception, FindomwebException {
		File file = fileUpload;
		byte[] bFileUpload = null;
		if (file != null) {
			bFileUpload = new byte[(int) file.length()];
			bFileUpload = readFile(new FileInputStream(file), bFileUpload);
			Integer idDomanda = Integer.valueOf((int) this.idDomanda.longValue());

			// 0. verifico dimensione
			String dimensioneMassima = getServiziFindomWeb().getValoreParametro(Constants.MAX_SIZE_SIGNED_FILE);
			if (StringUtils.isNotBlank(dimensioneMassima)) {
				long dimMax = Long.parseLong(dimensioneMassima);
				if (file.length() > dimMax) {
					throw new FindomwebException(Constants.ERR_MSG_MAX_SIZE_SIGNED_FILE);
				}
			}
			// 0.1 riverifico
			ArrayList<String> tipiDoc = new ArrayList<String>();
			try {
				tipiDoc = getServiziFindomWeb().getTipoDocumentoUpload(Constants.PARAMETRO_UPLOAD_FILE);
			} catch (ServiziFindomWebException e) {

			}
			if (!tipiDoc.contains(getEstensioneFileSenzaFirma())) { 
				throw new FindomwebException("Caricare il file pdf della domanda, firmato digitalmente con estensione .p7m");
			}

			// 1. verifico se il documento e' gia' sul db
			ShellDocumentoIndexDto shelDoc = getServiziFindomWeb().getDocumentoIndex(idDomanda, getFileUploadFileName());
			if (shelDoc != null) {
				throw new InsertException();
			}

			// 2. recupero i dati della domanda
			ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(idDomanda);
			if (listaAggData == null || (listaAggData != null && listaAggData.size() != 1)) {
				addActionError(Constants.CARICA_DOMANDA_FAILED);
				return ERROR;
			}

			AggrDataDto aggrDomanda = listaAggData.get(0);

			StatusInfo state = getStatus();
			String cfBenefic = state.getCodFiscaleBeneficiario();
			String userId = aggrDomanda.getUserId();

			String ruolo = getUserInfo().getRuolo();
			if ((Constants.RUOLO_AMM.equals(ruolo)) || (Constants.RUOLO_LR.equals(ruolo))
					|| (StringUtils.isNotBlank(cfBenefic) && StringUtils.isNotBlank(userId) && StringUtils.equals(userId, cfBenefic))) {
			} else {
				addActionError(Constants.CARICA_DOMANDA_NOTALLOWED);
				return ERROR;
			}

			// 3. setto lo state
			state.setTemplateId(aggrDomanda.getTemplateId());
			state.setModelName(aggrDomanda.getModelName());
			state.setNumProposta(aggrDomanda.getModelId());
			state.setStatoProposta(aggrDomanda.getModelStateFk());
			state.setNumSportello(aggrDomanda.getIdSportelloBando());
			state.setAperturaSportelloDa(aggrDomanda.getDataAperturaSportello());
			state.setAperturaSportelloA(aggrDomanda.getDataChiusuraSportello());

			int idSoggettoCreatore = 0;
			// recupero i dati della domanda da visualizzare nei titoli della
			// pagina
			if (Constants.RUOLO_AMM.equals(ruolo) || Constants.RUOLO_LR.equals(ruolo)) {
				idSoggettoCreatore = aggrDomanda.getIdSoggettoCreatore();
			} else {
				idSoggettoCreatore = state.getIdSoggettoCollegato();
			}

			int idSoggettoBeneficiario = state.getIdSoggettoBeneficiario();
			ArrayList<VistaDomandeDto> datiDomanda = getServiziFindomWeb().getVistaDomandaByCreatoreBeneficiarioDomanda(idSoggettoCreatore, idSoggettoBeneficiario, idDomanda);

			state.setDescrNormativa(datiDomanda.get(0).getNormativa());
			state.setCodiceAzione(datiDomanda.get(0).getCodiceAzione());
			state.setDescrBando(datiDomanda.get(0).getDescrBando());
			state.setDescrBreveBando(datiDomanda.get(0).getDescrBreveBando());
			state.setDescrTipolBeneficiario(datiDomanda.get(0).getDescrizioneTipolBeneficiario());
			state.setFlagBandoDematerializzato(datiDomanda.get(0).getFlagBandoDematerializzato());
			state.setTipoFirma(datiDomanda.get(0).getTipoFirma());

			setStatus(state);

			String templateCode = aggrDomanda.getTemplateCode();

			// 3. valorizzo oggetto da passare a index
			DocumentoIndex docInx = valorizzaOggettoDocumentoIndex(state, bFileUpload, templateCode, idDomanda, Constants.ID_ALLEGATO_DOCUMENTO_INTEGRATIVO);
			String annoApertSport = impostaAnnoAperturaSportello();

			// 4. salvo su index
			String uuidAllegato = indexHelper.insertDocIntegrativoSuIndex(docInx, annoApertSport, idDomanda, templateCode, true);

			// 5. salvo su db
			if (uuidAllegato != "ERROR_ON_INDEX") {
				ShellSoggettiDto utenteLoggato = getServiziFindomWeb().getDatiSoggettoByCodiceFiscale(getUserInfo().getCodFisc());

				String messageDigest = "";
				Integer idStatoDocumentoIndex = 1;
				String nomeFile = "-";
				String repository = "-";
				if (file != null) {
					nomeFile = getFileUploadFileName();
					repository = Constants.INDEX_REPOSITORY;
					idStatoDocumentoIndex = Constants.ID_STATO_DOC_INDEX_GENERATO;
				}

				getServiziFindomWeb().insertShellTDocumentoIndex(idDomanda, uuidAllegato, repository, nomeFile, messageDigest, null, idStatoDocumentoIndex, Constants.ID_ALLEGATO_DOCUMENTO_INTEGRATIVO,
						utenteLoggato.getIdSoggetto());

				ShellDocumentoIndexDto documen = null;
				documen = getServiziFindomWeb().getDocumentoIndex(uuidAllegato);
				if (documen == null) {
					throw new RuntimeException("Documento non salvato sul db ");
				}
			}
		}
		return SUCCESS;
	}

	private DocumentoIndex valorizzaOggettoDocumentoIndex(StatusInfo stato, byte[] bFileUpload, String templateCode, Integer idDomanda, Integer idTipologiaNewDoc)
			throws ServiziFindomWebException, Exception {
		DocumentoIndex docInx = new DocumentoIndex();
		
		// recupero informazioni sulla tipologia del documento allegato
		log.info( "[UploadDocumentoIntegrazioneAction::valorizzaOggettoDocumentoIndex] stato: " + stato+", templateCode: " +templateCode+", idDomanda: "+idDomanda+", idTipologiaNewDoc: "+idTipologiaNewDoc);
		HashMap<Integer, FindomAllegatiDto> mappaAllegati = getServiziFindomWeb().getMappaAllegati(stato.getTemplateId(), null, null);
		FindomAllegatiDto allegatoDto = null;
		String descrTipologiaAllegato = "";
		String flagObbl = "";
		if (mappaAllegati != null && !mappaAllegati.isEmpty()) {
			allegatoDto = (FindomAllegatiDto) mappaAllegati.get(idTipologiaNewDoc);

			if (allegatoDto != null) {
				descrTipologiaAllegato = allegatoDto.getDescrizione();
				flagObbl = allegatoDto.getFlagObbligatorio();
			}
		}

		String stereotipo = getServiziFindomWeb().getStereotipoDomanda(stato.getNumProposta());
		log.info( "[UploadDocumentoIntegrazioneAction::valorizzaOggettoDocumentoIndex] stereotipo = "+stereotipo);
		if(StringUtils.isBlank(stereotipo)){
			log.error( "[UploadDocumentoIntegrazioneAction::valorizzaOggettoDocumentoIndex] stereotipo null ");
			throw new Exception();
		}

		// valorizzo Oggetto DocumentoIndex
		if (bFileUpload != null) {
			docInx.setFilename(getFileUploadFileName());
		} else {
			docInx.setFilename("--");
		}
		docInx.setBytes(bFileUpload);
		// docInx.setUidNode(uidNode);
		docInx.setCodiceBando(templateCode);
		docInx.setDescrizioneBando(stato.getDescrBando());
		docInx.setIdBando(stato.getTemplateId());
		docInx.setIdDomanda(idDomanda);
		docInx.setCFUtenteUploader(stato.getOperatore());
		docInx.setDescrizioneUtenteUploader(stato.getDescrizioneOperatore());
		docInx.setCodiceImpresaEnte(stato.getCodFiscaleBeneficiario());
		docInx.setDescrizioneImpresaEnte(stato.getDescrImpresaEnte());
		docInx.setIdTipologiaBeneficiario(stato.getIdSoggettoBeneficiario()); // ??
		docInx.setDescrizioneBeneficiario(stato.getDescrTipolBeneficiario());
		docInx.setStereotipoBeneficiario(stereotipo);
		docInx.setIdTipologiaAllegato(idTipologiaNewDoc);
		docInx.setDescrizioneTipologiaAllegato(descrTipologiaAllegato);
		docInx.setContentType(getFileUploadContentType());
		docInx.setFlagObbligatorio(flagObbl);

		return docInx;

	}

	public static byte[] readFile(InputStream is, byte[] bytes) throws IOException {
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	private String impostaAnnoAperturaSportello() {
		String ret = "";
		String dataAperturaSportello = getStatus().getAperturaSportelloDa();

		if (StringUtils.isNotBlank(dataAperturaSportello)) {
			ret = dataAperturaSportello.substring(6, 10);
		} else {
			int anno = Calendar.getInstance().get(Calendar.YEAR);
			ret = anno + "";
		}
		return ret;
	}

	public String getEstensioneFile() {
		String estensione = "";
		String nomeFile = getFileUploadFileName();
		if (StringUtils.isNotEmpty(nomeFile)) {
			int index = nomeFile.lastIndexOf(".");
			estensione = nomeFile.substring(index);
		}
		return estensione;
	}

	/*
	 * il metodo restituisce l'estensione del file che si sta uploadando, tranne nel caso in cui tale estensione sia compresa tra quelle che sulla 
	 * tavola dei parametri sono in corrispondenza del codice SIGNEDFILE_EXTENSION; in quel caso il metodo ritorna l'estensione che la precede
	 */
	private String getEstensioneFileSenzaFirma() {
		String estensione = "";
		estensione = getEstensioneFile();

		//verifico se il file ha una estensione prevista dal parametro  SIGNEDFILE_EXTENSION (preso da findom_d_parametri su db)
		//e in caso affermativo verifico ritorno l'etensione che la precede
		String signedFileExtensions = "";
		try {
			signedFileExtensions = getServiziFindomWeb().getValoreParametro(Constants.SIGNEDFILE_EXTENSION);
		} catch (ServiziFindomWebException e1) {				
		}	
		if(StringUtils.isNotBlank(signedFileExtensions)){						
			ArrayList<String> signedFileExtensionsList = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(signedFileExtensions, ",");
			while (st.hasMoreElements()) {
				signedFileExtensionsList.add((String)st.nextElement());
			}
			if (!signedFileExtensionsList.isEmpty() && signedFileExtensionsList.contains(estensione)) {

				// il file ha una estensione che lo caratterizza come file firmato; individuo l'estensione precedente l'ultima estensione
				String nomeFile = getFileUploadFileName();	
				if (StringUtils.isNotBlank(nomeFile)){
					int indiceUltimaEstensione = nomeFile.lastIndexOf(".");
					String nomeFileOrig = nomeFile.substring(0, indiceUltimaEstensione);

					int indicePenultimaEstensione = nomeFileOrig.lastIndexOf(".");
					String estensioneOrig = nomeFileOrig.substring(indicePenultimaEstensione);
					if(StringUtils.isNotBlank(estensioneOrig)){
						estensione = estensioneOrig;
					}
				}
			}				
		}
		return estensione;		
	}

	public File getFileUpload() {
		return fileUpload;
	}

	public String getFileUploadContentType() {
		return fileUploadContentType;
	}

	public String getFileUploadFileName() {
		return fileUploadFileName;
	}

	public IndexHelper getIndexHelper() {
		return indexHelper;
	}

	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}

	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	public void setIndexHelper(IndexHelper indexHelper) {
		this.indexHelper = indexHelper;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public Long getIdBando() {
		return idBando;
	}

	public String getStatoRichiesta() {
		return statoRichiesta;
	}

	public List<DocumentiIntegrativiDto> getListaDocumenti() {
		return listaDocumenti;
	}

	public ShellDomandeDto getShellDomandeDto() {
		return shellDomandeDto;
	}

	public FindomTBandiDirezioneDto getFindomTBandiDirezioneDto() {
		return findomTBandiDirezioneDto;
	}

	public FindomDFunzionariDTO getFindomDFunzionariDTO() {
		return findomDFunzionariDTO;
	}

	public void setIdBando(Long idBando) {
		this.idBando = idBando;
	}

	public void setStatoRichiesta(String statoRichiesta) {
		this.statoRichiesta = statoRichiesta;
	}

	public void setListaDocumenti(List<DocumentiIntegrativiDto> listaDocumenti) {
		this.listaDocumenti = listaDocumenti;
	}

	public void setShellDomandeDto(ShellDomandeDto shellDomandeDto) {
		this.shellDomandeDto = shellDomandeDto;
	}

	public void setFindomTBandiDirezioneDto(FindomTBandiDirezioneDto findomTBandiDirezioneDto) {
		this.findomTBandiDirezioneDto = findomTBandiDirezioneDto;
	}

	public void setFindomDFunzionariDTO(FindomDFunzionariDTO findomDFunzionariDTO) {
		this.findomDFunzionariDTO = findomDFunzionariDTO;
	}

	public List<String> getErrorsList() {
		return errorsList;
	}

}
