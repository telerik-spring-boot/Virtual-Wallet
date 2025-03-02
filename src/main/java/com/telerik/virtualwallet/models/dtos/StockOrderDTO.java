package com.telerik.virtualwallet.models.dtos;


public class StockOrderDTO {

    private String symbol;

    private int quantity;

    public StockOrderDTO(String symbol, int quantity) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
