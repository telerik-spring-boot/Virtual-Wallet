package com.telerik.virtualwallet.models.dtos.wallet;

import com.telerik.virtualwallet.models.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

public class WalletPublicDisplayDTO {

    public List<String> walletHolders;

    public Currency currency;

    public WalletPublicDisplayDTO(List<String> walletHolders, Currency currency) {
        this.walletHolders = walletHolders;
        this.currency = currency;
    }

    public WalletPublicDisplayDTO() {
    }

    public List<String> getWalletHolders() {
        return walletHolders;
    }

    public void setWalletHolders(List<String> walletHolders) {
        this.walletHolders = walletHolders;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
