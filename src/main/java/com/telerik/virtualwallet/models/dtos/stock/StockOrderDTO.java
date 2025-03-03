package com.telerik.virtualwallet.models.dtos.stock;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StockOrderDTO {

    @NotBlank
    private String symbol;

    @NotNull
    @Min(0)
    private Double quantity;

    public StockOrderDTO(String symbol, Double quantity) {
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
