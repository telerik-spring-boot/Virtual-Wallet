package com.telerik.virtualwallet.models.dtos.transaction;


import java.time.LocalDateTime;
import java.util.List;

public class InvestmentDTO {

    private int id;

    private String username;

    private LocalDateTime purchasedAt;

    private String symbol;

    private double quantity;

    private double value;

    private double totalOrderValue;

    private String type;

    public InvestmentDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getTotalOrderValue() {
        return totalOrderValue;
    }

    public void setTotalOrderValue(double totalOrderValue) {
        this.totalOrderValue = totalOrderValue;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
