package com.telerik.virtualwallet.models.enums;

public enum TransactionStatus {
    INCOMING("Incoming"),
    OUTGOING("Outgoing");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
