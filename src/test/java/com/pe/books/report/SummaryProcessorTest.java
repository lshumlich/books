package com.pe.books.report;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import com.pe.books.Account;
import com.pe.books.Entry;

import junit.framework.TestCase;

public class SummaryProcessorTest extends TestCase {
	
    public void testSummary() {
    	SummaryProcessor processor = new SummaryProcessor();
        List<MonthlySummary> list = processor.summary(Date.valueOf("2008-01-01"), Date.valueOf("2009-01-01"));
//        this.dumpMonthlySummary(list);
    }

    public void testEntrysForMonthForAccount() {
    	SummaryProcessor processor = new SummaryProcessor();
        List<EntryByAccount> list = processor.entrysForMonth(Date.valueOf("2008-01-01"), new Long(1));
//        this.dumpEntryDisplay(list);
    }

    public void testEntrysForMonth() {
    	SummaryProcessor processor = new SummaryProcessor();
        List<Entry> list = processor.entrysForMonth(Date.valueOf("2008-01-01"));
//        this.dumpEntryDisplay(list);
    }


    private void dumpEntryDisplay(List<EntryByAccount> list) {
        Iterator<EntryByAccount> all = list.iterator();
        while (all.hasNext()) {
            System.out.println(all.next());
        }
    }

    private void dumpMonthlySummary(List<MonthlySummary> accountSummaryList) {
        Iterator<MonthlySummary> all = accountSummaryList.iterator();
        while (all.hasNext()) {
            System.out.println(all.next());
        }
    }

}
