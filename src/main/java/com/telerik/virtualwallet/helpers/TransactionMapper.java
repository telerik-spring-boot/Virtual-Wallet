package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.*;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionCreateDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionDisplayDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransferDisplayDTO;
import com.telerik.virtualwallet.services.transactionCategory.TransactionCategoryService;
import com.telerik.virtualwallet.services.user.UserService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapper {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionCategoryService transactionCategoryService;
    private final CardMapper cardMapper;

    @Autowired
    public TransactionMapper(UserService userService, WalletService walletService, TransactionCategoryService transactionCategoryService, CardMapper cardMapper) {
        this.userService = userService;
        this.walletService = walletService;
        this.transactionCategoryService = transactionCategoryService;
        this.cardMapper = cardMapper;
    }

    public TransactionDisplayDTO transactionToTransactionDisplayDTO(Transaction transaction) {
        TransactionDisplayDTO transactionDisplayDTO = new TransactionDisplayDTO();
        transactionDisplayDTO.setTransactionTime(transaction.getCreatedAt());
        transactionDisplayDTO.setAmount(transaction.getAmount());
        transactionDisplayDTO.setSenderUsername(transaction.getUserSender().getUsername());
        transactionDisplayDTO.setTransactionCategory(transaction.getTransactionCategory().getName());
        transactionDisplayDTO.setCurrency(transaction.getSenderWallet().getCurrency());
        transactionDisplayDTO.setMessage(transaction.getMessage());

        List<String> senderUsernames = transaction.getSenderWallet().getUsers().stream()
                .map(User::getUsername)
                .toList();

        transactionDisplayDTO.setSenderWalletHolders(senderUsernames);

        List<String> receiverUsernames = transaction.getReceiverWallet().getUsers().stream()
                .map(User::getUsername)
                .toList();

        transactionDisplayDTO.setReceiverWalletHolders(receiverUsernames);

        return transactionDisplayDTO;
    }

    public Transaction createDtoToTransaction(TransactionCreateDTO dto, String username) {
        Transaction transaction = new Transaction();

        User userSender = userService.getByUsername(username);
        transaction.setUserSender(userSender);

        transaction.setAmount(dto.getAmount());
        transaction.setMessage(dto.getMessage());

        Wallet receiverWallet = walletService.getWalletById(dto.getWalletReceiverId());
        transaction.setReceiverWallet(receiverWallet);

        Wallet senderWallet = walletService.getWalletById(dto.getWalletSenderId());
        transaction.setSenderWallet(senderWallet);

        TransactionCategory category = transactionCategoryService.getTransactionCategory(dto.getTransactionCategoryId());
        transaction.setTransactionCategory(category);

        return transaction;

    }

    public TransferDisplayDTO transferToTransferDisplayDTO(Transfer transfer) {
        TransferDisplayDTO transferDisplayDTO = new TransferDisplayDTO();

        transferDisplayDTO.setTransferTime(transfer.getCreatedAt());
        transferDisplayDTO.setAmount(transfer.getAmount());
        transferDisplayDTO.setSenderUsername(transfer.getSenderCard().getUser().getUsername());

        CardDisplayDTO cardDisplayDTO = cardMapper.cardToCardDisplayDTO(transfer.getSenderCard());
        transferDisplayDTO.setCardNumber(cardDisplayDTO.getCardNumber());

        List<String> walletHoldersUsernames = transfer.getReceiverWallet().getUsers().stream()
                .map(User::getUsername)
                .toList();

        transferDisplayDTO.setReceiverWalletHolders(walletHoldersUsernames);

        return transferDisplayDTO;
    }
}
