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
    public List<Transaction> getAllTransactionsWithWallets() {

        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transaction t " +
                                    "JOIN FETCH t.senderWallet " +
                                    "JOIN FETCH t.receiverWallet r " +
                                    "LEFT JOIN FETCH r.users " +
                                    "JOIN FETCH t.transactionCategory " +
                                    "JOIN FETCH t.userSender",
                            Transaction.class);
            return query.list();
        }

    }

    @Override
    public Transaction getTransactionWithWalletsById(int id) {

        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transaction t " +
                                    "JOIN FETCH t.senderWallet " +
                                    "JOIN FETCH t.receiverWallet r " +
                                    "LEFT JOIN FETCH r.users " +
                                    "JOIN FETCH t.transactionCategory " +
                                    "JOIN FETCH t.userSender " +
                                    "WHERE t.id=:id",
                            Transaction.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        }

    }

    @Override
    public List<Transaction> getAllTransactionsWithWalletsByWalletId(int walletId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery(
                    "SELECT DISTINCT t FROM Transaction t " +
                            "JOIN FETCH t.senderWallet " +
                            "JOIN FETCH t.receiverWallet r " +
                            "LEFT JOIN FETCH r.users " +
                            "JOIN FETCH t.transactionCategory " +
                            "JOIN FETCH t.userSender " +
                            "WHERE t.receiverWallet.id = :walletId OR t.senderWallet.id = :walletId",
                    Transaction.class);
            query.setParameter("walletId", walletId);
            return query.list();
        }

    }

    @Override
    public List<Transaction> getAllIncomingTransactionsWithWalletsByWalletId(int walletReceiverId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery(
                    "SELECT DISTINCT t FROM Transaction t " +
                            "JOIN FETCH t.senderWallet " +
                            "JOIN FETCH t.receiverWallet r " +
                            "LEFT JOIN FETCH r.users " +
                            "JOIN FETCH t.transactionCategory " +
                            "JOIN FETCH t.userSender " +
                            "WHERE t.receiverWallet.id = :walletId",
                    Transaction.class);
            query.setParameter("walletId", walletReceiverId);
            return query.list();
        }

    }

    @Override
    public List<Transaction> getAllOutgoingTransactionsWithWalletsByWalletId(int walletSenderId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> query = session.createQuery(
                    "SELECT DISTINCT t FROM Transaction t " +
                            "JOIN FETCH t.senderWallet " +
                            "JOIN FETCH t.receiverWallet r " +
                            "LEFT JOIN FETCH r.users " +
                            "JOIN FETCH t.transactionCategory " +
                            "JOIN FETCH t.userSender " +
                            "WHERE t.senderWallet.id = :walletId",
                    Transaction.class);
            query.setParameter("walletId", walletSenderId);
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

    @Override
    public boolean isUserTransactionParticipant(String username, int transactionId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery
                    ("SELECT COUNT(t) FROM Transaction t " +
                                    "JOIN t.senderWallet s " +
                                    "JOIN t.receiverWallet r " +
                                    "LEFT JOIN s.users u1 " +
                                    "LEFT JOIN r.users u2 " +
                                    "WHERE t.id=:transactionId " +
                                    "AND (u1.username = :username OR u2.username = :username)",
                            Long.class);

            query.setParameter("transactionId", transactionId);
            query.setParameter("username", username);

            Long count = query.uniqueResult();

            return count > 0;

        }
    }

}
