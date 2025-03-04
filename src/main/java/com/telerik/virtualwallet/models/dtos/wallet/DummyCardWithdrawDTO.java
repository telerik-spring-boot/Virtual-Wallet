package com.telerik.virtualwallet.models.dtos.wallet;

import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class DummyCardWithdrawDTO {

    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    private String cardNumber;

    private BigDecimal amount;

    public DummyCardWithdrawDTO() {
    }

    public DummyCardWithdrawDTO(String cardNumber, BigDecimal amount) {
        this.cardNumber = cardNumber;
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
