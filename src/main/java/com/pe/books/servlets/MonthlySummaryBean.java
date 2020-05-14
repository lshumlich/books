package com.pe.books.servlets;

import java.sql.Date;
import java.util.List;

import com.pe.books.report.MonthlySummary;
import com.pe.books.report.SummaryProcessor;
import com.pe.books.report.SummaryUtils;

public class MonthlySummaryBean {

    private int range = 12;

    private Date startDate;

    private Date endDate;

    private String types = "ARE";

    private List<MonthlySummary> summaryList;

    private int summaryListIndex;

    private AccountTypeSummaryBean asset = new AccountTypeSummaryBean();

    private AccountTypeSummaryBean revenue = new AccountTypeSummaryBean();

    private AccountTypeSummaryBean expense = new AccountTypeSummaryBean();

    public MonthlySummaryBean() {
    }

    public Date getStartDate() {
        if (this.startDate == null) {
            if (this.range > 0) {
                this.startDate = SummaryUtils.lastYear();
            } else {
                this.startDate = SummaryUtils.thisMonth();
            }
        }
        return startDate;
    }

    public void setStartDateS(String s) {
        this.startDate = Date.valueOf(s);
    }

    public Date getEndDate() {
        if (this.endDate == null) {
            if (this.range > 0) {
                this.endDate = SummaryUtils.thisMonth();
            } else {
                this.endDate = SummaryUtils.nextMonth();
            }
        }
        return endDate;
    }

    public String getEndDateS() {
        return this.endDate.toString();
    }

    public void setEndDateS(String s) {
        this.endDate = Date.valueOf(s);
    }

    public void setRangeS(String s) {
        this.range = Integer.parseInt(s);
    }

    public void calcSummaryList() {
        SummaryProcessor summary = new SummaryProcessor();
        this.summaryList = summary.summary(this.getStartDate(), this.getEndDate());
        this.summaryListIndex = -1;
        this.getAssetSummary().clearTotals(this.getMonths());
        this.getRevenueSummary().clearTotals(this.getMonths());
        this.getExpenseSummary().clearTotals(this.getMonths());
    }

    public boolean hasNext() {
        if (this.summaryList == null)
            return false;
        this.summaryListIndex++;
        if (this.summaryListIndex > this.summaryList.size())
            return false;
        for (; this.summaryListIndex < this.summaryList.size(); this.summaryListIndex++) {
            if (this.types.indexOf(this.summaryList.get(this.summaryListIndex).getType()) > -1) {
                return true;
            }
        }
        return false;
    }

    public MonthlySummary next() {
        this.addTotal(this.summaryList.get(this.summaryListIndex));
        return this.summaryList.get(this.summaryListIndex);
    }

    private void addTotal(MonthlySummary monthlySummary) {
        AccountTypeSummaryBean accountTypeSummary = null;
        if (monthlySummary.getAccount().isAssetAccount()) {
            accountTypeSummary = this.getAssetSummary();
        } else if (monthlySummary.getAccount().isRevenueAccount()) {
            accountTypeSummary = this.getRevenueSummary();
        } else {
            accountTypeSummary = this.getExpenseSummary();
        }
        accountTypeSummary.addTotal(monthlySummary);
    }

    public String getFormatedMonth(Date date, int monthIncrement) {
        return SummaryUtils.formatedMonth(date, monthIncrement);
    }

    public String getFormatedYYYYMMDD(int monthIncrement) {
        return SummaryUtils.formatedYYYYMMDD(this.startDate, monthIncrement);
    }

    public int getMonths() {
        return SummaryUtils.months(this.getStartDate(), this.getEndDate());
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types.toUpperCase();
    }

    public AccountTypeSummaryBean getAssetSummary() {
        return this.asset;
    }

    public AccountTypeSummaryBean getRevenueSummary() {
        return this.revenue;
    }

    public AccountTypeSummaryBean getExpenseSummary() {
        return this.expense;
    }

}