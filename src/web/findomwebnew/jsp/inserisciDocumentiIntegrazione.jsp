<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>

<r:include url="${layoutInclude.portalHead}" resourceProvider="sp" />

<style>
.background_dialog .ui-dialog-titlebar {
	background-color: #0153A9;
}

.centerInputFile {
	margin-left: 20%;
}
</style>
</head>
<body>

	<!-- PAGE (OBBLIGATORIO) -->
	<div id="page">
		<r:include url="${layoutInclude.portalHeader}" resourceProvider="sp" />

		<!-- APPICATION AREA (OBBLIGATORIO) -->
		<div id="applicationArea">

			<!--area userInfoPanel (dati utente + esci)-->
			<r:include url="${layoutInclude.applicationHeader}"
				resourceProvider="sp" />

			<!-- FINE HEADER (parte comune a tutto l'applicativo) -->

			<!-- PANNELLO DEI CONTENUTI -->
			<div id="contentPanel">
				<div id="northPanel">
					<div class="wrapper">

						<!-- INIZIO CONTENUTO NORTH PANEL -->

						<!--inizio filo arianna + link help e contatti-->
						<div id="contextPanel">
							<div id="breadCrumbPanel">
								<span class="element">Sistemapiemonte</span> <span
									class="separator"> &raquo; </span> <span id="currentElement"
									class="active">gestione delle domande</span>
							</div>
							<r:include url="${layoutInclude.applicationLinkHelpContact}"
								resourceProvider="sp" />
						</div>

						<!--fine filo arianna + link help e contatti-->
						<!--area searchBox-->

						<!--inizio barra utente di applicativo-->
						<s:include value="include/userDataPanel.jsp" />
						<!--fine barra utente di applicativo-->


						<!--inizio menu dropdown-->
						<div class="menuDropdown">
							<ul class="sf-menu sf-navdrop sf-navdrop-simple">
								<li id="current"><span>Home</span></li>
								<li class="disattivo"><span>Indice</span></li>
							</ul>
						</div>
						<!--fine menu dropdown-->

						<!-- FINE CONTENUTO NORTH PANEL -->
					</div>
				</div>
				<!--/northPanel-->


				<div id="CenterWrapper" class="floatWrapper">
					<div id="centerPanel">
						<!--INIZIO PARAMETRI 1 / elenco risultati -->

						<!-- INZIO CONTENUTO CENTER PANEL -->
						<div class="wrapper">
							<div class="stdMessagePanel">
								<s:if test="hasActionErrors()">
									<div class="feedKo">
										<s:actionerror />
									</div>
								</s:if>
								<s:if test="hasActionMessages()">
									<div class="feedCorrect">
										<s:actionmessage />
									</div>
								</s:if>

							</div>

							<h4 class="caption">
								<span id="toggle_handle_01"
									class="toggle_handle collapse numTotTitle"> NOTIFICA
									RICHIESTA INTEGRAZIONE PER LA DOMANDA N. <s:property
										value="shellDomandeDto.idDomanda" /> DEL BANDO <s:property
										value="findomTBandiDirezioneDto.descBreveBando" />
								</span>
							</h4>


							<div class="content_caption toggle_target_01">

								<div class="boxAlter">
									<p style="font-weight:bold" >Funzionario</p>
									<table class="formTable" summary="...">
										<colgroup>
											<col width="20%" />
											<col width="40%" />
											<col width="20%" />
											<col width="40%" />
										</colgroup>


										<tr>
											<td>Settore</td>
											<td><s:property
													value="findomTBandiDirezioneDto.descrizioneSettore" /></td>
											<td>Direzione</td>
											<td><s:property
													value="findomTBandiDirezioneDto.descrizioneDirezione" /></td>
										</tr>

										<tr>
											<td>Nome</td>
											<td><s:property value="findomDFunzionariDTO.nome" /></td>
											<td>Cognome</td>
											<td><s:property value="findomDFunzionariDTO.cognome" />
											</td>
										</tr>
									</table>
								</div>
								<!--/boxAlter-->

								<div class="boxAlter">
									<p style="font-weight:bold" >Richiesta</p>
									<p>E' imperativo inviare la documentazione integrativa richiesta entro la data termine indicata</p>
									<table class="formTable" summary="...">
										<colgroup>
											<col width="20%" />
											<col width="40%" />
											<col width="20%" />
											<col width="40%" />
										</colgroup>

										<tr>
											<td>Data termine</td>
											<td><s:property
													value="shellDomandeDto.dtTermineIntegrazione" /></td>
											<td>Data richiesta</td>
											<td><s:property
													value="shellDomandeDto.dtRichiestaIntegrazione" /></td>
										</tr>

										<tr>
											<td>Testo Richiesta</td>
											<td><s:property
													value="shellDomandeDto.noteRichiestaIntegrazione" /></td>
											<td></td>
											<td></td>
										</tr>
									</table>
								</div>
								<!--/boxAlter-->
							</div>
							<!-- content caption -->
						</div>
						<!-- FINE CONTENUTO CENTER PANEL -->
						<!-- INZIO CONTENUTO CENTER PANEL -->
						<div class="wrapper">
							<h4 class="caption">
								<span id="toggle_handle_02"
									class="toggle_handle collapse numTotTitle"> Upload
									documenti integrativi </span>
							</h4>
		
							<div class="content_caption toggle_target_02">

								<s:if test="statoRichiesta!=null && statoRichiesta=='C'.toString() && (shellDomandeDto.flagAbilitaIntegrazione==null || !shellDomandeDto.flagAbilitaIntegrazione)">
								<p>Non &egrave; possibile inserire nuovi documenti in quanto l'integrazione &egrave; stata chiusa</p>
								</s:if>
								<s:elseif test="statoRichiesta!=null && statoRichiesta=='C'.toString() && (shellDomandeDto.flagAbilitaIntegrazione!=null && shellDomandeDto.flagAbilitaIntegrazione)">
								<p>Non &egrave; possibile inserire nuovi documenti in quanto &egrave; decorso il termine previsto per l'invio</p>
								</s:elseif>
								
								<!--INIZIO RISULTATI -->
								<s:if test="listaDocumenti != null && listaDocumenti.size()==0">
									<h4></h4>

									<!--INIZIO NON CI SONO RISULTATI -->
									<div class="stdMessagePanel" id="elencoZeroRisultati">
										<div class="feedInfo">											
											<p>Non &#232; ancora stato inserito alcun documento integrativo</p>
										</div>
									</div>
									<!--FINE NON CI SONO RISULTATI -->

								</s:if>

								<s:elseif
									test="listaDocumenti != null && listaDocumenti.size() > 0">

									<h4>Elenco documenti integrativi inviati</h4>
									<div id="elencoRisultati">
										<display:table name="listaDocumenti" uid="documento"
											requestURI="" keepStatus="true"
											clearStatus="${param['trova'] != null}"
											class="myovertable tablesorter" defaultsort="2"
											defaultorder="ascending">

											<display:column title="Documento" sortable="true"
												headerClass="sortable" headerScope="col">
												<s:url id="fileDownload" action="viewIndexFile">
													<s:param name="uuidFile" value="#attr.documento.uuidNodo" />
													<s:param name="nomeFile" value="#attr.documento.nomeFile" />
													<s:param name="idAllegato" value="1" />
												</s:url>
												<s:a href="%{fileDownload}">
													<s:property value="#attr.documento.nomeFile" />
												</s:a>
											</display:column>
											<display:column title="Numero Protocollo" sortable="true"
												headerClass="sortable" headerScope="col">
												<s:property value="#attr.documento.numProtocollo" />
											</display:column>
											<display:column title="Data Protocollo" sortable="true"
												headerClass="sortable" headerScope="col">
												<s:property value="#attr.documento.dtProtocollo" />
											</display:column>
										</display:table>
								</s:elseif>
								<div class="commandPanel" style="padding-top: 16px;">
									<s:form action="cercaDomande">
										<div class="button left">
											<span class="highlighted"> <s:submit type="submit"
													name="Indietro" id="indietro" value="Indietro" />
											</span>
										</div>
									</s:form>
									<s:if
										test="statoRichiesta!=null && statoRichiesta=='A'.toString() ">
										<div class="button right">
											<span class="highlighted"> <input type="button"
												value="+ Aggiungi Documento" onClick="openDialog();" />
											</span>
										</div>
									</s:if>
								</div>
								<p>IMPORTANTE. Per comunicare al funzionario la chiusura dell'integrazione richiesta, premere il pulsante 'chiudi integrazione'. Alla pressione del pulsante non sar&agrave; pi&ugrave; possibile aggiungere nuovi documenti all'elenco.</p>
							
							</div>
							<!--
						 content caption -->
						</div>
						<!-- FINE CONTENUTO CENTER PANEL -->
						<s:if
							test="statoRichiesta!=null && statoRichiesta=='A'.toString()  && listaDocumenti!=null && listaDocumenti.size()>0">
							<div class="commandPanel">
								<s:url id="chiudiIntegrazione" action="chiudiIntegrazione">
									<s:param name="idDomanda" value="idDomanda" />
									<s:param name="idBando" value="idBando" />
									<s:param name="statoRichiesta" value="statoRichiesta" />
								</s:url>
								<div class="button right">
									<span class="highlighted"> <s:a
											href="%{chiudiIntegrazione}">chiudi Integrazione</s:a>
									</span>
								</div>
							</div>
						</s:if>

					</div>
				</div>
			</div>
			<!--#contentPanel-->
			<!-- FINE PANNELLO DEI CONTENUTI -->

			<!-- footer (con remincl) (parte comune a tutto l'applicativo)  -->

			<r:include url="${layoutInclude.portalFooter}" resourceProvider="sp" />

		</div>
		<!-- FINE APPICATION AREA (OBBLIGATORIO) -->

	</div>
	<!-- FINE PAGE (OBBLIGATORIO) -->

	<div id="dialog" title="Aggiungi documenti integrativi a domanda"
		style="display: none">
		<s:form action="uploadDocumentiIntegrazione" namespace="/"
			method="POST" enctype="multipart/form-data">

			<div class="centerInputFile">
				<s:file name="fileUpload" label="Select a File to upload" size="40" />
			</div>

			<s:hidden name="idDomanda" value="%{idDomanda}" />
			<s:hidden name="idBando" value="%{idBando}" />
			<s:hidden name="statoRichiesta" value="%{statoRichiesta}" />

			<div class="commandPanel" style="margin-top: 30px;">
				<div class="button left">
					<span> <input type="button" value="Indietro"
						onClick="closeDialog();" />
					</span>
				</div>
				<div class="button right">
					<span class="save highlighted"> <input type="submit"
						value="Salva" />
					</span>
				</div>
			</div>

		</s:form>
	</div>

	<script type="text/javascript">
		function openDialog() {
			$("#dialog").dialog({
				dialogClass : 'background_dialog',
				maxWidth : 600,
				maxHeight : 500,
				width : 400,
				height : 200,
			});
		}

		function closeDialog() {
			$("#dialog").dialog('close');
		}
	</script>
</body>
</html>