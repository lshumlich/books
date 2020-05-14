<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="com.pe.books.Account" %>
<jsp:useBean id="bean" scope="page" class="com.pe.books.servlets.AccountsBean" />
<jsp:setProperty name="bean" property="*"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="books.css" />
<title>Account List</title>
</head>
<body>

<jsp:include page="top.jsp"></jsp:include>

<br>
<h1 align="center">Account List</h1>

<table align=center cellpadding=2 cellspacing=2 border=5>
	<tr>
		<th>Order</th>
		<th>Code</th>
		<th>Type</th>
    	<th>Description</th>
    	<th align=right>Balance</th>
		<th align=right>Budget</th>
		<th>Active</th>
    	<th>Clear</th>
    </tr>
<%
	bean.getAccountsInOrder();
	while(bean.hasNext()) {
	    Account account = bean.next();
%>
	<tr>
		<td align=right> <%= account.getDisplayOrder() %> </td>
		<td><a href="AccountEdit.jsp?accountId=<%=account.getAccountId()%>"><%=account.getCode()%></a>  </td>
		<td> <%= account.getType() %> </td>
		<td> <%= account.getDescription() %> </td>
		<td align=right> <%= account.getCurrentBalanceFormatedCurrency() %> </td>
		<td align=right> <%= account.getBudgetFormatedCurrency() %> </td>
		<td> <%= account.getActive() %> </td>
		<td><a href="EntryClear.jsp?accountId=<%=account.getAccountId()%>">Reconcile</a>  </td>
	</tr>
<%
	} // end While
%>
	<tr>
		<td colspan="8" align="center" valign="middle">
			<FORM METHOD=POST">
			<INPUT TYPE=SUBMIT name="button" value="Show Inactive">
			</FORM>
		</td>
	</tr>
	</table>
</body>
</html>