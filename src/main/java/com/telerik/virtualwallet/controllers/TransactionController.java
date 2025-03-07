package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionCreateDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionDisplayDTO;
import com.telerik.virtualwallet.services.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasRole('ADMIN') OR " +
            "@transactionSecurityService.isUserTransactionParticipant(#transactionId, authentication.name)")
    public ResponseEntity<TransactionDisplayDTO> getTransactionsById(@PathVariable int transactionId) {

        Transaction transaction = transactionService.getTransactionById(transactionId);

        return ResponseEntity.ok(transactionMapper.transactionToTransactionDisplayDTO(transaction));
    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#dto.walletSenderId, authentication.name)")
    @PostMapping()
    public ResponseEntity<TransactionDisplayDTO> createTransaction(@RequestBody TransactionCreateDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Transaction transaction = transactionMapper.createDtoToTransaction(dto, auth.getName());

        transactionService.makeTransaction(transaction);

        return ResponseEntity.ok(transactionMapper.transactionToTransactionDisplayDTO(transactionService.getTransactionById(transaction.getId())));
    }


}
