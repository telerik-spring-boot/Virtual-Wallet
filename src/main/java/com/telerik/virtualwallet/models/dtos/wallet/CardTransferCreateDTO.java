package com.telerik.virtualwallet.models.dtos.wallet;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CardTransferCreateDTO {

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount must have up to 10 digits before the decimal and 2 after")
    private BigDecimal amount;

    public CardTransferCreateDTO(BigDecimal amount) {
        this.amount = amount;
    }

    public CardTransferCreateDTO() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
