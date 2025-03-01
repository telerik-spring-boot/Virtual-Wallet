package com.telerik.virtualwallet.services.user;

import com.telerik.virtualwallet.models.Stock;
import com.telerik.virtualwallet.models.User;

import java.util.List;


public interface UserService {


    User getById(int id);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    User getByUsername(String username);

    User getUserWithStocks(int id);

    void purchaseStocks(int userId, int walletId, List<String> symbols, List<Integer> quantities);

    void sellStocks(int userId, int walletId, List<String> symbols, List<Integer> quantities);

    void verifyEmail(int id);

    void create(User user);

    void update(User user);

    void delete(String username);


}
