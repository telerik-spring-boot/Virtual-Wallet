package com.telerik.virtualwallet.models.dtos.stock;

import java.util.ArrayList;
import java.util.List;

public class StockOrderMvcDTO {

    private List<Double> quantities = new ArrayList<>();
    private List<Integer> directions = new ArrayList<>();
    private List<String> symbols = new ArrayList<>();
    private List<Double> prices = new ArrayList<>();

    public StockOrderMvcDTO() {
    }

    public StockOrderMvcDTO(List<Double> quantities, List<Integer> directions, List<String> symbols, List<Double> prices) {
        this.quantities = quantities;
        this.directions = directions;
        this.symbols = symbols;
        this.prices = prices;
    }

    public List<Double> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Double> quantities) {
        this.quantities = quantities;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public List<Integer> getDirections() {
        return directions;
    }

    public void setDirections(List<Integer> directions) {
        this.directions = directions;
    }

    public List<Double> getPrices() {
        return prices;
    }

    public void setPrices(List<Double> prices) {
        this.prices = prices;
    }
}
