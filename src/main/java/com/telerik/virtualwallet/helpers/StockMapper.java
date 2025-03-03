package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.Stock;
import com.telerik.virtualwallet.models.dtos.StockDisplayDTO;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {


    public StockDisplayDTO stockToDto(Stock stock){

        StockDisplayDTO stockDisplayDTO = new StockDisplayDTO();

        stockDisplayDTO.setStockSymbol(stock.getStockSymbol());
        stockDisplayDTO.setPrice(stock.getPrice());
        stockDisplayDTO.setQuantity(stock.getQuantity());
        stockDisplayDTO.setPurchasedAt(stock.getPurchasedAt());

        return stockDisplayDTO;
    }
}
