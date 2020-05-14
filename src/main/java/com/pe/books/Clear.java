package com.pe.books;


public class Clear {
    private Long clearId;

    private Long accountId;

    private Long entryId;

    public Long getClearId() {
        return clearId;
    }

    public void setClearId(Long clearId) {
        this.clearId = clearId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String toString() {
        return super.toString() + " - " + this.getAccountId() + "," + this.getEntryId() + ",";
    }

}
