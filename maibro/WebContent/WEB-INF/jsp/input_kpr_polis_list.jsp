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

           				<table class="table">
					<tr>
						<th class="first"></th>
						<th>No SPAJ / SPAK</th>
						<th>No Polis</th>
						<th>Debitur</th>
						<th>Produk</th>
						<th>Plafon Kredit</th>
						<th>Rate</th>
						<th>Premi</th>
						<th>Beg Date</th>
						<th>Tgl Print Polis</th>
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
						<td><fmt:formatDate value="${p.tgl_print}" pattern="dd-MM-yyyy"/></td>
						<td>${p.namabank}<br/>${p.namacabbank}</td>
						<td>${p.namaasuransi}</td>
						<td><fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
			
						<td class="last">
							<c:choose>
		                    	<c:when test="${p.jenis eq \"1\" }"><c:set var="jenis" value="life"/><c:set var="sppa" value="SPAJ"/></c:when>
		                    	<c:when test="${p.jenis eq \"2\" }"><c:set var="jenis" value="fire"/><c:set var="sppa" value="SPAK"/></c:when>
		                    </c:choose>
							<c:if test="${pagename eq\"input\"}">
								<a href="${path}/kpr/edit/${p.id}/${pagename}" title="edit">
			                      <img src="${path }/static/pilu/images/icons/application_edit.png" alt="Edit" />
			                    </a>
			                    
			                    <a href="javascript:if(confirm('Are you sure want to Print ${sppa} \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print_spaj/${p.id}/spaj_${jenis}','800px','800px',false,false);timedRefresh(5000);}" title="print ${sppa}">
			                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print ${sppa}" />
			                    </a>
			                    <c:if test="${not empty p.tgl_print_spaj}">
			                    	 <a href="${path}/kpr${jenis}/upload/spaj/${p.id}" title="upload ${sppa}">
				                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="upload ${sppa}" />
				                    </a>
				                    <c:if test="${not empty p.tgl_upload_spaj }">
			                      		<a href="javascript:if(confirm('Are you sure want to Print ${sppa} Upload \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/sppa_upload','800px','800px',false,false);timedRefresh(5000);}" title="print ${sppa} upload">
					                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print ${sppa} Upload" />
					                    </a>
				                    </c:if>
				                     <a href="${path}/kpr${jenis}/upload/kuesioner/${p.id}" title="upload kuesioner">
				                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="upload kuesioner" />
				                    </a>
				                    <a href="${path}/kpr${jenis}/upload/ktp/${p.id}" title="upload KTP">
				                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="upload KTP" />
				                    </a>
				                    <c:if test="${p.noclaim gt 60}">
					                    <a href="${path}/kpr${jenis}/upload/noclaim/${p.id}" title="upload Subject To No Claim">
					                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="upload Subject To No Claim" />
					                    </a>
				                    </c:if>
			                    </c:if>
		                    </c:if>
		                    <c:if test="${ pagename eq\"upload\" }">
								<a href="${path}/kpr/edit/${p.id}/${pagename}" title="edit">
			                      <img src="${path }/static/pilu/images/icons/application_edit.png" alt="Edit" />
			                    </a>
		                    </c:if>
		                    <c:if test="${pagename eq\"validasi\"}">
		                    	<c:if test="${not empty p.tgl_upload_spaj }">
		                      		<a href="javascript:if(confirm('Are you sure want to Print ${sppa} Upload \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/sppa_upload','800px','800px',false,false);timedRefresh(5000);}" title="print ${sppa} upload">
				                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print ${sppa} Upload" />
				                    </a>
			                    </c:if>
			                     <c:if test="${not empty p.tgl_upload_kuesioner }">
				                    <a href="javascript:if(confirm('Are you sure want to Print KUESIONER Upload \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/KUESIONER','800px','800px',false,false);timedRefresh(5000);}" title="print KUESIONER upload">
				                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print KUESIONER Upload" />
				                    </a>
				                </c:if>
				                 <c:if test="${not empty p.tgl_upload_ktp }">
									<a href="javascript:if(confirm('Are you sure want to Print KTP Upload \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/KTP','800px','800px',false,false);timedRefresh(5000);}" title="print KTP upload">
				                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print KTP Upload" />
				                    </a>	
			                    </c:if>
			                     <c:if test="${p.noclaim gt 60 and not empty p.tgl_upload_no_klaim }">
			                     	 <a href="javascript:if(confirm('Are you sure want to Print Subject To No Claim Upload \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/NOCLAIM','800px','800px',false,false);timedRefresh(5000);}" title="print Subject To No Claim upload">
				                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print Subject To No Claim Upload" />
				                    </a>
			                     </c:if>
								<a href="${path}/kpr/validasi/${p.id}/${pagename}" title="validasi">
			                      <img src="${path }/static/pilu/images/icons/find.png" alt="validasi" />
			                    </a>
			                    
			                    
		                    </c:if>
		                     <c:if test="${pagename eq\"akseptasi\"}">
		                     	<c:if test="${not empty p.tgl_upload_spaj }">
			                      		<a href="javascript:if(confirm('Are you sure want to Print ${sppa} Upload \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/sppa_upload','800px','800px',false,false);timedRefresh(5000);}" title="print ${sppa} upload">
					                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print ${sppa} Upload" />
					                    </a>
				                 </c:if>
				                <c:if test="${not empty p.tgl_upload_kuesioner }">
				                    <a href="javascript:if(confirm('Are you sure want to Print KUESIONER Upload \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/KUESIONER','800px','800px',false,false);timedRefresh(5000);}" title="print KUESIONER upload">
				                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print KUESIONER Upload" />
				                    </a>
				                </c:if>
				                 <c:if test="${not empty p.tgl_upload_ktp }">
									<a href="javascript:if(confirm('Are you sure want to Print KTP Upload \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/KTP','800px','800px',false,false);timedRefresh(5000);}" title="print KTP upload">
				                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print KTP Upload" />
				                    </a>	
			                    </c:if>
			                     <c:if test="${p.noclaim gt 60 and not empty p.tgl_upload_no_klaim }">
			                     	 <a href="javascript:if(confirm('Are you sure want to Print Subject To No Claim Upload \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/NOCLAIM','800px','800px',false,false);timedRefresh(5000);}" title="print Subject To No Claim upload">
				                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print Subject To No Claim Upload" />
				                    </a>
			                     </c:if>
		                    </c:if>
		                     <c:if test="${pagename eq\"cetak\"}">
		                     	
		                     	<c:choose>
		                     		<c:when test="${p.jenis eq\"1\" }">
		                     			<a href="javascript:if(confirm('Are you sure want to Print KPS \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/polis_life','800px','800px',false,false);timedRefresh(5000);}" title="Cetak KPS">
					                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="Cetak KPS" />
					                    </a>
		                     		</c:when>
		                     		<c:when test="${p.jenis eq\"2\" }">
		                     			
		                     			
		                     			<a href="javascript:if(confirm('Are you sure want to Print Polis \'No. ${sppa} : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/polis_fire','800px','800px',false,false);timedRefresh(5000);}" title="Cetak Polis">
					                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="Cetak Polis" />
					                    </a>
		                     		</c:when>
		                     	</c:choose>
								
		                    </c:if>
		                     <c:if test="${pagename eq\"kps\"}">
								 <a href="${path }/kprlife/upload/kps/${p.id}" title="Upload KPS">
			                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="Upload KPS" />
			                    </a> 
			                    
		                    </c:if>
		                    <a href="${path}/kpr/view/${p.id}/${pagename}" title="view">
		                      <img src="${path }/static/pilu/images/icons/view.png" alt="View" />
		                    </a>
		                    <c:choose>
		                    	<c:when test="${pagename eq\"input\"  and empty p.premi}">
		                    		 <a href="javascript:if(confirm('Are you sure want to Transfer to Validasi \'No ${sppa} : ${p.spaj_no}\' ?'))window.location='${path}/kpr/tranfer/${p.id}/${pagename}';" title="Transfer to Validasi">
				                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Validasi" />
				                    </a>
		                    	</c:when>
		                    	<c:when test="${pagename eq\"input\"  and not empty p.tgl_upload_spaj}">
		                    		<c:choose>
		                    			<c:when test="${p.noclaim gt 60}">
		                    				<c:if test="${not empty p.tgl_upload_no_klaim }">
		                    				 <a href="javascript:if(confirm('Are you sure want to Transfer to Validasi \'No ${sppa} : ${p.spaj_no}\' ?'))window.location='${path}/kpr/tranfer/${p.id}/${pagename}';" title="Transfer to Validasi">
						                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Validasi" />
						                    </a>
						                    </c:if>
		                    			</c:when>
		                    			<c:otherwise>
		                    				 <a href="javascript:if(confirm('Are you sure want to Transfer to Validasi \'No ${sppa} : ${p.spaj_no}\' ?'))window.location='${path}/kpr/tranfer/${p.id}/${pagename}';" title="Transfer to Validasi">
						                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Validasi" />
						                    </a>
		                    			</c:otherwise>
		                    		</c:choose>
		                    		
		                    	</c:when>
		                    	<c:when test="${pagename eq\"upload\" }">
		                    		 <a href="javascript:if(confirm('Are you sure want to Transfer to Validasi \'No ${sppa} : ${p.spaj_no}\' ?'))window.location='${path}/kpr/tranfer/${p.id}/${pagename}';" title="Transfer to Validasi">
				                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Validasi" />
				                    </a>
		                    	</c:when>
		                    	<c:when test="${pagename eq\"validasi\" and not empty p.asuransi_id and p.flag_akseptasi_mb eq 1}">
		                    		<c:choose>
		                    			<c:when test="${ empty p.tgl_upload_spaj }">
		                    				 <a href="javascript:if(confirm('Are you sure want to Transfer to Akseptasi \'No ${sppa} : ${p.spaj_no}\' ?'))window.location='${path}/kpr/tranfer/${p.id}/${pagename}';" title="Back to Input">
						                      <img src="${path }/static/pilu/images/icons/collapsed.png" alt="Transfer to Bank" />
						                    </a>
		                    			</c:when>
		                    			<c:otherwise>
		                    				 <a href="javascript:if(confirm('Are you sure want to Transfer to Akseptasi \'No ${sppa} : ${p.spaj_no}\' ?'))window.location='${path}/kpr/tranfer/${p.id}/${pagename}';" title="Transfer to Akseptasi">
						                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Akseptasi" />
						                    </a>
		                    			</c:otherwise>
		                    		</c:choose>
		                    		
		                    	</c:when>
		                    	<c:when test="${pagename eq\"cetak\" and not empty p.tgl_print }">
		                    		<c:choose>
			                     		<c:when test="${p.jenis eq\"1\" }">
			                     			 <a href="javascript:if(confirm('Are you sure want to Transfer to Filling \'No ${sppa} : ${p.spaj_no}\' ?'))window.location='${path}/kpr/tranfer/${p.id}/${pagename}';" title="Transfer to Filling">
						                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Filling" />
						                    </a>
			                     		</c:when>
			                     		<c:when test="${p.jenis eq\"2\" and not empty p.tgl_paid }">
			                     			 <a href="javascript:if(confirm('Are you sure want to Transfer to Filling \'No ${sppa} : ${p.spaj_no}\' ?'))window.location='${path}/kpr/tranfer/${p.id}/${pagename}';" title="Transfer to Filling">
						                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="Transfer to Filling" />
						                    </a>
			                     		</c:when>
			                     	</c:choose>
		                    		
		                    	</c:when>
		                    	<c:otherwise></c:otherwise>
		                    </c:choose>
		                    <c:if test="">
			                   
		                   </c:if>
		                   
						</td>
					</tr>
					</c:forEach>
				</table> 
		                   	
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
	                    <button class="button" type="button" onclick="window.location='${path}/kpr/new';">
	                      <img src="${path }/static/pilu/images/icons/add.gif" alt="Add New" /> Add New
	                    </button>
	                   
	                    <c:if test="${pagename eq\"upload\" }">
	                    	 <button class="button" type="button" onclick="window.location='${path}/kpr/input/upload';">
		                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="Upload" />Upload
		                    </button>
	                    </c:if>
                     </c:if>
                     <c:if test="${pagename eq\"akseptasi\" }">
                     	<c:choose>
                     		<c:when test="${sessionScope.currentUser.bank_jenis eq\"2\" }">
                     			<button class="button" type="submit" name="download_akseptasi_life" value="." >
			                      <img src="${path }/static/pilu/images/icons/dl.jpg" alt="Download Akseptasi Life" /> Download Akseptasi Life
			                    </button>
			                     <button class="button" type="button" onclick="window.location='${path}/kprlife/akseptasi/upload';">
			                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="Upload Akseptasi Life" />Upload Akseptasi Life
			                    </button>
                     		</c:when>
                     		<c:when test="${sessionScope.currentUser.bank_jenis eq\"4\" }">
                     			 <button class="button" type="submit" name="download_akseptasi_fire" value="." >
			                      <img src="${path }/static/pilu/images/icons/dl.jpg" alt="Download Akseptasi Fire" /> Download Akseptasi Fire
			                    </button>
			                     <button class="button" type="button" onclick="window.location='${path}/kprfire/akseptasi/upload';">
			                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="Upload Akseptasi Fire" />Upload Akseptasi Fire
			                    </button>
                     		</c:when>
                     		<c:when test="${sessionScope.currentUser.bank_jenis eq\"3\" }">
                     			<button class="button" type="submit" name="download_akseptasi_life" value="." >
			                      <img src="${path }/static/pilu/images/icons/dl.jpg" alt="Download Akseptasi Life" /> Download Akseptasi Life
			                    </button>
			                     <button class="button" type="button" onclick="window.location='${path}/kprlife/akseptasi/upload';">
			                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="Upload Akseptasi Life" />Upload Akseptasi Life
			                    </button>
			                     <button class="button" type="submit" name="download_akseptasi_fire" value="." >
			                      <img src="${path }/static/pilu/images/icons/dl.jpg" alt="Download Akseptasi Fire" /> Download Akseptasi Fire
			                    </button>
			                     <button class="button" type="button" onclick="window.location='${path}/kprfire/akseptasi/upload';">
			                      <img src="${path }/static/pilu/images/icons/upload.jpg" alt="Upload Akseptasi Fire" />Upload Akseptasi Fire
			                    </button>
                     		</c:when>
                     	</c:choose>
	                    	
	                    </c:if>
                      <c:if test="${pagename eq\"klaim\"}">
                      	 <button class="button" type="button" onclick="window.location='${path}/kpr/new/${pagename}';">
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