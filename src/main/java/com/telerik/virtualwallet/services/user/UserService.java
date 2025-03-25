package com.telerik.virtualwallet.services.user;

import com.telerik.virtualwallet.models.Investment;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderDTO;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderMvcDTO;

import java.util.List;


public interface UserService {


    User getById(int id);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    User getByUsername(String username);

    User getByUsernameOrEmailOrPhoneNumberMVC(String uniqueField);

    User getUserWithStocks(String username);

    List<Investment> getInvestmentsByUsername(String username);

    void processStockOrderMvc(StockOrderMvcDTO stockOrder, String username);

    void purchaseStocks(String username, int walletId, List<StockOrderDTO> orderList);

    void sellStocks(String username, int walletId, List<StockOrderDTO> orderList);

    void verifyEmail(String email);

    void create(User user);

    void createWithReferral(User user, User referrer);

    void update(User user);

    void delete(String username);
}
