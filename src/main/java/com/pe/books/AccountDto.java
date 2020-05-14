package com.pe.books;

public class AccountDto {
    private String accountId;

    private String code;

    private String displayOrder;

    private String type;

    private String description;

    private String currentBalance;

    private String budget;

    private String active;

    private String lastUpdated;

    public AccountDto() {
        super();
        this.setAccountId("0");
        this.setCode("");
        this.setDisplayOrder("");
        this.setType("");
        this.setDescription("");
        this.setCurrentBalance("0.00");
        this.setBudget("0.00");
        this.setActive("true");
    }
    public AccountDto(Account account) {
        this();
        this.setAccountId(account.getAccountId().toString());
        this.setCode(account.getCode());
        this.setDisplayOrder(""+account.getDisplayOrder());
        this.setType(account.getType());
        this.setDescription(account.getDescription());
        this.setCurrentBalance(account.getCurrentBalanceFormatedCurrency());
        this.setBudget(account.getBudgetFormatedCurrency());
        this.setActive(account.getActive().toString());
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
        if(code == null) {
            this.code = null;
        } else {
            this.code = code.toLowerCase();
        }
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
        if(type == null) {
            this.type = null;
        } else {
            this.type = type.toUpperCase();
        }
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

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String toString() {
        return super.toString() + " - " + this.getAccountId() + "," + this.getCode() + "," + this.getDisplayOrder()
                + "," + this.getType() + "," + this.getDescription() + "," + this.getCurrentBalance() + ","
                + this.getBudget() + "," + this.getActive();
    }
}
