package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.wallet.WalletDisplayDTO;
import com.telerik.virtualwallet.services.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    private final WalletService walletService;

    @Autowired
    public WalletMapper(WalletService walletService) {
        this.walletService = walletService;
    }

    public WalletDisplayDTO walletToDto(Wallet wallet) {
        WalletDisplayDTO dto = new WalletDisplayDTO();

        dto.setBalance(wallet.getBalance());
        dto.setCurrency(wallet.getCurrency());

        return dto;
    }
}
