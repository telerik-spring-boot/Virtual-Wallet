package com.telerik.virtualwallet.services.admin;

import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    Page<User> getAllUsers(FilterUserOptions options, Pageable pageable);

    // List Of Transactions

    void blockUser(User user);

    void unblockUser(User user);

    void approveUserPictureVerification(int userId);

    void rejectUserPictureVerification(int userId);

    void revokeAdminRights(int userId);

    void giveAdminRights(int userId);

}
