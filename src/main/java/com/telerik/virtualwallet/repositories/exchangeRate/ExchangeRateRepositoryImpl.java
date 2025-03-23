package com.telerik.virtualwallet.repositories.exchangeRate;

import com.telerik.virtualwallet.models.ExchangeRate;
import com.telerik.virtualwallet.models.Investment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {


    private final SessionFactory sessionFactory;

    public ExchangeRateRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveAll(List<ExchangeRate> exchangeRates) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            for (ExchangeRate rate : exchangeRates) {
                session.merge(rate);
            }

            transaction.commit();
        }
    }

    @Override
    public List<ExchangeRate> getAll() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("from ExchangeRate", ExchangeRate.class).list();
        }
    }
}
