package com.pe.books;

import java.math.BigDecimal;

public class AccountValidator {

    private EditHelper editHelper = new EditHelper();

    public Account validateDto(AccountDto accountDto) throws EditException {
        EditException editException = new EditException("Edit Errors were found in Account.");
        Account account = new Account();
        if (accountDto == null) {
            editException.addError("", "AccountDto is null.");
            throw editException;
        }
        account.setAccountId(this.validateAccountId(accountDto.getAccountId(), editException));
        account.setCode(accountDto.getCode());
        this.validateCode(account, editException);
        account.setDisplayOrder(this.validateDisplayOrder(accountDto.getDisplayOrder(), editException));
        this.validateType(accountDto.getType(), editException);
        account.setType(accountDto.getType());
        this.validateDescription(accountDto.getDescription(), editException);
        account.setDescription(accountDto.getDescription());
        account.setCurrentBalance(this.validateCurrentBalance(accountDto.getCurrentBalance(), editException));
        account.setBudget(this.validateBudget(accountDto.getBudget(), editException));
        account.setActive(this.validateActive(accountDto.getActive(), editException));

        if (editException.getErrorCount() > 0) {
            throw editException;
        } else {
            return account;
        }
    }

    public void validate(Account account) throws EditException {
        EditException editException = new EditException("Edit Errors were found in Account.");

        this.validateCode(account, editException);
        this.validateType(account.getType(), editException);
        this.validateDescription(account.getDescription(), editException);
        this.validateCurrentBalance(account.getCurrentBalance(), editException);
        this.validateBudget(account.getBudget(), editException);
        this.validateActive(account.getActive(), editException);

        if (editException.getErrorCount() > 0) {
            throw editException;
        } else {
            return;
        }
    }

    protected Long validateAccountId(String s, EditException editException) {
        int id = 0;
        if (editHelper.isEmpty(s))
//            return new Long(id);
        	return new Long(id);

        try {
            id = (int) Long.parseLong(s);
        } catch (NumberFormatException e) {
            editException.addError("", "Invalid Account Id:" + e.getMessage());
        }
        return new Long(id);
    }

    protected void validateCode(Account accountToValidate, EditException editException) {
        if (editHelper.isEmpty(accountToValidate.getCode())) {
            editException.addError("", "Account Code can not be blank.");
        }
        Account account = BooksManager.getInstance().getAccountByCode(accountToValidate.getCode());
        if (account != null) {
            if (accountToValidate.getAccountId() == null || account.getAccountId().intValue() != accountToValidate.getAccountId().intValue()) {
                editException.addError("", "Account Code must be unique and it already exists.");
            }
        }
    }

    protected int validateDisplayOrder(String s, EditException editException) {
        int id = 0;
        if (editHelper.isEmpty(s))
            return id;

        try {
            id = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            editException.addError("", "Invalid Account Order:" + e.getMessage());
        }
        return id;
    }

    protected void validateType(String type, EditException editException) {
        if (editHelper.isEmpty(type)) {
            editException.addError("", "Account Type can not be blank.");
        } else {
            if (type.contentEquals("A") || type.contentEquals("R") || type.contentEquals("E")) {
                // It's ok
            } else {
                editException.addError("", "Account Type must be 'A', 'R' or 'E'.");
            }
        }
    }

    protected void validateDescription(String description, EditException editException) {
        if (editHelper.isEmpty(description)) {
            editException.addError("", "Account Description can not be blank.");
        }
    }

    protected BigDecimal validateCurrentBalance(String amount, EditException editException) {
        BigDecimal bd = null;
        if (this.editHelper.isEmpty(amount)) {
            editException.addError("", "Current Balance must be entered.");
        } else {
            try {
                amount = amount.replaceAll("\\$", "");
                amount = amount.replaceAll(",", "");
                bd = new BigDecimal(amount);
                this.validateCurrentBalance(bd, editException);
            } catch (NumberFormatException e) {
                editException.addError("", "Current Balance must be numeric '" + amount + "'");
            }
        }

        return bd;
    }

    protected void validateCurrentBalance(BigDecimal currentBalance, EditException editException) {
        if (currentBalance == null) {
            editException.addError("", "Current Balance must be entered (zero is ok).");
        } else {
            if (currentBalance.scale() > 2) {
                editException.addError("", "Current Balance precision must not be > 2 digits but it was "
                        + currentBalance.scale() + ".");
            }
        }
    }

    protected BigDecimal validateBudget(String amount, EditException editException) {
        BigDecimal bd = null;
        if (this.editHelper.isEmpty(amount)) {
            editException.addError("", "Budget must be entered.");
        } else {
            try {
                amount = amount.replaceAll("\\$", "");
                amount = amount.replaceAll(",", "");
                bd = new BigDecimal(amount);
                this.validateBudget(bd, editException);
            } catch (NumberFormatException e) {
                editException.addError("", "Budget must be numeric '" + amount + "'");
            }
        }

        return bd;
    }

    protected void validateBudget(BigDecimal budget, EditException editException) {
        if (budget == null) {
            editException.addError("", "Budget must be entered (zero is ok).");
        } else {
            if (budget.scale() > 2) {
                editException
                        .addError("", "Budget precision must not be > 2 digits but it was " + budget.scale() + ".");
            }
        }
    }

    protected Boolean validateActive(String s, EditException editException) {
        Boolean active = this.editHelper.validateBooleanDefaultTrue(s);
        if (active == null) {
            editException.addError("", "Active is not valid:" + s);
        } else {
            this.validateActive(active, editException);
        }
        return active;
    }

    protected void validateActive(Boolean active, EditException editException) {
        if (active == null) {
            editException.addError("", "Active must be entered.");
        }
    }
}