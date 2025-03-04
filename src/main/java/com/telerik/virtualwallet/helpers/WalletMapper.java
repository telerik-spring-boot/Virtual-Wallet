package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.wallet.WalletDisplayDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletPublicDisplayDTO;
import com.telerik.virtualwallet.services.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WalletMapper {

    private final WalletService walletService;

    @Autowired
    public WalletMapper(WalletService walletService) {
        this.walletService = walletService;
    }

    public WalletDisplayDTO walletToPrivateDto(Wallet wallet) {
        WalletDisplayDTO dto = new WalletDisplayDTO();

        List<String> walletHoldersUsernames = wallet.getUsers().stream()
                .map(User::getUsername)
                .toList();

        dto.setWalletHolders(walletHoldersUsernames);
        dto.setBalance(wallet.getBalance());
        dto.setCurrency(wallet.getCurrency());

        return dto;
    }

    public WalletPublicDisplayDTO walletToPublicDto(Wallet wallet) {
        WalletPublicDisplayDTO dto = new WalletPublicDisplayDTO();

        List<String> walletHoldersUsernames = wallet.getUsers().stream()
                .map(User::getUsername)
                .toList();

        dto.setWalletHolders(walletHoldersUsernames);
        dto.setCurrency(wallet.getCurrency());

        return dto;
    }
}
