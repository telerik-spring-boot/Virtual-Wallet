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
    public List<Card> getCardsByUsername(String username) {

        try (Session session = sessionFactory.openSession()) {
            Query<Card> query = session.createQuery("from Card where user.username = :username", Card.class);
            query.setParameter("username", username);
            return query.list();
        }

    }

    @Override
    public Card getFirstCardCreatedByUsername(String username) {

        try (Session session = sessionFactory.openSession()) {
            Query<Card> query = session.createQuery(
                    "from Card where user.username = :username order by id asc", Card.class);
            query.setParameter("username", username);
            query.setMaxResults(1);
            return query.uniqueResult();
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

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(card);
            session.getTransaction().commit();
        }

    }

    @Override
    public void updateCard(Card card) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(card);
            session.getTransaction().commit();
        }

    }

    @Override
    public void deleteCard(int cardId) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Card card = session.get(Card.class, cardId);
            session.remove(card);

            session.getTransaction().commit();
        }

    }

    @Override
    public boolean isUserCardHolder(String username, int cardId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery
                    ("SELECT COUNT(c) FROM Card c WHERE c.id = :cardId AND c.user.username = :username ",
                            Long.class);

            query.setParameter("cardId", cardId);
            query.setParameter("username", username);

            Long count = query.uniqueResult();

            return count > 0;

        }
    }

    @Override
    public boolean isCardAlreadyAssignedToUser(String username, Card card) {

        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery
                    ("SELECT COUNT(c) FROM Card c WHERE c.number = :cardNumber " +
                                    "AND c.user.username = :username " +
                                    "AND c.id !=: cardId",
                            Long.class);

            query.setParameter("cardNumber", card.getNumber());
            query.setParameter("username", username);
            query.setParameter("cardId", card.getId());

            Long count = query.uniqueResult();

            return count > 0;

        }

    }
}
