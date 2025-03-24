package com.telerik.virtualwallet.services.exchangeRate;

import com.telerik.virtualwallet.models.ExchangeRate;
import com.telerik.virtualwallet.repositories.exchangeRate.ExchangeRateRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final String apiKey;
    private final String baseUrl;


    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;

    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository, Environment env, RestTemplate restTemplate) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.restTemplate = restTemplate;
        this.apiKey = env.getProperty("twelve.data.api.key");
        this.baseUrl = env.getProperty("twelve.data.base.url");
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExchangeRates() {

        String symbolsStr= "EUR/USD,GBP/USD,EUR/GBP";

        String url = String.format("%s" +"price?symbol=%s&apikey=%s", baseUrl, symbolsStr, apiKey);

        ResponseEntity<Map<String, Map<String, String>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Map<String, String>> exchangeRates = response.getBody();
        Objects.requireNonNull(exchangeRates, "Exchange rates map must not be null");

        List<ExchangeRate> exchangeRatesOld = exchangeRateRepository.getAll();

        for(ExchangeRate exchangeRate : exchangeRatesOld){
            exchangeRate.setRate(Double.parseDouble(exchangeRates.get(String.format("%s/%s", exchangeRate.getFromCurrency(), exchangeRate.getToCurrency())).get("price")));
            exchangeRate.setUpdatedAt(LocalDateTime.now());
        }

        exchangeRateRepository.saveAll(exchangeRatesOld);

    }




    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.getAll();
    }
}
