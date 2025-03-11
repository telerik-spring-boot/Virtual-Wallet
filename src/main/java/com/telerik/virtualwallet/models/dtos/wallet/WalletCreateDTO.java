package com.telerik.virtualwallet.models.dtos.wallet;

import com.telerik.virtualwallet.models.enums.Currency;
import jakarta.validation.constraints.Pattern;

public class WalletCreateDTO {

    @Pattern(regexp = "^(EUR|GBP|USD)$", message = "Currency must be one of the following: EUR, GBP, USD")
    private Currency currency;

    public WalletCreateDTO() {
    }

    public Currency getCurrency() {
        return currency;
    }
}
