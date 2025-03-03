package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.models.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(int id);

    List<Transaction> getAllTransactionsByWalletId(int walletId);

    List<Transaction> getAllOutgoingTransactionsByWalletId(int walletId);

    List<Transaction> getAllIncomingTransactionsByWalletId(int walletId);

    void makeTransaction(int senderId, Transaction transaction);


}
