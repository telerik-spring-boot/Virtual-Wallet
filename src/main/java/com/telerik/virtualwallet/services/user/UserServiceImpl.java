package com.telerik.virtualwallet.services.user;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.InsufficientFundsException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.models.*;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderDTO;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderMvcDTO;
import com.telerik.virtualwallet.models.enums.Currency;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import com.telerik.virtualwallet.services.picture.PictureService;
import com.telerik.virtualwallet.services.stock.StockService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StockService stockService;
    private final PictureService pictureService;
    private final WalletService walletService;


    public UserServiceImpl(UserRepository userRepository, StockService stockService, PictureService pictureService, WalletService walletService) {
        this.userRepository = userRepository;
        this.stockService = stockService;
        this.pictureService = pictureService;
        this.walletService = walletService;
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
        User user = userRepository.getByUsernameWithRoles(username);

        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
        }

        return user;
    }

    @Override
    public User getUserWithStocks(String username) {
        User user = userRepository.getUserWithStocks(username);

        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
        }

        return user;
    }

    @Override
    public void processStockOrderMvc(StockOrderMvcDTO stockOrder, String username) {

        User user = userRepository.getUserWithStocksAndWalletsAndInvestments(username);

        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
        }


        checkIfVerifiedAndNotBlocked(user);

        List<StockData> stocksToBuy = new ArrayList<>();
        List<StockData> stocksToSell = new ArrayList<>();
        List<Double> buyQuantities = new ArrayList<>();
        List<Double> sellQuantities = new ArrayList<>();

        for (int i = 0; i < stockOrder.getDirections().size(); i++) {
            if (stockOrder.getDirections().get(i) == -1) {
                stocksToSell.add(new StockData(stockOrder.getSymbols().get(i), stockOrder.getPrices().get(i)));
                sellQuantities.add(stockOrder.getQuantities().get(i));
            }

            if (stockOrder.getDirections().get(i) == 1) {
                stocksToBuy.add(new StockData(stockOrder.getSymbols().get(i), stockOrder.getPrices().get(i)));
                buyQuantities.add(stockOrder.getQuantities().get(i));
            }
        }

        if(!stocksToBuy.isEmpty()) {
            handleStockPurchase(user, user.getMainWallet(), stocksToBuy, buyQuantities);
        }

        if(!stocksToSell.isEmpty()) {
            handleStockSale(user, user.getMainWallet(), stocksToSell, sellQuantities);
        }

        userRepository.update(user);
    }

    @Override
    public void purchaseStocks(String username, int walletId, List<StockOrderDTO> orderList) {
        processStockTransaction(username, walletId, orderList, true);
    }

    @Override
    public void sellStocks(String username, int walletId, List<StockOrderDTO> orderList) {
        processStockTransaction(username, walletId, orderList, false);
    }

    @Override
    public void verifyEmail(String email) {

        User user = userRepository.getByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("User", "email", email);
        }

        user.getVerification().setEmailVerified(true);

        userRepository.update(user);
    }

    @Override
    public void create(User user) {
        List<User> dbUsers = userRepository.getByAnyUniqueField(user.getUsername(), user.getEmail(), user.getPhoneNumber());


        if (!dbUsers.isEmpty()) {
            appropriateThrow(user, dbUsers.get(0));
        }


        setDefaultVerification(user);
        setDefaultWallet(user);


        userRepository.create(user);

    }


    @Override
    public void update(User user) {
        List<User> dbUsers = userRepository.getByAnyUniqueField(user.getUsername(), user.getEmail(), user.getPhoneNumber());

        if (!dbUsers.isEmpty()) {
            for (User dbUser : dbUsers) {
                if (dbUser.getId() == user.getId()) {
                    if (!user.getUsername().equals(dbUser.getUsername()))
                        throw new UnauthorizedOperationException("Username modification is not allowed.");
                } else appropriateThrow(user, dbUser);
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

        List<Wallet> wallets = walletService.getWalletsByUsername(username);


        List<Integer> walletsToDelete = new ArrayList<>();
        for (Wallet wallet : wallets) {
            if (wallet.getUsers().size() == 1) {
                walletsToDelete.add(wallet.getId());
            }
        }


        pictureService.delete(username);

        userRepository.delete(userToDelete.getId());

        walletService.deleteWallets(walletsToDelete);

    }

    private static void appropriateThrow(User user, User dbUser) {

        if (dbUser.getEmail().equalsIgnoreCase(user.getEmail())) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        if (dbUser.getUsername().equalsIgnoreCase(user.getUsername())) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
        if (dbUser.getPhoneNumber().equalsIgnoreCase(user.getPhoneNumber())) {
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

    private void setDefaultWallet(User user) {
        Wallet wallet = new Wallet();

        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency(Currency.USD);
        wallet.setCreator(user);

        user.getWallets().add(wallet);
        user.setMainWallet(wallet);

    }

    private void processStockTransaction(String username, int walletId, List<StockOrderDTO> orderList, boolean isPurchase) {
        User user = userRepository.getUserWithStocksAndWalletsAndInvestments(username);

        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
        }


        checkIfVerifiedAndNotBlocked(user);


        Wallet walletToUse = walletRequirementsVerification(user, walletId);

        List<String> symbols = new ArrayList<>();
        List<Double> quantities = new ArrayList<>();


        for (StockOrderDTO order : orderList) {
            symbols.add(order.getSymbol());
            quantities.add(order.getQuantity());
        }

        // Uncomment for production
        // List<StockData> stocks = stockService.getStockPrices(symbols);

        // dummy data for testing
        List<StockData> stocks = new ArrayList<>();
        for (StockOrderDTO order : orderList) {
            stocks.add(new StockData(order.getSymbol(), 100));
        }

        if (isPurchase) {
            handleStockPurchase(user, walletToUse, stocks, quantities);
        } else {
            handleStockSale(user, walletToUse, stocks, quantities);
        }

        userRepository.update(user);
    }


    private void handleStockPurchase(User user, Wallet wallet, List<StockData> stocks, List<Double> quantities) {
        double totalStockValue = IntStream.range(0, stocks.size())
                .mapToDouble(i -> stocks.get(i).getPrice() * quantities.get(i))
                .sum();

        if (wallet.getBalance().doubleValue() < totalStockValue) {
            throw new InsufficientFundsException("Your balance is not sufficient to finish the transaction.");
        }

        wallet.setBalance(wallet.getBalance().subtract(BigDecimal.valueOf(totalStockValue)));

        for (int i = 0; i < stocks.size(); i++) {
            StockData stockData = stocks.get(i);
            double quantity = quantities.get(i);

            Stock existingStock = user.getStocks().stream()
                    .filter(s -> s.getStockSymbol().equals(stockData.getSymbol()))
                    .findFirst()
                    .orElse(null);

            if (existingStock != null) {
                double newTotalQuantity = existingStock.getQuantity() + quantity;
                double newPrice = (existingStock.getPrice() * existingStock.getQuantity() + stockData.getPrice() * quantity) / newTotalQuantity;

                existingStock.setQuantity(newTotalQuantity);
                existingStock.setPrice(newPrice);
            } else {
                user.addStock(new Stock(stockData.getSymbol(), quantity, stockData.getPrice(), LocalDateTime.now(), user));
            }
        }

        addInvestment(user, stocks, quantities, totalStockValue, "BUY");


    }

    private void handleStockSale(User user, Wallet wallet, List<StockData> stocks, List<Double> quantities) {
        double totalBalanceToReceive = 0;

        for (int i = 0; i < stocks.size(); i++) {
            StockData stockData = stocks.get(i);
            double quantity = quantities.get(i);

            Stock existingStock = user.getStocks().stream()
                    .filter(s -> s.getStockSymbol().equals(stockData.getSymbol()))
                    .findFirst()
                    .orElse(null);

            if (existingStock != null) {
                double newTotalQuantity = existingStock.getQuantity() - quantity;

                if (newTotalQuantity <= 0) {
                    totalBalanceToReceive += stockData.getPrice() * existingStock.getQuantity();
                    user.sellStock(existingStock);
                } else {
                    existingStock.setQuantity(newTotalQuantity);
                    totalBalanceToReceive += stockData.getPrice() * quantity;
                }
            }
        }


        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(totalBalanceToReceive)));

        addInvestment(user, stocks, quantities, totalBalanceToReceive, "SELL");
    }

    private void addInvestment(User user, List<StockData> stocks, List<Double> quantities, double totalBalanceToReceive, String type) {
        user.addInvestment(new Investment(user, LocalDateTime.now(),
                stocks.stream().map(StockData::getSymbol).collect(Collectors.joining(",")),
                quantities.stream().map(String::valueOf).collect(Collectors.joining(",")),
                stocks.stream().map(stock -> String.valueOf(stock.getPrice())).collect(Collectors.joining(",")),
                totalBalanceToReceive,
                type));
    }

    private Wallet walletRequirementsVerification(User user, int walletId) {

        Wallet walletToUse = user.getWallets().stream()
                .filter(wallet -> wallet.getId() == walletId)
                .findFirst()
                .orElse(null);

        if (walletToUse == null) {
            throw new EntityNotFoundException("Wallet", "walletId", walletId);
        }

        if (!walletToUse.getCurrency().toString().equalsIgnoreCase("USD")) {
            throw new UnauthorizedOperationException("You can only use USD account for stock operations.");
        }

        return walletToUse;
    }

    private void checkIfVerifiedAndNotBlocked(User user) {
        if (user.isBlocked()) {
            throw new UnauthorizedOperationException("You are unable to make transactions due to being blocked.");
        }

        if (!(user.getVerification().isEmailVerified() && user.getVerification().isPicturesVerified())) {
            throw new UnauthorizedOperationException("You are unable to make transactions due to being not verified.");
        }
    }


}
