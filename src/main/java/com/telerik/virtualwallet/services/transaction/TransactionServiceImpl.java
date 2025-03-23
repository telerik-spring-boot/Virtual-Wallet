package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.IncompatibleCurrenciesException;
import com.telerik.virtualwallet.exceptions.InsufficientFundsException;
import com.telerik.virtualwallet.exceptions.InvalidSortParameterException;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import com.telerik.virtualwallet.repositories.transaction.TransactionRepository;
import com.telerik.virtualwallet.repositories.wallet.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String INSUFFICIENT_BALANCE_MESSAGE = "Your balance is not sufficient to send this transaction.";
    private static final String CURRENCIES_DO_NOT_MATCH = "Currencies do not match";

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;


    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }


    @Override
    public Page<Transaction> getAllTransactions(FilterTransactionsOptions options, Pageable pageable) {

        pageableHelper(pageable);

        return transactionRepository.getAllTransactionsWithWallets(options, pageable);

    }

    @Override
    public Page<Transaction> getTransactionsByUsername(FilterTransactionsOptions options, Pageable pageable, String username) {

        pageableHelper(pageable);

        return transactionRepository.getAllTransactionsWithWalletsByUsername(options, pageable, username);
    }

    @Override
    public List<Transaction> getTransactionsByUsername(String username) {

        return transactionRepository.getAllTransactionsWithWalletsByUsername(username);
    }

    @Override
    public List<Transaction> getIncomingTransactionsByUsername(String username) {

        return transactionRepository.getIncomingTransactionsWithWalletsByUsername(username);
    }

    @Override
    public List<Transaction> getOutgoingTransactionsByUsername(String username) {

        return transactionRepository.getOutgoingTransactionsWithWalletsByUsername(username);
    }

    @Override
    public Transaction getTransactionById(int id) {

        Transaction transaction = transactionRepository.getTransactionWithWalletsById(id);

        if (transaction == null) {
            throw new EntityNotFoundException("Transaction", "id", id);
        }

        return transaction;

    }

    @Override
    public Page<Transaction> getTransactionsByWalletId(FilterTransactionsOptions options, Pageable pageable, int walletId) {

        pageableHelper(pageable);

        return transactionRepository.getAllTransactionsWithWalletsByWalletId(options, pageable, walletId);

    }


    @Transactional
    @Override
    public void makeTransaction(Transaction transaction) {

        if (!transaction.getSenderWallet().getCurrency().equals(transaction.getReceiverWallet().getCurrency())) {
            throw new IncompatibleCurrenciesException(CURRENCIES_DO_NOT_MATCH);
        }

        BigDecimal senderBalanceBefore = transaction.getSenderWallet().getBalance();
        BigDecimal receiverBalanceBefore = transaction.getReceiverWallet().getBalance();

        if (transaction.getAmount().compareTo(senderBalanceBefore) > 0) {
            throw new InsufficientFundsException(INSUFFICIENT_BALANCE_MESSAGE);
        }

        transaction.getSenderWallet().setBalance(senderBalanceBefore.subtract(transaction.getAmount()));
        transaction.getReceiverWallet().setBalance(receiverBalanceBefore.add(transaction.getAmount()));

        walletRepository.updateWallet(transaction.getSenderWallet());
        walletRepository.updateWallet(transaction.getReceiverWallet());

        transactionRepository.createTransaction(transaction);

    }

    @Transactional
    @Override
    public void makeTransactionMVC(Transaction transaction, BigDecimal receivedAmount) {

        BigDecimal senderBalanceBefore = transaction.getSenderWallet().getBalance();
        BigDecimal receiverBalanceBefore = transaction.getReceiverWallet().getBalance();

        if (transaction.getAmount().compareTo(senderBalanceBefore) > 0) {
            throw new InsufficientFundsException(INSUFFICIENT_BALANCE_MESSAGE);
        }

        transaction.getSenderWallet().setBalance(senderBalanceBefore.subtract(transaction.getAmount()));
        transaction.getReceiverWallet().setBalance(receiverBalanceBefore.add(receivedAmount));

        walletRepository.updateWallet(transaction.getSenderWallet());
        walletRepository.updateWallet(transaction.getReceiverWallet());

        transactionRepository.createTransaction(transaction);

    }

    public static void pageableHelper(Pageable pageable) {
        Sort.Order sortOrder = pageable.getSort().iterator().next();

        validateSortByFieldTransaction(sortOrder.getProperty());
        validateSortOrderField(sortOrder.getDirection().name());
    }

    public static void validateSortByFieldTransaction(String type) {
        if (!type.equalsIgnoreCase("createdAt") &&
                !type.equalsIgnoreCase("amount")) {
            throw new InvalidSortParameterException(type);
        }
    }

    public static void validateSortOrderField(String type) {
        if (!type.equalsIgnoreCase("asc") && !type.equalsIgnoreCase("desc")) {
            throw new InvalidSortParameterException(type);
        }
    }


}
