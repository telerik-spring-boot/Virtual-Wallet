package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transaction;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> getAllTransactionsWithWallets();

    Transaction getTransactionWithWalletsById(int id);

    List<Transaction> getAllTransactionsWithWalletsByWalletId(int walletId);

    List<Transaction> getAllIncomingTransactionsWithWalletsByWalletId(int walletReceiverId);

    List<Transaction> getAllOutgoingTransactionsWithWalletsByWalletId(int walletSenderId);

    void createTransaction(Transaction transaction);

    boolean isUserTransactionParticipant(String username, int transactionId);
}
