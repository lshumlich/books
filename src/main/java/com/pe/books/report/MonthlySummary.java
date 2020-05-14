/**
 * 
 */
package com.pe.books.report;

import java.math.BigDecimal;

import com.pe.books.Account;
import com.pe.books.AccountComparatorIf;

public class MonthlySummary implements AccountComparatorIf {
    private Account account;

    private BigDecimal[] amount;

    private BigDecimal total;

    public MonthlySummary(int totalMonths, Account account) {
        this.amount = new BigDecimal[totalMonths];
        this.total = new BigDecimal("0.00");
        this.account = account;
    }

    public void add(int month, BigDecimal amount) {
        this.total = this.total.add(amount);
        if (this.amount[month] == null)
            this.amount[month] = new BigDecimal("0.00");
        this.amount[month] = this.amount[month].add(amount);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.account.getCode());
        sb.append("\t");
        sb.append(this.account.getType());
        sb.append("\t");
        sb.append(this.account.getDisplayOrder());
        sb.append("\t");
        sb.append(this.account.getDescription());
        sb.append("\t");
        sb.append(this.total);
        sb.append("\t");
        sb.append(this.account.getBudget());
        for (int i = 0; i < this.amount.length; i++) {
            sb.append("\t");
            if (this.amount[i] == null) {
                sb.append("0.00");
            } else {
                sb.append(this.amount[i]);
            }
        }
        return sb.toString();
    }

    public int getDisplayOrder() {
        return this.account.getDisplayOrder();
    }

    public String getType() {
        return this.account.getType();
    }
    
    public Account getAccount() {
        return this.account;
    }
    
    public String getAmountFormated0(int i) {
        return SummaryUtils.format0decimal(this.amount[i]);
    }
    
    public String getTotalFormated0() {
        return SummaryUtils.format0decimal(this.total);
    }
    
    public BigDecimal getAmount(int i) {
        return this.amount[i];
    }
    
    public BigDecimal getTotal() {
        return this.total;
    }
    
    public BigDecimal getAverage() {
        return this.total.divide(new BigDecimal(this.getMonths()),2,BigDecimal.ROUND_HALF_UP);
    }
    
    public String getAverageFormated0() {
        return SummaryUtils.format0decimal(this.getAverage());
    }
    
    public int getMonths() {
        return this.amount.length;
    }
}