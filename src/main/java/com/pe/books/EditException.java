package com.pe.books;

import java.util.ArrayList;
import java.util.List;

public class EditException extends Exception {
    private List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
    public EditException(String msg) {
        super(msg);
    }
    public void addError(String code, String msg) {
        errors.add(new ErrorMessage(code, msg));
    }
    public int getErrorCount() {
        return errors.size();
    }
    public List<ErrorMessage> getMessages() {
        return errors;
    }
    public String toString() {
        StringBuffer msg = new StringBuffer();
        msg.append("EditException:");
        for(int i = 0; i < errors.size(); i++) {
            msg.append(errors.get(i));
        }
        return msg.toString();
    }
}
