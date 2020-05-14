package com.pe.books.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pe.books.LoadExcel;

 public class UploadExcel extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
	public UploadExcel() {
		super();
	}   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    PrintWriter out = response.getWriter();
	    out.print("This servlet must be posted through UploadExcel.jsp.");
	    out.flush();
	    
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        PrintWriter out = response.getWriter();
        out.print("<h1 align=\"center\">Update Excel Results</h1>");
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = iter.next();
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    if(fileName.toLowerCase().endsWith(".xls")) {
                        String fieldName = item.getFieldName();
                        out.println("<pre>");
                        File file = new File(new File(fileName).getName()); // Just the file name part
                        item.write(file);
                        LoadExcel loadExcel = new LoadExcel();
                        loadExcel.process(file, out);
                        out.println("</pre>");
                        out.println("<br>Temp file written to:" + file.getAbsolutePath());
                    } else {
                        out.println("<h1 align=center>" + fileName + " must be a '.xls' file to be processed.</h1>");
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace(out);
        }
        out.flush();
	}   	  	    
}