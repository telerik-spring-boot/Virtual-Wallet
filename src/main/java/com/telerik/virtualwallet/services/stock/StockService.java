package com.telerik.virtualwallet.services.stock;

import com.telerik.virtualwallet.models.StockData;
import com.telerik.virtualwallet.models.StockResponse;

import java.util.List;
import java.util.Map;

public interface StockService {
    List<StockData> getStockPricesShort(List<String> symbols);

    Map<String, StockResponse> getStockPricesDetailed(List<String> symbols);
}
