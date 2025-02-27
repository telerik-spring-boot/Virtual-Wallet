package com.telerik.virtualwallet.services.user;

import com.telerik.virtualwallet.models.User;


public interface UserService {


    User getById(int id);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    User getByUsername(String username);

    void create(User user);

    void update(User user);

    void delete(int id);


}
