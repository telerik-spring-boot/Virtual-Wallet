package com.telerik.virtualwallet.services.security;

import com.telerik.virtualwallet.repositories.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionSecurityService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionSecurityService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;

    }

    public boolean isUserTransactionParticipant(int transactionId, String username)
    {
        return transactionRepository.isUserTransactionParticipant(username,transactionId);
    }
}
