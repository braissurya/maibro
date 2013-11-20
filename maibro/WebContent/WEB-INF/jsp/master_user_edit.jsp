<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
	$(document).ready(function() {
		autoPopulateSelect("${path}", "bank", "#mst_bank", "#mst_cab_bank", true, "${user.cab_bank_id}", "");
		$("#mst_bank").change();
	});
</script>
</head>
<body>

	<div id="main">
		<div class="block" id="block-forms">
          <div class="secondary-navigation">
            <ul class="wat-cf">
               <li class="headerTitle" ><a href="#">${user.mode } Master User </a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="user" name="formpost" method="POST" action="${path}/admin/user/save" cssClass="form" >
              	<c:choose>
              		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
              			<div class="flash">
				            <div class="message ${messageType}">
				              <p>${message }</p>
				            </div>
				          </div>
              		</c:when>
              		<c:otherwise>
              			<c:if test="${user.mode ne 'VIEW'}">
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
	                   <form:label path="group_menu_id"  cssClass="label" cssErrorClass="label labelError">Group Menu<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                     <form:errors path="group_menu_id" cssClass="error" />
	               </div>
                		<c:choose>
                			<c:when test="${groupmenuid eq\"-1\"}">
                				<c:choose>
									<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="group_menu_id" /><input type="text" class="text_field_read" value="${user.namagroup }" readonly="readonly" /></c:when>
									<c:otherwise>
										<form:select path="group_menu_id">
											<form:option value="">Silahkan Pilih Group Menu</form:option>
											<form:options items="${reff.AllGroupMenu}" itemValue="key" itemLabel="value"/>
										</form:select>
									</c:otherwise>
							   </c:choose>
                			</c:when>
                			<c:otherwise>
                				<form:hidden path="group_menu_id" /><input type="text" class="text_field_read" value="${groupmenuName}" readonly="readonly" size="20"/>
                			</c:otherwise>
                		</c:choose>
						
				 
                  <span class="description"></span>
                </div>
			 
		  		 <div class="group">
                   <div class="fieldWithErrors">
	                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID User</form:label>
	                    <form:errors path="id" cssClass="error" />
	               </div>
                
						<form:hidden path="id" /><input type="text" class="text_field_read" value="${user.id }" readonly="readonly" size="4"/>
				 
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="username" cssClass="label" cssErrorClass="label labelError">User Name <span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="username" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="username" /><input type="text" class="text_field_read" value="${user.username }" readonly="readonly" size="5"/></c:when>
						<c:otherwise><form:input path="username"  id="target" cssClass="text_field" size="50" /></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                <div class="group">
					<div class="fieldWithErrors">
						<form:label path="bank_id" cssClass="label" cssErrorClass="label labelError">Perusahaan <span class="mandatory" title="Wajib diisi"> *</span></form:label>
						<form:errors path="bank_id" cssClass="error" />
					</div>
					<form:select id="mst_bank" path="bank_id" cssErrorClass="inputError">
						<form:option value=""></form:option>
						<form:options items="${reff.listBank}" itemValue="key" itemLabel="value"/>
					</form:select>
				</div>
				
				<div class="group">
					<div class="fieldWithErrors">
						<form:label path="cab_bank_id" cssClass="label" cssErrorClass="label labelError">Cabang</form:label>
						<form:errors path="cab_bank_id" cssClass="error" />
					</div>
					<form:select id="mst_cab_bank" path="cab_bank_id" cssErrorClass="inputError"/>

				</div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="password" cssClass="label" cssErrorClass="label labelError">Password</form:label>
	                    <form:errors path="password" cssClass="error" />
	              </div>
                	
						<form:hidden path="password" />
						<form:hidden path="passdecrypt" />
						<c:choose>
						<c:when test="${user.passdecrypt eq \"123BCD\" or empty user.password }">
							<input type="text" class="text_field_read" value="default : 123BCD" readonly="readonly" size="20"/>
						</c:when>
						<c:otherwise>
							<input type="text" class="text_field_read" value="**********" readonly="readonly" size="20"/>
						</c:otherwise>
					</c:choose>
						
                  <span class="description"></span>
                </div>
                
                <div class="group">
					<div class="fieldWithErrors">
						<form:label path="mst_product_id" cssClass="label" cssErrorClass="label labelError">Group Product </form:label>
						<form:errors path="mst_product_id" cssClass="error" />
					</div>
					<form:select path="mst_product_id" cssErrorClass="inputError">
						<form:option value="">ALL</form:option>
						<form:options items="${reff.AllGroupProduct}" itemValue="key" itemLabel="value"/>
					</form:select>
				</div>
                
			  </div>
                
               
                
			  <div class="group navform wat-cf">
     			  <c:choose>
						<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="id" />
							<button class="button" type="button" onclick="window.location='${path}/admin/user/${groupmenuid }'">
			                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                     <input type="hidden" value="${groupmenuid}" name="groupid">
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/admin/user/${groupmenuid }'">
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
		
		<c:if test="${user.mode ne 'NEW' }">
		<div class="block">
		 
          	<h3>Others Master User</h3>
          	<div class="filter">
          		Filter : <input type="text" class="text-input" id="filter" value="" />
          
	       		 <span class="description" id="filter-count"></span>
	        </div>
	        <div class="navi">
	          <ul class="navigation_filter" id="filterList">
	          	<c:forEach items="${reff.AllUser}" var="u">
          		 		<c:if test="${u.desc eq user.group_menu_id }">
		          		<c:choose>
							<c:when test="${user.mode eq 'VIEW'}">
								
								<c:choose>
									<c:when test="${u.key eq user.id}">
										<li id="selected">
											<a href="${path}/admin/user/view/${u.key}/${u.desc }" class="selected" id="selected"> ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/admin/user/view/${u.key}/${u.desc }"> ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
							<c:when test="${user.mode eq 'EDIT'}">
								<c:choose>
									<c:when test="${u.key eq user.id}">
										<li id="selected">
										<a href="${path}/admin/user/edit/${u.key}/${u.desc }" class="selected" id="selected"> ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/admin/user/edit/${u.key}/${u.desc }"> ${u.value }</a>
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
          	<a href="${path}/admin/user/new/${groupmenuid }" class="add">Add New</a>
          	<c:if test="${user.mode eq 'VIEW'}">
          		<a href="${path}/admin/user/edit/${user.id }/${groupmenuid }" class="edit">Edit Current</a>
          	</c:if>
	       </div>
                 
        </div>
         </c:if>
        <div class="block">
        	 <h3>Others Link</h3>
	          <ul class="navigation">
	   			        
	          </ul>
        </div>
        <div class="block notice">
          <h4>Page Hint</h4>
          <p>You can ${user.mode } Master User Data </p>
        </div>
	</div>
	
	
	
</body>
</html>		