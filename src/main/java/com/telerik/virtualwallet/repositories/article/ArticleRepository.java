package com.telerik.virtualwallet.repositories.article;

import com.telerik.virtualwallet.models.Article;

import java.util.List;

public interface ArticleRepository {

    void saveAll(List<Article> articles);

    List<Article> getAll();
}
