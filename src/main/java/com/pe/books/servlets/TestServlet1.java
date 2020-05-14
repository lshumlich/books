package com.pe.books.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Sample of how to down load an excel file.
 * 
 * @author lshumlich
 *
 */

public class TestServlet1 extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    static final long serialVersionUID = 1L;

    public TestServlet1() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //String fileName = this.createExcelWorkbook();
        //ExcelDownLoadUtil excelDownLoad = new ExcelDownLoadUtil();
        //excelDownLoad.downLoad(response, getServletConfig().getServletContext(), fileName);
        PrintWriter out = response.getWriter();
        out.println("Attributes<br>");
        Enumeration<String> e = request.getAttributeNames();
        while(e.hasMoreElements()) {
            out.println("<br>" +e.nextElement());
        }
        out.println("getParameterNames<br>");
        Enumeration<String> e2 = request.getParameterNames();
        while(e2.hasMoreElements()) {
            
            out.println("<br>" +e2.nextElement());
        }
        out.println("<br>Something is written...");
        out.println("<br>" + request.getParameter("startDateS"));
        out.println("<br>" + request.getParameter("endDateS"));
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        PrintWriter out = response.getWriter();
        out.print("<h1 align=\"center\">TestServlet1 - doPost</h1>");
        out.flush();
    }

    private String createExcelWorkbook() {
        String fileName = "BooksData.xls";
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("new sheet");

            // Create a row and put some cells in it. Rows are 0 based.
            HSSFRow row = sheet.createRow(0);
            // Create a cell and put a value in it.
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(1);

            // Or do it on one line.
            row.createCell(1).setCellValue(1.2);
            row.createCell(2).setCellValue(new HSSFRichTextString("This is a string"));
            row.createCell(3).setCellValue(true);
            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
            cell = row.createCell(4);
            cell.setCellValue(new Date(System.currentTimeMillis()));
            cell.setCellStyle(cellStyle);

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            System.out.println();
        }

        return fileName;
    }
}