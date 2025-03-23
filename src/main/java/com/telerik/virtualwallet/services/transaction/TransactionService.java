package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    Page<Transaction> getAllTransactions(FilterTransactionsOptions options, Pageable pageable);

    Page<Transaction> getTransactionsByUsername(FilterTransactionsOptions options, Pageable pageable,String username);

    List<Transaction> getTransactionsByUsername(String username);

    List<Transaction> getIncomingTransactionsByUsername(String username);

    List<Transaction> getOutgoingTransactionsByUsername(String username);

    Transaction getTransactionById(int id);

    Page<Transaction> getTransactionsByWalletId(FilterTransactionsOptions options, Pageable pageable, int walletId);

    void makeTransaction(Transaction transaction);

    void makeTransactionMVC(Transaction transaction, BigDecimal receivedAmount);


}
