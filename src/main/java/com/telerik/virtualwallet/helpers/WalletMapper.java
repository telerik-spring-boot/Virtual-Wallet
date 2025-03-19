package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayForWalletDTO;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayMvcDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletCreateDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletMvcDisplayDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletPrivateDisplayDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletPublicDisplayDTO;
import com.telerik.virtualwallet.services.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class WalletMapper {

    private final WalletService walletService;
    private final UserMapper userMapper;

    @Autowired
    public WalletMapper(WalletService walletService, UserMapper userMapper) {
        this.walletService = walletService;
        this.userMapper = userMapper;
    }

    public WalletPrivateDisplayDTO walletToPrivateDto(Wallet wallet) {
        WalletPrivateDisplayDTO dto = new WalletPrivateDisplayDTO();

        List<String> walletHoldersUsernames = wallet.getUsers().stream()
                .map(User::getUsername)
                .toList();

        dto.setWalletHolders(walletHoldersUsernames);
        dto.setBalance(wallet.getBalance());
        dto.setCurrency(wallet.getCurrency());
        dto.setId(wallet.getId());

        return dto;
    }

    public WalletMvcDisplayDTO walletToMvcDto(Wallet wallet) {
        WalletMvcDisplayDTO dto = new WalletMvcDisplayDTO();

        List<UserDisplayForWalletDTO> walletHoldersUsernames = wallet.getUsers().stream()
                .map(userMapper::userToUserDisplayForWalletDTO).toList();

        dto.setWalletHolders(walletHoldersUsernames);
        dto.setCreator(userMapper.userToUserDisplayForWalletDTO(wallet.getCreator()));
        dto.setBalance(wallet.getBalance());
        dto.setCurrency(wallet.getCurrency().toString());
        dto.setId(wallet.getId());

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

    public Wallet createDtoToWallet(WalletCreateDTO dto) {

        Wallet wallet = new Wallet();
        wallet.setCurrency(dto.getCurrency());
        wallet.setBalance(BigDecimal.ZERO);

        return wallet;
    }
}
