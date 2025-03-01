package com.telerik.virtualwallet.models.filters;

import java.util.Optional;

public class FilterUserOptions{

    private final String username;
    private final String emailAddress;
    private final String phoneNumber;


    public FilterUserOptions(String username, String emailAddress, String phoneNumber) {
        this.username = username == null || username.isBlank() ? null : username;
        this.emailAddress = emailAddress == null || emailAddress.isBlank() ? null : emailAddress;
        this.phoneNumber = phoneNumber == null || phoneNumber.isBlank() ? null : phoneNumber;
    }



    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getPhoneNumber() { return Optional.ofNullable(phoneNumber); }

    public Optional<String> getEmailAddress() {
        return Optional.ofNullable(emailAddress);
    }
}
