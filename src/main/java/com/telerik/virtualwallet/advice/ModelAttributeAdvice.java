package com.telerik.virtualwallet.advice;

import com.telerik.virtualwallet.models.ExchangeRate;
import com.telerik.virtualwallet.services.exchangeRate.ExchangeRateService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ModelAttributeAdvice {

    private final ExchangeRateService exchangeRateService;

    public ModelAttributeAdvice(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }


    @ModelAttribute("exchangeRates")
    public List<ExchangeRate> populateExchangeRates() {
        return exchangeRateService.getAllExchangeRates();
    }
}
