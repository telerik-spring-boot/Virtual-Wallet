package com.telerik.virtualwallet.controllers;


import com.telerik.virtualwallet.models.StockData;
import com.telerik.virtualwallet.models.StockResponse;
import com.telerik.virtualwallet.services.stock.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @GetMapping("/stocks")
    public List<StockData> getStockPrices(@RequestParam List<String> symbols) {
        return stockService.getStockPricesShort(symbols);
    }

    @GetMapping("/stocks-detailed")
    public Map<String, StockResponse> getStockPricesDetailed(@RequestParam List<String> symbols) {
        return stockService.getStockPricesDetailed(symbols);
    }
}
