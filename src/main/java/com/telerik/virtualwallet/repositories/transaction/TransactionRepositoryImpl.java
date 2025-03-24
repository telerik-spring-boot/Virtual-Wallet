package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.enums.TransactionStatus;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                    "JOIN FETCH t.senderWallet sw " +
                                    "JOIN FETCH sw.users " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users",
                            Transaction.class);

            return query.list();
        }

    }

    @Override
    public List<Transaction> getAllTransactionsWithWalletsByUsername(String username) {

        try (Session session = sessionFactory.openSession()) {

            Query<Transaction> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transaction t " +
                                    "JOIN FETCH t.senderWallet sw " +
                                    "JOIN FETCH sw.users s " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE (s.username = :username OR r.username = :username)",
                            Transaction.class);

            query.setParameter("username", username);

            return query.list();
        }
    }

    @Override
    public List<Transaction> getIncomingTransactionsWithWalletsByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {

            Query<Transaction> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transaction t " +
                                    "JOIN FETCH t.senderWallet sw " +
                                    "JOIN FETCH sw.users s " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE  r.username = :username",
                            Transaction.class);

            query.setParameter("username", username);

            return query.list();
        }
    }

    @Override
    public List<Transaction> getOutgoingTransactionsWithWalletsByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {

            Query<Transaction> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transaction t " +
                                    "JOIN FETCH t.senderWallet sw " +
                                    "JOIN FETCH sw.users s " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE s.username = :username",
                            Transaction.class);

            query.setParameter("username", username);

            return query.list();
        }
    }

    @Override
    public List<Transaction> getAllTransactionsWithWalletsByWalletId(int walletId) {
        try (Session session = sessionFactory.openSession()) {

            Query<Transaction> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transaction t " +
                                    "JOIN FETCH t.senderWallet sw " +
                                    "JOIN FETCH sw.users s " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE (sw.id = :walletId OR rw.id = :walletId)",
                            Transaction.class);

            query.setParameter("walletId", walletId);

            return query.list();
        }
    }

    @Override
    public List<Transaction> getIncomingTransactionsWithWalletsByWalletId(int walletId) {
        try (Session session = sessionFactory.openSession()) {

            Query<Transaction> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transaction t " +
                                    "JOIN FETCH t.senderWallet sw " +
                                    "JOIN FETCH sw.users s " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE  rw.id = :walletId",
                            Transaction.class);

            query.setParameter("walletId", walletId);

            return query.list();
        }
    }

    @Override
    public List<Transaction> getOutgoingTransactionsWithWalletsByWalletId(int walletId) {
        try (Session session = sessionFactory.openSession()) {

            Query<Transaction> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transaction t " +
                                    "JOIN FETCH t.senderWallet sw " +
                                    "JOIN FETCH sw.users s " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE sw.id = :walletId",
                            Transaction.class);

            query.setParameter("walletId", walletId);

            return query.list();
        }
    }

    @Override
    public Page<Transaction> getAllTransactionsWithWallets(FilterTransactionsOptions options, Pageable pageable) {

        return getTransactionsWithFiltersHelper(options, pageable, -1,"");

    }

    @Override
    public Page<Transaction> getAllTransactionsWithWalletsByUsername(FilterTransactionsOptions options, Pageable pageable, String username) {
        return getTransactionsWithFiltersHelper(options, pageable, -1,username);
    }


    @Override
    public Transaction getTransactionWithWalletsById(int id) {

        try (Session session = sessionFactory.openSession()) {

            Query<Transaction> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transaction t " +
                                    "JOIN FETCH t.senderWallet sw " +
                                    "JOIN FETCH sw.users " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users " +
                                    "WHERE t.id=:id",
                            Transaction.class);
            query.setParameter("id", id);

            return query.uniqueResult();

        }

    }

    @Override
    public BigDecimal getBalanceChangeByWalletId(int walletId) {
        try (Session session = sessionFactory.openSession()) {

            Query<BigDecimal> query = session.createQuery(
                    "SELECT COALESCE(SUM(CASE WHEN t.receiverWallet.id = :walletId THEN t.amount ELSE 0 END), 0) " +
                            "- COALESCE(SUM(CASE WHEN t.senderWallet.id = :walletId THEN t.amount ELSE 0 END), 0) " +
                            "FROM Transaction t " +
                            "WHERE EXTRACT(MONTH FROM t.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) " +
                            "AND EXTRACT(YEAR FROM t.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)",
                    BigDecimal.class
            );
            query.setParameter("walletId", walletId);

            return query.uniqueResult();

        }
    }


    @Override
    public Page<Transaction> getAllTransactionsWithWalletsByWalletId(FilterTransactionsOptions options, Pageable pageable, int walletId) {

        return getTransactionsWithFiltersHelper(options, pageable, walletId,"");

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
                                    "JOIN t.senderWallet.users s " +
                                    "JOIN t.receiverWallet.users r " +
                                    "WHERE t.id=:transactionId " +
                                    "AND (s.username = :username OR r.username = :username)",
                            Long.class);

            query.setParameter("transactionId", transactionId);
            query.setParameter("username", username);

            Long count = query.uniqueResult();

            return count > 0;

        }
    }

    private PageImpl<Transaction> getTransactionsWithFiltersHelper(FilterTransactionsOptions options, Pageable pageable,
                                                                   int walletId, String username) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder("SELECT DISTINCT t FROM Transaction t " +
                    "JOIN FETCH t.senderWallet sw " +
                    "JOIN FETCH sw.users s " +
                    "JOIN FETCH t.receiverWallet rw " +
                    "JOIN FETCH rw.users r ");
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            options.getCurrency().ifPresent(value -> {
                filters.add("t.senderWallet.currency = :currency");
                params.put("currency", value);
            });

            options.getTransactionCategory().ifPresent(value -> {
                filters.add("t.transactionCategory.name = :transactionCategory");
                params.put("transactionCategory", value);
            });

            options.getStartTime().ifPresent(value -> {
                filters.add("t.createdAt >= :startTime");
                params.put("startTime", value);
            });

            options.getEndTime().ifPresent(value -> {
                filters.add("t.createdAt <= :endTime");
                params.put("endTime", value);
            });

            if(!username.isEmpty()){
                filters.add("(s.username = :username OR r.username = :username)");
                params.put("username", username);
            }

            if (walletId != -1) {

                options.getTransactionStatus().ifPresentOrElse(value -> {
                            if (value == TransactionStatus.INCOMING) {
                                filters.add("t.receiverWallet.id = :walletId");
                                params.put("walletId", walletId);
                            } else if (value == TransactionStatus.OUTGOING) {
                                filters.add("t.senderWallet.id = :walletId");
                                params.put("walletId", walletId);
                            }
                        },
                        () -> {
                            filters.add("(t.senderWallet.id = :walletId OR t.receiverWallet.id = :walletId)");
                            params.put("walletId", walletId);
                        });

            } else {
                options.getTransactionStatus().ifPresentOrElse(status -> {
                            if (status == TransactionStatus.INCOMING) {
                                options.getUsername().ifPresent(value -> {
                                    filters.add("r.username LIKE :receiverUsername");
                                    params.put("receiverUsername", "%" + value + "%");
                                });
                            } else if (status == TransactionStatus.OUTGOING) {
                                options.getUsername().ifPresent(value -> {
                                    filters.add("s.username LIKE :senderUsername");
                                    params.put("senderUsername", "%" + value + "%");
                                });
                            }
                        },
                        () ->
                            options.getUsername().ifPresent(value -> {
                                filters.add("(s.username LIKE :senderUsername OR r.username LIKE :receiverUsername)");
                                params.put("senderUsername", "%" + value + "%");
                                params.put("receiverUsername", "%" + value + "%");
                            })
                );

            }


            if (!filters.isEmpty()) {
                queryString.append(" WHERE ").append(String.join(" AND ", filters));
            }

            Query<Transaction> query = session.createQuery(queryString.toString(), Transaction.class);

            if (pageable.getSort().isSorted()) {
                Sort.Order order = pageable.getSort().iterator().next();
                queryString.append((" ORDER BY ")).append(order.getProperty()).append(" ").append(order.getDirection().name());
            }


            query.setProperties(params);

            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<Transaction> transactions = query.getResultList();

            String countQueryStr = "SELECT COUNT(DISTINCT t) FROM Transaction t " +
                    "JOIN t.senderWallet sw " +
                    "JOIN sw.users s " +
                    "JOIN t.receiverWallet rw " +
                    "JOIN rw.users r ";

            if (!filters.isEmpty()) {
                countQueryStr += " WHERE " + String.join(" AND ", filters);
            }
            Query<Long> countQuery = session.createQuery(countQueryStr, Long.class);
            countQuery.setProperties(params);

            long total = countQuery.getSingleResult();

            return new PageImpl<>(transactions, pageable, total);

        }
    }

}
