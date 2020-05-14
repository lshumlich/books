package com.pe.books.servlets;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pe.books.Account;
import com.pe.books.AccountComparator;
import com.pe.books.BooksManager;
import com.pe.books.HibernateUtil;

public class AccountsBean implements Iterator<Account> {

    private List<Account> accounts;
    private int index;
    private String inactive;
    private String button;
    
    public String getInactive() {
        return inactive;
    }

    public void setInactive(String inactive) {
        this.inactive = inactive;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public void getAccountsInOrder() {
    	
//    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        this.accounts = BooksManager.getInstance().getAccounts();
        Collections.sort(this.accounts, new AccountComparator());
        this.index = 0;
        
        transaction.commit();

        //System.out.println("Inactive:" + this.inactive);
        //System.out.println("Button:" + this.button);
        //System.out.println("Size" + this.accounts.size());
    }

    public boolean hasNext() {
        if(this.accounts == null) return false;
        while(this.index < accounts.size()) {
            if (accounts.get(this.index).getActive() && this.button == null) return true;
            if (!accounts.get(this.index).getActive() && this.button != null) return true;
            this.index++;
        }
        
        if(this.index >= accounts.size()) return false;
        return true;
    }

    public Account next() {
        return accounts.get(this.index++);
    }

    public void remove() {
        throw new RuntimeException();
    }
}