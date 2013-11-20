<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
<script type="text/javascript">
	$(document).ready(function() {
		autoPopulateSelect("${path}", "bank", "#mst_bank", "#mst_cab_bank", true, "${groupPolicy.polisMicro.bank.mst_cab_bank_id}", "");
		$("#mst_bank").change();
	});
</script>
</head>
<body>

	<div id="main" class="fullwidth">
		<div class="block" id="block-forms">
			<div class="secondary-navigation">
				<ul class="wat-cf">
					<li class="headerTitle"><a href="#"> ${groupPolicy.mode} Micro</a> 
					</li>
				</ul>
			</div>

			<div class="content">
				<div class="inner">
					<form:form commandName="groupPolicy" name="formpost" method="POST" action="${path}/micro/save/${groupPolicy.pagename}" cssClass="form">
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
							<div class="group">
			                   <div class="fieldWithErrors">
				                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID </form:label>
				                    <form:errors path="id" cssClass="error" />
				               </div>
			                
									<form:hidden path="polisMicro.id" /><input type="text" class="text_field_read" value="${groupPolicy.polisMicro.id }" readonly="readonly" />
							 
			                  <span class="description"></span>
			                </div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.product.mst_product_id" cssClass="label" cssErrorClass="label labelError">Nama Produk</form:label>
									<form:errors path="polisMicro.product.mst_product_id" cssClass="error" />
								</div>
								 <c:choose>
									<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisMicro.product.mst_product_id" /><input type="text" class="text_field_read" value="${groupPolicy.polisMicro.product.mst_product_id}" readonly="readonly" /></c:when>
									<c:otherwise>
										<form:select path="polisMicro.product.mst_product_id">
											<form:option value="">Silahkan Pilih Produk</form:option>
											<form:options items="${reff.listProductLife}" itemValue="key" itemLabel="value"/>
										</form:select>
									</c:otherwise>
							   </c:choose>
								<span class="description"></span>
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.dealref" cssClass="label" cssErrorClass="label labelError">Deal Reff</form:label>
									<form:errors path="polisMicro.dealref" cssClass="error" />
								</div>
								<form:input path="polisMicro.dealref" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="40"/>
								<span class="description"></span>
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.customer.nama" cssClass="label" cssErrorClass="label labelError">Nama Lengkap</form:label>
									<form:errors path="polisMicro.customer.nama" cssClass="error" />
								</div>
								<form:input path="polisMicro.customer.nama" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="40"/>
								<span class="description"></span>
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.customer.tempat_lahir" cssClass="label" cssErrorClass="label labelError">Tempat Lahir</form:label>
									<form:errors path="polisMicro.customer.tempat_lahir" cssClass="error" />
								</div>
								<form:input path="polisMicro.customer.tempat_lahir" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40"/>
								<span class="description"></span>
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.customer.tgl_lahir" cssClass="label" cssErrorClass="label labelError">Tanggal Lahir </form:label>
									<form:errors path="polisMicro.customer.tgl_lahir" cssClass="error" />
								</div>
								<form:input path="polisMicro.customer.tgl_lahir" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError"/>
							</div>
							
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.beg_date" cssClass="label" cssErrorClass="label labelError">Tanggal Realisasi</form:label>
									<form:errors path="polisMicro.beg_date" cssClass="error" />
								</div>
								<form:input path="polisMicro.beg_date" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.customer.pekerjaan" cssClass="label" cssErrorClass="label labelError">Pekerjaan </form:label>
									<form:errors path="polisMicro.customer.pekerjaan" cssClass="error" />
								</div>
								<form:input path="polisMicro.customer.pekerjaan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40"/>
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.ins_period" cssClass="label" cssErrorClass="label labelError">Lama Bulan</form:label>
									<form:errors path="polisMicro.ins_period" cssClass="error" />
								</div>
								<form:input path="polisMicro.ins_period" cssClass="text_field" cssErrorClass="text_field inputError" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.product.rate" cssClass="label" cssErrorClass="label labelError">Rate Asuransi</form:label>
									<form:errors path="polisMicro.product.rate" cssClass="error" />
								</div>
								<form:input path="polisMicro.product.rate" cssClass="text_field" cssErrorClass="text_field inputError" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.product.premi" cssClass="label" cssErrorClass="label labelError">Premi Asuransi</form:label>
									<form:errors path="polisMicro.product.premi" cssClass="error" />
								</div>
								<form:input path="polisMicro.product.premi" cssClass="text_field" cssErrorClass="text_field inputError" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.plafon_kredit" cssClass="label" cssErrorClass="label labelError">Plafon Kredit</form:label>
									<form:errors path="polisMicro.plafon_kredit" cssClass="error" />
								</div>
								<form:input path="polisMicro.plafon_kredit" cssClass="text_field" cssErrorClass="text_field inputError" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.gol_debitur" cssClass="label" cssErrorClass="label labelError">Golongan Debitur</form:label>
									<form:errors path="polisMicro.gol_debitur" cssClass="error" />
								</div>
								<form:input path="polisMicro.gol_debitur" cssClass="text_field" cssErrorClass="text_field inputError" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.kode_cabang" cssClass="label" cssErrorClass="label labelError">Kode Cabang</form:label>
									<form:errors path="polisMicro.kode_cabang" cssClass="error" />
								</div>
								<form:input path="polisMicro.kode_cabang" cssClass="text_field" cssErrorClass="text_field inputError" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.username" cssClass="label" cssErrorClass="label labelError">User Name</form:label>
									<form:errors path="polisMicro.username" cssClass="error" />
								</div>
								<form:input path="polisMicro.username" cssClass="text_field" cssErrorClass="text_field inputError" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.customer.address.alamat1" cssClass="label" cssErrorClass="label labelError">Alamat </form:label>
									<form:errors path="polisMicro.customer.address.alamat1" cssClass="error" />
								</div>
								<form:input path="polisMicro.customer.address.alamat1" cssClass="text_field" cssErrorClass="text_field inputError" size="100" /><br/><br/>
								<form:input path="polisMicro.customer.address.alamat2" cssClass="text_field" cssErrorClass="text_field inputError" size="100" /><br/><br/>
								<form:input path="polisMicro.customer.address.alamat3" cssClass="text_field" cssErrorClass="text_field inputError" size="100" /><br/><br/>
								<form:input path="polisMicro.customer.address.alamat4" cssClass="text_field" cssErrorClass="text_field inputError" size="100" /><br/><br/>
								<form:input path="polisMicro.customer.address.alamat5" cssClass="text_field" cssErrorClass="text_field inputError" size="100" />
							</div>
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.customer.address.kodepos_rumah" cssClass="label" cssErrorClass="label labelError">Kode Pos</form:label>
									<form:errors path="polisMicro.customer.address.kodepos_rumah" cssClass="error" />
								</div>
								<form:input path="polisMicro.customer.address.kodepos_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
							</div>
							
							<c:if test="${groupPolicy.mode eq 'VALIDASI'}">
							<div class="group">
								<div class="fieldWithErrors">
									<form:label path="polisMicro.asuransi_id" cssClass="label" cssErrorClass="label labelError">Pilih Asuransi</form:label>
									<form:errors path="polisMicro.asuransi_id" cssClass="error" />
								</div>
								 <c:choose>
									<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisMicro.asuransi_id" />
									<input type="text" class="text_field_read" value="
									<c:forEach items="${reff.listAsuransiLife}" var="v">
										<c:if test="${v.key eq groupPolicy.polisMicro.asuransi_id }">
											${v.value }
										</c:if>
									</c:forEach>
									" readonly="readonly" /></c:when>
									<c:otherwise>
										<form:select path="polisMicro.asuransi_id">
											<form:option value="">Silahkan Pilih Asuransi</form:option>
											<form:options items="${reff.listAsuransiLife}" itemValue="key" itemLabel="value"/>
										</form:select>
									</c:otherwise>
							   </c:choose>
								<span class="description"></span>
							</div>
						</c:if>
						</div>
						<div class="group navform wat-cf">

							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
								<img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
							</button>
							<span class="text_button_padding"></span>
							 <form:hidden path="mode"/>
							 <form:hidden path="microCheck"/>
							<button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/micro/input'">
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
