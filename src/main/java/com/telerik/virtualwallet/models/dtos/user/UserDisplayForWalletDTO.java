package com.telerik.virtualwallet.models.dtos.user;

public class UserDisplayForWalletDTO {

    private String username;

    private String fullName;

    public UserDisplayForWalletDTO() {
    }

    public UserDisplayForWalletDTO(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
