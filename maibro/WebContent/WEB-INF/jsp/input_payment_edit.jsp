<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
<script type="text/javascript">
	$(document).ready(function() {
		autoPopulateSelect("${path}", "bank", "#mst_bank", "#mst_cab_bank", true, "${policy.bank.mst_cab_bank_id}", "");
		$("#mst_bank").change();
	});
</script>
</head>
<body>

	<div id="main" class="fullwidth">
		<div class="block" id="block-forms">
			<div class="secondary-navigation">
				<ul class="wat-cf">
					<li class="headerTitle"><a href="#"> ${policy.mode} Micro</a> 
					</li>
				</ul>
			</div>

			<div class="content">
				<div class="inner">
					<form:form commandName="policy" name="formpost" method="POST" action="${path}/keuangan/input/save" cssClass="form">
						<c:choose>
							<c:when
								test="${messageType eq 'error' or messageType eq 'warning' }">
								<div class="flash">
									<div class="message ${messageType}">
										<p>${message}</p>
									</div>
								</div>
							</c:when>
						</c:choose>

						<div class="columns wat-cf">
							<fieldset>
								<legend>Data Nasabah</legend>
							
									<div class="group">
					                   <div class="fieldWithErrors">
						                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID </form:label>
						                    <form:errors path="id" cssClass="error" />
						               </div>
					                
											<form:hidden path="id" /><input type="text" class="text_field_read" value="${policy.id }" readonly="readonly" />
									 
					                  <span class="description"></span>
					                </div>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="product.mst_product_id" cssClass="label" cssErrorClass="label labelError">Nama Produk</form:label>
											<form:errors path="product.mst_product_id" cssClass="error" />
										</div>
										 <c:choose>
											<c:when test="${policy.mode eq 'VIEW'}"><form:hidden path="product.mst_product_id" /><input type="text" class="text_field_read" value="${policy.product.mst_product_id}" readonly="readonly" /></c:when>
											<c:otherwise>
												<form:select path="product.mst_product_id">
													<form:option value="">Silahkan Pilih Produk</form:option>
													<form:options items="${reff.AllProduct}" itemValue="key" itemLabel="value"/>
												</form:select>
											</c:otherwise>
									   </c:choose>
										<span class="description"></span>
									</div>
									
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="debitur" cssClass="label" cssErrorClass="label labelError">Nama Lengkap</form:label>
											<form:errors path="debitur" cssClass="error" />
										</div>
										<form:input path="debitur" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="40"/>
										<span class="description"></span>
									</div>
									
									
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="beg_date" cssClass="label" cssErrorClass="label labelError">Begdate</form:label>
											<form:errors path="beg_date" cssClass="error" />
										</div>
										<form:input path="beg_date" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError" />
									</div>
									
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="ins_period" cssClass="label" cssErrorClass="label labelError">Lama Bulan</form:label>
											<form:errors path="ins_period" cssClass="error" />
										</div>
										<form:input path="ins_period" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="product.rate" cssClass="label" cssErrorClass="label labelError">Rate Asuransi</form:label>
											<form:errors path="product.rate" cssClass="error" />
										</div>
										<form:input path="product.rate" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="product.premi" cssClass="label" cssErrorClass="label labelError">Premi Asuransi</form:label>
											<form:errors path="product.premi" cssClass="error" />
										</div>
										<form:input path="product.premi" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="plafon_kredit" cssClass="label" cssErrorClass="label labelError">Plafon Kredit</form:label>
											<form:errors path="plafon_kredit" cssClass="error" />
										</div>
										<form:input path="plafon_kredit" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="gol_debitur" cssClass="label" cssErrorClass="label labelError">Golongan Debitur</form:label>
											<form:errors path="gol_debitur" cssClass="error" />
										</div>
										<form:input path="gol_debitur" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="kode_cabang" cssClass="label" cssErrorClass="label labelError">Kode Cabang</form:label>
											<form:errors path="kode_cabang" cssClass="error" />
										</div>
										<form:input path="kode_cabang" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div>
							
							</fieldset>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="product.tgl_paid" cssClass="label" cssErrorClass="label labelError">Tanggal Pembayaran</form:label>
									<form:errors path="product.tgl_paid" cssClass="error" />
								</div>
								<form:input path="product.tgl_paid" cssClass="text_field datepicker" cssErrorClass="text_field inputError datepicker" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="product.nominal_paid" cssClass="label" cssErrorClass="label labelError">Jumlah Pembayaran</form:label>
									<form:errors path="product.nominal_paid" cssClass="error" />
								</div>
								<form:input path="product.nominal_paid" cssClass="text_field" cssErrorClass="text_field inputError" />
							</div>
							
							
							
						</div>
						<div class="group navform wat-cf">

							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
								<img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
							</button>
							<span class="text_button_padding"></span>
							 <form:hidden path="mode"/>
							<button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/keuangan/input'">
								<img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
							</button>

						</div>
					</form:form>

				</div>
			</div>
		</div>
	</div>

</body>
</html>
