package com.telerik.virtualwallet.services.wallet;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    List<Wallet> getAllWallets();

    List<Wallet> getWalletsByUserId(int userId);

    Wallet getWalletById(int walletId);

    void createWallet(int userRequestId, Wallet wallet);

    void addFundsToWallet(int userRequestId,int walletId, int cardId, BigDecimal amount);

    void addUserToWallet(int walletId, int userIdToAdd);

    void removeUserFromWallet(int walletId, int userIdToRemove);


}
