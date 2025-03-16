package com.telerik.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockResponse {

    private List<StockValue> values;


    public StockResponse() {
    }


    public StockResponse(List<StockValue> values) {
        this.values = values;
    }



    public List<StockValue> getValues() {
        return values;
    }

    public void setValues(List<StockValue> values) {
        this.values = values;
    }

}