package com.telerik.virtualwallet.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name="card_sender_id", nullable=false)
    private Card senderCard;

    @ManyToOne
    @JoinColumn(name="wallet_receiver_id", nullable=false)
    private Wallet receiverWallet;

    @Column(name = "transfer_time", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Transfer() {
    }

    public Transfer(int id, BigDecimal amount, Card senderCard, Wallet receiverWallet, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.senderCard = senderCard;
        this.receiverWallet = receiverWallet;
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

    public Card getSenderCard() {
        return senderCard;
    }

    public void setSenderCard(Card senderCard) {
        this.senderCard = senderCard;
    }

    public Wallet getReceiverWallet() {
        return receiverWallet;
    }

    public void setReceiverWallet(Wallet receiverWallet) {
        this.receiverWallet = receiverWallet;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
