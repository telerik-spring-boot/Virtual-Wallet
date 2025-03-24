package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.filters.FilterTransferOptions;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransferRepositoryImpl implements TransferRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TransferRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Page<Transfer> getAllTransfers(FilterTransferOptions options, Pageable pageable) {
        return getTransfersWithFiltersHelper(options, pageable, -1, "");
    }

    @Override
    public Page<Transfer> getAllTransfersMadeByUserByUsername(FilterTransferOptions options, Pageable pageable, String username) {
        return getTransfersWithFiltersHelper(options, pageable, -1, username);
    }

    @Override
    public Page<Transfer> getAllTransfersByWalletId(FilterTransferOptions options, Pageable pageable, int walletId) {
        return getTransfersWithFiltersHelper(options, pageable, walletId, "");
    }

    @Override
    public List<Transfer> getAllTransfersByWalletId(int walletId) {
        try (Session session = sessionFactory.openSession()) {

            Query<Transfer> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transfer t " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE rw.id =: walletId",
                            Transfer.class);
            query.setParameter("walletId", walletId);

            return query.list();

        }
    }

    @Override
    public List<Transfer> getAllTransfersMvc() {
        try (Session session = sessionFactory.openSession()) {

            Query<Transfer> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transfer t " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r ",
                            Transfer.class);

            return query.list();

        }
    }

    @Override
    public List<Transfer> getAllTransfersMadeByUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {

            Query<Transfer> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transfer t " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE t.senderCard.user.username = :username",
                            Transfer.class);
            query.setParameter("username", username);

            return query.list();

        }
    }

    @Override
    public List<Transfer> getAllTransfersToYourWalletsByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {

            Query<Transfer> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transfer t " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE r.username = :username",
                            Transfer.class);
            query.setParameter("username", username);

            return query.list();

        }
    }

    @Override
    public Transfer getTransferById(int id) {

        try (Session session = sessionFactory.openSession()) {

            Query<Transfer> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transfer t " +
                                    "JOIN FETCH t.receiverWallet rw " +
                                    "JOIN FETCH rw.users r " +
                                    "WHERE t.id=:id",
                            Transfer.class);
            query.setParameter("id", id);

            return query.uniqueResult();

        }
    }

    @Override
    public BigDecimal getBalanceChangeByWalletId(int walletId) {
        try (Session session = sessionFactory.openSession()) {

            Query<BigDecimal> query = session.createQuery(
                    "SELECT COALESCE(SUM(CASE WHEN t.receiverWallet.id = :walletId THEN t.amount ELSE 0 END), 0) " +
                            "FROM Transfer t " +
                            "WHERE EXTRACT(MONTH FROM t.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) " +
                            "AND EXTRACT(YEAR FROM t.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)",
                    BigDecimal.class
            );
            query.setParameter("walletId", walletId);

            return query.uniqueResult();

        }
    }

    @Override
    public BigDecimal getBalanceChangeByWalletAndCardId(int walletId, int cardId) {

        try (Session session = sessionFactory.openSession()) {

            Query<BigDecimal> query = session.createQuery(
                    "SELECT COALESCE(SUM(CASE WHEN (t.receiverWallet.id = :walletId AND t.senderCard.id = :cardId)" +
                            " THEN t.amount ELSE 0 END), 0) " +
                            "FROM Transfer t " +
                            "WHERE EXTRACT(MONTH FROM t.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) " +
                            "AND EXTRACT(YEAR FROM t.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)",
                    BigDecimal.class
            );
            query.setParameter("walletId", walletId);
            query.setParameter("cardId", cardId);

            return query.uniqueResult();

        }
    }

    @Override
    public List<Transfer> getTransfersForLastYearByWalletId(int walletId, LocalDateTime startOfMonth11MonthsAgo) {

        try (Session session = sessionFactory.openSession()) {

            Query<Transfer> query = session.createQuery
                    ("SELECT DISTINCT t FROM Transfer t " +
                                    "WHERE t.receiverWallet.id=:walletId " +
                                    "AND t.createdAt >= :startOfMonth11MonthsAgo",
                            Transfer.class);

            query.setParameter("walletId", walletId);
            query.setParameter("startOfMonth11MonthsAgo", startOfMonth11MonthsAgo);

            return query.list();

        }
    }

    @Override
    public void createTransfer(Transfer transfer) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(transfer);
            session.getTransaction().commit();
        }

    }

    @Override
    public boolean isUserTransferReceiver(int transferId, String username) {

        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery
                    ("SELECT COUNT(t) FROM Transfer t " +
                                    "JOIN t.receiverWallet.users r " +
                                    "WHERE t.id=:transferId " +
                                    "AND r.username = :username",
                            Long.class);

            query.setParameter("transferId", transferId);
            query.setParameter("username", username);

            Long count = query.uniqueResult();

            return count > 0;

        }
    }

    private PageImpl<Transfer> getTransfersWithFiltersHelper(FilterTransferOptions options, Pageable pageable,
                                                             int walletId, String username) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder("SELECT DISTINCT tr FROM Transfer tr " +
                    "JOIN FETCH tr.receiverWallet rw " +
                    "JOIN FETCH rw.users r ");
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            options.getCurrency().ifPresent(value -> {
                filters.add("rw.currency = :currency");
                params.put("currency", value);
            });

            options.getStartTime().ifPresent(value -> {
                filters.add("tr.createdAt >= :startTime");
                params.put("startTime", value);
            });

            options.getEndTime().ifPresent(value -> {
                filters.add("tr.createdAt <= :endTime");
                params.put("endTime", value);
            });

            if (!username.isEmpty()) {
                filters.add("tr.senderCard.user.username = :username");
                params.put("username", username);
            }

            if (walletId != -1) {

                filters.add("rw.id = :walletId");
                params.put("walletId", walletId);

            }

            if (!filters.isEmpty()) {
                queryString.append(" WHERE ").append(String.join(" AND ", filters));
            }

            Query<Transfer> query = session.createQuery(queryString.toString(), Transfer.class);

            if (pageable.getSort().isSorted()) {
                Sort.Order order = pageable.getSort().iterator().next();
                queryString.append((" ORDER BY ")).append(order.getProperty()).append(" ").append(order.getDirection().name());
            }

            query.setProperties(params);

            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<Transfer> transfers = query.getResultList();

            String countQueryStr = "SELECT COUNT(DISTINCT tr) FROM Transfer tr " +
                    "JOIN tr.receiverWallet rw " +
                    "JOIN rw.users r ";

            if (!filters.isEmpty()) {
                countQueryStr += " WHERE " + String.join(" AND ", filters);
            }
            Query<Long> countQuery = session.createQuery(countQueryStr, Long.class);
            countQuery.setProperties(params);

            long total = countQuery.getSingleResult();

            return new PageImpl<>(transfers, pageable, total);

        }
    }
}
