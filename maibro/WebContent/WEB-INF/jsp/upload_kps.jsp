<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
		function replaceComma(nilai){
			nilai=nilai.replace(/[^0-9\.]+/g,"");
			if(isNaN(nilai)){
				result=0;
			}else{					
				var result=nilai;
				if(result=="")result=nilai;
			}
			//alert(result);
			return result;
		}
		//fungsi2 yang dijalankan saat document sudah semua loaded
		$(document).ready(function() {
			
			
			
			$("#policy\\.extrapremi").blur(function(){
				var extra_premi=parseFloat(replaceComma($('#policy\\.premi').val()));
				var premi=parseFloat(replaceComma($(this).val()));
				if(premi!=null){
					
					var totalpremi=(premi+extra_premi);
					$('#policy\\.totalpremi').val(formatCurrency(totalpremi));
				}
			});
			
			$("#policy\\.premi").blur(function(){
				var extra_premi=parseFloat(replaceComma($('#policy\\.extrapremi').val()));
				var premi=parseFloat(replaceComma($(this).val()));
				if(premi!=null){
					
					var totalpremi=(premi+extra_premi);
					$('#policy\\.totalpremi').val(formatCurrency(totalpremi));
				}
			});
			
			
			//pesan error diletakkan di alert juga
			var message = "${message}".replace(/<br\s*[\/]?>/gi, "\n");
			if(message != ''){
			 	alert(message);
			}
		});
</script>
</head>
<body>

	<div id="main">
		<div class="block" id="block-forms">
          <div class="secondary-navigation">
            <ul class="wat-cf">
               <li class="headerTitle" ><a href="#"> ${namapage }</a></li> 
            </ul>
          </div>
          <div class="content">
          
            <div class="inner">
             <form:form commandName="upload" name="formpost" method="POST"  cssClass="form" enctype="multipart/form-data">
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
					<form:label path="mst_product_id" cssClass="label" cssErrorClass="label labelError">No SPAJ</form:label>
					<form:errors path="mst_product_id" cssClass="error" />
				</div>
				 <input type="text" value="${spaj_no }" readonly="readonly">
				 <form:hidden path="policy.id"/>
				<span class="description"></span>
			</div>
			<div class="group">
				<div class="fieldWithErrors">
					<form:label path="policy.flag_akseptasi" cssClass="label" cssErrorClass="label labelError">Akseptasi</form:label>
					<form:errors path="policy.flag_akseptasi" cssClass="error" />
				</div>
				 <form:radiobutton path="policy.flag_akseptasi" label="Aksep" value="1"/>
					<form:radiobutton path="policy.flag_akseptasi" label="Tolak" value="0"/>
				<span class="description"></span>
			</div>
			<%-- <div class="group">
				<div class="fieldWithErrors">
					<form:label path="policy.tgl_aksep" cssClass="label" cssErrorClass="label labelError">Tanggal Akseptasi</form:label>
					<form:errors path="policy.tgl_aksep" cssClass="error" />
				</div>
				<form:input path="policy.tgl_aksep" cssClass="text_field datepicker" cssErrorClass="text_field error datepicker"/>
				<span class="description"></span>
			</div> --%>
			<div class="group">
				<div class="fieldWithErrors">
					<form:label path="policy.policy_no" cssClass="label" cssErrorClass="label labelError">No Polis</form:label>
					<form:errors path="policy.policy_no" cssClass="error" />
				</div>
				 <form:input path="policy.policy_no" cssClass="text_field" cssErrorClass="text_field error"/>
					
				<span class="description"></span>
			</div>
			<div class="group">
				<div class="fieldWithErrors">
					<form:label path="policy.rate" cssClass="label" cssErrorClass="label labelError">Rate</form:label>
					<form:errors path="policy.rate" cssClass="error" />
				</div>
				 <form:input path="policy.rate" cssClass="text_field nominal" cssErrorClass="text_field error nominal"/>
					
				<span class="description"></span>
			</div>
			<div class="group">
				<div class="fieldWithErrors">
					<form:label path="policy.premi" cssClass="label" cssErrorClass="label labelError">Premi</form:label>
					<form:errors path="policy.premi" cssClass="error" />
				</div>
				 <form:input path="policy.premi" cssClass="text_field nominal" cssErrorClass="text_field error nominal"/>
					
				<span class="description"></span>
			</div>
			<div class="group">
				<div class="fieldWithErrors">
					<form:label path="policy.extrapremi" cssClass="label" cssErrorClass="label labelError">Extra Premi</form:label>
					<form:errors path="policy.extrapremi" cssClass="error" />
				</div>
				 <form:input path="policy.extrapremi" cssClass="text_field nominal" cssErrorClass="text_field error nominal"/>
					
				<span class="description"></span>
			</div>
			<div class="group">
				<div class="fieldWithErrors">
					<form:label path="policy.totalpremi" cssClass="label" cssErrorClass="label labelError">Total Premi</form:label>
					<form:errors path="policy.totalpremi" cssClass="error" />
				</div>
				 <form:input path="policy.totalpremi" cssClass="text_field_read nominal" readonly="true" cssErrorClass="text_field_read error nominal"/>
					
				<span class="description"></span>
			</div>
			<c:if test="${product ne \"micro\" }">
			  <div class="columns wat-cf">
		  		
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="uploadFile" cssClass="label" cssErrorClass="label labelError">Upload File:</form:label>
	                    <form:errors path="uploadFile" cssClass="error" />
	              </div>
	              <input type="file" class="file_1" name="uploadFile" size="50"/>
             	 
                  <span class="description">format *.pdf, max: 500kb</span>
                </div>
                
			  </div>
			  </c:if>
                
			  <div class="group navform wat-cf">
					<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
	                    <img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
	                  </button>
	                  <form:hidden path="mode"/>
	                  <form:hidden path="jenisUpload"/>
	                  <span class="text_button_padding"></span>
	                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/micro/upload'">
	                    <img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
	                  </button>
		        </div>
			
			
		
			
			
		</form:form>		
                
               
            </div>
          </div>
        </div>
	
		
		
		
		
		
	</div>
	<div id="sidebar">
		<div class="block">
          <h3>History File Upload </h3>
          <div style="height: 200px;overflow: auto;">
        	<table class="table">
        		
	        	<c:forEach items="${reff.listFileUpload}" var="v">
	        		<tr>
		        		<td>${v.value} <br/><span  class="desc"> upload date : ${v.key} </span></td>
	        		</tr>
	        	</c:forEach>
	        	
        	</table>
        	</div>
        </div>

        <div class="block notice">
          <h4>Page Hint</h4>
          <p>Upload Micro Polis Data </p>
        </div>
        
        
	</div>
	
	
	
</body>
</html>		