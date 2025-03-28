package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository {

    Page<Transaction> getAllTransactionsWithWallets(FilterTransactionsOptions options, Pageable pageable);

    Page<Transaction> getAllTransactionsWithWalletsByUsername(FilterTransactionsOptions options, Pageable pageable, String username);

    List<Transaction> getAllTransactionsWithWallets();

    List<Transaction> getAllTransactionsWithWalletsByUsername(String username);

    List<Transaction> getIncomingTransactionsWithWalletsByUsername(String username);

    List<Transaction> getOutgoingTransactionsWithWalletsByUsername(String username);

    List<Transaction> getAllTransactionsWithWalletsByWalletId(int walletId);

    List<Transaction> getIncomingTransactionsWithWalletsByWalletId(int walletId);

    List<Transaction> getOutgoingTransactionsWithWalletsByWalletId(int walletId);

    Transaction getTransactionWithWalletsById(int id);

    BigDecimal getBalanceChangeByWalletId(int walletId);

    List<Transaction> getIncomingTransactionsForLastYearByWalletId(int walletId, LocalDateTime startDate);

    List<Transaction> getOutgoingTransactionsForLastYearByWalletId(int walletId,LocalDateTime startDate);

    Page<Transaction> getAllTransactionsWithWalletsByWalletId(FilterTransactionsOptions options, Pageable pageable, int walletId);

    void createTransaction(Transaction transaction);

    boolean isUserTransactionParticipant(String username, int transactionId);
}
