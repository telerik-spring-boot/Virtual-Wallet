package com.telerik.virtualwallet.models.enums;

public enum Currency {
    USD("United States Dollar"),
    EUR("Euro"),
    GBP("British Pound");

    private final String description;

    Currency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
