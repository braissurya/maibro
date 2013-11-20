<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
		$(function(){
			function calculate() {	 	
			    $.map($('input:checkbox:checked'), function(e, i) {
			    	if($('#tglpaid_'+e.value).val()=='')
			        $('#tglpaid_'+e.value).val('${sysdate}');
			     }); 
			     
				$.map($('input:checkbox:not(:checked)'), function(e, i) {
			    	 $('#tglpaid_'+e.value).val('');
			    });		   
			}
					
			$('table.table').delegate('input:checkbox', 'click', calculate);
		});
	</script>
</head>
<body>


	<div id="main" class="fullwidth">
	
	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle" ><a href="#"> Input Payment</a></li>             
            </ul>
          </div>
		
		
		 <div class="content">
		 	<br/>
		 		<form name="formfilter" id="formfilter" method="post">
		 	<table style="width: 100%" >
		 		
		 		<tr>
		 			<th style="width: 200px;text-align: left;padding-left: 20px" >
		 			Filter :
		 			</th>
		 			<td>
		 			</td>
		 		</tr>
		 		<tr>
		 			<th style="width: 200px;text-align: left;padding-left: 20px" >
		 				<label>Periode (Tgl Mulai Asuransi)  </label>
					</th>
					<td style="text-align: left;">
						<input type="text" value="${begdate }" name="begdate" class="text_field datepicker"> s/d 
						<input type="text" value="${enddate }" name="enddate" class="text_field datepicker">
									
		 			</td>
		 		</tr>
		 		<tr>
		 			<td colspan="2">
		 			</td>
		 		</tr>
		 		<tr>
					<th style="text-align: left;padding-left: 20px"><label>Jenis Produk </label>
					</th>
					<td  style="text-align: left;">	
						<input type="radio" name="jenis_produk" value="0" class="radio" <c:if test="${jenis_produk eq \"0\" }" >checked="checked"</c:if>><label>ALL</label>	
						<c:forEach items="${reff.AllProduct}" var="p">
							<input type="radio" name="jenis_produk" value="${p.key}" class="radio" <c:if test="${jenis_produk eq p.key }" >checked="checked"</c:if>><label>${p.value }</label>
						</c:forEach>				
						
					</td>
				</tr>
				<tr>
					<th style="text-align: left;padding-left: 20px"><label>Status Bayar </label>
					</th>
					<td  style="text-align: left;">						
						<input type="radio" name="paid" value="-1" class="radio" <c:if test="${paid eq \"-1\" }" >checked="checked"</c:if>><label>ALL</label>
						<input type="radio" name="paid"  value="1" <c:if test="${paid eq \"1\" }">checked="checked"</c:if>><label>Paid</label>
						<input type="radio" name="paid"  value="2" <c:if test="${paid eq \"2\" }">checked="checked"</c:if>><label>Lebih Bayar</label>
						<input type="radio" name="paid"  value="3" <c:if test="${paid eq \"3\" }">checked="checked"</c:if>><label>Kurang Bayar</label>
						<input type="radio" name="paid" value="0" <c:if test="${paid eq \"0\" }">checked="checked"</c:if>><label>Out Standing</label>
						
					</td>
				</tr>
				<tr class="tglpaid"  >
		 			<th style="width: 200px;text-align: left;padding-left: 20px" >
		 				<label>Periode (Pembayaran)  </label>
					</th>
					<td style="text-align: left;">
						<input type="text" value="${begdatepaid }" name="begdatepaid" class="text_field datepicker"> s/d 
						<input type="text" value="${enddatepaid }" name="enddatepaid" class="text_field datepicker">
									
		 			</td>
		 		</tr>
		 		<tr>
		 			<td colspan="2">
		 			</td>
		 		</tr>
		 		<tr>
		 			<th>
		 			</th>
					<td style="text-align: left;" >
						<input type="submit" value="Show">
					</td>
				</tr>
		 		<tr>
			 		<td  colspan="2">
			            <div class="total">Total Data : ${totalData }</div>
			            <div class="search">
			            	<input type="text" name="s"  value="${search}" size="30" class="textfield">
			            	<input type="submit" name="btsearch"  value=" " class="btsearch" title="search">
			            	
			            </div>
		           	</td>
            	</tr>
            </table>	
            </form>
            <form name="form" id="form" method="post" action="${path}/keuangan/input/saveAll">
			<table style="width: 100%">
            <tr>
		 		<td colspan="2">
            <div class="inner">
      
            
          		<table class="table" >
					<tr>
						<th class="first"></th>
						<th>No SPAJ</th>
						<th>No Polis</th>
						<th>Debitur</th>
						<th>Produk</th>
						<th>Beg Date</th>
						<th>Plafon Kredit</th>
						<th>Rate</th>
						<th>Premi</th>
						<th>Status Bayar</th>
						<th>Paid</th>
						<th>Jumlah Bayar</th>
						<th>Tanggal Bayar</th>
						<th class="last">Actions</th>
					</tr>
					
					<c:forEach items="${listPolicy}" var="p">
					<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
						<td></td>
						<td>${p.spaj_no}</td>
						<td>${p.policy_no}</td>
						<td>${p.debitur}</td>
						<td>${p.produk}</td>
						<td><fmt:formatDate value="${p.beg_date}" pattern="dd-MM-yyyy"/></td>
						<td><fmt:formatNumber>${p.up }</fmt:formatNumber></td>
						<td><fmt:formatNumber>${p.rate }</fmt:formatNumber></td>
						<td><fmt:formatNumber>${p.premi }</fmt:formatNumber> </td>		
									
						<td nowrap="nowrap">
					
						
								<c:if test="${not empty p.tgl_paid }">
									<c:choose>
										<c:when test="${p.flag_paid eq 1}">
											Paid
										</c:when>										
										<c:when test="${ p.flag_paid eq 2}">
											Lebih Bayar
											<b><br/><fmt:formatNumber>${ p.nominal_paid - p.premi  }</fmt:formatNumber> </b>
										</c:when>
										<c:when test="${p.flag_paid eq 3}">
											Kurang Bayar
											<b><br/><fmt:formatNumber>${ p.nominal_paid - p.premi  }</fmt:formatNumber> </b>
										</c:when>
										<c:otherwise>Belum</c:otherwise>
									</c:choose>
									
								</c:if>
								
								
						</td>
						<td>
							<c:choose>
								<c:when test="${not empty p.tgl_paid}">
									<c:choose>
										<c:when test="${p.flag_paid eq 1}">
											<input type="checkbox" name="paid" checked="checked" value="${p.id}" disabled="disabled"  > 
										</c:when>
										<c:otherwise><input type="checkbox" name="paid" checked="checked" value="${p.id}" readonly="readonly"> </c:otherwise>
									</c:choose>
									
								</c:when>
								<c:otherwise>
									<input type="checkbox" name="paid"  value="${p.id}" > 
								</c:otherwise>
							</c:choose>
							
						</td>
						<td>
							<c:choose>
								<c:when test="${not empty p.tgl_paid and p.flag_paid eq 1 }">
									<fmt:formatNumber pattern="#,###.00">${p.nominal_paid }</fmt:formatNumber>
								</c:when>
								<c:otherwise>
									<input type="text" name="nominalpaid_${p.id}" id="nominalpaid_${p.id}" size="10" value="<fmt:formatNumber pattern="#,###.00">${p.nominal_paid }</fmt:formatNumber>" class="text_field nominal" >
								</c:otherwise>
							</c:choose>
						 </td>		
						<td>
							<c:choose>
								<c:when test="${not empty p.tgl_paid and p.flag_paid eq 1}">
									<fmt:formatDate value="${p.tgl_paid}" pattern="dd-MM-yyyy"/>
								</c:when>
								<c:otherwise>
									<input type="text" name="tglpaid_${p.id}" id="tglpaid_${p.id}" value="<fmt:formatDate value="${p.tgl_paid}" pattern="dd-MM-yyyy"/>" class="text_field datepicker">
								</c:otherwise>
							</c:choose>
						</td>
						<td class="last">
							<c:choose>
								<c:when test="${not empty p.tgl_paid }">
									 <a href="javascript:if(confirm('Are you sure want to Reset Payment \'No. SPAJ : ${p.spaj_no}\' ?'))window.location='${path}/keuangan/input/reset/${p.id}';" title="RESET Payment">
					                      <img src="${path }/static/pilu/images/icons/back_undo.png" alt="RESET Payment" />
					                   </a>
								</c:when>
								<c:otherwise>
									
								</c:otherwise>
							</c:choose>
	                      
		                   <a href="${path}/keuangan/input/view/${p.id}" title="view">
		                      <img src="${path }/static/pilu/images/icons/view.png" alt="View" />
		                   </a>
		                   
							<c:if test="${not empty p.tgl_paid and p.posisi eq 5 }">
							    <a href="javascript:if(confirm('Are you sure want to Transfer \'No SPAJ : ${p.spaj_no}\' ?'))window.location='${path}/keuangan/input/transfer/${p.id}';" title="transfer">
			                      <img src="${path }/static/pilu/images/icons/expanded.png" alt="transfer" />
			                    </a>
							</c:if>
								
		                    
						</td>
					</tr>
					</c:forEach>
				</table> 
              		
		                   	
                <span class="page">Page : ${page } </span>
               
	          
	                <div class="actions-bar wat-cf">
	                  <div class="actions">
	                  	<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
							<img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
						</button>
	                  </div>
              		</div>
          		
          	

			</div>
            </td>
            </tr>
            </table>
            </form>
          </div>
          
        </div>
		
	</div>
		
	
		

	


		
		
		
	
	
</body>
</html>