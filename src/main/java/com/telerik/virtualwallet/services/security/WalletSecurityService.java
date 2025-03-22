package com.telerik.virtualwallet.services.security;

import com.telerik.virtualwallet.repositories.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletSecurityService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletSecurityService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public boolean isUserWalletHolder(int walletId, String username) {

        return walletRepository.isUserWalletHolder(username, walletId);

    }

    public boolean isUserWalletCreator(int walletId, String username) {

        return walletRepository.isUserWalletCreator(username, walletId);

    }
}
