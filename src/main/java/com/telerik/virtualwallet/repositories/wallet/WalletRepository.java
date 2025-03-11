package com.telerik.virtualwallet.repositories.wallet;

import com.telerik.virtualwallet.models.Wallet;

import java.util.List;

public interface WalletRepository {

    List<Wallet> getAllWallets();

    Wallet getWalletById(int id);

    Wallet getWalletWithUsersById(int id);

    List<Wallet> getWalletsWithUsersByUsername(String username);

    void addWallet(Wallet wallet);

    void updateWallet(Wallet wallet);

    void deleteWallet(int id);

    void deleteWallets(List<Integer> ids);

    boolean isUserWalletHolder(String username, int walletId);
}
