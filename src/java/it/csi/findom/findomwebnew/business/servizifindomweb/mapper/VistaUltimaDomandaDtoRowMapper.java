/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.findomwebnew.business.servizifindomweb.mapper;

import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.CostituzioneImpresaDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.LegaleRappresentanteDocumentoDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.LegaleRappresentanteDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.OperatorePresentatoreDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.SedeLegaleDto;
import it.csi.findom.findomwebnew.dto.serviziFindomWeb.exp.VistaUltimaDomandaDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VistaUltimaDomandaDtoRowMapper extends GenericRowMapper {
	public VistaUltimaDomandaDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		VistaUltimaDomandaDto dto = new VistaUltimaDomandaDto();
		
		OperatorePresentatoreDto operatorePresentatore = new OperatorePresentatoreDto();
		CostituzioneImpresaDto costituzioneImpresa = new CostituzioneImpresaDto();
		SedeLegaleDto sedeLegale = new SedeLegaleDto();
		LegaleRappresentanteDto legaleRappresentante = new LegaleRappresentanteDto();
		
		operatorePresentatore.setCodiceFiscale(rs.getString("cod_fiscale"));
		operatorePresentatore.setDenominazione(rs.getString("denominazione"));
		operatorePresentatore.setIdFormaGiuridica(rs.getString("id_forma_giuridica"));
		operatorePresentatore.setCodiceFormaGiuridica(rs.getString("cod_forma_giuridica"));
		operatorePresentatore.setDescrizioneFormaGiuridica(rs.getString("descr_forma_giuridica"));
		operatorePresentatore.setPartitaIva(rs.getString("partita_iva"));
		operatorePresentatore.setIndirizzoPec(rs.getString("indirizzo_pec"));
		operatorePresentatore.setIdAteco2007(rs.getString("id_ateco"));
		operatorePresentatore.setCodicePrevalenteAteco2007(rs.getString("codice_ateco"));
		operatorePresentatore.setDescrizioneAteco2007(rs.getString("descrizione_ateco"));
		operatorePresentatore.setIdAttivitaEconomica(rs.getString("id_attivita_economica"));
		operatorePresentatore.setCodiceAttivitaEconomica(rs.getString("cod_attivita_economica"));
		operatorePresentatore.setDescrizioneAttivitaEconomica(rs.getString("descr_attivita_economica"));
		operatorePresentatore.setIdStato(rs.getString("id_stato"));
		operatorePresentatore.setDescrStato(rs.getString("descr_stato"));

		costituzioneImpresa.setDataCostituzioneImpresa(rs.getString("dt_costituzione_impresa"));
		costituzioneImpresa.setProvincia(rs.getString("sigla_provincia"));
		costituzioneImpresa.setProvinciaDescrizione(rs.getString("descrizione_provincia"));
		costituzioneImpresa.setIdStatoCostImpresa(rs.getString("id_stato_cost_impresa"));
		costituzioneImpresa.setDescrStatoCostImpresa(rs.getString("descr_stato_cost_impresa"));

		legaleRappresentante.setCodiceFiscale(rs.getString("cod_fiscale_rappr_leg"));
		legaleRappresentante.setCognome (rs.getString("cognome_rappr_leg"));
		legaleRappresentante.setNome(rs.getString("nome_rappr_leg"));
		legaleRappresentante.setGenere(rs.getString("genere_rappr_leg"));
		legaleRappresentante.setLuogoNascita(rs.getString("luogo_nascita_rappr_leg"));
		legaleRappresentante.setProvinciaNascita(rs.getString("cod_provincia_nascita_rappr_leg"));
		legaleRappresentante.setSiglaProvinciaNascita(rs.getString("sigla_provincia_nascita_rappr_leg"));
		legaleRappresentante.setProvinciaNascitaDescrizione(rs.getString("descr_provincia_nascita_rappr_leg"));
		legaleRappresentante.setComuneNascita(rs.getString("cod_comune_nascita_rappr_leg"));
		legaleRappresentante.setComuneNascitaDescrizione(rs.getString("descr_comune_nascita_rappr_leg"));
		legaleRappresentante.setStatoEsteroNascita(rs.getString("cod_stato_estero_nascita_rappr_leg"));
		legaleRappresentante.setStatoEsteroNascitaDescrizione(rs.getString("descr_stato_estero_nascita_rappr_leg"));
		legaleRappresentante.setDataNascita(rs.getString("data_nascita_rappr_leg"));
		
		LegaleRappresentanteDocumentoDto documento = new LegaleRappresentanteDocumentoDto();
		documento.setIdTipoDocRiconoscimento(rs.getString("id_tipo_doc_riconoscimento_rappr_leg"));
		documento.setDescrizioneTipoDocRiconoscimento(rs.getString("descr_tipo_doc_riconoscimento_rappr_leg"));
		documento.setNumDocumentoRiconoscimento(rs.getString("num_doc_riconoscimento_rappr_leg"));
		documento.setDocRilasciatoDa(rs.getString("num_doc_rilasciato_da_rappr_leg"));
		documento.setDataRilascioDoc(rs.getString("data_rilascio_doc_rappr_leg"));
		
		legaleRappresentante.setDocumento(documento);
		
		legaleRappresentante.setLuogoResidenza(rs.getString("descr_luogo_residenza_rappr_leg"));
		legaleRappresentante.setSiglaProvinciaResidenza(rs.getString("sigla_provincia_residenza_rappr_leg"));
		legaleRappresentante.setProvinciaResidenza(rs.getString("cod_provincia_residenza_rappr_leg"));
		legaleRappresentante.setProvinciaResidenzaDescrizione(rs.getString("descr_provincia_residenza_rappr_leg"));
		legaleRappresentante.setComuneResidenza(rs.getString("cod_comune_residenza_rappr_leg"));
		legaleRappresentante.setComuneResidenzaDescrizione(rs.getString("descr_comune_residenza_rappr_leg"));
		legaleRappresentante.setStatoEsteroResidenza(rs.getString("cod_stato_estero_residenza_rappr_leg"));
		legaleRappresentante.setStatoEsteroResidenzaDescrizione(rs.getString("descr_stato_estero_residenza_rappr_leg"));
		legaleRappresentante.setCittaEsteraResidenza(rs.getString("descr_citta_estera_residenza_rappr_leg"));
		legaleRappresentante.setCap(rs.getString("cod_cap_residenza_rappr_leg"));
		legaleRappresentante.setIndirizzo(rs.getString("descr_indirizzo_residenza_rappr_leg"));
		legaleRappresentante.setNumCivico(rs.getString("num_civico_residenza_rappr_leg"));

		sedeLegale.setStato(rs.getString("cod_stato_sede_legale"));
		sedeLegale.setProvincia(rs.getString("cod_provincia_sede_legale"));
		sedeLegale.setProvinciaSigla(rs.getString("sigla_provincia_sede_legale"));
		sedeLegale.setProvinciaDescrizione(rs.getString("descr_provincia_sede_legale"));
		sedeLegale.setComune(rs.getString("cod_comune_sede_legale"));
		sedeLegale.setComuneDescrizione(rs.getString("descr_comune_sede_legale"));
		sedeLegale.setStatoEstero(rs.getString("cod_stato_estero_sede_legale"));
		sedeLegale.setStatoEsteroDescrizione(rs.getString("descr_stato_estero_sede_legale"));
		sedeLegale.setCittaEstera(rs.getString("descr_citta_estera_sede_legale"));
		sedeLegale.setCap(rs.getString("cod_cap_sede_legale"));
		sedeLegale.setIndirizzo(rs.getString("descr_indirizzo_sede_legale"));
		sedeLegale.setNumCivico(rs.getString("num_civico_sede_legale"));
		sedeLegale.setTelefono(rs.getString("num_telefono_sede_legale"));
		sedeLegale.setPec(rs.getString("pec_sede_legale"));
		sedeLegale.setPersonaRifSL(rs.getString("persona_rif_sede_legale"));
		sedeLegale.setEmail(rs.getString("email_sede_legale"));
		sedeLegale.setCellulare(rs.getString("num_cellulare_sede_legale"));

		dto.setCostituzioneImpresa(costituzioneImpresa);
		dto.setOperatorePresentatore(operatorePresentatore);
		dto.setLegaleRappresentante(legaleRappresentante);
		dto.setSedeLegale(sedeLegale);
		
		return dto;
	}
}
