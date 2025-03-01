package com.telerik.virtualwallet.services.transaction;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.repositories.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @Override
    public List<Transaction> getAllTransactions() {
        return List.of();
    }

    @Override
    public Transaction getTransactionById(int id) {
        return null;
    }

    @Override
    public List<Transaction> getAllTransactionsByWalletId(int walletId) {
        return List.of();
    }

    @Override
    public List<Transaction> getAllOutgoingTransactionsByWalletId(int walletId) {
        return List.of();
    }

    @Override
    public List<Transaction> getAllIncomingTransactionsByWalletId(int walletId) {
        return List.of();
    }

    @Override
    public void makeTransaction(int userRequestId, Transaction transaction) {

    }


}
