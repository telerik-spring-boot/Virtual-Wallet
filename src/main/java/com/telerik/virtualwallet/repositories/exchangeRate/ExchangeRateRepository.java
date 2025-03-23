package com.telerik.virtualwallet.repositories.exchangeRate;

import com.telerik.virtualwallet.models.ExchangeRate;

import java.util.List;

public interface ExchangeRateRepository {

    void saveAll(List<ExchangeRate> exchangeRates);

    List<ExchangeRate> getAll();
}
