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
  <link rel="stylesheet" id="current-theme" href="${path }/static/pilu/css/themes/default/style.css" type="text/css" media="screen" />
  <link rel="stylesheet"  href="${path }/static/css/style.css" type="text/css" media="screen" />
  
  <link rel="stylesheet" type="text/css" href="${path }/static/css/ddlevelsmenu-base.css"  media="screen" />
  <link rel="stylesheet" type="text/css" href="${path }/static/css/ddlevelsmenu-topbar.css"  media="screen" />
  
  <link rel="stylesheet" type="text/css" href="${path }/static/css/${company.pageCSS}"  media="screen" />
  
	<link rel="stylesheet" href="${path}/static/jquery/themes/base/jquery.ui.all.css">
  
   <script type="text/javascript" charset="utf-8" src="${path }/static/default.js"></script>
	<script type="text/javascript" charset="utf-8" src="${path }/static/jquery/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="${path }/static/jquery/jquery-ui-1.10.0.custom.min.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/jquery/jquery.hotkeys.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/jquery/jquery.scrollTo.js"></script>
 	
 <script type="text/javascript" src="${path }/static/ddlevelsmenu.js">
	
	/***********************************************
	* All Levels Navigational Menu- (c) Dynamic Drive DHTML code library (http://www.dynamicdrive.com)
	* This notice MUST stay intact for legal use
	* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
	***********************************************/
	
   </script>
  
	<script>
	$(function() {
		$(".datepicker").datepicker({
			changeMonth: true,
			changeYear: true,
			dateFormat: "dd-mm-yy"
		});
		
		//untuk semua input yg mempunyai class "nominal" akan diformat 3 digit secara otomatis
		$(".nominal").change(function(){
			$(this).val(formatCurrency($(this).val()));
			 $("#nominal").text("");
		});
		
		$(".nominal").keyup(function(){
			 $("#nominal").text(formatCurrency($(this).val()));
		});
		
	});
	</script>
	
	  <script type="text/javascript">
	// Jalankan semua script jquery, setelah seluruh document selesai loading
		$().ready(function() {
			// pesan error/sukses
			var pesan = '${pesan}'.replace(/<br\s*[\/]?>/gi, "\n");
			if(pesan!='')alert(pesan);
		});
		
		$(document).ready(function(){
		  $("#target").focus();
		
		   $(".navi").scrollTo('#selected');
		  
		   $("#filter").keyup(function(){
		 
		        // Retrieve the input field text and reset the count to zero
		        var filter = $(this).val(), count = 0;
		 
		        // Loop through the comment list
		        $(".navigation_filter li").each(function(){
		 
		            // If the list item does not contain the text phrase fade it out
		            if ($(this).text().search(new RegExp(filter, "i")) < 0) {
		                $(this).fadeOut();
		 
		            // Show the list item if the phrase matches and increase the count by 1
		            } else {
		                $(this).show();
		                count++;
		            }
		            
		        });
		 
		        // Update the count
		        //var numberItems = count;
		        $("#filter-count").text(count+" data");
		         $(".navi").scrollTo('#selected');
		    });
		    
		  
		});
		
			 
	</script>
	  
    <decorator:head></decorator:head>
</head>
<body>
  <div id="container" style="height: 100%">
       
    <div id="header">
     <img alt="Maibro" src="${path}/static/img/maibro.png" width="300px">
      <div id="user-navigation">
        <ul class="wat-cf">          
          <li><a href="${path}/changepass">Change Password</a></li>
          <li><a class="logout" href="javascript:if(confirm('Are you sure want to Logout?'))window.location='${path }/logout';">Logout</a></li>
        </ul>
      </div>
      <div id="main-navigation">
		${sessionScope.currentUser.menuUser } 
      </div>
    </div>
    <div id="wrapper" class="wat-cf">
    	<div style="min-height: 300px;">    	
    		<decorator:body></decorator:body>
    	</div>
   	</div>
  </div>
  <div id="mainfooter">
  		
  					<div  class="copyright">
  						${company.name}<br>${company.address}<br>${company.copyright}
  					</div>
  				
  </div>
  
  
  


</body>
</html>

