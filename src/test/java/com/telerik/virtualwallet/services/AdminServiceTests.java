package com.telerik.virtualwallet.services;

import com.telerik.virtualwallet.exceptions.AdminRoleManagementException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.InvalidSortParameterException;
import com.telerik.virtualwallet.models.Role;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Verification;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import com.telerik.virtualwallet.repositories.role.RoleRepository;
import com.telerik.virtualwallet.repositories.transaction.TransactionRepository;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.services.admin.AdminServiceImpl;
import com.telerik.virtualwallet.services.picture.PictureService;
import com.telerik.virtualwallet.services.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

import static com.telerik.virtualwallet.DummyObjectProvider.createMockAdmin;
import static com.telerik.virtualwallet.DummyObjectProvider.createMockUser;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {


    @Mock
    private  UserRepository mockUserRepository;

    @Mock
    private  RoleRepository mockRoleRepository;

    @Mock
    private  UserService mockUserService;

    @Mock
    private  TransactionRepository mockTransactionRepository;

    @Mock
    private  PictureService mockPictureService;

    @InjectMocks
    private AdminServiceImpl adminService;


    @Test
    public void getAllUsers_Should_GetAllUsers_When_AllIsValid() {
        // Arrange
        User user = createMockUser();


        Mockito.when(mockUserRepository.getAll(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(user)));

        // Act
        Page<User> returnedUser = adminService.getAllUsers(new FilterUserOptions(null, null, null), PageRequest.of(0, 10, Sort.by("username").ascending()));

        // Assert
        Assertions.assertEquals(user, returnedUser.getContent().get(0));
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getAll(Mockito.any(), Mockito.any());
    }

    @Test
    public void getAllUsers_Should_Throw_When_SortNotValid() {

        // Arrange, Act, Assert
        Assertions.assertThrows(InvalidSortParameterException.class, () -> adminService.getAllUsers(new FilterUserOptions(null, null, null), PageRequest.of(0, 10, Sort.by("asdasfasf").ascending())));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .getAll(Mockito.any(), Mockito.any());
    }


    @Test
    public void getAllTransactionsShould_GetAllTransactions_When_AllIsValid() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setAmount(BigDecimal.TEN);


        Mockito.when(mockTransactionRepository.getAllTransactionsWithWallets())
                .thenReturn(List.of(transaction));

        // Act
        Transaction actualTransaction = adminService.getAllTransactions().get(0);

        // Assert
        Assertions.assertEquals(transaction, actualTransaction);
        Mockito.verify(mockTransactionRepository, Mockito.times(1))
                .getAllTransactionsWithWallets();
    }

    @Test
    public void blockUser_Should_BlockUser_When_Called(){
        // Arrange
        User user = createMockUser();

        // Act
        adminService.blockUser(user);

        // Assert
        Assertions.assertTrue(user.isBlocked());
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);
    }


    @Test
    public void unblockUser_Should_BlockUser_When_Called(){
        // Arrange
        User user = createMockUser();
        user.setBlocked(true);

        // Act
        adminService.unblockUser(user);

        // Assert
        Assertions.assertFalse(user.isBlocked());
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);
    }

    @Test
    public void approveUserPictureVerification_Should_VerifyPictures_When_AllIsValid(){
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getByUsername(user.getUsername()))
                .thenReturn(user);

        // Act
        adminService.approveUserPictureVerification(user.getUsername());

        // Assert
        Assertions.assertTrue(user.getVerification().isPicturesVerified());
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);

    }

    @Test
    public void approveUserPictureVerification_Should_Throw_When_UserNotFound(){
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getByUsername(user.getUsername()))
                .thenReturn(null);


        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> adminService.approveUserPictureVerification(user.getUsername()));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);

    }

    @Test
    public void rejectUserPictureVerification_Should_RejectPictures_When_AllIsValid(){
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getByUsername(user.getUsername()))
                .thenReturn(user);

        // Act
        adminService.rejectUserPictureVerification(user.getUsername());

        // Assert
        Assertions.assertFalse(user.getVerification().isPicturesVerified());
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);

    }

    @Test
    public void rejectUserPictureVerification_Should_Throw_When_UserNotFound(){
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getByUsername(user.getUsername()))
                .thenReturn(null);


        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> adminService.rejectUserPictureVerification(user.getUsername()));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);

    }


    @Test
    public void revokeAdminRights_Should_RevokeAdminRights_When_AllIsValid(){
        // Arrange
        User user = createMockAdmin();
        user.setVerification(new Verification());
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ROLE_ADMIN");

        Mockito.when(mockUserRepository.getByIdWithRoles(user.getId()))
                .thenReturn(user);

        Mockito.when(mockRoleRepository.getRoleByName("ROLE_ADMIN"))
                .thenReturn(role);

        // Act
        adminService.revokeAdminRights(user.getId());

        // Assert
        Assertions.assertFalse(user.getRoles().contains(role));
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);

    }

    @Test
    public void  revokeAdminRights_Should_Throw_When_UserNotFound(){
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getByIdWithRoles(user.getId()))
                .thenReturn(null);


        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> adminService.revokeAdminRights(user.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);

    }


    @Test
    public void  revokeAdminRights_Should_Throw_When_UserNotAdmin(){
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getByIdWithRoles(user.getId()))
                .thenReturn(user);


        // Act, Assert
        Assertions.assertThrows(AdminRoleManagementException.class, () -> adminService.revokeAdminRights(user.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);

    }


    @Test
    public void giveAdminRights_Should_GiveAdminRights_When_AllIsValid(){
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ROLE_ADMIN");

        Mockito.when(mockUserRepository.getByIdWithRoles(user.getId()))
                .thenReturn(user);

        Mockito.when(mockRoleRepository.getRoleByName("ROLE_ADMIN"))
                .thenReturn(role);

        // Act
        adminService.giveAdminRights(user.getId());

        // Assert
        Assertions.assertTrue(user.getRoles().contains(role));
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);

    }

    @Test
    public void  giveAdminRights_Should_Throw_When_UserNotFound(){
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getByIdWithRoles(user.getId()))
                .thenReturn(null);


        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> adminService.giveAdminRights(user.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);

    }


    @Test
    public void  giveAdminRights_Should_Throw_When_UserAlreadyAdmin(){
        // Arrange
        User user = createMockAdmin();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getByIdWithRoles(user.getId()))
                .thenReturn(user);


        // Act, Assert
        Assertions.assertThrows(AdminRoleManagementException.class, () -> adminService.giveAdminRights(user.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);

    }





}
