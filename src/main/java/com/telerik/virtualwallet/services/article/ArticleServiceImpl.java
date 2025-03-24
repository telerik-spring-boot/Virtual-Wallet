package com.telerik.virtualwallet.services.article;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerik.virtualwallet.models.Article;
import com.telerik.virtualwallet.models.api.ArticleResponse;
import com.telerik.virtualwallet.repositories.article.ArticleRepository;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {


    private final String apiKey;
    private final String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final ArticleRepository articleRepository;


    public ArticleServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper,
                              Environment env, ArticleRepository articleRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiKey = env.getProperty("finnhub.api.key");
        this.baseUrl = env.getProperty("finnhub.base.url");
        this.articleRepository = articleRepository;
    }


    @Override
    @Scheduled(cron = "0 0 */6 * * *")
    public void updateArticles() {
        String url = String.format("%s" + "news?category=technology&token=%s", baseUrl, apiKey);

        String jsonString = restTemplate.getForObject(url, String.class);
        List<Article> articles = getArticles();
        try {
            List<ArticleResponse> articleResponses = objectMapper.readValue(jsonString, new TypeReference<>() {
            });

            for (int i = 0; i < articles.size(); i++) {
                articles.get(i).setDescription(articleResponses.get(i).getSummary());
                articles.get(i).setUrl(articleResponses.get(i).getUrl());
                articles.get(i).setTitle(articleResponses.get(i).getHeadline());
                articles.get(i).setPublishedOn(Instant.ofEpochSecond(articleResponses.get(i).getDatetime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while parsing news response.");
        }

        articleRepository.saveAll(articles);

    }

    @Override
    public List<Article> getArticles() {
        return articleRepository.getAll();
    }
}
