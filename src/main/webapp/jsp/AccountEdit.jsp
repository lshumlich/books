<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="bean" scope="page" class="com.pe.books.servlets.AccountBean" />
<jsp:setProperty name="bean" property="*"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="books.css" />
<title>Account Update</title>
</head>
<body>
<%bean.processRequest();%>
<jsp:include page="top.jsp"></jsp:include>
<h1 align="center">Account Update</h1>
<FORM METHOD=POST ACTION="AccountEdit.jsp">
<INPUT TYPE="hidden" NAME=accountId value="<%=bean.getAccountId()%>">
<table align=center>
	<tr>
		<td>Code</td>
		<td> <INPUT TYPE=TEXT NAME=code value="<%=bean.getCode()%>" SIZE=20></td>
	</tr>
	<tr>
		<td>Display Order</td>
		<td> <INPUT TYPE=TEXT NAME=displayOrder value="<%=bean.getDisplayOrder()%>" SIZE=5></td>
	</tr>
	<tr>
		<td>Type</td>
		<td> <INPUT TYPE=TEXT NAME=type value="<%=bean.getType()%>" SIZE=1> (A/R/E)</td>
	</tr>
	<tr>
		<td>Description</td>
		<td> <INPUT TYPE=TEXT NAME=description value="<%=bean.getDescription()%>" SIZE=50></td>
	</tr>
	<tr>
		<td>Current Balance</td>
		<td> <INPUT TYPE=TEXT NAME=currentBalance value="<%=bean.getCurrentBalance()%>" SIZE=10></td>
	</tr>
	<tr>
		<td>Budget</td>
		<td> <INPUT TYPE=TEXT NAME=budget value="<%=bean.getBudget()%>" SIZE=10></td>
	</tr>
	<tr>
		<td>Active?</td>
		<td> <INPUT TYPE=TEXT NAME=active value="<%=bean.getActive()%>" SIZE=5></td>
	</tr>
	<tr>
		<td colspan="2" align="center"> 
			<br>
			<br>
			<INPUT TYPE=SUBMIT name="button" value="Update">
			<INPUT TYPE=SUBMIT name="button" value="Cancel">
			<INPUT TYPE=SUBMIT name="button" value="Count">
			<INPUT TYPE=SUBMIT name="button" value="Delete">
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center"> 
			<font size=3 color=red>
			<%= bean.getMessage() %>
			</font>
		</td>
	</tr>
</table>
<input name="button" type="hidden" value="submit">
</FORM>
</body>
</html>