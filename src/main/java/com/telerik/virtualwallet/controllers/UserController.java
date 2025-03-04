package com.telerik.virtualwallet.controllers;


import com.telerik.virtualwallet.helpers.StockMapper;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.helpers.WalletMapper;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.stock.StockDisplayDTO;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderDTO;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayDTO;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayForTransactionsDTO;
import com.telerik.virtualwallet.models.dtos.user.UserUpdateDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletDisplayDTO;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import com.telerik.virtualwallet.services.admin.AdminService;
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


    public UserController(UserService userService, UserMapper userMapper, AdminService adminService, StockMapper stockMapper, WalletService walletService, WalletMapper walletMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.adminService = adminService;
        this.stockMapper = stockMapper;
        this.walletService = walletService;
        this.walletMapper = walletMapper;
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

    @GetMapping("/username/wallets")
    @PreAuthorize("hasRole('ADMIN') OR #username == authentication.name")
    public ResponseEntity<List<WalletDisplayDTO>> getWalletsByUsername(@RequestParam String username) {

        List<Wallet> wallets = walletService.getWalletsByUsername(username);

        return ResponseEntity.ok(wallets.stream()
                .map(walletMapper::walletToDto)
                .toList());

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
