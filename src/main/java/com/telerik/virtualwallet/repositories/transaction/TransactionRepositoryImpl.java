package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Transaction> getAllTransactions() {

        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery("from Transaction", Transaction.class);
            return query.list();
        }

    }

    @Override
    public Transaction getTransactionById(int id) {

        try (Session session = sessionFactory.openSession()) {
            return session.get(Transaction.class, id);
        }

    }

    @Override
    public List<Transaction> getAllIncomingTransactionsByWalletId(int walletReceiverId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery(
                    "FROM Transaction t WHERE t.receiverWallet.id = :walletReceiverId",
                    Transaction.class
            );
            query.setParameter("walletReceiverId", walletReceiverId);
            return query.list();
        }

    }

    @Override
    public List<Transaction> getAllOutgoingTransactionsByWalletId(int walletSenderId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery(
                    "FROM Transaction t WHERE t.senderWallet.id = :walletSenderId",
                    Transaction.class
            );
            query.setParameter("walletSenderId", walletSenderId);
            return query.list();
        }

    }

    @Override
    public void createTransaction(Transaction transaction) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(transaction);
            session.getTransaction().commit();
        }

    }
}
