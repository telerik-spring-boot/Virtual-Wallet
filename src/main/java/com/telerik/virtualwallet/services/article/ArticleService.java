package com.telerik.virtualwallet.services.article;

import com.telerik.virtualwallet.models.Article;

import java.util.List;

public interface ArticleService {

    void updateArticles();

    List<Article> getArticles();
}
