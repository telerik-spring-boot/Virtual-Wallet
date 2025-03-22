package com.telerik.virtualwallet.models.dtos.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class TransactionConfirmationMVCCreateDTO {

    @Positive(message = "Wallet receiver id must be greater than zero.")
    private int walletReceiverId;

    @Positive(message = "Wallet sender id must be greater than zero.")
    private int walletSenderId;

    private String receiverFullName;

    @NotNull(message = "Amount cannot be null.")
    @Positive(message = "Amount must be greater than zero.")
    private BigDecimal sentAmount;

    @NotNull(message = "Amount cannot be null.")
    @Positive(message = "Amount must be greater than zero.")
    private BigDecimal receivedAmount;

    private String senderCurrency;

    private String receiverCurrency;

    @NotNull(message = "Transaction category ID cannot be null.")
    @Positive(message = "Transaction category ID must be greater than zero.")
    private int transactionCategoryId;

    @NotBlank(message = "Message cannot be empty")
    @Size(min = 2, max = 50, message = "Message must be between 2 and 50 characters")
    private String message;

    public TransactionConfirmationMVCCreateDTO() {
    }

    public TransactionConfirmationMVCCreateDTO(int walletReceiverId, int walletSenderId, String receiverFullName, BigDecimal sentAmount, BigDecimal receivedAmount, String senderCurrency, String receiverCurrency, int transactionCategoryId, String message) {
        this.walletReceiverId = walletReceiverId;
        this.walletSenderId = walletSenderId;
        this.receiverFullName = receiverFullName;
        this.sentAmount = sentAmount;
        this.receivedAmount = receivedAmount;
        this.senderCurrency = senderCurrency;
        this.receiverCurrency = receiverCurrency;
        this.transactionCategoryId = transactionCategoryId;
        this.message = message;
    }

    public BigDecimal getSentAmount() {
        return sentAmount;
    }

    public void setSentAmount(BigDecimal sentAmount) {
        this.sentAmount = sentAmount;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getReceiverCurrency() {
        return receiverCurrency;
    }

    public void setReceiverCurrency(String receiverCurrency) {
        this.receiverCurrency = receiverCurrency;
    }

    public String getSenderCurrency() {
        return senderCurrency;
    }

    public void setSenderCurrency(String senderCurrency) {
        this.senderCurrency = senderCurrency;
    }

    public int getTransactionCategoryId() {
        return transactionCategoryId;
    }

    public void setTransactionCategoryId(int transactionCategoryId) {
        this.transactionCategoryId = transactionCategoryId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getWalletReceiverId() {
        return walletReceiverId;
    }

    public void setWalletReceiverId(int walletReceiverId) {
        this.walletReceiverId = walletReceiverId;
    }

    public int getWalletSenderId() {
        return walletSenderId;
    }

    public void setWalletSenderId(int walletSenderId) {
        this.walletSenderId = walletSenderId;
    }

    public String getReceiverFullName() {
        return receiverFullName;
    }

    public void setReceiverFullName(String receiverFullName) {
        this.receiverFullName = receiverFullName;
    }
}
