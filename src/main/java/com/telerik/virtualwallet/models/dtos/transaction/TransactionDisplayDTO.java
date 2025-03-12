package com.telerik.virtualwallet.models.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.telerik.virtualwallet.models.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionDisplayDTO {

    private int transactionId;

    private String senderUsername;

    private String senderWalletHolders;

    private String receiverWalletHolders;

    private int receiverWalletId;

    private String transactionCategory;

    private BigDecimal amount;

    private Currency currency;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm '['dd-MM-yyyy']'")
    public LocalDateTime transactionTime;

    public TransactionDisplayDTO() {
    }

    public TransactionDisplayDTO(String senderUsername, String senderWalletHolders, String receiverWalletHolders, String transactionCategory, BigDecimal amount, Currency currency, String message, LocalDateTime transactionTime) {
        this.senderUsername = senderUsername;
        this.senderWalletHolders = senderWalletHolders;
        this.receiverWalletHolders = receiverWalletHolders;
        this.transactionCategory = transactionCategory;
        this.amount = amount;
        this.currency = currency;
        this.message = message;
        this.transactionTime = transactionTime;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverWalletHolders() {
        return receiverWalletHolders;
    }

    public void setReceiverWalletHolders(String receiverWalletHolders) {
        this.receiverWalletHolders = receiverWalletHolders;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getSenderWalletHolders() {
        return senderWalletHolders;
    }

    public void setSenderWalletHolders(String senderWalletHolders) {
        this.senderWalletHolders = senderWalletHolders;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setReceiverWalletId(int receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
