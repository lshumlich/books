<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" import="com.pe.books.report.EntryByAccount"%>
<%@page language="java" import="com.pe.books.Account" %>
<%@page language="java" import="java.util.Iterator" %>
<jsp:useBean id="clear" scope="page" class="com.pe.books.servlets.EntryClearBean" />
<jsp:setProperty name="clear" property="*"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="books.css" />
<title>Clear Entries</title>
</head>
<body>
<%clear.processRequest();%>
<jsp:include page="top.jsp"></jsp:include>
<h1 align="center">Clear Entries</h1>
<%
 	Account account = clear.getAccount(); 
%>
<center> <b>
<%= account.getCode() %> -
<%= account.getDescription() %>
Balance: <%= account.getCurrentBalanceFormatedCurrency() %>
<br>
Uncleared: <%= clear.getUnClearedAmountCurrency() %>
Cleared: <%= clear.getClearedAmountCurrency() %>
<br>
Opening Balance: <%= clear.getOpeningBalanceCurrency() %>
Closing Balance: <%= clear.getClosingBalanceCurrency() %>
<br>
</b> </center>
<table align=center cellpadding=2 cellspacing=2 border=2>
	<tr>
		<th>Date</th>
		<th>Offset</th>
    	<th>Description</th>
		<th>Dr Amount</th>
		<th>Cr Amount</th>
		<th>Check No.</th>
    </tr> 
<%
	Iterator<EntryByAccount> it = clear.getDisplayList().iterator();
	while(it.hasNext()) {
	    EntryByAccount entry = it.next();
%>
	<tr>
		<td nowrap="nowrap"><a href="EntryEdit.jsp?entryId=<%=entry.getEntryId()%>"><%=entry.getDate()%></a> </td>		
		<td nowrap="nowrap"><%=entry.getOffsetCode()%></td>		
		<td nowrap="nowrap"> <%= entry.getDescription() %> </td>
		<td align=right> <%= entry.getDrAmountFormatedCurrency() %> </td>
		<td align=right> <%= entry.getCrAmountFormatedCurrency() %> </td>
		<td align=right> <%= entry.getCheckNo() %> </td>
		<%if(entry.isCleared()) { %>
		<td nowrap="nowrap"><a href="EntryClear.jsp?accountId=<%=account.getAccountId()%>&unClearEntryId=<%=entry.getEntryId()%>">UnClear</a></td>
		<% } else { %>
		<td nowrap="nowrap"><a href="EntryClear.jsp?accountId=<%=account.getAccountId()%>&clearEntryId=<%=entry.getEntryId()%>">Clear</a></td>
		<% } %>		
	</tr>
<%
	} // end While
%>
	
	<tr>
	<td colspan="6" align="center" valign="middle">
	<FORM METHOD=POST ACTION="EntryClear.jsp">
	<INPUT TYPE="hidden" NAME=accountId value="<%=account.getAccountId()%>">
	<INPUT TYPE=SUBMIT name="button" value="Update">
	<INPUT TYPE=SUBMIT name="button" value="Restart">
	<input name="button" type="hidden" value="submit">
	</FORM>
	</td></tr>
	</table>
</body>
</html>