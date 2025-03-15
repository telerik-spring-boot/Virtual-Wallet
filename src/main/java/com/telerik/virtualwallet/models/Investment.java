package com.telerik.virtualwallet.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "investments")
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt;

    @Column(name = "symbols")
    private String symbols;

    @Column(name = "quantities")
    private String quantities;

    @Column(name = "stock_values")
    private String stockValues;

    @Column(name = "total_value")
    private double totalValue;

    @Column(name = "type")
    private String type;

    public Investment() {
    }

    public Investment(User user, LocalDateTime purchasedAt, String symbols, String quantities, String stockValues, double totalValue, String type) {
        this.user = user;
        this.purchasedAt = purchasedAt;
        this.symbols = symbols;
        this.quantities = quantities;
        this.stockValues = stockValues;
        this.totalValue = totalValue;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public String getStockValues() {
        return stockValues;
    }

    public void setStockValues(String stockValues) {
        this.stockValues = stockValues;
    }

    public String getQuantities() {
        return quantities;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
