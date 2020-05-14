package com.pe.books.report;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pe.books.Account;
import com.pe.books.AccountComparator;
import com.pe.books.Entry;
import com.pe.books.HibernateUtil;

public class SummaryProcessor extends TestCase {

    private int totalMonths = 0;

//    private SummaryUtils summaryUtils = new SummaryUtils();

    private HashMap<String, MonthlySummary> summaryTable = new HashMap<String, MonthlySummary>();

//    public void xtestSummary() {
//        List<MonthlySummary> list = this.summary(Date.valueOf("2008-01-01"), Date.valueOf("2009-01-01"));
//        this.dumpMonthlySummary(list);
//    }
//
//    public void testEntryDisplay() {
//        List<EntryByAccount> list = this.entrysForMonth(Date.valueOf("2008-01-01"), new Long(1));
//        this.dumpEntryDisplay(list);
//    }

    public List<MonthlySummary> summary(Date startDate, Date endDate) {
        this.totalMonths = SummaryUtils.months(startDate, endDate);
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = session.beginTransaction();
        transaction.setTimeout(5);
        ArrayList<MonthlySummary> accountSummaryList;
        long startMills = System.currentTimeMillis();
        try {
//          Account account = (Account) session.createQuery("from Account where code = :code").setParameter("code", s).uniqueResult();
//            List<Entry> list = session.createQuery("from Entry where date >= ? and date < ?").setDate(0, startDate)
//                    .setDate(1, endDate).list();
            List<Entry> list = session.createQuery("from Entry where date >= :startDate and date < :endDate")
            		.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate).list();
            Entry entry;
            int month;
            Iterator<Entry> it = list.iterator();
            while (it.hasNext()) {
                entry = it.next();
                month = SummaryUtils.months(startDate, entry.getDate());
                this.addSummary(month, entry.getDrAccount(), entry.getAmount());
                this.addSummary(month, entry.getCrAccount(), entry.getAmount().negate());
            }
            transaction.commit();
            accountSummaryList = new ArrayList<MonthlySummary>(this.summaryTable.values());
            Collections.sort(accountSummaryList, new AccountComparator());
        } catch (Exception e) {
            accountSummaryList = new ArrayList<MonthlySummary>();
            e.printStackTrace();
        } finally {
//            System.out.println("--- Summary Query took: " + (System.currentTimeMillis() - startMills) + " mills. " + new java.util.Date());
        }

        return accountSummaryList;
    }

    public List<EntryByAccount> entrysForMonth(Date startDate, Long accountId) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(startDate.getTime());
        calendar.add(Calendar.MONTH, 1);
        Date endDate = new Date(calendar.getTimeInMillis());
        return entrysForMonthRange(startDate, endDate, accountId);
    }

    public List<EntryByAccount> entrysForMonthRange(Date startDate, Date endDate, Long accountId) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        List<Entry> list = session.createQuery(
//                "from Entry where date >= ? and date < ? and (drAccountId = ? or crAccountId = ?)").setDate(0,
//                startDate).setDate(1, endDate).setLong(2, accountId).setLong(3, accountId).list();
        List<Entry> list = session.createQuery(
                "from Entry where date >= :startDate and date < :endDate and (drAccountId = :accountId or crAccountId = :accountId)")
        		.setParameter("startDate",startDate)
        		.setParameter("endDate", endDate)
        		.setParameter("accountId", accountId)
        		.list();
        Entry entry;
        ArrayList<EntryByAccount> displayList = new ArrayList<EntryByAccount>();

        Iterator<Entry> it = list.iterator();
        while (it.hasNext()) {
            entry = it.next();
            displayList.add(new EntryByAccount(accountId, entry));
        }
        transaction.commit();
        // Collections.sort(accountSummaryList, new AccountComparator());
        return displayList;
    }
    
    public List<Entry> entrysForMonth(Date date) {
        Date startDate = date;
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.MONTH, 1);
        Date endDate = new Date(calendar.getTimeInMillis());
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
//        List<Entry> list = session.createQuery(
//                "from Entry where date >= ? and date < ? order by date").setDate(0,
//                startDate).setDate(1, endDate).list();
        List<Entry> list = session.createQuery(
                "from Entry where date >= :startDate and date < :endDate order by date")
        		.setParameter("startDate",startDate)
        		.setParameter("endDate", endDate)
        		.list();
        Iterator<Entry> it = list.iterator();
        // We need to Instantiate the account entries;
        while(it.hasNext()) {
            Entry entry = it.next();
            entry.getDrAccount().getCode();
            entry.getCrAccount().getCode();
        }
        
        transaction.commit();
        return list;
    }
    
    private void addSummary(int month, Account account, BigDecimal amount) {
        MonthlySummary summary = this.summaryTable.get(account.getCode());
        if (summary == null) {
            summary = new MonthlySummary(this.totalMonths, account);
            this.summaryTable.put(account.getCode(), summary);
        }
        summary.add(month, amount);
    }

}
