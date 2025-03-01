package com.telerik.virtualwallet.exceptions;

public class InvalidSortParameterException extends RuntimeException {
    public InvalidSortParameterException(String name) {
        super(String.format("Invalid sort parameter: %s", name));
    }
}
