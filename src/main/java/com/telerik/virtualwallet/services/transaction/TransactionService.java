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

    List<Transaction> getTransactionsByWalletId(int walletId);

    List<Transaction> getIncomingTransactionsByWalletId(int walletId);

    List<Transaction> getOutgoingTransactionsByWalletId(int walletId);

    Transaction getTransactionById(int id);

    BigDecimal getBalanceChangeForTheCurrentMonthByWalletId(int walletId);

    List<BigDecimal> getIncomingTransactionsForTheLastYearByWalletId(int walletId);

    List<BigDecimal> getOutgoingTransactionsForTheLastYearByWalletId(int walletId);

    Page<Transaction> getTransactionsByWalletId(FilterTransactionsOptions options, Pageable pageable, int walletId);

    void makeTransaction(Transaction transaction);

    void makeTransactionMVC(Transaction transaction, BigDecimal receivedAmount);


}
