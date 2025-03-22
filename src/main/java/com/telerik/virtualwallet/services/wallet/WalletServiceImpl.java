package com.telerik.virtualwallet.services.wallet;

import com.telerik.virtualwallet.exceptions.*;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.repositories.transaction.TransferRepository;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.repositories.wallet.WalletRepository;
import com.telerik.virtualwallet.services.card.CardService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private static final String NO_WALLETS_MESSAGE = "No wallets are found.";
    private static final String NO_WALLETS_FOUND_MESSAGE = "No wallets associated with %s found.";
    private static final String USER_ALREADY_WALLET_HOLDER_MESSAGE = "Wallet is already managed by a user %s.";
    private static final String USER_NOT_WALLET_HOLDER_MESSAGE = "Wallet is not managed by user %s.";
    private static final String WALLET_WITH_NO_USERS_EXCEPTION = "A wallet has to be managed by at least one user.";
    private static final String WALLET_ALREADY_MAIN_MESSAGE = "Wallet with id %d is already the main wallet for user %s.";
    private static final String USER_WITH_NO_WALLETS_EXCEPTION = "A user has to manage at least one wallet.";
    private static final String MAXIMUM_WALLET_SIZE_MESSAGE_YOU = "You cannot have more than 10 wallets.";
    private static final String MAXIMUM_WALLET_SIZE_MESSAGE_USER = "User %s cannot have more than 10 wallets.";

    private final WalletRepository walletRepository;
    private final CardService cardService;
    private final UserRepository userRepository;
    private final DummyCardTransferService dummyCardTransferService;
    private final TransferRepository transferRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, CardService cardService, UserRepository userRepository, DummyCardTransferService dummyCardTransferService, TransferRepository transferRepository) {
        this.walletRepository = walletRepository;
        this.cardService = cardService;
        this.userRepository = userRepository;
        this.dummyCardTransferService = dummyCardTransferService;
        this.transferRepository = transferRepository;
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
    public List<Wallet> getWalletsByUsername(String username) {

        List<Wallet> wallets = walletRepository.getWalletsWithUsersByUsername(username);

        if (wallets.isEmpty()) {
            throw new EntityNotFoundException(String.format(NO_WALLETS_FOUND_MESSAGE, username));
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
    public void createWallet(String username, Wallet wallet) {

        User user = userRepository.getUserWithWallets(username);

        if (user.getWallets().size() >= 10) {
            throw new UnauthorizedOperationException(MAXIMUM_WALLET_SIZE_MESSAGE_YOU);
        }

        wallet.getUsers().add(user);
        wallet.setCreator(user);
        walletRepository.addWallet(wallet);

    }


    @Override
    public void makeWalletMainWalletById(int walletId, String username) {

        User user = userRepository.getByUsername(username);

        Wallet newMainWallet = walletRepository.getWalletById(walletId);

        if (newMainWallet == null) {
            throw new EntityNotFoundException("Wallet", "id", walletId);
        }

        if (newMainWallet.getId() == user.getMainWallet().getId()) {
            throw new DuplicateEntityException(String.format(WALLET_ALREADY_MAIN_MESSAGE, walletId, username));
        }

        user.setMainWallet(newMainWallet);
        userRepository.update(user);
    }

    @Transactional
    @Override
    public void addFundsToWallet(int walletId, int cardId, BigDecimal amount) {

        Card card = cardService.getCardById(cardId);

        Wallet wallet = getWalletById(walletId);

        boolean hasFunds = dummyCardTransferService.successfulCardTransfer(card, amount, wallet.getCurrency());

        if (!hasFunds) {
            throw new InsufficientFundsException("Card transfer unsuccessful.");
        }

        wallet.setBalance(wallet.getBalance().add(amount));

        walletRepository.updateWallet(wallet);

        Transfer transfer = new Transfer();
        transfer.setAmount(amount);
        transfer.setReceiverWallet(wallet);
        transfer.setSenderCard(card);

        transferRepository.createTransfer(transfer);

    }

    @Override
    public void addUserToWallet(int walletId, String usernameToAdd) {

        Wallet wallet = walletRepository.getWalletWithUsersById(walletId);
        User userToAdd = userRepository.getUserWithWallets(usernameToAdd);

        if (userToAdd == null) {
            throw new EntityNotFoundException("User", "username", usernameToAdd);
        }

        boolean userExists = wallet.getUsers().stream()
                .anyMatch(user -> user.getId() == userToAdd.getId());

        if (userExists) {
            throw new DuplicateEntityException(String.format(USER_ALREADY_WALLET_HOLDER_MESSAGE, usernameToAdd));
        }

        if (userToAdd.getWallets().size() >= 10) {
            throw new UnauthorizedOperationException(String.format(MAXIMUM_WALLET_SIZE_MESSAGE_USER,usernameToAdd));
        }

        wallet.getUsers().add(userToAdd);

        walletRepository.updateWallet(wallet);

    }

    @Override
    public void removeUserFromWallet(int walletId, String usernameToRemove) {

        Wallet wallet = walletRepository.getWalletWithUsersById(walletId);

        if (wallet.getUsers().size() <= 1) {
            throw new InconsistentOperationException(WALLET_WITH_NO_USERS_EXCEPTION);
        }

        User userToRemove = userRepository.getUserWithWallets(usernameToRemove);

        if (userToRemove == null) {
            throw new EntityNotFoundException("User", "username", usernameToRemove);
        }

        User existingUser = wallet.getUsers().stream()
                .filter(user -> user.getId() == userToRemove.getId())
                .findFirst()
                .orElseThrow(() ->
                        new DuplicateEntityException(String.format(USER_NOT_WALLET_HOLDER_MESSAGE, usernameToRemove)));

        if (userToRemove.getWallets().size() <= 1) {
            throw new InconsistentOperationException(USER_WITH_NO_WALLETS_EXCEPTION);
        }

        if (userToRemove.getMainWallet().getId() == walletId) {

            Wallet newMainWallet = userToRemove.getWallets().stream()
                    .max(Comparator.comparing(Wallet::getBalance))
                    .orElseThrow(() -> new EntityNotFoundException(NO_WALLETS_MESSAGE));

            userToRemove.setMainWallet(newMainWallet);
            userRepository.update(userToRemove);

        }

        wallet.getUsers().remove(existingUser);

        walletRepository.updateWallet(wallet);

    }

    @Override
    public void deleteWallet(int walletId) {

        getWalletById(walletId);
        walletRepository.deleteWallet(walletId);

    }

    @Override
    public void deleteWallets(List<Integer> walletIds) {
        walletRepository.deleteWallets(walletIds);
    }

}
