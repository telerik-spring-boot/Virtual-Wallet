package com.telerik.virtualwallet.models.filters;

import com.telerik.virtualwallet.models.TransactionCategory;
import com.telerik.virtualwallet.models.enums.Currency;
import com.telerik.virtualwallet.models.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public class FilterTransactionsOptions {

    private final String username;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Currency currency;
    private final TransactionStatus transactionStatus;
    private final String transactionCategory;

    public FilterTransactionsOptions(String username, LocalDateTime startTime, LocalDateTime endTime, Currency currency, TransactionStatus transactionStatus, String transactionCategory) {
        this.username = username == null || username.isBlank() ? null : username;
        this.startTime = startTime;
        this.endTime = endTime;
        this.currency = currency;
        this.transactionStatus = transactionStatus;
        this.transactionCategory = transactionCategory == null || transactionCategory.isBlank() ? null : transactionCategory;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    public Optional<Currency> getCurrency() {
        return Optional.ofNullable(currency);
    }

    public Optional<TransactionStatus> getTransactionStatus() {
        return Optional.ofNullable(transactionStatus);
    }

    public Optional<String> getTransactionCategory() {
        return Optional.ofNullable(transactionCategory);
    }
}
