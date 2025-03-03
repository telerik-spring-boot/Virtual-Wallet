package com.telerik.virtualwallet;

import com.telerik.virtualwallet.models.Role;
import com.telerik.virtualwallet.models.User;


public class DummyObjectProvider {


    public static User createMockUser(){
        User user = new User();

        user.setId(1);
        user.setEmail("george.bush@gmail.com");
        user.setUsername("george_bush");
        user.setPassword("Password123!");
        user.setBlocked(false);
        user.setPhoneNumber("+0123456789");

        return user;
    }


    public static User createMockAdmin(){
        User user = new User();

        user.setId(2);
        user.setEmail("george.bush2@gmail.com");
        user.setUsername("george_bush2");
        user.setPassword("Password123!");
        user.setBlocked(false);
        user.setPhoneNumber("+0123456785");
        Role role = new Role("ROLE_ADMIN");
        role.setId(1);
        user.getRoles().add(role);

        return user;
    }
}
