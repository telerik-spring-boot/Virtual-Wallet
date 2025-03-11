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
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private static final String NO_WALLETS_MESSAGE = "No wallets are found.";
    private static final String NO_WALLETS_FOUND_MESSAGE = "No wallets associated with %s found.";
    private static final String USER_ALREADY_WALLET_HOLDER_MESSAGE = "Wallet with id %d is already managed by a user with id %d.";
    private static final String USER_NOT_WALLET_HOLDER_MESSAGE = "Wallet with id %d is not managed by user with id %d.";
    private static final String WALLET_WITH_NO_USERS_EXCEPTION = "A wallet has to be managed by at least 1 user.";
    private static final String WALLET_ALREADY_MAIN_MESSAGE = "Wallet with id %d is already the main wallet for user %s.";

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
    public void createAdditionalWallet(String username, Wallet wallet) {

        User user = userRepository.getByUsername(username);
        wallet.getUsers().add(user);
        wallet.setMainWallet(false);
        walletRepository.addWallet(wallet);

    }

    @Override
    public void createMainWallet(String username, Wallet wallet) {

        User user = userRepository.getByUsername(username);
        wallet.getUsers().add(user);
        wallet.setMainWallet(true);
        walletRepository.addWallet(wallet);

    }

    @Override
    public void makeWalletMainWalletById(int walletId, String username) {

        Wallet oldMainWallet = walletRepository.getMainWalletByUsername(username);
        Wallet newMainWallet = walletRepository.getWalletById(walletId);

        if(newMainWallet == null) {
            throw new EntityNotFoundException("Wallet", "id", walletId);
        }

        if(newMainWallet.getId() == oldMainWallet.getId()) {
            throw new DuplicateEntityException(String.format(WALLET_ALREADY_MAIN_MESSAGE,walletId, username));
        }

        oldMainWallet.setMainWallet(false);
        walletRepository.updateWallet(oldMainWallet);

        newMainWallet.setMainWallet(true);
        walletRepository.updateWallet(newMainWallet);
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
    public void addUserToWallet(int walletId, int userIdToAdd) {

        Wallet wallet = walletRepository.getWalletWithUsersById(walletId);
        User userToAdd = userRepository.getById(userIdToAdd);

        boolean userExists = wallet.getUsers().stream()
                .anyMatch(user -> user.getId() == userToAdd.getId());

        if (userExists) {
            throw new DuplicateEntityException(String.format(USER_ALREADY_WALLET_HOLDER_MESSAGE, walletId, userIdToAdd));
        }

        wallet.getUsers().add(userToAdd);

        walletRepository.updateWallet(wallet);

    }

    @Override
    public void removeUserFromWallet(int walletId, int userIdToRemove) {

        Wallet wallet = walletRepository.getWalletWithUsersById(walletId);

        if (wallet.getUsers().size() == 1) {
            throw new InconsistentOperationException(WALLET_WITH_NO_USERS_EXCEPTION);
        }

        User userToRemove = userRepository.getById(userIdToRemove);

        User existingUser = wallet.getUsers().stream()
                .filter(user -> user.getId() == userToRemove.getId())
                .findFirst()
                .orElseThrow(() ->
                        new DuplicateEntityException(String.format(USER_NOT_WALLET_HOLDER_MESSAGE, walletId, userIdToRemove)));

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
