package com.telerik.virtualwallet.models.dtos.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateMvcDTO {

    @Size(min=2, max =20, message = "Full name must be between 2 and 20 characters.")
    @Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+)*$", message = "Full name must contain only letters and spaces (one space between each word).")
    private String fullName;

    @Size(min = 5, max = 254, message = "Email must be between 5 and 254 symbols.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email must be valid.")
    private String emailAddress;

    @Pattern(regexp = "^\\+\\d{10}$", message = "Phone number must start with '+' followed by exactly 10 digits.")
    private String phoneNumber;

    public UserUpdateMvcDTO(){}

    public UserUpdateMvcDTO(String fullName, String emailAddress, String phoneNumber){
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
