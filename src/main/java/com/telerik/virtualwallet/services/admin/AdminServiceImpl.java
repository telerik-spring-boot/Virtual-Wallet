package com.telerik.virtualwallet.services.admin;

import com.telerik.virtualwallet.exceptions.AdminRoleManagementException;
import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.InvalidSortParameterException;
import com.telerik.virtualwallet.models.Investment;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import com.telerik.virtualwallet.repositories.role.RoleRepository;
import com.telerik.virtualwallet.repositories.transaction.InvestmentRepository;
import com.telerik.virtualwallet.repositories.transaction.TransactionRepository;
import com.telerik.virtualwallet.repositories.transaction.TransferRepository;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.services.picture.PictureService;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    private static final String ALREADY_ADMIN_ERROR_MESSAGE = "User is already an admin.";
    private static final String NOT_ADMIN_ERROR_MESSAGE = "User is not an admin.";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;
    private final PictureService pictureService;
    private final InvestmentRepository investmentRepository;

    public AdminServiceImpl(UserRepository userRepository, TransferRepository transferRepository, RoleRepository roleRepository, TransactionRepository transactionRepository, PictureService pictureService, InvestmentRepository investmentRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.transactionRepository = transactionRepository;
        this.pictureService = pictureService;
        this.transferRepository = transferRepository;
        this.investmentRepository = investmentRepository;
    }


    @Override
    public Page<User> getAllUsers(FilterUserOptions options, Pageable pageable) {

        Sort.Order sortOrder = pageable.getSort().iterator().next();

        validateSortByFieldUser(sortOrder.getProperty());
        validateSortOrderField(sortOrder.getDirection().name());

        return userRepository.getAll(options, pageable);
    }

    @Override
    public List<User> getAllUsersMvc() {
        return userRepository.getAllMvc();
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return transferRepository.getAllTransfersMvc();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.getAllTransactionsWithWallets();
    }

    @Override
    public List<Investment> getAllInvestments() {
        return investmentRepository.getAllInvestments();
    }

    @Override
    public void blockUser(User user) {

        user.setBlocked(true);

        userRepository.update(user);

    }

    @Override
    public void unblockUser(User user) {

        user.setBlocked(false);

        userRepository.update(user);
    }

    @Override
    public void approveUserPictureVerification(String username) {

        User userToBeVerified = userRepository.getByUsername(username);

        if(userToBeVerified  == null){
            throw new EntityNotFoundException("User", "username", username);
        }

        if(userToBeVerified.getVerification().isPicturesVerified()){
            throw new DuplicateEntityException("User with username " + username + "is already verified!");
        }


        userToBeVerified.getVerification().setPicturesVerified(true);
        userToBeVerified.getVerification().setVerifiedAt(LocalDateTime.now());


        if(userToBeVerified.getReferredBy() != null){

            User referral = userToBeVerified.getReferredBy().getReferrer();
            referral.getMainWallet().setBalance(referral.getMainWallet().getBalance().add(BigDecimal.valueOf(50)));
            userToBeVerified.getMainWallet().setBalance(userToBeVerified.getMainWallet().getBalance().add(BigDecimal.valueOf(50)));

        }

        userRepository.update(userToBeVerified);
    }


    @Override
    public void rejectUserPictureVerification(String username) {

        User userToBeVerified = userRepository.getByUsername(username);

        if(userToBeVerified  == null){
            throw new EntityNotFoundException("User", "username", username);
        }

        userToBeVerified.getVerification().setPicturesVerified(false);

        pictureService.delete(username);

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

        userRepository.update(user);

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

        userRepository.update(user);

    }



    private void validateSortByFieldUser(String type) {
        if (!type.equalsIgnoreCase("email") &&
                !type.equalsIgnoreCase("phoneNumber") &&
                !type.equalsIgnoreCase("username")) {
            throw new InvalidSortParameterException(type);
        }
    }

    public void validateSortOrderField(String type) {
        if (!type.equalsIgnoreCase("asc") && !type.equalsIgnoreCase("desc")) {
            throw new InvalidSortParameterException(type);
        }
    }
}
