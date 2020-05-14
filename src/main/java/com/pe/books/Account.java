package com.pe.books;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.pe.books.report.SummaryUtils;

public class Account implements AccountComparatorIf{
    private Long accountId;

    private String code;

    private int displayOrder;

    // 'A'sset / 'E'xpense / 'R'evenue
    private String type;

    private String description;

    private BigDecimal currentBalance;

    private BigDecimal budget;

    private Boolean active;

    private Timestamp lastUpdated;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
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

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public String getCurrentBalanceFormatedCurrency() {
        return SummaryUtils.formatCurrency(this.getCurrentBalance());
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getBudgetFormatedCurrency() {
        return SummaryUtils.formatCurrency(this.getBudget());
    }

    public String getBudgetFormated0() {
        return SummaryUtils.format0decimal(this.getBudget());
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String toString() {
        return super.toString() + " - " + this.getAccountId() + "," + this.getCode() + ","
                + this.getDisplayOrder() + "," + this.getType() + "," + this.getDescription() + ","
                + this.getCurrentBalance() + "," + this.getBudget() + "," + this.getActive();
    }

    public boolean isAssetAccount() {
        if(this.getType() == null) return false;
        return this.getType().contentEquals("A");
    }

    public boolean isRevenueAccount() {
        if(this.getType() == null) return false;
        return this.getType().contentEquals("R");
    }

    public boolean isExpenseAccount() {
        if(this.getType() == null) return false;
        return this.getType().contentEquals("E");
    }

//    public boolean isActive() {
//        return this.getActive();
//    }
    public void addToBalance(BigDecimal amount) {
        this.setCurrentBalance(this.getCurrentBalance().add(amount));
    }

    public void subtractFromBalance(BigDecimal amount) {
        this.setCurrentBalance(this.getCurrentBalance().subtract(amount));
    }
}
