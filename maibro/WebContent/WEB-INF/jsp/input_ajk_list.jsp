<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>

	<form name="form" id="form" method="post">
	<div id="main">
	
	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle" ><a href="#"> Input AJK</a></li>             
            </ul>
          </div>
		
		
		 <div class="content">
		 	<table style="width: 100%">
		 		<tr>
		 		<td>
		            <div class="total">Total Data : ${totalData }</div>
		            <div class="search">
		            	<input type="text" name="s"  value="${search}" size="30" class="textfield">
		            	<input type="submit" name="btsearch"  value=" " class="btsearch" title="search">
		            	
		            </div>
            	</td>
            	</tr>
            	
            <tr>
		 		<td>
            <div class="inner">
      
              	
       	<table class="table">
			<tr>
				<th class="first"></th>
				<th>No Spaj</th>
				<th>No Polis</th>
				<th>Debitur</th>
				<th>Produk</th>
				<th>Create By</th>
				<th>Create Date</th>
				 <th class="last">Actions</th>
			</tr>
			
			<c:forEach items="${listPolis}" var="p">
			<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
				<td></td>
				<td>${p.spaj_no}</td>
				<td>${p.policy_no}</td>
				<td>${p.debitur}</td>
				<td>${p.produk}</td>
				<td>${p.createuser}</td>
				<td><fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
	
				<td class="last">
					<a href="${path}/proses/input_ajk/edit/${p.id}" title="edit">
	                      <img src="${path }/static/pilu/images/icons/application_edit.png" alt="Edit" />
	                    </a>
	                    <a href="${path}/proses/input_ajk/view/${p.id}" title="view">
	                      <img src="${path }/static/pilu/images/icons/view.png" alt="View" />
	                    </a>
	                    <a href="javascript:if(confirm('Are you sure want to Delete \'Id : ${u.lpc_id}\' ?'))window.location='${path}/proses/input_ajk/delete/${p.id}';" title="delete">
	                      <img src="${path }/static/pilu/images/icons/delete.png" alt="Delete" />
	                   </a>
				</td>
			</tr>
			</c:forEach>
		</table>             	
                <span class="page">Page : ${page } / ${totalPage }</span>
                <span class="datacount">
	            Row Per Page
	            	<select name="rowcount" onchange="form.submit();">
	            		<c:forEach items="${listNumRows }" var="ln">
	            			<option value="${ln}" <c:if test="${rowcount eq ln}">selected="true"</c:if>>${ln}</option>
	            		</c:forEach>
	            	</select>
	            </span>
                <div class="actions-bar wat-cf">
                  <div class="actions">
                    <button class="button" type="button" onclick="window.location='${path}/proses/input_ajk/new';">
                      <img src="${path }/static/pilu/images/icons/add.gif" alt="Add New" /> Add New
                    </button>
                  </div>
                  <div class="pagination">
                  	<input type="hidden" name="page" id="page" value="${page }">
                  	<input type="hidden" name="sort" id="sort" value="${sort }">
                  	<input type="hidden" name="st" id="st" value="${sort_type}">
                  	<c:choose>
                  		<c:when test="${totalPage eq \"1\" }">
                  			<span class="current">1</span>
                  		</c:when>
                  		<c:when test="${totalPage lt \"10\" }">
                  			<c:choose>
               					<c:when test="${page eq 1 }">
               						<span class="disabled prev_page">« Previous</span>
               					</c:when>
               					<c:otherwise>
               						<a class="prev_page" href="javascript:gotoPage('${page-1 }','${sort }','${sort_type}','form')">« Previous</a>
               					</c:otherwise>
               				</c:choose>
                  			
                  			<c:forEach items="${pages}" var="p" varStatus="s">
                  				<c:choose>
                  					<c:when test="${p eq page }">
                  						<span  class="current">${p}</span>
                  					</c:when>
                  					<c:otherwise>
                  						<a href="javascript:gotoPage('${p}','${sort }','${sort_type}','form');">${p}</a>
                  					</c:otherwise>
                  				</c:choose>	
                  				
                  			</c:forEach>
                  			<c:choose>
               					<c:when test="${page eq totalPage }">
               						<span class="disabled next_page">Next »</span>
               					</c:when>
               					<c:otherwise>
               						<a class="next_page" href="javascript:gotoPage('${page+1 }','${sort }','${sort_type}','form')">Next »</a>
               					</c:otherwise>
               				</c:choose>
                  		</c:when>
                  		<c:when test="${totalPage gt \"9\" }">
                  			
                  			<c:choose>
               					<c:when test="${page eq 1 }">
               						<span class="disabled prev_page">« Previous</span>
               					</c:when>
               					<c:otherwise>
               						<a class="prev_page" href="javascript:gotoPage('${page-1 }','${sort }','${sort_type}','form')">« Previous</a>
               					</c:otherwise>
               				</c:choose>
                  			<c:forEach items="${pages}" var="p" varStatus="s">
                  				<c:choose>
                  					<c:when test="${p eq 1 or p eq 2 or p eq totalPage or p eq totalPage-1}">
               							<c:choose>
		                  					<c:when test="${p eq page }">
		                  						<span  class="current">${p}</span>
		                  					</c:when>
		                  					<c:otherwise>
		                  						<a href="javascript:gotoPage('${p}','${sort }','${sort_type}','form');">${p}</a>
		                  					</c:otherwise>
		                  				</c:choose>	
                  					</c:when>
                  					<c:when test="${p eq halfPage}">
                  						<div class="middle">...</div>
                  						<c:choose>
                  							<c:when test="${page gt 2 and page lt totalPage-1  }">
                  								<input type="text" value="${page}" name="p" class="middle" onchange="gotoPage(this.value,'${sort }','${sort_type}','form');"> 
                  							</c:when>
                  							<c:otherwise>
                  								<input type="text" value="${p}" name="p" class="middle_current" onchange="gotoPage(this.value,'${sort }','${sort_type}','form');"> 
                  							</c:otherwise>
                  						</c:choose>
                  						<div class="middle">...</div>
               							
                  					</c:when>
                  					
                  				</c:choose>
                  				
                  			</c:forEach>
                  			
                  			<c:choose>
               					<c:when test="${page eq totalPage }">
               					
               						<span class="disabled next_page">Next »</span>
               					</c:when>
               					<c:otherwise>
               						<a class="next_page" href="javascript:gotoPage('${page+1 }','${sort }','${sort_type}','form')">Next »</a>
               					</c:otherwise>
               				</c:choose>
                  		</c:when>
                  	</c:choose>
                    	
                  </div>
                </div>
            </div>
            </td>
            </tr>
            </table>
          </div>
          
        </div>
		
	</div>
		
	<div id="sidebar">
		
		<div class="block">
          <h3>Others Link</h3>
          <ul class="navigation">
          	<%--  <li><a href="${path}/maintenance/master/acc_master" >Master Account Master</a></li>     
          	  <li><a href="${path}/maintenance/master/profit_center" >Master Profit Center</a></li> --%>     
          </ul>
        </div>
        <div class="block notice">
          <h4>Page Hint</h4>
          <p>Daftar seluruh polis</p>
        </div>
	</div>
		

	
</form>
	

		
		
		
	
	
</body>
</html>