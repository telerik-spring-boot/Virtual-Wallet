package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionRepository {

    Page<Transaction> getAllTransactionsWithWallets(FilterTransactionsOptions options, Pageable pageable);

    Page<Transaction> getAllTransactionsWithWalletsByUsername(FilterTransactionsOptions options, Pageable pageable, String username);

    List<Transaction> getAllTransactionsWithWallets();

    List<Transaction> getAllTransactionsWithWalletsByUsername(String username);

    Transaction getTransactionWithWalletsById(int id);

    Page<Transaction> getAllTransactionsWithWalletsByWalletId(FilterTransactionsOptions options, Pageable pageable, int walletId);

    void createTransaction(Transaction transaction);

    boolean isUserTransactionParticipant(String username, int transactionId);
}
