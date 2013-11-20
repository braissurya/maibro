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
              <li class="headerTitle"><a href="#"> Change Password</a></li>             
            </ul>
          </div>
         
		 <div class="content" >
			<div class="inner">
				<form:form commandName="user" name="formpost" method="POST" action="${path}/changepass" cssClass="form">
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
											<form:label path="password" cssClass="label" cssErrorClass="label labelError">Old Password</form:label>
											<form:errors path="password" cssClass="error" />
										</div>
										<form:password path="password"/>
										<span class="description"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="newPassword" cssClass="label" cssErrorClass="label labelError">New Password</form:label>
											<form:errors path="newPassword" cssClass="error" />
										</div>
										<form:password path="newPassword"/>
										<span class="description"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="group">
										<div class="fieldWithErrors">
											<form:label path="confirmPassword" cssClass="label" cssErrorClass="label labelError">Confirm Password</form:label>
											<form:errors path="confirmPassword" cssClass="error" />
										</div>
										<form:password path="confirmPassword"/>
										<span class="description"></span>
									</div>
								</td>
							</tr>
						</table>
					</div>
					
					<div class="group navform wat-cf">

						<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
							<img src="${path }/static/pilu/images/icons/tick.png" alt="Save" /> Save
						</button>
						<span class="text_button_padding"></span>
						 <form:hidden path="mode"/>
						<button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/changepass'">
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