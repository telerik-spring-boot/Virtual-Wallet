package com.telerik.virtualwallet.repositories.transactionCategory;

import com.telerik.virtualwallet.models.TransactionCategory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionCategoryRepositoryImpl implements TransactionCategoryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TransactionCategoryRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TransactionCategory> getTransactionCategories() {

        try (Session session = sessionFactory.openSession()) {
            Query<TransactionCategory> query = session.createQuery("from TransactionCategory", TransactionCategory.class);
            return query.list();
        }

    }

    @Override
    public TransactionCategory getTransactionCategory(int id) {

        try (Session session = sessionFactory.openSession()) {
            return session.get(TransactionCategory.class, id);
        }

    }
}
