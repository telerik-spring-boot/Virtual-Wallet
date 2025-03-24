package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.filters.FilterTransferOptions;
import com.telerik.virtualwallet.repositories.transaction.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.telerik.virtualwallet.services.transaction.TransactionServiceImpl.pageableHelper;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public Page<Transfer> getAllTransfers(FilterTransferOptions options, Pageable pageable) {

        pageableHelper(pageable);

        return transferRepository.getAllTransfers(options, pageable);
    }

    @Override
    public Page<Transfer> getAllTransfersByUsername(FilterTransferOptions options, Pageable pageable, String username) {

        pageableHelper(pageable);

        return transferRepository.getAllTransfersMadeByUserByUsername(options, pageable, username);
    }

    @Override
    public List<Transfer> getAllTransfersByUsername(String username) {

        return transferRepository.getAllTransfersMadeByUserByUsername(username);
    }

    @Override
    public Page<Transfer> getAllTransfersByWalletId(FilterTransferOptions options, Pageable pageable, int walletId) {

        pageableHelper(pageable);

        return transferRepository.getAllTransfersByWalletId(options, pageable, walletId);
    }

    @Override
    public List<Transfer> getAllTransfersByWalletId(int walletId) {
        return transferRepository.getAllTransfersByWalletId(walletId);
    }

    @Override
    public BigDecimal getBalanceChangeForTheCurrentMonthByWalletId(int walletId) {
        return transferRepository.getBalanceChangeByWalletId(walletId);
    }

    @Override
    public BigDecimal getBalanceChangeForTheCurrentMonthByWalletAndCardId(int walletId, int cardId) {
        return transferRepository.getBalanceChangeByWalletAndCardId(walletId, cardId);
    }

    @Override
    public List<Transfer> getAllTransfersToYourWalletsByUsername(String username) {
        return transferRepository.getAllTransfersToYourWalletsByUsername(username);
    }

    @Override
    public Transfer getTransferById(int transferId) {

        Transfer transfer = transferRepository.getTransferById(transferId);

        if (transfer == null) {
            throw new EntityNotFoundException("Transfer", "id", transferId);
        }

        return transfer;
    }
}
