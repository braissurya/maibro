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
					<li class="headerTitle"><a href="#"> Input Fire</a>
					</li>
				</ul>
			</div>

			<div class="content">
				<div class="inner">
					<form:form commandName="policy" name="formpost" method="POST" action="${path}/proses/inputfire/save" cssClass="form">
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
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="bank.mst_cab_bank_id" cssClass="label" cssErrorClass="label labelError">KC / KCP</form:label>
												<form:errors path="bank.mst_cab_bank_id" cssClass="error" />
											</div>
											 <c:choose>
												<c:when test="${policy.mode eq 'VIEW'}"><form:hidden path="bank.mst_cab_bank_id" /><input type="text" class="text_field_read" value="${policy.bank.mst_cab_bank_id}" readonly="readonly" /></c:when>
												<c:otherwise>
													<form:select path="bank.mst_cab_bank_id">
														<form:option value="">Silahkan Pilih KC / KCP</form:option>
														<form:options items="${reff.cab_bank}" itemValue="key" itemLabel="value"/>
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
												<form:label path="customer.nama" cssClass="label" cssErrorClass="label labelError">Nama Nasabah</form:label>
												<form:errors path="customer.nama" cssClass="error" />
											</div>
											<c:choose>
												<c:when test="${policy.mode eq 'VIEW'}"><form:hidden path="customer.nama" /><input type="text" class="text_field_read" value="${policy.customer.nama}" readonly="readonly" /></c:when>
												<c:otherwise>
													<form:input path="customer.nama" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40"/>
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
												<form:label path="bank.clause" cssClass="label" cssErrorClass="label labelError">Banker Clause</form:label>
												<form:errors path="bank.clause" cssClass="error" />
											</div>
											<c:choose>
												<c:when test="${policy.mode eq 'VIEW'}"><form:hidden path="bank.clause" /><input type="text" class="text_field_read" value="${policy.bank.clause}" readonly="readonly" /></c:when>
												<c:otherwise>
													<form:input path="bank.clause" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40"/>
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
												<form:label path="customer.address.alamat_rumah" cssClass="label" cssErrorClass="label labelError">Alamat Nasabah </form:label>
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
												<form:label path="customer.address.telp_rumah"
													cssClass="label" cssErrorClass="label labelError">Telp Rumah</form:label>
												<form:errors path="customer.address.telp_rumah"
													cssClass="error" />
											</div>
											<form:input path="customer.address.telp_rumah"
												cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
										<td class="gap"></td>
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
									
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.alamat_rumah" cssClass="label" cssErrorClass="label labelError">Alamat Object Pertanggungan</form:label>
												<form:errors path="customer.addressInsured.alamat_rumah" cssClass="error" />
											</div>
											<form:textarea path="customer.addressInsured.alamat_rumah" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.kota_rumah" cssClass="label" cssErrorClass="label labelError">Kota</form:label>
												<form:errors path="customer.addressInsured.kota_rumah" cssClass="error" />
											</div>
											<form:input path="customer.addressInsured.kota_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
									<td class="gap"></td>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.kodepos_rumah" cssClass="label" cssErrorClass="label labelError">Kode Pos</form:label>
												<form:errors path="customer.addressInsured.kodepos_rumah" cssClass="error" />
											</div>
											<form:input path="customer.addressInsured.kodepos_rumah" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div></td>
								</tr>
								
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.no_sertifikat" cssClass="label" cssErrorClass="label labelError">No. SHM / SHGB</form:label>
												<form:errors path="customer.addressInsured.no_sertifikat" cssClass="error" />
											</div>
											<form:input path="customer.addressInsured.no_sertifikat" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div>
									</td>
									<td class="gap"></td>
									<td>
									</td>
									<td class="gap"></td>
									<td>
									</td>
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.penggunaan_bangunan" cssClass="label" cssErrorClass="label labelError">Penggunaan Bangunan (tahun)</form:label>
												<form:errors path="customer.addressInsured.penggunaan_bangunan" cssClass="error" />
											</div>
											<form:input path="customer.addressInsured.penggunaan_bangunan" cssClass="text_field" cssErrorClass="text_field inputError" />
										</div>
									</td>
									<td class="gap"></td>
									<td>
									</td>
									<td class="gap"></td>
									<td>
									</td>
								</tr>
								<tr>
									<td>
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="ins_period" cssClass="label" cssErrorClass="label labelError">Periode Pertanggungan</form:label>
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
									<td colspan="3"><br/><br/>
										<label class="label">Harga Pertanggungan )*</label>
									</td>
								</tr>
								<tr>
									<td class="sorong">
									
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.harga_bangunan" cssClass="label" cssErrorClass="label labelError">a. Bangunan (Rp)</form:label>
												<form:errors path="customer.addressInsured.harga_bangunan" cssClass="error" />
											</div>
											
										</div>
									</td>
									<td class="gap"></td>
									<td colspan="3"  >
										<form:input path="customer.addressInsured.harga_bangunan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
										<table>
											<tr>
												<td>
											
													<div class="group" >
														<div class="fieldWithErrors">
															<form:label path="customer.addressInsured.luas_bangunan" cssClass="label" cssErrorClass="label labelError">Luas Tanah (m<sup>2</sup>)</form:label>
															<form:errors path="customer.addressInsured.luas_bangunan" cssClass="error" />
														</div>
														<form:input path="customer.addressInsured.luas_bangunan" cssClass="text_field" cssErrorClass="text_field inputError" />
													</div>
												</td>
												<td>
													<div class="group">
														<div class="fieldWithErrors">
															<form:label path="customer.addressInsured.luas_tanah" cssClass="label" cssErrorClass="label labelError">Luas Bangunan (m<sup>2</sup>)</form:label>
															<form:errors path="customer.addressInsured.luas_tanah" cssClass="error" />
														</div>
														<form:input path="customer.addressInsured.luas_tanah" cssClass="text_field" cssErrorClass="text_field inputError" />
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
												<form:label path="customer.addressInsured.harga_stock" cssClass="label" cssErrorClass="label labelError">b. Stock (Rp)</form:label>
												<form:errors path="customer.addressInsured.harga_stock" cssClass="error" />
											</div>
											
										</div>
									</td>
									<td class="gap"></td>
									<td colspan="3"  >
										<form:input path="customer.addressInsured.harga_stock" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
									</td>
									
								</tr>
								<tr>
									<td  class="sorong">
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.harga_perabot" cssClass="label" cssErrorClass="label labelError">c. Perabot (Rp)</form:label>
												<form:errors path="customer.addressInsured.harga_perabot" cssClass="error" />
											</div>
											
										</div>
									</td>
									<td class="gap"></td>
									<td colspan="3"  >
										<form:input path="customer.addressInsured.harga_perabot" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
									</td>
									
								</tr>
								<tr>
									<td   class="sorong">
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.harga_lainnya" cssClass="label" cssErrorClass="label labelError">d. Lainnya (Rp)</form:label>
												<form:errors path="customer.addressInsured.harga_lainnya" cssClass="error" />
											</div>
											
										</div>
									</td>
									<td class="gap"></td>
									<td colspan="3"  >
										<form:input path="customer.addressInsured.harga_lainnya" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
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
										<form:input path="product.up" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
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
												<form:label path="customer.addressInsured.kondisi_kiri" cssClass="label" cssErrorClass="label labelError">- Sebelah Kiri</form:label>
												<form:errors path="customer.addressInsured.kondisi_kiri" cssClass="error" />
											</div>
											
										</div>
									</td>
									<td class="gap"></td>
									<td colspan="3"  >
										<form:input path="customer.addressInsured.kondisi_kiri" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
									</td>
									
								</tr>
								<tr>
									<td   class="sorong">
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.kondisi_kanan" cssClass="label" cssErrorClass="label labelError">- Sebelah Kanan</form:label>
												<form:errors path="customer.addressInsured.kondisi_kanan" cssClass="error" />
											</div>
											
										</div>
									</td>
									<td class="gap"></td>
									<td colspan="3"  >
										<form:input path="customer.addressInsured.kondisi_kanan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
									</td>
									
								</tr>
								<tr>
									<td class="sorong">
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.kondisi_depan" cssClass="label" cssErrorClass="label labelError">- Sebelah Depan</form:label>
												<form:errors path="customer.addressInsured.kondisi_depan" cssClass="error" />
											</div>
											
										</div>
									</td>
									<td class="gap"></td>
									<td colspan="3"  >
										<form:input path="customer.addressInsured.kondisi_depan" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
									</td>
									
								</tr>
								<tr>
									<td   class="sorong">
										<div class="group">
											<div class="fieldWithErrors">
												<form:label path="customer.addressInsured.kondisi_belakang" cssClass="label" cssErrorClass="label labelError">- Sebelah Belakang</form:label>
												<form:errors path="customer.addressInsured.kondisi_belakang" cssClass="error" />
											</div>
											
										</div>
									</td>
									<td class="gap"></td>
									<td colspan="3"  >
										<form:input path="customer.addressInsured.kondisi_belakang" cssClass="text_field" cssErrorClass="text_field inputError" maxlength="45" size="40" />
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
												<form:label path="spaj_dateStr" cssClass="label" cssErrorClass="label labelError">Tanggal Pengajuan</form:label>
												<form:errors path="spaj_dateStr" cssClass="error" />
											</div>
											<c:choose>
												<c:when test="${policy.mode eq 'VIEW'}"><form:hidden path="spaj_dateStr" /><input type="text" class="text_field_read" value="${policy.spaj_date}" readonly="readonly" /></c:when>
												<c:otherwise>
													<form:input path="spaj_dateStr" cssClass="text_field datepicker" cssErrorClass="text_field datepicker inputError"/>
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
							
						</div>
						<div class="group navform wat-cf">

							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
								<img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
							</button>
							<span class="text_button_padding"></span>
							 <form:hidden path="mode"/>
							<button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/proses/inputfire'">
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
