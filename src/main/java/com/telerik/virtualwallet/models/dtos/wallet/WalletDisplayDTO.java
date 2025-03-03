package com.telerik.virtualwallet.models.dtos.wallet;

import com.telerik.virtualwallet.models.enums.Currency;

import java.math.BigDecimal;

public class WalletDisplayDTO {

    public BigDecimal balance;

    public Currency currency;

    public WalletDisplayDTO(BigDecimal balance, Currency currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public WalletDisplayDTO() {
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
