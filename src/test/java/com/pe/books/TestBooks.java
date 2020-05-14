package com.pe.books;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.pe.books.report.SummaryUtilsTest;
import com.pe.books.report.SummaryProcessorTest;
import com.pe.books.servlets.AccountsBeanTest;
import com.pe.books.servlets.AccountBeanTest;
import com.pe.books.servlets.EntryBeanTest;
import com.pe.books.servlets.EntryClearBeanTest;


public class TestBooks extends TestCase {

    public static Test suite() { 
        TestSuite suite= new TestSuite();
        suite.addTestSuite(EditHelperTest.class);
        suite.addTestSuite(AccountTest.class);
        suite.addTestSuite(AccountValidatorTest.class);
        suite.addTestSuite(BooksManagerTest.class);
        suite.addTestSuite(EntryValidatorTest.class);
        suite.addTestSuite(SummaryUtilsTest.class);
        suite.addTestSuite(SummaryProcessorTest.class);
        suite.addTestSuite(AccountsBeanTest.class);
        suite.addTestSuite(AccountBeanTest.class);
        suite.addTestSuite(EntryBeanTest.class);
//        suite.addTestSuite(EntryClearBeanTest.class);
        
        // Sample of how to run just one test.
        //suite.addTest(new AccountValidatorTest());
        return suite;
    }
}
