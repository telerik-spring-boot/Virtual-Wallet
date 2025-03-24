package com.telerik.virtualwallet.repositories.article;

import com.telerik.virtualwallet.models.Article;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepositoryImpl implements ArticleRepository{
    private final SessionFactory sessionFactory;

    public ArticleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveAll(List<Article> articles) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            for (Article article : articles) {
                session.merge(article);
            }

            transaction.commit();
        }
    }

    @Override
    public List<Article> getAll() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("from Article", Article.class).list();
        }
    }
}
