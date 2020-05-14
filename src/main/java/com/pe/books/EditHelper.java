package com.pe.books;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditHelper {

    public boolean isEmpty(String s) {
        if (s == null || s.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Expects the date to be in "yyyy/mm/dd" format;
     */
    public Date validateYYYYMMDDDate(String s) throws EditException {
        int year = 0;
        int month = 0;
        int day = 0;
        
        if (this.isEmpty(s)) {
            throw new EditException("Invalid Date format, expected 'YYYY/MM/DD'.");
        }
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            year = Integer.parseInt(matcher.group());
        }
        if (matcher.find()) {
            month = Integer.parseInt(matcher.group());
        }
        if (matcher.find()) {
            day = Integer.parseInt(matcher.group());
        }
        if (matcher.find()) {
            throw new EditException("Invalid Date format, expected 'YYYY/MM/DD'.");
        }
        if (year == 0 || month == 0 || day == 0) {
            throw new EditException("Invalid Date format, expected 'YYYY/MM/DD'.");
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(0);
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.MONTH, month - 1);
        calendar.set(GregorianCalendar.DAY_OF_MONTH, day);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);

        // Invalid day so the month changes (java is just to smart)
        if (calendar.get(GregorianCalendar.MONTH) != month - 1) {
            throw new EditException("Not a valid day for the month, expected 'YYYY/MM/DD'.");
        }

        return new Date(calendar.getTimeInMillis());
    }
    /**
     * @param s
     * @return null if invalid 
     */
    public Boolean validateBoolean(String s) {
        if (this.isEmpty(s))
            return false;
        String s2 = s.toLowerCase();
        if (s2.contentEquals("y") || s2.contentEquals("yes") || s2.contentEquals("true") || s2.contentEquals("1"))
            return true;
        if (s2.contentEquals("n") || s2.contentEquals("no") || s2.contentEquals("false") || s2.contentEquals("0"))
            return false;
        return null;
    }
    
    public Boolean validateBooleanDefaultTrue(String s) {
        if (this.isEmpty(s))
            return true;
        return this.validateBoolean(s);
    }
}
