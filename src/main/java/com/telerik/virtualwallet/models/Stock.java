package com.telerik.virtualwallet.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="purchased_at")
    private LocalDateTime purchasedAt;

    @Column(name="stock_symbol")
    private String stockSymbol;

    @Column(name="amount")
    private int amount;

    @Column(name="value")
    private double value;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User user;

    public Stock() {
    }

    public Stock(LocalDateTime purchasedAt, int amount, String stockSymbol, double value, User user) {
        this.purchasedAt = purchasedAt;
        this.amount = amount;
        this.stockSymbol = stockSymbol;
        this.value = value;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
