package com.telerik.virtualwallet.controllers;


import com.telerik.virtualwallet.models.StockData;
import com.telerik.virtualwallet.services.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @GetMapping("/stocks")
    public List<StockData> getStockPrices(@RequestParam List<String> symbols) {
        return stockService.getStockPrices(symbols);
    }
}
