package com.telerik.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerik.virtualwallet.models.enums.Currency;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private int id;

    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;

    @Column(name="is_main_wallet")
    private boolean isMainWallet;


    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "users_wallets",
            joinColumns = @JoinColumn(name = "wallet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    public Wallet() {
    }

    public Wallet(int id, BigDecimal balance, boolean isMainWallet, Currency currency, Set<User> users) {
        this.id = id;
        this.balance = balance;
        this.isMainWallet = isMainWallet;
        this.currency = currency;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean isMainWallet() {
        return isMainWallet;
    }

    public void setMainWallet(boolean mainWallet) {
        isMainWallet = mainWallet;
    }
}
