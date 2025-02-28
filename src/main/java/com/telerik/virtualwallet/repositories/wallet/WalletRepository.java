package com.telerik.virtualwallet.repositories.wallet;

import com.telerik.virtualwallet.models.Wallet;

import java.util.List;

public interface WalletRepository {

    List<Wallet> getAllWallets();

    Wallet getWalletById(int id);

    List<Wallet> getWalletsByUserId(int userId);

    void addWallet(Wallet wallet);

    void updateWallet(Wallet wallet);

    void deleteWallet(String id);
}
