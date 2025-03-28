package com.telerik.virtualwallet.services.wallet;

import com.telerik.virtualwallet.models.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    List<Wallet> getAllWallets();

    List<Wallet> getWalletsByUsername(String username);

    Wallet getWalletById(int walletId);

    Wallet getReceiverWalletBySenderWalletIdAndReceiverUsernameMVC(int senderWalletId, String receiverUsername);

    Wallet getmainWalletByUsername(String username);

    void createWallet(String username, Wallet wallet);

    void makeWalletMainWalletById(int walletId, String username);

    void addFundsToWallet(int walletId, int cardId, BigDecimal amount);

    void addUserToWallet(int walletId, String usernameToAdd);

    void removeUserFromWallet(int walletId, String usernameToRemove);

    void deleteWallet(int walletId);

    void deleteWallets(List<Integer> walletIds);


}
