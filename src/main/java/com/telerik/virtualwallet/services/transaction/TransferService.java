package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.filters.FilterTransferOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransferService {

    Page<Transfer> getAllTransfers(FilterTransferOptions options, Pageable pageable);

    Page<Transfer> getAllTransfersByUsername(FilterTransferOptions options, Pageable pageable, String username);

    List<Transfer> getAllTransfersByUsername(String username);

    Page<Transfer> getAllTransfersByWalletId(FilterTransferOptions options, Pageable pageable, int walletId);

    List<Transfer> getAllTransfersByWalletId(int walletId);

    List<Transfer> getAllTransfersToYourWalletsByUsername(String username);

    Transfer getTransferById(int transferId);
}
