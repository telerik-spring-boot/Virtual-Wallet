package com.telerik.virtualwallet.controllers;


import com.telerik.virtualwallet.helpers.*;
import com.telerik.virtualwallet.models.*;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.models.dtos.stock.StockDisplayDTO;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionDisplayDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransferDisplayDTO;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayDTO;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayForTransactionsDTO;
import com.telerik.virtualwallet.models.dtos.user.UserUpdateDTO;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import com.telerik.virtualwallet.models.filters.FilterTransferOptions;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import com.telerik.virtualwallet.services.admin.AdminService;
import com.telerik.virtualwallet.services.card.CardService;
import com.telerik.virtualwallet.services.transaction.TransactionService;
import com.telerik.virtualwallet.services.transaction.TransferService;
import com.telerik.virtualwallet.services.user.UserService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AdminService adminService;
    private final StockMapper stockMapper;
    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final TransactionService transactionService;
    private final TransferService transferService;
    private final TransactionMapper transactionMapper;
    private final CardService cardService;
    private final CardMapper cardMapper;


    public UserController(UserService userService, UserMapper userMapper, AdminService adminService, StockMapper stockMapper, WalletService walletService, WalletMapper walletMapper, TransactionService transactionService, TransferService transferService, TransactionMapper transactionMapper, CardService cardService, CardMapper cardMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.adminService = adminService;
        this.stockMapper = stockMapper;
        this.walletService = walletService;
        this.walletMapper = walletMapper;
        this.transactionService = transactionService;
        this.transferService = transferService;
        this.transactionMapper = transactionMapper;
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<UserDisplayDTO> updateUser(
            @PathVariable String username,
            @RequestBody UserUpdateDTO userUpdateDTO) {

        User userToBeUpdated = userMapper.dtoToUser(userUpdateDTO, username);

        userService.update(userToBeUpdated);

        return ResponseEntity.ok(userMapper.userToUserDisplayDTO(userToBeUpdated));
    }


    @GetMapping("/{username}")
    public ResponseEntity<UserDisplayDTO> getUserByUsername(@PathVariable String username) {

        User user = userService.getByUsername(username);

        return ResponseEntity.ok(userMapper.userToUserDisplayDTO(user));
    }


    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') OR #username == authentication.name")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {

        userService.delete(username);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UserDisplayForTransactionsDTO>> getAllUsers(FilterUserOptions filterOptions,
                                                                           @PageableDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<User> res = adminService.getAllUsers(filterOptions, pageable);

        List<UserDisplayForTransactionsDTO> userDisplayDTOs = res.getContent().stream()
                .map(userMapper::userToUserDisplayForTransactionsDTO)
                .toList();

        return ResponseEntity.ok(new PageImpl<>(userDisplayDTOs, pageable, res.getTotalElements()));
    }

    @GetMapping("/{username}/wallets")
    public ResponseEntity<List<?>> getWalletsByUsername(@PathVariable String username) {

        List<Wallet> wallets = walletService.getWalletsByUsername(username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = auth.getName().equals(username);

        if (isAdmin || isOwner) {

            return ResponseEntity.ok(wallets.stream()
                    .map(walletMapper::walletToPrivateDto)
                    .toList());

        } else {

            return ResponseEntity.ok(wallets.stream()
                    .map(walletMapper::walletToPublicDto)
                    .toList());

        }

    }

    @GetMapping("/{username}/transactions")
    @PreAuthorize("hasRole('ADMIN') OR #username == authentication.name")
    public ResponseEntity<Page<TransactionDisplayDTO>> getAllTransactionsByUsername(@PathVariable String username, FilterTransactionsOptions filterOptions,
                                                                                    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Transaction> transactions = transactionService.getTransactionsByUsername(filterOptions, pageable, username);

        List<TransactionDisplayDTO> transactionDisplayDTOs = transactions.getContent().stream()
                .map(transactionMapper::transactionToTransactionDisplayDTO)
                .toList();
        return ResponseEntity.ok(new PageImpl<>(transactionDisplayDTOs, pageable, transactions.getTotalElements()));

    }

    @GetMapping("/{username}/transfers")
    @PreAuthorize("hasRole('ADMIN') OR #username == authentication.name")
    public ResponseEntity<Page<TransferDisplayDTO>> getAllTransfersByUsername(@PathVariable String username, FilterTransferOptions filterOptions,
                                                                                 @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Transfer> transfers = transferService.getAllTransfersByUsername(filterOptions, pageable, username);

        List<TransferDisplayDTO> transferDisplayDTOs = transfers.getContent().stream()
                .map(transactionMapper::transferToTransferDisplayDTO)
                .toList();
        return ResponseEntity.ok(new PageImpl<>(transferDisplayDTOs, pageable, transfers.getTotalElements()));

    }



    @GetMapping("/{username}/cards")
    @PreAuthorize("hasRole('ADMIN') OR #username == authentication.name")
    public ResponseEntity<List<CardDisplayDTO>> getAllCardsByUsername(@PathVariable String username) {

        List<Card> cards = cardService.getCardsByUsername(username);

        List<CardDisplayDTO> cardDisplayDTOs = cards.stream()
                .map(cardMapper::cardToCardDisplayDTO)
                .toList();

        return ResponseEntity.ok(cardDisplayDTOs);

    }

    @PutMapping("/{username}/wallets/{walletId}/stocks")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<String> purchaseStocks(@Valid @RequestBody List<StockOrderDTO> purchaseList, @PathVariable int walletId, @PathVariable String username) {

        userService.purchaseStocks(username, walletId, purchaseList);

        return ResponseEntity.ok("Stocks purchased successfully.");
    }

    @DeleteMapping("/{username}/wallets/{walletId}/stocks")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<String> sellStocks(@Valid @RequestBody List<StockOrderDTO> purchaseList, @PathVariable int walletId, @PathVariable String username) {

        userService.sellStocks(username, walletId, purchaseList);

        return ResponseEntity.ok("Stocks sold successfully.");
    }

    @GetMapping("/{username}/stocks")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<List<StockDisplayDTO>> getUserPortfolio(@PathVariable String username) {

        List<StockDisplayDTO> result = userService.getUserWithStocks(username).getStocks()
                .stream()
                .map(stockMapper::stockToDto)
                .toList();

        return ResponseEntity.ok().body(result);
    }

}
