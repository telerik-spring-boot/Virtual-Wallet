package com.telerik.virtualwallet.services.security;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.repositories.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletSecurityService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Autowired
    public WalletSecurityService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }



    public boolean isUserWalletHolder(int walletId, String username) {

        User user = userRepository.getByUsername(username);
        if (user == null) {
            return false;
        }

        return walletRepository.isUserWalletHolder(user.getId(), walletId);
    }
}
