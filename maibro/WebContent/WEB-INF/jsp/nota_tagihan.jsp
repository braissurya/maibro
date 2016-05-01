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
					<li class="headerTitle"><a href="#">Nota Tagihan</a>
					</li>
				</ul>
			</div>

			<div class="content">
				<div class="inner">
					<form method="post" target="_blank">
						<div class="columns wat-cf">
							<table class="report">
								<tr>
									<th><label>Tgl Terbit Polis</label></th>
									<td>
										<input type="text" name="beg_date1" class="text_field date-my" value="${tgl_awal}">
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
									<th><label>Pejabat</label></th>
									<td>
										<input type="text" name="pejabat" class="text_field" value="">
									</td>
								</tr>
								<tr>
									<th><label>Format Laporan</label></th>
									<td>
										<select name="format">
											<optgroup label="Others">
												<option value="pdf">Adobe Reader (.pdf)</option>
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