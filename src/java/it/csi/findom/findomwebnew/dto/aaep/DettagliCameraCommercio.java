/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.dto.aaep;

import java.io.Serializable;

public class DettagliCameraCommercio  implements Serializable{

	private static final long serialVersionUID = 1L;

	private String anno;
	private String dataAggiornamento;
	private String dataCancellazioneREA;
	private String dataIscrizioneREA;
	private String dataIscrizioneRegistroImprese;
	private String numIscrizionePosizioneREA;
	private String numRegistroImprese;
	private String numero;
	private String provincia;
	private String siglaProvincia;
	private String siglaProvinciaIscrizioneREA;
	private String tribunale;
	
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public String getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public String getDataCancellazioneREA() {
		return dataCancellazioneREA;
	}
	public void setDataCancellazioneREA(String dataCancellazioneREA) {
		this.dataCancellazioneREA = dataCancellazioneREA;
	}
	public String getDataIscrizioneREA() {
		return dataIscrizioneREA;
	}
	public void setDataIscrizioneREA(String dataIscrizioneREA) {
		this.dataIscrizioneREA = dataIscrizioneREA;
	}
	public String getDataIscrizioneRegistroImprese() {
		return dataIscrizioneRegistroImprese;
	}
	public void setDataIscrizioneRegistroImprese(
			String dataIscrizioneRegistroImprese) {
		this.dataIscrizioneRegistroImprese = dataIscrizioneRegistroImprese;
	}
	public String getNumIscrizionePosizioneREA() {
		return numIscrizionePosizioneREA;
	}
	public void setNumIscrizionePosizioneREA(String numIscrizionePosizioneREA) {
		this.numIscrizionePosizioneREA = numIscrizionePosizioneREA;
	}
	public String getNumRegistroImprese() {
		return numRegistroImprese;
	}
	public void setNumRegistroImprese(String numRegistroImprese) {
		this.numRegistroImprese = numRegistroImprese;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}
	public String getSiglaProvinciaIscrizioneREA() {
		return siglaProvinciaIscrizioneREA;
	}
	public void setSiglaProvinciaIscrizioneREA(String siglaProvinciaIscrizioneREA) {
		this.siglaProvinciaIscrizioneREA = siglaProvinciaIscrizioneREA;
	}
	public String getTribunale() {
		return tribunale;
	}
	public void setTribunale(String tribunale) {
		this.tribunale = tribunale;
	}
    
}
