package com.pe.books;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class AccountTest extends TestCase  {
    
    public void testIsAssetAccount() {
        Account account = new Account();
        assertFalse(account.isAssetAccount());

        account.setType("X");
        assertFalse(account.isAssetAccount());

        account.setType("a");
        assertFalse(account.isAssetAccount());

        account.setType("A");
        assertTrue(account.isAssetAccount());
    }
    
    public void testAddToBalance() {
        Account account = new Account();
        account.setCurrentBalance(new BigDecimal("10"));
        account.addToBalance(new BigDecimal("1.11"));
        assertEquals(new BigDecimal("11.11"),account.getCurrentBalance());
    }
    
    public void testSubtractFromBalance() {
        Account account = new Account();
        account.setCurrentBalance(new BigDecimal("11.11"));
        account.subtractFromBalance(new BigDecimal("1.11"));
        assertEquals(new BigDecimal("10.00"),account.getCurrentBalance());
    }
}
