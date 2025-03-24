package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.filters.FilterTransferOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TransferRepository {

    Page<Transfer> getAllTransfers(FilterTransferOptions options, Pageable pageable);

    Page<Transfer> getAllTransfersMadeByUserByUsername(FilterTransferOptions options, Pageable pageable, String username);

    Page<Transfer> getAllTransfersByWalletId(FilterTransferOptions options, Pageable pageable, int walletId);

    List<Transfer> getAllTransfersByWalletId(int walletId);

    List<Transfer> getAllTransfersMvc();

    List<Transfer> getAllTransfersMadeByUserByUsername(String username);

    List<Transfer> getAllTransfersToYourWalletsByUsername(String username);

    Transfer getTransferById(int id);

    BigDecimal getBalanceChangeByWalletId(int walletId);

    BigDecimal getBalanceChangeByWalletAndCardId(int walletId, int cardId);

    void createTransfer(Transfer transfer);

    boolean isUserTransferReceiver(int transferId, String username);
}
