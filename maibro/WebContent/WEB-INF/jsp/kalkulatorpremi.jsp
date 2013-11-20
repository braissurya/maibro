<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
<script type="text/javascript">
	
	//fungsi2 yang dijalankan saat document sudah semua loaded
	$(document).ready(function() {
	
		//onclick dari radio button product
		$("[name='produk']").click( function(){
			$("[id^='section']").hide();
			$("#section" + this.value).fadeIn();
		});
		
		//onclick dari tombol calculate
		$("#calculate").click( function(){
			
			$.getJSON('${path}/hitungpremi', {
				produk:$("[name='produk']:checked").val(), 
				plafon:$("[name='plafon']").val(),
				ins:$("[name='ins']").val(),
				usia:$("[name='usia']").val(),
				jenisbangunan:$("[name='jenisbangunan']").val()
			}, function(data) {

				if(data != null){
					var pesan = "KALKULATOR PREMI\n";

					if(data.rate != null) pesan += "\nRate = " + formatCurrency(data.rate) + "%";
					if(data.premi != null) pesan += "\nPremi = Rp. " + formatCurrency(data.premi);
					if(data.rateDesc != null) pesan += "\nJenis Rate = " + data.rateDesc;
					if(data.medisDesc != null) pesan += "\nJenis Medis = " + data.medisDesc;
					if(data.catatan != null) pesan += "\nCatatan = " + data.catatan;
					if(data.catatan2 != null) pesan += "\nCatatan = " + data.catatan2;
					if(data.errm != null) pesan += "\n" + data.errm;
					
					alert(pesan);
					return;					
				}
				
				alert("Data tidak ada");
			});
		});
		$("#section3").fadeIn();
	});
	
</script>
</head>
<body>
	<div id="main" class="fullwidth">
	
 	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle"><a href="#"> Kalkulator Premi Sementara</a></li>             
            </ul>
          </div>
         
		 <div class="content" >
			<div class="inner">
				<form method="post">
				<table class="grid">
					<tr>
						<th>Produk</th>
						<td>
							<c:forEach items="${listProduk}" var="p">
								<input type="radio" name="produk" value="${p.key}" id="produk${p.key}"><label for="produk${p.key}"> ${p.value}</label>&nbsp;
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td colspan="2"><hr></td>				
					</tr>	
					<tr>
						<td colspan="2">
							<table>
								<tr>
									<th style="width: 250px;">Plafon Kredit (Life) / Nilai Bangunan (Fire)</th>
									<td><input name="plafon" type="text" style="text-align: right;" class="nominal"><span class="desc"> Rupiah</span></td>
								</tr>
								<tr>
									<th>Masa Asuransi</th>
									<td><input name="ins" type="text" size="2"><span class="desc"> tahun</span></td>
								</tr>
							</table>
						
							<table id="section1" style="display: none;"> <!-- KPR Life -->
								<tr >
									<th style="width: 250px;">Usia Debitur</th>
									<td><input name="usia" type="text" size="2"><span class="desc"> tahun</span></td>
								</tr>
								<tr>
									<td colspan="2" class="messageBox">
										<strong>Catatan:</strong>
										<br/>- Hasil perhitungan premi adalah simulasi apabila kondisi STANDAR (tidak dikenakan ekstra premi).
										<br/>- Hasil perhitungan premi bersifat SEMENTARA, silahkan melakukan konfirmasi dengan pihak MaiBro.
									</td>
								</tr>
							</table>
							<table  id="section2" style="display: none;"> <!-- KPR Fire -->
								<tr>
									<th style="width: 250px;">Jenis Bangunan</th>
									<td>
										<select name="jenisbangunan">
											<c:forEach items="${listJenisBangunan}" var="b">
												<option value="${b.key}">${b.value}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="messageBox">
										<strong>Catatan:</strong>
										<br/>- Hasil perhitungan premi adalah simulasi apabila kondisi STANDAR (tidak dikenakan ekstra premi).
										<br/>- Hasil perhitungan premi bersifat SEMENTARA, silahkan melakukan konfirmasi dengan pihak MaiBro.
									</td>
								</tr>
							</table>
							<table id="section3" style="display: none;"> <!-- Micro Life -->
								<tr>
									<td colspan="2" class="messageBox">
										<strong>Catatan:</strong>
										<br/>- Hasil perhitungan premi adalah simulasi apabila kondisi STANDAR (tidak dikenakan ekstra premi).
										<br/>- Hasil perhitungan premi bersifat SEMENTARA, silahkan melakukan konfirmasi dengan pihak MaiBro.
									</td>
								</tr>
							</table>
							
						</td>
					</tr>
				</table>
				</form>
				
				<div class="group navform wat-cf">
					<button class="button" type="button" name="calculate" id="calculate">
						<img src="${path }/static/pilu/images/icons/tick.png" alt="Calculate" /> Calculate
					</button>
					<span class="text_button_padding"></span>
					<button class="button" type="reset">
						<img src="${path }/static/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
					</button>
				</div>
			</div>
	     </div>
			   
		</div>
	
	 </div>
		
</body>
</html>