package com.telerik.virtualwallet.models.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransferDisplayDTO {

    private int transferId;

    private String cardNumber;

    private String senderUsername;

    private List<String> receiverWalletHolders;

    private int receiverWalletId;

    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm '['dd-MM-yyyy']'")
    private LocalDateTime transferTime;

    public TransferDisplayDTO() {
    }

    public TransferDisplayDTO(String cardNumber, String senderUsername, List<String> receiverWalletHolders, BigDecimal amount, LocalDateTime transferTime) {
        this.cardNumber = cardNumber;
        this.senderUsername = senderUsername;
        this.receiverWalletHolders = receiverWalletHolders;
        this.amount = amount;
        this.transferTime = transferTime;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public List<String> getReceiverWalletHolders() {
        return receiverWalletHolders;
    }

    public void setReceiverWalletHolders(List<String> receiverWalletHolders) {
        this.receiverWalletHolders = receiverWalletHolders;
    }

    public int getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setReceiverWalletId(int receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(LocalDateTime transferTime) {
        this.transferTime = transferTime;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

}
