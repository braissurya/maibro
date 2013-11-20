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
					
				<span class="description"></span>
			</div>
			 
			  
			    <c:if test="${not empty daftarFile}">
		                  <br/><br/>
		                  	<label class="label">File Yang sudah di Upload</label>
		                  	
			        			<table class="table">
			        				<tr>
			        					<th>
			        						Nama File
			        					</th>
			        					<th>
			        					</th>
			        				</tr>
				        			<c:forEach items="${daftarFile}" var="p">
				        				<tr>
					        				<td>${p.key }</td>
					        				<td>
						        				<a href="javascript:popWin('${path }/openfile/3?loc=${p.encrypt}','800px','800px',false,false)" title="view">
							                      <img src="${path }/static/pilu/images/icons/view.png" alt="View" />
							                    </a>
							                    
							                 
							                   
							                </td>
					                   </tr>
				        		 	</c:forEach>
			        		 	</table>
		                  </c:if>
                
			 <div class="group navform wat-cf">					
                  <button class="button" type="button" onclick="if(confirm('Are you sure want to close?'))window.close();">
                    <img src="${path }/static/pilu/images/icons/cross.png" alt="Close" /> Close
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
          <p>Upload Kuesioner Polis </p>
        </div>
        
        
	</div>
	
	
	
</body>
</html>		