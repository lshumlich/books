package com.pe.books.report;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SummaryUtils {
    private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CANADA);
    private static DecimalFormat decimal0 = new DecimalFormat("#,###,##0");
    private static SimpleDateFormat dateFormatyyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateFormatmmm_yy = new SimpleDateFormat("MMM yy");
    
    public static int months(Date fromDate, Date toDate) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(fromDate.getTime());
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        calendar.setTimeInMillis(toDate.getTime());
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        return (endYear - startYear) * 12 + (endMonth - startMonth);
    }
    
    public static Date nextMonth() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, 1);
        return new Date(calendar.getTimeInMillis());
    }
    
    public static Date thisMonth() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }
    
    public static Date lastYear() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.YEAR, -1);
        return new Date(calendar.getTimeInMillis());
    }
    
    public static String formatedMonth(Date date, int monthIncrement) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.MONTH, monthIncrement);
        return SummaryUtils.dateFormatmmm_yy.format(calendar.getTime());
    }

    public static String formatedYYYYMMDD(Date date, int monthIncrement) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.MONTH, monthIncrement);
        return SummaryUtils.dateFormatyyyy_mm_dd.format(calendar.getTime());
    }
    
    public static String format0decimal(BigDecimal number) {
        if(number == null) return "0";
        return SummaryUtils.decimal0.format(number);
    }
    
    public static String formatCurrency(BigDecimal number) {
        if(number == null) return "$0.00";
        return SummaryUtils.currencyFormat.format(number);
    }
}
