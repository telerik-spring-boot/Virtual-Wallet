package com.telerik.virtualwallet.models.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.telerik.virtualwallet.models.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionDisplayDTO {

    public String senderUsername;

    public List<String> senderWalletHolders;

    public List<String> receiverWalletHolders;

    public String transactionCategory;

    public BigDecimal amount;

    public Currency currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm '['dd-MM-yyyy']'")
    public LocalDateTime transactionTime;

    public TransactionDisplayDTO() {
    }

    public TransactionDisplayDTO(String senderUsername, List<String> senderWalletHolders, List<String> receiverWalletHolders, String transactionCategory, BigDecimal amount, Currency currency, LocalDateTime transactionTime) {
        this.senderUsername = senderUsername;
        this.senderWalletHolders = senderWalletHolders;
        this.receiverWalletHolders = receiverWalletHolders;
        this.transactionCategory = transactionCategory;
        this.amount = amount;
        this.currency = currency;
        this.transactionTime = transactionTime;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public List<String> getReceiverWalletHolders() {
        return receiverWalletHolders;
    }

    public void setReceiverWalletHolders(List<String> receiverWalletHolders) {
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

    public List<String> getSenderWalletHolders() {
        return senderWalletHolders;
    }

    public void setSenderWalletHolders(List<String> senderWalletHolders) {
        this.senderWalletHolders = senderWalletHolders;
    }
}
