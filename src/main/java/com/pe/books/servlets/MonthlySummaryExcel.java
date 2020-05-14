package com.pe.books.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.pe.books.Account;
import com.pe.books.report.MonthlySummary;

/**
 * Sample of how to down load an excel file.
 * MonthlySummaryExcel?startDateS=2008-04-01&endDateS=2009-04-01
 * 
 * @author lshumlich
 * 
 */

public class MonthlySummaryExcel extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    static final long serialVersionUID = 1L;

    public MonthlySummaryExcel() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HSSFWorkbook workbook = this
                .createExcelWorkbook(request.getParameter("startDateS"), request.getParameter("endDateS"), request.getParameter("types"));
        ExcelDownLoadUtil excelDownLoad = new ExcelDownLoadUtil();
        excelDownLoad.downLoad(response, getServletConfig().getServletContext(), "BooksData.xls", workbook);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        PrintWriter out = response.getWriter();
        out.print("<h1 align=\"center\">TestServlet1 - doPost</h1>");
        out.flush();
    }

    private HSSFWorkbook createExcelWorkbook(String startDateS, String endDateS, String types) {
        MonthlySummaryBean bean = new MonthlySummaryBean();
        bean.setStartDateS(startDateS);
        bean.setEndDateS(endDateS);
        bean.setTypes(types);
        HSSFWorkbook workbook = new HSSFWorkbook();

//        String fileName = "BooksData.xls";
        try {

            HSSFSheet sheet = workbook.createSheet("Summary");
            HSSFCellStyle bold = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            bold.setFont(font);
            HSSFCellStyle boldDate = workbook.createCellStyle();
            boldDate.setFont(font);
            boldDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("mmm yy"));

            HSSFCell cell;

            HSSFRow row = sheet.createRow(0);

            row.createCell(0).setCellValue(new HSSFRichTextString("Code"));
            row.createCell(1).setCellValue(new HSSFRichTextString("Type"));
            row.createCell(2).setCellValue(new HSSFRichTextString("Order"));
            row.createCell(3).setCellValue(new HSSFRichTextString("Description"));
            row.createCell(4).setCellValue(new HSSFRichTextString("Balance"));
            row.createCell(5).setCellValue(new HSSFRichTextString("Total"));
            row.createCell(6).setCellValue(new HSSFRichTextString("Average"));
            row.createCell(7).setCellValue(new HSSFRichTextString("Budget"));
            row.getCell(0).setCellStyle(bold);
            row.getCell(1).setCellStyle(bold);
            row.getCell(2).setCellStyle(bold);
            row.getCell(3).setCellStyle(bold);
            row.getCell(4).setCellStyle(bold);
            row.getCell(5).setCellStyle(bold);
            row.getCell(6).setCellStyle(bold);
            row.getCell(7).setCellStyle(bold);
            for (int i = 0; i < bean.getMonths(); i++) {
                row.createCell(8 + i).setCellValue(
                        new HSSFRichTextString(bean.getFormatedMonth(bean.getStartDate(), i)));
                row.getCell(8 + i).setCellStyle(boldDate);
            }

            int rowNum = 1;
            bean.calcSummaryList();
            while (bean.hasNext()) {
                row = sheet.createRow(rowNum++);
                MonthlySummary summary = bean.next();
                Account account = summary.getAccount();
                row.createCell(0).setCellValue(new HSSFRichTextString(account.getCode()));
                row.createCell(1).setCellValue(new HSSFRichTextString(account.getType()));
                row.createCell(2).setCellValue(account.getDisplayOrder());
                row.createCell(3).setCellValue(new HSSFRichTextString(account.getDescription()));
                row.createCell(4).setCellValue(account.getCurrentBalance().intValue());
                row.createCell(5).setCellValue(summary.getTotal().intValue());
                row.createCell(6).setCellValue(summary.getAverage().intValue());
                row.createCell(7).setCellValue(account.getBudget().intValue());
                for (int i = 0; i < summary.getMonths(); i++) {
                    if (summary.getAmount(i) != null) {
                        row.createCell(8 + i).setCellValue(summary.getAmount(i).intValue());
                    }
                }
            }
            // cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy
            // h:mm"));

            // Write the output to a file
//            FileOutputStream fileOut = new FileOutputStream(fileName);
//            workbook.write(fileOut);
//            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return fileName;
        return workbook;
    }
}