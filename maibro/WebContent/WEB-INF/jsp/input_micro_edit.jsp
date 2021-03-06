<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
<script type="text/javascript">
	//copas value dari src ke dst
	function copas(){
		isi = $("#mst_bank_id option:selected").text();
		$("#namaBank").val(isi);
		isi2 = $("#mst_cab_bank_id option:selected").text();
		$("#namaCabang").val(isi2);
	
	}
	//copas value dari src ke dst
	
	//fungsi2 yang dijalankan saat document sudah semua loaded
	$(document).ready(function() {
		//autopopulate cab_bank sesuai bank yg dipilih
		autoPopulateSelect("${path}", "bank", "#mst_bank_id", "#mst_cab_bank_id", true, "${groupPolicy.mst_cab_bank_id}", "");
		//jalankan fungsi2 awal untuk set nilai2
		$("#mst_bank_id").change();
		
		//copy value dari bank dan cabangnya ke textbox dibagian input polis
		$("#mst_bank_id").blur( copas );
		$("#mst_cab_bank_id").blur( copas );
		

		if('${sessionScope.currentUser.cab_bank_jenis}'!=0&'${sessionScope.currentUser.bank_jenis}'!=3){
			$("#mst_bank_id").attr("disabled","disabled");
			$("#mst_cab_bank_id").attr("disabled","disabled");
		}
		
		
		//pesan error diletakkan di alert juga
		var message = "${message}".replace(/<br\s*[\/]?>/gi, "\n");
		if(message != ''){
		 	alert(message);
		}
	});
</script>
</head>
<body>

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

	<div id="main" class="fullwidth">
		<div class="block" id="block-forms3">

			<div class="secondary-navigation">
				<ul class="wat-cf">
					<li class="headerTitle">
						<a href="#"> Informasi Bank</a>
					</li>
				</ul>
			</div>
			
			<div class="content">
				<div class="inner">
					<div class="columns wat-cf">
						<table class="inputan">
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="nama_agent" cssClass="label" cssErrorClass="label labelError">Nama Penanggung Jawab</form:label>
											<form:errors path="nama_agent" cssClass="error" />
										</div>
										<form:input path="nama_agent" cssClass="text_field" cssErrorClass="text_field inputError" />
										<span class="description"></span>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="telp_agent" cssClass="label" cssErrorClass="label labelError">No HP</form:label>
											<form:errors path="telp_agent" cssClass="error" />
										</div>
										<form:input path="telp_agent" cssClass="text_field" cssErrorClass="text_field inputError" />
										<span class="description"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="mst_bank_id" cssClass="label" cssErrorClass="label labelError">Bank (Kreditur)<span class="mandatory" title="Wajib diisi">*</span></form:label>
											<form:errors path="mst_bank_id" cssClass="error" />
										</div>
										
										<form:select  path="mst_bank_id" cssErrorClass="inputError" disabled="disabled">
											<form:option value=""></form:option>
											<form:options items="${reff.listBank}" itemValue="key" itemLabel="value"/>
										</form:select>
										<form:hidden path="mst_bank_id"/>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="mst_cab_bank_id" cssClass="label" cssErrorClass="label labelError">KC/KCP<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="mst_cab_bank_id" cssClass="error" />
										</div>
										<form:select path="mst_cab_bank_id" disabled="disabled" cssErrorClass="inputError"/>
										<form:hidden path="mst_cab_bank_id"/>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>

		</div>
		
		<div class="block" id="block-forms">
			<div class="secondary-navigation">
				<ul class="wat-cf">
					<li class="headerTitle">
						<a> Input Asuransi Jiwa (Life)</a>
					</li>
				</ul>
			</div>

			<div id="contentLife" class="content">
				<div class="inner">
					<div class="columns wat-cf">
						<table class="inputan">
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.product.mst_product_id" cssClass="label" cssErrorClass="label labelError">Nama Produk<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.product.mst_product_id" cssClass="error" />
											<form:hidden path="polisMicro.id"/>
											<form:hidden path="polisMicro.beneficiary.policy_id"/>
											<form:hidden path="polisMicro.agent.policy_id"/>
											<form:hidden path="polisMicro.product.policy_id"/>
											<form:hidden path="polisMicro.customer.id"/>
											<form:hidden path="polisMicro.customer.address.id"/>
										</div>
										 <c:choose>
											<c:when test="${groupPolicy.polisMicro.mode eq 'VIEW'}"><form:hidden path="polisMicro.product.mst_product_id" /><input type="text" class="text_field_read" value="${groupPolicy.polisMicro.product.mst_product_id}" readonly="readonly" /></c:when>
											<c:otherwise>
												<form:select path="polisMicro.product.mst_product_id" cssErrorClass="inputError">
													<form:option value="">Silahkan Pilih Produk</form:option>
													<form:options items="${reff.listProductLife}" itemValue="key" itemLabel="value"/>
												</form:select>
											</c:otherwise>
									   </c:choose>
										<span class="description"></span>
									</div></td>
								<td class="gap"></td>
								<td>
								</td>
								<td class="gap"></td>
								<td></td>
							</tr>
						</table>
						<h3>I. Data Calon Tertanggung / Debitur </h3>
						<table class="inputan">
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.dealref" cssClass="label" cssErrorClass="label labelError">No PK / No Pengajuan</form:label>
											<form:errors path="polisMicro.dealref" cssClass="error" />
										</div>
										<form:input path="polisMicro.dealref" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="50" size="50"/>
										<span class="description"></span>
									</div></td>
								<td class="gap"></td>
								<td>
								</td>
								<td class="gap"></td>
								<td></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.nama" cssClass="label" cssErrorClass="label labelError">Nama Lengkap<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.customer.nama" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.nama" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="40"/>
										<span class="description"></span>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.gender" cssClass="label" cssErrorClass="label labelError">Jenis Kelamin</form:label>
											<form:errors path="polisMicro.customer.gender" cssClass="error" />
										</div>
										<div>
											<form:radiobutton id="genderp" path="polisMicro.customer.gender" value="P" cssClass="radio" />
											<label for="genderp">Pria</label>
										</div>
										<div>
											<form:radiobutton id="genderw" path="polisMicro.customer.gender" value="W" cssClass="radio" />
											<label for="genderw">Wanita</label>
										</div>
										<span class="description"></span>
									</div></td>
								<td class="gap"></td>
								<td></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.tempat_lahir" cssClass="label" cssErrorClass="label labelError">Tempat Lahir<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.customer.tempat_lahir" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.tempat_lahir" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40"/>
										<span class="description"></span>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.tgl_lahir" cssClass="label" cssErrorClass="label labelError">Tanggal Lahir<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.customer.tgl_lahir" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.tgl_lahir" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError"/>
										<span class="description">(dd-MM-yyyy)</span>
									</div></td>
								<td class="gap"></td>
								<td></td>
							</tr>
							<%-- <tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.berat_badan" cssClass="label" cssErrorClass="label labelError">Berat Badan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.customer.berat_badan" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.berat_badan" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" maxlength="6" size="6" />
										<span class="description">(kg)</span>										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.tinggi_badan" cssClass="label" cssErrorClass="label labelError">Tinggi Badan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.customer.tinggi_badan" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.tinggi_badan" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" maxlength="6" size="6" />
										<span class="description">(cm)</span>										
									</div></td>
								<td class="gap"></td>
								<td></td>
							</tr> --%>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.pekerjaan" cssClass="label" cssErrorClass="label labelError">Pekerjaan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.customer.pekerjaan" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.pekerjaan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="100" size="40"/>
									</div></td>
								<td class="gap"></td>
								<td colspan="3">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.bagian" cssClass="label" cssErrorClass="label labelError">Perusahaan dan Bagian<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.customer.bagian" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.bagian" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="65"/>
									</div></td>
							</tr>
							<%-- <tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.address.alamat_kantor" cssClass="label" cssErrorClass="label labelError">Alamat Tempat Kerja </form:label>
											<form:errors path="polisMicro.customer.address.alamat_kantor" cssClass="error" />
										</div>
										<form:textarea path="polisMicro.customer.address.alamat_kantor" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.address.kota_kantor" cssClass="label" cssErrorClass="label labelError">Kota </form:label>
											<form:errors path="polisMicro.customer.address.kota_kantor" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.address.kota_kantor" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.address.kodepos_kantor" cssClass="label" cssErrorClass="label labelError">Kode Pos </form:label>
											<form:errors path="polisMicro.customer.address.kodepos_kantor" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.address.kodepos_kantor" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
							</tr> --%>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.address.alamat_rumah" cssClass="label" cssErrorClass="label labelError">Alamat Tempat Tinggal<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.customer.address.alamat_rumah" cssClass="error" />
										</div>
										<form:textarea path="polisMicro.customer.address.alamat_rumah" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>
									</div></td>
								<td class="gap"></td>
								<td>
									<%-- <div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.address.kota_rumah" cssClass="label" cssErrorClass="label labelError">Kota</form:label>
											<form:errors path="polisMicro.customer.address.kota_rumah" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.address.kota_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div> --%></td>
								<td class="gap"></td>
								<td>
									<%-- <div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.address.kodepos_rumah" cssClass="label" cssErrorClass="label labelError">Kode Pos</form:label>
											<form:errors path="polisMicro.customer.address.kodepos_rumah" cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.address.kodepos_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div> --%></td>
							</tr>
							<%-- <tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.address.telp_kantor"
												cssClass="label" cssErrorClass="label labelError">Telp Kantor</form:label>
											<form:errors path="polisMicro.customer.address.telp_kantor"
												cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.address.telp_kantor"
											cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.address.telp_hp" cssClass="label" cssErrorClass="label labelError">Telp HP</form:label>
											<form:errors path="polisMicro.customer.address.telp_hp"
												cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.address.telp_hp"
											cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.customer.address.telp_rumah"
												cssClass="label" cssErrorClass="label labelError">Telp Rumah</form:label>
											<form:errors path="polisMicro.customer.address.telp_rumah"
												cssClass="error" />
										</div>
										<form:input path="polisMicro.customer.address.telp_rumah"
											cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
							</tr> --%>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.plafon_kredit" cssClass="label" cssErrorClass="label labelError">Jumlah Kredit/Plafon Pinjaman<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.plafon_kredit" cssClass="error" />
										</div>
										<form:input path="polisMicro.plafon_kredit" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" />
										<span class="description">(dalam Rupiah)</span>										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.product.premi" cssClass="label" cssErrorClass="label labelError">Premi Asuransi</form:label>
											<form:errors path="polisMicro.product.premi" cssClass="error" />
										</div>
										<form:input path="polisMicro.product.premi" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" readonly="true"/>
										<span class="description">(dalam Rupiah)</span>
									</div>
								</td>
								<td class="gap"></td>
								<td></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.ins_period" cssClass="label" cssErrorClass="label labelError">Masa Asuransi<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.ins_period" cssClass="error" />
										</div>
										<form:input path="polisMicro.ins_period" cssClass="text_field" cssErrorClass="text_field inputError" />
										<span class="description">(tahun)</span>										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.beg_date" cssClass="label" cssErrorClass="label labelError">Tgl Mulai<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisMicro.beg_date" cssClass="error" />
										</div>
										<form:input path="polisMicro.beg_date" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError" />
										<span class="description">(dd-MM-yyyy)</span>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.end_date" cssClass="label" cssErrorClass="label labelError">Tgl Berakhir</form:label>
											<form:errors path="polisMicro.end_date" cssClass="error" />
										</div>
										<fmt:formatDate value="${groupPolicy.polisMicro.end_date}" pattern="dd-MM-yyyy"/>
									</div></td>
							</tr>
							<tr>
								<td colspan="5">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.jenis_manfaat" cssClass="label" cssErrorClass="label labelError">Manfaat Asuransi</form:label>
											<form:errors path="polisMicro.jenis_manfaat" cssClass="error" />
										</div>
										<form:radiobuttons path="polisMicro.jenis_manfaat" items="${reff.listManfaat}" itemValue="key" itemLabel="value" cssClass="radio"/>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="5">
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Cara Pembayaran</label>
										</div>
										Sekaligus
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Create By</label>
										</div>
										${groupPolicy.polisMicro.createuser}
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Modify By</label>
										</div>
										${groupPolicy.polisMicro.modifyuser}
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Create Date</label>
										</div>
										${groupPolicy.polisMicro.createdate}
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Modify Date</label>
										</div>
										${groupPolicy.polisMicro.modifydate}
									</div></td>
							</tr>
						</table>
						<table class="inputan">
							<caption class="caption">
								<label class="label">Yang Berhak Menerima Uang Asuransi</label>
							</caption>
							<tr>
								<td><label class="label">1.</label>
								</td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Bank (Kreditur)</label>
										</div>
										<form:input path="polisMicro.bank.namaBank" id="namaBank" readonly="true"/>
										<form:hidden path="polisMicro.bank.mst_bank_id"/>
										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">KC/KCP</label>
										</div>
										<form:input path="polisMicro.bank.namaCabang" id="namaCabang" readonly="true" size="30"/>
										<form:hidden path="polisMicro.bank.mst_cab_bank_id"/>
									</div>
								</td>
							</tr>
							<%--
							YUSUF - 11 MEI 2013 - REQ ARIS, BENEF DIHILANGKAN
							<tr>
								<td><label class="label">2.</label>
								</td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.beneficiary.nama" cssClass="label" cssErrorClass="label labelError">Yang Ditunjuk</form:label>
											<form:errors path="polisMicro.beneficiary.nama" cssClass="error" />
										</div>
										<form:input path="polisMicro.beneficiary.nama" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisMicro.beneficiary.relasi" cssClass="label" cssErrorClass="label labelError">Hubungan dengan Debitur</form:label>
											<form:errors path="polisMicro.beneficiary.relasi" cssClass="error" />
										</div>
										<form:select path="polisMicro.beneficiary.relasi" cssErrorClass="inputError">
											<form:option value=""></form:option>
											<form:options items="${reff.listRelasi }" itemLabel="value" itemValue="key"/>
											<form:option value="0">Lainnya</form:option>
										</form:select>
										<div style="display: block;">
											<div class="fieldWithErrors">
												<form:label path="polisMicro.beneficiary.relasi_lain" cssClass="label" cssErrorClass="label labelError">Hubungan Lainnya:</form:label>
												<form:errors path="polisMicro.beneficiary.relasi_lain" cssClass="error" />
											</div>
											<form:input path="polisMicro.beneficiary.relasi_lain" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div>
									</div></td>
							</tr>
							--%>
						</table>
						<%-- <h3>II. KETERANGAN KESEHATAN</h3>
						<table class="inputan">
							<tr>
								<td>1.</td>
								<td><div class="fieldWithErrors">
										Apakah Anda sekarang dalam keadaan sehat? Jika "Tidak", mohon dijelaskan.
										<form:errors path="polisMicro.customer.q1" cssClass="error" />
										<form:errors path="polisMicro.customer.q1_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td style="width: 80px; ">
									<div>
										<form:radiobutton id="q1y" path="polisMicro.customer.q1" value="true" cssClass="radio" />
										<label for="q1y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q1t" path="polisMicro.customer.q1" value="false" cssClass="radio" />
										<label for="q1t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisMicro.customer.q1_desc" cssClass="textarea" cssErrorClass="textarea inputError"/></td>
							</tr>
							<tr>
								<td>2.</td>
								<td><div class="fieldWithErrors">
										Apakah dalam 2 tahun terakhir Anda pernah dioperasi/dirawat
										di rumah sakit atau dalam masa pengobatan/perawatan yang
										membutuhkan obat-obatan dalam masa yang lama? Jika "Ya",
										mohon dijelaskan.
										<form:errors path="polisMicro.customer.q2" cssClass="error" />
										<form:errors path="polisMicro.customer.q2_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div>
										<form:radiobutton id="q2y" path="polisMicro.customer.q2" value="true" />
										<label for="q2y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q2t" path="polisMicro.customer.q2" value="false" />
										<label for="q2t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisMicro.customer.q2_desc" cssClass="textarea" cssErrorClass="textarea inputError"/></td>
							</tr> --%>
							<%--
							<tr>
								<td>3.</td>
								<td><div class="fieldWithErrors">
										Apakah berat badan Anda berubah dalam 12 bulan terakhir? Jika
										"Ya", mohon dijelaskan.
										<form:errors path="polisMicro.customer.q3" cssClass="error" />
										<form:errors path="polisMicro.customer.q3_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div>
										<form:radiobutton id="q3y" path="polisMicro.customer.q3" value="true" />
										<label for="q3y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q3t" path="polisMicro.customer.q3" value="false" />
										<label for="q3t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisMicro.customer.q3_desc" cssClass="textarea" cssErrorClass="textarea inputError" /></td>
							</tr>
							--%>
							<%-- <tr>
								<td>3.</td>
								<td><div class="fieldWithErrors">
										Apakah Anda pernah atau sedang menderita penyakit: cacat,
										tumor/kanker, TBC, paru-paru, asma, kencing manis, hati,
										ginjal, stroke, jantung, tekanan darah tinggi, gangguan jiwa,
										atau penyakit lainnya? Jika "Ya", mohon dijelaskan.
										<form:errors path="polisMicro.customer.q4" cssClass="error" />
										<form:errors path="polisMicro.customer.q4_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div>
										<form:radiobutton id="q4y" path="polisMicro.customer.q4" value="true" />
										<label for="q4y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q4t" path="polisMicro.customer.q4" value="false" />
										<label for="q4t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisMicro.customer.q4_desc" cssClass="textarea" cssErrorClass="textarea inputError" /></td>
							</tr>
							<tr>
								<td>4.</td>
								<td><div class="fieldWithErrors">
										Khusus untuk wanita: Apakah Anda sedang hamil? Jika "Ya",
										berapa minggu usia kandungan Anda?
										<form:errors path="polisMicro.customer.q5" cssClass="error" />
										<form:errors path="polisMicro.customer.q5_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div>
										<form:radiobutton id="q5y" path="polisMicro.customer.q5" value="true" />
										<label for="q5y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q5t" path="polisMicro.customer.q5" value="false" />
										<label for="q5t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisMicro.customer.q5_desc" cssClass="textarea" cssErrorClass="textarea inputError" /></td>
							</tr>
						</table> --%>
							
					</div>

				</div>
			</div>
			</div>
			<c:if test="${groupPolicy.mode eq 'VALIDASI'}">
				<div class="block" id="block-forms3">
	
				<div class="secondary-navigation">
					<ul class="wat-cf">
						<li class="headerTitle">
							<a href="#"> ${groupPolicy.mode} Polis</a>
						</li>
					</ul>
				</div>
				
				<div class="content">
					<div class="inner">
						<div class="columns wat-cf">
							<c:if test="${groupPolicy.mode eq 'VALIDASI'}">
								<br/><br/>
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
								<div class="group">
									<div class="fieldWithErrors">
										<form:label path="polisMicro.flag_akseptasi_mb" cssClass="label" cssErrorClass="label labelError">Status Akseptasi (oleh MaiBro)</form:label>
										<form:errors path="polisMicro.flag_akseptasi_mb" cssClass="error" />
									</div>
									 <c:choose>
										<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisMicro.flag_akseptasi_mb" />
											<form:select path="polisMicro.flag_akseptasi_mb" disabled="true">
												<form:options items="${reff.listStatusAksep}" itemValue="key" itemLabel="value"/>
											</form:select>													
										</c:when>
										<c:otherwise>
											<form:select path="polisMicro.flag_akseptasi_mb">
												<form:options items="${reff.listStatusAksep}" itemValue="key" itemLabel="value"/>
											</form:select>													
										</c:otherwise>
								   </c:choose>
									<span class="description"></span>
								</div>		
								<div class="group">
									<div class="fieldWithErrors">
										<form:label path="polisMicro.asuransi_desc" cssClass="label" cssErrorClass="label labelError">Keterangan</form:label>
										<form:errors path="polisMicro.asuransi_desc" cssClass="error" />
									</div>
									 <c:choose>
										<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisMicro.asuransi_desc" />
										<input type="text" class="text_field_read" value="${groupPolicy.polisMicro.asuransi_desc }" readonly="readonly" /></c:when>
										<c:otherwise>
											<form:textarea path="polisMicro.asuransi_desc"/>
										</c:otherwise>
								   </c:choose>
									<span class="description"></span>
								</div>
								
							</c:if>
						</div>
					</div>
				</div>
			
			</div>
			</c:if>

		
		
		<c:if test="${groupPolicy.mode ne 'VIEW'}">
			<div class="group navform wat-cf">
				<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
					<img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
				</button>
				<span class="text_button_padding"></span>
				 <form:hidden path="mode"/>
				 <form:hidden path="pagename"/>
				  <form:hidden path="microCheck"/>
				  <form:hidden path="lifeCheck"/>
				<button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/micro/${groupPolicy.pagename}'">
					<img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
				</button>
			</div>		
		</c:if>
		<c:if test="${groupPolicy.mode eq 'VIEW'}">
			<div class="group navform wat-cf">
				<button class="button" type="button" onclick="window.location='${path}/micro/${groupPolicy.pagename}'">
                   <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
                 </button>
			</div>		
		</c:if>
		
	</div>

	</form:form>

</body>
</html>
