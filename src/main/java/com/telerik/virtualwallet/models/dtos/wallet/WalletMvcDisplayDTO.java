package com.telerik.virtualwallet.models.dtos.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayForWalletDTO;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayMvcDTO;
import com.telerik.virtualwallet.models.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

public class WalletMvcDisplayDTO {

    public List<UserDisplayForWalletDTO> walletHolders;

    public BigDecimal balance;

    public String currency;

    public int id;

    public WalletMvcDisplayDTO() {
    }

    public WalletMvcDisplayDTO(List<UserDisplayForWalletDTO> walletHolders, BigDecimal balance, String currency, int id) {
        this.walletHolders = walletHolders;
        this.balance = balance;
        this.currency = currency;
        this.id = id;
    }

    public List<UserDisplayForWalletDTO> getWalletHolders() {
        return walletHolders;
    }

    public void setWalletHolders(List<UserDisplayForWalletDTO> walletHolders) {
        this.walletHolders = walletHolders;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
