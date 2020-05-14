package com.pe.books.servlets;

import junit.framework.TestCase;

public class EntryBeanTest extends TestCase {
	
	public void testPopulateBean() {
		EntryBean bean = new EntryBean();
		bean.processRequest();
	}
	
	public void testAddOrUpdate() {
		EntryBean bean = new EntryBean();
		bean.setButton("Update");
		bean.processRequest();
	}
	
	public void testDelete() {
		EntryBean bean = new EntryBean();
		bean.setButton("Delete");
		bean.processRequest();
	}

}
