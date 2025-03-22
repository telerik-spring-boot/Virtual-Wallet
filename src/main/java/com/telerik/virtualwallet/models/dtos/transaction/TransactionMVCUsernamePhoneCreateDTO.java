package com.telerik.virtualwallet.models.dtos.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransactionMVCUsernamePhoneCreateDTO {

    @NotNull(message= "Username or phone number cannot be blank.")
    private String usernameOrPhone;

    @NotNull(message = "Amount cannot be null.")
    @Positive(message = "Amount must be greater than zero.")
    private BigDecimal amount;

    public TransactionMVCUsernamePhoneCreateDTO() {
    }

    public TransactionMVCUsernamePhoneCreateDTO(String usernameOrPhone, BigDecimal amount) {
        this.usernameOrPhone = usernameOrPhone;
        this.amount = amount;
    }

    public String getUsernameOrPhone() {
        return usernameOrPhone;
    }

    public void setUsernameOrPhone(String usernameOrPhone) {
        this.usernameOrPhone = usernameOrPhone;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
