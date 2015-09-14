function sort(sortclass,sortfield){
	var orderType = $("#orderType").val();
	$("#currentPage").val(1);
	if("asc" == orderType){
		$("#orderType").val("desc");
	}else if("desc" == orderType){
		$("#orderType").val("asc");
	}else{
		$("#orderType").val("desc");
	}
	var sortTh = $("th").index(sortclass);
	$("#sortTh").val(sortTh);
	$("#orderField").val(sortfield);
	$('#form1').submit();
}

function pagechange(index){
	$("#isPage").val('true');
	$("#currentPage").val(index);
	$('#form1').submit();
/*    var url = document.location;
    var regExp = /(S*\?page=)(S*)/;
    var arr = regExp.exec(url);
   
    if(arr==null)url = url+"?page="+index;
    else{ 
        url = arr[1]+index;
    }
    //alert(url);
    window.location = url;*/
}