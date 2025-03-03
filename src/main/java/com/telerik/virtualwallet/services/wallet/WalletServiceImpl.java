package com.telerik.virtualwallet.services.wallet;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.repositories.wallet.WalletRepository;
import com.telerik.virtualwallet.services.card.CardService;
import com.telerik.virtualwallet.services.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private static final String NO_WALLETS_MESSAGE = "No wallets are found.";
    private static final String NO_WALLETS_FOUND_MESSAGE = "No wallets associated with user with id %d found.";
    private static final String USER_ALREADY_WALLET_HOLDER_MESSAGE = "Wallet with id %d is already managed by a user with id %d";
    private static final String USER_NOT_WALLET_HOLDER_MESSAGE = "Wallet with id %d is not managed by user with id %d.";

    private final WalletRepository walletRepository;
    private final CardService cardService;
    private final UserRepository userRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, CardService cardService, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.cardService = cardService;
        this.userRepository = userRepository;
    }

    @Override
    public List<Wallet> getAllWallets() {

        List<Wallet> allWallets = walletRepository.getAllWallets();

        if (allWallets.isEmpty()) {
            throw new EntityNotFoundException(NO_WALLETS_MESSAGE);
        }

        return allWallets;

    }

    @Override
    public List<Wallet> getWalletsByUserId(int userId) {

        List<Wallet> wallets = walletRepository.getWalletsByUserId(userId);

        if (wallets.isEmpty()) {
            throw new EntityNotFoundException(String.format(NO_WALLETS_FOUND_MESSAGE, userId));
        }

        return wallets;

    }

    @Override
    public Wallet getWalletById(int walletId) {

        Wallet wallet = walletRepository.getWalletWithUsersById(walletId);

        if (wallet == null) {
            throw new EntityNotFoundException("Wallet", "id", walletId);
        }

        return wallet;

    }

    @Override
    public void createWallet(int userRequestId, Wallet wallet) {

        User user = userRepository.getById(userRequestId);
        wallet.getUsers().add(user);
        walletRepository.addWallet(wallet);

    }

    @Transactional
    @Override
    public void addFundsToWallet(int userRequestId, int walletId, int cardId, BigDecimal amount) {

        //Card card = cardService.getCardById(user, cardId);
        // TODO: Add dummy card transfer check to this. Do we need userRequestId here?

        Wallet wallet = getWalletById(walletId);

        wallet.setBalance(wallet.getBalance().add(amount));

    }

    @Override
    public void addUserToWallet(int walletId, int userIdToAdd) {

        Wallet wallet = walletRepository.getWalletWithUsersById(walletId);
        User userToAdd = userRepository.getById(userIdToAdd);

        if (wallet.getUsers().contains(userToAdd)) {
            throw new DuplicateEntityException(String.format(USER_ALREADY_WALLET_HOLDER_MESSAGE, walletId, userIdToAdd));
        }

        wallet.getUsers().add(userToAdd);

        walletRepository.updateWallet(wallet);

    }

    @Override
    public void removeUserFromWallet(int walletId, int userIdToRemove) {

        Wallet wallet = walletRepository.getWalletWithUsersById(walletId);
        User userToRemove = userRepository.getById(userIdToRemove);

        if (!wallet.getUsers().contains(userToRemove)) {
            throw new DuplicateEntityException(String.format(USER_NOT_WALLET_HOLDER_MESSAGE, walletId, userIdToRemove));
        }

        wallet.getUsers().remove(userToRemove);

        walletRepository.updateWallet(wallet);

    }

}
