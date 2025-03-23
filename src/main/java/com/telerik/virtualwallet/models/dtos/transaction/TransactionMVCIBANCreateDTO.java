package com.telerik.virtualwallet.models.dtos.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransactionMVCIBANCreateDTO {

    @NotNull(message= "IBAN number cannot be blank.")
    private String IBAN;

    @NotNull(message = "Amount cannot be null.")
    @Positive(message = "Amount must be greater than zero.")
    private BigDecimal amount;

    public TransactionMVCIBANCreateDTO() {
    }

    public TransactionMVCIBANCreateDTO(String IBAN, BigDecimal amount) {
        this.IBAN = IBAN;
        this.amount = amount;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
