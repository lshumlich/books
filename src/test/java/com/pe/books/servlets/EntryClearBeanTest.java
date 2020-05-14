package com.pe.books.servlets;

import com.pe.books.EditException;

import junit.framework.TestCase;

public class EntryClearBeanTest extends TestCase {
	
    public void testProcessRequest() throws EditException {
    	EntryClearBean bean = new EntryClearBean();
    	bean.setAccountId("0");
//    	Will need to setup clearing entry
//    	bean.processRequest();
    }

}
