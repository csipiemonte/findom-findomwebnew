<%@page import="it.csi.util.performance.StopWatch"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@taglib prefix="a" uri="http://www.sistemapiemonte.it/formazioneprofessionale/tlds/aggregatoreutil"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<%
	StopWatch stopWatch = new StopWatch("findomwebnew");
	stopWatch.start();
	
	// dichiarazione dell'oggetto di "stato" inserito nel contesto
	it.csi.findom.findomwebnew.presentation.vo.StatusInfo statusInfo = null;
	
	try{
%>

<jsp:useBean id="findomwebContext" class="java.util.TreeMap" scope="session"/>

<%
	StopWatch watchInit = new StopWatch("domweb");
	watchInit.start();
%>
<a:init />
<%
	watchInit.stop();
	watchInit.dumpElapsed("aggregatoreUtilImpl.jsp", "INIT TAG", "Renderizzazione", "-");
%>

<%
	// lettura delle informazioni di "stato" inserite nel contesto da aggregatoreUtil dopo l'inizializzazione
	String  formId    = (String ) findomwebContext.get("xformId");
	String  formType  = (String ) findomwebContext.get("xformType");
	String  formDesc  = (String ) findomwebContext.get("xformDesc");
	Integer formProg  = (Integer) findomwebContext.get("xformProg");
	String  formName  = (String ) findomwebContext.get("xformName");
	String  formState = (String ) findomwebContext.get("xformState");
	//
	List<String> validationInfos  = (List<String>) findomwebContext.get("AGGR_messageValidationInfo");
	List<String> validationWarns  = (List<String>) findomwebContext.get("AGGR_messageValidationWarn");
	List<String> validationErrors = (List<String>) findomwebContext.get("AGGR_messageValidationError");
	//
	statusInfo = (it.csi.findom.findomwebnew.presentation.vo.StatusInfo) findomwebContext.get("statusInfo");
	
	// log vars
	// System.out.println("JSP - formId:"    + formId);
	// System.out.println("JSP - formType:"  + formType);
	// System.out.println("JSP - formDesc:"  + formDesc);
	// System.out.println("JSP - formProg:"  + formProg);
	// System.out.println("JSP - formName:"  + formName);
	// System.out.println("JSP - formState:" + formState);
	//
	// System.out.println("JSP - validationInfos:"  + validationInfos);
	// System.out.println("JSP - validationWarns:"  + validationWarns);
	// System.out.println("JSP - validationErrors:" + validationErrors);
	//
	// if(statusInfo!=null){
	// 	System.out.println("JSP - statusInfo.getOperatore:"    + statusInfo.getOperatore());
	// 	System.out.println("JSP - statusInfo.getTemplateId:"   + statusInfo.getTemplateId());
	// 	System.out.println("JSP - statusInfo.getDataTrasmissione:" + statusInfo.getDataTrasmissione());
	// }else{
	// 	System.out.println("JSP - statusInfo NULL");  
	// }
	//
	// Iterator contextIter = findomwebContext.entrySet().iterator();
	// while (contextIter.hasNext()) {
	// 	Map.Entry entry = (Map.Entry) contextIter.next();
	// 	System.out.println("-----CONTEXT-----> k=[" + entry.getKey() + "] v=[" + entry.getValue()+"]");
	// }
%>

<%
	StopWatch watchBar = new StopWatch("domweb");
	watchBar.start();
%>
<a:bar
	homeVisible="true" homeLabel="Home"
	listVisible="false" listLabel="Debug" listUrl="exec.do?xcommId=LIST_PAGE"
	menuVisible="true" menuLabel="Indice"
/>
<%
	watchBar.stop();
	watchBar.dumpElapsed("aggregatoreUtilImpl.jsp", "BAR TAG", "Renderizzazione", "-");
%>

<%
	StopWatch watchInclude4Menu = new StopWatch("domweb");
	watchInclude4Menu.start();
%>
<a:include4Menu name="cosa_posso_fare_e_sei_in" >
	<div id="FRAGMENT_COSA_POSSO_FARE_E_SEI_IN">
		<h4 class="caption">
			<span id="toggle_handle_01" class="toggle_handle collapse">Accesso alla Domanda</span>
		</h4>
		<div class="content_caption toggle_target_01 indice">
			<div class="stdMessagePanel noSpaceBottom">
				<div class="feedInfo">
					<p class="boxSX">Cosa posso fare?</p>
					<%if (it.csi.findom.findomwebnew.presentation.util.Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(statusInfo.getFlagBandoDematerializzato())){ %>
					<div class="boxDX">
		                <ul>
		                  <li>Compilare i dati previsti nelle sezioni</li>
		                  <li>Salvare e stampare la Domanda in bozza</li>
		                  <li>Effettuare i controlli sulla Domanda</li>
		                 </ul>
		                 <ul> 
		                  <li>Concludere e stampare il modulo definitivo</li>
		                  <li>Firmare la domanda</li>
		                  <li>Fare l'upload della domanda firmata</li>
		                </ul>
	                </div>
					<%} else { %>
	            	<div class="boxDX">
		                <ul>
		                  <li>Compilare i dati previsti nelle sezioni</li>
		                  <li>Salvare e stampare la Domanda in bozza</li>
		                 </ul>
		                 <ul> 
		                  <li>Effettuare i controlli sulla Domanda</li>
		                  <li>Inviare e stampare il modulo definitivo</li>
		                </ul>
	                </div>
	                <%} %>
	                <p class="helpInfo">Per il dettaglio delle singole funzionalit&agrave;, consulta la sezione 'Regole di compilazione' o accedi alla pagina 'Documentazione'</p>
				</div>
			</div>
		</div>
		<!--/content_caption -->

		<h3 class="spaceTop">
			Sei in >> Domanda n&deg;<%= statusInfo.getNumProposta()+" - "+ statusInfo.getDescrNormativa()+" - "+ statusInfo.getDescrBreveBando()%>
		</h3>
		<p class="riga_2"><%= statusInfo.getDescrBando()+" - "+statusInfo.getDescrTipolBeneficiario() %></p>
		
		<% if (formState != null && formState.equals("IN")) { %>
			<div class="stdMessagePanel noSpaceBottom">		
				<div class="feedInfo">
				<%if (it.csi.findom.findomwebnew.presentation.util.Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(statusInfo.getFlagBandoDematerializzato())){ %>
					<p>La domanda &egrave; stata inviata e non &egrave; pi&ugrave; modificabile: &egrave; possibile la stampa e l'accesso a tutti i contenuti in sola lettura e il download del modulo firmato.</p>
				<%} else {%>
					<p>La domanda &egrave; stata confermata e non &egrave; pi&ugrave; modificabile: &egrave; possibile la stampa e l'accesso a tutti i contenuti in sola lettura</p>
				<%} %>
				</div>
			</div>
		<% } %>
		<% if (formState != null && formState.equals("CO")) { %>
			<div class="stdMessagePanel noSpaceBottom">		
				<div class="feedInfo">	
				<%if (it.csi.findom.findomwebnew.presentation.util.Constants.FIRMA_OLOGRAFA.equalsIgnoreCase(statusInfo.getTipoFirma())){ %>
					<p>La domanda &egrave; stata chiusa correttamente e non &egrave; pi&ugrave; modificabile: &egrave; possibile la stampa, l'accesso a tutti i contenuti in sola lettura.
					   Per l'invio della domanda &egrave; necessario scaricare il pdf, firmarlo con firma autografa e ricaricarlo sul sistema.</p>
				<%} else {%>
					<p>La domanda &egrave; stata chiusa correttamente e non &egrave; pi&ugrave; modificabile: &egrave; possibile la stampa, l'accesso a tutti i contenuti in sola lettura. 
					   Per l'invio della domanda &egrave; necessario scaricare il pdf, firmarlo digitalmente e ricaricarlo sul sistema.</p>
				<%} %>
				</div>
			</div>
		<% } %>
	</div><!--/FRAGMENT_COSA_POSSO_FARE_E_SEI_IN -->
</a:include4Menu>

<a:include4Menu name="torna_alla_home">
	<div class="commandPanel">
		<div class="button left">
			<span>
				<a href="/@@war.context.name@@/cercaDomande.do" title="Torna alla home">Torna alla home</a>
			</span>
		</div>
	</div>
</a:include4Menu>

<a:include4Menu name="div_menu_container_open">
	<div class="pageContainer indexStep">
</a:include4Menu>

<a:include4Page name="dettaglio_proposta" >

	<div id="FRAGMENT_DETTAGLIO_PROPOSTA">
		<h4 class="caption">
			<span id="toggle_handle_0" class="toggle_handle collapse">Dettaglio della Domanda</span>
		</h4>
		<div class="content_caption toggle_target_0">
			<table class="formTable">
				<col width="20%" />
				<col width="30%" />
				<col width="20%" />
				<col width="30%" />
				<col width="80%" />
				<tr>
					<th>Bando</th>
					<td><%= statusInfo.getDescrNormativa()+" - "+statusInfo.getDescrBreveBando() %></td>
					<th>Sportello<br/><p style="font-style:italic; font-weight:normal;">data inizio - data fine</p></th>
					<td>					
					<%= statusInfo.getAperturaSportelloDa() %>					
					<% if(statusInfo.getAperturaSportelloA()!=null){ %>						
						- <%= statusInfo.getAperturaSportelloA()%>						
					<%}else{ %>
						- data fine non definita
					<%}%>
					</td>					
				</tr>
				<tr>
					<th>N&deg; domanda</th>
					<td><%= statusInfo.getNumProposta()+"" %></td>
					<th>Stato domanda</th>
					<td><%= ((Map<String, String>) statusInfo.getStatoPropostaMap()).get(formState) %></td>
				</tr>
			</table>
		</div><!--/content_caption -->
	</div><!--/FRAGMENT_DETTAGLIO_PROPOSTA -->

</a:include4Page>
<%
	watchInclude4Menu.stop();
	watchInclude4Menu.dumpElapsed("aggregatoreUtilImpl.jsp", "INCLUDE4MENU TAG", "Renderizzazione 4 tag", "-");
%>

<%
	StopWatch watchMessage = new StopWatch("domweb");
	watchMessage.start();
%>
<a:message />
<%
	watchMessage.stop();
	watchMessage.dumpElapsed("aggregatoreUtilImpl.jsp", "BAR MESSAGE", "Renderizzazione", "-");
%>

<a:include4Menu name="mostra_segnalazioni_validazione_globale">
<% if ((validationWarns != null && !validationWarns.isEmpty()) || (validationErrors != null && !validationErrors.isEmpty())) { %>
	<div class="commandPanel">
		<div class="button left">
			<span>
				<a href="/@@war.context.name@@/mostraSegnalazioniValidazione.do" target="_blank" title="mostra segnalazioni">Mostra segnalazioni</a>
			</span>
		</div>
	</div>
<% } %>
</a:include4Menu>

<%
	StopWatch watchMenu = new StopWatch("domweb");
	watchMenu.start();
%>
<a:menu />
<%
	watchMenu.stop();
	watchMenu.dumpElapsed("aggregatoreUtilImpl.jsp", "MENU TAG", "Renderizzazione", "-");
%>

<a:include4Page name="div_page_container_open">
	<div class="pageContainer msgBoxPos">
</a:include4Page>

<a:include4Page  name="javascript comuni" >
<div id="FRAGMENT_COMMON_JAVASCRIPTS">
		<script type="text/javascript">
			function getFromSplit(str, sep, index) {
				if (str === undefined || sep === undefined)
					return "";
				else
					return $.trim(str.split(sep)[index]);
			}

			function openDetail(itemSelected_id, itemSelected_val, itemId_id, itemId_val, modifyButton_id) {
				if (itemSelected_id != "") {
					$("#" + itemSelected_id).val(itemSelected_val);
				}
				if (itemId_id != "") {
					$("#" + itemId_id).val(itemId_val);
				}
				$("#" + modifyButton_id).click();
			}

			function deleteItem(itemSelected_id, itemSelected_val, deleteButton_id, modalewinConfirm_id) {
				$("#" + itemSelected_id).val(itemSelected_val);

				$(function() {
					$("#" + modalewinConfirm_id)
						.dialog({
								title: $("#" + deleteButton_id).attr("title"),
								width: 600,
								modal: true,
								bgiframe: false,
								resizable: false,
								draggable: false,
								chiudi: false,
								load: function(dialog_target) {
									jQuery("span.close_js", dialog_target).jbutton({
										text : 'no, chiudi',
										callback : function (el) {
											$(el).parents(".ui-dialog-content:eq(0)").dialog("close");
										}
									});
								}
					});
				});
			}
			
			function deleteItem2(itemSelected_id, itemSelected_val, itemSelected_id2, itemSelected_val2, deleteButton_id, modalewinConfirm_id) {
				$("#" + itemSelected_id).val(itemSelected_val);
				$("#" + itemSelected_id2).val(itemSelected_val2);

				$(function() {
					$("#" + modalewinConfirm_id)
						.dialog({
								title: $("#" + deleteButton_id).attr("title"),
								width: 600,
								modal: true,
								bgiframe: false,
								resizable: false,
								draggable: false,
								chiudi: false,
								load: function(dialog_target) {
									jQuery("span.close_js", dialog_target).jbutton({
										text : 'no, chiudi',
										callback : function (el) {
											$(el).parents(".ui-dialog-content:eq(0)").dialog("close");
										}
									});
								}
					});
				});
			}

			function confirmItemDeletion(modaleConfirm_id, deleteButton_id) {
				$("#" + modaleConfirm_id).dialog("close");
				$("#" + deleteButton_id).click();
			}

			function createInputTypeHidden(formId, inputName, inputValue) {
				inputTypeHidden = document.createElement('input');
				inputTypeHidden.type = 'hidden';
				inputTypeHidden.name = inputName;
				inputTypeHidden.value = inputValue;
				execForm = document.getElementById(formId);
				execForm.appendChild(inputTypeHidden);
			}
		</script>
	</div><!--/FRAGMENT_COMMON_JAVASCRIPTS -->
</a:include4Page>


<%
	StopWatch watchBody = new StopWatch("domweb");
	watchBody.start();
%>
<a:body />
<%
	watchBody.stop();
	watchBody.dumpElapsed("aggregatoreUtilImpl.jsp", "BODY TAG", "Renderizzazione", "-");
%>

<div class="commandPanel">
	<a:buttons />
	
	<a:include4Menu>
		<!-- bottoni aggiuntivi che devono essere gestiti dal fruitore -->
		<div class="button left">
			<% if (formState != null && (formState.equals("OK") || formState.equals("OW"))) { %>
			 	<!--  VISUALIZZA IL BOTTONE INVIA O CONCLUDI -->
				<%if (it.csi.findom.findomwebnew.presentation.util.Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(statusInfo.getFlagBandoDematerializzato())){ %>
				
				
				<!--  MODALE INIZIO  -->
				<script type="text/javascript">
					function viewModaleConfermaConcludi(viewModaleButton_id, modaleConfirm_id) {

						$(function() {
							$("#" + modaleConfirm_id)
								.dialog({
										title: $("#" + viewModaleButton_id).attr("title"),
										width: 600,
										modal: true,
										bgiframe: false,
										resizable: false,
										draggable: false,
										chiudi: false,
										load: function(dialog_target) {
											jQuery("span.close_js", dialog_target).jbutton({
												text : 'no, chiudi',
												callback : function (el) {
													$(el).parents(".ui-dialog-content:eq(0)").dialog("close");
												}
											});
										}
							});
						});
					}
					
					function showSendingBoxConcludi(){
						$("#id_modaleConfermaConcludi").dialog("close");
						$("#loading_box").dialog({
							title: "Conclusione in corso..",
							width: 600,
							modal: true,
							bgiframe: false,
							resizable: false,
							draggable: false,
							chiudi: false,
							load: function(dialog_target) {
								jQuery("span.close_js", dialog_target).jbutton({
									text : 'no, chiudi',
									callback : function (el) {
										$(el).parents(".ui-dialog-content:eq(0)").dialog("close");
									}
								});
							}
						});
					}
				</script>

				<div id="loading_box" hidden="true">
					<div id="dialog_target">
						<h3>Conclusione in corso..</h3>
						<div class="dialogPanel">
							<div class="stdMessagePanel">
								<div class="feedLoad"><p>Attendere prego: il sistema sta concludendo la domanda...</p></div>
							</div>
						</div> 
					</div>
				</div>
				
				<span>
					<a class="send" href="#" style="dialog-elimina" id="id_confermaConcludi" 
						title="Concludi Domanda" 
						onclick="viewModaleConfermaConcludi( 'id_confermaConcludi', 'id_modaleConfermaConcludi')">
						Concludi
					</a>
				</span>

				<div hidden="true" id="id_modaleConfermaConcludi">
					<div id="dialog_target">
						<h3>Concludi Domanda</h3>
						<div class="dialogPanel">
							<div class="stdMessagePanel">

								<div class="feedWarning">
									<s:if test="sportelloOpen == 'false'">
									<p><strong>Attenzione!<br />Non &egrave; possibile concludere la domanda, lo sportello non &egrave; attivo</strong></p>
									</s:if>
									<s:else>
										<s:if test="numMaxDomandeBandoPresentate == 'true'">
											<p><strong>Attenzione!<br />E' gi&agrave; stato inviato il numero massimo di domande previsto dal bando.<br/>Si intende concludere comunque la domanda?</strong></p>
											<p>La domanda sar&agrave; conclusa e non potr&agrave; pi&ugrave; essere modificata.</p>
										</s:if>
									<s:elseif test="numMaxDomandeSportelloPresentate == 'true'">
										<p><strong>Attenzione!<br />E' gi&agrave; stato inviato il numero massimo di domande previsto dallo sportello.<br/>Si intende concludere comunque la domanda?</strong></p>
										<p>La domanda sar&agrave; conclusa e non potr&agrave; pi&ugrave; essere modificata.</p>									
									</s:elseif>
										<s:else>
											<p>
												<strong>
													Attenzione!<br />
													<%-- Sei sicuro di voler concludere la domanda? --%>
													 Sei sicuro di voler chiudere la domanda?
												</strong>
											</p>
												<p>
													<%-- La domanda sar&agrave; conclusa e non potr&agrave; pi&ugrave; essere modificata. --%>
													Dopo la chiusura, la domanda non potr&agrave; pi&ugrave; essere modificata.
												</p>
										</s:else>
									</s:else>
								</div>
							</div>
						</div><!--/dialogPanel -->

						<div class="commandPanel noSpaceButton">
							<div class="button left">
								<span class="close_js"></span>
							</div>
 							<s:if test="sportelloOpen == 'true'">
							<div class="button right">
								<span >
									<a class="send" id="concludiLaProposta"
									   title="concludi Domanda"
									   href="/@@war.context.name@@/inviaPropostaAction.do?idDomanda=<s:property value='%{status.numProposta}'/>" onclick="showSendingBoxConcludi();">si, prosegui
									</a>
								</span>
							</div>
						</s:if>
						</div>

					</div><!--/dialog_target -->
				</div><!--/id_modaleConfermaConcludi -->
				
				
				<%} else {%>
				<!--  MODALE INIZIO  -->
				<script type="text/javascript">
					function viewModaleConfermaInvio(viewModaleButton_id, modaleConfirm_id) {

						$(function() {
							$("#" + modaleConfirm_id)
								.dialog({
										title: $("#" + viewModaleButton_id).attr("title"),
										width: 600,
										modal: true,
										bgiframe: false,
										resizable: false,
										draggable: false,
										chiudi: false,
										load: function(dialog_target) {
											jQuery("span.close_js", dialog_target).jbutton({
												text : 'no, chiudi',
												callback : function (el) {
													$(el).parents(".ui-dialog-content:eq(0)").dialog("close");
												}
											});
										}
							});
						});
					}
					
					function showSendingBox(){
						$("#id_modaleConfermaInvia").dialog("close");
						$("#loading_box").dialog({
							title: "Invio in corso..",
							width: 600,
							modal: true,
							bgiframe: false,
							resizable: false,
							draggable: false,
							chiudi: false,
							load: function(dialog_target) {
								jQuery("span.close_js", dialog_target).jbutton({
									text : 'no, chiudi',
									callback : function (el) {
										$(el).parents(".ui-dialog-content:eq(0)").dialog("close");
									}
								});
							}
						});
					}
				</script>

				<div id="loading_box" hidden="true">
					<div id="dialog_target">
						<h3>Invio in corso..</h3>
						<div class="dialogPanel">
							<div class="stdMessagePanel">
								<div class="feedLoad"><p>Attendere prego: il sistema sta inviando la domanda...</p></div>
							</div>
						</div> 
					</div>
				</div>
				
				<span>
					<a class="send" href="#" style="dialog-elimina" id="id_confermaInvio" title="Invio Domanda" onclick="viewModaleConfermaInvio( 'id_confermaInvio', 'id_modaleConfermaInvia')">
						Invia
					</a>
				</span>

				<div hidden="true" id="id_modaleConfermaInvia">
					<div id="dialog_target">
						<h3>Invio Domanda</h3>
						<div class="dialogPanel">
							<div class="stdMessagePanel">

								<div class="feedWarning">
								 <s:if test="sportelloOpen == 'false'">
									 <p><strong>Attenzione!<br />Non &egrave; possibile inviare la domanda, lo sportello non &egrave; attivo</strong></p>
								</s:if>
								<s:else>
									<s:if test="numMaxDomandeBandoPresentate == 'true'">
										<p><strong>Attenzione!<br />E' gi&agrave; stato inviato il numero massimo di domande previsto dal bando.<br/>Si intende inviare comunque la domanda?</strong></p>
										<p>La domanda sar&agrave; inviata e non potr&agrave; pi&ugrave; essere modificata.</p>
									</s:if>
									<s:elseif test="numMaxDomandeSportelloPresentate == 'true'">
										<p><strong>Attenzione!<br />E' gi&agrave; stato inviato il numero massimo di domande previsto dallo sportello.<br/>Si intende inviare comunque la domanda?</strong></p>
										<p>La domanda sar&agrave; inviata e non potr&agrave; pi&ugrave; essere modificata.</p>									
									</s:elseif>
									<s:elseif test="bandoMaterializzatoSenzaPEC == 'true'">
										<p><strong>Attenzione!<br />Sei sicuro di voler confermare la domanda?</strong></p>
										<p>La domanda non potr&agrave; pi&ugrave; essere modificata.</p>	
									</s:elseif>
									<s:else>
									<p><strong>Attenzione!<br />Sei sicuro di voler confermare la domanda?</strong></p>
									<p>La domanda non potr&agrave; pi&ugrave; essere modificata.</p>
									<p>Proseguendo con l&apos;invio sar&agrave; creato il PDF definitivo della domanda, da salvare, firmare e inviare tramite PEC (Posta Elettronica Certificata) entro i termini previsti dal bando. </p>
									<p>Solo con l'invio della PEC la domanda risulter&agrave; presentata. </p>
									</s:else>
								</s:else>
								</div>
							</div>
						</div><!--/dialogPanel -->

						<div class="commandPanel noSpaceButton">
							<div class="button left">
								<span class="close_js"></span>
							</div>
 <s:if test="sportelloOpen == 'true'">
							<div class="button right">
								<span >
									<a class="send" id="inviaLaProposta"
									   title="invia Domanda"
									   href="/@@war.context.name@@/inviaPropostaAction.do?idDomanda=<s:property value='%{status.numProposta}'/>" onclick="showSendingBox();">si, prosegui
									</a>
								</span>
							</div>
</s:if>
						</div>

					</div><!--/dialog_target -->
				</div><!--/id_modaleConfermaInvia -->
				<% } %>
				<!-- MODALE FINE -->
			<% } else if (formState != null && (formState.equals("IN") || formState.equals("CO"))) { %>
				<!-- VISUALIZZA IL BOTTONE INVIA DISABILITATO CON LA DATA -->
				<!-- DISABILITARE il BOTTONE DI STAMPA creato da aggregatoreUtil perche farebbe la stampa al volo e non leggerebbe il pdf dal db !!! -->
				<%if (it.csi.findom.findomwebnew.presentation.util.Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(statusInfo.getFlagBandoDematerializzato())){ %>
					<span>
					<input class="stato_conclusa" type="submit" disabled="disabled" id="concludi_proposta" value="Conclusa" title="stato: conclusa" />
					</span>
				<% } else { %>
					<span>
					<input class="stato_inviata" type="submit" disabled="disabled" id="invia_proposta" value="Inviata" title="stato: inviata" />
					</span>
					<% if (statusInfo.getDataTrasmissione() != null) { %>
						data e ora invio: <b><%= new SimpleDateFormat("dd/MM/yyyy HH:mm").format(statusInfo.getDataTrasmissione()) %></b>
					<% } %>
				<% } %>
			<% } else { %>
				<%if (it.csi.findom.findomwebnew.presentation.util.Constants.FLAG_BANDO_DEMATERIALIZZATO.equalsIgnoreCase(statusInfo.getFlagBandoDematerializzato())){ %>
				<span>
					<input class="send" disabled="disabled" type="submit" value="Concludi" title="E' possibile concludere la domanda solamente se la funzionalit&agrave; di verifica non ha riscontrato nessun errore bloccante." />
				</span>				
				<% } else { %>
				<span>
					<input class="send" disabled="disabled" type="submit" value="Invia" title="E' possibile inviare la domanda solamente se la funzionalit&agrave; di verifica non ha riscontrato nessun errore bloccante." />
				</span>
				<% } %>
			<% } %>
		
		</div>
	</a:include4Menu>
</div>

<a:include4Page name="div_page_container_close">
	</div>
</a:include4Page>

<a:include4Menu name="div_menu_container_close">
	</div>
</a:include4Menu>

<%
	StopWatch watchEnd = new StopWatch("domweb");
	watchEnd.start();
%>
<a:end />
<%
	watchEnd.stop();
	watchEnd.dumpElapsed("aggregatoreUtilImpl.jsp", "END TAG", "Renderizzazione", "-");
%>

<%
	stopWatch.stop();
	stopWatch.dumpElapsed("aggregatoreUtilImpl.jsp", "main", "Renderizzazione jsp", "-");
	
	}catch(Exception ex){

		System.out.println("aggregatoreUtilImpl.jsp ------------ ERRORRE GRAVE:"+ex.getMessage());
		//ex.printStackTrace();
		
%>
<!-- INZIO CONTENUTO CENTER PANEL -->
	<div class="pageContainer">
		<h3>Errore</h3>
		<br/>
		Si &egrave; verificato un errore imprevisto.<br/>
		Impossibile ottenere la pagina richiesta.<br/><br/>
				
		<!-- FINE CONTENUTO CENTER PANEL -->
	</div>
<%
	}
%>