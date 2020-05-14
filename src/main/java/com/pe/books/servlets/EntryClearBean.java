package com.pe.books.servlets;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pe.books.Account;
import com.pe.books.BooksManager;
import com.pe.books.EditException;
import com.pe.books.HibernateUtil;
import com.pe.books.report.EntryByAccount;
import com.pe.books.report.SummaryUtils;

public class EntryClearBean {

    private String accountId;

    private String clearEntryId;

    private String unClearEntryId;

    private BigDecimal clearedAmount;

    private BigDecimal unClearedAmount;

    private BigDecimal openingBalance;

    private BigDecimal closingBalance;

    private List<EntryByAccount> displayList;

    private Account account;

    private List<Long> clearedEntries;

    private String button;

    public void processRequest() throws EditException {
    	
//    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
    	
        if (this.getClearEntryId() != null) {
            this.clearEntry();
        }
        if (this.getUnClearEntryId() != null) {
            this.unClearEntry();
        }
        if (this.getButton() != null) {
            if (this.getButton().contentEquals("Update")) {
                this.update();
            }
            if (this.getButton().contentEquals("Restart")) {
                this.restart();
            }
        }
        this.populateBean();
        this.calculate();
        
        transaction.commit();
        
    }

    private void update() throws EditException {
        Long accountId = Long.parseLong(this.getAccountId());
        List<Long> clearedEntries = BooksManager.getInstance().getClearedEntries(accountId);
        BooksManager.getInstance().updateClearedEntries(accountId, clearedEntries);
    }

    private void restart() {
        Long accountId = Long.parseLong(this.getAccountId());
        BooksManager.getInstance().resetClearedEntries(accountId);
    }

    private void calculate() {
        Iterator<EntryByAccount> it = this.displayList.iterator();
        EntryByAccount entryByAccount;
        this.clearedAmount = new BigDecimal("0.00");
        this.unClearedAmount = new BigDecimal("0.00");
        while (it.hasNext()) {
            entryByAccount = it.next();
            entryByAccount.setCleared(this.clearedEntries.contains(entryByAccount.getEntryId()));
            if (entryByAccount.isCleared()) {
                this.clearedAmount = this.add(this.clearedAmount, entryByAccount.getDrAmount());
                this.clearedAmount = this.subtract(this.clearedAmount, entryByAccount.getCrAmount());
            } else {
                this.unClearedAmount = this.add(this.unClearedAmount, entryByAccount.getDrAmount());
                this.unClearedAmount = this.subtract(this.unClearedAmount, entryByAccount.getCrAmount());
            }
        }
        this.openingBalance = this.account.getCurrentBalance().subtract(this.unClearedAmount).subtract(
                this.clearedAmount);
        this.closingBalance = this.account.getCurrentBalance().subtract(this.unClearedAmount);
        Collections.sort(this.displayList, new ClearEntryComparater());
    }

    private BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null && b == null)
            return new BigDecimal("0.00");
        if (a == null)
            return b;
        if (b == null)
            return a;

        return a.add(b);
    }

    private BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null && b == null)
            return new BigDecimal("0.00");
        if (a == null)
            return b.negate();
        if (b == null)
            return a;

        return a.subtract(b);
    }

    private void clearEntry() throws EditException {
        Long accountId = Long.parseLong(this.getAccountId());
        Long entryId = Long.parseLong(this.getClearEntryId());
        BooksManager.getInstance().clearEntry(accountId, entryId);
    }

    private void unClearEntry() throws EditException {
        Long accountId = Long.parseLong(this.getAccountId());
        Long entryId = Long.parseLong(this.getUnClearEntryId());
        BooksManager.getInstance().unClearEntry(accountId, entryId);
    }

    private void populateBean() throws EditException {
        Long accountId = Long.parseLong(this.accountId);
        this.account = BooksManager.getInstance().getAccountById(accountId);
        this.displayList = BooksManager.getInstance().getEntrysToClear(accountId);
        this.clearedEntries = BooksManager.getInstance().getClearedEntries(accountId);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return this.account;
    }

    public String getClearEntryId() {
        return clearEntryId;
    }

    public void setClearEntryId(String clearEntryId) {
        this.clearEntryId = clearEntryId;
    }

    public String getUnClearEntryId() {
        return unClearEntryId;
    }

    public void setUnClearEntryId(String unClearEntryId) {
        this.unClearEntryId = unClearEntryId;
    }

    public List<EntryByAccount> getDisplayList() {
        return this.displayList;
    }

    class ClearEntryComparater implements Comparator<EntryByAccount> {

        public int compare(EntryByAccount arg0, EntryByAccount arg1) {
            if (arg0.isCleared() != arg1.isCleared()) {
                if (arg0.isCleared())
                    return 1;
                return -1;
            }
            int value = (arg0.getDate().compareTo(arg1.getDate()));
            if (value != 0)
                return value;
            return (arg0.getEntryId().compareTo(arg1.getEntryId()));
        }
    }

    public void setDisplayList(List<EntryByAccount> displayList) {
        this.displayList = displayList;
    }

    public String getUnClearedAmountCurrency() {
        return SummaryUtils.formatCurrency(this.unClearedAmount);
    }

    public String getClearedAmountCurrency() {
        return SummaryUtils.formatCurrency(this.clearedAmount);
    }

    public String getOpeningBalanceCurrency() {
        return SummaryUtils.formatCurrency(this.openingBalance);
    }

    public String getClosingBalanceCurrency() {
        return SummaryUtils.formatCurrency(this.closingBalance);
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
}