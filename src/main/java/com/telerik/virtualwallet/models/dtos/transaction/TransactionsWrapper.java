package com.telerik.virtualwallet.models.dtos.transaction;

import com.telerik.virtualwallet.models.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionsWrapper {

    private int transactionId;

    private String sender;

    private String receivers;

    private String mainReceiver;

    private int receiverWalletId;

    private String transactionCategory;

    private String transactionType;

    private BigDecimal amount;

    private String currency;

    private String message;

    private LocalDateTime transactionTime;

    public TransactionsWrapper() {
    }

    public TransactionsWrapper(int transactionId, String sender, String receivers, String mainReceiver, int receiverWalletId, String transactionCategory, String transactionType, BigDecimal amount, String currency, String message, LocalDateTime transactionTime) {
        this.transactionId = transactionId;
        this.sender = sender;
        this.receivers = receivers;
        this.mainReceiver = mainReceiver;
        this.receiverWalletId = receiverWalletId;
        this.transactionCategory = transactionCategory;
        this.transactionType = transactionType;
        this.amount = amount;
        this.currency = currency;
        this.message = message;
        this.transactionTime = transactionTime;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
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

    public String getMainReceiver() {
        return mainReceiver;
    }

    public void setMainReceiver(String mainReceiver) {
        this.mainReceiver = mainReceiver;
    }
}
