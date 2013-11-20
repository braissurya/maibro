<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
<script type="text/javascript">
	$(document).ready(function() {
		autoPopulateSelect("${path}", "bank", "#bank_id", "#cab_bank_id", true, "${cab_bank_id}", "");
		$("#bank_id").change();
	});
</script>
</head>
<body>

	<div id="main" class="fullwidth">
		<div class="block" id="block-forms">
			<div class="secondary-navigation">
				<ul class="wat-cf">
					<li class="headerTitle"><a href="#"> Laporan Rekapitulasi Peserta Asuransi per Bank</a>
					</li>
				</ul>
			</div>

			<div class="content">
				<div class="inner">
					<form method="post" target="_blank">
						<div class="columns wat-cf">
							<table class="report">
								<tr>
									<th><label>Jenis Report</label></th>
									<td>
										<input type="radio" name="jenis_report" value="1" checked="checked"><label>Summary</label>
										<input type="radio" name="jenis_report" value="2"><label>Detail</label>
									</td>
								</tr>
								<tr>
									<th><label>Periode (Tgl Akseptasi)</label></th>
									<td>
										<input type="text" name="beg_date1" class="text_field datepicker" value=${tgl_awal1}> s/d 
										<input type="text" name="end_date1" class="text_field datepicker" value=${tgl_akhir1}>
									</td>
								</tr>
								<tr>
									<th><label>Periode (Tgl Mulai Asuransi)</label></th>
									<td>
										<input type="text" name="beg_date" class="text_field datepicker" value=${tgl_awal}> s/d 
										<input type="text" name="end_date" class="text_field datepicker" value=${tgl_akhir}>
									</td>
								</tr>
								<tr>
									<th><label>Terbit Polis</label></th>
									<td>
										<input type="radio" name="jenis_terbit" value="1" checked="checked"><label>ALL</label>
										<input type="radio" name="jenis_terbit" value="2"><label>Sudah Terbit Polis</label>
										<input type="radio" name="jenis_terbit" value="3"><label>Belum Terbit Polis</label>
									</td>
								</tr>
								<tr>
									<th><label>Produk</label></th>
									<td>
										<c:choose>
											<c:when test="${not empty product_id}">								
												<select name="jenis_produk" id="jenis_produk">
													<option value="">ALL</option>
													<c:forEach items="${reff.AllProdukGroup}" var="g">
														<option value="${g.key}">${g.value}</option>											
													</c:forEach>
												</select>
											</c:when>
											<c:otherwise>									
												<select name="jenis_produk" id="jenis_produk">
													<option value="">ALL</option>
													<c:forEach items="${reff.AllProduk}" var="g">
														<option value="${g.key}">${g.value}</option>											
													</c:forEach>
												</select>
											</c:otherwise>	
										</c:choose>	
									</td>
								</tr>
								<tr>
									<th><label>Bank</label></th>
									<td>
										<c:choose>
											<c:when test="${jenis_user eq '1'}">
												<input type="hidden" name="bank" id="bank_id" value="${bank_id}" />
												<input type="text" name="bank_nama" id="bank_nama" value="${bank_nama}" disabled="disabled" />
												<c:choose>
													<c:when test="${jenis_user_cab ne '0'}">
														<input type="hidden" name="cab_bank"  value="${cab_id}" />
														<input type="text" name="cab_nama" id="bank_nama" value="${cab_nama}" disabled="disabled" />								
													</c:when>	
													<c:otherwise>
														<select name="cab_bank" id="cab_bank_id">
															<option value="">ALL</option>
														</select>
													</c:otherwise>	
												</c:choose>
											</c:when>
											<c:otherwise>									
												<select name="bank" id="bank_id">
													<option value="">ALL</option>
													<c:forEach items="${reff.AllBank}" var="g">
														<option value="${g.key}">${g.value}</option>											
													</c:forEach>
												</select>
												<select name="cab_bank" id="cab_bank_id">
													<option value="">ALL</option>
												</select>
											</c:otherwise>	
										</c:choose>								
									</td>
								</tr>
								<tr>
									<th><label>Asuransi</label></th>
									<td>
										<c:choose>
											<c:when test="${jenis_user eq '2' or jenis_user eq '4'}">
												<input type="hidden" name="asuransi" id="asuransi_id" value="${bank_id}" />
												<input type="text" name="asuransi_nama" id="asuransi_nama" value="${bank_nama}" disabled="disabled" style="width: 300px;" />
											</c:when>
											<c:otherwise>		
												<select name="asuransi" id="asuransi_id">
																									
													<option value="">ALL</option>
													<c:forEach items="${reff.AllAsuransi}" var="g">
														<option value="${g.key}">${g.value}</option>											
													</c:forEach>
												</select>
											</c:otherwise>	
										</c:choose>			
									</td>
								</tr>
								<tr>
									<th><label>Format Laporan</label></th>
									<td>
										<select name="format">
											<optgroup label="Office">
												<option value="xls">Excel (.xls)</option>
												<option value="xlsx">Excel 2007-2010 (.xlsx)</option>
												<!-- <option value="jxl">jExcel (.xls)</option> -->
												<option value="pptx">Powerpoint 2007-2010 (.pptx)</option>
												<option value="docx">Word 2007-2010 (.docx)</option>
											</optgroup>
											<optgroup label="Others">
												<option value="pdf">Adobe Reader (.pdf)</option>
												<option value="html">HTML (.html)</option>
												<option value="odt">OpenOffice Writer (.odt)</option>
												<option value="jxl">OpenOffice Calc (.ods)</option>
												<option value="rtf">Rich Text Format (.rtf)</option>
												<option value="txt">Text (.txt)</option>
											</optgroup>
										</select>
									</td>
								</tr>
								<tr>
									<th></th>
									<td>
										<button class="button" type="submit" name="show" value="1">
											<img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Show
										</button>
										<span class="text_button_padding"></span>
										<button class="button" type="reset">
											<img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Reset
										</button>
									</td>
								</tr>
							</table>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>	