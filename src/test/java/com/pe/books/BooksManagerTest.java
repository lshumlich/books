package com.pe.books;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pe.books.report.EntryByAccount;

public class BooksManagerTest extends TestCase {

    private AccountValidatorTest accountValidatorTest = new AccountValidatorTest();
 
    public void testSingleton() {
        BooksManager instance1 = BooksManager.getInstance();
        BooksManager instance2 = BooksManager.getInstance();
        assertEquals(instance1, instance2);
    }

    public void testInsertandUpdateEntryWithAssetAccountBalance() throws EditException {
        this.deleteAllEntryAndAccountRows();
        Account assetAccount = this.insertValidAssetAccount();
        Account expenseAccount = this.insertValidExpenseAccount();
        BigDecimal assetBalance = assetAccount.getCurrentBalance();

        BooksManager booksManager = BooksManager.getInstance();

        Entry entry = new Entry();
        entry.setDate(new Date(System.currentTimeMillis()));
        entry.setDrAccount(assetAccount);
        entry.setDrClear(false);
        entry.setCrAccount(expenseAccount);
        entry.setCrClear(false);
        entry.setAmount(new BigDecimal("100."));
        booksManager.addOrUpdateEntry(entry);
        assetBalance = assetBalance.add(new BigDecimal("100"));

        Account updatedAssetAccount2 = booksManager.getAccountByCode(assetAccount.getCode());
        assertEquals(assetBalance, updatedAssetAccount2.getCurrentBalance());

        entry = new Entry();
        entry.setDate(new Date(System.currentTimeMillis()));
        entry.setDrAccount(expenseAccount);
        entry.setDrClear(false);
        entry.setCrAccount(assetAccount);
        entry.setCrClear(false);
        entry.setAmount(new BigDecimal("200."));
        booksManager.addOrUpdateEntry(entry);
        assetBalance = assetBalance.subtract(new BigDecimal("200"));

        Account updatedAssetAccount3 = booksManager.getAccountByCode(assetAccount.getCode());
        assertEquals(assetBalance, updatedAssetAccount3.getCurrentBalance());

        // Test an update with the dr and cr accounts switched
        Entry updatedEntry = new Entry();
        updatedEntry.setEntryId(entry.getEntryId());
        updatedEntry.setDate(new Date(System.currentTimeMillis()));
        updatedEntry.setDrAccount(assetAccount);
        updatedEntry.setDrClear(false);
        updatedEntry.setCrAccount(expenseAccount);
        updatedEntry.setCrClear(false);
        updatedEntry.setAmount(new BigDecimal("100."));
        booksManager.addOrUpdateEntry(updatedEntry);
        assetBalance = assetBalance.add(new BigDecimal("200")); // Reverse the
                                                                // original
                                                                // entry
        assetBalance = assetBalance.add(new BigDecimal("100")); // Add this
                                                                // entry

        Account updatedAssetAccount4 = booksManager.getAccountByCode(assetAccount.getCode());
        assertEquals(assetBalance, updatedAssetAccount4.getCurrentBalance());
    }

    public void testAccountDtoAddOrUpdate() throws EditException {
        BooksManager booksManager = BooksManager.getInstance();
        this.deleteAllEntryAndAccountRows();
        AccountDto accountDto = null;

        accountDto = new AccountDto();
        accountDto.setCode("New Asset Account");
        accountDto.setDisplayOrder("110");
        accountDto.setType("A");
        accountDto.setDescription("Even a bigger Description");
        accountDto.setCurrentBalance("120.12");
        accountDto.setBudget("10000");
        accountDto.setActive("yes");
        Account account1 = booksManager.addOrUpdateAccountDto(accountDto);

        accountDto = new AccountDto();
        accountDto.setCode("Newer Asset Account");
        accountDto.setDisplayOrder("110");
        accountDto.setType("A");
        accountDto.setDescription("Even a bigger Description");
        accountDto.setCurrentBalance("120.12");
        accountDto.setBudget("10000");
        accountDto.setActive("yes");
        Account account2 = booksManager.addOrUpdateAccountDto(accountDto);

        List<Account> list = booksManager.getAccounts();
        assertEquals(2, list.size());
    }

    public void testAccountDtoAddOrUpdateWithErrors() {
        BooksManager booksManager = BooksManager.getInstance();
        this.deleteAllEntryAndAccountRows();
        AccountDto accountDto = null;
        try {
            Account account1 = booksManager.addOrUpdateAccountDto(accountDto);
        } catch (EditException e) {
            // Expected there should have been edit errors.
        }
        List<Account> list = booksManager.getAccounts();
        assertEquals(0, list.size());
    }
 
    public void testEntryNotFoundForUpdate() {
        BooksManager booksManager = BooksManager.getInstance();
        Entry entry = new Entry();
        entry.setEntryId(new Long(+1000000));
        try {
            booksManager.addOrUpdateEntry(entry);
            fail("We should have got an Entry not found for updated exeception");
        } catch (EditException e) {
            // what we expect
        }
    }
    
    public void testAccountNotFoundForUpdate() {
        BooksManager booksManager = BooksManager.getInstance();
        Account account = new Account();
        account.setAccountId(new Long(+1000000));
        try {
            booksManager.addOrUpdateAccount(account);
            fail("We should have got an Account not found for updated exeception");
        } catch (EditException e) {
            // what we expect
        }
    }

    public void testAddEntryDtoWithEditErrors() {
        BooksManager booksManager = BooksManager.getInstance();
        EntryDto entryDto = new EntryDto();
        try {
            booksManager.addOrUpdateEntryDto(entryDto);
            fail("We should not update an entryDto with edit errors.");
        } catch (EditException e) {
            // what we expect
        }
    }

    public void testAddAndUpdateEntryDto() throws EditException {
        this.deleteAllEntryAndAccountRows();
        Account assetAccount = this.insertValidAssetAccount();
        Account expenseAccount = this.insertValidExpenseAccount();
        BooksManager booksManager = BooksManager.getInstance();
        BigDecimal assetBalance = assetAccount.getCurrentBalance();

        EntryDto entryDto = new EntryDto();
        entryDto.setDate("2009/03/18");
        entryDto.setDrCode(assetAccount.getCode());
        entryDto.setCrCode(expenseAccount.getCode());
        entryDto.setDescription("Another wopping expense.");
        entryDto.setAmount("2500.12");
        Entry entry = booksManager.addOrUpdateEntryDto(entryDto);
        Account updatedAssetAccount2 = booksManager.getAccountByCode(assetAccount.getCode());
        assertEquals(assetBalance.add(new BigDecimal("2500.12")), updatedAssetAccount2.getCurrentBalance());

        entryDto.setEntryId(entry.getEntryId().toString());
        entryDto.setAmount("1200.06");
        entry = booksManager.addOrUpdateEntryDto(entryDto);
        updatedAssetAccount2 = booksManager.getAccountByCode(assetAccount.getCode());
        assertEquals(assetBalance.add(new BigDecimal("1200.06")), updatedAssetAccount2.getCurrentBalance());
    }

    public void testGetAccountByCode() throws EditException {
        BooksManager booksManager = BooksManager.getInstance();
        this.deleteAllEntryAndAccountRows();

        Account newAccount = this.insertValidAssetAccount();

        Account account = booksManager.getAccountByCode("asdf");
        assertNull(account); // Should not be found

        account = booksManager.getAccountByCode(newAccount.getCode());
        assertNotNull(account);
    }

    public void testClearing() throws EditException {
        Long account1 = new Long(1);
        Long account2 = new Long(2);
        BooksManager booksManager = BooksManager.getInstance();
        
        booksManager.resetClearedEntries(account1);
        booksManager.resetClearedEntries(account2);
        assertEquals(0, booksManager.getClearedEntries(account1).size());
        assertEquals(0, booksManager.getClearedEntries(account2).size());
        
        booksManager.clearEntry(account1, new Long(1));
        assertEquals(1, booksManager.getClearedEntries(account1).size());
        assertEquals(0, booksManager.getClearedEntries(account2).size());
        
        booksManager.clearEntry(account1, new Long(2));
        assertEquals(2, booksManager.getClearedEntries(account1).size());
        assertEquals(0, booksManager.getClearedEntries(account2).size());
        
        booksManager.clearEntry(account1, new Long(3));
        assertEquals(3, booksManager.getClearedEntries(account1).size());
        assertEquals(0, booksManager.getClearedEntries(account2).size());
        
        
        booksManager.unClearEntry(account1, new Long(2));
        assertEquals(2, booksManager.getClearedEntries(account1).size());
        assertEquals(0, booksManager.getClearedEntries(account2).size());
        
        booksManager.resetClearedEntries(account2);
        assertEquals(2, booksManager.getClearedEntries(account1).size());
        assertEquals(0, booksManager.getClearedEntries(account2).size());
        
        booksManager.resetClearedEntries(account1);
        assertEquals(0, booksManager.getClearedEntries(account1).size());
        assertEquals(0, booksManager.getClearedEntries(account2).size());
    }

    public void testGetAndClearEntries() throws EditException {
        this.deleteAllEntryAndAccountRows();
        Account assetAccount = this.insertValidAssetAccount();
        Account expenseAccount = this.insertValidExpenseAccount();
        BooksManager booksManager = BooksManager.getInstance();

        EntryDto entryDto = new EntryDto();
        entryDto.setDate("2009/03/18");
        entryDto.setDrCode(assetAccount.getCode());
        entryDto.setCrCode(expenseAccount.getCode());
        entryDto.setDescription("Another wopping expense.");
        entryDto.setAmount("100.01");
        booksManager.addOrUpdateEntryDto(entryDto);

        entryDto.setAmount("100.02");
        booksManager.addOrUpdateEntryDto(entryDto);

        entryDto.setAmount("100.03");
        booksManager.addOrUpdateEntryDto(entryDto);

        entryDto.setDrCode(expenseAccount.getCode());
        entryDto.setCrCode(assetAccount.getCode());
        entryDto.setDescription("A little deposit.");
        entryDto.setAmount("100.04");
        booksManager.addOrUpdateEntryDto(entryDto);

        entryDto.setDrCode(expenseAccount.getCode());
        entryDto.setCrCode(expenseAccount.getCode());
        entryDto.setDescription("Another silly entry.");
        entryDto.setAmount("100.05");
        Entry entry = booksManager.addOrUpdateEntryDto(entryDto);
        List<EntryByAccount> entryByAccountList;

        List<Entry> entrys = booksManager.getEntrys();
        assertEquals(5, entrys.size());

        entryByAccountList = booksManager.getEntrysToClear(assetAccount.getAccountId());
        assertEquals(4, entryByAccountList.size());
        ArrayList<Long> list = new ArrayList<Long>();
        list.add(entryByAccountList.get(0).getEntryId());
        booksManager.updateClearedEntries(assetAccount.getAccountId(), list);
        entryByAccountList = booksManager.getEntrysToClear(assetAccount.getAccountId());
        assertEquals(3, entryByAccountList.size());

        list.clear();
        list.add(entryByAccountList.get(1).getEntryId());
        list.add(entryByAccountList.get(2).getEntryId());
        booksManager.updateClearedEntries(assetAccount.getAccountId(), list);
        entryByAccountList = booksManager.getEntrysToClear(assetAccount.getAccountId());
        assertEquals(1, entryByAccountList.size());

        entrys = booksManager.getEntrys();
        assertEquals(5, entrys.size());

        try {
            list.clear();
            list.add(entrys.get(1).getEntryId());
            booksManager.updateClearedEntries(new Long(12345), list);
            fail("Should have throwed an Edit Exception.");
        } catch (EditException e) {
            // expected result
        }
    }

    protected void deleteAllEntryAndAccountRows() {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        
        int entryRows = session.createQuery("delete Entry").executeUpdate();
        int accountRows = session.createQuery("delete Account").executeUpdate();
        System.out.println(entryRows + " Entry rows deleted.");
        System.out.println(accountRows + " Account rows deleted.");
        transaction.commit();
    }

    protected Account insertValidAssetAccount() throws EditException {
        Account account = this.accountValidatorTest.getValidAssetAccount();
        BooksManager booksManager = BooksManager.getInstance();
        booksManager.addOrUpdateAccount(account);
        return account;
    }

    protected Account insertValidExpenseAccount() throws EditException {
        Account account = this.accountValidatorTest.getValidExpenseAccount();
        BooksManager booksManager = BooksManager.getInstance();
        booksManager.addOrUpdateAccount(account);
        return account;
    }
}
