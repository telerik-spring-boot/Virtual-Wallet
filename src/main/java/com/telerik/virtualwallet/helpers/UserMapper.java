package com.telerik.virtualwallet.helpers;


import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.RegisterDTO;
import com.telerik.virtualwallet.models.dtos.UserDisplayAdminDTO;
import com.telerik.virtualwallet.models.dtos.UserDisplayUserDTO;
import com.telerik.virtualwallet.models.dtos.UserUpdateDTO;
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

    public User dtoToUser(UserUpdateDTO dto, String username) {
        User user = userService.getByUsername(username);

        if(dto.getPassword() != null){
            user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        }

        if(dto.getEmailAddress() != null){
            user.setEmail(dto.getEmailAddress());
        }

        if(dto.getPhoneNumber() != null){
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        return user;
    }

    public UserDisplayUserDTO userToUserDisplayDTO(User user){
        UserDisplayUserDTO dto = new UserDisplayUserDTO();

        dto.setUsername(user.getUsername());

        return dto;
    }

    public UserDisplayAdminDTO userToAdminDisplayDTO(User user){
        UserDisplayAdminDTO dto = new UserDisplayAdminDTO();

        dto.setEmailAddress(user.getEmail());

        dto.setUsername(user.getUsername());

        dto.setPhoneNumber(user.getPhoneNumber());

        return dto;
    }
}
