package com.telerik.virtualwallet.models.dtos.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CardCreateDTO {

    @NotBlank(message = "Card number cannot be empty")
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 12 digits")
    private String cardNumber;

    @NotBlank(message = "Cardholder name cannot be empty")
    @Size(min = 2, max = 50, message = "Cardholder name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z]+([ '-][A-Za-z]+)*$",
            message = "Cardholder name can only contain letters, spaces, hyphens, and apostrophes")
    private String cardHolderName;

    @NotBlank(message = "Card cvv cannot be empty")
    @Pattern(regexp = "\\d{3}", message = "Card cvv must be exactly 3 digits")
    private String cvv;

    @NotBlank(message = "Expiry month cannot be empty")
    @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Expiry month must be between 01 and 12")
    private String expiryMonth;

    @NotBlank(message = "Expiry year cannot be empty")
    @Pattern(regexp = "\\d{2}", message = "Expiry year must be between 00 and 99")
    private String expiryYear;

    public CardCreateDTO() {
    }

    public CardCreateDTO(String cardNumber, String cardHolderName, String cvv, String expiryMonth, String expiryYear) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }
}
