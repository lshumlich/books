package com.pe.books.servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelDownLoadUtil {
    public void downLoad(HttpServletResponse response, ServletContext servletContext, String fileName) throws ServletException, IOException {
 
    }
    
    public void downLoad(HttpServletResponse response, ServletContext servletContext, String fileName, HSSFWorkbook workbook) throws ServletException, IOException {
        ServletOutputStream op = response.getOutputStream();
        String mimetype = servletContext.getMimeType(fileName);
        //
        // Set the response and go!
        //
        String contentType = (mimetype != null) ? mimetype : "application/octet-stream";
        response.setContentType(contentType);
//        File file = new File(fileName);
//        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        
	    workbook.write(op);
	    op.close();


        //
        // Stream to the requester.
        //
//        int BUFSIZE = 1000;
//        byte[] bbuf = new byte[BUFSIZE];
//        DataInputStream in = new DataInputStream(new FileInputStream(file));
//
//        int length = 0;
//        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
//            op.write(bbuf, 0, length);
//        }
//
//        in.close();
//        op.flush();
    }

}
