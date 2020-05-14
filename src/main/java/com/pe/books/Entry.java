package com.pe.books;

import java.math.BigDecimal;
import java.sql.Date;

import com.pe.books.report.SummaryUtils;

public class Entry {
    private Long entryId;

    private Date date;

    private Account drAccount;

    private Boolean drClear;

    private Account crAccount;

    private Boolean crClear;

    private String description;

    private BigDecimal amount;

    private Long checkNo;

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Account getDrAccount() {
        return drAccount;
    }

    public void setDrAccount(Account drAccount) {
        this.drAccount = drAccount;
    }

    public Boolean getDrClear() {
        return drClear;
    }

    public void setDrClear(Boolean drClear) {
        this.drClear = drClear;
    }

    public Account getCrAccount() {
        return crAccount;
    }

    public void setCrAccount(Account crAccount) {
        this.crAccount = crAccount;
    }

    public Boolean getCrClear() {
        return crClear;
    }

    public void setCrClear(Boolean crClear) {
        this.crClear = crClear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getAmountFormatedCurrency() {
        return SummaryUtils.formatCurrency(this.getAmount());
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(Long checkNo) {
        this.checkNo = checkNo;
    }

    public String toString() {
        return super.toString() + "-" + this.getEntryId() + "," + this.getDate() + "," + this.getAmount() + ","
                + this.getDescription();
    }
}
