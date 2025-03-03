package com.telerik.virtualwallet.services.stock;

import com.telerik.virtualwallet.models.StockData;

import java.util.List;

public interface StockService {
    List<StockData> getStockPrices(List<String> symbols);
}
