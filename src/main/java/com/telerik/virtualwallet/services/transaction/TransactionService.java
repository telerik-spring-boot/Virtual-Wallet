package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.User;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(User user, int id);

    List<Transaction> getAllTransactionsByWalletId(User user, int walletId);

    List<Transaction> getAllOutgoingTransactionsByWalletId(User user, int walletId);

    List<Transaction> getAllIncomingTransactionsByWalletId(User user, int walletId);

    void makeTransaction(User user, Transaction transaction);


}
