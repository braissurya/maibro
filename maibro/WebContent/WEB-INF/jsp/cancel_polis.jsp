<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
</head>
<body>

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
	
 	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle"><a href="#"> Cancel Polis</a></li>             
            </ul>
          </div>
         
		 <div class="content" >
			<div class="inner">
			<br>
				<form:form commandName="policy" name="formpost" method="POST" action="${path}/cancel/${policy_id}" cssClass="form">
				<table class="grid">
					<tr>
						<th>Spaj / Polis</th>
						<td>${policy.spaj_no} / ${policy.policy_no}</td>
					</tr>
					<tr>
						<th>Posisi Dokumen Polis</th>
						<td>${policy.posisiKet}</td>
					</tr>
					<tr>
						<th>Periode Asuransi</th>
						<td><fmt:formatDate value="${policy.beg_date}" pattern="dd-MM-yyyy"/> s/d <fmt:formatDate value="${policy.end_date}" pattern="dd-MM-yyyy"/></td>
					</tr>
					<tr>
						<th>Produk</th>
						<td>${policy.namaproduk}</td>
					</tr>
					<tr>
						<th>Premi</th>
						<td><fmt:formatNumber pattern="#,###.00" value="${policy.premi}" /></td>
					</tr>
					<tr>
						<th>Uang Pertanggungan / Plafon Kredit</th>
						<td><fmt:formatNumber pattern="#,###.00" value="${policy.up}" /></td>
					</tr>
					<tr>
						<th>Nasabah</th>
						<td>${policy.namacustomer}</td>
					</tr>
					<tr>
						<th>Bank / Cabang</th>
						<td>${policy.namabank} / ${policy.namacabbank}</td>
					</tr>
					<tr>
						<th>Asuransi</th>
						<td>${policy.namaasuransi}</td>
					</tr>
					<tr>
						<th class="fieldWithErrors">
							<form:label path="keteranganCancel" cssClass="label" cssErrorClass="label labelError">Alasan Pembatalan<span class="mandatory" title="Wajib diisi"> *</span></form:label>
							<form:errors path="keteranganCancel" cssClass="error" />
						</th>
						<td>
							<form:textarea path="keteranganCancel" cssClass="textarea" cssErrorClass="textarea inputError" rows="3" cols="35"/>						
						</td>
					</tr>
					<tr>
						<td colspan="2" class="messageBox">
							<strong>Catatan:</strong>
							<br/>- Pembatalan Polis bersifat permanen. Harap melakukan pengecekan ulang sebelum Anda membatalkan Polis.
						</td>
					</tr>
				</table>
				<div class="group navform wat-cf">
				
					<button class="button" type="submit" name="cancel_polis" onclick="if(!confirm('Are you sure want to cancel Polis?'))return false;">
						<img src="${path }/static/pilu/images/icons/tick.png" alt="Cancel Polis" /> Cancel Polis
					</button>
				</div>
				</form:form>
			</div>
	     </div>
			   
		</div>
	
	 </div>
		
</body>
</html>