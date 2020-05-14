/**
 * 
 */
package com.pe.books;

public class ErrorMessage {
    private String code;
    private String msg;

    public ErrorMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public String toString() {
        return code + ":" + msg;
    }
    
}