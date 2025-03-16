package com.telerik.virtualwallet.services.stock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerik.virtualwallet.models.StockData;
import com.telerik.virtualwallet.models.StockResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService{

    private static final int SINGLE_SYMBOL = 1;

    private final String apiKey;
    private final String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public StockServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper, Environment env) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiKey = env.getProperty("twelve.data.api.key");
        this.baseUrl = env.getProperty("twelve.data.base.url");
    }

    @Override
    public List<StockData> getStockPricesShort(List<String> symbols) {
        List<StockData> stocks= new ArrayList<>();

        String symbolsStr = String.join(",", symbols);

        String url = String.format("%s" +"price?symbol=%s&apikey=%s", baseUrl, symbolsStr, apiKey);

        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            if(symbols.size() == SINGLE_SYMBOL){

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


    @Override
    public  Map<String, StockResponse>  getStockPricesDetailed(List<String> symbols) {

        String symbolsStr = String.join(",", symbols);

        String url = String.format("%s" +"time_series?symbol=%s&interval=5min&outputsize=100&apikey=%s", baseUrl, symbolsStr, apiKey);

        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {

            return objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        }catch(Exception e){
            throw new RuntimeException("Failed to get stock data", e);
        }

    }

    @Override
    public Map<String, StockResponse> getStockPricesDetailed() {

        return getStockPricesDetailed(List.of("AAPL,TSLA"));
    }
}
