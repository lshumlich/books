package com.pe.books;

import java.sql.Date;

public class EntryDto {
    private String entryId;

    private String date;

    private String drCode;

    private String drClear;

    private String crCode;

    private String crClear;

    private String description;

    private String amount;

    private String checkNo;

    public EntryDto() {
        super();
        this.setEntryId("0");
        this.setDate(new Date(System.currentTimeMillis()).toString());
        this.setDrCode("");
        this.setCrCode("");
        this.setDescription("");
        this.setAmount("");
        this.setCheckNo("");
    }

    public EntryDto(Entry entry) {
        super();
        this.setEntryId(entry.getEntryId().toString());
        this.setDate(entry.getDate().toString());
        this.setDrCode(entry.getDrAccount().getCode());
        this.setCrCode(entry.getCrAccount().getCode());
        this.setDescription(entry.getDescription());
        this.setAmount(entry.getAmountFormatedCurrency());
        this.setCheckNo(entry.getCheckNo().toString());
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
        if (drCode == null) {
            this.drCode = null;
        } else {
            this.drCode = drCode.toLowerCase();
        }
    }

    public String getDrClear() {
        return drClear;
    }

    public void setDrClear(String drClear) {
        this.drClear = drClear;
    }

    public String getCrCode() {
        return crCode;
    }

    public void setCrCode(String crCode) {
        if (crCode == null) {
            this.crCode = null;
        } else {
            this.crCode = crCode.toLowerCase();
        }
    }

    public String getCrClear() {
        return crClear;
    }

    public void setCrClear(String crClear) {
        this.crClear = crClear;
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

    public String toString() {
        return super.toString() + "-" + this.getEntryId() + "," + this.getDate() + "," + this.getDrCode() + ","
                + this.getDrClear() + "," + this.getCrCode() + "," + this.getCrClear() + "," + this.getDescription()
                + "," + this.getAmount() + "," + this.getCheckNo();
    }
}