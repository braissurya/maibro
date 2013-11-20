<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>

	<div id="main">
		<div class="block" id="block-forms">
          <div class="secondary-navigation">
            <ul class="wat-cf">
               <li class="headerTitle" ><a href="#">${mstbank.mode } Master Perusahaan</a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="mstbank" name="formpost" method="POST" action="${path}/master/bank/save" enctype="multipart/form-data" cssClass="form" >
              	<c:choose>
              		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
              			<div class="flash">
				            <div class="message ${messageType}">
				              <p>${message }</p>
				            </div>
				          </div>
              		</c:when>
              		<c:otherwise>
              			<c:if test="${lstconsolidation.mode ne 'VIEW'}">
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
	                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID</form:label>
	                    <form:errors path="id" cssClass="error" />
	               </div>
                
						<form:hidden path="id" /><input type="text" class="text_field_read" value="${mstbank.id }" readonly="readonly" />
				 
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="jenis" cssClass="label" cssErrorClass="label labelError">Jenis <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="jenis" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="jenis" /><input type="text" class="text_field_read" value="${mstbank.jenisName }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="jenis">
								<form:option value="">Silahkan Pilih Jenis</form:option>
								<form:options items="${reff.JenisBank}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama Perusahaan <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="nama" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field_read" value="${mstbank.nama }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="polis_induk" cssClass="label" cssErrorClass="label labelError">Polis Induk</form:label>
	                    <form:errors path="polis_induk" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="polis_induk" /><input type="text" class="text_field_read" value="${mstbank.polis_induk }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="polis_induk"  id="target" cssClass="text_field" /></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>      
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="email" cssClass="label" cssErrorClass="label labelError">Email</form:label>
	                    <form:errors path="email" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="email" /><input type="text" class="text_field_read" value="${mstbank.email }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="email"  id="target" cssClass="text_field" size="40" maxlength="100"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>      
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="diskon_premi" cssClass="label" cssErrorClass="label labelError">Diskon Premi</form:label>
	                    <form:errors path="diskon_premi" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="diskon_premi" /><input type="text" class="text_field_read nominal" value="${mstbank.diskon_premi }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="diskon_premi"  id="target" cssClass="text_field nominal" size="5"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="diskon_brokerage" cssClass="label" cssErrorClass="label labelError">Brokerage %</form:label>
	                    <form:errors path="diskon_brokerage" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="diskon_brokerage" /><input type="text" class="text_field_read nominal" value="${mstbank.diskon_brokerage }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="diskon_brokerage"  id="target" cssClass="text_field nominal" size="5"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="diskon_komisi" cssClass="label" cssErrorClass="label labelError">Diskon Komisi</form:label>
	                    <form:errors path="diskon_komisi" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="diskon_komisi" /><input type="text" class="text_field_read nominal" value="${mstbank.diskon_komisi }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="diskon_komisi"  id="target" cssClass="text_field nominal" size="5"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="ppn_premi" cssClass="label" cssErrorClass="label labelError">PPN Premi</form:label>
	                    <form:errors path="ppn_premi" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="ppn_premi" /><input type="text" class="text_field_read nominal" value="${mstbank.ppn_premi }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="ppn_premi"  id="target" cssClass="text_field nominal" size="5"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="pph_premi" cssClass="label" cssErrorClass="label labelError">PPH Premi</form:label>
	                    <form:errors path="pph_premi" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="pph_premi" /><input type="text" class="text_field_read nominal" value="${mstbank.pph_premi }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="pph_premi"  id="target" cssClass="text_field nominal" size="5"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="pph_bpr" cssClass="label" cssErrorClass="label labelError">PPH Bank</form:label>
	                    <form:errors path="pph_bpr" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="pph_bpr" /><input type="text" class="text_field_read nominal" value="${mstbank.pph_bpr }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="pph_bpr"  id="target" cssClass="text_field nominal" size="5"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="pph_agent" cssClass="label" cssErrorClass="label labelError">PPH Agent</form:label>
	                    <form:errors path="pph_agent" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="pph_agent" /><input type="text" class="text_field_read nominal" value="${mstbank.pph_agent }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="pph_agent"  id="target" cssClass="text_field nominal" size="5"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="uploadFile" cssClass="label" cssErrorClass="label labelError">Upload Logo:</form:label>
	                    <form:errors path="uploadFile" cssClass="error" />
	              </div>
	              <input type="file" class="file_1" name="uploadFile" size="50"/>
             	 
                  <span class="description">format *.gif, max: 500kb</span>
                  <c:if test="${not empty mstbank.id}">
                  	<br/>
                  		<br/>
                 	 <img alt="logo perusahaan" src="${path }/imagegenerator/inline/logo/${mstbank.id}.gif"  width="200px">
                  </c:if>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="uploadFileTTD" cssClass="label" cssErrorClass="label labelError">Upload Tanda Tangan:</form:label>
	                    <form:errors path="uploadFileTTD" cssClass="error" />
	              </div>
	              <input type="file" class="file_1" name="uploadFileTTD" size="50"/>
             	 
                  <span class="description">format *.gif, max: 500kb</span>
                  <c:if test="${not empty mstbank.id}">
                  	<br/>
                  		<br/>
                 	 <img alt="logo perusahaan" src="${path }/imagegenerator/inline/logo/${mstbank.id}_ttd.gif"  width="200px"> 
                  </c:if>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="uploadFileCap" cssClass="label" cssErrorClass="label labelError">Upload Cap:</form:label>
	                    <form:errors path="uploadFileCap" cssClass="error" />
	              </div>
	              <input type="file" class="file_1" name="uploadFileCap" size="50"/>
             	 
                  <span class="description">format *.gif, max: 500kb</span>
                  <c:if test="${not empty mstbank.id}">
                  	<br/>
                  		<br/>
                 	 <img alt="logo perusahaan" src="${path }/imagegenerator/inline/logo/${mstbank.id}_cap.gif"  width="200px"> 
                  </c:if>
                </div>
                
			  </div>
                
			  <div class="group navform wat-cf">
     			  <c:choose>
						<c:when test="${mstbank.mode eq 'VIEW'}"><form:hidden path="id" />
							<button class="button" type="button" onclick="window.location='${path}/master/bank'">
			                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/bank'">
			                    <img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                  </button>
						</c:otherwise>
				   </c:choose>
                  
                </div>
			
			
		
			
			
		</form:form>		
                
               
            </div>
          </div>
        </div>
	
		
		
		
		
		
	</div>
	<div id="sidebar">
		
		<c:if test="${mstbank.mode ne 'NEW' }">
		<div class="block">
		 
          	<h3>Others Master Perusahaan</h3>
          	<div class="filter">
          		Filter : <input type="text" class="text-input" id="filter" value="" />
          
	       		 <span class="description" id="filter-count"></span>
	        </div>
	        <div class="navi">
	          <ul class="navigation_filter" id="filterList">
	          	<c:forEach items="${reff.AllBank}" var="u">
          		 	
		          		<c:choose>
							<c:when test="${mstbank.mode eq 'VIEW'}">
								<c:choose>
									<c:when test="${u.key eq mstbank.id}">
										<li id="selected">
											<a href="${path}/master/bank/view/${u.key}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/master/bank/view/${u.key}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
							<c:when test="${mstbank.mode eq 'EDIT'}">
								<c:choose>
									<c:when test="${u.key eq mstbank.id}">
										<li id="selected">
										<a href="${path}/master/bank/edit/${u.key}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/master/bank/edit/${u.key}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
					   </c:choose>
	          	</c:forEach>
	           
	          </ul>
          </div>
           <div class="action">
          	<a href="${path}/master/bank/new" class="add">Add New</a>
          	<c:if test="${mstbank.mode eq 'VIEW'}">
          		<a href="${path}/master/bank/edit/${mstbank.id }" class="edit">Edit Current</a>
          	</c:if>
	       </div>
                 
        </div>
         </c:if>
        <div class="block">
        	 <h3>Others Link</h3>
	          <ul class="navigation">
	   			<li><a href="${path}/master/bank" >Master Perusahaan</a></li>  
		   		<li><a href="${path}/master/product" >Master Product</a></li> 
	          </ul>
        </div>
        <div class="block notice">
          <h4>Page Hint</h4>
          <p>You can ${mstbank.mode } Master Perusahaan Data </p>
        </div>
	</div>
	
	
	
</body>
</html>		