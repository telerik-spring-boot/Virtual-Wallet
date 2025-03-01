package com.telerik.virtualwallet.controllers;


import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.UserDisplayUserDTO;
import com.telerik.virtualwallet.models.dtos.UserUpdateDTO;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import com.telerik.virtualwallet.services.admin.AdminService;
import com.telerik.virtualwallet.services.user.UserService;
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


    public UserController(UserService userService, UserMapper userMapper, AdminService adminService){
        this.userService = userService;
        this.userMapper = userMapper;
        this.adminService = adminService;
    }



    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<User> updateUser(
            @PathVariable String username,
            @RequestBody UserUpdateDTO userUpdateDTO) {

        User userToBeUpdated = userMapper.dtoToUser(userUpdateDTO, username);

        userService.update(userToBeUpdated);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {

        User user = userService.getByUsername(username);

        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') OR #username == authentication.name")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {

        userService.delete(username);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UserDisplayUserDTO>> getAllUsers(FilterUserOptions filterOptions,
                                                                @PageableDefault(sort="username", direction = Sort.Direction.ASC) Pageable pageable) {


        Page<User> res = adminService.getAllUsers(filterOptions, pageable);

        List<UserDisplayUserDTO> userDisplayDTOs = res.getContent().stream()
                .map(userMapper::userToUserDisplayDTO)
                .toList();

        return ResponseEntity.ok(new PageImpl<>(userDisplayDTOs, pageable, res.getTotalElements()));
    }
}
