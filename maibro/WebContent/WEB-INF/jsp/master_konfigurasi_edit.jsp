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
               <li class="headerTitle" ><a href="#">${mstmaster.mode } Master Konfigurasi</a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="mstmaster" name="formpost" method="POST" action="${path}/admin/konfigurasi/save" cssClass="form" >
              	<c:choose>
              		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
              			<div class="flash">
				            <div class="message ${messageType}">
				              <p>${message }</p>
				            </div>
				          </div>
              		</c:when>
              		<c:otherwise>
              			<c:if test="${mstmaster.mode ne 'VIEW'}">
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
	                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID Konfigurasi</form:label>
	                    <form:errors path="id" cssClass="error" />
	               </div>
                  	<c:choose>
						<c:when test="${mstmaster.mode eq 'VIEW'}"><form:hidden path="id" /><input type="text" class="text_field_read" value="${mstmaster.idName }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="id">
								<form:option value="">Silahkan Pilih ID Konfigurasi</form:option>
								<form:options items="${reff.JenisKonfigurasi}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
					</c:choose>
					<form:hidden path="idUpdate" />
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="jenis" cssClass="label" cssErrorClass="label labelError">Jenis Konfigurasi  <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="jenis" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstmaster.mode eq 'VIEW'}"><form:hidden path="jenis" /><input type="text" class="text_field_read" value="${mstmaster.jenis }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="jenis"  id="target" cssClass="text_field" size="5" maxlength="5"/></c:otherwise>
				   </c:choose>
				   <form:hidden path="jenisUpdate" />
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="keterangan" cssClass="label" cssErrorClass="label labelError">Keterangan  <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="keterangan" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstmaster.mode eq 'VIEW'}"><form:hidden path="keterangan" /><input type="text" class="text_field_read" value="${mstmaster.keterangan }" size="50" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="keterangan"  id="target" cssClass="text_field" size="50" maxlength="30"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
			  </div>
                
			  <div class="group navform wat-cf">
     			  <c:choose>
						<c:when test="${mstmaster.mode eq 'VIEW'}"><form:hidden path="id" />
							<button class="button" type="button" onclick="window.location='${path}/admin/konfigurasi'">
			                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/admin/konfigurasi'">
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
		
		<c:if test="${mstmaster.mode ne 'NEW' }">
		<div class="block">
		 
          	<h3>Others Master Konfigurasi</h3>
          	<div class="filter">
          		Filter : <input type="text" class="text-input" id="filter" value="" />
          
	       		 <span class="description" id="filter-count"></span>
	        </div>
	        <div class="navi">
	          <ul class="navigation_filter" id="filterList">
	          	<c:forEach items="${reff.AllKonfigurasi}" var="u">
          		 	
		          		<c:choose>
							<c:when test="${mstmaster.mode eq 'VIEW'}">
								<c:choose>
									<c:when test="${u.key eq mstmaster.id and u.value eq mstmaster.jenis}">
										<li id="selected">
											<a href="${path}/admin/konfigurasi/view/${u.key}/${u.value}" class="selected" id="selected">${u.key} ${u.desc } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/admin/konfigurasi/view/${u.key}/${u.value}">${u.key} ${u.desc }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
							<c:when test="${mstmaster.mode eq 'EDIT'}">
								<c:choose>
									<c:when test="${u.key eq mstmaster.id and u.value eq mstmaster.jenis}">
										<li id="selected">
										<a href="${path}/admin/konfigurasi/edit/${u.key}/${u.value}" class="selected" id="selected">${u.key} ${u.desc } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/admin/konfigurasi/edit/${u.key}/${u.value}">${u.key} ${u.desc }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
					   </c:choose>
	          	</c:forEach>
	           
	          </ul>
          </div>
           <div class="action">
          	<a href="${path}/admin/konfigurasi/new" class="add">Add New</a>
          	<c:if test="${mstmaster.mode eq 'VIEW'}">
          		<a href="${path}/admin/konfigurasi/edit/${mstmaster.id }/${mstmaster.jenis}" class="edit">Edit Current</a>
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
          <p>You can ${mstmaster.mode } Master Konfigurasi Data </p>
        </div>
	</div>
	
	
	
</body>
</html>		