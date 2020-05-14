package com.pe.books.servlets;

import com.pe.books.Account;
import com.pe.books.BooksManager;
import com.pe.books.EditException;

public class BalanceBean {

    private String code = "";

    private String currentBalance = "";

    public BalanceBean() {
    }

    public void processRequest() {
        if (this.code != null & this.code != "") {
            Account account = BooksManager.getInstance().getAccountByCode(this.code);
            if (account != null) {
                this.currentBalance = account.getCurrentBalanceFormatedCurrency();
            } else {
                this.currentBalance = this.code + " not found.";
            }
        }
    }

    private void populateBean() throws EditException {
        Account account = BooksManager.getInstance().getAccountByCode(this.code);
        this.currentBalance = account.getCurrentBalanceFormatedCurrency();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

}