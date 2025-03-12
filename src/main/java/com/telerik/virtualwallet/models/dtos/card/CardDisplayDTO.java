package com.telerik.virtualwallet.models.dtos.card;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CardDisplayDTO {

    @JsonIgnore
    private int id;

    private String cardNumber;

    @JsonIgnore
    private String cardNumberFull;

    @JsonIgnore
    private String cvv;

    private String cardHolder;

    private String expiryMonth;

    private String expiryYear;

    private String type;

    public CardDisplayDTO() {
    }

    public CardDisplayDTO(int id, String cardNumber, String cardNumberFull, String cardHolder, String expiryMonth, String expiryYear, String type) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardNumberFull = cardNumberFull;
        this.cardHolder = cardHolder;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardNumberFull() {
        return cardNumberFull;
    }

    public void setCardNumberFull(String cardNumberFull) {
        this.cardNumberFull = cardNumberFull;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
