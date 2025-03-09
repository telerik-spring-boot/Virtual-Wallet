package com.telerik.virtualwallet.services.security;

import com.telerik.virtualwallet.repositories.transaction.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferSecurityService {

    private final TransferRepository transferRepository;

    @Autowired
    public TransferSecurityService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public boolean isUserTransferReceiver(int transferId, String username) {

        return transferRepository.isUserTransferReceiver(transferId, username);

    }
}
