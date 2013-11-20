//function untuk paging
function gotoPage(page,sort,type,idform){	
	document.getElementById("page").value=page;	
	document.getElementById("sort").value=sort;
	document.getElementById("st").value=type;
	document.getElementById(idform).submit();
}

/* Popup Window, persis ditengah layar */
function popWin(href, height, width,scrollbar,stat) {
	var vWin;
	if(scrollbar!='no')scrollbar='yes';
	if(stat!='yes')stat='no';
	
	vWin = window.open(href,'','height='+height+',width='+width+
		',toolbar=no,directories=no,menubar=no,scrollbars='+scrollbar+',resizable=yes,modal=yes,status=yes'+
		//',toolbar=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,modal=yes,status=yes'+
		',left='+((screen.availWidth-width)/2)+
		',top='+((screen.availHeight-height)/2));
	vWin.opener = self;
} 

//autopopulate select
function autoPopulateSelect(path, ajaxType, parentSelectId, childSelectId, optionALL, selectedId, textAll){
	//ajax autopopulate select box
	$(parentSelectId).change(function(){
		$(childSelectId).children().remove(); //remove all isi dari child

		//populate ulang isi dari child
		$.getJSON(path + '/json/' + ajaxType + '/' + $(parentSelectId).val(), {t:ajaxType, p:$(parentSelectId).val()}, function(data) {
			var html = '';
			if(textAll==null)textAll="ALL";
			if(optionALL == true) html = '<option value="">'+textAll+'</option>';
		    var len = data.length;
		    for (var i = 0; i< len; i++) {
		    	if(selectedId!=null){
		    		var select="";
		    		if(selectedId==data[i].key){
		    			select="selected=\"selected\"";			    			
		    		}
		    		html += '<option value="' + data[i].key + '" '+select+' >' + data[i].value + '</option>';
		    	}else{
		        	html += '<option value="' + data[i].key + '">' + data[i].value + '</option>';
		        }
		    }
		    $(childSelectId).append(html);
		});
	});
}

function formatCurrency( num ){
    num = num.toString().replace(/\,/g, '');
    if( isNaN(num) )
        num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    cents = num % 100;
    num = Math.floor(num / 100).toString();
    if( cents < 10 )
        cents = "0" + cents;
    for( var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++ )
        num = num.substring(0, num.length - (4 * i + 3)) + ',' +
              num.substring(num.length - (4 * i + 3));
    return (((sign) ? '' : '-') + num + '.' + cents);
}



/* redirect dalam x detik */
function timeOut(self, link, timeout){
	if(self.opener){
		if(self.opener.opener){
			self.opener.opener.setTimeout("top.location = '" +link+ "'",timeout);	
			self.opener.setTimeout("window.close();",timeout);
			self.setTimeout("window.close();",timeout);
		}else{
			self.opener.setTimeout("top.location = '" +link+ "'",timeout);
			self.setTimeout("window.close();",timeout);
		}
	}else{
	    self.setTimeout("top.location = '" +link+ "'",timeout);
	}
}


function timedRefresh(timeoutPeriod) {setTimeout("location.reload(true);",timeoutPeriod);}