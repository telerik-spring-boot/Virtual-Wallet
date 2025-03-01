package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import com.telerik.virtualwallet.services.admin.AdminService;
import com.telerik.virtualwallet.services.user.UserService;
import org.springframework.data.domain.Page;
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


    public AdminController(AdminService adminService, UserService userService){
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(FilterUserOptions filterOptions,
                                                  @PageableDefault(sort="username", direction = Sort.Direction.ASC) Pageable pageable) {


        return ResponseEntity.ok(adminService.getAllUsers(filterOptions, pageable));
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

}
