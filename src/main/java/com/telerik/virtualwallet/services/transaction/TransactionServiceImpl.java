package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.InsufficientFundsException;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.repositories.transaction.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String NO_TRANSACTIONS_MESSAGE = "No transactions are found.";
    private static final String NO_TRANSACTIONS_FOUND_MESSAGE = "No transactions associated with wallet with id %d found.";
    private static final String NO_TRANSACTIONS_TYPE_FOUND_MESSAGE = "No %s transactions associated with wallet with id %d found.";
    private static final String INSUFFICIENT_BALANCE_MESSAGE = "Your balance is not sufficient to send this transaction.";

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @Override
    public List<Transaction> getAllTransactions() {

        List<Transaction> allTransactions = transactionRepository.getAllTransactionsWithWallets();

        if (allTransactions.isEmpty()) {
            throw new EntityNotFoundException(NO_TRANSACTIONS_MESSAGE);
        }

        return allTransactions;

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
    public List<Transaction> getAllTransactionsByWalletId(int walletId) {

        List<Transaction> transactions = transactionRepository.getAllTransactionsWithWalletsByWalletId(walletId);

        if (transactions.isEmpty()) {
            throw new EntityNotFoundException(String.format(NO_TRANSACTIONS_FOUND_MESSAGE, walletId));
        }

        return transactions;

    }

    @Override
    public List<Transaction> getAllOutgoingTransactionsByWalletId(int walletId) {

        List<Transaction> transactions = transactionRepository.getAllOutgoingTransactionsWithWalletsByWalletId(walletId);

        if (transactions.isEmpty()) {
            throw new EntityNotFoundException(String.format(NO_TRANSACTIONS_TYPE_FOUND_MESSAGE, "outgoing", walletId));
        }

        return transactions;

    }

    @Override
    public List<Transaction> getAllIncomingTransactionsByWalletId(int walletId) {

        List<Transaction> transactions = transactionRepository.getAllIncomingTransactionsWithWalletsByWalletId(walletId);

        if (transactions.isEmpty()) {
            throw new EntityNotFoundException(String.format(NO_TRANSACTIONS_TYPE_FOUND_MESSAGE, "incoming", walletId));
        }

        return transactions;

    }

    @Transactional
    @Override
    public void makeTransaction(Transaction transaction) {

        BigDecimal senderBalanceBefore = transaction.getSenderWallet().getBalance();
        BigDecimal receiverBalanceBefore = transaction.getReceiverWallet().getBalance();

        if (transaction.getAmount().compareTo(senderBalanceBefore) > 0) {
            throw new InsufficientFundsException(INSUFFICIENT_BALANCE_MESSAGE);
        }

        transaction.getSenderWallet().setBalance(senderBalanceBefore.subtract(transaction.getAmount()));
        transaction.getReceiverWallet().setBalance(receiverBalanceBefore.add(transaction.getAmount()));

        transactionRepository.createTransaction(transaction);

    }
}
