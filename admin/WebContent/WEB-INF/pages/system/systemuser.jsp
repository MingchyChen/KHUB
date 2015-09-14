<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增系統用戶</title>
</head>
<body>
	<div class="clear"></div>
	<p>${status}</p>
	<div style="width: 400px;"><select>
		<option>
	</select>
		<form:form modelAttribute="systemUser" action="sysUser/createUser"
			id="systemUser" method="post">

			<table>
				<tr>
					<th align="left">帳號：</th>
					<td><form:input path="account" id="account" /></td>
				</tr>
				<tr>
					<th align="left">用戶名：</th>
					<td><form:input path="username" id="username" /></td>

				</tr>
				<tr>
					<th align="left">用戶密碼：</th>
					<td><form:input path="password" id="password" /></td>
				</tr>
				<tr>
					<th align="left">真實姓名：</th>
					<td><form:input path="realName" id="realName" /></td>
				</tr>
			</table>
			<div>
				<br> <input type="submit" value="儲存">
			</div>
		</form:form>
	</div>
</body>
</html>
