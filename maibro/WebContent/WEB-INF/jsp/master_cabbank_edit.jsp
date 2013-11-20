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
               <li class="headerTitle" ><a href="#">${mstcabbank.mode } Master Cabang</a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="mstcabbank" name="formpost" method="POST" action="${path}/master/cabbank/save" cssClass="form" >
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
	                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID Cabang</form:label>
	                    <form:errors path="id" cssClass="error" />
	               </div>
                
						<form:hidden path="id" /><input type="text" class="text_field_read" value="${mstcabbank.id }" readonly="readonly" />
				 
                  <span class="description"></span>
                </div>
                
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="mst_bank_id" cssClass="label" cssErrorClass="label labelError">Nama Perusahaan <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="mst_bank_id" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstcabbank.mode eq 'VIEW'}"><form:hidden path="mst_bank_id" /><input type="text" class="text_field_read" value="${mstcabbank.namabank }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="mst_bank_id">
								<form:option value="">Silahkan Pilih Nama Perusahaan</form:option>
								<form:options items="${reff.AllBank}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="jenis" cssClass="label" cssErrorClass="label labelError">Jenis</form:label>
	                    <form:errors path="jenis" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstcabbank.mode eq 'VIEW'}"><form:hidden path="jenis" /><input type="text" class="text_field_read" value="${mstcabbank.jenisName }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="jenis">
								<form:option value="">Silahkan Pilih Jenis</form:option>
								<form:options items="${reff.JenisCabBank}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="cab_induk_id" cssClass="label" cssErrorClass="label labelError">Cabang Induk</form:label>
	                    <form:errors path="cab_induk_id" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstcabbank.mode eq 'VIEW'}"><form:hidden path="cab_induk_id" /><input type="text" class="text_field_read" value="${mstcabbank.namacabinduk }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="cab_induk_id">
								<form:option value="">Silahkan Pilih Cabang Induk</form:option>
								<form:options items="${reff.AllCabBankInduk}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="kode_bank" cssClass="label" cssErrorClass="label labelError">Kode Bank</form:label>
	                    <form:errors path="kode_bank" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstcabbank.mode eq 'VIEW'}"><form:hidden path="kode_bank" /><input type="text" class="text_field_read" value="${mstcabbank.kode_bank }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="kode_bank"  id="target" cssClass="text_field" size="4" maxlength="4"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama Cabang <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="nama" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstcabbank.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field_read" value="${mstcabbank.nama }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
			  </div>
                
			  <div class="group navform wat-cf">
     			  <c:choose>
						<c:when test="${mstcabbank.mode eq 'VIEW'}"><form:hidden path="id" />
							<button class="button" type="button" onclick="window.location='${path}/master/cabbank/${mstcabbank.mst_bank_id}'">
			                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/cabbank/${mstcabbank.mst_bank_id}'">
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
		
		<c:if test="${mstcabbank.mode ne 'NEW' }">
		<div class="block">
		 
          	<h3>Others Master Cabang Bank</h3>
          	<div class="filter">
          		Filter : <input type="text" class="text-input" id="filter" value="" />
          
	       		 <span class="description" id="filter-count"></span>
	        </div>
	        <div class="navi">
	          <ul class="navigation_filter" id="filterList">
	          	<c:forEach items="${reff.AllCabBank}" var="u">
          		 	<c:if test="${u.desc eq mstcabbank.mst_bank_id }">
		          		<c:choose>
							<c:when test="${mstcabbank.mode eq 'VIEW'}">
								<c:choose>
									<c:when test="${u.key eq mstcabbank.id}">
										<li id="selected">
											<a href="${path}/master/cabbank/view/${u.key}/${u.desc}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/master/cabbank/view/${u.key}/${u.desc}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
							<c:when test="${mstcabbank.mode eq 'EDIT'}">
								<c:choose>
									<c:when test="${u.key eq mstcabbank.id}">
										<li id="selected">
										<a href="${path}/master/cabbank/edit/${u.key}/${u.desc}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/master/cabbank/edit/${u.key}/${u.desc}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
					   </c:choose>
					   </c:if>
	          	</c:forEach>
	           
	          </ul>
          </div>
           <div class="action">
          	<a href="${path}/master/cabbank/new/${mstcabbank.mst_bank_id}" class="add">Add New</a>
          	<c:if test="${mstcabbank.mode eq 'VIEW'}">
          		<a href="${path}/master/cabbank/edit/${mstcabbank.id}/${mstcabbank.mst_bank_id}" class="edit">Edit Current</a>
          	</c:if>
	       </div>
                 
        </div>
         </c:if>
        <div class="block">
        	 <h3>Others Link</h3>
	          <ul class="navigation">
	   			<li><a href="${path}/master/bank" >Master Bank</a></li>  
		   		<li><a href="${path}/master/product" >Master Product</a></li>  
	          </ul>
        </div>
        <div class="block notice">
          <h4>Page Hint</h4>
          <p>You can ${mstcabbank.mode } Master Cabang Bank Data </p>
        </div>
	</div>
	
	
	
</body>
</html>		