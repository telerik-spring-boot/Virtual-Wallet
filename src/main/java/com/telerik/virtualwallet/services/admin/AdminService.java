package com.telerik.virtualwallet.services.admin;

import com.telerik.virtualwallet.models.Investment;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {

    Page<User> getAllUsers(FilterUserOptions options, Pageable pageable);

    List<User> getAllUsersMvc();

    List<Transfer> getAllTransfers();

    List<Transaction> getAllTransactions();

    List<Investment> getAllInvestments();

    void blockUser(User user);

    void unblockUser(User user);

    void approveUserPictureVerification(String username);

    void rejectUserPictureVerification(String username);

    void revokeAdminRights(int userId);

    void giveAdminRights(int userId);

}
