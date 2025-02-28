package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transaction;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(int id);

    List<Transaction> getAllTransactionsByWalletId(int walletId);

    List<Transaction> getAllIncomingTransactionsByWalletId(int walletReceiverId);

    List<Transaction> getAllOutgoingTransactionsByWalletId(int walletSenderId);

    void createTransaction(Transaction transaction);
}
