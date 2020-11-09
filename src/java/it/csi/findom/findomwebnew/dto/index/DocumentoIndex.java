/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.index;

import java.io.Serializable;

/**
 * Classe che rappresenta i metadati del file da uploadare su Index
 * @author 1427
 *
 */
public class DocumentoIndex implements Serializable {

	private static final long serialVersionUID = 1L;

	private String filename;
	private String uidNode;
	private byte[] bytes;
	private String  codiceBando; 				// INCOMERA (aggr_t_template.template_code) [string]
	private String  descrizioneBando; 			// Incomera aggr_t_template.template_description) [string]
	private Integer idBando; 					// 1 (aggr_t_template.template_id) [integer]
	private Integer idDomanda; 					// 2116 (aggr_t_model.model_id) [integer]
	private String  CFUtenteUploader; 			// AAAAAA00A11B000J  (CF user che ha fatto upload ) [string]
	private String  descrizioneUtenteUploader; 	// DEMO 21 CSI PIEMONTE (Nome e Cognome user che ha fatto upload ) [string] 
	private String  codiceImpresaEnte; 			// 04971390010 (aggr_t_model.user_id) (CF-PIVA impresa-ente per cui viene presentata la domanda ) [string]
	private String  descrizioneImpresaEnte; 	// FINSOFT I.S. (aggr_t_model.user_id) (CF-PIVA impresa-ente per cui viene presentata la domanda ) [string]
	private Integer idTipologiaBeneficiario; 	// 18 (findom_d_tipol_beneficiari.id_tipol_beneficiario) [integer]
	private String  descrizioneBeneficiario; 	// Organismo di ricerca (findom_d_tipol_beneficiari.descrizione)  [string]
	private String  stereotipoBeneficiario; 	// OR (findom_d_tipol_beneficiari.cod_stereotipo)  [string]
	private Integer idTipologiaAllegato; 		// 1 (findom_d_allegati.id_allegato) [integer]
	private String  descrizioneTipologiaAllegato; // Fotocopia documento d'identita del legale rappresentante (findom_d_allegati.descrizione)  [string]
	private String flagObbligatorio ;			// NON fa parte dei metadata di index
	private String docDifferibile; 				// NON fa parte dei metadata di index
	private String contentType;
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\t filename:" + getFilename() + "\n");
		sb.append("\t uidNode:" + getUidNode() + "\n");
		sb.append("\t codiceBando:" + getCodiceBando() + "\n");
		sb.append("\t descrizioneBando:" + getDescrizioneBando() + "\n");
		sb.append("\t idBando:" + getIdTipologiaBeneficiario() + "\n");
		sb.append("\t idDomanda:" + getIdDomanda() + "\n");
		sb.append("\t CFUtenteUploader:" + getCFUtenteUploader() + "\n");
		sb.append("\t descrizioneUtenteUploader:" + getDescrizioneUtenteUploader() + "\n");
		sb.append("\t codiceImpresaEnte:" + getCodiceImpresaEnte() + "\n");
		sb.append("\t descrizioneImpresaEnte:" + getDescrizioneImpresaEnte() + "\n");
		sb.append("\t idTipologiaBeneficiario:" + getIdTipologiaBeneficiario() + "\n");
		sb.append("\t descrizioneBeneficiario:" + getDescrizioneBeneficiario() + "\n");
		sb.append("\t stereotipoBeneficiario:" + getStereotipoBeneficiario() + "\n");
		sb.append("\t idTipologiaAllegato:" + getIdTipologiaAllegato() + "\n");
		sb.append("\t descrizioneTipologiaAllegato:" + getDescrizioneTipologiaAllegato() + "\n");
		sb.append("\t flagObbligatorio:" + getFlagObbligatorio() + "\n");
		sb.append("\t docDifferibile:" + getDocDifferibile() + "\n");
		return "DocumentoIndex:[[\n" + sb.toString() + "]]";
	}
	
	
	// GETTERS && SETTERS
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getUidNode() {
		return uidNode;
	}
	public void setUidNode(String uidNode) {
		this.uidNode = uidNode;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public String getCodiceBando() {
		return codiceBando;
	}
	public void setCodiceBando(String codiceBando) {
		this.codiceBando = codiceBando;
	}
	public String getDescrizioneBando() {
		return descrizioneBando;
	}
	public void setDescrizioneBando(String descrizioneBando) {
		this.descrizioneBando = descrizioneBando;
	}
	public Integer getIdBando() {
		return idBando;
	}
	public void setIdBando(Integer idBando) {
		this.idBando = idBando;
	}
	public Integer getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(Integer idDomanda) {
		this.idDomanda = idDomanda;
	}
	public String getCFUtenteUploader() {
		return CFUtenteUploader;
	}
	public void setCFUtenteUploader(String cFUtenteUploader) {
		CFUtenteUploader = cFUtenteUploader;
	}
	public String getDescrizioneUtenteUploader() {
		return descrizioneUtenteUploader;
	}
	public void setDescrizioneUtenteUploader(String descrizioneUtenteUploader) {
		this.descrizioneUtenteUploader = descrizioneUtenteUploader;
	}
	public String getCodiceImpresaEnte() {
		return codiceImpresaEnte;
	}
	public void setCodiceImpresaEnte(String codiceImpresaEnte) {
		this.codiceImpresaEnte = codiceImpresaEnte;
	}
	public String getDescrizioneImpresaEnte() {
		return descrizioneImpresaEnte;
	}
	public void setDescrizioneImpresaEnte(String descrizioneImpresaEnte) {
		this.descrizioneImpresaEnte = descrizioneImpresaEnte;
	}
	public Integer getIdTipologiaBeneficiario() {
		return idTipologiaBeneficiario;
	}
	public void setIdTipologiaBeneficiario(Integer idTipologiaBeneficiario) {
		this.idTipologiaBeneficiario = idTipologiaBeneficiario;
	}
	public String getStereotipoBeneficiario() {
		return stereotipoBeneficiario;
	}
	public void setStereotipoBeneficiario(String stereotipoBeneficiario) {
		this.stereotipoBeneficiario = stereotipoBeneficiario;
	}
	public Integer getIdTipologiaAllegato() {
		return idTipologiaAllegato;
	}
	public void setIdTipologiaAllegato(Integer idTipologiaAllegato) {
		this.idTipologiaAllegato = idTipologiaAllegato;
	}
	public String getDescrizioneTipologiaAllegato() {
		return descrizioneTipologiaAllegato;
	}
	public void setDescrizioneTipologiaAllegato(String descrizioneTipologiaAllegato) {
		this.descrizioneTipologiaAllegato = descrizioneTipologiaAllegato;
	}
	public String getDescrizioneBeneficiario() {
		return descrizioneBeneficiario;
	}
	public void setDescrizioneBeneficiario(String descrizioneBeneficiario) {
		this.descrizioneBeneficiario = descrizioneBeneficiario;
	}
	public String getFlagObbligatorio() {
		return flagObbligatorio;
	}
	public void setFlagObbligatorio(String flagObbligatorio) {
		this.flagObbligatorio = flagObbligatorio;
	}
	public String getDocDifferibile() {
		return docDifferibile;
	}
	public void setDocDifferibile(String docDifferibile) {
		this.docDifferibile = docDifferibile;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
