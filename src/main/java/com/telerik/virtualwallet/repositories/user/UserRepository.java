package com.telerik.virtualwallet.repositories.user;

import com.telerik.virtualwallet.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    void create(User user);

    void update(User user);

    void delete(int id);
}
