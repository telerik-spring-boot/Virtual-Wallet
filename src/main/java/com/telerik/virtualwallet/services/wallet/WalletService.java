package com.telerik.virtualwallet.services.wallet;

import com.telerik.virtualwallet.models.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    List<Wallet> getAllWallets();

    List<Wallet> getWalletsByUsername(String username);

    Wallet getWalletById(int walletId);

    void createAdditionalWallet(String username, Wallet wallet);

    void createMainWallet(String username, Wallet wallet);

    void makeWalletMainWalletById(int walletId, String username);

    void addFundsToWallet(int walletId, int cardId, BigDecimal amount);

    void addUserToWallet(int walletId, int userIdToAdd);

    void removeUserFromWallet(int walletId, int userIdToRemove);

    void deleteWallet(int walletId);

    void deleteWallets(List<Integer> walletIds);


}
