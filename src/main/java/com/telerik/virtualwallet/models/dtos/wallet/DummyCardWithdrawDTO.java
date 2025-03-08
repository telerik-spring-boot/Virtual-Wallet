package com.telerik.virtualwallet.models.dtos.wallet;

import com.telerik.virtualwallet.models.enums.Currency;

import java.math.BigDecimal;

public class DummyCardWithdrawDTO {

    private String cardNumber;

    private BigDecimal amount;

    private Currency currency;

    public DummyCardWithdrawDTO() {
    }

    public DummyCardWithdrawDTO(String cardNumber, BigDecimal amount, Currency currency) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
