package com.pe.books.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.pe.books.Account;
import com.pe.books.report.EntryByAccount;
import com.pe.books.report.MonthlySummary;

/**
 * Sample of how to down load an excel file.
 * YearEndExcel?startDateS=2008-04-01&endDateS=2009-04-01
 * 
 * @author lshumlich
 * 
 */

public class YearEndExcel extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    static final long serialVersionUID = 1L;

    public YearEndExcel() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HSSFWorkbook workbook = this
                .createExcelWorkbook(request.getParameter("startDateS"), request.getParameter("endDateS"), request.getParameter("types"));
        ExcelDownLoadUtil excelDownLoad = new ExcelDownLoadUtil();
        excelDownLoad.downLoad(response, getServletConfig().getServletContext(), "YearEndData.xls", workbook);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        PrintWriter out = response.getWriter();
        out.print("<h1 align=\"center\">TestServlet1 - doPost</h1>");
        out.flush();
    }

    protected HSSFWorkbook createExcelWorkbook(String startDateS, String endDateS, String types) {
        MonthlySummaryBean bean = new MonthlySummaryBean();
        bean.setStartDateS(startDateS);
        bean.setEndDateS(endDateS);
        bean.setTypes(types);
        HSSFWorkbook workbook = new HSSFWorkbook();

//        String fileName = "YearEndData.xls";
        try {

            HSSFSheet sheet = workbook.createSheet("Summary");
            HSSFCellStyle bold = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            bold.setFont(font);
            HSSFCellStyle boldDate = workbook.createCellStyle();
            boldDate.setFont(font);
            boldDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("mmm yy"));
            HSSFCellStyle dollars = workbook.createCellStyle();
            dollars.setDataFormat(workbook.createDataFormat().getFormat("$##,##0.00"));

//            HSSFCell cell;

            HSSFRow row = sheet.createRow(0);
            row.createCell(3).setCellValue(new HSSFRichTextString("Lean Minds Books"));
            row.createCell(4).setCellValue(new HSSFRichTextString(startDateS));
            row.createCell(5).setCellValue(new HSSFRichTextString(endDateS));

            row = sheet.createRow(2);
            row.createCell(0).setCellValue(new HSSFRichTextString("Code"));
            row.createCell(1).setCellValue(new HSSFRichTextString("Type"));
            row.createCell(2).setCellValue(new HSSFRichTextString("Order"));
            row.createCell(3).setCellValue(new HSSFRichTextString("Description"));
            row.createCell(4).setCellValue(new HSSFRichTextString("Balance"));
            row.createCell(5).setCellValue(new HSSFRichTextString("Total"));
            row.getCell(0).setCellStyle(bold);
            row.getCell(1).setCellStyle(bold);
            row.getCell(2).setCellStyle(bold);
            row.getCell(3).setCellStyle(bold);
            row.getCell(4).setCellStyle(bold);
            row.getCell(5).setCellStyle(bold);
            sheet.setColumnWidth(3, 30 * 256);
            sheet.setColumnWidth(4, 12 * 256);
            sheet.setColumnWidth(5, 12 * 256);

            int rowNum = 3;
            bean.calcSummaryList();
            while (bean.hasNext()) {
                MonthlySummary summary = bean.next();
                Account account = summary.getAccount();
            	this.createEntriesByAccountWorksheet(workbook,bean.getStartDate(),bean.getEndDate(),account);
                row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(new HSSFRichTextString(account.getCode()));
                row.createCell(1).setCellValue(new HSSFRichTextString(account.getType()));
                row.createCell(2).setCellValue(account.getDisplayOrder());
                row.createCell(3).setCellValue(new HSSFRichTextString(account.getDescription()));
                row.createCell(4).setCellValue(account.getCurrentBalance().doubleValue());
                row.createCell(5).setCellValue(summary.getTotal().doubleValue());
                row.getCell(4).setCellStyle(dollars);
                row.getCell(5).setCellStyle(dollars);
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

	private void createEntriesByAccountWorksheet(HSSFWorkbook workbook,
			Date startDate, Date endDate, Account account) {
        try {
//        	System.out.println(account.getCode());
//        	System.out.println(account.getDescription());
            EntriesByAccountBean bean = new EntriesByAccountBean();
            bean.setStartDate(startDate);
            bean.setEndDate(endDate);
            bean.setAccountIdS(account.getAccountId());

            HSSFSheet sheet = workbook.createSheet(account.getCode());
            HSSFCellStyle bold = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            bold.setFont(font);
            HSSFCellStyle boldDate = workbook.createCellStyle();
            HSSFCellStyle dollars = workbook.createCellStyle();
            dollars.setDataFormat(workbook.createDataFormat().getFormat("$##,##0.00"));
            boldDate.setFont(font);
            boldDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("mmm yy"));

//            HSSFCell cell;

            HSSFRow row = sheet.createRow(0);
            row.createCell(1).setCellValue(new HSSFRichTextString(account.getCode()));
            row.createCell(2).setCellValue(new HSSFRichTextString(account.getDescription()));
            row.getCell(1).setCellStyle(bold);
            row.getCell(2).setCellStyle(bold);

            row = sheet.createRow(2);
            row.createCell(0).setCellValue(new HSSFRichTextString("Date"));
            row.createCell(1).setCellValue(new HSSFRichTextString("Offset"));
            row.createCell(2).setCellValue(new HSSFRichTextString("Description"));
            row.createCell(3).setCellValue(new HSSFRichTextString("Dr Amount"));
            row.createCell(4).setCellValue(new HSSFRichTextString("Cr Amount"));
            row.getCell(0).setCellStyle(bold);
            row.getCell(1).setCellStyle(bold);
            row.getCell(2).setCellStyle(bold);
            row.getCell(3).setCellStyle(bold);
            row.getCell(4).setCellStyle(bold);
            sheet.setColumnWidth(0, 10 * 256);
            sheet.setColumnWidth(2, 40 * 256);
            sheet.setColumnWidth(3, 12 * 256);
            sheet.setColumnWidth(4, 12 * 256);

            int rowNum = 3;
        	Iterator<EntryByAccount> it = bean.getEntryRangeDisplay().iterator();
        	while(it.hasNext()) {
        	    EntryByAccount entry = it.next();
                row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(new HSSFRichTextString(entry.getDate().toString()));
                row.createCell(1).setCellValue(new HSSFRichTextString(entry.getOffsetCode()));
                row.createCell(2).setCellValue(new HSSFRichTextString(entry.getDescription()));
                if(entry.getDrAmount() != null) {
                	row.createCell(3).setCellValue(entry.getDrAmount().doubleValue());
                    row.getCell(3).setCellStyle(dollars);
                }
                if(entry.getCrAmount() != null) {
                	row.createCell(4).setCellValue(entry.getCrAmount().doubleValue());
                    row.getCell(4).setCellStyle(dollars);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
}