<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
		$(document).ready(function() {
			//set onclick pada checkbox bagian input polis
			if('${claim.mode}'=='AKSEPTASI'){
				$("#statusClaim_1").click( function(){
					$("#jumBayar").show(300);
				});
				
				$("#statusClaim_2").click( function(){
					$("#jumBayar").hide(300);
				});
				
				$("#statusClaim_0").click( function(){
					$("#jumBayar").hide(300);
				});
				
				if('${claim.statusClaim}'==1)$("#jumBayar").show(300);
			}
		});
	</script>
</head>
<body>

		              	
	<div id="main">
		
         
	            <c:choose>
	            	<c:when test="${empty claim.policy_id }">
            		    <form:form commandName="claim" name="formpost" method="POST"  cssClass="form" >
            		    <div class="block" id="block-forms">
				          <div class="secondary-navigation">
				            <ul class="wat-cf">
				               <li class="headerTitle" ><a href="#">${claim.mode } Klaim</a></li> 
				            </ul>
				          </div>
           		     	  <div class="content">
				          
				            <div class="inner">
            		  	  <div class="group">
		               	  <div class="fieldWithErrors">
			                   <form:label path="spaj_no" cssClass="label" cssErrorClass="label labelError">No SPAJ<span class="mandatory" title="Wajib diisi"> *</span></form:label>
			                    <form:errors path="spaj_no" cssClass="error" />
			              </div>
		                  <c:choose>
								<c:when test="${claim.mode eq 'VIEW'}"><form:hidden path="spaj_no" /><input type="text" class="text_field_read" value="${claim.nama }" readonly="readonly" /></c:when>
								<c:otherwise><form:input path="spaj_no"  id="target" cssClass="text_field" size="80" maxlength="80"/></c:otherwise>
						   </c:choose>
		                  <span class="description"></span>
		                </div>
		                 <div class="group navform wat-cf">
		     			  <c:choose>
								<c:when test="${claim.mode eq 'VIEW'}"><form:hidden path="id" />
									<button class="button" type="button" onclick="window.location='${path}/klaim/input'">
					                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
					                  </button>
								</c:when>
								<c:otherwise>
									<button class="button" type="submit" name="cari"  onclick="if(!confirm('Are you sure want to search no spaj?'))return false;">
					                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Cari No SPAJ
					                  </button>
					                  <form:hidden path="mode"/>
					                  <span class="text_button_padding"></span>
					                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/klaim/input'">
					                    <img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
					                  </button>
								</c:otherwise>
						   </c:choose>
		                  
		                </div>
		                            </div>
          </div>
        </div>
		                </form:form>
	            	</c:when>
	            	<c:otherwise>
	            		 <form:form commandName="claim" name="formpost" method="POST" action="${path}/klaim/save" enctype="multipart/form-data"  cssClass="form" >
	            		  <div class="block" id="block-forms">
				          <div class="secondary-navigation">
				            <ul class="wat-cf">
				               <li class="headerTitle" ><a href="#">${claim.mode } Klaim</a></li> 
				            </ul>
				          </div>
           		     	  <div class="content">
				          
				            <div class="inner">
	            		<c:choose>
		              		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
		              			<div class="flash">
						            <div class="message ${messageType}">
						              <p>${message }</p>
						            </div>
						          </div>
		              		</c:when>
		              		<c:otherwise>
		              			<c:if test="${claim.mode ne 'VIEW'}">
			              			<div class="flash">
							            <div class="message notice">
							              <p><fmt:message key="CompleteForm" /> </p>
							            </div>
						            </div>
					           </c:if>
		              		</c:otherwise>
		              	</c:choose>
		                
		              
					  
					  <div class="columns wat-cf">
					  	
				  		 <div class="group">
		                   <div class="fieldWithErrors">
			                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID Klaim</form:label>
			                    <form:errors path="id" cssClass="error" />
			               </div>
		                
								<form:hidden path="id" /><input type="text" class="text_field_read" value="${claim.id }" readonly="readonly" />
						 
		                  <span class="description"></span>
		                </div>
		                
		              	<div class="group">
		               	  <div class="fieldWithErrors">
			                   <form:label path="spaj_no" cssClass="label" cssErrorClass="label labelError">No SPAJ<span class="mandatory" title="Wajib diisi"> *</span></form:label>
			                    <form:errors path="spaj_no" cssClass="error" />
			              </div>
		                 	<form:hidden path="spaj_no" /><input type="text" class="text_field_read" value="${claim.spaj_no }" readonly="readonly" />
								
		                  <span class="description"></span>
		                </div>
		                <div class="group">
		               	  <div class="fieldWithErrors">
			                   <form:label path="namacustomer" cssClass="label" cssErrorClass="label labelError">Debitur<span class="mandatory" title="Wajib diisi"> *</span></form:label>
			                    <form:errors path="namacustomer" cssClass="error" />
			              </div>
			              	<form:hidden path="policy_id" />
		                 	<form:hidden path="namacustomer" /><input type="text" class="text_field_read" value="${claim.namacustomer }" size="50" readonly="readonly" />
								
		                  <span class="description"></span>
		                </div>
		                
		                <div class="group">
		               	  <div class="fieldWithErrors">
			                   <form:label path="jumlah_klaim" cssClass="label" cssErrorClass="label labelError">Jumlah Klaim<span class="mandatory" title="Wajib diisi"> *</span></form:label>
			                    <form:errors path="jumlah_klaim" cssClass="error" />
			              </div>
		                  <c:choose>
								<c:when test="${claim.mode eq 'VIEW'}"><form:hidden path="jumlah_klaim" /><input type="text" class="text_field_read" value="${claim.jumlah_klaim }" readonly="readonly" /></c:when>
								<c:otherwise><form:input path="jumlah_klaim"  id="target" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" size="20"/></c:otherwise>
						   </c:choose>
		                  <span class="description"></span>
		                </div>
		                
		                 <div class="group">
		               	  <div class="fieldWithErrors">
			                   <form:label path="keterangan" cssClass="label" cssErrorClass="label labelError">Keterangan</form:label>
			                    <form:errors path="keterangan" cssClass="error" />
			              </div>
		                  <form:textarea path="keterangan"  id="target" cssClass="text_area" />
		                  <span class="description"></span>
		                </div>
		                
		                 
		                
		                 
		                
		                
		                <div class="group">
		               	  <div class="fieldWithErrors">
			                   <form:label path="uploadFile" cssClass="label" cssErrorClass="label labelError">Upload Dokumen Klaim<span class="mandatory" title="Wajib diisi"> *</span></form:label>
			                   <form:errors path="uploadFile" cssClass="error" /> <br/> 
				              <form:select path="fileCategory" cssErrorClass="inputError">
								<form:option value="">Silahkan Pilih Kategori File</form:option>
								<form:options items="${file_category}" itemValue="value" itemLabel="desc"/>
							  </form:select><form:errors path="spaj_no" cssClass="error" /><br/> 
			             	 <input type="file" class="file_1" name="uploadFile" size="50"/>
		             	    </div>
		                  <span class="description">format *.gif, *.pdf max: 500kb</span>
		                  
		                  
		                  <c:if test="${not empty daftarFileKlaim}">
		                  <br/><br/>
		                  	<label class="label">File Yang sudah di Upload</label>
		                  	<table class="table">
		                  		<tr>
		                  			<th>Kategori File</th>
		                  			<th>Daftar FIle</th>
		                  		</tr>
		                 	 	<c:forEach items="${daftarFileKlaim}" var="v">
					        		<tr>
					        			<td>
					        				${v.desc }
					        			</td>
						        		<td>	
						        			<table style="width: 100%">
							        			<c:forEach items="${v.lsfile}" var="p">
							        				<tr>
								        				<td>${p.key }</td>
								        				<td>
									        				<a href="javascript:popWin('${path }/openfile/2?loc=${p.encrypt}','800px','800px',false,false)" title="view">
										                      <img src="${path }/static/pilu/images/icons/view.png" alt="View" />
										                    </a>
										                    <c:if test="${claim.mode eq\"UPDATE\" }">
											                    <a href="javascript:if(confirm('Are you sure want to Delete \'File : ${p.key}\' ?'))window.location='${path}/klaim/deleteFileKlaim/${claim.id }/${claim.pagename}?loc=${p.encrypt}';" title="delete">
											                      <img src="${path }/static/pilu/images/icons/cross.png" alt="Delete" />
											                   </a>
										                   </c:if>
										                </td>
								                   </tr>
							        		 	</c:forEach>
						        		 	</table>
						        		</td>
					        		</tr>
					        	</c:forEach>
					        </table>
		                  </c:if>
		                </div>
		                
					  </div>
		                
					  <div class="group navform wat-cf">
		     			  <c:choose>
								<c:when test="${claim.mode eq 'VIEW'}"><form:hidden path="id" />
									<button class="button" type="button" onclick="window.location='${path}/klaim/${claim.pagename }'">
					                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
					                  </button>
								</c:when>
								<c:when test="${claim.mode eq 'NEW' or claim.mode eq 'EDIT'}"><form:hidden path="id" />
									<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
					                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
					                  </button>
					                  <form:hidden path="mode"/>
					                     <form:hidden path="pagename"/>
					                  <span class="text_button_padding"></span>
					                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/klaim/${claim.pagename }'">
					                    <img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
					                  </button>
								</c:when>
						   </c:choose>
		                  
		                </div>
		                            </div>
			          </div>
			        </div>
			        
			        <c:if test="${claim.mode eq 'VALIDASI' or claim.mode eq 'AKSEPTASI'}">
		<div class="block" id="block-forms3">

		<div class="secondary-navigation">
			<ul class="wat-cf">
				<li class="headerTitle">
					<a href="#"> ${claim.mode} Polis</a>
				</li>
			</ul>
		</div>
		
		<div class="content">
			<div class="inner">
				<div class="columns wat-cf">
					<c:if test="${claim.mode eq 'VALIDASI'}">
						<br/><br/>
						
						<div class="group">
							
							<div class="fieldWithErrors">
								<form:label path="statusClaim" cssClass="label" cssErrorClass="label labelError">Apakah pengajuan Klaim sudah valid?</form:label>
								<form:errors path="statusClaim" cssClass="error" />
							</div>
							<label for="statusClaim_1">
								<form:radiobutton path="statusClaim" id="statusClaim_1" value="1"/>
								Sudah
							</label>
							<label for="statusClaim_0">
								<form:radiobutton path="statusClaim" id="statusClaim_0"  value="0"/>
								Belum
							</label>
							<span class="description"></span>
						</div>
						 <div class="group navform wat-cf">
							<form:hidden path="id" />
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                     <form:hidden path="pagename"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/klaim/${claim.pagename }'">
			                    <img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                  </button>
		                 </div>
					</c:if>
					
					<c:if test="${claim.mode eq 'AKSEPTASI'}">
						<br/><br/>
						
						<div class="group">
							
							<div class="fieldWithErrors">
								<form:label path="statusClaim" cssClass="label" cssErrorClass="label labelError">Apakah pengajuan Klaim diterima?</form:label>
								<form:errors path="statusClaim" cssClass="error" />
							</div>
							<label for="statusClaim_1">
								<form:radiobutton path="statusClaim" id="statusClaim_1" value="1" />
								diterima
							</label>
							<label for="statusClaim_2">
								<form:radiobutton path="statusClaim" id="statusClaim_2" value="2"/>
								ditolak
							</label>
							<label for="statusClaim_0">
								<form:radiobutton path="statusClaim" id="statusClaim_0"  value="0"/>
								proses ulang
							</label>
							<span class="description"></span>
						</div>
						
						<div class="group" id="jumBayar" style="display: none">
		               	  <div class="fieldWithErrors">
			                   <form:label path="jumlah_bayar" cssClass="label" cssErrorClass="label labelError">Jumlah Bayar</form:label>
			                    <form:errors path="jumlah_bayar" cssClass="error" />
			              </div>
		                  <c:choose>
								<c:when test="${claim.mode eq 'VIEW'}"><form:hidden path="jumlah_bayar" /><input type="text" class="text_field_read" value="${claim.jumlah_bayar }" readonly="readonly" /></c:when>
								<c:otherwise><form:input path="jumlah_bayar"  id="target" cssClass="text_field" size="20"/></c:otherwise>
						   </c:choose>
		                  <span class="description"></span>
		                </div>
						 <div class="group navform wat-cf">
							<form:hidden path="id" />
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                     <form:hidden path="pagename"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/klaim/${claim.pagename }'">
			                    <img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                  </button>
		                 </div>
					</c:if>
				</div>
			</div>
		</div>
	
	</div>
	</c:if>
							</form:form>
	            	</c:otherwise>
	            </c:choose>

        
	</div>
	
	
	

	<c:if test="${not empty daftarFile }">
	<div id="sidebar">
		<div class="block">
          <h3>Download Form</h3>
          <div style="height: 100px;overflow: auto;">
        	<table class="table">
        		
	        	<c:forEach items="${daftarFile}" var="v">
	        		<tr>
		        		<td>	${v.key }
		        		 <br/><span  class="desc"> <a href="javascript:popWin('${path }/openfile/1?loc=${v.encrypt}','800px','800px',false,false)" >Download File</a> </span></td>
	        		</tr>
	        	</c:forEach>
	        	
        	</table>
        	</div>
        </div>

        <div class="block notice" >
          <h4>Syarat Pengajuan Klaim</h4>
          <div style="height: 200px;overflow: auto;padding-left: 20px;">
	          <ol>
	          		<c:forEach items="${kelengkapan_klaim}" var="v">
	          			<li>${v.desc }</li>
	          		</c:forEach>
	          </ol>
          </div>
        </div>
        
        
	</div>
	
	</c:if>
	
</body>
</html>		