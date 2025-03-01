package com.telerik.virtualwallet.models.dtos;

public class UserDisplayUserDTO {

    private String username;

    public UserDisplayUserDTO(String username) {
        this.username = username;
    }

    public UserDisplayUserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
