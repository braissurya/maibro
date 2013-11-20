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
               <li class="headerTitle" ><a href="#">${groupmenu.mode } Master Group Menu</a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="groupmenu" name="formpost" method="POST" action="${path}/admin/groupmenu/save" cssClass="form" >
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
	                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID Group Menu</form:label>
	                    <form:errors path="id" cssClass="error" />
	               </div>
                
						<form:hidden path="id" /><input type="text" class="text_field_read" value="${groupmenu.id }" readonly="readonly" />
				 
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="nama" cssClass="label"  cssErrorClass="label labelError">Nama  Group Menu <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="nama" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${groupmenu.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field_read" value="${groupmenu.nama }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                <table class="table">
                	<tr>
                		<th class="first"></th>
                		<th>ID</th>
						<th>Parent</th>
						<th>Menu</th>
						<th>Link</th>
						<th>Akses</th>
			
						<th class="last">
								
						</th>
                	</tr>
                	<c:forEach items="${groupmenu.menu}" var="p" varStatus="vs">
					<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
						<td></td>
						<td>${p.id}
							<form:hidden path="menu[${vs.index}].id"/>
						</td>
						<td>${p.parent_nama}
							<form:hidden path="menu[${vs.index}].parent_nama"/>
						</td>
						<td>${p.nama}
							<form:hidden path="menu[${vs.index}].nama"/>
						</td>
						<td>${p.link}
							<form:hidden path="menu[${vs.index}].link"/>
						</td>
						<td>
							<form:checkbox path="menu[${vs.index}].akses"/>
						</td>
						
						<td class="last">
								
						</td>
					</tr>
					</c:forEach>
				</table> 
			  </div>
                
                
			  <div class="group navform wat-cf">
     			  <c:choose>
						<c:when test="${groupmenu.mode eq 'VIEW'}"><form:hidden path="id" />
							<button class="button" type="button" onclick="window.location='${path}/admin/groupmenu'">
			                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/admin/groupmenu'">
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
		
		<c:if test="${groupmenu.mode ne 'NEW' }">
		<div class="block">
		 
          	<h3>Others Master Group Menu</h3>
          	<div class="filter">
          		Filter : <input type="text" class="text-input" id="filter" value="" />
          
	       		 <span class="description" id="filter-count"></span>
	        </div>
	        <div class="navi">
	          <ul class="navigation_filter" id="filterList">
	          	<c:forEach items="${reff.AllGroupMenu}" var="u">
          		 	
		          		<c:choose>
							<c:when test="${groupmenu.mode eq 'VIEW'}">
								<c:choose>
									<c:when test="${u.key eq groupmenu.id}">
										<li id="selected">
											<a href="${path}/admin/groupmenu/view/${u.key}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/admin/groupmenu/view/${u.key}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
							<c:when test="${groupmenu.mode eq 'EDIT'}">
								<c:choose>
									<c:when test="${u.key eq groupmenu.id}">
										<li id="selected">
										<a href="${path}/admin/groupmenu/edit/${u.key}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/admin/groupmenu/edit/${u.key}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
					   </c:choose>
	          	</c:forEach>
	           
	          </ul>
          </div>
           <div class="action">
          	<a href="${path}/admin/groupmenu/new" class="add">Add New</a>
          	<c:if test="${groupmenu.mode eq 'VIEW'}">
          		<a href="${path}/admin/groupmenu/edit/${groupmenu.id }" class="edit">Edit Current</a>
          	</c:if>
	       </div>
                 
        </div>
         </c:if>
	</div>
	
	
	
</body>
</html>		