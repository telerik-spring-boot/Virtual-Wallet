package com.telerik.virtualwallet.services.wallet;

import com.telerik.virtualwallet.models.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    List<Wallet> getAllWallets();

    List<Wallet> getWalletsByUsername(String username);

    Wallet getWalletById(int walletId);

    void createWallet(int userRequestId, Wallet wallet);

    void addFundsToWallet(int walletId, int cardId, BigDecimal amount);

    void addUserToWallet(int walletId, int userIdToAdd);

    void removeUserFromWallet(int walletId, int userIdToRemove);


}
