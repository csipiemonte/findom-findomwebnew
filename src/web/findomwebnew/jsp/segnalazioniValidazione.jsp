<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>


<r:include url="${layoutInclude.portalHead}" resourceProvider="sp" />

</head>
  <body>

    <!-- PAGE (OBBLIGATORIO) -->
    <div id="page">
      <r:include url="${layoutInclude.portalHeader}" resourceProvider="sp" />
      
      <!-- riempitivo sx (facoltativo) -->
		<div id="sx"></div>

		<!-- riempitivo dx (facoltativo) -->
		<div id="dx"></div>

		<!-- APPICATION AREA (OBBLIGATORIO) -->
		<div id="applicationArea">
		
		<!-- PANNELLO DEI CONTENUTI -->
        <div id="contentPanel">
        
        <form action="#" method="get">

			<div id="CenterWrapper" class="floatWrapper">
				<div id="centerPanel">
					<div class="wrapper">
						<!-- INZIO CONTENUTO CENTER PANEL -->

						<h3>Verifica anomalie</h3>

						<div class="pageContainer">

							<div class="stdMessagePanel">
								<div class="feedInfo">
									<p>
										La verifica &egrave; stata eseguita il giorno
										<s:property value="dataVerifica" />
										alle ore
										<s:property value="oraVerifica" />.
									</p>
								</div>
							</div>

							<h4>Anomalie riscontrate</h4>

							<div class="listaCheck">
								<p>
									<span class="check">
										<input type="checkbox" id="id_anom_blocc" class="noborder" />
									</span>
									<label for="id_anom_blocc">Visualizza solo le anomalie bloccanti</label>
								</p>
							</div>
							<!--listaCheck-->
							<table summary="...." id="verifica_anomalie"
								class="myovertable tablesorter">
								<col width="20%" />
								<col width="60%" />
								<col width="20%" />
								<thead>
									<tr>
										<th class="header headerSortUp" scope="col">Sezione</th>
										<th scope="col">Anomalia di compilazione</th>
										<th class="header" scope="col">Tipologia</th>
									</tr>
								</thead>
								<tbody>
									<%
										boolean even = true;
									%>
									<s:iterator value="segnalazioneValidazioneList">
										<%
											if (even) {
										%>
											<s:if test="bloccante">
												<tr class="even">
											</s:if>
											<s:else>
												<tr class="even ruleHideShow">
											</s:else>
										<%
											} else {
										%>
											<s:if test="bloccante">
												<tr>
											</s:if>
											<s:else>
												<tr class="ruleHideShow">
											</s:else>
										<%
											}
										%>
											<td><s:property value="tab1tab2" /></td>
											<td><s:property value="testo" /></td>
											<s:if test="bloccante">
												<td class="bloccante">Anomalia bloccante</td>
											</s:if>
											<s:else>
												<td>Segnalazione</td>
											</s:else>
										</tr>
										<%
											even = !even;
										%>
									</s:iterator>
								</tbody>
							</table>
							<script type="text/javascript">
								jQuery(document).ready(function() {
									initReady();
								}); // fine JQuery.document.ready.function
							
								function initReady() {
									$('#id_anom_blocc').click(function() {
										var $this = $(this);
										if ($this.is(':checked')) {
											$(".ruleHideShow").addClass("show-hide");
										} else {
											$(".ruleHideShow").removeClass("show-hide");
									}
								});
							</script>
						    <s:if test="altreSegnalazioneValidazioneList!=null">
							<table summary="...." id="verifica_altre_anomalie"
								class="myovertable tablesorter">
								<col width="20%" />
								<col width="60%" />
								<col width="20%" />
								<thead>
									<tr>
										<th class="header headerSortUp" scope="col">Ambito</th>
										<th scope="col">Segnalazione</th>
										<th class="header" scope="col">Tipologia</th>
									</tr>
								</thead>
								<tbody>
									<%
										boolean even2 = true;
									%>
									<s:iterator value="altreSegnalazioneValidazioneList">
										<%
											if (even2) {
										%>
									<s:if test="bloccante">
										<tr class="even">
									</s:if>
									<s:else>
										<tr class="even ruleHideShow">
									</s:else>
										<%
											} else {
										%>
											<s:if test="bloccante">
												<tr>
											</s:if>
											<s:else>
												<tr class="ruleHideShow">
											</s:else>
										<%
											}
										%>
											<td><s:property value="tab1tab2" /></td>
											<td><s:property value="testo" escape="false" /></td>
											<s:if test="bloccante">
												<td class="bloccante">Segnalazione bloccante</td>
											</s:if>
											<s:else>
												<td>Segnalazione</td>
											</s:else>
										</tr>
										<%
											even2 = !even2;
										%>
									</s:iterator>
								</tbody>
							
							</table>
						    </s:if>	
						</div><!--/pageContainer-->
						<!-- FINE CONTENUTO CENTER PANEL -->
					</div>
				</div>
			</div>
		</form>
		</div>	<!-- #contentPanel-->
        <!-- FINE PANNELLO DEI CONTENUTI -->

        <!-- footer (con remincl) (parte comune a tutto l'applicativo)  -->

        <r:include url="${layoutInclude.portalFooter}" resourceProvider="sp" />

      </div>
      <!-- FINE APPICATION AREA (OBBLIGATORIO) -->

    </div>
    <!-- FINE PAGE (OBBLIGATORIO) -->
  </body>
</html>