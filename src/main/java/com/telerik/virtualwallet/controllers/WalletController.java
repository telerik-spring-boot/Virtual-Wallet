package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.helpers.WalletMapper;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionDisplayDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletDisplayDTO;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import com.telerik.virtualwallet.services.transaction.TransactionService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;



    @Autowired
    public WalletController(WalletService walletService, WalletMapper walletMapper, TransactionMapper transactionMapper, TransactionService transactionService) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;

        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
    }


    @PreAuthorize("hasRole('ADMIN') OR @walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDisplayDTO> getUserById(@PathVariable int walletId) {
        Wallet wallet = walletService.getWalletById(walletId);
        return ResponseEntity.ok(walletMapper.walletToPrivateDto(wallet));
    }

    @GetMapping("/{walletId}/transactions")
    @PreAuthorize("hasRole('ADMIN') OR @walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    public ResponseEntity<Page<TransactionDisplayDTO>> getAllTransactionsByWalletId(@PathVariable int walletId, FilterTransactionsOptions filterOptions,
                                                                                    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Transaction> transactions = transactionService.getTransactionsByWalletId(filterOptions,pageable,walletId);

        List<TransactionDisplayDTO> transactionDisplayDTOs = transactions.getContent().stream()
                .map(transactionMapper::transactionToTransactionDisplayDTO)
                .toList();
        return ResponseEntity.ok(new PageImpl<>(transactionDisplayDTOs, pageable, transactions.getTotalElements()));

    }



}
