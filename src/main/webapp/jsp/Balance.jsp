<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="bean" scope="page" class="com.pe.books.servlets.BalanceBean" />
<jsp:setProperty name="bean" property="*"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="books.css" />
<title>Balance</title>
</head>
<body>
<%bean.processRequest();%> 
<jsp:include page="top.jsp"></jsp:include>
<h1 align="center">Balance</h1>
<FORM METHOD=POST>
<table align=center>
	<tr>
		<td>Code</td>
		<td> <INPUT TYPE=TEXT NAME=code value="<%=bean.getCode()%>" SIZE=10></td>
		<td colspan="2" align="center"> 
			<INPUT TYPE=SUBMIT name="button" value="Get">
		</td>
	</tr>
	<tr>
		<td>Balance</td>
		<td> <%=bean.getCurrentBalance()%> </td>
	</tr>
</table>
<input name="button" type="hidden" value="submit">
</FORM>
</body>
</html>