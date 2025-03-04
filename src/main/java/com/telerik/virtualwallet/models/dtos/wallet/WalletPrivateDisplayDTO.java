package com.telerik.virtualwallet.models.dtos.wallet;

import com.telerik.virtualwallet.models.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

public class WalletPrivateDisplayDTO {

    public List<String> walletHolders;

    public BigDecimal balance;

    public Currency currency;

    public WalletPrivateDisplayDTO(List<String> walletHolders, BigDecimal balance, Currency currency) {
        this.walletHolders = walletHolders;
        this.balance = balance;
        this.currency = currency;
    }

    public WalletPrivateDisplayDTO() {
    }

    public List<String> getWalletHolders() {
        return walletHolders;
    }

    public void setWalletHolders(List<String> walletHolders) {
        this.walletHolders = walletHolders;
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
