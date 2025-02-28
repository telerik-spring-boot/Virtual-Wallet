package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.services.admin.AdminService;
import com.telerik.virtualwallet.services.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;


    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUserById() {

        List<User> users = adminService.getAllUsers();

        return ResponseEntity.ok(users);
    }
}
