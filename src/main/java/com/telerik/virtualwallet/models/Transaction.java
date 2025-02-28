package com.telerik.virtualwallet.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name="wallet_sender_id", nullable=false)
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name="wallet_receiver_id", nullable=false)
    private Wallet receiverWallet;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private TransactionCategory transactionCategory;

    @Column(name = "transaction_time", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Transaction() {
    }

    public Transaction(int id, BigDecimal amount, Wallet senderWallet, Wallet receiverWallet, TransactionCategory transactionCategory, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.transactionCategory = transactionCategory;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Wallet getSenderWallet() {
        return senderWallet;
    }

    public void setSenderWallet(Wallet senderWallet) {
        this.senderWallet = senderWallet;
    }

    public Wallet getReceiverWallet() {
        return receiverWallet;
    }

    public void setReceiverWallet(Wallet receiverWallet) {
        this.receiverWallet = receiverWallet;
    }

    public TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
