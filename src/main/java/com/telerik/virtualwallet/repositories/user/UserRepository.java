package com.telerik.virtualwallet.repositories.user;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserRepository {

    Page<User> getAll(FilterUserOptions options, Pageable pageable);

    List<User> getAllMvc();

    User getById(int id);

    User getByIdWithRoles(int id);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    User getByUsername(String username);

    User getByUsernameWithRoles(String username);

    User getUserWithStocks(String username);

    User getUserWithStocksAndWalletsAndInvestments(String username);

    User getUserWithWallets(String username);

    List<User> getByAnyUniqueField(String username, String email, String phone);

    void create(User user);

    void update(User user);

    void delete(User user);

}
