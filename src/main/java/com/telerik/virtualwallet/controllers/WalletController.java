package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.helpers.WalletMapper;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.WalletDisplayDTO;
import com.telerik.virtualwallet.services.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;



    @Autowired
    public WalletController(WalletService walletService, WalletMapper walletMapper) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;

    }


    @PreAuthorize("hasRole('ADMIN') OR @walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDisplayDTO> getUserById(@PathVariable int walletId) {
        Wallet wallet = walletService.getWalletById(walletId);
        return ResponseEntity.ok(walletMapper.walletToDto(wallet));
    }

}
