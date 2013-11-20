<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
	$(document).ready(function () {
		$("#showDetail").click(function () {
			 $("#stackTrace").animate({
		      height:'toggle'
		    });
		});
	});
	</script>
</head>
<body>
	
<div id="main">
	<div class="block" id="block-messages">
          <div class="secondary-navigation">
            <ul class="wat-cf">
             <li class="headerTitle"><a href="#">Error Page</a></li> 
            </ul>
          </div>
          <div class="content">
            <h2 class="title">Messages</h2>
            <div class="inner">
              <div class="flash">
                <div >
                  <p>Maaf telah terjadi kesalahan pada aplikasi.<br/>
                     Detail aplikasi sudah dikirim ke administrator dan dapat dilihat di  bawah.<br/>
                     Untuk info lebih lanjut silahkan email ke :<br/>
               			
                  </p>
                 
                </div>
                 <ul style="margin-left: 20px">
              		<li>
               			Administrator
               			<div class="group">
		                   Email :
			               maibro_it@maibro.co.id
						</div>
       				</li>
       				
              	</ul>
              	   <br/>     
              		<a href="${path}">&laquo; Kembali ke Halaman Utama</a>
					<br/><a href="#" onclick="history.go(-1);return false;">&laquo; Kembali ke Halaman Sebelumnya</a>
			   		<br/><br/>
			   		<a href="#" id="showDetail">&laquo;Show / Hide Error Detail &raquo; </a>
              </div>
            </div>
			<div id="stackTrace" style="display: none;overflow: auto;margin: 20px;width: 90%;border: 1px solid #aaa;height: 200px">
						<pre>${exception}</pre>
			</div>
          </div>
        </div>
	</div>

</body>
</html>