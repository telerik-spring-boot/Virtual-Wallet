package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.helpers.CardMapper;
import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionDisplayDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransferDisplayDTO;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayDTO;
import com.telerik.virtualwallet.models.filters.FilterTransactionsOptions;
import com.telerik.virtualwallet.models.filters.FilterTransferOptions;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import com.telerik.virtualwallet.services.admin.AdminService;
import com.telerik.virtualwallet.services.card.CardService;
import com.telerik.virtualwallet.services.transaction.TransactionService;
import com.telerik.virtualwallet.services.transaction.TransferService;
import com.telerik.virtualwallet.services.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TransactionService transactionService;
    private final TransferService transferService;
    private final TransactionMapper transactionMapper;
    private final CardService cardService;
    private final CardMapper cardMapper;


    public AdminController(AdminService adminService, UserService userService, UserMapper userMapper, TransactionService transactionService, TransferService transferService, TransactionMapper transactionMapper, CardService cardService, CardMapper cardMapper){
        this.adminService = adminService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.transactionService = transactionService;
        this.transferService = transferService;
        this.transactionMapper = transactionMapper;
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDisplayDTO>> getAllUsers(FilterUserOptions filterOptions,
                                                            @PageableDefault(sort="username", direction = Sort.Direction.ASC) Pageable pageable) {


        Page<User> res = adminService.getAllUsers(filterOptions, pageable);

        List<UserDisplayDTO> userDisplayDTOs = res.getContent().stream()
                .map(userMapper::userToUserDisplayDTO)
                .toList();

        return ResponseEntity.ok(new PageImpl<>(userDisplayDTOs, pageable, res.getTotalElements()));
    }


    @PutMapping("/users/{userId}/block")
    public ResponseEntity<UserDisplayDTO> blockUser(@PathVariable int userId){

        User userToBlock = userService.getById(userId);

        adminService.blockUser(userToBlock);

        return ResponseEntity.ok(userMapper.userToUserDisplayDTO(userToBlock));
    }

    @PutMapping("/users/{userId}/unblock")
    public ResponseEntity<UserDisplayDTO> unblockUser(@PathVariable int userId){

        User userToUnblock = userService.getById(userId);

        adminService.unblockUser(userToUnblock);

        return ResponseEntity.ok(userMapper.userToUserDisplayDTO(userToUnblock));
    }

    @GetMapping("/transactions")
    public ResponseEntity<Page<TransactionDisplayDTO>> getAllTransactions(FilterTransactionsOptions filterOptions,
                                                                          @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Transaction> transactions = transactionService.getAllTransactions(filterOptions, pageable);

        List<TransactionDisplayDTO> transactionDisplayDTOs = transactions.getContent().stream()
                .map(transactionMapper::transactionToTransactionDisplayDTO)
                .toList();
        return ResponseEntity.ok(new PageImpl<>(transactionDisplayDTOs, pageable, transactions.getTotalElements()));
    }

    @GetMapping("/transfers")
    public ResponseEntity<Page<TransferDisplayDTO>> getAllTransfers(FilterTransferOptions filterOptions,
                                                                    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Transfer> transfers = transferService.getAllTransfers(filterOptions, pageable);

        List<TransferDisplayDTO> transferDisplayDTOs = transfers.getContent().stream()
                .map(transactionMapper::transferToTransferDisplayDTO)
                .toList();
        return ResponseEntity.ok(new PageImpl<>(transferDisplayDTOs, pageable, transfers.getTotalElements()));
    }

    @GetMapping("/cards")
    public ResponseEntity<List<CardDisplayDTO>> getAllCards(){

        List<Card> cards = cardService.getAllCards();

        List<CardDisplayDTO> cardDisplayDTOs = cards.stream()
                .map(cardMapper::cardToCardDisplayDTO)
                .toList();

        return ResponseEntity.ok(cardDisplayDTOs);
    }

    @PostMapping("/users/rights/{userId}")
    public ResponseEntity<Void> giveAdminRights(@PathVariable int userId) {

        adminService.giveAdminRights(userId);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/users/rights/{userId}")
    public ResponseEntity<Void> revokeAdminRights (@PathVariable int userId) {

        adminService.revokeAdminRights(userId);

        return ResponseEntity.ok().build();

    }

    @PutMapping("/users/{username}/verify")
    public ResponseEntity<String> verifyUserPictures(@PathVariable String username){

        adminService.approveUserPictureVerification(username);

        return ResponseEntity.ok("Successfully verified the pictures of user with username: " + username);
    }

    @DeleteMapping("/users/{username}/verify")
    public ResponseEntity<String> rejectUserPictures(@PathVariable String username){

        adminService.rejectUserPictureVerification(username);

        return ResponseEntity.ok("Successfully rejected the pictures of user with username: " + username);
    }

}
