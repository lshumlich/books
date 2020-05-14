/**
 * 
 */
package com.pe.books;

import java.util.Comparator;

public class AccountComparator implements Comparator<AccountComparatorIf> {
    public int compare(AccountComparatorIf arg0, AccountComparatorIf arg1) {
        if (!arg0.getType().contentEquals(arg1.getType())) {
            return "ARE".indexOf(arg0.getType()) - "ARE".indexOf(arg1.getType());
        } else {
            return arg0.getDisplayOrder() - arg1.getDisplayOrder();
        }
    }
}