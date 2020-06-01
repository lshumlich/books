package com.pe.books.servlets;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pe.books.Account;
import com.pe.books.BooksManager;
import com.pe.books.HibernateUtil;
import com.pe.books.report.EntryByAccount;
import com.pe.books.report.SummaryProcessor;

public class EntriesByAccountBean {

    private Date date;
    private Date endDate;

    private Long accountId;

    public List<EntryByAccount> getEntryDisplay() {
        SummaryProcessor summary = new SummaryProcessor();
        return summary.entrysForMonth(this.date, this.accountId);
    }

    public List<EntryByAccount> getEntryRangeDisplay() {
        SummaryProcessor summary = new SummaryProcessor();
        return summary.entrysForMonthRange(this.date, this.endDate, this.accountId);
    }
    
    public Account getAccount() {

//    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	
    	Transaction transaction = session.beginTransaction();

        Account account = BooksManager.getInstance().getAccountById(this.accountId);
        
        transaction.commit();
        
        return account;
    }

    public void setDateS(String s) {
        this.date = Date.valueOf(s);
    }
    
    public void setStartDate(Date startDate){
    	this.date = startDate;
    }
    
    public void setEndDate(Date endDate) {
    	this.endDate = endDate;
    }
    
    public void setAccountIdS(String s) {
        this.accountId = Long.parseLong(s);
    }
    
    public void setAccountIdS(Long l) {
        this.accountId = l;
    }
    
    public String getDate() {
        if (this.date == null) return "date is null";
        return this.date.toString();
    }
    
    public String getAccountId() {
        if (this.accountId == null) return "accountId is null";
        return this.accountId.toString();
    }
}
