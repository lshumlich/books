package com.pe.books.servlets;

import junit.framework.TestCase;

import com.pe.books.EditException;

public class YearEndExcelTest extends TestCase {

    public void testStuff() throws EditException {
        System.out.println("" + new java.sql.Date(System.currentTimeMillis()));
        System.out.println("" + new java.util.Date(System.currentTimeMillis()));
        System.out.println("" + new java.util.Date());
        System.out.println(new YearEndExcel().createExcelWorkbook("2012-01-01", "2012-08-01", "ARE"));
    }

}
