package com.telerik.virtualwallet.repositories.wallet;

import com.telerik.virtualwallet.models.Wallet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WalletRepositoryImpl implements WalletRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public WalletRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Wallet> getAllWallets() {

        try (Session session = sessionFactory.openSession()) {
            Query<Wallet> query = session.createQuery("from Wallet", Wallet.class);
            return query.list();
        }

    }

    @Override
    public Wallet getWalletbyId(String id) {

        try (Session session = sessionFactory.openSession()) {
            return session.get(Wallet.class, id);
        }
    }

    @Override
    public List<Wallet> getWalletsByUserId(int userId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Wallet> query = session.createQuery(
                    "from Wallet w join w.users u where u.id = :userId", Wallet.class);
            query.setParameter("userId", userId);
            return query.list();
        }

    }

    @Override
    public void addWallet(Wallet wallet) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(wallet);
            session.getTransaction().commit();
        }

    }

    @Override
    public void updateWallet(Wallet wallet) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(wallet);
            session.getTransaction().commit();
        }

    }

    @Override
    public void deleteWallet(String id) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Wallet wallet = session.get(Wallet.class, id);
            session.remove(wallet);

            session.getTransaction().commit();
        }

    }
}
