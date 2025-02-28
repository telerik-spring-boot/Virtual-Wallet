package com.telerik.virtualwallet.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerik.virtualwallet.models.StockData;
import com.telerik.virtualwallet.models.StockPriceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService{

    private final String TWELVE_DATA_API_KEY = "5616a3a84fbe41c4b6daab6e5d61c00a";
    private final String BASE_URL = "https://api.twelvedata.com/price";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public StockServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<StockData> getStockPrices(List<String> symbols) {
        List<StockData> stocks= new ArrayList<>();

        String symbolsStr = String.join(",", symbols);

        String url = String.format("%s?symbol=%s&apikey=%s", BASE_URL, symbolsStr, TWELVE_DATA_API_KEY);

        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            if(symbols.size() == 1){

                Map<String, String> singleStockData = objectMapper.readValue(jsonResponse, new TypeReference<>() {});


                    if (singleStockData != null && singleStockData.containsKey("price")) {
                        double price = Double.parseDouble(singleStockData.get("price"));
                        stocks.add(new StockData(symbols.get(0), price));
                    }

            }else{
                Map<String, Map<String, String>> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

                for (String symbol : symbols) {
                    if (response.containsKey(symbol)) {
                        Map<String, String> stockData = response.get(symbol);
                        if (stockData != null && stockData.containsKey("price")) {
                            double price = Double.parseDouble(stockData.get("price"));
                            stocks.add(new StockData(symbol, price));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse stock data", e);
        }

        return stocks;
    }
}
