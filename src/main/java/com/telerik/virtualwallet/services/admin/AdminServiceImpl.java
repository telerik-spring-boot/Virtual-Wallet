package com.telerik.virtualwallet.services.admin;

import com.telerik.virtualwallet.exceptions.AdminRoleManagementException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.models.Role;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.repositories.role.RoleRepository;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.services.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    private static final String ALREADY_ADMIN_ERROR_MESSAGE = "User is already an admin.";
    private static final String NOT_ADMIN_ERROR_MESSAGE = "User is not an admin.";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserService userService){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public void blockUser(User user) {

        user.setBlocked(true);

        userService.update(user);

    }

    @Override
    public void unblockUser(User user) {

        user.setBlocked(false);

        userService.update(user);
    }

    @Override
    public void approveUserPictureVerification(int userId) {

        User userToBeVerified = userRepository.getById(userId);

        if(userToBeVerified  == null){
            throw new EntityNotFoundException("User", "id", userId);
        }

        userToBeVerified.getVerification().setPicturesVerified(true);

        userRepository.update(userToBeVerified);
    }


    @Override
    public void rejectUserPictureVerification(int userId) {

        User userToBeVerified = userRepository.getById(userId);

        if(userToBeVerified  == null){
            throw new EntityNotFoundException("User", "id", userId);
        }

        userToBeVerified.getVerification().setPicturesVerified(false);

        userRepository.update(userToBeVerified);
    }

    @Override
    public void revokeAdminRights(int userId) {
        User user = userRepository.getByIdWithRoles(userId);

        if (user == null) {
            throw new EntityNotFoundException("User", "user.id", userId);
        }

        if(user.getRoles().isEmpty()){
            throw new AdminRoleManagementException(NOT_ADMIN_ERROR_MESSAGE);
        }

        user.removeRole(roleRepository.getRoleByName("ROLE_ADMIN"));

        userService.update(user);

    }

    @Override
    public void giveAdminRights(int userId) {

        User user = userRepository.getByIdWithRoles(userId);

        if (user == null) {
            throw new EntityNotFoundException("User", "user.id", userId);
        }

        if(!user.getRoles().isEmpty()){
            throw new AdminRoleManagementException(ALREADY_ADMIN_ERROR_MESSAGE);
        }

        user.addRole(roleRepository.getRoleByName("ROLE_ADMIN"));

        userService.update(user);

    }
}
