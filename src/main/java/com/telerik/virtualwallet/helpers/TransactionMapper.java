package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.*;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.models.dtos.transaction.*;
import com.telerik.virtualwallet.services.exchangeRate.ExchangeRateService;
import com.telerik.virtualwallet.services.transactionCategory.TransactionCategoryService;
import com.telerik.virtualwallet.services.user.UserService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionCategoryService transactionCategoryService;
    private final CardMapper cardMapper;
    private final ExchangeRateService exchangeRateService;

    @Autowired
    public TransactionMapper(UserService userService, WalletService walletService, TransactionCategoryService transactionCategoryService, CardMapper cardMapper, ExchangeRateService exchangeRateService) {
        this.userService = userService;
        this.walletService = walletService;
        this.transactionCategoryService = transactionCategoryService;
        this.cardMapper = cardMapper;
        this.exchangeRateService = exchangeRateService;
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

    public Transaction mvcDtoToTransaction(TransactionConfirmationMVCCreateDTO dto, String usernameSender) {
        Transaction transaction = new Transaction();

        transaction.setAmount(dto.getSentAmount());
        transaction.setMessage(dto.getMessage());
        transaction.setUserSender(userService.getByUsername(usernameSender));

        transaction.setSenderWallet(walletService.getWalletById(dto.getWalletSenderId()));
        transaction.setReceiverWallet(walletService.getWalletById(dto.getWalletReceiverId()));

        transaction.setTransactionCategory(transactionCategoryService.getTransactionCategoryByName(dto.getTransactionCategory()));

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


    public TransactionsWrapper transferToTransactionWrapper(Transfer transfer) {
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
        transactionsWrapper.setTransactionCategory("Top Up");
        transactionsWrapper.setTransactionTime(transfer.getCreatedAt());

        return transactionsWrapper;
    }

    public TransactionsWrapper transactionToTransactionWrapper(Transaction transaction) {
        TransactionsWrapper transactionsWrapper = new TransactionsWrapper();

        transactionsWrapper.setTransactionId(transaction.getId());
        transactionsWrapper.setAmount(transaction.getAmount());
        transactionsWrapper.setCurrency(transaction.getSenderWallet().getCurrency());
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

    public List<InvestmentDTO> investmentToInvestmentDTO(Investment investment) {

        List<InvestmentDTO> investments = new ArrayList<>();

        String[] symbols = investment.getSymbols().split(",");
        String[] quantities = investment.getQuantities().split(",");
        String[] values = investment.getStockValues().split(",");

        for (int i = 0; i < symbols.length; i++) {
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

    public TransactionConfirmationMVCCreateDTO handleConfirmationMVCDTOLogic(int senderWalletId, Wallet receiverWallet,
                                                                             User receiverUser, BigDecimal sendingAmount) {
        BigDecimal EUR_TO_USD = BigDecimal.valueOf(1.09);
        BigDecimal GBP_TO_USD = BigDecimal.valueOf(1.30);
        BigDecimal GBP_TO_EUR = GBP_TO_USD.divide(EUR_TO_USD, RoundingMode.HALF_UP);

        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();

        for (ExchangeRate exchangeRate : exchangeRates) {
            if (exchangeRate.getFromCurrency().equals("EUR")
                    && exchangeRate.getToCurrency().equals("USD")) {
                EUR_TO_USD = BigDecimal.valueOf(exchangeRate.getRate());
            } else if (exchangeRate.getFromCurrency().equals("GBP")
                    && exchangeRate.getToCurrency().equals("USD")) {
                GBP_TO_USD = BigDecimal.valueOf(exchangeRate.getRate());
            } else if (exchangeRate.getFromCurrency().equals("EUR")
                    && exchangeRate.getToCurrency().equals("GBP")) {
                GBP_TO_EUR = BigDecimal.valueOf(1 / exchangeRate.getRate());
            }
        }


        TransactionConfirmationMVCCreateDTO dto = new TransactionConfirmationMVCCreateDTO();

        Wallet senderWallet = walletService.getWalletById(senderWalletId);

        dto.setWalletSenderId(senderWalletId);
        dto.setWalletReceiverId(receiverWallet.getId());

        dto.setReceiverCurrency(receiverWallet.getCurrency().toString());
        dto.setSenderCurrency(senderWallet.getCurrency().toString());

        dto.setReceiverFullName(receiverUser.getFullName());
        dto.setSentAmount(sendingAmount.setScale(2, RoundingMode.HALF_UP));

        if (dto.getReceiverCurrency().equals(dto.getSenderCurrency())) {

            dto.setReceivedAmount(dto.getSentAmount());

        } else {

            switch (dto.getSenderCurrency()) {
                case "USD" -> {

                    if (dto.getReceiverCurrency().equals("EUR")) {
                        BigDecimal USD_TO_EUR = BigDecimal.ONE.divide(EUR_TO_USD, 10, RoundingMode.HALF_UP);
                        dto.setReceivedAmount(sendingAmount.multiply(USD_TO_EUR).setScale(2, RoundingMode.HALF_UP));
                    }

                    if (dto.getReceiverCurrency().equals("GBP")) {
                        BigDecimal USD_TO_GBP = BigDecimal.ONE.divide(GBP_TO_USD, 10, RoundingMode.HALF_UP);
                        dto.setReceivedAmount(sendingAmount.multiply(USD_TO_GBP).setScale(2, RoundingMode.HALF_UP));
                    }
                }
                case "EUR" -> {

                    if (dto.getReceiverCurrency().equals("USD")) {

                        dto.setReceivedAmount(sendingAmount.multiply(EUR_TO_USD).setScale(2, RoundingMode.HALF_UP));
                    }

                    if (dto.getReceiverCurrency().equals("GBP")) {
                        BigDecimal EUR_TO_GBP = BigDecimal.ONE.divide(GBP_TO_EUR, 10, RoundingMode.HALF_UP);
                        dto.setReceivedAmount(sendingAmount.multiply(EUR_TO_GBP).setScale(2, RoundingMode.HALF_UP));
                    }
                }
                case "GBP" -> {

                    if (dto.getReceiverCurrency().equals("USD")) {

                        dto.setReceivedAmount(sendingAmount.multiply(GBP_TO_USD).setScale(2, RoundingMode.HALF_UP));
                    }

                    if (dto.getReceiverCurrency().equals("EUR")) {

                        dto.setReceivedAmount(sendingAmount.multiply(GBP_TO_EUR).setScale(2, RoundingMode.HALF_UP));
                    }
                }
            }

        }

        return dto;


    }
}
