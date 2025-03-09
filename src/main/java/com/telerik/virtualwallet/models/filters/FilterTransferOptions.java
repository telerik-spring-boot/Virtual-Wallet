package com.telerik.virtualwallet.models.filters;

import com.telerik.virtualwallet.models.enums.Currency;

import java.time.LocalDateTime;
import java.util.Optional;

public class FilterTransferOptions {

    private final String username;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Currency currency;

    public FilterTransferOptions(String username, LocalDateTime startTime, LocalDateTime endTime, Currency currency) {
        this.username = username == null || username.isBlank() ? null : username;
        this.startTime = startTime;
        this.endTime = endTime;
        this.currency = currency;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    public Optional<Currency> getCurrency() {
        return Optional.ofNullable(currency);
    }

}
