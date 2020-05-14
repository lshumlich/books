package com.pe.books;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class EditHelperTest extends TestCase {

    private EditHelper editHelper = new EditHelper();
    
    public void testIsEmpty() {
        assertTrue(this.editHelper.isEmpty(null));
        assertTrue(this.editHelper.isEmpty(""));
        assertFalse(this.editHelper.isEmpty("asdf"));
        assertFalse(this.editHelper.isEmpty("a"));
    }

    public void testDateFormats() throws EditException {
        Date date;
        Date baseDate = Date.valueOf("2008-02-14");
        GregorianCalendar calendar = new GregorianCalendar();

        try {
            date = this.editHelper.validateYYYYMMDDDate("asdf"); // error
            fail("Should be invalid");
        } catch (EditException e) {
            // expected
        }

        try {
            date = this.editHelper.validateYYYYMMDDDate(null); // error
            fail("Should be invalid");
        } catch (EditException e) {
            // expected
        }

        date = this.editHelper.validateYYYYMMDDDate("2008x02x14");
        assertEquals(baseDate.getTime(), date.getTime());
        assertEquals(baseDate, date);
        
        date = this.editHelper.validateYYYYMMDDDate("2008-02-14");
        assertEquals(baseDate.getTime(), date.getTime());
        assertEquals(baseDate, date);

        try {
            date = this.editHelper.validateYYYYMMDDDate("2008/04/31"); // Error
            fail("There is not 31 days in April and should be invalid");
        } catch (EditException e) {
            // expected
        }
        try {
            date = this.editHelper.validateYYYYMMDDDate("2008/02/14/11/11"); // Error
            fail("There is not 31 days in April and should be invalid");
        } catch (EditException e) {
            // expected
        }

        date = this.editHelper.validateYYYYMMDDDate("2008/02/14");
        calendar.setTime(date);
        assertEquals(2008, calendar.get(Calendar.YEAR));
        // Java starts months at zero. January is zero.
        assertEquals(2, calendar.get(Calendar.MONTH) + 1);
        assertEquals(14, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(date.getTime(), Date.valueOf("2008-02-14").getTime());
    }
    
    public void testDrClear() {
        EditException editException = new EditException("Edit Error.");

        assertTrue(this.editHelper.validateBoolean("Yes"));
        assertTrue(this.editHelper.validateBoolean("Y"));
        assertTrue(this.editHelper.validateBoolean("True"));
        assertTrue(this.editHelper.validateBoolean("1"));
        assertFalse(this.editHelper.validateBoolean("No"));
        assertFalse(this.editHelper.validateBoolean("N"));
        assertFalse(this.editHelper.validateBoolean("False"));
        assertFalse(this.editHelper.validateBoolean("0"));
        assertFalse(this.editHelper.validateBoolean(""));
        assertFalse(this.editHelper.validateBoolean(null));
        assertNull(this.editHelper.validateBoolean("asdf"));
        assertNull(this.editHelper.validateBoolean("x"));
        assertNull(this.editHelper.validateBoolean("z"));
    }

}
