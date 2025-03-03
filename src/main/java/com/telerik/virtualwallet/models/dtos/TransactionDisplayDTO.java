package com.telerik.virtualwallet.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionDisplayDTO {

    public String senderUsername;

    public List<String> receiverUsername;

    public String transactionCategory;

    public BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm '['dd-MM-yyyy']'")
    public LocalDateTime transactionTime;

    public TransactionDisplayDTO() {
    }

    public TransactionDisplayDTO(String senderUsername, List<String> receiverUsername, String transactionCategory, BigDecimal amount, LocalDateTime transactionTime) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.transactionCategory = transactionCategory;
        this.amount = amount;
        this.transactionTime = transactionTime;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public List<String> getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(List<String> receiverUsername) {
        this.receiverUsername = receiverUsername;
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
}
