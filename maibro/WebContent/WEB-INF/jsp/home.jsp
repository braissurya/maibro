<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<div id="main">
	
 	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle"><a href="#"> Home</a></li>             
            </ul>
          </div>
         
		 <div class="content" >
		 	     <div class="inner">
		 	      <p><jsp:include page="include_nav.jsp" /></p>
			</div>			
	
	     </div>
			   
		</div>

	 </div>
		
	<div id="sidebar">
		
		<div class="block">
          <h3>User Information</h3>
          <table class="sidebar">
          	<tr>
          		<th>Username:</th>
          		<td>${sessionScope.currentUser.username}</td>
          	</tr>
          	<tr>
          		<th>Type:</th>
          		<td>${sessionScope.currentUser.namabank} - ${sessionScope.currentUser.namacabang}</td>
          	</tr>
          	<tr>
          		<th>Login Time:</th>
          		<td><fmt:formatDate value="${sessionScope.currentUser.loginTime}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
          	</tr>
          </table>
        </div>
        
        <div class="block">
          <h3>Tools</h3>
          <table class="sidebar">
          	<tr>
          		<td><a href="${path}/calc">Kalkulator Premi</a></td>
          	</tr>
          	<tr>
          		<td><a href="${path}/changepass">Rubah Password</a></td>
          	</tr>
          </table>
        </div>
        
          <div class="block">
          <h3>Rekening Maibro</h3>
            <table class="sidebar">
          <c:forEach items="${company.rekening }" var="cr" varStatus="s">
          		<tr>
          			<th>${s.count }</th>
	          		<th style="text-align: left;">Nama Produk </th>
	          		<th>:</th>
	          		<td>${cr.nama }</td>
	          	</tr>
	          	<tr>
	          		<th></th>
	          		<th style="text-align: left;">No Rekening</th>
	          		<th>:</th>
	          		<td>${cr.rek_no }</td>
	          	</tr>
	          	<tr>
	          		<th></th>
	          		<th style="text-align: left;">Atas Nama </th>
	          		<th>:</th>
	          		<td>${cr.rek_nama }</td>
	          	</tr>
	          	
          </c:forEach>
        
          	
          </table>
        </div>

	</div>
	
</body>
</html>