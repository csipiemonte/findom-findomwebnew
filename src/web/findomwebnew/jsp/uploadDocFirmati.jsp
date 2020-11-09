<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page import="it.csi.util.performance.StopWatch"%>
<%@page import="it.csi.findom.findomwebnew.presentation.util.NumericDescCellComparator"%>
<%@page import="it.csi.findom.findomwebnew.presentation.util.Constants"%>

<%@page import="it.csi.findom.findomwebnew.dto.serviziFindomWeb.ShellDocumentoIndexDto"%>


<%
StopWatch stopWatch = new StopWatch("findomwebnew");
stopWatch.start();

it.csi.findom.findomwebnew.presentation.vo.StatusInfo statusInfo = (it.csi.findom.findomwebnew.presentation.vo.StatusInfo)session.getAttribute(Constants.STATUS_INFO);

java.util.ArrayList<ShellDocumentoIndexDto> listaDocFirmati = (java.util.ArrayList<ShellDocumentoIndexDto>)session.getAttribute("listaDocFirmati");

if (listaDocFirmati!=null){	
	ShellDocumentoIndexDto doc = listaDocFirmati.get(0);
} 

			%>
<r:include url="${layoutInclude.portalHead}" resourceProvider="sp" />

<% //l'import di questi due javascript sembra NON servire a nulla %>

<script type="text/javascript" src="/ris/resources/application/finanziamenti/findomweb/js/ricercaDomande.js"></script>
<script type="text/javascript" src="/ris/resources/application/finanziamenti/findomweb/js/ricercaDomandeINS.js"></script>

</head>
<body>

    <!-- PAGE (OBBLIGATORIO) -->
	<div id="page">
		<r:include url="${layoutInclude.portalHeader}" resourceProvider="sp" />
     
		<!-- APPICATION AREA (OBBLIGATORIO) -->
      	<div id="applicationArea">
          
        <!--area userInfoPanel (dati utente + esci)-->
        <r:include url="${layoutInclude.applicationHeader}" resourceProvider="sp" />
        
        <!-- FINE HEADER (parte comune a tutto l'applicativo) -->

        <!-- PANNELLO DEI CONTENUTI -->
        <div id="contentPanel">
        <!--area menu verticale-->

        	<div id="northPanel">
            	<div class="wrapper">

              	<!-- INIZIO CONTENUTO NORTH PANEL -->

              	<!--inizio filo arianna + link help e contatti-->
              	<div id="contextPanel">
                <div id="breadCrumbPanel">
                  <span class="element">Sistemapiemonte</span>
                  <span class="separator"> &raquo; </span>
                  <span id="currentElement" class="active">gestione delle domande</span>
                </div>
                <r:include url="${layoutInclude.applicationLinkHelpContact}" resourceProvider="sp" />
              	</div>

              	<!--fine filo arianna + link help e contatti-->
              	<!--area searchBox-->

              	<!--inizio barra utente di applicativo-->
              	<!--inizio userInfoPanel (dati utente + esci)-->
              	<s:include value="include/userDataPanel.jsp" />
              	<!--fine userInfoPanel -->
              	<!--fine barra utente di applicativo-->

				<!-- stdMessagePanel CON MESSAGGI [- APPARE SOLO QUANDO NECESSARIO -] -->
				<div class="stdMessagePanel allPage">			
			    <div class="feedWarning">
			    <p><strong>Attenzione!</strong></p>
			    <p>Questo applicativo non gestisce le funzioni indietro e avanti del browser, si prega di non usarle.</p>
			    </div>
				</div>
				<!-- FINE stdMessagePanel -->
        
            	<!--inizio menu dropdown-->
            	<div class="menuDropdown">
					<ul class="sf-menu sf-navdrop sf-navdrop-simple">                  		
                  		<li class="">
						<a href="/@@war.context.name@@/cercaDomande.do">Home</a>
						</li>                  		
                  		<li class="disattivo"><span>Indice</span></li>
                	</ul>
           		</div>
            	<!--fine menu dropdown-->

            	<!-- FINE CONTENUTO NORTH PANEL -->
          		</div>
          </div><!--/northPanel-->

          <div id="CenterWrapper" class="floatWrapper">
            <div id="centerPanel">

              <!-- INZIO CONTENUTO CENTER PANEL -->
              <div class="wrapper">

                <!--INIZIO PARAMETRI 1 / elenco risultati -->

                <div class="stdMessagePanel" id="elencoZeroRisultati">
                  <s:if test="hasActionMessages()">
                    <div class="feedCorrect">
                      <s:actionmessage />
                    </div>
                  </s:if>
                  <s:if test="hasActionErrors()">
                    <div class="feedKo">
                      <s:actionerror />
                    </div>
                  </s:if>
                </div>				

				<div style="display:none" id="id_modale_uploadFile">
				 	<div id="dialog_target">
						<h3>Upload File</h3>
						<div class="dialogPanel">
							<div class="stdMessagePanel"><div class="feedInfo"><p>Aggiungere un file di dimensione massima pari a 5Mb. </p></div></div>
							<form method="post" id="idUpldfile" action="/@@war.context.name@@/caricaDocFirmato.do" enctype="multipart/form-data">
							<table class="formTable" summary="dati di riepilogo">
								<colgroup>
								<col width="20%" />
								<col width="80%" />
								</colgroup>
								<tr>
									<th scope="row"><label>Upload domanda</label></th>
									<td>
									<input type="file" name="upFile" value="" class="fileBrowser medium" id="id_upload_file"/>
									</td>
								</tr>		
							</table>							
							<input type="submit" id="id_upload_fileWO" name="carica" value="Upload" hidden="hidden" />
							</form><br/>
						</div>
						
						<div class="commandPanel noSpaceButton">
							<div class="button left">
								<span>
								<input type="submit" value="Torna indietro" id="id_upload_button" onclick="confirm_close_modale_updateWO()" />
								</span>
							</div>
							<div class="button right">
								<span class="go highlighted">
								<input type="submit" value="Invia" id="id_upload_button" onclick="confirm_modale_updateWO()" />
								</span>
							</div>
						</div>
					</div>
				</div>

				<h4 class="caption">
                  <span id="toggle_handle_01" class="toggle_handle collapse numTotTitle">
                    Gestione della domanda firmata
                  </span>
                  <span class="numTot">
                    <s:property value="domandeSalvateContate"/>
                  </span>
                </h4>
				
				<div class="content_caption toggle_target_01">					
				
					
                <br/><br/>
                <s:form action="cercaDomande" method="post">
                    
               	<!--INIZIO RISULTATI -->
               	
               
               	
               	<s:if test="listaDocFirmati == null || listaDocFirmati.size()==0">
               	
                    <h4>Upload della domanda n&deg; <%= statusInfo.getNumProposta()+"" %> firmata</h4>

                    <!--INIZIO NON CI SONO RISULTATI -->
                    <div class="stdMessagePanel" id="elencoZeroRisultati"> 
                      <div class="feedInfo">
                        <p>Non ci sono elementi da visualizzare</p>
                      </div>
                    </div>
                    <!--FINE NON CI SONO RISULTATI -->
               	</s:if> 
               		<s:elseif test="listaDocFirmati != null && listaDocFirmati.size() > 0">
                                             
                	<div id="elencoRisultati">
					<display:table name="listaDocFirmati" uid="shellDocumentoIndexDto" pagesize="5" requestURI="" keepStatus="true" 
						clearStatus="${param['trova'] != null}" class="myovertable tablesorter" defaultsort="2" defaultorder="ascending">
				
						<s:url action="caricaAllegati" id="urlCaricaAllegati">
							<s:param name="viewedoc" value="%{#attr.shellDocumentoIndexDto.idDocumentoIndex}" />
						</s:url>
						<display:column title="Nome documento" sortable="true" headerClass="sortable" headerScope="col" >	
							<s:a href="%{urlCaricaAllegati}"><s:property value="#attr.shellDocumentoIndexDto.nomeFile" /></s:a>													
						</display:column>			

						<display:column title="Data invio" sortable="true" headerClass="sortable" headerScope="col" >
							  
                         	<s:if test="domanda.dtInvioDomanda != null">
                         	<s:property value="domanda.dtInvioDomanda" />
                         	</s:if>
                         	<s:else>
                         		-	
                         	</s:else>
                         
						</display:column>
						
					</display:table>
                    
            		</div> <!-- elencoRisultati-->                         
             
				 </s:elseif>
                    
                <br/><br/>    
                                  
				<div class="commandPanel">
	                  
					<div class="button left">
                        <span>
                        	<s:submit value="Indietro"/>                        	
                        </span>

                        <s:if test="%{#session.statusInfo.statoProposta=='CO'}">		                       
                        <span class="highlighted">
							   <a href="#" class="uploadFile4" id="id_uploadDoc" title="Aggiungi documento firmato" onclick="openModaleUploadWO()" >
									<span class="nascosto">Aggiungi documento firmato</span>
									Aggiungi documento firmato
								</a>
						</span>   
						</s:if>                     
                        <!-- span class="highlighted"><s:submit type="submit" name="invia" id="cerca" value="Invia" tabindex="8"/></span> -->
					</div>
				</div>
                    
      			</s:form>
      			 
<script type="text/javascript">
							
function openModaleUploadWO() {
						
	$("#id_modale_uploadFile").dialog({
		title: "Selezione domanda firmata",
		width: 600,
		modal: true,
		bgiframe: false,
		resizable: true,
		draggable: true,
		chiudi: false	
	});
}

function confirm_close_modale_updateWO(){
	//$('body').find("#id_modale_uploadFile").remove();
	$("#id_modale_uploadFile").dialog("destroy");
	$("#id_modale_uploadFile").dialog("close");
}
					
function confirm_modale_updateWO(){						
						
	$("#id_modale_uploadFile").dialog("close");
	//$("#id_upload_fileWO").click();
	$("#id_upload_fileWO").click();
}									
						
</script>
<!--                     
   					<s:form action="uploadDocFirmati" method="post">  
                  	</s:form>
-->
				</div> <!-- content caption -->

              </div>
              <!-- FINE CONTENUTO CENTER PANEL -->
            </div>
          </div>
        </div><!--#contentPanel-->

        <!-- FINE PANNELLO DEI CONTENUTI -->
        <!-- footer (con remincl) (parte comune a tutto l'applicativo)  -->
        <r:include url="${layoutInclude.portalFooter}" resourceProvider="sp" />
      </div>
      <!-- FINE APPICATION AREA (OBBLIGATORIO) -->

    </div>
    <!-- FINE PAGE (OBBLIGATORIO) -->
<%
stopWatch.stop();
stopWatch.dumpElapsed("gestisciDOmande.jsp", "main", "Renderizzazione jsp", "-");
%>
  </body>
</html>