<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>User Not Found Error page</title>

<link rel="stylesheet" type="text/css" href="css/jquery/ui/uicss.css">
<script type="text/javascript" src="js/jquery/jquery-1.8.0.min.js"></script>

<script type="text/javascript">
	function onShow(){
		var url=document.getElementById("hidurl").value;
		if(url!=""){
			window.location.href=url;
		}
	}
	</script>
</head>
<body onload="onShow()">
	<h2>找不到您的帳號資料</h2>
	<p>
		<input id="hidurl" type="hidden" value="${hidurl}"> <a
			id="gotoNotFound" class="logout" href="gotoNotFound">返回圖書館員工入口</a>
	</p>
</body>
</html>