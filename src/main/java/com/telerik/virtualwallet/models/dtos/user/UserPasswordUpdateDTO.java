package com.telerik.virtualwallet.models.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserPasswordUpdateDTO {

    @NotBlank(message = "Password is required.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,20}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number, one symbol, and be between 8 to 20 characters long.")
    private String password;

    @NotBlank(message = "Confirm password is required.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,20}$", message = "Confirm password must contain at least one uppercase letter, one lowercase letter, one number, one symbol, and be between 8 to 20 characters long.")
    private String confirmPassword;

    public UserPasswordUpdateDTO(String password, String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserPasswordUpdateDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
