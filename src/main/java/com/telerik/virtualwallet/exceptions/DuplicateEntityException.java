package com.telerik.virtualwallet.exceptions;

public class DuplicateEntityException  extends RuntimeException{

    public DuplicateEntityException(String type, String attribute, Object value) {
        super(String.format("%s with %s %s already exists!", type, attribute, value));
    }
}
