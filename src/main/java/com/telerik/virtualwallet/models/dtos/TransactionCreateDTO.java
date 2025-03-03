package com.telerik.virtualwallet.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

    public TransactionCreateDTO() {
    }

    public TransactionCreateDTO(int walletReceiverId, int walletSenderId, int transactionCategoryId, BigDecimal amount) {
        this.walletReceiverId = walletReceiverId;
        this.walletSenderId = walletSenderId;
        this.transactionCategoryId = transactionCategoryId;
        this.amount = amount;
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
}
