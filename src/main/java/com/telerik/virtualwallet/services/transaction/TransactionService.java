package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    Page<Transaction> getAllTransactions(FilterTransactionsOptions options, Pageable pageable);

    Page<Transaction> getTransactionsByUsername(FilterTransactionsOptions options, Pageable pageable,String username);

    Transaction getTransactionById(int id);

    Page<Transaction> getTransactionsByWalletId(FilterTransactionsOptions options, Pageable pageable, int walletId);

    void makeTransaction(Transaction transaction);


}
