package com.telerik.virtualwallet.models.filters;

import java.util.Optional;

public class FilterUserOptions implements Sortable{

    private final String username;
    private final String emailAddress;
    private final String phoneNumber;
    private final String sortBy;
    private final String sortOrder;


    public FilterUserOptions(String username, String emailAddress, String phoneNumber, String sortBy, String sortOrder) {
        this.username = username == null || username.isBlank() ? null : username;
        this.emailAddress = emailAddress == null || emailAddress.isBlank() ? null : emailAddress;
        this.phoneNumber = phoneNumber == null || phoneNumber.isBlank() ? null : phoneNumber;
        this.sortBy = sortBy == null || sortBy.isBlank() ? null : sortBy;
        this.sortOrder = sortOrder == null || sortOrder.isBlank() ? null : sortOrder;
    }



    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getPhoneNumber() { return Optional.ofNullable(phoneNumber); }

    public Optional<String> getEmailAddress() {
        return Optional.ofNullable(emailAddress);
    }

    public Optional<String> getSortBy() {
        return Optional.ofNullable(sortBy);
    }

    public Optional<String> getSortOrder() {
        return Optional.ofNullable(sortOrder);
    }
}
