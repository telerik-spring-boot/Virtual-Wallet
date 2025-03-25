package com.telerik.virtualwallet.services;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.InsufficientFundsException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.models.Stock;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Verification;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderDTO;
import com.telerik.virtualwallet.models.enums.Currency;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.services.picture.PictureService;
import com.telerik.virtualwallet.services.stock.StockService;
import com.telerik.virtualwallet.services.user.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.telerik.virtualwallet.DummyObjectProvider.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {


    @Mock
    private  UserRepository mockUserRepository;

    @Mock
    private  StockService mockStockRepository;

    @Mock
    private  PictureService mockPictureRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void getById_Should_ReturnUser_When_IdIsValid() {
        // Arrange
        User user = createMockUser();


        Mockito.when(mockUserRepository.getById(Mockito.anyInt()))
                .thenReturn(user);

        // Act
        User returnedUser = userService.getById(1);

        // Assert
        Assertions.assertEquals(user, returnedUser);
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getById(Mockito.anyInt());
    }


    @Test
    public void getById_Should_ThrowError_When_UserIdInvalid() {
        // Arrange
        Mockito.when(mockUserRepository.getById(Mockito.anyInt()))
                .thenReturn(null);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getById(1));
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getById(Mockito.anyInt());
    }



    @Test
    public void getByEmail_Should_ReturnUser_When_EmailIsValid() {
        // Arrange
        User user = createMockUser();


        Mockito.when(mockUserRepository.getByEmail(Mockito.anyString()))
                .thenReturn(user);

        // Act
        User returnedUser = userService.getByEmail(user.getEmail());

        // Assert
        Assertions.assertEquals(user, returnedUser);
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getByEmail(Mockito.anyString());
    }


    @Test
    public void getByEmail_Should_ThrowError_When_UserEmailInvalid() {
        // Arrange
        Mockito.when(mockUserRepository.getByEmail(Mockito.anyString()))
                .thenReturn(null);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getByEmail("Something@asdasd.bg"));
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getByEmail(Mockito.anyString());
    }

    @Test
    public void getByPhone_Should_ReturnUser_When_PhoneIsValid() {
        // Arrange
        User user = createMockUser();


        Mockito.when(mockUserRepository.getByPhoneNumber(Mockito.anyString()))
                .thenReturn(user);

        // Act
        User returnedUser = userService.getByPhoneNumber(user.getPhoneNumber());

        // Assert
        Assertions.assertEquals(user, returnedUser);
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getByPhoneNumber(Mockito.anyString());
    }


    @Test
    public void getByPhone_Should_ThrowError_When_UserPhoneInvalid() {
        // Arrange
        Mockito.when(mockUserRepository.getByPhoneNumber(Mockito.anyString()))
                .thenReturn(null);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getByPhoneNumber("21321312321"));
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getByPhoneNumber(Mockito.anyString());
    }


    @Test
    public void getByUsername_Should_ReturnUser_When_UsernameIsValid() {
        // Arrange
        User user = createMockUser();


        Mockito.when(mockUserRepository.getByUsername(Mockito.anyString()))
                .thenReturn(user);

        // Act
        User returnedUser = userService.getByUsername(user.getUsername());

        // Assert
        Assertions.assertEquals(user, returnedUser);
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getByUsername(Mockito.anyString());
    }


    @Test
    public void getByUsername_Should_ThrowError_When_UserUsernameInvalid() {
        // Arrange
        Mockito.when(mockUserRepository.getByUsername(Mockito.anyString()))
                .thenReturn(null);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getByUsername("asdasd"));
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getByUsername(Mockito.anyString());
    }


    @Test
    public void getUserWithStocks_Should_ReturnUser_When_UsernameIsValid() {
        // Arrange
        User user = createMockUser();
        user.addStock(new Stock("AAPL", 5, 5, LocalDateTime.now(),user));


        Mockito.when(mockUserRepository.getUserWithStocks(Mockito.anyString()))
                .thenReturn(user);

        // Act
        User returnedUser = userService.getUserWithStocks(user.getUsername());

        // Assert
        Assertions.assertEquals(user, returnedUser);
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getUserWithStocks(Mockito.anyString());
    }


    @Test
    public void getUserWithStocks_Should_ThrowError_When_UserUsernameInvalid() {
        // Arrange
        Mockito.when(mockUserRepository.getUserWithStocks(Mockito.anyString()))
                .thenReturn(null);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getUserWithStocks("asdasd"));
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getUserWithStocks(Mockito.anyString());
    }


    @Test
    public void verifyEmail_Should_VerifyEmail_When_EmailIsValid() {
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getByEmail(Mockito.anyString()))
                .thenReturn(user);

        // Act
        userService.verifyEmail(user.getEmail());

        // Assert
        Assertions.assertTrue(user.getVerification().isEmailVerified());
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getByEmail(Mockito.anyString());
    }


    @Test
    public void verifyEmail_Should_ThrowError_When_UserEmailInvalid() {
        // Arrange
        Mockito.when(mockUserRepository.getByEmail(Mockito.anyString()))
                .thenReturn(null);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.verifyEmail("asdasd"));
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .getByEmail(Mockito.anyString());
    }


    @Test
    public void create_Should_CreateUser_When_EverythingIsValid() {
        // Arrange
        User user = createMockUser();

        Mockito.doNothing().when(mockUserRepository).create(user);

        // Act
        userService.create(user);

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .create(user);
    }

    @Test
    public void create_Should_Throw_When_EmailIsNotUnique() {
        // Arrange
        User user = createMockUser();

        User userToCreate = new User();
        userToCreate.setEmail(user.getEmail());
        userToCreate.setPhoneNumber("11");
        userToCreate.setUsername("22");

        Mockito.when(mockUserRepository.getByAnyUniqueField(userToCreate.getUsername(), userToCreate.getEmail(), userToCreate.getPhoneNumber()))
                .thenReturn(List.of(user));

        // Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> userService.create(userToCreate));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .create(user);
    }

    @Test
    public void create_Should_Throw_When_UsernameIsNotUnique() {
        // Arrange
        User user = createMockUser();

        User userToCreate = new User();
        userToCreate.setEmail("11");
        userToCreate.setPhoneNumber("11");
        userToCreate.setUsername(user.getUsername());

        Mockito.when(mockUserRepository.getByAnyUniqueField(userToCreate.getUsername(), userToCreate.getEmail(), userToCreate.getPhoneNumber()))
                .thenReturn(List.of(user));

        // Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> userService.create(userToCreate));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .create(user);
    }

    @Test
    public void create_Should_Throw_When_PhoneIsNotUnique() {
        // Arrange
        User user = createMockUser();

        User userToCreate = new User();
        userToCreate.setEmail("11");
        userToCreate.setPhoneNumber(user.getPhoneNumber());
        userToCreate.setUsername("33");

        Mockito.when(mockUserRepository.getByAnyUniqueField(userToCreate.getUsername(), userToCreate.getEmail(), userToCreate.getPhoneNumber()))
                .thenReturn(List.of(user));

        // Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> userService.create(userToCreate));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .create(user);
    }



    @Test
    public void update_Should_UpdateUser_When_EverythingIsValid() {
        // Arrange
        User user = createMockUser();

        Mockito.doNothing().when(mockUserRepository).update(user);

        // Act
        userService.update(user);

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);
    }

    @Test
    public void update_Should_Throw_When_FieldIsNotUniqueAndIdDiffers() {
        // Arrange
        User user = createMockUser();

        User userToUpdate = new User();
        userToUpdate.setId(2);
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhoneNumber("11");
        userToUpdate.setUsername("22");

        Mockito.when(mockUserRepository.getByAnyUniqueField(userToUpdate.getUsername(), userToUpdate.getEmail(), userToUpdate.getPhoneNumber()))
                .thenReturn(List.of(user));

        // Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> userService.update(userToUpdate));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);
    }


    @Test
    public void update_Should_Throw_When_TryingToModifyUsername() {
        // Arrange
        User user = createMockUser();

        User userToUpdate = new User();
        userToUpdate.setId(1);
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhoneNumber("11");
        userToUpdate.setUsername("asdasdasdasdasad");

        Mockito.when(mockUserRepository.getByAnyUniqueField(userToUpdate.getUsername(), userToUpdate.getEmail(), userToUpdate.getPhoneNumber()))
                .thenReturn(List.of(user));

        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> userService.update(userToUpdate));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);
    }



    @Test
    public void delete_Should_DeleteUser_When_EverythingIsValid() {
        // Arrange
        User user = createMockUser();

        Mockito.when(mockUserRepository.getByUsername(user.getUsername()))
                .thenReturn(user);

        // Act
        userService.delete(user.getUsername());

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .delete(user);
    }

    @Test
    public void delete_Should_Throw_When_UsernameNotFound() {
        // Arrange
        User user = createMockUser();

        Mockito.when(mockUserRepository.getByUsername(user.getUsername()))
                .thenReturn(null);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.delete(user.getUsername()));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .delete(user);
    }

    @Test
    public void purchaseStocks_Should_Throw_When_UserNotFound(){
        // Arrange
        User user = createMockUser();

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(null);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.purchaseStocks(user.getUsername(), 2, List.of(new StockOrderDTO())));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);
    }


    @Test
    public void purchaseStocks_Should_Throw_When_UserBlocked(){
        // Arrange
        User user = createMockUser();
        user.setBlocked(true);

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(user);

        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> userService.purchaseStocks(user.getUsername(), 2, List.of(new StockOrderDTO())));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);
    }


    @Test
    public void purchaseStocks_Should_Throw_When_UserNotVerified(){
        // Arrange
        User user = createMockUser();
        user.setVerification(new Verification());

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(user);

        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> userService.purchaseStocks(user.getUsername(), 2, List.of(new StockOrderDTO())));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);
    }


    @Test
    public void purchaseStocks_Should_Throw_When_WalletNotFound(){
        // Arrange
        User user = createMockUser();

        Wallet wallet = new Wallet();
        wallet.setId(1);
        wallet.setUsers(Set.of(user));
        wallet.setCurrency(Currency.USD);

        Verification verification = new Verification();
        verification.setEmailVerified(true);
        verification.setPicturesVerified(true);

        user.setVerification(verification);
        user.setWallets(Set.of(wallet));

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(user);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.purchaseStocks(user.getUsername(), 2, List.of(new StockOrderDTO())));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);
    }


    @Test
    public void purchaseStocks_Should_Throw_When_WalletNotInUSD(){
        // Arrange
        User user = createMockUser();

        Wallet wallet = new Wallet();
        wallet.setId(1);
        wallet.setUsers(Set.of(user));
        wallet.setCurrency(Currency.EUR);

        Verification verification = new Verification();
        verification.setEmailVerified(true);
        verification.setPicturesVerified(true);

        user.setVerification(verification);
        user.setWallets(Set.of(wallet));

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(user);

        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> userService.purchaseStocks(user.getUsername(), 1, List.of(new StockOrderDTO())));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);
    }


    @Test
    public void purchaseStocks_Should_Throw_When_InsufficientFunds(){
        // Arrange
        User user = createMockUser();

        Wallet wallet = new Wallet();
        wallet.setId(1);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUsers(Set.of(user));
        wallet.setCurrency(Currency.USD);

        Verification verification = new Verification();
        verification.setEmailVerified(true);
        verification.setPicturesVerified(true);

        user.setVerification(verification);
        user.setWallets(Set.of(wallet));

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(user);

        // Assert
        Assertions.assertThrows(InsufficientFundsException.class, () -> userService.purchaseStocks(user.getUsername(), 1, List.of(new StockOrderDTO("AAPL", 35.3))));
        Mockito.verify(mockUserRepository, Mockito.times(0))
                .update(user);
    }


    @Test
    public void purchaseStocks_Should_Work_When_EverythingIsValid(){
        // Arrange
        User user = createMockUser();

        Wallet wallet = new Wallet();
        wallet.setId(1);
        wallet.setBalance(BigDecimal.valueOf(10000));
        wallet.setUsers(Set.of(user));
        wallet.setCurrency(Currency.USD);

        Verification verification = new Verification();
        verification.setEmailVerified(true);
        verification.setPicturesVerified(true);

        user.setVerification(verification);
        user.setWallets(Set.of(wallet));

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(user);

        // Act
        userService.purchaseStocks(user.getUsername(), wallet.getId(), List.of(new StockOrderDTO("AAPL", 1.0)));

        // Assert
        Assertions.assertEquals(BigDecimal.valueOf(9900.0), wallet.getBalance());
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);
    }

    @Test
    public void purchaseStocks_Should_Work_When_UpdateSTockQuantity(){
        // Arrange
        User user = createMockUser();

        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("AAPL",1.0,150, LocalDateTime.now(), user));
        user.setStocks(stocks);
        Wallet wallet = new Wallet();
        wallet.setId(1);
        wallet.setBalance(BigDecimal.valueOf(10000));
        wallet.setUsers(Set.of(user));
        wallet.setCurrency(Currency.USD);

        Verification verification = new Verification();
        verification.setEmailVerified(true);
        verification.setPicturesVerified(true);

        user.setVerification(verification);
        user.setWallets(Set.of(wallet));

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(user);

        // Act
        userService.purchaseStocks(user.getUsername(), wallet.getId(), List.of(new StockOrderDTO("AAPL", 1.0)));

        // Assert
        Assertions.assertEquals(2.0, user.getStocks().get(0).getQuantity());
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);
    }


    @Test
    public void sellStocks_Should_WorkAndRemoveStock_When_EverythingIsValidAndFullSale(){
        // Arrange
        User user = createMockUser();

        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("AAPL",1.0,150, LocalDateTime.now(), user));
        user.setStocks(stocks);
        Wallet wallet = new Wallet();
        wallet.setId(1);
        wallet.setBalance(BigDecimal.valueOf(10000));
        wallet.setUsers(Set.of(user));
        wallet.setCurrency(Currency.USD);

        Verification verification = new Verification();
        verification.setEmailVerified(true);
        verification.setPicturesVerified(true);

        user.setVerification(verification);
        user.setWallets(Set.of(wallet));

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(user);

        // Act
        userService.sellStocks(user.getUsername(), wallet.getId(), List.of(new StockOrderDTO("AAPL", 1.0)));

        // Assert
        Assertions.assertEquals(BigDecimal.valueOf(10100.0), wallet.getBalance());
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);
    }



    @Test
    public void sellStocks_Should_Work_When_UpdateSTockQuantity(){
        // Arrange
        User user = createMockUser();

        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("AAPL",2.0,150, LocalDateTime.now(), user));
        user.setStocks(stocks);
        Wallet wallet = new Wallet();
        wallet.setId(1);
        wallet.setBalance(BigDecimal.valueOf(10000));
        wallet.setUsers(Set.of(user));
        wallet.setCurrency(Currency.USD);

        Verification verification = new Verification();
        verification.setEmailVerified(true);
        verification.setPicturesVerified(true);

        user.setVerification(verification);
        user.setWallets(Set.of(wallet));

        Mockito.when(mockUserRepository.getUserWithStocksAndWalletsAndInvestments(user.getUsername()))
                .thenReturn(user);

        // Act
        userService.sellStocks(user.getUsername(), wallet.getId(), List.of(new StockOrderDTO("AAPL", 1.0)));

        // Assert
        Assertions.assertEquals(1.0, user.getStocks().get(0).getQuantity());
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .update(user);
    }



}
