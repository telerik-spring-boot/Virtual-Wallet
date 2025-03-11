package com.telerik.virtualwallet.models.dtos.wallet;

import com.telerik.virtualwallet.models.enums.Currency;

public class WalletCreateDTO {

    private Currency currency;

    public WalletCreateDTO() {
    }

    public Currency getCurrency() {
        return currency;
    }
}
