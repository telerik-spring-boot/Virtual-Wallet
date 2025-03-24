package com.telerik.virtualwallet.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockData {

    private String symbol;

    @JsonProperty("price")
    private double price;

    public StockData(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public StockData() {}


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}