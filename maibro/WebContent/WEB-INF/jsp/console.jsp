<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>

</head>
<body>
	<div id="main" class="fullwidth">
	
 	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle"><a href="#"> Console</a></li>             
            </ul>
          </div>
         
		 <div class="content" >
		 	     <div class="inner">
		 	     <table>
						<tr>
							<th>ID</th>
							<th>Username</th>
							<th>Logintime</th>
						</tr>
						<c:forEach items="${userMap}" var="u">
							<tr>
								<td>${u.value.id}</td>
								<td>${u.value.username}</td>
								<td><fmt:formatDate value="${u.value.loginTime}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
							</tr>
						</c:forEach>
					</table>
			</div>
			
	
	     </div>
			   
		</div>


	
	 </div>
		
</body>
</html>