package com.pe.books.servlets;

import junit.framework.TestCase;

public class EntryBeanTest extends TestCase {
	
	public void testPopulateBean() {
		EntryBean bean = new EntryBean();
		bean.processRequest();
		System.out.println("testPopulateBean: " + bean.getMessage());
	}
	
	public void testAddOrUpdate() {
		EntryBean bean = new EntryBean();
		bean.setButton("Update");
		bean.processRequest();
		System.out.println("testAddOrUpdate: " + bean.getMessage());
	}
	
	public void testDelete() {
		EntryBean bean = new EntryBean();
		bean.setButton("Delete");
		bean.processRequest();
		System.out.println("testDetete: " + bean.getMessage());
	}

}
