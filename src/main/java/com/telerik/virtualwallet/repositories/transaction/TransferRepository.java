package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransferRepository {

    Page<Transfer> getAllTransfers(FilterTransactionsOptions options, Pageable pageable);

    Page<Transfer> getAllTransfersByUsername(FilterTransactionsOptions options, Pageable pageable, String username);

    void createTransfer(Transfer transfer);
}
