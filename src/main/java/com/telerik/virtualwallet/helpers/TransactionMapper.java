package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.*;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.models.dtos.transaction.*;
import com.telerik.virtualwallet.services.transactionCategory.TransactionCategoryService;
import com.telerik.virtualwallet.services.user.UserService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
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


        transactionDisplayDTO.setTransactionId(transaction.getId());
        transactionDisplayDTO.setReceiverWalletId(transaction.getReceiverWallet().getId());
        transactionDisplayDTO.setTransactionTime(transaction.getCreatedAt());
        transactionDisplayDTO.setAmount(transaction.getAmount());
        transactionDisplayDTO.setSenderUsername(transaction.getUserSender().getUsername());
        transactionDisplayDTO.setTransactionCategory(transaction.getTransactionCategory().getName());
        transactionDisplayDTO.setCurrency(transaction.getSenderWallet().getCurrency());
        transactionDisplayDTO.setMessage(transaction.getMessage());

        List<String> senderUsernames = transaction.getSenderWallet().getUsers().stream()
                .map(User::getUsername)
                .toList();

        transactionDisplayDTO.setSenderWalletHolders(String.join(", ", senderUsernames));

        List<String> receiverUsernames = transaction.getReceiverWallet().getUsers().stream()
                .map(User::getUsername)
                .toList();

        transactionDisplayDTO.setReceiverWalletHolders(String.join(", ", receiverUsernames));

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

        transferDisplayDTO.setTransferId(transfer.getId());
        transferDisplayDTO.setReceiverWalletId(transfer.getId());
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


    public TransactionsWrapper transferToTransactionWrapper(Transfer transfer){
        TransactionsWrapper transactionsWrapper = new TransactionsWrapper();

        transactionsWrapper.setTransactionId(transfer.getId());
        transactionsWrapper.setAmount(transfer.getAmount());
        transactionsWrapper.setCurrency(transfer.getReceiverWallet().getCurrency());
        transactionsWrapper.setReceivers(String.join(", ", transfer.getReceiverWallet().getUsers().stream()
                .map(User::getUsername)
                .toList()));
        transactionsWrapper.setTransactionType("Deposit");
        transactionsWrapper.setSender("**** **** **** " + transfer.getSenderCard().getNumber().substring(12));
        transactionsWrapper.setMessage("No Message");
        transactionsWrapper.setReceiverWalletId(transfer.getReceiverWallet().getId());
        transactionsWrapper.setTransactionCategory("Deposit");
        transactionsWrapper.setTransactionTime(transfer.getCreatedAt());

        return transactionsWrapper;
    }

    public TransactionsWrapper transactionToTransactionWrapper(Transaction transaction){
        TransactionsWrapper transactionsWrapper = new TransactionsWrapper();

        transactionsWrapper.setTransactionId(transaction.getId());
        transactionsWrapper.setAmount(transaction.getAmount());
        transactionsWrapper.setCurrency(transaction.getReceiverWallet().getCurrency());
        transactionsWrapper.setReceivers(String.join(", ", transaction.getReceiverWallet().getUsers().stream()
                .map(User::getUsername)
                .toList()));
        transactionsWrapper.setTransactionType("Internal Transfer");
        transactionsWrapper.setSender(transaction.getUserSender().getUsername());
        transactionsWrapper.setMessage(transaction.getMessage());
        transactionsWrapper.setReceiverWalletId(transaction.getReceiverWallet().getId());
        transactionsWrapper.setTransactionCategory(transaction.getTransactionCategory().getName());
        transactionsWrapper.setTransactionTime(transaction.getCreatedAt());

        return transactionsWrapper;
    }

    public List<InvestmentDTO> investmentToInvestmentDTO(Investment investment){

        List<InvestmentDTO> investments = new ArrayList<>();

        String [] symbols = investment.getSymbols().split(",");
        String [] quantities = investment.getQuantities().split(",");
        String [] values = investment.getStockValues().split(",");

        for(int i = 0; i<symbols.length; i++){
            InvestmentDTO investmentDTO = new InvestmentDTO();

            investmentDTO.setId(investment.getId());

            investmentDTO.setPurchasedAt(investment.getPurchasedAt());

            investmentDTO.setUsername(investment.getUser().getUsername());

            investmentDTO.setSymbol(symbols[i]);

            investmentDTO.setQuantity(Double.parseDouble(quantities[i]));

            investmentDTO.setValue(Double.parseDouble(values[i]));

            investmentDTO.setTotalOrderValue(investment.getTotalValue());

            investmentDTO.setType(investment.getType());

            investments.add(investmentDTO);
        }


        return investments;
    }
}
