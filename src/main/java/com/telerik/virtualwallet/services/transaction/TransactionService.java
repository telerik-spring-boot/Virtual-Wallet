package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    Page<Transaction> getAllTransactions(FilterTransactionsOptions options, Pageable pageable);

    Transaction getTransactionById(int id);

    List<Transaction> getAllTransactionsByWalletId(int walletId);

    List<Transaction> getAllOutgoingTransactionsByWalletId(int walletId);

    List<Transaction> getAllIncomingTransactionsByWalletId(int walletId);

    void makeTransaction(Transaction transaction);


}
