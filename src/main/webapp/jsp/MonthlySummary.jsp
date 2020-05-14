<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" import="com.pe.books.report.MonthlySummary"%>
<%@page language="java" import="com.pe.books.Account" %>
<%@page language="java" import="com.pe.books.servlets.AccountTypeSummaryBean" %>
<jsp:useBean id="bean" scope="page" class="com.pe.books.servlets.MonthlySummaryBean" />
<jsp:setProperty name="bean" property="*"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="books.css" />
<title>MonthlySummary</title>
</head>
<body>
<jsp:include page="top.jsp"></jsp:include>
<h1 align="center">Monthly Summary</h1>
<center>
<FORM METHOD=POST ACTION="MonthlySummary.jsp">
Start <INPUT TYPE=TEXT NAME=startDateS value="<%=bean.getStartDate()%>" SIZE=10>
End <INPUT TYPE=TEXT NAME=endDateS value="<%=bean.getEndDate()%>" SIZE=10>
Type to Show <INPUT TYPE=TEXT NAME=types value="<%=bean.getTypes()%>" SIZE=10> (A/R/E)

<INPUT TYPE=SUBMIT name="button" value="Update">
</FORM>
</center>

<table align=center cellpadding=2 cellspacing=2 border=2>
	<tr>
		<th>Code</th>
		<th>Type</th>
		<th>Order</th>
    	<th>Description</th>
    	<th align=right>Balance</th>
		<th align=right>Total</th>
		<th align=right>Average</th>
		<th align=right>Budget</th>
		<%for(int i = 0; i < bean.getMonths(); i++) {%>
		<td align=right nowrap="nowrap"> <%= bean.getFormatedMonth(bean.getStartDate(), i) %> </td>
		<%}%>
    </tr>
<%
	bean.calcSummaryList();
	while(bean.hasNext()) {
	    MonthlySummary summary = bean.next();
	    Account account = summary.getAccount();
%>
	<tr>
		<td><a href="AccountEdit.jsp?accountId=<%=account.getAccountId()%>"><%=account.getCode()%></a>  </td>
		<td align=right> <%= account.getType() %> </td>
		<td align=right> <%= account.getDisplayOrder() %> </td>
		<td nowrap="nowrap"> <%= account.getDescription() %> </td>
		<td align=right nowrap="nowrap"> <%= account.getCurrentBalanceFormatedCurrency() %> </td>
		<td align=right nowrap="nowrap"> <%= summary.getTotalFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= summary.getAverageFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= account.getBudgetFormated0() %> </td>
		<% 
		for(int i = 0; i < summary.getMonths(); i++) {
		%>
		<td align=right nowrap="nowrap"><a href="EntriesByAccount.jsp?accountIdS=<%=account.getAccountId()%>&dateS=<%=bean.getFormatedYYYYMMDD(i) %>"><%=summary.getAmountFormated0(i)%></a>  </td>		
		<%
		}
		%>
	</tr>
<%
	} // end While
%>
<%
	AccountTypeSummaryBean typeSummary = bean.getAssetSummary();
%>
	<tr bordercolor="green">
		<td colspan="4" align="center"> <b> Total Asset <%= bean.getMonths() %> months </b>  </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalBalanceFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalTotalFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getAverageFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalBudgetFormated0() %> </td>
		<%for(int i = 0; i < bean.getMonths(); i++) {%>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalAmountFormated0(i) %> </td>
		<%}%>
	</tr>
<%
	typeSummary = bean.getRevenueSummary();
%>
	<tr bordercolor="green">
		<td colspan="4" align="center"> <b> Total Revenue <%= bean.getMonths() %> months </b>  </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalBalanceFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalTotalFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getAverageFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalBudgetFormated0() %> </td>
		<%for(int i = 0; i < bean.getMonths(); i++) {%>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalAmountFormated0(i) %> </td>
		<%}%>
	</tr>
<%
	typeSummary = bean.getExpenseSummary();
%>
	<tr bordercolor="green">
		<td colspan="4" align="center"> <b> Total Expense <%= bean.getMonths() %> months </b>  </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalBalanceFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalTotalFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getAverageFormated0() %> </td>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalBudgetFormated0() %> </td>
		<%for(int i = 0; i < bean.getMonths(); i++) {%>
		<td align=right nowrap="nowrap"> <%= typeSummary.getTotalAmountFormated0(i) %> </td>
		<%}%>
	</tr>
	<tr>
		<td colspan="9" align="center">  <a href="../MonthlySummaryExcel?startDateS=<%=bean.getStartDate()%>&endDateS=<%=bean.getEndDate()%>&types=<%=bean.getTypes()%>">Excel</a> </td>
		<td colspan="9" align="center">  <a href="../YearEndExcel?startDateS=<%=bean.getStartDate()%>&endDateS=<%=bean.getEndDate()%>&types=<%=bean.getTypes()%>">Year End Excel</a> </td>
	</tr>
	</table>
	
</body>
</html>