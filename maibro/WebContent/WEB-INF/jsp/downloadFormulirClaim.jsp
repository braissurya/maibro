<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>

</head>
<body>
	<div id="main">
	
 	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle"><a href="#"> Download Formulir</a></li>             
            </ul>
          </div>
         
		 <div class="content" >
		 	 <div class="inner">
		 	
		 	 <form name="formpost" id="formpost" method="post" class="form">
			 	 <div class="columns wat-cf">
				 	 	<div class="group">
							<div class="fieldWithErrors">
								<label class="label">Perusahaan Asuransi </label>								
							</div>
							 	<select name="asuransi" onchange="formpost.submit();">
							 		<option>Pilih Perusahaan Asuransi</option>
							 		<c:forEach items="${lsasuransi}" var="d">
							 			<option value="${d.key }" <c:if test="${d.key eq asuransi }">selected="selected"</c:if>>${d.value }</option>
							 		</c:forEach>
							 	</select>
									
							<span class="description"></span>
						</div>
			 	     </div>
			
			</form>
			
			<c:if test="${not empty daftarFile }">
				<table class="table">
					<tr>
						<th>
							No
						</th>
						<th>
							Nama File
						</th>
						<th>
							Download
						</th>
					</tr>
				<c:forEach items="${daftarFile}" var="d" varStatus="s">
					<tr>
						<td>
							${s.count }
						</td>
						<td>
							${d.key }
						</td>
						<td>
							<a href="${path }/openfile/1?loc=${d.encrypt }">Download File</a>
						</td>
					</tr>
				</c:forEach>
					
				</table>
			</c:if>
		</div>
	     </div>
			   
		</div>


	
	 </div>
		
	<div id="sidebar">
		
		<div class="block">
          <h3>Others Link</h3>
          <ul class="navigation">
           	  <li><a href="${path}/micro/klaim" >Klaim Micro</a></li>     
          	    <li><a href="${path}/kpr/klaim" >Klaim KPR</a></li>          
          </ul>
        </div>
        <div class="block notice">
          <h4>Page Hint</h4>
          <p>Silahkan Download formulir</p>
        </div>
	</div>

	
	
	
</body>
</html>