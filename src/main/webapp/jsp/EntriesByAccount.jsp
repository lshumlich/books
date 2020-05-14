<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" import="com.pe.books.report.EntryByAccount"%>
<%@page language="java" import="com.pe.books.Account" %>
<%@page language="java" import="java.util.Iterator" %>
<jsp:useBean id="bean" scope="page" class="com.pe.books.servlets.EntriesByAccountBean" />
<jsp:setProperty name="bean" property="*"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="books.css" />
<title>Entries By Account</title>
</head>
<body>
<jsp:include page="top.jsp"></jsp:include>
<h1 align="center">Entries By Account</h1>
<%
 	Account account = bean.getAccount(); 
%>
<center> <b>
<%= account.getCode() %> -
<%= account.getDescription() %>
<%
	if(account.getType().contentEquals("A")) {
	    out.print("Balance: " + account.getCurrentBalanceFormatedCurrency());
	} else if(account.getType().contentEquals("E")) {
	    out.print("Budget: " + account.getBudgetFormatedCurrency());
	}
%>
</b> </center>
<table align=center cellpadding=2 cellspacing=2 border=2>
	<tr>
		<th>Date</th>
		<th>Offset</th>
    	<th>Description</th>
		<th>Dr Amount</th>
		<th>Cr Amount</th>
    </tr>
<%
	Iterator<EntryByAccount> it = bean.getEntryDisplay().iterator();
	while(it.hasNext()) {
	    EntryByAccount entry = it.next();
%>
	<tr>
		<td nowrap="nowrap"><a href="EntryEdit.jsp?entryId=<%=entry.getEntryId()%>"><%=entry.getDate()%></a> </td>		
		<td nowrap="nowrap"><a href="EntriesByAccount.jsp?accountIdS=<%=entry.getOffsetAccountId()%>&dateS=<%=bean.getDate() %>"><%=entry.getOffsetCode()%></a></td>		
		<td nowrap="nowrap"> <%= entry.getDescription() %> </td>
		<td align=right> <%= entry.getDrAmountFormatedCurrency() %> </td>
		<td align=right> <%= entry.getCrAmountFormatedCurrency() %> </td>
	</tr>
<%
	} // end While
%>
	</table>
</body>
</html>