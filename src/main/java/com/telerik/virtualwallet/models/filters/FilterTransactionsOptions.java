package com.telerik.virtualwallet.models.filters;

import com.telerik.virtualwallet.models.enums.Currency;
import com.telerik.virtualwallet.models.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public class FilterTransactionsOptions {

    private final String senderUsername;
    private final String receiverUsername;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Currency currency;
    private final TransactionStatus transactionStatus;

    public FilterTransactionsOptions(String senderUsername, String receiverUsername, LocalDateTime startTime, LocalDateTime endTime, Currency currency, TransactionStatus transactionStatus) {
        this.senderUsername = senderUsername == null || senderUsername.isBlank() ? null : senderUsername;
        this.receiverUsername = receiverUsername== null || receiverUsername.isBlank() ? null : receiverUsername;
        this.startTime = startTime;
        this.endTime = endTime;
        this.currency = currency;
        this.transactionStatus = transactionStatus;
    }

    public Optional<String> getSenderUsername() {
        return Optional.ofNullable(senderUsername);
    }

    public Optional<String> getReceiverUsername() {
        return Optional.ofNullable(receiverUsername);
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
}
