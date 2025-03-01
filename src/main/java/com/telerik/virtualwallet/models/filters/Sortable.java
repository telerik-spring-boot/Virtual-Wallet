package com.telerik.virtualwallet.models.filters;

import java.util.Optional;

public interface Sortable {

    Optional<String> getSortBy();
    Optional<String> getSortOrder();

}
