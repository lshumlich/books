/**
 * 
 */
package com.pe.books.report;

import java.math.BigDecimal;
import java.sql.Date;

import com.pe.books.Entry;

public class EntryByAccount {
    private Long entryId;
    
    private Date date;

    private String description;

    private String offsetCode;
    
    private Long offsetAccountId;

    private BigDecimal drAmount;

    private BigDecimal crAmount;
    
    private Long checkNo;
    
    private boolean cleared;
    
    public String toString() {
        return super.toString() + "-" + this.getDate() + "," + this.getOffsetCode() + "," + this.getDescription() + ","
                + this.getDrAmount() + " " + this.getCrAmount();
    }

    public EntryByAccount(Long accountId, Entry entry) {
        this.setDate(entry.getDate());
        this.setDescription(entry.getDescription());
        this.setEntryId(entry.getEntryId());
        this.setCheckNo(entry.getCheckNo());
        if (entry.getCrAccount().getAccountId().longValue() == accountId.longValue()) {
            this.setOffsetCode(entry.getDrAccount().getCode());
            this.setOffsetAccountId(entry.getDrAccount().getAccountId());
            this.setCrAmount(entry.getAmount());
        } else if (entry.getDrAccount().getAccountId().longValue() == accountId.longValue()) {
            this.setOffsetCode(entry.getCrAccount().getCode());
            this.setOffsetAccountId(entry.getCrAccount().getAccountId());
            this.setDrAmount(entry.getAmount());
        } else {
            this.setOffsetCode(entry.getDrAccount().getCode() + "/" + entry.getCrAccount().getCode());
            this.setOffsetAccountId(entry.getCrAccount().getAccountId());
            this.setDrAmount(entry.getAmount());
            this.setCrAmount(entry.getAmount());
        }
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public String getOffsetCode() {
        return offsetCode;
    }

    public void setOffsetCode(String offsetCode) {
        this.offsetCode = offsetCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDrAmount() {
        return drAmount;
    }

    public void setDrAmount(BigDecimal drAmount) {
        this.drAmount = drAmount;
    }

    public BigDecimal getCrAmount() {
        return crAmount;
    }

    public void setCrAmount(BigDecimal crAmount) {
        this.crAmount = crAmount;
    }

    public String getDrAmountFormatedCurrency() {
        if (this.drAmount == null)
            return "0.00";
        return SummaryUtils.formatCurrency(this.drAmount);
    }

    public String getCrAmountFormatedCurrency() {
        if (this.crAmount == null)
            return "0.00";
        return SummaryUtils.formatCurrency(this.crAmount);
    }

    public Long getOffsetAccountId() {
        return offsetAccountId;
    }

    public void setOffsetAccountId(Long offsetAccountId) {
        this.offsetAccountId = offsetAccountId;
    }

    public Long getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(Long checkNo) {
        this.checkNo = checkNo;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }
}