package com.telerik.virtualwallet.services.exchangeRate;

import com.telerik.virtualwallet.models.ExchangeRate;

import java.util.List;

public interface ExchangeRateService {

    void updateExchangeRates();

    List<ExchangeRate> getAllExchangeRates();

}
