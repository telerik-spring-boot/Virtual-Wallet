package com.telerik.virtualwallet.models.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.telerik.virtualwallet.models.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionsWrapper {

    private int transactionId;

    private String sender;

    private String receivers;

    private int receiverWalletId;

    private String transactionCategory;

    private String transactionType;

    private BigDecimal amount;

    private Currency currency;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm '['dd-MM-yyyy']'")
    private LocalDateTime transactionTime;

    public TransactionsWrapper() {
    }

    public TransactionsWrapper( int transactionId,  int receiverWalletId, String receivers, BigDecimal amount, String transactionType, String transactionCategory, Currency currency, String message) {
        this.transactionId = transactionId;
        this.receiverWalletId = receiverWalletId;
        this.receivers = receivers;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
        this.currency = currency;
        this.message = message;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public int getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setReceiverWalletId(int receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
