<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="bean" scope="page" class="com.pe.books.servlets.EntryBean" />
<jsp:setProperty name="bean" property="*"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="books.css" />
<title>Entry Update</title>
</head>
<body>
<%bean.processRequest();%> 
<jsp:include page="top.jsp"></jsp:include>
<h1 align="center">Entry Update</h1>
<FORM METHOD=POST ACTION="EntryEdit.jsp">
<INPUT TYPE="hidden" NAME=entryId value="<%=bean.getEntryId()%>">
<table align=center>
	<tr>
		<td>Date</td>
		<td> <INPUT TYPE=TEXT NAME=date value="<%=bean.getDate()%>" SIZE=10> (YYYY/MM/DD)</td>
	</tr>
	<tr>
		<td>Dr Code</td>
		<td> <INPUT TYPE=TEXT NAME=drCode value="<%=bean.getDrCode()%>" SIZE=10><%=bean.getDrCodeMessage()%></td>
	</tr>
	<tr>
		<td>Cr Code</td>
		<td> <INPUT TYPE=TEXT NAME=crCode value="<%=bean.getCrCode()%>" SIZE=10><%=bean.getCrCodeMessage()%></td>
	</tr>
	<tr>
		<td>Description</td>
		<td> <INPUT TYPE=TEXT NAME=description value="<%=bean.getDescription()%>" SIZE=50></td>
	</tr>
	<tr>
		<td>Amount</td>
		<td> <INPUT TYPE=TEXT NAME=amount value="<%=bean.getAmount()%>" SIZE=10></td>
	</tr>
	<tr>
		<td>Check No.</td>
		<td> <INPUT TYPE=TEXT NAME=checkNo value="<%=bean.getCheckNo()%>" SIZE=10></td>
	</tr>
	<tr>
		<td colspan="2" align="center"> 
			<br>
			<br>
			<INPUT TYPE=SUBMIT name="button" value="Update">
			<INPUT TYPE=SUBMIT name="button" value="Cancel">
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