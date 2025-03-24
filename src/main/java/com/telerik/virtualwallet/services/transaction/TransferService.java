package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.filters.FilterTransferOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TransferService {

    Page<Transfer> getAllTransfers(FilterTransferOptions options, Pageable pageable);

    Page<Transfer> getAllTransfersByUsername(FilterTransferOptions options, Pageable pageable, String username);

    List<Transfer> getAllTransfersByUsername(String username);

    Page<Transfer> getAllTransfersByWalletId(FilterTransferOptions options, Pageable pageable, int walletId);

    List<Transfer> getAllTransfersByWalletId(int walletId);

    BigDecimal getBalanceChangeForTheCurrentMonthByWalletId(int walletId);

    BigDecimal getBalanceChangeForTheCurrentMonthByWalletAndCardId(int walletId, int cardId);

    List<BigDecimal> getTransfersForTheLastYearByWalletId(int walletId);

    List<Transfer> getAllTransfersToYourWalletsByUsername(String username);

    Transfer getTransferById(int transferId);
}
