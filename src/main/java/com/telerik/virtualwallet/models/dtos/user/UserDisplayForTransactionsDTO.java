package com.telerik.virtualwallet.models.dtos.user;

public class UserDisplayForTransactionsDTO {

    private String username;

    public UserDisplayForTransactionsDTO(String username) {
        this.username = username;
    }

    public UserDisplayForTransactionsDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
