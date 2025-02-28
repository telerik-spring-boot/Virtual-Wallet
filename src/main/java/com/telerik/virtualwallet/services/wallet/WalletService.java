package com.telerik.virtualwallet.services.wallet;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    List<Wallet> getAllWallets();

    List<Wallet> getWalletsByUserId(User user, int userId);

    Wallet getWalletById(User user, int walletId);

    void createWallet(User user, Wallet wallet);

    void addFundsToWallet(User user, int walletId, int cardId, BigDecimal amount);

    void addUserToWallet(User user, int walletId, int userIdToAdd);

    void removeUserFromWallet(User user, int walletId, int userIdToRemove);


}
