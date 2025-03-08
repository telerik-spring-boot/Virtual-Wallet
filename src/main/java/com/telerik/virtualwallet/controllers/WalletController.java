package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.helpers.WalletMapper;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionDisplayDTO;
import com.telerik.virtualwallet.models.dtos.wallet.CardTransferCreateDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletPrivateDisplayDTO;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import com.telerik.virtualwallet.services.security.WalletSecurityService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final WalletSecurityService walletSecurityService;


    @Autowired
    public WalletController(WalletService walletService, WalletMapper walletMapper, TransactionMapper transactionMapper, TransactionService transactionService, WalletSecurityService walletSecurityService) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;

        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
        this.walletSecurityService = walletSecurityService;
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<?> getWalletById(@PathVariable int walletId) {

        Wallet wallet = walletService.getWalletById(walletId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = walletSecurityService.isUserWalletHolder(walletId, auth.getName());

        if (isAdmin || isOwner) {
            return ResponseEntity.ok(walletMapper.walletToPrivateDto(wallet));
        } else {
            return ResponseEntity.ok(walletMapper.walletToPublicDto(wallet));
        }
    }

    @GetMapping("/{walletId}/transactions")
    @PreAuthorize("hasRole('ADMIN') OR @walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    public ResponseEntity<Page<TransactionDisplayDTO>> getAllTransactionsByWalletId(@PathVariable int walletId, FilterTransactionsOptions filterOptions,
                                                                                    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Transaction> transactions = transactionService.getTransactionsByWalletId(filterOptions, pageable, walletId);

        List<TransactionDisplayDTO> transactionDisplayDTOs = transactions.getContent().stream()
                .map(transactionMapper::transactionToTransactionDisplayDTO)
                .toList();
        return ResponseEntity.ok(new PageImpl<>(transactionDisplayDTOs, pageable, transactions.getTotalElements()));

    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @PostMapping("/{walletId}/users/{userId}")
    public ResponseEntity<WalletPrivateDisplayDTO> addUserToWallet(@PathVariable int walletId, @PathVariable int userId) {

        walletService.addUserToWallet(walletId, userId);
        return ResponseEntity.ok(walletMapper.walletToPrivateDto(walletService.getWalletById(walletId)));

    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @DeleteMapping("/{walletId}/users/{userId}")
    public ResponseEntity<WalletPrivateDisplayDTO> removeUserFromWallet(@PathVariable int walletId, @PathVariable int userId) {

        walletService.removeUserFromWallet(walletId, userId);
        return ResponseEntity.ok(walletMapper.walletToPrivateDto(walletService.getWalletById(walletId)));

    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name) AND" +
            "@cardSecurityService.isUserCardHolder(#cardId, authentication.name)")
    @PostMapping("/{walletId}/cards/{cardId}")
    public ResponseEntity<WalletPrivateDisplayDTO> addMoneyToWalletFromSavedCard(@PathVariable int walletId,
                                                                                 @PathVariable int cardId,
                                                                                 @RequestBody CardTransferCreateDTO dto) {

        walletService.addFundsToWallet(walletId, cardId, dto.getAmount());

        return ResponseEntity.ok(walletMapper.walletToPrivateDto(walletService.getWalletById(walletId)));
    }


}
