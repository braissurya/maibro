<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>

	<form name="form" id="form" method="post">
	<div id="main" class="fullwidth">
	
	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle" ><a href="#"> ${namaMenu }</a></li>             
            </ul>
          </div>
		
		
		 <div class="content">
		 	<table style="width: 100%">
		 		<tr>
		 			<th style="text-align: center;">
		 				<label>Periode (Tgl Mulai Asuransi)</label>
									<br/>
						<input type="text" value="${begdate }" name="begdate" class="text_field datepicker"> s/d 
						<input type="text" value="${enddate }" name="enddate" class="text_field datepicker">
									
		 			</th>
		 		</tr>
		 		<tr>
					<td style="text-align: center;">
						<input type="submit" value="Show">
					</td>
				</tr>
		 		<tr>
			 		<td>
			            <div class="total">Total Data : ${totalData }</div>
			            <div class="search">
			            	<input type="text" name="s"  value="${search}" size="30" class="textfield">
			            	<input type="submit" name="btsearch"  value=" " class="btsearch" title="search">
			            	
			            </div>
		           	</td>
            	</tr>
            	
            	
            <tr>
		 		<td>
            <div class="inner">
      
              	<c:choose>
              		<c:when test="${not empty listPolicy}">
              				<table class="table">
								<tr>
									<th class="first"></th>
									<th>No Spaj</th>
									<th>No Polis</th>
									<th>Debitur</th>
									<th>Produk</th>
									<th>Plafon Kredit</th>
									<th>Rate</th>
									<th>Premi</th>
									<th>Beg Date</th>
									<th>Bank</th>
									<th>Asuransi</th>
									<th>Create Date</th>
									 <th class="last">Actions</th>
								</tr>
								
								<c:forEach items="${listPolicy}" var="p">
								<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
									<td></td>
									<td>${p.spaj_no}</td>
									<td>${p.policy_no}</td>
									<td>${p.debitur}</td>
									<td>${p.produk}</td>
									<td><fmt:formatNumber>${p.up}</fmt:formatNumber></td>
									<td><fmt:formatNumber>${p.rate}</fmt:formatNumber></td>
									<td><fmt:formatNumber>${p.premi}</fmt:formatNumber></td>
									<td><fmt:formatDate value="${p.beg_date}" pattern="dd-MM-yyyy"/></td>
									<td>${p.namabank}<br/>${p.namacabbank}</td>
									<td>${p.namaasuransi}</td>
									<td><fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
						
									<td class="last">
										<c:if test="${pagename eq\"input\" or pagename eq\"upload\" }">
											<a href="${path}/micro/edit/${p.id}/${pagename}" title="edit">
						                      <img src="${path }/static/pilu/images/icons/application_edit.png" alt="Edit" />
						                    </a>
					                    </c:if>
					                    <c:if test="${pagename eq\"validasi\"}">
											<a href="${path}/micro/validasi/${p.id}/${pagename}" title="validasi">
						                      <img src="${path }/static/pilu/images/icons/find.png" alt="validasi" />
						                    </a>
					                    </c:if>
					                     <c:if test="${pagename eq\"akseptasi\"}">
											<%-- <a href="${path }/kpr/akseptasi" title="Akseptasi Polis">
						                      <img src="${path }/static/pilu/images/icons/filetick.gif" alt="Akseptasi Polis" />
						                    </a> --%>
						                    
					                    </c:if>
					                      <c:if test="${pagename eq\"kps\"}">
											 <a href="${path }/micro/upload/kps/${p.id}" title="Upload KPS">
						                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="Upload KPS" />
						                    </a> 
						                    
					                    </c:if>
					                    <c:if test="${pagename eq\"cetak\"}">
					                     	
				                     			<a href="javascript:if(confirm('Are you sure want to Print KPS \'No. SPAJ : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/polis_life','800px','800px',false,false);wn}" title="Cetak KPS">
							                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="Cetak KPS" />
							                    </a>
					                     		
											
					                    </c:if>	
					                   
					                    <a href="${path}/micro/view/${p.id}/${pagename}" title="view">
					                      <img src="${path }/static/pilu/images/icons/view.png" alt="View" />
					                    </a>
					                    
					                      <c:choose>
					                    	<c:when test="${pagename eq\"input\" or pagename eq\"upload\" }">
					                    		 <a href="javascript:if(confirm('Are you sure want to Transfer to Validasi \'No SPAJ : ${p.spaj_no}\' ?'))window.location='${path}/micro/tranfer/${p.id}/${pagename}';" title="Transfer to Validasi">
							                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Validasi" />
							                    </a>
					                    	</c:when>
					                    	<c:when test="${pagename eq\"validasi\" and not empty p.asuransi_id and p.flag_akseptasi_mb eq 1}">
					                    		 <a href="javascript:if(confirm('Are you sure want to Transfer to Akseptasi \'No SPAJ : ${p.spaj_no}\' ?'))window.location='${path}/micro/tranfer/${p.id}/${pagename}';" title="Transfer to Akseptasi">
							                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Akseptasi" />
							                    </a>
					                    	</c:when>
					                    	<c:when test="${pagename eq\"cetak\" and not empty p.tgl_print }">
					                    		 <a href="javascript:if(confirm('Are you sure want to Transfer to Filling \'No SPAJ : ${p.spaj_no}\' ?'))window.location='${path}/micro/tranfer/${p.id}/${pagename}';" title="Transfer to Filling">
							                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Filling" />
							                    </a>
					                    	</c:when>
					                    	<c:otherwise></c:otherwise>
					                    </c:choose>
					                   
									</td>
								</tr>
								</c:forEach>
							</table> 
              		</c:when>
              		<c:when test="${not empty listClaim}">
              				<table class="table">
								<tr>
									<th class="first"></th>
									<th>ID</th>
									<th>No Spaj</th>
									<th>No Polis</th>
									<th>Debitur</th>
									<th>Asuransi</th>
									<th>Produk</th>
									<th>Beg Date</th>
									<th>Create By</th>
									<th>Create Date</th>
									 <th class="last">Actions</th>
								</tr>
								
								<c:forEach items="${listClaim}" var="p">
								<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
									<td></td>
									<td>${p.id }</td>
									<td>${p.spaj_no}</td>
									<td>${p.policy_no}</td>
									<td>${p.namabank}</td>
									<td>${p.namaasuransi}</td>
									<td>${p.namaproduk}</td>
									<td><fmt:formatDate value="${p.beg_date}" pattern="dd-MM-yyyy"/></td>
									<td>${p.klaimusername}</td>
									<td><fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
						
									<td class="last">
										
					                    <a href="${path}/micro/viewclaim/${p.id}/${pagename}" title="view">
					                      <img src="${path }/static/pilu/images/icons/view.png" alt="View" />
					                    </a>
					                    
					                    <a href="javascript:if(confirm('Are you sure want to Transfer \'Id : ${p.id}\' ?'))window.location='${path}/micro/tranfer/${p.id}/${pagename}';" title="transfer">
					                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="transfer" />
					                    </a>
					                   
									</td>
								</tr>
								</c:forEach>
							</table> 
              		</c:when>
              	</c:choose>
		                   	
                <span class="page">Page : ${page } / ${totalPage }</span>
                <span class="datacount">
	            Row Per Page
	            	<select name="rowcount" onchange="form.submit();">
	            		<c:forEach items="${listNumRows }" var="ln">
	            			<option value="${ln}" <c:if test="${rowcount eq ln}">selected="true"</c:if>>${ln}</option>
	            		</c:forEach>
	            	</select>
	            </span>
                <div class="actions-bar wat-cf">
                  <div class="actions">
                    <c:if test="${pagename eq\"input\" or pagename eq\"upload\" }">
	                    <button class="button" type="button" onclick="window.location='${path}/micro/new/${pagename}';">
	                      <img src="${path }/static/pilu/images/icons/add.gif" alt="Add New" /> Add New
	                    </button>
	                   
	                    <c:if test="${pagename eq\"upload\" }">
	                    	 <button class="button" type="button" onclick="window.location='${path}/micro/input/upload';">
		                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="Upload" />Upload
		                    </button>
	                    </c:if>
                     </c:if>
                        <c:if test="${pagename eq\"akseptasi\" }">
	                    	 <button class="button" type="button" onclick="window.location='${path}/micro/akseptasi/upload';">
		                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="Upload Akseptasi" />Upload Akseptasi
		                    </button>
		                     <button class="button" type="submit" name="download_akseptasi" value="." >
		                      <img src="${path }/static/pilu/images/icons/dl.jpg" alt="Download Akseptasi" /> Download Akseptasi
		                    </button>
	                    </c:if>
                      <c:if test="${pagename eq\"klaim\"}">
                      	 <button class="button" type="button" onclick="window.location='${path}/micro/new/${pagename}';">
	                      <img src="${path }/static/pilu/images/icons/add.gif" alt="Add New" /> Add Klaim
	                    </button>
	                     <button class="button" type="button" onclick="window.location='${path}/klaim/formulir';">
	                      <img src="${path }/static/pilu/images/icons/dl.jpg" alt="Download Formulir" /> Download Formulir
	                    </button>
	                    
                      </c:if>
                  </div>
                  <div class="pagination">
                  	<input type="hidden" name="page" id="page" value="${page }">
                  	<input type="hidden" name="sort" id="sort" value="${sort }">
                  	<input type="hidden" name="st" id="st" value="${sort_type}">
                  	<c:choose>
                  		<c:when test="${totalPage eq \"1\" }">
                  			<span class="current">1</span>
                  		</c:when>
                  		<c:when test="${totalPage lt \"10\" }">
                  			<c:choose>
               					<c:when test="${page eq 1 }">
               						<span class="disabled prev_page">� Previous</span>
               					</c:when>
               					<c:otherwise>
               						<a class="prev_page" href="javascript:gotoPage('${page-1 }','${sort }','${sort_type}','form')">� Previous</a>
               					</c:otherwise>
               				</c:choose>
                  			
                  			<c:forEach items="${pages}" var="p" varStatus="s">
                  				<c:choose>
                  					<c:when test="${p eq page }">
                  						<span  class="current">${p}</span>
                  					</c:when>
                  					<c:otherwise>
                  						<a href="javascript:gotoPage('${p}','${sort }','${sort_type}','form');">${p}</a>
                  					</c:otherwise>
                  				</c:choose>	
                  				
                  			</c:forEach>
                  			<c:choose>
               					<c:when test="${page eq totalPage }">
               						<span class="disabled next_page">Next �</span>
               					</c:when>
               					<c:otherwise>
               						<a class="next_page" href="javascript:gotoPage('${page+1 }','${sort }','${sort_type}','form')">Next �</a>
               					</c:otherwise>
               				</c:choose>
                  		</c:when>
                  		<c:when test="${totalPage gt \"9\" }">
                  			
                  			<c:choose>
               					<c:when test="${page eq 1 }">
               						<span class="disabled prev_page">� Previous</span>
               					</c:when>
               					<c:otherwise>
               						<a class="prev_page" href="javascript:gotoPage('${page-1 }','${sort }','${sort_type}','form')">� Previous</a>
               					</c:otherwise>
               				</c:choose>
                  			<c:forEach items="${pages}" var="p" varStatus="s">
                  				<c:choose>
                  					<c:when test="${p eq 1 or p eq 2 or p eq totalPage or p eq totalPage-1}">
               							<c:choose>
		                  					<c:when test="${p eq page }">
		                  						<span  class="current">${p}</span>
		                  					</c:when>
		                  					<c:otherwise>
		                  						<a href="javascript:gotoPage('${p}','${sort }','${sort_type}','form');">${p}</a>
		                  					</c:otherwise>
		                  				</c:choose>	
                  					</c:when>
                  					<c:when test="${p eq halfPage}">
                  						<div class="middle">...</div>
                  						<c:choose>
                  							<c:when test="${page gt 2 and page lt totalPage-1  }">
                  								<input type="text" value="${page}" name="p" class="middle" onchange="gotoPage(this.value,'${sort }','${sort_type}','form');"> 
                  							</c:when>
                  							<c:otherwise>
                  								<input type="text" value="${p}" name="p" class="middle_current" onchange="gotoPage(this.value,'${sort }','${sort_type}','form');"> 
                  							</c:otherwise>
                  						</c:choose>
                  						<div class="middle">...</div>
               							
                  					</c:when>
                  					
                  				</c:choose>
                  				
                  			</c:forEach>
                  			
                  			<c:choose>
               					<c:when test="${page eq totalPage }">
               					
               						<span class="disabled next_page">Next �</span>
               					</c:when>
               					<c:otherwise>
               						<a class="next_page" href="javascript:gotoPage('${page+1 }','${sort }','${sort_type}','form')">Next �</a>
               					</c:otherwise>
               				</c:choose>
                  		</c:when>
                  	</c:choose>
                    	
                  </div>
                </div>
            </div>
            </td>
            </tr>
            </table>
          </div>
          
        </div>
		
	</div>
		
	
		

	
</form>
	

		
		
		
	
	
</body>
</html>