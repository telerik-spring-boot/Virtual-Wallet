package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.filters.FilterTransferOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransferService {

    Page<Transfer> getAllTransfers(FilterTransferOptions options,Pageable pageable);

    Page<Transfer> getAllTransfersByUsername(FilterTransferOptions options,Pageable pageable, String username);

    Page<Transfer> getAllTransfersByWalletId(FilterTransferOptions options,Pageable pageable, int walletId);

    Transfer getTransferById(int transferId);
}
