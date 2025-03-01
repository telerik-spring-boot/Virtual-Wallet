package com.telerik.virtualwallet.models.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,20}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number, one symbol, and be between 8 to 20 characters long.")
    private String password;

    @Size(min = 5, max = 254, message = "Email must be between 5 and 254 symbols.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email must be valid.")
    private String emailAddress;

    @Pattern(regexp = "^\\+\\d{10}$", message = "Phone number must start with '+' followed by exactly 10 digits.")
    private String phoneNumber;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String password, String phoneNumber, String emailAddress) {
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
