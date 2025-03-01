package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import com.telerik.virtualwallet.services.admin.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;


    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(FilterUserOptions filterOptions,
                                                  @PageableDefault(sort="username", direction = Sort.Direction.ASC) Pageable pageable) {


        return ResponseEntity.ok(adminService.getAllUsers(filterOptions, pageable));
    }
}
