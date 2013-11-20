<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>

	<form name="form" id="form" method="post">
	<div id="main" class="fullwidth">
	
	<div class="block" id="block-tables">	
		 <div class="secondary-navigation">
            <ul class="wat-cf">
              <li class="headerTitle" ><a href="#"> Master Menu</a></li>             
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
				<th>
					<c:choose>
                    	<c:when test="${sort eq 'id'}">
	                    	<c:choose>
				              	<c:when test="${sort_type eq 'asc' }"> <a href="javascript:gotoPage('${page}','id','desc','form')" class="up" >ID Menu</a></c:when>
				              	<c:when test="${sort_type eq 'desc' }"><a href="javascript:gotoPage('${page}','id','asc','form')" class="down" >ID Menu</a></c:when>
				              	<c:otherwise><a href="javascript:gotoPage('${page}','id','asc','form')"  >ID Menu</a></c:otherwise>
			              	</c:choose>
			            </c:when>
		              	<c:otherwise>
		              		<a href="javascript:gotoPage('${page}','id','asc','form')"  >ID Menu</a>
		              	</c:otherwise>
	              	</c:choose>
				</th>
				<th>
					<c:choose>
                    	<c:when test="${sort eq 'parent_nama'}">
	                    	<c:choose>
				              	<c:when test="${sort_type eq 'asc' }"> <a href="javascript:gotoPage('${page}','parent_nama','desc','form')" class="up" >Parent Menu</a></c:when>
				              	<c:when test="${sort_type eq 'desc' }"><a href="javascript:gotoPage('${page}','parent_nama','asc','form')" class="down" >Parent Menu</a></c:when>
				              	<c:otherwise><a href="javascript:gotoPage('${page}','parent_nama','asc','form')"  >Parent Menu</a></c:otherwise>
			              	</c:choose>
			            </c:when>
		              	<c:otherwise>
		              		<a href="javascript:gotoPage('${page}','parent_nama','asc','form')"  >Parent Menu</a>
		              	</c:otherwise>
	              	</c:choose>
				</th>
				<th>
					<c:choose>
                    	<c:when test="${sort eq 'nama'}">
	                    	<c:choose>
				              	<c:when test="${sort_type eq 'asc' }"> <a href="javascript:gotoPage('${page}','nama','desc','form')" class="up" >Nama Menu</a></c:when>
				              	<c:when test="${sort_type eq 'desc' }"><a href="javascript:gotoPage('${page}','nama','asc','form')" class="down" >Nama Menu</a></c:when>
				              	<c:otherwise><a href="javascript:gotoPage('${page}','nama','asc','form')"  >Nama Menu</a></c:otherwise>
			              	</c:choose>
			            </c:when>
		              	<c:otherwise>
		              		<a href="javascript:gotoPage('${page}','nama','asc','form')"  >Nama Menu</a>
		              	</c:otherwise>
	              	</c:choose>
				</th>
				<th>
					<c:choose>
                    	<c:when test="${sort eq 'link'}">
	                    	<c:choose>
				              	<c:when test="${sort_type eq 'asc' }"> <a href="javascript:gotoPage('${page}','link','desc','form')" class="up" >Link</a></c:when>
				              	<c:when test="${sort_type eq 'desc' }"><a href="javascript:gotoPage('${page}','link','asc','form')" class="down" >Link</a></c:when>
				              	<c:otherwise><a href="javascript:gotoPage('${page}','link','asc','form')"  >Link</a></c:otherwise>
			              	</c:choose>
			            </c:when>
		              	<c:otherwise>
		              		<a href="javascript:gotoPage('${page}','link','asc','form')"  >Link</a>
		              	</c:otherwise>
	              	</c:choose>
				</th>
				<th>
					<c:choose>
                    	<c:when test="${sort eq 'urut'}">
	                    	<c:choose>
				              	<c:when test="${sort_type eq 'asc' }"> <a href="javascript:gotoPage('${page}','urut','desc','form')" class="up" >Urut</a></c:when>
				              	<c:when test="${sort_type eq 'desc' }"><a href="javascript:gotoPage('${page}','urut','asc','form')" class="down" >Urut</a></c:when>
				              	<c:otherwise><a href="javascript:gotoPage('${page}','urut','asc','form')"  >Urut</a></c:otherwise>
			              	</c:choose>
			            </c:when>
		              	<c:otherwise>
		              		<a href="javascript:gotoPage('${page}','urut','asc','form')"  >Urut</a>
		              	</c:otherwise>
	              	</c:choose>
				</th>
				<th>
					<c:choose>
                    	<c:when test="${sort eq 'createby'}">
	                    	<c:choose>
				              	<c:when test="${sort_type eq 'asc' }"> <a href="javascript:gotoPage('${page}','p.createby','desc','form')" class="up" >Create By</a></c:when>
				              	<c:when test="${sort_type eq 'desc' }"><a href="javascript:gotoPage('${page}','p.createby','asc','form')" class="down" >Create By</a></c:when>
				              	<c:otherwise><a href="javascript:gotoPage('${page}','p.createby','asc','form')"  >Create By</a></c:otherwise>
			              	</c:choose>
			            </c:when>
		              	<c:otherwise>
		              		<a href="javascript:gotoPage('${page}','p.createby','asc','form')"  >Create By</a>
		              	</c:otherwise>
	              	</c:choose>
				</th>
				<th>
					<c:choose>
                    	<c:when test="${sort eq 'createdate'}">
	                    	<c:choose>
				              	<c:when test="${sort_type eq 'asc' }"> <a href="javascript:gotoPage('${page}','createdate','desc','form')" class="up" >Create Date</a></c:when>
				              	<c:when test="${sort_type eq 'desc' }"><a href="javascript:gotoPage('${page}','createdate','asc','form')" class="down" >Create Date</a></c:when>
				              	<c:otherwise><a href="javascript:gotoPage('${page}','createdate','asc','form')"  >Create Date</a></c:otherwise>
			              	</c:choose>
			            </c:when>
		              	<c:otherwise>
		              		<a href="javascript:gotoPage('${page}','createdate','asc','form')"  >Create Date</a>
		              	</c:otherwise>
	              	</c:choose>
				</th>
				 <th class="last">Actions</th>
			</tr>
			
			<c:forEach items="${listMenu}" var="p">
			<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
				<td></td>
				<td>${p.id}</td>
				<td>${p.parent_nama}</td>
				<td>${p.nama}</td>
				<td>${p.link}</td>
				<td>${p.urut} </td>
				<td>${p.createby}</td>
				<td><fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
	
				<td class="last">
						<a href="${path}/admin/menu/edit/${p.id}" title="edit">
	                      <img src="${path }/static/pilu/images/icons/application_edit.png" alt="Edit" />
	                    </a>
	                    <a href="${path}/admin/menu/view/${p.id}" title="view">
	                      <img src="${path }/static/pilu/images/icons/view.png" alt="View" />
	                    </a>
	                    <a href="javascript:if(confirm('Are you sure want to Delete \'Id : ${p.id}\' ?'))window.location='${path}/admin/menu/delete/${p.id}';" title="delete">
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
                    <button class="button" type="button" onclick="window.location='${path}/admin/menu/new';">
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
		
</form>
	
</body>
</html>