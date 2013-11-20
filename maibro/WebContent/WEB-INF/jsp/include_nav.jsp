<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<div id="nav">
<!-- 	Nav: -->
<!-- 	<a href="${path}">Home</a> -->
<!-- 	<a href="${path}/logout">Logout</a> -->
	<c:forEach items="${sessionScope.currentUser.listMenu}" var="m" varStatus="s">
		<c:choose>
			<c:when test="${parent ne m.parent}">
				<c:if test="${s.index gt 0}">
						</ul>
					</ul>
				</c:if>
				<ul>
				<c:if test="${s.index eq 0}">
					<li><a href="${path}">Home</a></li>
					<li><a href="${path}/logout">Logout</a></li>
				</c:if>
					<li><a>${m.parent_nama}</a>
					<ul>
						<li><a href="${path}/${m.link}">${m.nama}</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="${path}/${m.link}">${m.nama}</a></li>
			</c:otherwise>
		</c:choose>
		<c:set var="parent" value="${m.parent}"></c:set>
	</c:forEach>
</div>