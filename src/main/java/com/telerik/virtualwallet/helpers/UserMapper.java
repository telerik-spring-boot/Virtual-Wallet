package com.telerik.virtualwallet.helpers;


import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.RegisterDTO;
import com.telerik.virtualwallet.models.dtos.user.*;
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

        user.setFullName(dto.getFullName());
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


    public User dtoToUser(UserUpdateMvcDTO dto, String username) {
        User user = userService.getByUsername(username);


        if(dto.getFullName() != null){
            user.setFullName(dto.getFullName());
        }

        if(dto.getEmailAddress() != null){
            user.setEmail(dto.getEmailAddress());
        }

        if(dto.getPhoneNumber() != null){
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        return user;
    }

    public UserUpdateMvcDTO userToUserUpdateMvcDto (User user) {
        UserUpdateMvcDTO userUpdateDTO =  new UserUpdateMvcDTO();

        userUpdateDTO.setFullName(user.getFullName());
        userUpdateDTO.setEmailAddress(user.getEmail());
        userUpdateDTO.setPhoneNumber(user.getPhoneNumber());

        return userUpdateDTO;
    }

    public UserDisplayForTransactionsDTO userToUserDisplayForTransactionsDTO(User user){
        UserDisplayForTransactionsDTO dto = new UserDisplayForTransactionsDTO();

        dto.setUsername(user.getUsername());

        return dto;
    }

    public UserDisplayDTO userToUserDisplayDTO(User user){
        UserDisplayDTO dto = new UserDisplayDTO();

        dto.setEmailAddress(user.getEmail());

        dto.setUsername(user.getUsername());

        dto.setBlocked(user.isBlocked());

        dto.setPhoneNumber(user.getPhoneNumber());

        return dto;
    }

    public UserDisplayMvcDTO userToUserDisplayMvcDTO(User user){
        UserDisplayMvcDTO dto = new UserDisplayMvcDTO();

        dto.setFullName(user.getFullName());

        dto.setEmailAddress(user.getEmail());

        dto.setUsername(user.getUsername());

        dto.setBlocked(user.isBlocked());

        dto.setVerified(user.getVerification().isEmailVerified() && user.getVerification().isPicturesVerified());

        dto.setPhoneNumber(user.getPhoneNumber());

        return dto;
    }
}
