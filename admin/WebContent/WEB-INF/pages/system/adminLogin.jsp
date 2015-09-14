<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>農委會-登錄入口</title>
<link rel="stylesheet" href="css/system/default.css" type="text/css" />
<link rel="stylesheet" href="../css/system/default.css" type="text/css" />
<script>
	function doLogin() {
		var uname = document.getElementById("username").value;
		var upass = document.getElementById("password").value;
		if (uname == "") {
			alert("帳號不能為空");
			return false;
		} else if (upass == "") {
			alert("密碼不能為空");
			return false;
		} else {
			var url = "Admin/doLogin";
			document.myform.action = url;
			document.myform.submit();
		}
	}
</script>
</head>
<body>

	<div id="header">
		<div id="header1">
			<!-- <img alt="" src="images/portal/logo.png" height="80" width="80"> -->
		</div>
		<div id="header2">
			<h1>農委會</h1>
		</div>
	</div>

	<div id="login">
		<h1>管理者登入</h1>
		<br />
		<form:form name="myform" action="" method="post"
			modelAttribute="loginForm">
			<table width="260" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="3" valign="middle"><span style="color: red;">${loginForm.message}</span></td>
				</tr>
				<tr>
					<td height="40" valign="middle">帳號：</td>
					<td colspan="2"><form:input path="username" /></td>
				</tr>
				<tr>
					<td height="40" valign="middle">密碼：</td>
					<td colspan="2"><form:password path="password" /></td>
				</tr>
				<tr>
					<td height="40" colspan="3" align="center"><input
						type="button" class="formbutton" value="登入" onClick="doLogin()" /></td>
				</tr>
			</table>
		</form:form>
	</div>


</body>
</html>