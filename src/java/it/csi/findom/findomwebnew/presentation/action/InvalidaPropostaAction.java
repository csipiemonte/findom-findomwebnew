/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.presentation.action;

import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.findom.findomwebnew.business.helper.IndexHelper;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.AggrDataDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.CapofilaAcronimoDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.CapofilaAcronimoPartnerDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellSoggettiDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.TipolBeneficiariDto;
import it.csi.findom.findomwebnew.exception.serviziFindomWeb.ServiziFindomWebException;
import it.csi.findom.findomwebnew.presentation.util.Constants;
import it.csi.findom.findomwebnew.presentation.util.StringUtils;
import it.csi.findom.findomwebnew.presentation.vo.Domanda;
import it.csi.findom.findomwebnew.presentation.vo.SelItem;
import it.csi.findom.findomwebnew.presentation.vo.StatusInfo;
import java.util.ArrayList;
import java.util.Objects;

public class InvalidaPropostaAction  extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "InvalidaPropostaAction";	
	private IndexHelper indexHelper; 
	
	String idDomanda;
	
	// liste del form di ricerca
	SelItem[] listaNormative;
	SelItem[] listadescBreveBando;
	SelItem[] listaBandi;
	SelItem[] listaSportelli;
	SelItem[] listaStati;
	
	// liste del form di inserimento
	SelItem[] listaNormativeINS;
	SelItem[] listadescBreveBandoINS;
	SelItem[] listaBandiINS;
	SelItem[] listaSportelliINS;
	TipolBeneficiariDto[] listaTipologieBeneficiariINS;
	
	// lista delle domande trovate
	ArrayList<Domanda> listaDomande ;
	
	// campi che vengono postati per la ricerca
	String normativa;
	String descBreveBando;
	String bando;
	String sportello;
	String statoDomanda;
	String numDomanda;
	
	@Override
	public String executeAction() throws SystemException, UnrecoverableException, CSIException {
		
		final String methodName = "executeAction";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
		log.info(logprefix + " idDomanda="+idDomanda);
		
		if(StringUtils.isBlank(idDomanda)){
			log.error(logprefix + " idDomanda NULLO, impossibile effettuare l'invalidazione della domanda ");
			setRisultatoKO(Constants.INVALIDA_DOMANDA_FAILED);
			return ERROR;
		}
		
		Integer idDomInt = Integer.parseInt(idDomanda);

		// 0. carico i dati dell'aggregatore per la domanda selezionata
		ArrayList<AggrDataDto> listaAggData = getServiziFindomWeb().getAggrDataByIdDomanda(idDomInt);
		if(listaAggData==null || (listaAggData!=null && listaAggData.size()!=1)){
			log.error(logprefix + " listaAggData NULLA o di dimensione errata");
			setRisultatoKO(Constants.INVALIDA_DOMANDA_FAILED);
			return ERROR;
		}
		
		AggrDataDto aggrDomanda = listaAggData.get(0);
		
		Integer modelProg = aggrDomanda.getModelProgr();
		log.info(logprefix + " modelProg="+modelProg);

		// 1. verifico che la domanda in questione sia invalidabile (stato BZ, OK, KO, OW);
		//inoltre la domanda deve essere di un bando di tipo 'progetto comune' e il ruolo 
		//del beneficiario della domanda deve essere gia' stato definito (capofila o partner o proponente unico).
		//Sono controlli probabilmente ridondanti in quanto gia' a livello di jsp sono stati effettuati 
		//per far apparire il pulsante 'invalida'
		String statoDomanda = aggrDomanda.getModelStateFk();
		log.info(logprefix + " statoDomanda="+statoDomanda);
		
		Integer idSoggettoBeneficiario = aggrDomanda.getIdSoggettoBeneficiario();
		log.info(logprefix + " idSoggettoBeneficiario= "+idSoggettoBeneficiario);
		
		Integer idSoggettoCreatore = aggrDomanda.getIdSoggettoCreatore();
		log.info(logprefix + " idSoggettoCreatore= "+idSoggettoCreatore);
		
		String ruolo = getUserInfo().getRuolo();
		log.info(logprefix + " ruolo preso da UserInfo= "+ruolo);	
		
		String flagProgettiComuni =  getServiziFindomWeb().getFlagProgettiComuniDomanda(idDomInt);
		log.info(logprefix + " flagProgettiComuni= "+flagProgettiComuni);
		
		if(StringUtils.equals(flagProgettiComuni, "false")){
			log.error(logprefix + " impossibile invalidare la domanda, stato = "+statoDomanda);
			setRisultatoKO(Constants.STATO_DOMANDA_NON_AMMESSO_PER_INVALIDA);
			return ERROR;
		}
		String ruoloDefinitoInProgettoComune = "";  //C sta per Capofila o proponente unico, P sta per Partner
		String idCapofilaAcronimo = "";
		String idAcronimoBando = "";
		String idPartner = "";
		CapofilaAcronimoDto capofilaAcronimoDto = getServiziFindomWeb().getCapofilaAcronimoValidoByIdDomanda(idDomInt);
		if(capofilaAcronimoDto!=null){
			ruoloDefinitoInProgettoComune = "C";
			idCapofilaAcronimo = Objects.toString(capofilaAcronimoDto.getIdCapofilaAcronimo(),"");
			idAcronimoBando = Objects.toString(capofilaAcronimoDto.getIdAcronimoBando(),"");
		}
		if (StringUtils.isBlank(ruoloDefinitoInProgettoComune)){
			//la domanda non e' di un capofila; verifico se e' la domanda di un partner
			CapofilaAcronimoPartnerDto capofilaAcronimoPartnerDto = getServiziFindomWeb().getCapofilaAcronimoPartnerValidoByIdDomanda(idDomInt); 
			if(capofilaAcronimoPartnerDto !=null){
				idCapofilaAcronimo = Objects.toString(capofilaAcronimoPartnerDto.getIdCapofilaAcronimo(),"");
				idPartner = Objects.toString(capofilaAcronimoPartnerDto.getIdPartner(),"");
				ruoloDefinitoInProgettoComune = "P";
			}
		}
		
		if(StringUtils.isBlank(ruoloDefinitoInProgettoComune)){
			log.error(logprefix + " impossibile invalidare la domanda perche' il ruolo (capofila o partner o proponente unico) non e' stato ancora definito ");
			setRisultatoKO(Constants.INVALIDA_NON_AMMESSO_PER_RUOLO_NON_DEFINITO);
			return ERROR;
		}
		
		if(StringUtils.equals(Constants.STATO_INVIATA, statoDomanda) 
				|| StringUtils.equals(Constants.STATO_CONCLUSA, statoDomanda)
				|| StringUtils.equals(Constants.STATO_INVALIDATA, statoDomanda)){
			log.error(logprefix + " impossibile invalidare la domanda, stato = "+statoDomanda);
			setRisultatoKO(Constants.STATO_DOMANDA_NON_AMMESSO_PER_INVALIDA);
			return ERROR;
		}
		
		// 2. verifico che l'utente collegato abbia i permessi per invalidare la domanda in questione.
		// L'utente deve essere amministratore o legale rappresentante oppure il codice fiscale del beneficiario deve corrispondere al valore in aggr_t_model.userId.
		// Anche in questo caso il controllo e' superfluo perche' se la ricerca ha restituito la domanda, allora essa e' di sicuro relativa al beneficiario
		// che l'utente ha scelto all'accesso a findom e quindi anche se l'utente non è amministratore o legale rappresentante 

		StatusInfo statusInfo = getStatus();
		log.info(logprefix + " state="+statusInfo);
		
		String cfBenefic = statusInfo.getCodFiscaleBeneficiario();
		log.info(logprefix + " CodFiscaleBeneficiario="+cfBenefic);
		log.info(logprefix + " IdSoggettoBeneficiario="+statusInfo.getIdSoggettoBeneficiario());
		
		//se l'utente non ha aperto la domanda da cancellare lo status non contiene i dati del beneficiario		
		if(cfBenefic==null || statusInfo.getIdSoggettoBeneficiario()==null){
			getStatus().setIdSoggettoBeneficiario(idSoggettoBeneficiario);
			
			log.info(logprefix + " IdSoggettoBeneficiario ricaricato="+idSoggettoBeneficiario);
			
			if (cfBenefic == null) {
				ArrayList<String> listaId = new ArrayList<String>();
				listaId.add(idSoggettoBeneficiario.toString());
				ArrayList<ShellSoggettiDto> listaSoggetti = getServiziFindomWeb().getDatiSoggettoByIdSoggetto(listaId);
				if (listaSoggetti.size()>0 && listaSoggetti.get(0)!=null) {
					cfBenefic = listaSoggetti.get(0).getCodiceFiscale();
					getStatus().setCodFiscaleBeneficiario(cfBenefic);
				}
			}
			log.info(logprefix + " CodFiscaleBeneficiario ricaricato su StatusInfo ="+cfBenefic);
		}
		
		String userId = aggrDomanda.getUserId();
		log.info(logprefix + " userId="+userId);
		
		if((Constants.RUOLO_AMM.equals(ruolo)) || (Constants.RUOLO_LR.equals(ruolo)) ||
				(StringUtils.isNotBlank(cfBenefic) && StringUtils.isNotBlank(userId) && StringUtils.equals(userId,cfBenefic))){
			// utente ha i permessi per visualizzare la domanda
			log.info(logprefix+" utente valido");
		}else{
			log.info(logprefix + "l' utente non ha i permessi per invalidare la domanda numero:"+idDomanda);
			setRisultatoKO(Constants.INVALIDA_DOMANDA_NOTALLOWED);
			return ERROR;
		}		
		
		//3. Se il ruolo del beneficiario e' Capofila, verifico che non ci siano domande dei suoi partner in stato Inviato 
		if(StringUtils.isNotBlank(ruoloDefinitoInProgettoComune) && ruoloDefinitoInProgettoComune.equals("C")){ 
			//potrebbe essere anche proponente unico, ma in questo caso non troverei di sicuro dei partner
			log.info(logprefix + " verifico se esistono dei partner con domande in stato finale");
			boolean trovataDomandaInStatoFinale= esisteAlmenoUnaDomandaPartnerInStatoFinale(idCapofilaAcronimo, idDomInt);
			if(trovataDomandaInStatoFinale){
				log.info(logprefix + "esistono dei partner con domande in stato finale");
				setRisultatoKO(Constants.INVALIDA_NON_AMMESSO_PER_DOMANDE_PARTNER_IN_STATO_FINALE);
				return ERROR;
			}
				
			
		}
		//4. disattivo i record eventualmente presenti sulle tavole del capofila, degli acronimi e dei capofila-partner; 
		//metto in stato invalidato le eventuali domande del capofila e dei partner
		String codApplicativo = Constants.CSI_LOG_IDAPPL;
		String utente = getUserInfo().getCodFisc()+" - "+ getUserInfo().getCognome() + " " + getUserInfo().getNome();
		String tipoOperazione = Constants.CSI_LOG_OPER_INVALIDA_DOMANDA;		
		String descrOperazione = "";
		
		if(StringUtils.isNotBlank(ruoloDefinitoInProgettoComune)){
			//passo ai due metodi anche le informazioni da inserite nei log audit in modo che anche la tracciatura sia in transazione
			if(ruoloDefinitoInProgettoComune.equals("C")){
				descrOperazione = "invalidata domanda (capofila) numero: "+idDomInt;
				getServiziFindomWeb().invalidaDomandaCapofila(idCapofilaAcronimo, idAcronimoBando, idDomInt,
						codApplicativo, utente, tipoOperazione, descrOperazione);
			}else if(ruoloDefinitoInProgettoComune.equals("P")){
				descrOperazione = "invalidata domanda (partner) numero: "+idDomInt;
				getServiziFindomWeb().invalidaDomandaPartner(idCapofilaAcronimo, idPartner, idDomInt,
						codApplicativo, utente, tipoOperazione, descrOperazione );
			}
		} 		
		
		getServletRequest().getSession().removeAttribute("listaDomande");		
		// le combo di inserimento sono in getServletRequest() e non vanno modificate
		// le combo di ricerca devono essere svuotate per forzarne il ricaricamento nella Action "cercaDomande"
		predisponiListe();
		
		log.debug(logprefix + " END");
		setRisultatoOK(Constants.INVALIDA_DOMANDA);
		return SUCCESS;
	}

	/**
	 * Popolo le combo box del form di ricerca e del form di creazione
	 * 
	 * @return
	 */
	private void predisponiListe() {
		final String methodName = "predisponiListe";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.debug(logprefix + " BEGIN");
		
		
		/////////////////////////////////////////
		// Combo di ricerca 
		// svuoto le liste delle combo forzandone il ricaricamento nella Action "cercaDomande"
		/////////////////////////////////////////
		
		// popolo le combo del form di inserimento
		listaNormative = (SelItem[]) getServletRequest().getSession().getAttribute("listaNormative");
		listadescBreveBando = (SelItem[]) getServletRequest().getSession().getAttribute("listadescBreveBando");
		listaBandi = (SelItem[]) getServletRequest().getSession().getAttribute("listaBandi");
		listaSportelli = (SelItem[]) getServletRequest().getSession().getAttribute("listaSportelli");
		listaStati = (SelItem[]) getServletRequest().getSession().getAttribute("listaStati");
		log.debug(logprefix + " ricaricate liste delle combo del form di ricerca dalla sessione");
		
		setListaDomande(null);
		log.debug(logprefix + " eliminata lista delle domande trovate dalla sessione");
		
		
		/////////////////////////////////////////
		// Combo di inserimento
		// le prendo dalla getServletRequest().
		/////////////////////////////////////////
		
		log.debug(logprefix + "listaNormativeINS presa da request="+getServletRequest().getSession().getAttribute("listaNormativeINS"));
		log.debug(logprefix + "listaBandiINS presa da request="+getServletRequest().getSession().getAttribute("listaBandiINS"));
		log.debug(logprefix + "listadescBreveBandoINS presa da request="+getServletRequest().getSession().getAttribute("listadescBreveBandoINS"));
		log.debug(logprefix + "listaSportelliINS presa da request="+getServletRequest().getSession().getAttribute("listaSportelliINS"));
		log.debug(logprefix + "listaTipologieBeneficiariINS presa da request="+getServletRequest().getSession().getAttribute("listaTipologieBeneficiariINS"));
		
		// popolo le combo del form di inserimento
		listaNormativeINS = (SelItem[]) getServletRequest().getSession().getAttribute("listaNormativeINS");
		listadescBreveBandoINS = (SelItem[]) getServletRequest().getSession().getAttribute("listadescBreveBandoINS");
		listaBandiINS = (SelItem[]) getServletRequest().getSession().getAttribute("listaBandiINS");
		listaSportelliINS = (SelItem[]) getServletRequest().getSession().getAttribute("listaSportelliINS");
		listaTipologieBeneficiariINS = (TipolBeneficiariDto[]) getServletRequest().getSession().getAttribute("listaTipologieBeneficiariINS");
		
		log.debug(logprefix + " ricaricate liste delle combo del form di inserimento dalla sessione");
		
		log.debug(logprefix + " END");
	}
	
	
	private boolean esisteAlmenoUnaDomandaPartnerInStatoFinale(String idCapofilaAcronimo, Integer idDomandaCapofila) throws ServiziFindomWebException {		
		final String methodName = "esisteAlmenoUnaDomandaPartnerInStatoFinale";
		final String logprefix = "[" + CLASS_NAME + "::" + methodName + "] ";
		log.info(logprefix + " BEGIN");
	
		ArrayList<CapofilaAcronimoPartnerDto> capDtoList = getServiziFindomWeb().getCapofilaAcronimoPartnerListByIdCapofilaAcronimo(idCapofilaAcronimo);
		log.info(logprefix + " ho recuperato da shell_r_capofila_acronimo_partner una lista di " + (capDtoList == null ? "0" : capDtoList.size()) + " partner validi " );
		
		if(capDtoList!=null && !capDtoList.isEmpty()){
			for (CapofilaAcronimoPartnerDto curCapDto : capDtoList) {
				if(curCapDto==null || StringUtils.isBlank(curCapDto.getIdPartner())){
					continue;
				}				
				String idDomandaPartner = Objects.toString(curCapDto.getIdDomandaPartner(), "");
				if(StringUtils.isNotBlank(idDomandaPartner) && !idDomandaPartner.equals("0")){
					String statoDomanda = getServiziFindomWeb().getStatoDomanda(idDomandaPartner);
					if(StringUtils.isNotBlank(statoDomanda) && statoDomanda.equals(Constants.STATO_INVIATA)){
						log.info(logprefix + " trovata una domanda partner in stato finale - esco dal metodo");
						return true;
					}
				}
			}
		}
		log.info(logprefix + " END");
		return false;
	}
	
	public void setRisultatoKO(String risultato) {
		getServletRequest().getSession().setAttribute("risultatoKO", risultato);
	}

	public void setRisultatoOK(String risultato) {
		getServletRequest().getSession().setAttribute("risultatoOK", risultato);
	}
	
	// GETTERS && SETTERS	
	public SelItem[] getListaNormative() {
		return listaNormative;
	}
	public void setListaNormative(SelItem[] listaNormative) {
		this.listaNormative = listaNormative;
	}
	public SelItem[] getListadescBreveBando() {
		return listadescBreveBando;
	}
	public void setListadescBreveBando(SelItem[] listadescBreveBando) {
		this.listadescBreveBando = listadescBreveBando;
	}
	public SelItem[] getListaBandi() {
		return listaBandi;
	}
	public void setListaBandi(SelItem[] listaBandi) {
		this.listaBandi = listaBandi;
	}
	public SelItem[] getListaSportelli() {
		return listaSportelli;
	}
	public void setListaSportelli(SelItem[] listaSportelli) {
		this.listaSportelli = listaSportelli;
	}
	public SelItem[] getListaStati() {
		return listaStati;
	}
	public void setListaStati(SelItem[] listaStati) {
		this.listaStati = listaStati;
	}
	public SelItem[] getListaNormativeINS() {
		return listaNormativeINS;
	}
	public void setListaNormativeINS(SelItem[] listaNormativeINS) {
		this.listaNormativeINS = listaNormativeINS;
	}
	public SelItem[] getListadescBreveBandoINS() {
		return listadescBreveBandoINS;
	}
	public void setListadescBreveBandoINS(SelItem[] listadescBreveBandoINS) {
		this.listadescBreveBandoINS = listadescBreveBandoINS;
	}
	public SelItem[] getListaBandiINS() {
		return listaBandiINS;
	}
	public void setListaBandiINS(SelItem[] listaBandiINS) {
		this.listaBandiINS = listaBandiINS;
	}
	public SelItem[] getListaSportelliINS() {
		return listaSportelliINS;
	}
	public void setListaSportelliINS(SelItem[] listaSportelliINS) {
		this.listaSportelliINS = listaSportelliINS;
	}
	public TipolBeneficiariDto[] getListaTipologieBeneficiariINS() {
		return listaTipologieBeneficiariINS;
	}
	public void setListaTipologieBeneficiariINS(
			TipolBeneficiariDto[] listaTipologieBeneficiariINS) {
		this.listaTipologieBeneficiariINS = listaTipologieBeneficiariINS;
	}
	public String getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(String idDomanda) {
		this.idDomanda = idDomanda;
	}
	public ArrayList<Domanda> getListaDomande() {
		return listaDomande;
	}
	public void setListaDomande(ArrayList<Domanda> listaDomande) {
		this.listaDomande = listaDomande;
	}
	public String getNormativa() {
		return normativa;
	}
	public void setNormativa(String normativa) {
		this.normativa = normativa;
	}
	public String getDescBreveBando() {
		return descBreveBando;
	}
	public void setDescBreveBando(String descBreveBando) {
		this.descBreveBando = descBreveBando;
	}
	public String getBando() {
		return bando;
	}
	public void setBando(String bando) {
		this.bando = bando;
	}
	public String getSportello() {
		return sportello;
	}
	public void setSportello(String sportello) {
		this.sportello = sportello;
	}
	public String getStatoDomanda() {
		return statoDomanda;
	}
	public void setStatoDomanda(String statoDomanda) {
		this.statoDomanda = statoDomanda;
	}
	public String getNumDomanda() {
		return numDomanda;
	}
	public void setNumDomanda(String numDomanda) {
		this.numDomanda = numDomanda;
	}
	
	public IndexHelper getIndexHelper() {
		return indexHelper;
	}
	public void setIndexHelper(IndexHelper indexHelper) {
		this.indexHelper = indexHelper;
	}
	
}
