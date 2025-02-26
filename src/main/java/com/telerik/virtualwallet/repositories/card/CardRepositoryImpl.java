package com.telerik.virtualwallet.repositories.card;

import com.telerik.virtualwallet.models.Card;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRepositoryImpl implements CardRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CardRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Card> getAllCards() {

        try (Session session = sessionFactory.openSession()) {
            Query<Card> query = session.createQuery("from Card", Card.class);
            return query.list();
        }

    }

    @Override
    public List<Card> getCardsByUserId(int userId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Card> query = session.createQuery("from Card where user.id = :userId", Card.class);
            query.setParameter("userId", userId);
            return query.list();
        }

    }

    @Override
    public Card getCardById(int id) {

        try (Session session = sessionFactory.openSession()) {
            return session.get(Card.class, id);
        }

    }

    @Override
    public void addCard(Card card) {

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(card);
            session.getTransaction().commit();
        }

    }

    @Override
    public void updateCard(Card card) {

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(card);
            session.getTransaction().commit();
        }

    }

    @Override
    public void deleteCard(int cardId) {

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Card card = session.get(Card.class, cardId);
            session.remove(card);

            session.getTransaction().commit();
        }

    }
}
