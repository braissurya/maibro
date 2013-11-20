<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
<script type="text/javascript">

	//show/hide bagian input polis
	function toggle(checkbox, section){
		if( checkbox.is(':checked') ) $(section).show(300);
		else $(section).hide(300);
	}
	
	//copas value dari src ke dst
	function copas(){
		isi = $("#mst_bank_id option:selected").text();
		$("#namaBank").val(isi);
		$("#namaBankFire").val(isi);
		isi2 = $("#mst_cab_bank_id option:selected").text();
		$("#namaCabang").val(isi2);
		$("#namaCabangFire").val(isi2);
		$("#bankerClause").val("Bank bjb Jawa Barat dan Banten " + isi2);
	}
	
	function replaceComma(nilai){
		nilai=nilai.replace(/[^0-9\.]+/g,"");
		if(isNaN(nilai)){
			result=0;
		}else{					
			var result=nilai;
			if(result=="")result=nilai;
		}
		//alert(result);
		return result;
	}
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
		
		//set onclick pada checkbox bagian input polis
		$("#lifeCheck").click( function(){
			toggle($(this), "#contentLife");
		});
		$("#fireCheck").click( function(){
			toggle($(this), "#contentFire");
		});

		//set onclick pada tombol copy debitur
		$("#copyFireToLife").click( function(){
			//field2 yg mau di copy value nya. Karakter titik (.) harus di escape dengan double backslash
			var fields = ["nama","address\\.alamat_rumah","address\\.kota_rumah", "address\\.kodepos_rumah", "address\\.telp_kantor", "address\\.telp_hp", "address\\.telp_rumah"];
			for (i=0;i<fields.length;i++) {
				var src = "#polisFire\\.customer\\." + fields[i];
				var dest = "#polisAJK\\.customer\\." + fields[i];
				$(dest).val($(src).val());
			}
			return false;
		});
		$("#copyLifeToFire").click( function(){
			//field2 yg mau di copy value nya. Karakter titik (.) harus di escape dengan double backslash
			var fields = ["nama","address\\.alamat_rumah","address\\.kota_rumah", "address\\.kodepos_rumah", "address\\.telp_kantor", "address\\.telp_hp", "address\\.telp_rumah"];
			for (i=0;i<fields.length;i++) {
				var src = "#polisAJK\\.customer\\." + fields[i];
				var dest = "#polisFire\\.customer\\." + fields[i];
				$(dest).val($(src).val());
			}
			return false;
		});
		$("#copyAddressInsured").click( function(){
			//field2 yg mau di copy value nya. Karakter titik (.) harus di escape dengan double backslash
			var fields = ["alamat_rumah","kota_rumah", "kodepos_rumah"];
			for (i=0;i<fields.length;i++) {
				var src = "#polisFire\\.customer\\.address\\." + fields[i];
				var dest = "#polisFire\\.customer\\.addressInsured\\." + fields[i];
				$(dest).val($(src).val());
			}
			return false;
		});
		
		//detail harga perabot dll di polis fire
		$("#cekDetail").click( function(){
			if($(this).is(':checked')) {
				$("#detailHarga1").fadeIn();
				$("#detailHarga2").fadeIn();
				$("#detailHarga3").fadeIn();
			}else{
				$("#detailHarga1").fadeOut();
				$("#detailHarga2").fadeOut();
				$("#detailHarga3").fadeOut();
			}
		});
		
		$("#polisFire\\.rate").blur(function(){
			var up=parseFloat(replaceComma($('#polisFire\\.product\\.up').val()));
			var rate=parseFloat(replaceComma($(this).val()));
			if(rate!=null){
				var premi=(rate/1000) * up;
				$('#polisFire\\.premi').val(formatCurrency(premi));
			}
		});
		
		
		$("#polisFire\\.premi").blur(function(){
			var up=parseFloat(replaceComma($('#polisFire\\.product\\.up').val()));
			var premi=parseFloat(replaceComma($(this).val()));
			if(premi!=null){
				var rate=(premi/up) * 1000;
				$('#polisFire\\.rate').val(formatCurrency(rate));
			}
		});
		
		//jalankan fungsi2 awal untuk set nilai2
		toggle($("#lifeCheck"), "#contentLife");
		toggle($("#fireCheck"), "#contentFire");
		
		//pesan error diletakkan di alert juga
		var message = "${message}".replace(/<br\s*[\/]?>/gi, "\n");
		if(message != ''){
		 	alert(message);
		}
	});
</script>
</head>
<body>

	<form:form commandName="groupPolicy" name="formpost" method="POST" action="${path}/kpr/save" cssClass="form">

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
											<form:label path="nama_agent" cssClass="label" cssErrorClass="label labelError">Nama Penanggung Jawab<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="nama_agent" cssClass="error" />
										</div>
										<form:input path="nama_agent" cssClass="text_field" cssErrorClass="text_field inputError" />
										<span class="description"></span>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="telp_agent" cssClass="label" cssErrorClass="label labelError">No HP<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="telp_agent" cssClass="error" />
										</div>
										<form:input path="telp_agent" cssClass="text_field" cssErrorClass="text_field inputError" />
										<span class="description"></span>
									</div>
								</td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="jabatan_agent" cssClass="label" cssErrorClass="label labelError">Jabatan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="jabatan_agent" cssClass="error" />
										</div>
										<form:input path="jabatan_agent" cssClass="text_field" cssErrorClass="text_field inputError" />
										<span class="description"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="mst_bank_id" cssClass="label" cssErrorClass="label labelError">Bank (Kreditur)<span class="mandatory" title="Wajib diisi"> *</span></form:label>
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
								<td>
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
						<a><form:checkbox id="lifeCheck" path="lifeCheck" /> Input Asuransi Jiwa (Life)</a>
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
											<form:label path="polisAJK.product.mst_product_id" cssClass="label" cssErrorClass="label labelError">Nama Produk<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.product.mst_product_id" cssClass="error" />
											<form:hidden path="polisAJK.id"/>
											<form:hidden path="polisAJK.beneficiary.policy_id"/>
											<form:hidden path="polisAJK.agent.policy_id"/>
											<form:hidden path="polisAJK.product.policy_id"/>
											<form:hidden path="polisAJK.customer.id"/>
											<form:hidden path="polisAJK.customer.address.id"/>
										</div>
										 <c:choose>
											<c:when test="${groupPolicy.polisAJK.mode eq 'VIEW'}"><form:hidden path="polisAJK.product.mst_product_id" /><input type="text" class="text_field_read" value="${groupPolicy.polisAJK.product.mst_product_id}" readonly="readonly" /></c:when>
											<c:otherwise>
												<form:select path="polisAJK.product.mst_product_id" cssErrorClass="inputError">
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
						<h3>I. Data Calon Tertanggung / Debitur <a href="#" id="copyFireToLife" title="Copy data Debitur dari asuransi Fire">(Copy)</a></h3>
						<table class="inputan">
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.nama" cssClass="label" cssErrorClass="label labelError">Nama Lengkap<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.nama" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.nama" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="40"/>
										<span class="description"></span>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.gender" cssClass="label" cssErrorClass="label labelError">Jenis Kelamin<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.gender" cssClass="error" />
										</div>
										<div>
											<form:radiobutton id="genderp" path="polisAJK.customer.gender" value="P" cssClass="radio" />
											<label for="genderp">Pria</label>
										</div>
										<div>
											<form:radiobutton id="genderw" path="polisAJK.customer.gender" value="W" cssClass="radio" />
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
											<form:label path="polisAJK.customer.tempat_lahir" cssClass="label" cssErrorClass="label labelError">Tempat Lahir<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.tempat_lahir" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.tempat_lahir" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40"/>
										<span class="description"></span>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.tgl_lahir" cssClass="label" cssErrorClass="label labelError">Tanggal Lahir <span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.tgl_lahir" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.tgl_lahir" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError"/>
										<span class="description">(dd-MM-yyyy)</span>
									</div></td>
								<td class="gap"></td>
								<td></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.berat_badan" cssClass="label" cssErrorClass="label labelError">Berat Badan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.berat_badan" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.berat_badan" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" maxlength="6" size="6" />
										<span class="description">(kg)</span>										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.tinggi_badan" cssClass="label" cssErrorClass="label labelError">Tinggi Badan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.tinggi_badan" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.tinggi_badan" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" maxlength="6" size="6" />
										<span class="description">(cm)</span>										
									</div></td>
								<td class="gap"></td>
								<td></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.pekerjaan" cssClass="label" cssErrorClass="label labelError">Pekerjaan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.pekerjaan" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.pekerjaan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="100" size="40"/>
									</div></td>
								<td class="gap"></td>
								<td colspan="3">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.bagian" cssClass="label" cssErrorClass="label labelError">Perusahaan dan Bagian<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.bagian" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.bagian" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="65"/>
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.address.alamat_kantor" cssClass="label" cssErrorClass="label labelError">Alamat Tempat Kerja </form:label>
											<form:errors path="polisAJK.customer.address.alamat_kantor" cssClass="error" />
										</div>
										<form:textarea path="polisAJK.customer.address.alamat_kantor" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.address.kota_kantor" cssClass="label" cssErrorClass="label labelError">Kota </form:label>
											<form:errors path="polisAJK.customer.address.kota_kantor" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.address.kota_kantor" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.address.kodepos_kantor" cssClass="label" cssErrorClass="label labelError">Kode Pos </form:label>
											<form:errors path="polisAJK.customer.address.kodepos_kantor" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.address.kodepos_kantor" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.address.alamat_rumah" cssClass="label" cssErrorClass="label labelError">Alamat Tempat Tinggal<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.address.alamat_rumah" cssClass="error" />
										</div>
										<form:textarea path="polisAJK.customer.address.alamat_rumah" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.address.kota_rumah" cssClass="label" cssErrorClass="label labelError">Kota<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.address.kota_rumah" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.address.kota_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.address.kodepos_rumah" cssClass="label" cssErrorClass="label labelError">Kode Pos</form:label>
											<form:errors path="polisAJK.customer.address.kodepos_rumah" cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.address.kodepos_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.address.telp_kantor"
												cssClass="label" cssErrorClass="label labelError">Telp Kantor</form:label>
											<form:errors path="polisAJK.customer.address.telp_kantor"
												cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.address.telp_kantor"
											cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.address.telp_hp" cssClass="label" cssErrorClass="label labelError">Telp HP<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.customer.address.telp_hp"
												cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.address.telp_hp"
											cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.customer.address.telp_rumah"
												cssClass="label" cssErrorClass="label labelError">Telp Rumah</form:label>
											<form:errors path="polisAJK.customer.address.telp_rumah"
												cssClass="error" />
										</div>
										<form:input path="polisAJK.customer.address.telp_rumah"
											cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.plafon_kredit" cssClass="label" cssErrorClass="label labelError">Jumlah Kredit/Plafon Pinjaman<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.plafon_kredit" cssClass="error" />
										</div>
										<form:input path="polisAJK.plafon_kredit" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" />
										<span class="description">(dalam Rupiah)</span>										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.product.premi" cssClass="label" cssErrorClass="label labelError">Premi Asuransi</form:label>
											<form:errors path="polisAJK.product.premi" cssClass="error" />
										</div>
										<form:input path="polisAJK.product.premi" cssClass="text_field text_field_readonly nominal" readonly="true"/>
										<span class="description">(dalam Rupiah)</span>
									</div></td>
								<td class="gap"></td>
								<td></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.ins_period" cssClass="label" cssErrorClass="label labelError">Masa Asuransi<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.ins_period" cssClass="error" />
										</div>
										<form:input path="polisAJK.ins_period" cssClass="text_field" cssErrorClass="text_field inputError" />
										<span class="description">(tahun)</span>										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.beg_date" cssClass="label" cssErrorClass="label labelError">Tgl Mulai<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.beg_date" cssClass="error" />
										</div>
										<form:input path="polisAJK.beg_date" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError" />
										<span class="description">(dd-MM-yyyy)</span>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.end_date" cssClass="label" cssErrorClass="label labelError">Tgl Berakhir</form:label>
											<form:errors path="polisAJK.end_date" cssClass="error" />
										</div>
										<form:input path="polisAJK.end_date" cssClass="text_field text_field_readonly" readonly="true" cssStyle="width: 80px;"/>										
										<%-- <fmt:formatDate value="${groupPolicy.polisAJK.end_date}" pattern="dd-MM-yyyy"/> --%>
									</div></td>
							</tr>
							<tr>
								<td colspan="5">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.jenis_manfaat" cssClass="label" cssErrorClass="label labelError">Manfaat Asuransi<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisAJK.jenis_manfaat" cssClass="error" />
										</div>
										<form:radiobuttons path="polisAJK.jenis_manfaat" items="${reff.listManfaat}" itemValue="key" itemLabel="value" cssClass="radio"/>
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
										<form:input path="polisAJK.createuser" cssClass="text_field text_field_readonly" readonly="true" />										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Modify By</label>
										</div>
										<form:input path="polisAJK.modifyuser" cssClass="text_field text_field_readonly" readonly="true" />										
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Create Date</label>
										</div>
										<form:input path="polisAJK.createdate" cssClass="text_field text_field_readonly" readonly="true" />										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Modify Date</label>
										</div>
										<form:input path="polisAJK.modifydate" cssClass="text_field text_field_readonly" readonly="true" />										
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
											<label class="label">Bank (Kreditur)<span class="mandatory" title="Wajib diisi"> *</span></label>
										</div>
										
										<form:input path="polisAJK.bank.namaBank" id="namaBank" readonly="true"/>
										<form:hidden path="polisAJK.bank.mst_bank_id"/>
										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">KC/KCP<span class="mandatory" title="Wajib diisi"> *</span></label>
										</div>
										<form:input path="polisAJK.bank.namaCabang" id="namaCabang" readonly="true" size="30"/>
										<form:hidden path="polisAJK.bank.mst_cab_bank_id"/>
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
											<form:label path="polisAJK.beneficiary.nama" cssClass="label" cssErrorClass="label labelError">Yang Ditunjuk</form:label>
											<form:errors path="polisAJK.beneficiary.nama" cssClass="error" />
										</div>
										<form:input path="polisAJK.beneficiary.nama" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.beneficiary.relasi" cssClass="label" cssErrorClass="label labelError">Hubungan dengan Debitur</form:label>
											<form:errors path="polisAJK.beneficiary.relasi" cssClass="error" />
										</div>
										<form:select path="polisAJK.beneficiary.relasi" cssErrorClass="inputError">
											<form:option value=""></form:option>
											<form:options items="${reff.listRelasi }" itemLabel="value" itemValue="key"/>
											<form:option value="0">Lainnya</form:option>
										</form:select>
										<div style="display: block;">
											<div class="fieldWithErrors">
												<form:label path="polisAJK.beneficiary.relasi_lain" cssClass="label" cssErrorClass="label labelError">Hubungan Lainnya:</form:label>
												<form:errors path="polisAJK.beneficiary.relasi_lain" cssClass="error" />
											</div>
											<form:input path="polisAJK.beneficiary.relasi_lain" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div>
									</div></td>
							</tr>
							--%>
						</table>
						<h3>II. KETERANGAN KESEHATAN</h3>
						<table class="inputan">
							<tr>
								<td>1.</td>
								<td><div class="fieldWithErrors">
										Apakah Anda sekarang dalam keadaan sehat? Jika "Tidak", mohon dijelaskan.<span class="mandatory" title="Wajib diisi"> *</span>
										<form:errors path="polisAJK.customer.q1" cssClass="error" />
										<form:errors path="polisAJK.customer.q1_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td style="width: 80px; ">
									<div>
										<form:radiobutton id="q1y" path="polisAJK.customer.q1" value="true" cssClass="radio" />
										<label for="q1y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q1t" path="polisAJK.customer.q1" value="false" cssClass="radio" />
										<label for="q1t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisAJK.customer.q1_desc" cssClass="textarea" cssErrorClass="textarea inputError"/></td>
							</tr>
							<tr>
								<td>2.</td>
								<td><div class="fieldWithErrors">
										Apakah dalam 2 tahun terakhir Anda pernah dioperasi/dirawat
										di rumah sakit atau dalam masa pengobatan/perawatan yang
										membutuhkan obat-obatan dalam masa yang lama? Jika "Ya",
										mohon dijelaskan.<span class="mandatory" title="Wajib diisi"> *</span>
										<form:errors path="polisAJK.customer.q2" cssClass="error" />
										<form:errors path="polisAJK.customer.q2_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div>
										<form:radiobutton id="q2y" path="polisAJK.customer.q2" value="true" />
										<label for="q2y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q2t" path="polisAJK.customer.q2" value="false" />
										<label for="q2t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisAJK.customer.q2_desc" cssClass="textarea" cssErrorClass="textarea inputError"/></td>
							</tr>
							<%--
							<tr>
								<td>3.</td>
								<td><div class="fieldWithErrors">
										Apakah berat badan Anda berubah dalam 12 bulan terakhir? Jika
										"Ya", mohon dijelaskan.<span class="mandatory" title="Wajib diisi"> *</span>
										<form:errors path="polisAJK.customer.q3" cssClass="error" />
										<form:errors path="polisAJK.customer.q3_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div>
										<form:radiobutton id="q3y" path="polisAJK.customer.q3" value="true" />
										<label for="q3y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q3t" path="polisAJK.customer.q3" value="false" />
										<label for="q3t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisAJK.customer.q3_desc" cssClass="textarea" cssErrorClass="textarea inputError" /></td>
							</tr>
							--%>
							<tr>
								<td>3.</td>
								<td><div class="fieldWithErrors">
										Apakah Anda pernah atau sedang menderita penyakit: cacat,
										tumor/kanker, TBC, paru-paru, asma, kencing manis, hati,
										ginjal, stroke, jantung, tekanan darah tinggi, gangguan jiwa,
										atau penyakit lainnya? Jika "Ya", mohon dijelaskan.<span class="mandatory" title="Wajib diisi"> *</span>
										<form:errors path="polisAJK.customer.q4" cssClass="error" />
										<form:errors path="polisAJK.customer.q4_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div>
										<form:radiobutton id="q4y" path="polisAJK.customer.q4" value="true" />
										<label for="q4y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q4t" path="polisAJK.customer.q4" value="false" />
										<label for="q4t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisAJK.customer.q4_desc" cssClass="textarea" cssErrorClass="textarea inputError" /></td>
							</tr>
							<tr>
								<td>4.</td>
								<td><div class="fieldWithErrors">
										Khusus untuk wanita: Apakah Anda sedang hamil? Jika "Ya",
										berapa minggu usia kandungan Anda?<span class="mandatory" title="Wajib diisi"> *</span>
										<form:errors path="polisAJK.customer.q5" cssClass="error" />
										<form:errors path="polisAJK.customer.q5_desc" cssClass="error" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div>
										<form:radiobutton id="q5y" path="polisAJK.customer.q5" value="true" />
										<label for="q5y">Ya</label>
									</div>
									<div>
										<form:radiobutton id="q5t" path="polisAJK.customer.q5" value="false" />
										<label for="q5t">Tidak</label>
									</div></td>
								<td><form:textarea path="polisAJK.customer.q5_desc" cssClass="textarea" cssErrorClass="textarea inputError" /></td>
							</tr>
						</table>
							
					</div>
					
					<c:if test="${not empty groupPolicy.polisAJK.product.premi}">
						<div class="messageBox" style="padding: 20px;">
							<strong>Summary:</strong>
							<br>Premi Asuransi Jiwa (Life) = Rp. <fmt:formatNumber pattern="#,###.00" value="${groupPolicy.polisAJK.product.premi}" />
							<c:if test="${groupPolicy.polisAJK.noclaim gt 60}">
								<br/>Harap Upload Form Subject To No Claim.
							</c:if>
						</div>
					</c:if>

				</div>
			</div>

		</div>
		<div class="block" id="block-forms2">

			<div class="secondary-navigation">
				<ul class="wat-cf">
					<li class="headerTitle">
						<a><form:checkbox id="fireCheck" path="fireCheck" /> Input Asuransi Kebakaran (Fire)</a>
					</li>
				</ul>
			</div>

			<div id="contentFire" class="content">
				<div class="inner">
					<div class="columns wat-cf">
						
						<table class="inputan">
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.product.mst_product_id" cssClass="label" cssErrorClass="label labelError">Nama Produk<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.product.mst_product_id" cssClass="error" />
											<form:hidden path="polisFire.id"/>
											<form:hidden path="polisFire.beneficiary.policy_id"/>
											<form:hidden path="polisFire.agent.policy_id"/>
											<form:hidden path="polisFire.product.policy_id"/>
											<form:hidden path="polisFire.customer.id"/>
											<form:hidden path="polisFire.customer.address.id"/>
											<form:hidden path="polisFire.customer.addressInsured.id"/>
										</div>
										 <c:choose>
											<c:when test="${groupPolicy.polisFire.mode eq 'VIEW'}"><form:hidden path="polisFire.product.mst_product_id" /><input type="text" class="text_field_read" value="${groupPolicy.polisFire.product.mst_product_id}" readonly="readonly" /></c:when>
											<c:otherwise>
												<form:select path="polisFire.product.mst_product_id" cssErrorClass="inputError">
													<form:option value="">Silahkan Pilih Produk</form:option>
													<form:options items="${reff.listProductFire}" itemValue="key" itemLabel="value"/>
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
							<tr>
								<td>
									
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Bank (Kreditur)<span class="mandatory" title="Wajib diisi"> *</span></label>
										</div>
										<form:input path="polisFire.bank.namaBank" id="namaBankFire" readonly="true"/>
										<form:hidden path="polisFire.bank.mst_bank_id"/>
										
									</div>
								</td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">KC/KCP<span class="mandatory" title="Wajib diisi"> *</span></label>
										</div>
										<form:input path="polisFire.bank.namaCabang" id="namaCabangFire" readonly="true" size="30"/>
										<form:hidden path="polisFire.bank.mst_cab_bank_id"/>
									</div>
								</td>
								<td class="gap"></td>
								<td></td>
							</tr>							
							<tr>
								<td colspan="4">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.bank.clause" cssClass="label" cssErrorClass="label labelError">Banker Clause<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.bank.clause" cssClass="error" />
										</div>
										<c:choose>
											<c:when test="${groupPolicy.polisFire.mode eq 'VIEW'}"><form:hidden id="bankerClause" path="polisFire.bank.clause" /><input type="text" class="text_field_read" value="${groupPolicy.polisFire.bank.clause}" readonly="readonly" /></c:when>
											<c:otherwise>
												<form:input path="polisFire.bank.clause" id="bankerClause" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="80" />
											</c:otherwise>
									   </c:choose>
										<span class="description"></span>
									</div>
								</td>
							</tr>
						</table>
						<h3>I. Data Calon Tertanggung / Debitur <a href="#" id="copyLifeToFire" title="Copy data Debitur dari asuransi Life">(Copy)</a></h3>
						<table class="inputan">
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.nama" cssClass="label" cssErrorClass="label labelError">Nama Nasabah<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.nama" cssClass="error" />
										</div>
										<c:choose>
											<c:when test="${groupPolicy.polisFire.mode eq 'VIEW'}"><form:hidden path="polisFire.customer.nama" /><input type="text" class="text_field_read" value="${groupPolicy.polisFire.customer.nama}" readonly="readonly" /></c:when>
											<c:otherwise>
												<form:input path="polisFire.customer.nama" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40"/>
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
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.address.alamat_rumah" cssClass="label" cssErrorClass="label labelError">Alamat Nasabah<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.address.alamat_rumah" cssClass="error" />
										</div>
										<form:textarea path="polisFire.customer.address.alamat_rumah" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.address.kota_rumah" cssClass="label" cssErrorClass="label labelError">Kota<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.address.kota_rumah" cssClass="error" />
										</div>
										<form:input path="polisFire.customer.address.kota_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.address.kodepos_rumah" cssClass="label" cssErrorClass="label labelError">Kode Pos</form:label>
											<form:errors path="polisFire.customer.address.kodepos_rumah" cssClass="error" />
										</div>
										<form:input path="polisFire.customer.address.kodepos_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.address.telp_rumah"
												cssClass="label" cssErrorClass="label labelError">Telp Rumah</form:label>
											<form:errors path="polisFire.customer.address.telp_rumah"
												cssClass="error" />
										</div>
										<form:input path="polisFire.customer.address.telp_rumah"
											cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
									<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.address.telp_kantor"
												cssClass="label" cssErrorClass="label labelError">Telp Kantor</form:label>
											<form:errors path="polisFire.customer.address.telp_kantor"
												cssClass="error" />
										</div>
										<form:input path="polisFire.customer.address.telp_kantor"
											cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.address.telp_hp" cssClass="label" cssErrorClass="label labelError">Telp HP<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.address.telp_hp"
												cssClass="error" />
										</div>
										<form:input path="polisFire.customer.address.telp_hp"
											cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
							</tr>
						</table>
						<h3>II. Data Objek Pertanggungan <a href="#" id="copyAddressInsured" title="Copy data alamat dari alamat debitur">(Copy)</a></h3>
						<table class="inputan">
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.alamat_rumah" cssClass="label" cssErrorClass="label labelError">Alamat Object Pertanggungan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.alamat_rumah" cssClass="error" />
										</div>
										<form:textarea path="polisFire.customer.addressInsured.alamat_rumah" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.kota_rumah" cssClass="label" cssErrorClass="label labelError">Kota<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.kota_rumah" cssClass="error" />
										</div>
										<form:input path="polisFire.customer.addressInsured.kota_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.kodepos_rumah" cssClass="label" cssErrorClass="label labelError">Kode Pos</form:label>
											<form:errors path="polisFire.customer.addressInsured.kodepos_rumah" cssClass="error" />
										</div>
										<form:input path="polisFire.customer.addressInsured.kodepos_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div></td>
							</tr>
							
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.type_sertifikat" cssClass="label" cssErrorClass="label labelError">Tipe Sertifikat<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.type_sertifikat" cssClass="error" />
										</div>
										<form:input path="polisFire.customer.addressInsured.type_sertifikat" cssClass="text_field " cssErrorClass="text_field inputError" maxlength="100" size="25" />
										<span class="description">(SHM / SHGB / lainnya)</span>							
										<%--
										YUSUF - 11 MEI 2013 - REQ ARIS, TIPE SERTIFIKAT DIGANTI LAINNYA
										<form:radiobutton path="polisFire.customer.addressInsured.type_sertifikat" value="SHM" label="SHM"/>
										<form:radiobutton path="polisFire.customer.addressInsured.type_sertifikat" value="SHGB" label="SHGB"/>
										--%>
									</div>
								
									
								</td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.no_sertifikat" cssClass="label" cssErrorClass="label labelError">No. Sertifikat<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.no_sertifikat" cssClass="error" />
										</div>
										<form:input path="polisFire.customer.addressInsured.no_sertifikat" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div>
								</td>
								<td class="gap"></td>
								<td>
								</td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.jenis_bangunan" cssClass="label" cssErrorClass="label labelError">Jenis Bangunan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.jenis_bangunan" cssClass="error" />
										</div>
										<form:select path="polisFire.customer.addressInsured.jenis_bangunan" cssErrorClass="inputError">
											<form:option value="">Silahkan Pilih Jenis Bangunan</form:option>
											<form:options items="${reff.listJenisBangunan}" itemValue="key" itemLabel="value"/>
										</form:select>
									</div>
								</td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.penggunaan_bangunan" cssClass="label" cssErrorClass="label labelError">Jenis Bangunan Lainnya</form:label>
											<form:errors path="polisFire.customer.addressInsured.penggunaan_bangunan" cssClass="error" />
										</div>
										<form:input path="polisFire.customer.addressInsured.penggunaan_bangunan" cssClass="text_field" cssErrorClass="text_field inputError" />
									</div>
								</td>
								<td class="gap"></td>
								<td>
								</td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.ins_period" cssClass="label" cssErrorClass="label labelError">Masa Asuransi<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.ins_period" cssClass="error" />
										</div>
										<form:input path="polisFire.ins_period" cssClass="text_field" cssErrorClass="text_field inputError" />
										<span class="description">(tahun)</span>										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.beg_date" cssClass="label" cssErrorClass="label labelError">Tgl Mulai<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.beg_date" cssClass="error" />
										</div>
										<form:input path="polisFire.beg_date" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError" />
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.end_date" cssClass="label" cssErrorClass="label labelError">Tgl Berakhir</form:label>
											<form:errors path="polisFire.end_date" cssClass="error" />
										</div>
										<form:input path="polisFire.end_date" cssClass="text_field text_field_readonly" readonly="true" cssStyle="width: 80px;"/>										
										<%-- <fmt:formatDate value="${groupPolicy.polisFire.end_date}" pattern="dd-MM-yyyy"/> --%>
									</div></td>
							</tr>
							<tr>
								<td colspan="3"><br/><br/>
									<label class="label">Harga Pertanggungan )*</label>
								</td>
							</tr>
							<tr>
								<td class="sorong">
								
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.harga_bangunan" cssClass="label" cssErrorClass="label labelError">a. Bangunan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.harga_bangunan" cssClass="error" />
										</div>
										
									</div>
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.customer.addressInsured.harga_bangunan" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" maxlength="45" size="40" />
									<span class="description">(dalam Rupiah)</span>										
									<table>
										<tr>
											<td>
												<div class="group">
													<div class="fieldWithErrors">
														<form:label path="polisFire.customer.addressInsured.luas_tanah" cssClass="label" cssErrorClass="label labelError">Luas Tanah<span class="mandatory" title="Wajib diisi"> *</span></form:label>
														<form:errors path="polisFire.customer.addressInsured.luas_tanah" cssClass="error" />
													</div>
													<form:input path="polisFire.customer.addressInsured.luas_tanah" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" />
													<span class="description">(m<sup>2</sup>)</span>										
												</div>
											</td>
											<td>
												<div class="group" >
													<div class="fieldWithErrors">
														<form:label path="polisFire.customer.addressInsured.luas_bangunan" cssClass="label" cssErrorClass="label labelError">Luas Bangunan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
														<form:errors path="polisFire.customer.addressInsured.luas_bangunan" cssClass="error" />
													</div>
													<form:input path="polisFire.customer.addressInsured.luas_bangunan" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" />
													<span class="description">(m<sup>2</sup>)</span>										
												</div>
											</td>
										</tr>
									</table>
								</td>
								
							</tr>
							<tr>
								<td  class="sorong">
									<div class="group">
										<div class="fieldWithErrors">
											<input type="checkbox" id="cekDetail"><label for="cekDetail"> Tampilkan Detail Bangunan</label>
										</div>
									</div>
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
								
								</td>
							</tr>
							<tr id="detailHarga1" style="display: none;">
								<td  class="sorong">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.harga_stock" cssClass="label" cssErrorClass="label labelError">b. Stock</form:label>
											<form:errors path="polisFire.customer.addressInsured.harga_stock" cssClass="error" />
										</div>
										
									</div>
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.customer.addressInsured.harga_stock" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" maxlength="45" size="40" />
									<span class="description">(dalam Rupiah)</span>										
								</td>
								
							</tr>
							<tr id="detailHarga2" style="display: none;">
								<td  class="sorong">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.harga_perabot" cssClass="label" cssErrorClass="label labelError">c. Perabot</form:label>
											<form:errors path="polisFire.customer.addressInsured.harga_perabot" cssClass="error" />
										</div>
										
									</div>
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.customer.addressInsured.harga_perabot" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" maxlength="45" size="40" />
									<span class="description">(dalam Rupiah)</span>										
								</td>
								
							</tr>
							<tr id="detailHarga3" style="display: none;">
								<td   class="sorong">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.harga_lainnya" cssClass="label" cssErrorClass="label labelError">d. Lainnya</form:label>
											<form:errors path="polisFire.customer.addressInsured.harga_lainnya" cssClass="error" />
										</div>
										
									</div>
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.customer.addressInsured.harga_lainnya" cssClass="text_field nominal" cssErrorClass="text_field inputError nominal" maxlength="45" size="40" />
									<span class="description">(dalam Rupiah)</span>										
								</td>
								
							</tr>
							<tr style="height: 10px;">
								<td>
								</td>
								<td colspan="4" style="text-align: right;">
									+
								<hr size="2px" style="border-bottom-style: dashed !important;"/>
									
								</td>
							<tr>
							<tr>
								<td ><label class="label">Total Harga Pertanggungan</label>
								
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.product.up" cssClass="text_field text_field_readonly nominal" maxlength="45" size="40" readonly="true"/>
									<span class="description">(dalam Rupiah)</span>										
								</td>
								
							</tr>

							<tr>
								<td ><label class="label">Premi Asuransi</label>
								
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.product.premi" cssClass="text_field text_field_readonly nominal" maxlength="45" size="40" readonly="true"/>
									<span class="description">(dalam Rupiah)</span>										
								</td>
								
							</tr>
							
							<tr>
								<td colspan="5">
								<br/><br/>
								<label class="label">Kondisi sekeliling (wajib diisi)</label>
							
								</td>
							<tr>
								<td   class="sorong">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.kondisi_kiri" cssClass="label" cssErrorClass="label labelError">- Sebelah Kiri<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.kondisi_kiri" cssClass="error" />
										</div>
										
									</div>
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.customer.addressInsured.kondisi_kiri" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
								</td>
								
							</tr>
							<tr>
								<td   class="sorong">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.kondisi_kanan" cssClass="label" cssErrorClass="label labelError">- Sebelah Kanan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.kondisi_kanan" cssClass="error" />
										</div>
										
									</div>
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.customer.addressInsured.kondisi_kanan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
								</td>
								
							</tr>
							<tr>
								<td class="sorong">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.kondisi_depan" cssClass="label" cssErrorClass="label labelError">- Sebelah Depan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.kondisi_depan" cssClass="error" />
										</div>
										
									</div>
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.customer.addressInsured.kondisi_depan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
								</td>
								
							</tr>
							<tr>
								<td   class="sorong">
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.customer.addressInsured.kondisi_belakang" cssClass="label" cssErrorClass="label labelError">- Sebelah Belakang<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.customer.addressInsured.kondisi_belakang" cssClass="error" />
										</div>
										
									</div>
								</td>
								<td class="gap"></td>
								<td colspan="3"  >
									<form:input path="polisFire.customer.addressInsured.kondisi_belakang" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
								</td>
								
							</tr>
							<tr>
								<td colspan="5">
									<br/><br/>
								</td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisFire.spaj_date" cssClass="label" cssErrorClass="label labelError">Tanggal Pengajuan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
											<form:errors path="polisFire.spaj_date" cssClass="error" />
										</div>
										<c:choose>
											<c:when test="${groupPolicy.polisFire.mode eq 'VIEW'}"><form:hidden path="polisFire.spaj_date" /><input type="text" class="text_field_read" value="${groupPolicy.polisFire.spaj_date}" readonly="readonly" /></c:when>
											<c:otherwise>
												<form:input path="polisFire.spaj_date" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError"/>
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
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Create By</label>
										</div>
										<form:input path="polisFire.createuser" cssClass="text_field text_field_readonly" readonly="true" />										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Modify By</label>
										</div>
										<form:input path="polisFire.modifyuser" cssClass="text_field text_field_readonly" readonly="true" />										
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Create Date</label>
										</div>
										<form:input path="polisFire.createdate" cssClass="text_field text_field_readonly" readonly="true" />										
									</div></td>
								<td class="gap"></td>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<label class="label">Modify Date</label>
										</div>
										<form:input path="polisFire.modifydate" cssClass="text_field text_field_readonly" readonly="true" />										
									</div></td>
							</tr>
						</table>
						
					</div>		
					
					<c:if test="${not empty groupPolicy.polisFire.product.premi}">
						<div class="messageBox" style="padding: 20px;">
							<strong>Summary:</strong>
							<br>Premi Asuransi Kebakaran (Fire) = Rp. <fmt:formatNumber pattern="#,###.00" value="${groupPolicy.polisFire.product.premi}" />
						</div>
					</c:if>
							
				</div>
			</div>
		</div>
		
		<c:choose>
			<c:when test="${groupPolicy.fireCheck }">
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
										<br/>
										<c:if test="${groupPolicy.polisFire.customer.addressInsured.jenis_bangunan eq 3}">
											<div class="group">
												<div class="fieldWithErrors">
													<form:label path="polisFire.rate" cssClass="label" cssErrorClass="label labelError">Rate</form:label>
													<form:errors path="polisFire.rate" cssClass="error" />
												</div>
												 <c:choose>
													<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisFire.rate" />
														<input type="text" class="text_field_read" value="${groupPolicy.polisFire.rate }" readonly="readonly" />
													</c:when>
													<c:otherwise>
														<form:input path="polisFire.rate"/>
													</c:otherwise>
											   </c:choose>
												<span class="description"></span>
											</div>
											<div class="group">
												<div class="fieldWithErrors">
													<form:label path="polisFire.premi" cssClass="label" cssErrorClass="label labelError">Premi</form:label>
													<form:errors path="polisFire.premi" cssClass="error" />
												</div>
												 <c:choose>
													<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisFire.premi" />
														<input type="text" class="text_field_read" value="${groupPolicy.polisFire.premi }" readonly="readonly" />
													</c:when>
													<c:otherwise>
														<form:input path="polisFire.premi"/>
													</c:otherwise>
											   </c:choose>
												<span class="description"></span>
											</div>
											<div class="group">
												<div class="fieldWithErrors">
													<form:label path="polisFire.deductible" cssClass="label" cssErrorClass="label labelError">Deductible</form:label>
													<form:errors path="polisFire.deductible" cssClass="error" />
												</div>
												 <c:choose>
													<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisFire.deductible" />
														<input type="text" class="text_field_read" value="${groupPolicy.polisFire.deductible }" readonly="readonly" />
													</c:when>
													<c:otherwise>
														<form:input path="polisFire.deductible"/>
													</c:otherwise>
											   </c:choose>
												<span class="description"></span>
											</div>
											
										</c:if>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="polisFire.asuransi_id" cssClass="label" cssErrorClass="label labelError">Pilih Asuransi</form:label>
												<form:errors path="polisFire.asuransi_id" cssClass="error" />
											</div>
											 <c:choose>
												<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisFire.asuransi_id" />
												<input type="text" class="text_field_read" value="
												<c:forEach items="${reff.listAsuransiFire}" var="v">
													<c:if test="${v.key eq groupPolicy.polisFire.asuransi_id }">
														${v.value }
													</c:if>
												</c:forEach>
												" readonly="readonly" /></c:when>
												<c:otherwise>
													<form:select path="polisFire.asuransi_id">
														<form:option value="">Silahkan Pilih Asuransi</form:option>
														<form:options items="${reff.listAsuransiFire}" itemValue="key" itemLabel="value"/>
													</form:select>
												</c:otherwise>
										   </c:choose>
											<span class="description"></span>
										</div>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="polisFire.flag_akseptasi_mb" cssClass="label" cssErrorClass="label labelError">Status Akseptasi (oleh MaiBro)</form:label>
												<form:errors path="polisFire.flag_akseptasi_mb" cssClass="error" />
											</div>
											 <c:choose>
												<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisFire.flag_akseptasi_mb" />
													<form:select path="polisFire.flag_akseptasi_mb" disabled="true">
														<form:options items="${reff.listStatusAksep}" itemValue="key" itemLabel="value"/>
													</form:select>													
												</c:when>
												<c:otherwise>
													<form:select path="polisFire.flag_akseptasi_mb">
														<form:options items="${reff.listStatusAksep}" itemValue="key" itemLabel="value"/>
													</form:select>													
												</c:otherwise>
										   </c:choose>
											<span class="description"></span>
										</div>										
										
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="polisFire.asuransi_desc" cssClass="label" cssErrorClass="label labelError">Keterangan</form:label>
												<form:errors path="polisFire.asuransi_desc" cssClass="error" />
											</div>
											 <c:choose>
												<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisFire.asuransi_desc" />
													<input type="text" class="text_field_read" value="${groupPolicy.polisFire.asuransi_desc }" readonly="readonly" />
												</c:when>
												<c:otherwise>
													<form:textarea path="polisFire.asuransi_desc"/>
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
			</c:when>
			<c:when test="${groupPolicy.lifeCheck }">
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
											<form:label path="polisAJK.asuransi_id" cssClass="label" cssErrorClass="label labelError">Pilih Asuransi</form:label>
											<form:errors path="polisAJK.asuransi_id" cssClass="error" />
										</div>
										 <c:choose>
											<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisAJK.asuransi_id" />
											<input type="text" class="text_field_read" value="
											<c:forEach items="${reff.listAsuransiLife}" var="v">
												<c:if test="${v.key eq groupPolicy.polisAJK.asuransi_id }">
													${v.value }
												</c:if>
											</c:forEach>
											" readonly="readonly" /></c:when>
											<c:otherwise>
												<form:select path="polisAJK.asuransi_id">
													<form:option value="">Silahkan Pilih Asuransi</form:option>
													<form:options items="${reff.listAsuransiLife}" itemValue="key" itemLabel="value"/>
												</form:select>
											</c:otherwise>
									   </c:choose>
										<span class="description"></span>
									</div>
									<div class="group">
											<div class="fieldWithErrors">
												<form:label path="polisAJK.flag_akseptasi_mb" cssClass="label" cssErrorClass="label labelError">Status Akseptasi (oleh MaiBro)</form:label>
												<form:errors path="polisAJK.flag_akseptasi_mb" cssClass="error" />
											</div>
											 <c:choose>
												<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisAJK.flag_akseptasi_mb" />
													<form:select path="polisAJK.flag_akseptasi_mb" disabled="true">
														<form:options items="${reff.listStatusAksep}" itemValue="key" itemLabel="value"/>
													</form:select>													
												</c:when>
												<c:otherwise>
													<form:select path="polisAJK.flag_akseptasi_mb">
														<form:options items="${reff.listStatusAksep}" itemValue="key" itemLabel="value"/>
													</form:select>													
												</c:otherwise>
										   </c:choose>
											<span class="description"></span>
										</div>		
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="polisAJK.asuransi_desc" cssClass="label" cssErrorClass="label labelError">Keterangan</form:label>
											<form:errors path="polisAJK.asuransi_desc" cssClass="error" />
										</div>
										 <c:choose>
											<c:when test="${groupPolicy.mode eq 'VIEW'}"><form:hidden path="polisAJK.asuransi_desc" />
											<input type="text" class="text_field_read" value="${groupPolicy.polisAJK.asuransi_desc }" readonly="readonly" /></c:when>
											<c:otherwise>
												<form:textarea path="polisAJK.asuransi_desc"/>
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
			</c:when>
		</c:choose>
		
		<c:if test="${groupPolicy.mode ne 'VIEW'}">
			<div class="group navform wat-cf">
				<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
					<img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
				</button>
				<span class="text_button_padding"></span>
				 <form:hidden path="mode"/>
				 <form:hidden path="pagename"/>
				<button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/kpr/${groupPolicy.pagename}'">
					<img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
				</button>
			</div>		
		</c:if>
		<c:if test="${groupPolicy.mode eq 'VIEW'}">
			<div class="group navform wat-cf">
				<button class="button" type="button" onclick="window.location='${path}/kpr/${groupPolicy.pagename}'">
                   <img src="${path }/static/pilu/images/icons/back.png" alt="Back" /> Back
                 </button>
			</div>		
		</c:if>
		
	</div>

	</form:form>

</body>
</html>
