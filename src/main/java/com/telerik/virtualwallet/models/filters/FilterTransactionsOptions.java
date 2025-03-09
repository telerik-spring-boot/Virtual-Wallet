package com.telerik.virtualwallet.models.filters;

import com.telerik.virtualwallet.models.enums.Currency;
import com.telerik.virtualwallet.models.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public class FilterTransactionsOptions extends FilterTransferOptions {

    private final TransactionStatus transactionStatus;
    private final String transactionCategory;

    public FilterTransactionsOptions(String username, LocalDateTime startTime, LocalDateTime endTime, Currency currency, TransactionStatus transactionStatus, String transactionCategory) {
        super(username, startTime, endTime, currency);

        this.transactionStatus = transactionStatus;
        this.transactionCategory = transactionCategory == null || transactionCategory.isBlank() ? null : transactionCategory;
    }


    public Optional<TransactionStatus> getTransactionStatus() {
        return Optional.ofNullable(transactionStatus);
    }

    public Optional<String> getTransactionCategory() {
        return Optional.ofNullable(transactionCategory);
    }
}
