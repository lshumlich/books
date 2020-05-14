package com.pe.books;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class EntryValidatorTest extends TestCase {

    private EntryValidator entryValidator = new EntryValidator();

    private Date date = Date.valueOf("2009-03-17");

    public void testPopulateEntryFromEntryDto() throws EditException {
        BooksManagerTest booksManagerTest = new BooksManagerTest();
        booksManagerTest.deleteAllEntryAndAccountRows();
        Account assetAccount = booksManagerTest.insertValidAssetAccount();
        Account expenseAccount = booksManagerTest.insertValidExpenseAccount();
        EntryDto entryDto = this.getValidEntryDto();
        entryDto.setDrCode(assetAccount.getCode());
        entryDto.setCrCode(expenseAccount.getCode());
        try {
            Entry entry = this.entryValidator.validate(entryDto);
            assertEquals(new Long(entryDto.getEntryId()), entry.getEntryId());
            assertEquals(this.date.getTime(), entry.getDate().getTime());
            assertEquals(this.date, entry.getDate());
            assertEquals(entryDto.getDrCode(), entry.getDrAccount().getCode());
            assertFalse(entry.getDrClear());
            assertEquals(entryDto.getCrCode(), entry.getCrAccount().getCode());
            assertFalse(entry.getCrClear());
            assertEquals(entryDto.getDescription(), entry.getDescription());
            assertEquals(new BigDecimal(entryDto.getAmount()), entry.getAmount());
            assertEquals(new Long(entryDto.getCheckNo()), entry.getCheckNo());
        } catch (EditException e) {
            fail(e.toString());
        }
    }

    private EntryDto getValidEntryDto() {
        EntryDto entryDto = new EntryDto();
        entryDto.setEntryId("0");
        entryDto.setDate(date.toString());
        entryDto.setDrCode("????"); // Must be in database
        entryDto.setCrCode("????"); // Must be in database
        entryDto.setDescription("Some big wopper of an expense");
        entryDto.setAmount("1000.20");
        entryDto.setCheckNo("0");
        return entryDto;
    }

    public void testEntryId() {
        Long entryId = null;
        EditException editException = new EditException("Edit Error.");
        entryId = entryValidator.validateEntryId("", editException);
        this.assertEditErrors(0, editException);
        assertEquals(new Long(0), entryId);

        editException = new EditException("Edit Error.");
        entryValidator.validateEntryId("123", editException);
        this.assertEditErrors(0, editException);
        assertEquals(new Long(0), entryId);

        editException = new EditException("Edit Error.");
        entryValidator.validateEntryId("123.1", editException);
        this.assertEditErrors(1, editException);
        assertEquals(new Long(0), entryId);

        editException = new EditException("Edit Error.");
        entryValidator.validateEntryId("asdf", editException);
        this.assertEditErrors(1, editException);
        assertEquals(new Long(0), entryId);
    }

    public void testValidateDate() {
        EditException editException = new EditException("Edit Error.");
        this.entryValidator.validateDate("asdf", editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        this.entryValidator.validateDate("", editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        this.entryValidator.validateDate((String) null, editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        Date date = this.entryValidator.validateDate("2008-02-18", editException);
        this.assertEditErrors(0, editException);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        assertEquals(2008, calendar.get(Calendar.YEAR));
        // Java starts months at zero. January is zero.
        assertEquals(2, calendar.get(Calendar.MONTH) + 1);
        assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void testDrCode() throws EditException {
        BooksManagerTest booksManagerTest = new BooksManagerTest();
        booksManagerTest.deleteAllEntryAndAccountRows();
        Account newAccount = booksManagerTest.insertValidAssetAccount();

        Account account;

        EditException editException = new EditException("Edit Error.");
        account = this.entryValidator.validateDrCode("", editException);
        this.assertEditErrors(1, editException);
        assertNull(account);

        editException = new EditException("Edit Error.");
        account = this.entryValidator.validateDrCode((String) null, editException);
        this.assertEditErrors(1, editException);
        assertNull(account);

        editException = new EditException("Edit Error.");
        account = this.entryValidator.validateDrCode("asdf", editException);
        this.assertEditErrors(1, editException);
        assertNull(account);

        editException = new EditException("Edit Error.");
        account = this.entryValidator.validateDrCode(newAccount.getCode(), editException);
        this.assertEditErrors(0, editException);
        assertNotNull(account);
    }

    public void testCrCode() throws EditException {
        BooksManagerTest booksManagerTest = new BooksManagerTest();
        booksManagerTest.deleteAllEntryAndAccountRows();
        Account newAccount = booksManagerTest.insertValidAssetAccount();

        Account account;

        EditException editException = new EditException("Edit Error.");
        account = this.entryValidator.validateCrCode("", editException);
        this.assertEditErrors(1, editException);
        assertNull(account);

        editException = new EditException("Edit Error.");
        account = this.entryValidator.validateCrCode((String) null, editException);
        this.assertEditErrors(1, editException);
        assertNull(account);

        editException = new EditException("Edit Error.");
        account = this.entryValidator.validateCrCode("asdf", editException);
        this.assertEditErrors(1, editException);
        assertNull(account);

        editException = new EditException("Edit Error.");
        account = this.entryValidator.validateCrCode(newAccount.getCode(), editException);
        this.assertEditErrors(0, editException);
        assertNotNull(account);
    }

    public void testInactiveAccount() throws EditException {
        BooksManagerTest booksManagerTest = new BooksManagerTest();
        BooksManager booksManager = BooksManager.getInstance();
        booksManagerTest.deleteAllEntryAndAccountRows();
        Account assetAccount = booksManagerTest.insertValidAssetAccount();

        Account expenseAccount = booksManagerTest.insertValidExpenseAccount();
        EntryDto entryDto = this.getValidEntryDto();
        entryDto.setDrCode(assetAccount.getCode());
        entryDto.setCrCode(expenseAccount.getCode());
        booksManager.addOrUpdateEntryDto(entryDto);
        booksManager.addOrUpdateEntryDto(entryDto);

        assetAccount = booksManager.getAccountByCode(assetAccount.getCode());
        assetAccount.setActive(false);
        booksManager.addOrUpdateAccount(assetAccount);

        try {
            booksManager.addOrUpdateEntryDto(entryDto);
            fail("Should not be allowed to add or update an entry to an inactive account.");
        } catch (EditException e) {
            // This error is expected.
        }
    }

    public void testDrClear() {
        EditException editException = new EditException("Edit Error.");

        assertTrue(this.entryValidator.validateDrClear("Yes", editException));
        this.assertEditErrors(0, editException);
        assertTrue(this.entryValidator.validateDrClear("Y", editException));
        this.assertEditErrors(0, editException);
        assertTrue(this.entryValidator.validateDrClear("True", editException));
        this.assertEditErrors(0, editException);
        assertTrue(this.entryValidator.validateDrClear("1", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateDrClear("No", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateDrClear("N", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateDrClear("False", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateDrClear("0", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateDrClear(null, editException));
        this.assertEditErrors(0, editException);
    }

    public void testCrClear() {
        EditException editException = new EditException("Edit Error.");

        assertTrue(this.entryValidator.validateCrClear("Yes", editException));
        this.assertEditErrors(0, editException);
        assertTrue(this.entryValidator.validateCrClear("Y", editException));
        this.assertEditErrors(0, editException);
        assertTrue(this.entryValidator.validateCrClear("True", editException));
        this.assertEditErrors(0, editException);
        assertTrue(this.entryValidator.validateCrClear("1", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateCrClear("No", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateCrClear("N", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateCrClear("False", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateCrClear("0", editException));
        this.assertEditErrors(0, editException);
        assertFalse(this.entryValidator.validateCrClear(null, editException));
        this.assertEditErrors(0, editException);
    }

    public void testAmount() {
        EditException editException = new EditException("Edit Error.");
        BigDecimal amount;

        amount = this.entryValidator.validateAmount("asdf", editException);
        this.assertEditErrors(1, editException);
        editException = new EditException("Edit Error.");

        this.entryValidator.validateAmount(new BigDecimal("123.12"), editException);
        this.assertEditErrors(0, editException);

        this.entryValidator.validateAmount(new BigDecimal("123.1"), editException);
        this.assertEditErrors(0, editException);

        this.entryValidator.validateAmount(new BigDecimal("123"), editException);
        this.assertEditErrors(0, editException);

        this.entryValidator.validateAmount(new BigDecimal("123.123"), editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        this.entryValidator.validateAmount(new BigDecimal("0.00"), editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        this.entryValidator.validateAmount(new BigDecimal("0.01"), editException);
        this.assertEditErrors(0, editException);

        editException = new EditException("Edit Error.");
        this.entryValidator.validateAmount(new BigDecimal(123.12), editException);
        this.assertEditErrors(1, editException);
    }

    public void testCheckNo() {
        EditException editException = new EditException("Edit Error.");
        Long checkNo;

        checkNo = this.entryValidator.validateCheckNo("asdf", editException);
        this.assertEditErrors(1, editException);
        editException = new EditException("Edit Error.");

        checkNo = this.entryValidator.validateCheckNo("", editException);
        this.assertEditErrors(0, editException);
        assertEquals(0l, checkNo.longValue());
        editException = new EditException("Edit Error.");

        checkNo = this.entryValidator.validateCheckNo("123", editException);
        this.assertEditErrors(0, editException);
        assertEquals(123l, checkNo.longValue());
        editException = new EditException("Edit Error.");
    }

    private void assertEditErrors(int expectedCount, EditException editException) {
        assertEquals(editException.toString(), expectedCount, editException.getErrorCount());
    }
}