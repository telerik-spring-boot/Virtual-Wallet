package com.telerik.virtualwallet.services.stock;

import com.telerik.virtualwallet.models.api.StockData;
import com.telerik.virtualwallet.models.api.StockResponse;

import java.util.List;
import java.util.Map;

public interface StockService {
    List<StockData> getStockPricesShort(List<String> symbols);

    Map<String, StockResponse> getStockPricesDetailed(List<String> symbols);

    Map<String, StockResponse> getStockPricesDetailed();
}
