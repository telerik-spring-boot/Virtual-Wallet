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
    private int quantity;

    @Column(name="value")
    private double price;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User user;

    public Stock() {
    }

    public Stock(LocalDateTime purchasedAt, int quantity, String stockSymbol, double price, User user) {
        this.purchasedAt = purchasedAt;
        this.quantity = quantity;
        this.stockSymbol = stockSymbol;
        this.price = price;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
