package com.pe.books.servlets;

import java.math.BigDecimal;

import com.pe.books.report.MonthlySummary;
import com.pe.books.report.SummaryUtils;

public class AccountTypeSummaryBean {

    private BigDecimal[] totalAmount;

    private BigDecimal totalBalance;

    private BigDecimal totalBudget;

    private BigDecimal totalTotal;

    private int months;

    public void clearTotals(int months) {
        this.months = months;
        this.totalAmount = new BigDecimal[months];
        this.totalBalance = new BigDecimal("0.00");
        this.totalBudget = new BigDecimal("0.00");
        this.totalTotal = new BigDecimal("0.00");

    }

    protected void addTotal(MonthlySummary monthlySummary) {
        this.totalBalance = this.totalBalance.add(monthlySummary.getAccount().getCurrentBalance());
        this.totalBudget = this.totalBudget.add(monthlySummary.getAccount().getBudget());
        this.totalTotal = this.totalTotal.add(monthlySummary.getTotal());
        for (int i = 0; i < monthlySummary.getMonths(); i++) {
            if (this.totalAmount[i] == null)
                this.totalAmount[i] = new BigDecimal("0.00");
            if (monthlySummary.getAmount(i) != null) {
                this.totalAmount[i] = this.totalAmount[i].add(monthlySummary.getAmount(i));
            }
        }
    }

    public BigDecimal[] getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public BigDecimal getTotalTotal() {
        return totalTotal;
    }

    public String getTotalBalanceFormated0() {
        return SummaryUtils.format0decimal(this.totalBalance);
    }

    public String getTotalBudgetFormated0() {
        return SummaryUtils.format0decimal(this.totalBudget);
    }

    public String getTotalTotalFormated0() {
        return SummaryUtils.format0decimal(this.totalTotal);
    }

    public String getTotalAmountFormated0(int i) {
        return SummaryUtils.format0decimal(this.totalAmount[i]);
    }

    public BigDecimal getAverage() {
        return this.totalTotal.divide(new BigDecimal(this.months), 2, BigDecimal.ROUND_HALF_UP);
    }

    public String getAverageFormated0() {
        return SummaryUtils.format0decimal(this.getAverage());
    }

}
