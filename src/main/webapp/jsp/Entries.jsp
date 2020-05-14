<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" import="com.pe.books.Entry" %>
<%@page language="java" import="java.util.Iterator" %>

<jsp:useBean id="bean" scope="page" class="com.pe.books.servlets.EntriesBean" />
<jsp:setProperty name="bean" property="*"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="books.css" />
<title>Entries for Month</title>
</head>
<body>

<jsp:include page="top.jsp"></jsp:include>

<br>
	<%
	    Iterator<Entry> it = bean.getEntries().iterator();
	%>
<table align=center cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle"> <a href="Entries.jsp?date=<%=bean.getPrevMonth()%>"> <img src="../images/left.gif" alt="Previous Month"></a> <td>
		<td valign="middle">
		<h1 align="center">Entries for Month</h1>
		</td>
		<td valign="middle"> <a href="Entries.jsp?date=<%=bean.getNextMonth()%>"> <img src="../images/right.gif" alt="Next Month"></a> <td>
	</tr>
	</table>

<table align=center cellpadding=2 cellspacing=2 border=5>
	<tr>
		<th>Date</th>
		<th>Dr Account</th>
		<th>Cr Account</th>
    	<th>Description</th>
    	<th align=right>Amount</th>
		<th align=right>CheckNo</th>
    </tr>
	<%
	    while (it.hasNext()) {
	        Entry entry = it.next();
	%>
	<tr>
		<td nowrap="nowrap"><a href="EntryEdit.jsp?entryId=<%=entry.getEntryId()%>"><%=entry.getDate()%></a> </td>
 		<td> <a href="AccountEdit.jsp?accountId=<%=entry.getDrAccount().getAccountId()%>"><%=entry.getDrAccount().getCode()%> </a>  </td>
		<td> <a href="AccountEdit.jsp?accountId=<%=entry.getCrAccount().getAccountId()%>"><%=entry.getCrAccount().getCode()%> </a>  </td>
 		<td> <%=entry.getDescription()%> </td>
		<td align=right> <%=entry.getAmountFormatedCurrency()%> </td>
		<td align=right> <%=entry.getCheckNo()%> </td>
	</tr>
<%
    } // end While
%>
	<tr>
		<td colspan="6" align="center"> 
			<FORM METHOD=POST">	<input type=text name=date size=8 value=<%=bean.getDate()%>> <INPUT TYPE=SUBMIT name="button" value="enter"> </FORM>
		</td>
	</tr>
</table>
</body>
</html>