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
					<li class="headerTitle"><a href="#"> Input Asuransi Jiwa</a>
					</li>
				</ul>
			</div>

			<div class="content">
				<div class="inner">
					<form:form commandName="policy" name="formpost" method="POST" action="${path}/proses/input_ajk/save" cssClass="form">
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
							<table class="inputan">
							<tr>
									<td>
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
										</div></td>
									<td class="gap"></td>
									<td>
									</td>
									<td class="gap"></td>
									<td></td>
								</tr>
							</table>
							<h3>I. Data Calon Tertanggung</h3>
							<table class="inputan">
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.nama" cssClass="label" cssErrorClass="label labelError">Nama Lengkap</form:label>
												<form:errors path="customer.nama" cssClass="error" />
											</div>
											<form:input path="customer.nama" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="40"/>
											<span class="description"></span>
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.gender" cssClass="label" cssErrorClass="label labelError">Jenis Kelamin</form:label>
												<form:errors path="customer.gender" cssClass="error" />
											</div>
											<div>
												<form:radiobutton id="genderp" path="customer.gender" value="P" cssClass="radio" />
												<label for="genderp">Pria</label>
											</div>
											<div>
												<form:radiobutton id="genderw" path="customer.gender" value="W" cssClass="radio" />
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
												<form:label path="customer.tempat_lahir" cssClass="label" cssErrorClass="label labelError">Tempat Lahir</form:label>
												<form:errors path="customer.tempat_lahir" cssClass="error" />
											</div>
											<form:input path="customer.tempat_lahir" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40"/>
											<span class="description"></span>
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.tgl_lahirStr" cssClass="label" cssErrorClass="label labelError">Tanggal Lahir </form:label>
												<form:errors path="customer.tgl_lahirStr" cssClass="error" />
											</div>
											<form:input path="customer.tgl_lahirStr" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError"/>
										</div></td>
									<td class="gap"></td>
									<td></td>
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.berat_badan" cssClass="label" cssErrorClass="label labelError">Berat Badan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
												<form:errors path="customer.berat_badan" cssClass="error" />
											</div>
											<form:input path="customer.berat_badan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="6" size="6" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.tinggi_badan" cssClass="label" cssErrorClass="label labelError">Tinggi Badan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
												<form:errors path="customer.tinggi_badan" cssClass="error" />
											</div>
											<form:input path="customer.tinggi_badan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="6" size="6" />
										</div></td>
									<td class="gap"></td>
									<td></td>
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.pekerjaan" cssClass="label" cssErrorClass="label labelError">Pekerjaan </form:label>
												<form:errors path="customer.pekerjaan" cssClass="error" />
											</div>
											<form:input path="customer.pekerjaan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="100" size="40"/>
										</div></td>
									<td class="gap"></td>
									<td colspan="3">
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.bagian" cssClass="label" cssErrorClass="label labelError">Perusahaan dan Bagian<span class="mandatory" title="Wajib diisi"> *</span></form:label>
												<form:errors path="customer.bagian" cssClass="error" />
											</div>
											<form:input path="customer.bagian" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="200" size="65"/>
										</div></td>
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.address.alamat_kantor" cssClass="label" cssErrorClass="label labelError">Alamat Tempat Kerja </form:label>
												<form:errors path="customer.address.alamat_kantor" cssClass="error" />
											</div>
											<form:textarea path="customer.address.alamat_kantor" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.address.kota_kantor" cssClass="label" cssErrorClass="label labelError">Kota </form:label>
												<form:errors path="customer.address.kota_kantor" cssClass="error" />
											</div>
											<form:input path="customer.address.kota_kantor" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.address.kodepos_kantor" cssClass="label" cssErrorClass="label labelError">Kode Pos </form:label>
												<form:errors path="customer.address.kodepos_kantor" cssClass="error" />
											</div>
											<form:input path="customer.address.kodepos_kantor" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.address.alamat_rumah" cssClass="label" cssErrorClass="label labelError">Alamat Tempat Tinggal </form:label>
												<form:errors path="customer.address.alamat_rumah" cssClass="error" />
											</div>
											<form:textarea path="customer.address.alamat_rumah" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.address.kota_rumah" cssClass="label" cssErrorClass="label labelError">Kota</form:label>
												<form:errors path="customer.address.kota_rumah" cssClass="error" />
											</div>
											<form:input path="customer.address.kota_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.address.kodepos_rumah" cssClass="label" cssErrorClass="label labelError">Kode Pos</form:label>
												<form:errors path="customer.address.kodepos_rumah" cssClass="error" />
											</div>
											<form:input path="customer.address.kodepos_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.address.telp_kantor"
													cssClass="label" cssErrorClass="label labelError">Telp Kantor</form:label>
												<form:errors path="customer.address.telp_kantor"
													cssClass="error" />
											</div>
											<form:input path="customer.address.telp_kantor"
												cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.address.telp_hp" cssClass="label" cssErrorClass="label labelError">Telp HP</form:label>
												<form:errors path="customer.address.telp_hp"
													cssClass="error" />
											</div>
											<form:input path="customer.address.telp_hp"
												cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.address.telp_rumah"
													cssClass="label" cssErrorClass="label labelError">Telp Rumah</form:label>
												<form:errors path="customer.address.telp_rumah"
													cssClass="error" />
											</div>
											<form:input path="customer.address.telp_rumah"
												cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="ins_period" cssClass="label" cssErrorClass="label labelError">Masa Asuransi</form:label>
												<form:errors path="ins_period" cssClass="error" />
											</div>
											<form:input path="ins_period" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="beg_dateStr" cssClass="label" cssErrorClass="label labelError">Tgl Mulai</form:label>
												<form:errors path="beg_dateStr" cssClass="error" />
											</div>
											<form:input path="beg_dateStr" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="end_dateStr" cssClass="label" cssErrorClass="label labelError">Tgl Berakhir</form:label>
												<form:errors path="end_dateStr" cssClass="error" />
											</div>
											<form:input path="end_dateStr" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError" />
										</div></td>
								</tr>
								<tr>
									<td colspan="5">
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="jenis_manfaat" cssClass="label" cssErrorClass="label labelError">Manfaat Asuransi</form:label>
												<form:errors path="jenis_manfaat" cssClass="error" />
											</div>
											<div>
												<form:radiobutton id="manf1" path="jenis_manfaat" value="1" cssClass="radio" />
												<label for="manf1">Jiwa</label>
											</div>
											<div>
												<form:radiobutton id="manf2" path="jenis_manfaat" value="2" cssClass="radio" />
												<label for="manf2">Jiwa + Cacat Tetap Total + Tunggakan + Denda</label>
											</div>
										</div></td>
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
											${policy.createuser}
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<label class="label">Modify By</label>
											</div>
											${policy.modifyuser}
										</div></td>
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<label class="label">Create Date</label>
											</div>
											${policy.createdate}
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<label class="label">Modify Date</label>
											</div>
											${policy.modifydate}
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
												<form:label path="bank.mst_bank_id" cssClass="label" cssErrorClass="label labelError">Kreditur</form:label>
												<form:errors path="bank.mst_bank_id" cssClass="error" />
											</div>
											<form:select id="mst_bank" path="bank.mst_bank_id" cssErrorClass="inputError">
												<form:option value=""></form:option>
												<form:options items="${reff.listBank}" itemValue="key" itemLabel="value"/>
											</form:select>
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="bank.mst_cab_bank_id" cssClass="label" cssErrorClass="label labelError">KC/KCP</form:label>
												<form:errors path="bank.mst_cab_bank_id" cssClass="error" />
											</div>
											<form:select id="mst_cab_bank" path="bank.mst_cab_bank_id" cssErrorClass="inputError"/>

										</div></td>
								</tr>
								<%--
								YUSUF - 11 MEI 2013 - REQ ARIS, BENEF DIHILANGKAN
								<tr>
									<td><label class="label">2.</label>
									</td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="beneficiary.nama" cssClass="label" cssErrorClass="label labelError">Yang Ditunjuk</form:label>
												<form:errors path="beneficiary.nama" cssClass="error" />
											</div>
											<form:input path="beneficiary.nama" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="beneficiary.relasi" cssClass="label" cssErrorClass="label labelError">Hubungan dengan Debitur</form:label>
												<form:errors path="beneficiary.relasi" cssClass="error" />
											</div>
											<form:select path="beneficiary.relasi" cssErrorClass="inputError" items="${reff.listRelasi }" itemLabel="value" itemValue="key"/>
											<div style="display: block;">
												<div class="fieldWithErrors">
													<form:label path="beneficiary.relasi_lain" cssClass="label" cssErrorClass="label labelError">Hubungan Lainnya:</form:label>
													<form:errors path="beneficiary.relasi_lain" cssClass="error" />
												</div>
												<form:input path="beneficiary.relasi_lain" cssClass="text_field" cssErrorClass="text_field inputError" />
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
											Apakah Anda sekarang dalam keadaan sehat? Jika "Tidak", mohon dijelaskan.
											<form:errors path="customer.q1" cssClass="error" />
											<form:errors path="customer.q1_desc" cssClass="error" />
										</div></td>
									<td class="gap"></td>
									<td style="width: 80px; ">
										<div>
											<form:radiobutton id="q1y" path="customer.q1" value="true" cssClass="radio" />
											<label for="q1y">Ya</label>
										</div>
										<div>
											<form:radiobutton id="q1t" path="customer.q1" value="false" cssClass="radio" />
											<label for="q1t">Tidak</label>
										</div></td>
									<td><form:textarea path="customer.q1_desc" cssClass="textarea" cssErrorClass="textarea inputError"/></td>
								</tr>
								<tr>
									<td>2.</td>
									<td><div class="fieldWithErrors">
											Apakah dalam 2 tahun terakhir Anda pernah dioperasi/dirawat
											di rumah sakit atau dalam masa pengobatan/perawatan yang
											membutuhkan obat-obatan dalam masa yang lama? Jika "Ya",
											mohon dijelaskan.
											<form:errors path="customer.q2" cssClass="error" />
											<form:errors path="customer.q2_desc" cssClass="error" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div>
											<form:radiobutton id="q2y" path="customer.q2" value="true" />
											<label for="q2y">Ya</label>
										</div>
										<div>
											<form:radiobutton id="q2t" path="customer.q2" value="false" />
											<label for="q2t">Tidak</label>
										</div></td>
									<td><form:textarea path="customer.q2_desc" cssClass="textarea" cssErrorClass="textarea inputError"/></td>
								</tr>
								<%--
								<tr>
									<td>3.</td>
									<td><div class="fieldWithErrors">
											Apakah berat badan Anda berubah dalam 12 bulan terakhir? Jika
											"Ya", mohon dijelaskan.
											<form:errors path="customer.q3" cssClass="error" />
											<form:errors path="customer.q3_desc" cssClass="error" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div>
											<form:radiobutton id="q3y" path="customer.q3" value="true" />
											<label for="q3y">Ya</label>
										</div>
										<div>
											<form:radiobutton id="q3t" path="customer.q3" value="false" />
											<label for="q3t">Tidak</label>
										</div></td>
									<td><form:textarea path="customer.q3_desc" cssClass="textarea" cssErrorClass="textarea inputError" /></td>
								</tr>
								--%>
								<tr>
									<td>3.</td>
									<td><div class="fieldWithErrors">
											Apakah Anda pernah atau sedang menderita penyakit: cacat,
											tumor/kanker, TBC, paru-paru, asma, kencing manis, hati,
											ginjal, stroke, jantung, tekanan darah tinggi, gangguan jiwa,
											atau penyakit lainnya? Jika "Ya", mohon dijelaskan.
											<form:errors path="customer.q4" cssClass="error" />
											<form:errors path="customer.q4_desc" cssClass="error" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div>
											<form:radiobutton id="q4y" path="customer.q4" value="true" />
											<label for="q4y">Ya</label>
										</div>
										<div>
											<form:radiobutton id="q4t" path="customer.q4" value="false" />
											<label for="q4t">Tidak</label>
										</div></td>
									<td><form:textarea path="customer.q4_desc" cssClass="textarea" cssErrorClass="textarea inputError" /></td>
								</tr>
								<tr>
									<td>4.</td>
									<td><div class="fieldWithErrors">
											Khusus untuk wanita: Apakah Anda sedang hamil? Jika "Ya",
											berapa minggu usia kandungan Anda?
											<form:errors path="customer.q5" cssClass="error" />
											<form:errors path="customer.q5_desc" cssClass="error" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div>
											<form:radiobutton id="q5y" path="customer.q5" value="true" />
											<label for="q5y">Ya</label>
										</div>
										<div>
											<form:radiobutton id="q5t" path="customer.q5" value="false" />
											<label for="q5t">Tidak</label>
										</div></td>
									<td><form:textarea path="customer.q5_desc" cssClass="textarea" cssErrorClass="textarea inputError" /></td>
								</tr>
							</table>

							<h3>III. INFORMASI AGEN</h3>

							<table class="inputan">
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="agent.nama" cssClass="label" cssErrorClass="label labelError">Nama Agent</form:label>
												<form:errors path="agent.nama" cssClass="error" />
											</div>
											<form:input path="agent.nama" cssClass="text_field" cssErrorClass="text_field inputError" />
											<span class="description"></span>
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="agent.telp_hp" cssClass="label" cssErrorClass="label labelError">No HP</form:label>
												<form:errors path="agent.telp_hp" cssClass="error" />
											</div>
											<form:input path="agent.telp_hp" cssClass="text_field" cssErrorClass="text_field inputError" />
											<span class="description"></span>
										</div></td>
								</tr>
							</table>
						</div>
						<div class="group navform wat-cf">

							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
								<img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
							</button>
							<span class="text_button_padding"></span>
							 <form:hidden path="mode"/>
							<button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/proses/input_ajk'">
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
