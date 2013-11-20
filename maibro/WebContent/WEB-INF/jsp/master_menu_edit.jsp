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
               <li class="headerTitle" ><a href="#">${menu.mode } Master Menu</a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="menu" name="formpost" method="POST" action="${path}/admin/menu/save" cssClass="form" >
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
	                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID Menu</form:label>
	                    <form:errors path="id" cssClass="error" />
	               </div>
                
						<form:hidden path="id" /><input type="text" class="text_field_read" value="${menu.id }" readonly="readonly" />
				 
                  <span class="description"></span>
                </div>
                
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="parent" cssClass="label" cssErrorClass="label labelError">Parent Menu <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="parent" cssClass="error" />
	              </div>
                 <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="parent" /><input type="text" class="text_field_read" value="${menu.parent_nama }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="parent">
								<form:option value="">Silahkan Pilih Parent Menu</form:option>
								<form:options items="${reff.parentMenu}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama Menu <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="nama" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field_read" value="${menu.nama }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="nama"   cssClass="text_field" size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="link" cssClass="label" cssErrorClass="label labelError">Link </form:label>
	                    <form:errors path="link" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="link" /><input type="text" class="text_field_read" value="${menu.link }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="link"  id="target" cssClass="text_field" size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
			  </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="urut" cssClass="label" cssErrorClass="label labelError">Urut <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="urut" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="urut" /><input type="text" class="text_field_read" value="${menu.urut }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="urut"  cssClass="text_field" size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                
                
			  <div class="group navform wat-cf">
     			  <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="id" />
							<button class="button" type="button" onclick="window.location='${path}/admin/menu'">
			                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/admin/menu'">
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
		
		<c:if test="${menu.mode ne 'NEW' }">
		<div class="block">
		 
          	<h3>Others Master Menu</h3>
          	<div class="filter">
          		Filter : <input type="text" class="text-input" id="filter" value="" />
          
	       		 <span class="description" id="filter-count"></span>
	        </div>
	        <div class="navi">
	          <ul class="navigation_filter" id="filterList">
	          	<c:forEach items="${reff.AllMenu}" var="u">
          		 	
		          		<c:choose>
							<c:when test="${menu.mode eq 'VIEW'}">
								<c:choose>
									<c:when test="${u.key eq menu.id}">
										<li id="selected">
											<a href="${path}/admin/menu/view/${u.key}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/admin/menu/view/${u.key}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
							<c:when test="${menu.mode eq 'EDIT'}">
								<c:choose>
									<c:when test="${u.key eq menu.id}">
										<li id="selected">
										<a href="${path}/admin/menu/edit/${u.key}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/admin/menu/edit/${u.key}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
					   </c:choose>
	          	</c:forEach>
	           
	          </ul>
          </div>
           <div class="action">
          	<a href="${path}/admin/menu/new" class="add">Add New</a>
          	<c:if test="${menu.mode eq 'VIEW'}">
          		<a href="${path}/admin/menu/edit/${menu.id }" class="edit">Edit Current</a>
          	</c:if>
	       </div>
                 
        </div>
         </c:if>
	</div>
	
	
	
</body>
</html>		