package com.telerik.virtualwallet.exceptions;

public class ExpiredCardException extends RuntimeException {
    public ExpiredCardException(String message) {
        super(message);
    }
}
