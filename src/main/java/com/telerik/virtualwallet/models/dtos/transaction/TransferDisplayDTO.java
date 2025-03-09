package com.telerik.virtualwallet.models.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransferDisplayDTO {

    public String cardNumber;

    public List<String> receiverWalletHolders;

    public BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm '['dd-MM-yyyy']'")
    public LocalDateTime transferTime;

    public TransferDisplayDTO() {
    }

    public TransferDisplayDTO(String cardNumber, List<String> receiverWalletHolders, BigDecimal amount, LocalDateTime transferTime) {
        this.cardNumber = cardNumber;
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
}
