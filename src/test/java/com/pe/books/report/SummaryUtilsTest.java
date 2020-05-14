package com.pe.books.report;

import java.sql.Date;

import junit.framework.TestCase;

public class SummaryUtilsTest extends TestCase {

    public void testMonths() {
        SummaryUtils summaryUtils = new SummaryUtils();
        assertEquals(1, SummaryUtils.months(Date.valueOf("2008-01-01"), Date.valueOf("2008-02-01")));
        assertEquals(12, SummaryUtils.months(Date.valueOf("2008-01-01"), Date.valueOf("2009-01-01")));
        assertEquals(1, SummaryUtils.months(Date.valueOf("2008-12-01"), Date.valueOf("2009-01-01")));
        assertEquals(4, SummaryUtils.months(Date.valueOf("2008-10-01"), Date.valueOf("2009-02-01")));
    }
    
    public void xtestNextMonth() {
        System.out.println(SummaryUtils.nextMonth());
        System.out.println(SummaryUtils.lastYear());
    }
    
    public void testFormatedMonth() {
        assertEquals("Jan. 08",SummaryUtils.formatedMonth(Date.valueOf("2008-01-01"),0));
        assertEquals("Mar. 08",SummaryUtils.formatedMonth(Date.valueOf("2008-01-01"),2));
        assertEquals("Mar. 09",SummaryUtils.formatedMonth(Date.valueOf("2008-01-01"),14));
    }
}
