<%@page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<script type="text/javascript">
var secs =3; //倒計時的秒數
function Load(){ 
 
for(var i=secs;i>=0;i--) 
{ 
window.setTimeout('doUpdate(' + i + ')', (secs-i) * 1000); 
} 
} 
function doUpdate(num) 
{ 
document.getElementById('ShowDiv').innerHTML = '資源不存在，將在'+num+'秒後自動跳轉到查詢頁面' ; 
if(num == 0) { 
	setTimeout("history.back()",0); 
} 
} 
</script>
</head>
<body onload="Load();">
	<div id="ShowDiv" style="font-size: 30px; color: red"></div>
</body>
</html>
