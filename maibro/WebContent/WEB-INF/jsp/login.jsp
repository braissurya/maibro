<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<jsp:include page="include_head.jsp" />
</head>
<body>

	<jsp:include page="include_header.jsp" />

	<div id="content">
		<form:form commandName="user" method="POST">
			<table>
				<tr>
					<th><form:label path="username">Username: </form:label></th>
					<td><form:input path="username" /><form:errors path="username" cssClass="err" /></td>
				</tr>
				<tr>
					<th><form:label path="password">Password: </form:label></th>
					<td><form:password path="password"/><form:errors path="password" cssClass="err" /></td>
				</tr>
				<tr>
					<th></th>
					<td><input type="submit" value="Login"></td>
				</tr>
			</table>
		<%-- <form:errors path="*" cssClass="err" /> --%>
	</form:form>
	</div>
	
	<jsp:include page="include_footer.jsp" />

</body>
</html>