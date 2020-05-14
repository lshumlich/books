<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="books.css" />
<title>Upload Excel</title>
</head>
<body>
<jsp:include page="top.jsp"></jsp:include>
<h1 align="center">Upload Excel</h1>
<table align="center">
	<tr> <td>
		<FORM ENCTYPE='multipart/form-data' method='POST' action='../UploadExcel'>
		<INPUT TYPE='file' NAME='mptest'>
		<INPUT TYPE='submit' VALUE='upload'>
		</FORM>
	</td> </tr> 
	<tr> <td align="center">
		<br>
		<a href="../doc/SampleExcelLoad.xls">Sample Spreadsheet</a>
	</td> </tr>
</table> 
</body>
</html>