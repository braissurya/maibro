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
               <li class="headerTitle" ><a href="#"> Upload Rate</a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="upload" name="formpost" method="POST" action="${path }/rate/uploadProses/${upload.mst_product_id }"  cssClass="form" enctype="multipart/form-data">
              	<c:choose>
              		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
              			<div class="flash">
				            <div class="message ${messageType}">
				              <p>${message }</p>
				              <c:forEach items="${errorList}" var="s">
				              	${s }
				              </c:forEach>
				            </div>
				          </div>
              		</c:when>
              		<c:otherwise>
              			<c:if test="${upload.mode ne 'VIEW'}">
	              			<div class="flash">
					            <div class="message notice">
					              <p><fmt:message key="CompleteForm" /> </p>
					            </div>
				            </div>
			           </c:if>
              		</c:otherwise>
              	</c:choose>
                
              
			  <div class="group">
				<div class="fieldWithErrors">
					<form:label path="mst_product_id" cssClass="label" cssErrorClass="label labelError">Nama Produk<span class="mandatory" title="Wajib diisi"> *</span></form:label>
					<form:errors path="mst_product_id" cssClass="error" />
				</div>
				 <form:select path="mst_product_id" cssErrorClass="inputError">
					<form:option value="">Silahkan Pilih Produk</form:option>
					<form:options items="${reff.listProduct}" itemValue="key" itemLabel="value"/>
				</form:select>
					
				<span class="description"></span>
			</div>
			  <div class="columns wat-cf">
		  		 <div class="group">
                   <div class="fieldWithErrors">
	                   <form:label path="importStartLine"  cssClass="label" cssErrorClass="label labelError">Start Baris Ke<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="importStartLine" cssClass="error" />
	               </div>
                		<form:input path="importStartLine" cssClass="text_field" id="target" size="4"/> 
						
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="uploadFile" cssClass="label" cssErrorClass="label labelError">Upload File<span class="mandatory" title="Wajib diisi"> *</span></form:label>
	                    <form:errors path="uploadFile" cssClass="error" />
	              </div>
	              <input type="file" class="file_1" name="uploadFile" size="50"/>
             	 
                  <span class="description"></span>
                </div>
                
			  </div>
                
			  <div class="group navform wat-cf">
					<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
	                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Upload" /> Upload
	                  </button>
	                  <form:hidden path="mode"/>
	                  <form:hidden path="jenisUpload"/>
	                  <span class="text_button_padding"></span>
	                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/rate/${upload.mst_product_id }'">
	                    <img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
	                  </button>
		        </div>
			
			
		
			
			
		</form:form>		
                
               
            </div>
          </div>
        </div>
	
		
		
		
		
		
	</div>
	<div id="sidebar">
		

        <div class="block notice">
          <h4>Page Hint</h4>
          <p>Upload Micro Polis Data </p>
        </div>
	</div>
	
	
	
</body>
</html>		