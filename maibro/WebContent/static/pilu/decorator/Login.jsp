<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html >
<html lang="en-US">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>${company.name} - <decorator:title  default="Selamat Datang"/></title>
    <link href="${path}/static/favicon.ico" rel="shortcut icon"/>
  <link rel="stylesheet" href="${path }/static/pilu/css/base.css" type="text/css" media="screen" />
  <link rel="stylesheet" id="current-theme" href="${path }/static/pilu/css/themes/warehouse/style.css" type="text/css" media="screen" />
  <script type="text/javascript" charset="utf-8" src="${path }/static/jquery-1.3.min.js"></script>
  <script type="text/javascript" charset="utf-8" src="${path }/static/jquery.scrollTo.js"></script>
  <script type="text/javascript" charset="utf-8" src="${path }/static/jquery.localscroll.js"></script>
  <script type="text/javascript" charset="utf-8" src="${path }/static/default.js"></script>
  
</head>
<body>
  <div id="container" style="height: 100%">
       
    <div id="box">
      <h1></h1>
      <div class="block" id="block-login">
        <h2>Login Page</h2>
        <div class="content login">
        	 <img class="headerImage" src="${path}/static/img/maibro.png" width="300px">
        	<c:if test="${not empty message }">
	          <div class="flash">
	            <div class="message ${messageType }">
	              <p>${message }</p>
	            </div>
	          </div>
         	 </c:if>
          
          <form:form commandName="user" method="POST" cssClass="form login">
            <div class="group wat-cf">
              <div class="left">
                <form:label path="username" cssClass="label right">Username</form:label>
              </div>
              <div class="right">
                <form:input path="username" cssClass="text_field" />
                 <div class="fieldWithErrors">
                	<form:errors path="username" cssClass="error" />
                </div>
              </div>
            </div>
            <div class="group wat-cf">
              <div class="left">
               <form:label path="password" cssClass="label right">Password </form:label>
              </div>
              <div class="right">
                <form:password path="password" cssClass="text_field" />
                <div class="fieldWithErrors">
                <form:errors path="password" cssClass="error" />
                </div>
              </div>
            </div>
            <div class="group navform wat-cf">
              <div class="right">
                <button class="button" type="submit">
                  <img src="${path }/static/pilu/images/icons/key.png" alt="Login" /> Login
                </button>
              </div>
            </div>
           
          </form:form>
        </div>
      </div>
	 <div id="wrapper" class="wat-cf" style="padding-left: 180px;">
	 		<div style="text-align: center;">
                <button class="button" type="button" onclick="window.open('${path }/static/comp_profile.pdf');">
                  <img src="${path }/static/pilu/images/icons/company.png" alt="company profile" /> Company Profile
                </button>
              </div>
    		
   	</div>
			
		
    </div>      
        
  </div>
   <div id="mainfooter">
  		
			<div  class="copyright" >
				${company.name}<br>${company.address}<br>${company.copyright}
			</div>
	  				
	  </div>
  
  
</body>
</html>

