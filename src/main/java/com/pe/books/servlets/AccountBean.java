package com.pe.books.servlets;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pe.books.Account;
import com.pe.books.AccountDto;
import com.pe.books.BooksManager;
import com.pe.books.EditException;
import com.pe.books.ErrorMessage;
import com.pe.books.HibernateUtil;

public class AccountBean {
    private String accountId;

    private String code;

    private String displayOrder;

    private String type;

    private String description;

    private String currentBalance;

    private String budget;

    private String active;

    private String button;

    private String message;

    public AccountBean() {
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
            } else if (this.getButton().contentEquals("Count")) {
                this.count();
            }
            this.setButton(null);
        } catch (EditException e) {
            this.setMessage(this.getMessage() + " *** Error: " + e.getMessage());
            System.out.println(e);
        }
    }

    private void initBean() {
        this.setAccountId("0");
        this.setCode("");
        this.setDisplayOrder("");
        this.setType("");
        this.setDescription("");
        this.setCurrentBalance("");
        this.setBudget("");
        this.setActive("");
        this.setMessage("");
    }

    private void populateBean() throws EditException {

//    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
    	
        if (this.getAccountId() == null)
            this.setAccountId("0");
        Long accountId = Long.parseLong(this.getAccountId());
        if (accountId.longValue() != 0) {
            Account account = BooksManager.getInstance().getAccountById(accountId);
            this.setAccountId(account.getAccountId().toString());
            this.setCode(account.getCode());
            this.setDisplayOrder(""+account.getDisplayOrder());
            this.setType(account.getType());
            this.setDescription(account.getDescription());
            this.setCurrentBalance(account.getCurrentBalanceFormatedCurrency());
            this.setBudget(account.getBudgetFormatedCurrency());
            this.setActive(account.getActive().toString());
        }

        transaction.commit();
    }

    private void addOrUpdate() throws EditException {

//    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        AccountDto accountDto = null;
        Account account = null;
        Long accountId = Long.parseLong(this.getAccountId());
        if (accountId.longValue() != 0) {
            account = BooksManager.getInstance().getAccountById(Long.parseLong(this.getAccountId()));
            accountDto = new AccountDto(account);
        } else {
            accountDto = new AccountDto();
        }
        accountDto.setAccountId(this.getAccountId());
        accountDto.setCode(this.getCode());
        accountDto.setDisplayOrder(this.getDisplayOrder());
        accountDto.setType(this.getType());
        accountDto.setDescription(this.getDescription());
        accountDto.setCurrentBalance(this.getCurrentBalance());
        accountDto.setBudget(this.getBudget());
        accountDto.setActive(this.getActive());
        try {
            account = BooksManager.getInstance().addOrUpdateAccountDto(accountDto);
            this.setAccountId(account.getAccountId().toString());
            this.setMessage("Complete.");
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
    
    private void delete() throws EditException {

    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        Long accountId = Long.parseLong(this.getAccountId());
        int rows = BooksManager.getInstance().deleteAccount(accountId);
        if (rows == 1) {
            this.setMessage("Account deleted");
        } else {
            this.setMessage("*** Error " + rows + " deleted");
        }

        transaction.commit();

    }
    
    private void count() throws EditException {

    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Long accountId = Long.parseLong(this.getAccountId());
        int entrys = BooksManager.getInstance().countEntrysForAccount(accountId);
        this.setMessage("This account has " + entrys + " entry records.");

        transaction.commit();

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
    
}