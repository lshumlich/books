package com.pe.books.servlets;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.pe.books.Entry;
import com.pe.books.report.SummaryProcessor;

public class EntriesBean {

    private List<Entry> entries;

    private String date;

    public List<Entry> getEntries() {
        Date searchDate = null;
        if (date == null) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            searchDate = new Date(calendar.getTimeInMillis());
            this.date = searchDate.toString();
        } else {
            searchDate = Date.valueOf(this.date);
        }
        SummaryProcessor summaryProcessor = new SummaryProcessor();
        this.entries = summaryProcessor.entrysForMonth(searchDate);
        return this.entries;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
    
    public String getNextMonth() {
        Date d = Date.valueOf(this.date);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(d.getTime());
        calendar.add(Calendar.MONTH,1);
        d.setTime(calendar.getTimeInMillis());
        return d.toString();
    }
    
    public String getPrevMonth() {
        Date d = Date.valueOf(this.date);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(d.getTime());
        calendar.add(Calendar.MONTH,-1);
        d.setTime(calendar.getTimeInMillis());
        return d.toString();
    }

}