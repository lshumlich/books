package com.pe.books.servlets;

import java.sql.Date;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pe.books.Account;
import com.pe.books.BooksManager;
import com.pe.books.EditException;
import com.pe.books.Entry;
import com.pe.books.EntryDto;
import com.pe.books.ErrorMessage;
import com.pe.books.HibernateUtil;

public class EntryBean {
    private String entryId;

    private String date;

    private String drCode;

    private String drCodeMessage;

    private String crCode;

    private String crCodeMessage;

    private String description;

    private String amount;

    private String checkNo;

    private String button;

    private String message;

    public EntryBean() {
        super();
        this.initBean();
    }

    public void processRequest() {
        try {
            if (this.getButton() == null) {
                this.populateBean();
                this.setMessage("");
            } else if (this.getButton().contentEquals("Cancel")) {
                this.setMessage("Cancel");
            } else if (this.getButton().contentEquals("Update")) {
                this.addOrUpdate();
            } else if (this.getButton().contentEquals("Delete")) {
                this.delete();
            }
            this.setButton(null);
        } catch (EditException e) {
            this.setMessage(this.getMessage() + " *** Error: " + e.getMessage());
        }
    }

    private void initBean() {
        this.setEntryId("0");
        this.setDate(new Date(System.currentTimeMillis()).toString());
        this.setDrCode("");
        this.setCrCode("");
        this.setDescription("");
        this.setAmount("");
        this.setCheckNo("");
        this.setDrCodeMessage("");
        this.setCrCodeMessage("");
        this.setMessage("");
    }

    private void populateBean() throws EditException {

//    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        if (this.getEntryId() == null)
            this.setEntryId("0");
        Long entryId = Long.parseLong(this.getEntryId());
        if (entryId.longValue() != 0) {
            Entry entry = BooksManager.getInstance().getEntryById(entryId);
            this.setEntryId(entry.getEntryId().toString());
            this.setDate(entry.getDate().toString());
            this.setDrCode(entry.getDrAccount().getCode());
            this.setCrCode(entry.getCrAccount().getCode());
            this.setDescription(entry.getDescription());
            this.setAmount(entry.getAmountFormatedCurrency());
            this.setCheckNo(entry.getCheckNo().toString());
        }

        transaction.commit();

    }

    private void addOrUpdate() throws EditException {

//    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        EntryDto entryDto = null;
        Entry entry = null;
        Long entryId = Long.parseLong(this.getEntryId());
        if (entryId.longValue() != 0) {
            entry = BooksManager.getInstance().getEntryById(Long.parseLong(this.getEntryId()));
            entryDto = new EntryDto(entry);
        } else {
            entryDto = new EntryDto();
        }
        entryDto.setDate(this.getDate());
        entryDto.setDrCode(this.getDrCode());
        entryDto.setCrCode(this.getCrCode());
        entryDto.setDescription(this.getDescription());
        entryDto.setAmount(this.getAmount());
        entryDto.setCheckNo(this.getCheckNo());
        try {
            entry = BooksManager.getInstance().addOrUpdateEntryDto(entryDto);
            this.setMessage("Complete " + entry.getAmountFormatedCurrency());
            this.setBalanceMessages(entry);
        } catch (EditException e) {
            StringBuffer msg = new StringBuffer();
            Iterator<ErrorMessage> it = e.getMessages().iterator();
            while (it.hasNext()) {
                msg.append(it.next() + "<br>");
            }
            this.setMessage(msg.toString());
        }
        
        transaction.commit();
    }

    private void setBalanceMessages(Entry entry) {
        if (entry.getDrAccount().isAssetAccount()) {
            // The account record in the entry is sometimes out of date.
            Account account = BooksManager.getInstance().getAccountById(entry.getDrAccount().getAccountId());
            this.setDrCodeMessage("Balance: " + account.getCurrentBalanceFormatedCurrency());
        }
        if (entry.getCrAccount().isAssetAccount()) {
            Account account = BooksManager.getInstance().getAccountById(entry.getCrAccount().getAccountId());
            this.setCrCodeMessage("Balance: " + account.getCurrentBalanceFormatedCurrency());
        }
    }
    
    private void delete() throws EditException {
    	
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Entry entry = BooksManager.getInstance().getEntryById(Long.parseLong(this.getEntryId()));
        int rows = BooksManager.getInstance().deleteEntry(Long.parseLong(this.getEntryId()));
        if (rows == 1) {
            this.setMessage("Deleted");
        } else {
            this.setMessage("*** Error " + rows + " deleted");
        }
        this.setBalanceMessages(entry);
        
        transaction.commit();

    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDrCode() {
        return drCode;
    }

    public void setDrCode(String drCode) {
        this.drCode = drCode;
    }

    public String getCrCode() {
        return crCode;
    }

    public void setCrCode(String crCode) {
        this.crCode = crCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDrCodeMessage() {
        return drCodeMessage;
    }

    public void setDrCodeMessage(String drCodeMessage) {
        this.drCodeMessage = drCodeMessage;
    }

    public String getCrCodeMessage() {
        return crCodeMessage;
    }

    public void setCrCodeMessage(String crCodeMessage) {
        this.crCodeMessage = crCodeMessage;
    }
}