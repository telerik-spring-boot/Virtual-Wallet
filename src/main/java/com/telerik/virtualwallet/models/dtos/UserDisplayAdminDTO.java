package com.telerik.virtualwallet.models.dtos;

public class UserDisplayAdminDTO {

    private String username;

    private String emailAddress;

    private String phoneNumber;

    public UserDisplayAdminDTO() {
    }

    public UserDisplayAdminDTO(String username, String emailAddress, String phoneNumber) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
