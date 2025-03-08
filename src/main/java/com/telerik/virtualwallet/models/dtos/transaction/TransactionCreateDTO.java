package com.telerik.virtualwallet.models.dtos.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class TransactionCreateDTO {

    @NotNull(message = "Receiver wallet ID cannot be null.")
    @Positive(message = "Receiver wallet ID must be greater than zero.")
    private int walletReceiverId;

    @NotNull(message = "Sender wallet ID cannot be null.")
    @Positive(message = "Sender wallet ID must be greater than zero.")
    private int walletSenderId;

    @NotNull(message = "Transaction category ID cannot be null.")
    @Positive(message = "Transaction category ID must be greater than zero.")
    private int transactionCategoryId;

    @NotNull(message = "Amount cannot be null.")
    @Positive(message = "Amount must be greater than zero.")
    private BigDecimal amount;

    @NotBlank(message = "Message cannot be empty")
    @Size(min = 2, max = 50, message = "Message must be between 2 and 50 characters")
    private String message;

    public TransactionCreateDTO() {
    }

    public TransactionCreateDTO(int walletReceiverId, int walletSenderId, int transactionCategoryId, BigDecimal amount, String message) {
        this.walletReceiverId = walletReceiverId;
        this.walletSenderId = walletSenderId;
        this.transactionCategoryId = transactionCategoryId;
        this.amount = amount;
        this.message = message;
    }

    public int getWalletReceiverId() {
        return walletReceiverId;
    }

    public void setWalletReceiverId(int walletReceiverId) {
        this.walletReceiverId = walletReceiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getWalletSenderId() {
        return walletSenderId;
    }

    public void setWalletSenderId(int walletSenderId) {
        this.walletSenderId = walletSenderId;
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
}
