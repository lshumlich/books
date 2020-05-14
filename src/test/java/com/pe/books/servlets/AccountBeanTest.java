package com.pe.books.servlets;

import junit.framework.TestCase;

public class AccountBeanTest extends TestCase  {
	
	public void testCreateBean() {
		System.out.println("This test started");
		AccountBean bean = new AccountBean();
		bean.processRequest();
		for (int i = 0; i < 5; i++) {
			System.out.println(i);
			bean.setButton("Update");
			bean.processRequest();
			System.out.println(bean.getMessage());
		}
		bean.setButton("Update");
		bean.setCode("code4");
		bean.setType("a");
		bean.setDescription("Larry's Test Account");
		bean.setCurrentBalance("0");
		bean.setBudget("0");		
		bean.processRequest();
		System.out.println(bean.getMessage());
		
		for (int i = 0; i < 5; i++) {
			System.out.println(i);
			bean.setButton("Update");
			String s = "0" + i;
			bean.setCurrentBalance(s);
			bean.processRequest();
			System.out.println(bean.getMessage());
		}
//		System.out.println(bean.getMessage());
		System.out.println("This test endeded");
	}
	
	

}
