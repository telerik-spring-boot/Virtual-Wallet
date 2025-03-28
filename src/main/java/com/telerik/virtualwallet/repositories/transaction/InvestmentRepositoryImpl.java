package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Investment;
import com.telerik.virtualwallet.models.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvestmentRepositoryImpl implements InvestmentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public InvestmentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Investment> getAllInvestments() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Investment", Investment.class).list();
        }
    }

    @Override
    public List<Investment> getAllInvestmentsByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {

            Query<Investment> query = session.createQuery
                    ("FROM Investment i " +
                                    "WHERE i.user.username = :username",
                            Investment.class);

            query.setParameter("username", username);

            return query.list();
        }
    }
}
