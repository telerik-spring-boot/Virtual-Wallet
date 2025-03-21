package com.telerik.virtualwallet.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ContactDTO {

    @NotBlank(message = "Name is required.")
    @Size(min=2, max =20, message = "Name must be between 2 and 20 characters.")
    @Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+)*$", message = "Name must contain only letters and spaces (one space between each word).")
    private String name;

    @Size(min = 5, max = 254, message = "Email must be between 5 and 254 symbols.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Message is required.")
    @Size(min = 2, max = 512, message = "Message must be between 2 and 512 symbols.")
    private String message;

    public ContactDTO() {
    }

    public ContactDTO(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
