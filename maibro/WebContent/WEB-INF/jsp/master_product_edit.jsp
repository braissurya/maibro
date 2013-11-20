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
               <li class="headerTitle" ><a href="#">${mstproduct.mode } Master Product</a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="mstproduct" name="formpost" method="POST" action="${path}/master/product/save" cssClass="form" >
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
	                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID Produk</form:label>
	                    <form:errors path="id" cssClass="error" />
	               </div>
                
						<form:hidden path="id" /><input type="text" class="text_field_read" value="${mstproduct.id }" readonly="readonly" />
				 
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="kode" cssClass="label" cssErrorClass="label labelError">Kode Produk<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="kode" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstproduct.mode eq 'VIEW'}"><form:hidden path="kode" /><input type="text" class="text_field_read" value="${mstproduct.kode }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="kode"  id="target" cssClass="text_field" size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama Produk<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="nama" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstproduct.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field_read" value="${mstproduct.nama }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="jenis" cssClass="label" cssErrorClass="label labelError">Jenis Produk<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="jenis" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstproduct.mode eq 'VIEW'}"><form:hidden path="jenis" /><input type="text" class="text_field_read" value="${mstproduct.jenisName }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="jenis">
								<form:option value="">Silahkan Pilih Jenis Produk</form:option>
								<form:options items="${reff.JenisProduct}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="group_product" cssClass="label" cssErrorClass="label labelError">Group Produk<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="group_product" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstproduct.mode eq 'VIEW'}"><form:hidden path="group_product" /><input type="text" class="text_field_read" value="${mstproduct.group_product_name }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="group_product">
								<form:option value="">Silahkan Pilih Group Produk</form:option>
								<form:options items="${reff.GroupProduct}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
			  </div>
                
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="mst_bank_id" cssClass="label" cssErrorClass="label labelError">Nama Bank<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="mst_bank_id" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstproduct.mode eq 'VIEW'}"><form:hidden path="mst_bank_id" /><input type="text" class="text_field_read" value="${mstproduct.namabank }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="mst_bank_id">
								<form:option value="">Silahkan Pilih Nama Bank</form:option>
								<form:options items="${reff.AllBank}" itemValue="key" itemLabel="value"/>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="rek_no" cssClass="label" cssErrorClass="label labelError">No Rekening</form:label>
	                    <form:errors path="rek_no" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstproduct.mode eq 'VIEW'}"><form:hidden path="rek_no" /><input type="text" class="text_field_read" value="${mstproduct.rek_no }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="rek_no"  id="target" cssClass="text_field" size="30" maxlength="30"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="rek_nama" cssClass="label" cssErrorClass="label labelError">Nama Rekening</form:label>
	                    <form:errors path="rek_nama" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${mstproduct.mode eq 'VIEW'}"><form:hidden path="rek_nama" /><input type="text" class="text_field_read" value="${mstproduct.rek_nama }" readonly="readonly" /></c:when>
						<c:otherwise><form:input path="rek_nama"  id="target" cssClass="text_field" size="100" maxlength="100"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>   
                
			  <div class="group navform wat-cf">
     			  <c:choose>
						<c:when test="${mstproduct.mode eq 'VIEW'}"><form:hidden path="id" />
							<button class="button" type="button" onclick="window.location='${path}/master/product'">
			                    <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/product'">
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
		
		<c:if test="${mstproduct.mode ne 'NEW' }">
		<div class="block">
		 
          	<h3>Others Master Product</h3>
          	<div class="filter">
          		Filter : <input type="text" class="text-input" id="filter" value="" />
          
	       		 <span class="description" id="filter-count"></span>
	        </div>
	        <div class="navi">
	          <ul class="navigation_filter" id="filterList">
	          	<c:forEach items="${reff.AllProduct}" var="u">
          		 	
		          		<c:choose>
							<c:when test="${mstproduct.mode eq 'VIEW'}">
								<c:choose>
									<c:when test="${u.key eq mstproduct.id}">
										<li id="selected">
											<a href="${path}/master/product/view/${u.key}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/master/product/view/${u.key}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
							<c:when test="${mstproduct.mode eq 'EDIT'}">
								<c:choose>
									<c:when test="${u.key eq mstproduct.id}">
										<li id="selected">
										<a href="${path}/master/product/edit/${u.key}" class="selected" id="selected">${u.key} ${u.value } </a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
										<a href="${path}/master/product/edit/${u.key}">${u.key} ${u.value }</a>
										</li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
					   </c:choose>
	          	</c:forEach>
	           
	          </ul>
          </div>
           <div class="action">
          	<a href="${path}/master/product/new" class="add">Add New</a>
          	<c:if test="${mstproduct.mode eq 'VIEW'}">
          		<a href="${path}/master/product/edit/${mstproduct.id }" class="edit">Edit Current</a>
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
          <p>You can ${mstproduct.mode } Master Product Data </p>
        </div>
	</div>
	
	
	
</body>
</html>		