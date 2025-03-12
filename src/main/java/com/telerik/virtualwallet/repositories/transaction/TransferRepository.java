package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.filters.FilterTransferOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransferRepository {

    Page<Transfer> getAllTransfers(FilterTransferOptions options, Pageable pageable);

    Page<Transfer> getAllTransfersByUsername(FilterTransferOptions options, Pageable pageable, String username);

    Page<Transfer> getAllTransfersByWalletId(FilterTransferOptions options, Pageable pageable, int walletId);

    List<Transfer> getAllTransfersMvc();

    Transfer getTransferById(int id);

    void createTransfer(Transfer transfer);

    boolean isUserTransferReceiver(int transferId, String username);
}
