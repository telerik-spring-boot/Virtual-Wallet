package com.telerik.virtualwallet.helpers;


import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.RegisterDTO;
import com.telerik.virtualwallet.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }


    public User dtoToUser(RegisterDTO dto) {
        User user = new User();

        user.setUsername(dto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setEmail(dto.getEmailAddress());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }
}
