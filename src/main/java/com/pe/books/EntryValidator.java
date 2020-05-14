package com.pe.books;

import java.math.BigDecimal;
import java.sql.Date;

public class EntryValidator {

    private EditHelper editHelper = new EditHelper();

    public Entry validate(EntryDto entryDto) throws EditException {
        EditException editException = new EditException("Edit Errors were found in Entry.");
        Entry entry = new Entry();

        entry.setEntryId(this.validateEntryId(entryDto.getEntryId(), editException));
        entry.setDate(this.validateDate(entryDto.getDate(), editException));
        entry.setDrAccount(this.validateDrCode(entryDto.getDrCode(), editException));
        entry.setCrAccount(this.validateCrCode(entryDto.getCrCode(), editException));
        entry.setDrClear(this.validateDrClear(entryDto.getDrClear(), editException));
        entry.setCrClear(this.validateCrClear(entryDto.getCrClear(), editException));
        entry.setDescription(entryDto.getDescription());
        entry.setAmount(this.validateAmount(entryDto.getAmount(), editException));
        entry.setCheckNo(this.validateCheckNo(entryDto.getCheckNo(), editException));

        if (editException.getErrorCount() > 0) {
            throw editException;
        } else {
            return entry;
        }
    }

    protected Long validateEntryId(String s, EditException editException) {
        long id = 0;
        if (editHelper.isEmpty(s))
            return new Long(id);

        try {
            id = Long.parseLong(s);
        } catch (NumberFormatException e) {
            editException.addError("", "Invalid Entry Id:" + e.getMessage());
        }
        return new Long(id);
    }

    protected Date validateDate(String dateString, EditException editException) {
        Date date = null;
        try {
            date = editHelper.validateYYYYMMDDDate(dateString);
        } catch (EditException e) {
            editException.addError("", "Date is not valid: '" + dateString + "'");
        }

        return date;
    }

    protected void validateDate(Date date, EditException editException) {
        if (date == null) {
            editException.addError("", "Date must be entered.");
        }
    }

    protected Account validateDrCode(String drCode, EditException editException) {
        Account account = null;
        if (this.editHelper.isEmpty(drCode)) {
            editException.addError("", "Debit Code must be entered.");
        } else {
            account = this.validateAccountCode(drCode);
            if (account == null) {
                editException.addError("", "Debit Code is invalid or account is inactive: '" + drCode + "'");
            }
        }
        return account;
    }

    protected Account validateCrCode(String crCode, EditException editException) {
        Account account = null;
        if (this.editHelper.isEmpty(crCode)) {
            editException.addError("", "Credit Code must be entered.");
        } else {
            account = this.validateAccountCode(crCode);
            if (account == null) {
                editException.addError("", "Credit Code is invalid or account is inactive: '" + crCode + "'");
            }
        }
        return account;
    }

    protected Account validateAccountCode(String code) {
        Account account = BooksManager.getInstance().getAccountByCode(code);
        if (account != null && !account.getActive())
            return null;
        return account;
    }

    protected Boolean validateDrClear(String s, EditException editException) {
        Boolean b = this.editHelper.validateBoolean(s);
        if (b == null) {
            editException.addError("", "Debit clear is not valid: '" + s + "'");
        }
        return b;
    }

    protected Boolean validateCrClear(String s, EditException editException) {
        Boolean b = this.editHelper.validateBoolean(s);
        if (b == null) {
            editException.addError("", "Credit clear is not valid: '" + s + "'");
        }
        return b;
    }

    protected BigDecimal validateAmount(String amount, EditException editException) {

        BigDecimal bd = null;
        if (this.editHelper.isEmpty(amount)) {
            editException.addError("", "Amount must be entered.");
        } else {
            try {
                amount = amount.replaceAll("\\$", "");
                amount = amount.replaceAll(",", "");
                bd = new BigDecimal(amount);
                this.validateAmount(bd, editException);
            } catch (NumberFormatException e) {
                editException.addError("", "Amount must be numeric '" + amount + "'");
            }
        }

        return bd;
    }

    protected void validateAmount(BigDecimal amount, EditException editException) {
        if (amount == null) {
            editException.addError("", "Amount must be entered and not zero.");
            return;
        } else {
            if (amount.scale() > 2) {
                editException
                        .addError("", "Amount precision must not be > 2 digits but it was " + amount.scale() + ".");
                return;
            }
        }
        if (amount.floatValue() <= .00) {
            editException.addError("", "Amount must be entered and not zero.");
        }
    }

    protected Long validateCheckNo(String checkNo, EditException editException) {
        Long checkNoLong = null;
        if (this.editHelper.isEmpty(checkNo)) {
            checkNoLong = new Long(0);
        } else {
            try {
                checkNoLong = new Long(checkNo);
            } catch (NumberFormatException e) {
                editException.addError("", "Check number must be numeric '" + checkNo + "'");
            }
        }

        return checkNoLong;
    }
}
