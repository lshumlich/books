package com.pe.books;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class AccountValidatorTest extends TestCase {

    private AccountValidator accountValidator = new AccountValidator();

    public void testValidateAllErrors() {
        Account account = new Account();
        this.assertEditErrors(6, account);
    }

    public void testValidateDtoAllErrors() {
        AccountDto accountDto = new AccountDto();
        try {
            this.accountValidator.validateDto(accountDto);
            fail("We should have had 5 edit errors.");
        } catch (EditException editException) {
            assertEquals(3, editException.getErrorCount());
        }
    }
    
    public void testValidateDtoNoErrors() throws EditException {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId("100");
        accountDto.setCode("Asset Account");
        accountDto.setDisplayOrder("1");
        accountDto.setType("A");
        accountDto.setDescription("Huge Account not enouph zero's");
        accountDto.setCurrentBalance("2");
        accountDto.setBudget("3");
        accountDto.setActive("no");
        Account account = this.accountValidator.validateDto(accountDto);
        
        assertEquals(new Long(100), account.getAccountId());
        assertEquals("asset account", account.getCode());
        assertEquals(1, account.getDisplayOrder());
        assertEquals("A", account.getType());
        assertEquals("Huge Account not enouph zero's",account.getDescription());
        assertEquals(new BigDecimal("2"), account.getCurrentBalance());
        assertEquals(new BigDecimal("3"), account.getBudget());
        assertEquals(new Boolean("false"), account.getActive());
    }

    public void testValidAssetAccount() {
        new BooksManagerTest().deleteAllEntryAndAccountRows();
        Account account = this.getValidAssetAccount();
        this.assertEditErrors(0, account);
    }

    public void testValidExpenseAccount() {
        new BooksManagerTest().deleteAllEntryAndAccountRows();
        Account account = this.getValidExpenseAccount();
        this.assertEditErrors(0, account);
    }

    public void testValidateCode() {
        EditException editException = new EditException("Edit Error.");
        Account account = new Account();

        account.setCode("asdf");
        this.accountValidator.validateCode(account, editException);
        this.assertEditErrors(0, editException);

        account.setCode("");
        this.accountValidator.validateCode(account, editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        account.setCode(null);
        this.accountValidator.validateCode(account, editException);
        this.assertEditErrors(1, editException);
    }
    
    public void testValidateCodeWithUpdate() throws EditException {
        EditException editException = new EditException("Edit Error.");
        BooksManagerTest booksManagerTest = new BooksManagerTest();
        booksManagerTest.deleteAllEntryAndAccountRows();
        Account assetAccount = booksManagerTest.insertValidAssetAccount();
        Account account = new Account();

        account.setCode("asdf");
        this.accountValidator.validateCode(account, editException);
        this.assertEditErrors(0, editException);

        account.setCode(assetAccount.getCode());
        this.accountValidator.validateCode(assetAccount, editException);
        this.assertEditErrors(0, editException);
    }

    public void testValidateType() {
        EditException editException = new EditException("Edit Error.");
        this.accountValidator.validateType("E", editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateType("X", editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        this.accountValidator.validateType("", editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        this.accountValidator.validateType(null, editException);
        this.assertEditErrors(1, editException);
    }

    public void testValidateDescription() {
        EditException editException = new EditException("Edit Error.");
        this.accountValidator.validateDescription("asdf", editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateDescription("", editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        this.accountValidator.validateDescription(null, editException);
        this.assertEditErrors(1, editException);
    }

    public void testCurrentBalanceBD() throws EditException {
        EditException editException = new EditException("Edit Error.");
        this.accountValidator.validateCurrentBalance(new BigDecimal("123.12"), editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateCurrentBalance(new BigDecimal("123.1"), editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateCurrentBalance(new BigDecimal("123"), editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateCurrentBalance(new BigDecimal("123.123"), editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        this.accountValidator.validateCurrentBalance(new BigDecimal(123.12), editException);
        this.assertEditErrors(1, editException);
    }
    
    public void testCurrentBalanceString() {
        EditException editException = new EditException("Edit Error.");
        this.accountValidator.validateCurrentBalance("123.12", editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateCurrentBalance("$1,123.12", editException);
        this.assertEditErrors(0, editException);
    }

    public void testBudget() throws EditException {
        EditException editException = new EditException("Edit Error.");
        this.accountValidator.validateBudget(new BigDecimal("123.12"), editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateBudget(new BigDecimal("123.1"), editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateBudget(new BigDecimal("123"), editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateBudget(new BigDecimal("123.123"), editException);
        this.assertEditErrors(1, editException);

        editException = new EditException("Edit Error.");
        this.accountValidator.validateBudget(new BigDecimal(123.12), editException);
        this.assertEditErrors(1, editException);
    }

    public void testActive() throws EditException {
        EditException editException = new EditException("Edit Error.");
        this.accountValidator.validateActive(new Boolean(true), editException);
        this.assertEditErrors(0, editException);

        this.accountValidator.validateActive((Boolean)null, editException);
        this.assertEditErrors(1, editException);
    }

    private void assertEditErrors(int expectedCount, EditException editException) {
        assertEquals(editException.toString(), expectedCount, editException.getErrorCount());
    }

    private void assertEditErrors(int expectedCount, Account account) {
        try {
            this.accountValidator.validate(account);
            if (expectedCount != 0) {
                fail("We expected " + expectedCount + " edit errors but got zero.");
            }
        } catch (EditException e) {
            assertEditErrors(expectedCount, e);
        }
    }

    protected Account getValidAssetAccount() {
        Account account = new Account();
        account.setCode("cibc");
        account.setDisplayOrder(10);
        account.setType("A");
        account.setDescription("Bank Account");
        account.setCurrentBalance(new BigDecimal("1212.12"));
        account.setBudget(new BigDecimal("20.20"));
        account.setActive(true);
        return account;
    }

    protected Account getValidExpenseAccount() {
        Account account = new Account();
        account.setCode("entertainment");
        account.setDisplayOrder(10);
        account.setType("E");
        account.setDescription("Having tons of fun");
        account.setCurrentBalance(new BigDecimal("20.50"));
        account.setBudget(new BigDecimal("20000.20"));
        account.setActive(true);
        return account;
    }

}
