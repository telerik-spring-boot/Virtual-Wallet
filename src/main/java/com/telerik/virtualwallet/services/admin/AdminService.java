package com.telerik.virtualwallet.services.admin;

import com.telerik.virtualwallet.models.User;

import java.util.List;

public interface AdminService {

    List<User> getAllUsers();

    // List Of Transactions

    void blockUser(User user);

    void unblockUser(User user);

    void approveUserPictureVerification(int userId);

    void rejectUserPictureVerification(int userId);

    void revokeAdminRights(int userId);

    void giveAdminRights(int userId);

}
