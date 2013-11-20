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
               <li class="headerTitle" ><a href="#">${mstrate.mode } Master Rate </a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="mstrate" name="formpost" method="POST" action="${path}/master/rate/save" cssClass="form" >
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
	                   <form:label path="mst_product_id"  cssClass="label" cssErrorClass="label labelError">Produk Name<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    
	               </div>
                
						<form:hidden path="mst_product_id" /><input type="text" class="text_field_read" value="${prodName}" readonly="readonly" size="20"/>
				 
                  <span class="description"></span>
                </div>
			 
		  		 <div class="group">
                   <div class="fieldWithErrors">
	                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID Rate</form:label>
	                    <form:errors path="id" cssClass="error" />
	               </div>
                
						<form:hidden path="id" /><input type="text" class="text_field_read" value="${mstrate.id }" readonly="readonly" size="4"/>
				 
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="tenor" cssClass="label" cssErrorClass="label labelError">Tenor<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="tenor" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstrate.mode eq 'VIEW'}"><form:hidden path="tenor" /><input type="text" class="text_field_read" value="${mstrate.tenor }" readonly="readonly" size="5"/></c:when>
						<c:otherwise><form:input path="tenor"  id="target" cssClass="text_field" size="5" /></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="umur" cssClass="label" cssErrorClass="label labelError">Umur</form:label>
	                    <form:errors path="umur" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstrate.mode eq 'VIEW'}"><form:hidden path="umur" /><input type="text" class="text_field_read" value="${mstrate.umur }" readonly="readonly" size="5"/></c:when>
						<c:otherwise>
							<form:input path="umur"  cssClass="text_field" size="5" />
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
			  </div>
                
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="jenis_bangunan" cssClass="label" cssErrorClass="label labelError">Jenis Bangunan</form:label>
	                    <form:errors path="jenis_bangunan" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstrate.mode eq 'VIEW'}"><form:hidden path="jenis_bangunan" /><input type="text" class="text_field_read" value="${mstrate.jenis_bangunanNama }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="jenis_bangunan">
								<form:option value="">Silahkan Pilih Jenis Bangunan</form:option>
								<form:options items="${reff.JenisBangunan}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="jenis_rate" cssClass="label" cssErrorClass="label labelError">Jenis Rate<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="jenis_rate" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstrate.mode eq 'VIEW'}"><form:hidden path="jenis_rate" /><input type="text" class="text_field_read" value="${mstrate.jenis_rateNama }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="jenis_rate">
								<form:option value="">Silahkan Pilih Jenis Rate</form:option>
								<form:options items="${reff.JenisRate}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
               
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="rate" cssClass="label" cssErrorClass="label labelError">Rate<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="rate" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstrate.mode eq 'VIEW'}"><form:hidden path="rate" /><input type="text" class="text_field_read nominal" value="${mstrate.rate }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="rate"  id="target" cssClass="text_field nominal" size="10"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
			  <div class="group navform wat-cf">
     			  <c:choose>
						<c:when test="${mstrate.mode eq 'VIEW'}"><form:hidden path="id" />
							<button class="button" type="button" onclick="window.location='${path}/master/rate/${mstrate.mst_product_id}'">
			                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/rate/${mstrate.mst_product_id}'">
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
		
		<c:if test="${mstrate.mode ne 'NEW' }">
		<div class="block">
		 
          	<h3>Others Master Rate</h3>
          	<div class="filter">
          		Filter : <input type="text" class="text-input" id="filter" value="" />
          
	       		 <span class="description" id="filter-count"></span>
	        </div>
	        <div class="navi">
	          <ul class="navigation_filter" id="filterList">
	          	<c:forEach items="${reff.AllRate}" var="u">
          		 	<c:if test="${mstrate.mst_product_id eq u.value}">
		          		<c:choose>
							<c:when test="${mstrate.mode eq 'VIEW'}">
								<c:choose>
									<c:when test="${u.key eq mstrate.id}">
										<li id="selected">
											<a href="${path}/master/rate/view/${u.key}/${u.value}" class="selected" id="selected">${u.key} ${u.desc } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/master/rate/view/${u.key}/${u.value}">${u.key} ${u.desc }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
							<c:when test="${mstrate.mode eq 'EDIT'}">
								<c:choose>
									<c:when test="${u.key eq mstrate.id}">
										<li id="selected">
										<a href="${path}/master/rate/edit/${u.key}/${u.value}" class="selected" id="selected">${u.key} ${u.desc } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/master/rate/edit/${u.key}/${u.value}">${u.key} ${u.desc }</a>
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
          	<a href="${path}/master/rate/new/${mstrate.mst_product_id}" class="add">Add New</a>
          	<c:if test="${mstrate.mode eq 'VIEW'}">
          		<a href="${path}/master/rate/edit/${mstrate.id }/${mstrate.mst_product_id}" class="edit">Edit Current</a>
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
          <p>You can ${mstrate.mode } Master Rate Data </p>
        </div>
	</div>
	
	
	
</body>
</html>		