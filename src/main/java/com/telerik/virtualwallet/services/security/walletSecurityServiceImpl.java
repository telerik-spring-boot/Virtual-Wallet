package com.telerik.virtualwallet.services.security;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.repositories.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class walletSecurityServiceImpl implements WalletSecurityService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Autowired
    public walletSecurityServiceImpl(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isUserWalletHolder(int walletId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Authenticated user: " + username);

        User user = userRepository.getByUsername(username);
        if (user == null) {
            return false;
        }

        return walletRepository.isUserWalletHolder(user.getId(), walletId);
    }
}
