package com.telerik.virtualwallet.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String type, String attribute, Object value) {
        super(String.format("%s with %s %s not found", type, attribute, value));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

}