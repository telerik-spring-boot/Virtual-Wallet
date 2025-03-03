package com.telerik.virtualwallet.models.dtos.stock;


import java.time.LocalDateTime;

public class StockDisplayDTO {

    private LocalDateTime purchasedAt;

    private String stockSymbol;

    private double quantity;

    private double price;


    public StockDisplayDTO() {
    }

    public StockDisplayDTO(LocalDateTime purchasedAt, String stockSymbol, double quantity, double price) {
        this.purchasedAt = purchasedAt;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }
}
