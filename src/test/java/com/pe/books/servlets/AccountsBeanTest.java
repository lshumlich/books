package com.pe.books.servlets;

import junit.framework.TestCase;

public class AccountsBeanTest extends TestCase {
	public void testCreateBean() {
		AccountsBean bean = new AccountsBean();
		bean.getAccountsInOrder();
	}
}
