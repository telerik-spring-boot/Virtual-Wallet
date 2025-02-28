package com.telerik.virtualwallet.services.wallet;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;
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
    private static final String UNAUTHORIZED_MESSAGE = "This wallet is not associated with your account.";
    private static final String NO_WALLETS_FOUND_MESSAGE = "No wallets associated with user with id %d found.";
    private static final String USER_ALREADY_WALLET_HOLDER_MESSAGE = "Wallet with id %d is already managed by a user with id %d";
    private static final String USER_NOT_WALLET_HOLDER_MESSAGE = "Wallet with id %d is not managed by user with id %d.";

    private final WalletRepository walletRepository;
    private final CardService cardService;
    private final UserService userService;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, CardService cardService, UserService userService) {
        this.walletRepository = walletRepository;
        this.cardService = cardService;
        this.userService = userService;
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
    public List<Wallet> getWalletsByUserId(User user, int userId) {

        if (user.getId() != userId) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MESSAGE);
        }

        List<Wallet> wallets = walletRepository.getWalletsByUserId(userId);

        if (wallets.isEmpty()) {
            throw new EntityNotFoundException(String.format(NO_WALLETS_FOUND_MESSAGE, userId));
        }

        return wallets;

    }

    @Override
    public Wallet getWalletById(User user, int walletId) {

        Wallet wallet = walletRepository.getWalletById(walletId);

        if (wallet == null) {
            throw new EntityNotFoundException("Wallet", "id", walletId);
        }

        if (isUserNotWalletHolder(user, wallet)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MESSAGE);
        }

        return wallet;

    }

    @Override
    public void addWallet(User user, Wallet wallet) {

        if (isUserNotWalletHolder(user, wallet)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MESSAGE);
        }

        walletRepository.addWallet(wallet);

    }

    @Transactional
    @Override
    public void addFundsToWallet(User user, int walletId, int cardId, BigDecimal amount) {

        Card card = cardService.getCardById(user, cardId);
        // TODO: Add dummy card transfer check to this

        Wallet wallet = getWalletById(user, walletId);

        wallet.setBalance(wallet.getBalance().add(amount));

    }

    @Override
    public void addUserToWallet(User user, int walletId, int userIdToAdd) {

        Wallet wallet = getWalletById(user, walletId);
        User userToAdd = userService.getById(userIdToAdd);

        if(wallet.getUsers().contains(userToAdd)) {
            throw new DuplicateEntityException(String.format(USER_ALREADY_WALLET_HOLDER_MESSAGE, walletId, userIdToAdd));
        }

        wallet.getUsers().add(userToAdd);

        walletRepository.updateWallet(wallet);

    }

    @Override
    public void removeUserFromWallet(User user, int walletId, int userIdToRemove) {

        Wallet wallet = getWalletById(user, walletId);
        User userToRemove = userService.getById(userIdToRemove);

        if(!wallet.getUsers().contains(userToRemove)) {
            throw new DuplicateEntityException(String.format(USER_NOT_WALLET_HOLDER_MESSAGE, walletId, userIdToRemove));
        }

        wallet.getUsers().remove(userToRemove);

        walletRepository.updateWallet(wallet);

    }

    private static boolean isUserNotWalletHolder(User user, Wallet wallet) {

        return !wallet.getUsers().contains(user);

    }

}
