package com.telerik.virtualwallet.models.dtos;


public class StockOrderDTO {

    private String symbol;

    private double quantity;

    public StockOrderDTO(String symbol, double quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public StockOrderDTO() {}


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
}
