package com.telerik.virtualwallet.models.dtos.user;

import jakarta.validation.constraints.NotBlank;

public class UserRetrieveDTO {

    @NotBlank(message = "Username is required.")
    private String username;

    public UserRetrieveDTO() {
    }

    public UserRetrieveDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
