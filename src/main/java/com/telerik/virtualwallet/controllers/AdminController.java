package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.UserDisplayAdminDTO;
import com.telerik.virtualwallet.models.dtos.UserDisplayUserDTO;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import com.telerik.virtualwallet.services.admin.AdminService;
import com.telerik.virtualwallet.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final UserMapper userMapper;


    public AdminController(AdminService adminService, UserService userService, UserMapper userMapper){
        this.adminService = adminService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDisplayAdminDTO>> getAllUsers(FilterUserOptions filterOptions,
                                                                 @PageableDefault(sort="username", direction = Sort.Direction.ASC) Pageable pageable) {


        Page<User> res = adminService.getAllUsers(filterOptions, pageable);

        List<UserDisplayAdminDTO> userDisplayDTOs = res.getContent().stream()
                .map(userMapper::userToAdminDisplayDTO)
                .toList();

        return ResponseEntity.ok(new PageImpl<>(userDisplayDTOs, pageable, res.getTotalElements()));
    }


    @PutMapping("/users/{userId}/block")
    public ResponseEntity<User> blockUser(@PathVariable int userId){

        User userToBlock = userService.getById(userId);

        adminService.blockUser(userToBlock);

        return ResponseEntity.ok(userToBlock);
    }

    @PutMapping("/users/{userId}/unblock")
    public ResponseEntity<User> unblockUser(@PathVariable int userId){

        User userToBlock = userService.getById(userId);

        adminService.unblockUser(userToBlock);

        return ResponseEntity.ok(userToBlock);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        // to do pagination / filter once done
        return ResponseEntity.ok(adminService.getAllTransactions());
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

}
