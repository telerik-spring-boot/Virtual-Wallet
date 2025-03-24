package com.telerik.virtualwallet.advice;

import com.telerik.virtualwallet.models.Article;
import com.telerik.virtualwallet.models.ExchangeRate;
import com.telerik.virtualwallet.models.api.ArticleResponse;
import com.telerik.virtualwallet.services.exchangeRate.ExchangeRateService;
import com.telerik.virtualwallet.services.article.ArticleService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class ModelAttributeAdvice {

    private final ExchangeRateService exchangeRateService;
    private final ArticleService articleService;

    public ModelAttributeAdvice(ExchangeRateService exchangeRateService, ArticleService articleService) {
        this.exchangeRateService = exchangeRateService;
        this.articleService = articleService;
    }


    @ModelAttribute("exchangeRates")
    public List<ExchangeRate> populateExchangeRates() {
        return exchangeRateService.getAllExchangeRates();
    }

    @ModelAttribute("articles")
    public List<Article> populateNews() {
        return articleService.getArticles();
    }
}
