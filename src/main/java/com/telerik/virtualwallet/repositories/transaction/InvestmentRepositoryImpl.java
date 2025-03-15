package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Investment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvestmentRepositoryImpl  implements InvestmentRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public InvestmentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Investment> getAllInvestments() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("from Investment", Investment.class).list();
        }
    }
}
