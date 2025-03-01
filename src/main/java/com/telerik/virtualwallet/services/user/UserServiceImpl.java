package com.telerik.virtualwallet.services.user;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.InsufficientFundsException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.models.*;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.services.StockService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final StockService stockService;

    public UserServiceImpl(UserRepository userRepository, StockService stockService){
        this.userRepository = userRepository;
        this.stockService = stockService;
    }


    @Override
    public User getById(int id) {

        User user = userRepository.getById(id);

        if (user == null) {
            throw new EntityNotFoundException("User", "id", id);
        }

        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.getByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("User", "email", email);
        }

        return user;
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        User user = userRepository.getByPhoneNumber(phoneNumber);

        if (user == null) {
            throw new EntityNotFoundException("User", "phoneNumber", phoneNumber);
        }

        return user;
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.getByUsername(username);

        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
        }

        return user;
    }

    @Override
    public User getUserWithStocks(int id) {
        User user = userRepository.getUserWithStocks(id);

        if (user == null) {
            throw new EntityNotFoundException("User", "id", id);
        }

        return user;
    }

    @Override
    public void purchaseStocks(int userId, int walletId, List<String> symbols, List<Integer> quantities) {
        User user = userRepository.getUserWithStocksAndWallets(userId);

        if (user == null) {
            throw new EntityNotFoundException("User", "id", userId);
        }

        Wallet walletToUse = walletRequirementsVerification(user, walletId);

        List<StockData> stocks = stockService.getStockPrices(symbols);

        double totalStockValue = IntStream.range(0, stocks.size())
                .mapToDouble(i -> stocks.get(i).getPrice() * quantities.get(i))
                .sum();

        if(walletToUse.getBalance().doubleValue() < totalStockValue){
            throw new InsufficientFundsException("Your balance is not sufficient to finish the transaction.");
        }

        int indexOfQuantities = 0;

        walletToUse.setBalance(walletToUse.getBalance().subtract(BigDecimal.valueOf(totalStockValue)));

        for(StockData stock : stocks){
            String symbol = stock.getSymbol();
            double price = stock.getPrice();
            int quantity = quantities.get(indexOfQuantities++);

            Stock existingStock = user.getStocks().stream()
                    .filter(userStock -> userStock.getStockSymbol().equals(symbol))
                    .findFirst()
                    .orElse(null);

            if (existingStock != null) {

                int newTotalQuantity = quantity + existingStock.getQuantity();
                double newPrice = (existingStock.getPrice() * existingStock.getQuantity() + price * quantity) / newTotalQuantity;

                existingStock.setQuantity(newTotalQuantity);
                existingStock.setPrice(newPrice);

            } else {
                Stock newStock = new Stock();

                newStock.setStockSymbol(symbol);
                newStock.setPrice(price);
                newStock.setQuantity(quantity);
                newStock.setPurchasedAt(LocalDateTime.now());
                newStock.setUser(user);

                user.addStock(newStock);
            }
        }

        userRepository.update(user);

    }

    @Override
    public void sellStocks(int userId, int walletId, List<String> symbols, List<Integer> quantities) {

        User user = userRepository.getUserWithStocksAndWallets(userId);

        if (user == null) {
            throw new EntityNotFoundException("User", "id", userId);
        }

        Wallet walletToUse = walletRequirementsVerification(user, walletId);

        List<StockData> stocks = stockService.getStockPrices(symbols);

        int indexOfQuantities = 0;
        double totalBalanceToReceive = 0;

        for(StockData stock : stocks){
            String symbol = stock.getSymbol();
            double price = stock.getPrice();
            int quantity = quantities.get(indexOfQuantities++);

            Stock existingStock = user.getStocks().stream()
                    .filter(userStock -> userStock.getStockSymbol().equals(symbol))
                    .findFirst()
                    .orElse(null);

            if (existingStock != null) {

                int newTotalQuantity = existingStock.getQuantity() - quantity;

                if(newTotalQuantity <= 0){

                    totalBalanceToReceive += price * existingStock.getQuantity();
                    user.sellStock(existingStock);

                }else{

                    existingStock.setQuantity(newTotalQuantity);
                    totalBalanceToReceive += price * (quantity - newTotalQuantity);

                }
            }
        }
        walletToUse.setBalance(walletToUse.getBalance().add(BigDecimal.valueOf(totalBalanceToReceive)));

        userRepository.update(user);
    }

    @Override
    public void verifyEmail(String email) {

        User user = userRepository.getByEmail(email);

        if(user == null){
            throw new EntityNotFoundException("User", "email", email);
        }

        user.getVerification().setEmailVerified(true);

        userRepository.update(user);
    }

    @Override
    public void create(User user) {
        List<User> dbUsers = userRepository.getByAnyUniqueField(user.getUsername(), user.getEmail(), user.getPhoneNumber());

        setDefaultVerification(user);

        if(!dbUsers.isEmpty()){
            appropriateThrow(user, dbUsers.get(0));
        }

        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        List<User> dbUsers = userRepository.getByAnyUniqueField(user.getUsername(), user.getEmail(), user.getPhoneNumber());

        if(!dbUsers.isEmpty()){
            for(User dbUser : dbUsers){
                if(dbUser.getId() == user.getId()){
                    if(!user.getUsername().equals(dbUser.getUsername()))
                        throw new UnauthorizedOperationException("Username modification is not allowed.");
                }else appropriateThrow(user, dbUser);
            }
        }


        userRepository.update(user);

    }

    @Override
    public void delete(String username) {

        User userToDelete = userRepository.getByUsername(username);

        if (userToDelete == null) {
            throw new EntityNotFoundException("User", "username", username);
        }

        userRepository.delete(userToDelete.getId());

    }

    private static void appropriateThrow(User user, User dbUser) {

        if(dbUser.getEmail().equalsIgnoreCase(user.getEmail())){
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        if(dbUser.getUsername().equalsIgnoreCase(user.getUsername())){
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
        if(dbUser.getPhoneNumber().equalsIgnoreCase(user.getPhoneNumber())){
            throw new DuplicateEntityException("User", "Phone Number", user.getPhoneNumber());
        }

    }

    private void setDefaultVerification(User user) {
        Verification verification = new Verification();

        verification.setUser(user);
        verification.setPicturesVerified(false);
        verification.setEmailVerified(false);

        user.setVerification(verification);
    }

    private Wallet walletRequirementsVerification(User user, int walletId){

        Wallet walletToUse = user.getWallets().stream()
                .filter(wallet -> wallet.getId() == walletId)
                .findFirst()
                .orElse(null);

        if(walletToUse == null){
            throw new EntityNotFoundException("Wallet", "walletId", walletId);
        }

        if(!walletToUse.getCurrency().getDescription().equalsIgnoreCase("USD")){
            throw new UnauthorizedOperationException("You can only use USD account for stock operations.");
        }

        return walletToUse;
    }
}
