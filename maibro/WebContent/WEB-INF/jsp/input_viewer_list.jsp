<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
		$(function(){
			function calculate() {	 	
			    $.map($('input:checkbox:checked'), function(e, i) {
			    	if($('#tglview_'+e.value).val()=='')
			        $('#tglview_'+e.value).val('${sysdate}');
			     }); 
			     
				$.map($('input:checkbox:not(:checked)'), function(e, i) {
			    	 $('#tglview_'+e.value).val('');
			    });		   
			}
					
			$('table.table').delegate('input:checkbox', 'click', calculate);
		});
	</script>
	<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('#exportPDFButton').click( function(){
			jQuery('#_FormatExport').val("pdf");
				myPost(reportUrl, getParam());
		});
	
		jQuery('#exportExcelButton').click( function(){
			jQuery('#_FormatExport').val("xls");
				myPost(reportUrl, getParam());
		});
	});
	var reportUrl = "${path}/report/viewer/print";
	function myPost(path, params) {
		var form = document.createElement("form");
		form.setAttribute("method", "POST");
		form.setAttribute("name", "formpost");
		form.setAttribute("target", "_blank");
		form.setAttribute("action", reportUrl);
		for( var key in params ) {
			if( params.hasOwnProperty(key) ) {
				var hiddenField = document.createElement("input");
				hiddenField.setAttribute("type", "hidden");
				hiddenField.setAttribute("name", key);
				hiddenField.setAttribute("value", params[key]);
				form.appendChild(hiddenField);
			}
		}
		
		document.body.appendChild(form);
		form.submit();
	}
	
	function getParam() {
		var params = {};
		params['tgl_aksep'] = jQuery('#id_tgl_aksep').val(),
		params['tgl_aksep_end'] = jQuery('#id_tgl_aksep_end').val(),
		params['begdate'] = jQuery('#id_begdate').val(),
		params['enddate'] = jQuery('#id_enddate').val(),
		params['begdatepaid'] = jQuery('#id_begdatepaid').val(),
		params['enddatepaid'] = jQuery('#id_enddatepaid').val();
		params['format'] = jQuery('#_FormatExport').val();
		params['sort'] = jQuery('#sort').val();
		params['st'] = jQuery('#st').val();
		params['s'] =jQuery('#id_s').val();
		params['jenis_produk'] =$('input[name=jenis_produk]:checked').val();
		params['paid'] =$('input[name=paid]:checked').val();
		
		return params;
	}
	</script>
</head>
<body>
	<form name="form" id="form" method="post">

	<div id="main" class="fullwidth">
	
	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle" ><a href="#"> Viewer</a></li>             
            </ul>
          </div>
		
		
		 <div class="content">
		 	<br/>
		 
		 	<table style="width: 100%" >
		 		
		 		<tr>
		 			<th style="width: 200px;text-align: left;padding-left: 20px" >
		 			Filter :
		 			</th>
		 			<td>
		 					<input type="hidden" name="page" id="page" value="${page }">
                  	<input type="hidden" name="sort" id="sort" value="${sort }">
                  	<input type="hidden" name="st" id="st" value="${sort_type}">
		 			</td>
		 		</tr>
		 		<tr>
		 			<th style="width: 200px;text-align: left;padding-left: 20px" >
		 				<label>Periode (Tgl Akseptasi)  </label>
					</th>
					<td style="text-align: left;">
						<input type="text" value="${tgl_aksep }" name="tgl_aksep" id="id_tgl_aksep" class="text_field datepicker"> s/d 
						<input type="text" value="${tgl_aksep_end }" name="tgl_aksep_end" id="id_tgl_aksep_end"  class="text_field datepicker">
									
		 			</td>
		 		</tr>	
		 		<tr>
		 			<th style="width: 200px;text-align: left;padding-left: 20px" >
		 				<label>Periode (Tgl Mulai Asuransi)  </label>
					</th>
					<td style="text-align: left;">
						<input type="text" value="${begdate }" name="begdate" id="id_begdate" class="text_field datepicker"> s/d 
						<input type="text" value="${enddate }" name="enddate" id="id_enddate" class="text_field datepicker">
									
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
						<c:choose>
							<c:when test="${not empty product_id}">								
								<input type="radio" name="jenis_produk" value="0" class="radio" <c:if test="${jenis_produk eq \"0\" }" >checked="checked"</c:if>><label>ALL</label>	
								<c:forEach items="${reff.AllProdukGroup}" var="p">
									<input type="radio" name="jenis_produk" value="${p.key}" class="radio" <c:if test="${jenis_produk eq p.key }" >checked="checked"</c:if>><label>${p.value }</label>
								</c:forEach>	
							</c:when>
							<c:otherwise>									
								<input type="radio" name="jenis_produk" value="0" class="radio" <c:if test="${jenis_produk eq \"0\" }" >checked="checked"</c:if>><label>ALL</label>	
								<c:forEach items="${reff.AllProduct}" var="p">
									<input type="radio" name="jenis_produk" value="${p.key}" class="radio" <c:if test="${jenis_produk eq p.key }" >checked="checked"</c:if>><label>${p.value }</label>
								</c:forEach>	
							</c:otherwise>	
						</c:choose>			
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
						<input type="radio" name="paid" value="0" <c:if test="${paid eq \"0\" }">checked="checked"</c:if>><label>Outstanding</label>
					</td>
				</tr>
				<tr class="tglpaid"  >
		 			<th style="width: 200px;text-align: left;padding-left: 20px" >
		 				<label>Periode (Pembayaran)  </label>
					</th>
					<td style="text-align: left;">
						<input type="text" value="${begdatepaid }" name="begdatepaid" id="id_begdatepaid" class="text_field datepicker"> s/d 
						<input type="text" value="${enddatepaid }" name="enddatepaid" id="id_enddatepaid"  class="text_field datepicker">
									
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
						<input id="exportPDFButton" type="button"  value="Export to PDF">
						<input id="exportExcelButton" type="button"  value="Export to Excel">
						<input type="hidden" id="_FormatExport">
					</td>
				</tr>
		 		<tr>
			 		<td  colspan="2">
			            <div class="total">Total Data : ${totalData } </div>
			            <div class="total">Total Aktif : ${total.aktif } </div>
			            <div class="total">Total Premi : <fmt:formatNumber>${total.premi } </fmt:formatNumber> </div>
			           <c:if test="${sessionScope.currentUser.bank_jenis ne 1}">
			            <div class="total">Total Net Premi : <fmt:formatNumber>${total.premi_net } </fmt:formatNumber> </div>
			            <div class="total">Total Bayar : <fmt:formatNumber>${total.bayar } </fmt:formatNumber> </div>
			           </c:if> 
			            <div class="search">
			            	<input type="text" name="s" id="id_s"  value="${search}" size="30" class="textfield">
			            	<input type="submit" name="btsearch"  value=" " class="btsearch" title="search">
			            	
			            </div>
		           	</td>
            	</tr>
     </table>
 	</form>
 	 <form name="form" id="form" method="post" action="${path}/viewer/saveAll">
 	<table>
            <tr>
		 		<td colspan="2">
           		 <div class="inner">
      				
            		
	          		<table class="table" >
						<tr>
							<th class="first"></th>
							<c:if test="${sessionScope.currentUser.bank_jenis ne 1}"><th >View</th></c:if>
							<th >Data Polis</th>
							<th>Status Bayar</th>
							<th>Jumlah Bayar</th>
							<th>Tanggal Bayar</th>
							<th>
								<c:choose>
			                    	<c:when test="${sort eq 'namacabbank'}">
				                    	<c:choose>
							              	<c:when test="${sort_type eq 'asc' }"> <a href="javascript:gotoPage('${page}','namacabbank','desc','form')" class="up" >Bank</a></c:when>
							              	<c:when test="${sort_type eq 'desc' }"><a href="javascript:gotoPage('${page}','namacabbank','asc','form')" class="down" >Bank</a></c:when>
							              	<c:otherwise><a href="javascript:gotoPage('${page}','namacabbank','asc','form')"  >Bank</a></c:otherwise>
						              	</c:choose>
						            </c:when>
					              	<c:otherwise>
					              		<a href="javascript:gotoPage('${page}','namacabbank','asc','form')"  >Bank</a>
					              	</c:otherwise>
				              	</c:choose>
							</th>
							<th>Asuransi</th>
							<th>Posisi</th>
							<th class="last">Actions</th>
						</tr>
						
						<c:forEach items="${listPolicy}" var="p">
						<%-- <tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>> --%>
						<tr <c:if test="${p.posisi eq 95}">class="merah"</c:if>>
							<td></td>
							<c:if test="${sessionScope.currentUser.bank_jenis ne 1}">
							<td>
								
								<c:choose>
									<c:when test="${not empty p.tgl_view_asuransi}">
										<input type="checkbox" name="flag_view" checked="checked" value="${p.id}" disabled="disabled"  > 
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="flag_view"  value="${p.id}" onclick="form.submit();"> 
									</c:otherwise>
								</c:choose>
								
							</td>
							</c:if>
							<td style="text-align: left;vertical-align: top;" >
								<table class="inTable">
									<tr>
										<td style="border: none;width: 50%;">
											<table class="inTable">
												<tr><th nowrap="nowrap">No. Spaj</th><th> :</th><td nowrap="nowrap"> ${p.spaj_no}</td> </tr>
												<c:if test="${p.jenis eq 3}"><tr><th nowrap="nowrap">No. PK </th><th> :</th><td nowrap="nowrap"> ${p.dealref}</td> </tr></c:if>
												<c:if test="${not empty p.policy_no}"><tr><th nowrap="nowrap">No. Polis </th><th> :</th><td nowrap="nowrap"> ${p.policy_no}</td> </tr></c:if>
												<tr><th nowrap="nowrap">Produk</th><th> :</th><td nowrap="nowrap">${p.produk}</td> </tr>
												<tr><th nowrap="nowrap">Debitur </th><th> :</th><td  > ${p.debitur}</td> </tr>
												<tr><th nowrap="nowrap">Beg Date </th><th> :</th><td nowrap="nowrap">  <fmt:formatDate value="${p.beg_date}" pattern="dd-MM-yyyy"/></td> </tr>
											</table>
										</td>
										<td  style="border: none;width: 50%;">
											<table class="inTable">
												<tr><th nowrap="nowrap">Plafon Kredit</th><th> :</th><td nowrap="nowrap"><fmt:formatNumber>${p.up }</fmt:formatNumber></td> </tr>
												<tr><th nowrap="nowrap">Rate</th><th> :</th><td nowrap="nowrap"> <fmt:formatNumber>${p.rate }</fmt:formatNumber></td> </tr>
												<tr><th nowrap="nowrap">Premi</th><th> :</th><td nowrap="nowrap"><fmt:formatNumber>${p.premi }</fmt:formatNumber></td> </tr>
												<c:if test="${sessionScope.currentUser.bank_jenis ne 1}">
												<tr><th nowrap="nowrap">Diskon Premi </th><th> :</th><td  nowrap="nowrap"> <fmt:formatNumber>${p.diskon}</fmt:formatNumber></td> </tr>
												<tr><th nowrap="nowrap">PPN </th><th> :</th><td nowrap="nowrap"> <fmt:formatNumber>${p.ppn}</fmt:formatNumber></td> </tr>
												<tr><th nowrap="nowrap">PPH </th><th> :</th><td nowrap="nowrap"> <fmt:formatNumber>${p.pph}</fmt:formatNumber></td> </tr>
												<tr><th nowrap="nowrap">Net Premi </th><th> :</th><td nowrap="nowrap"> <fmt:formatNumber>${p.premi_net}</fmt:formatNumber></td> </tr>
												</c:if>
											</table>
										</td>
									</tr>
								</table>
							</td>
									
							<td>
						
								<c:if test="${not empty p.tgl_paid }">
									<c:choose>
										<c:when test="${p.flag_paid eq 1}">
											Paid
										</c:when>										
										<c:when test="${ p.flag_paid eq 2}">
											Lebih Bayar
										</c:when>
										<c:when test="${p.flag_paid eq 3}">
											Kurang Bayar
										</c:when>
										<c:otherwise>Belum</c:otherwise>
									</c:choose>
									
								</c:if>
								
								
						</td>
							<td><fmt:formatNumber>${p.nominal_paid }</fmt:formatNumber> </td>		
							<td>
								<c:choose>
									<c:when test="${not empty p.tgl_paid }">
										<fmt:formatDate value="${p.tgl_paid}" pattern="dd-MM-yyyy"/>
									</c:when>
									<c:otherwise>
										
									</c:otherwise>
								</c:choose>
							</td>
							<td>${p.namabank}<br/>${p.namacabbank}</td>
							<td>${p.namaasuransi}</td>
							<td>
								<%-- Request 03052016 --  Field (Posisi) di Viewer & Report saat di Export, tahapan harus sesuai denganproses alur data, misal, proses baru di input dr sisi bank maka sts "Posisi"=Input Bank dan apabila sudah terbit polis dan sudahada pembayaran maka sts "Posisi"=Filling yang artinya proses pengajuan data dar "A" sd "Z" sudah selesai.
								<c:choose>
									<c:when test="${sessionScope.currentUser.bank_jenis eq 1}">
										<c:choose>
											<c:when test="${p.posisi gt 1 }">Filling</c:when>
											<c:otherwise>${p.posisiKet}</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>${p.posisiKet}</c:otherwise>
								</c:choose> --%>
								${p.posisiKet}
							</td>
							<td class="last">
                    			<c:if test="${sessionScope.currentUser.bank_jenis ne\"1\" }"> <%-- BILA USER BUKAN BANK --%>
									<c:if test="${not empty p.tgl_upload_spaj }">
			                      		<a href="javascript:if(confirm('Are you sure want to Print SPAJ Upload \'No. SPAJ : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/sppa_upload','800px','800px',false,false);}" title="print spaj upload">
					                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print spaj Upload" />
					                    </a>
				                    </c:if>
				                     <c:if test="${not empty p.tgl_upload_kuesioner }">
					                    <a href="javascript:popWin('${path}/kpr${jenis}/kuesioner/${p.id}','800px','800px',false,false);" title="print KUESIONER upload">
					                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print KUESIONER Upload" />
					                    </a>
					                </c:if>
					                 <c:if test="${not empty p.tgl_upload_ktp }">
										<a href="javascript:popWin('${path}/kpr${jenis}/ktp/${p.id}','800px','800px',false,false);" title="print KTP upload">
					                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print KTP Upload" />
					                    </a>	
				                    </c:if>
				                     <c:if test="${p.noclaim gt 60 and not empty p.tgl_upload_no_klaim }">
				                     	 <a href="javascript:if(confirm('Are you sure want to Print Subject To No Claim Upload \'No. SPAJ : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/NOCLAIM','800px','800px',false,false);timedRefresh(5000);}" title="print Subject To No Claim upload">
					                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="print Subject To No Claim Upload" />
					                    </a>
				                     </c:if>
				                    <c:choose>
			                     		<c:when test="${p.jenis eq\"1\" and p.posisi gt 5 }">
			                     			<a href="javascript:if(confirm('Are you sure want to Print KPS \'No. SPAJ : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/polis_life','800px','800px',false,false);}" title="Cetak KPS">
						                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="Cetak KPS/Polis" />
						                    </a>
			                     		</c:when>
			                     		<c:when test="${p.jenis eq\"2\" and p.posisi gt 4}">
			                     			<a href="javascript:if(confirm('Are you sure want to Print Polis \'No. SPAJ : ${p.spaj_no}\' ?')){popWin('${path}/report/print/${p.id}/polis_fire','800px','800px',false,false);}" title="Cetak Polis">
						                      <img src="${path }/static/pilu/images/icons/noteprint.gif" alt="Cetak Polis" />
						                    </a>
			                     		</c:when>
			                     	</c:choose>
			                     </c:if>
                    			<c:if test="${sessionScope.currentUser.bank_jenis eq \"3\" and p.posisi ne 95}"> <%-- BILA USER MAIBRO --%>
	                     			<a href="${path}/cancel/${p.id}" onclick="return confirm('Are you sure want to CANCEL POLIS \'No. SPAJ : ${p.spaj_no}\' ?')" title="CANCEL Polis">
				                      <img src="${path }/static/pilu/images/icons/cross.png" alt="Batal Polis" />
				                    </a>
                    			</c:if>
								<%-- <c:if test="${not empty p.tgl_paid }">
				                    <a href="${path}/klaim/new?spaj=${p.spaj_no}&cari=klaim" title="Input Klaim">
				                      <img src="${path }/static/pilu/images/icons/coins_add.gif" alt="Input Klaim" />
				                   </a>
			                   </c:if> --%>
			                   <a href="${path}/viewer/view/${p.id}" title="view">
			                      <img src="${path }/static/pilu/images/icons/view.png" alt="View" />
			                   </a>
			                   
			                   
			                   
							</td>
						</tr>
						</c:forEach>
					</table> 
	              		
	                <span class="page">Page : ${page } </span>
	                    	
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