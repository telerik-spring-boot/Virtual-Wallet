package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.InsufficientFundsException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.helpers.CardMapper;
import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.helpers.WalletMapper;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.Stock;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.api.StockResponse;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderMvcDTO;
import com.telerik.virtualwallet.models.dtos.transaction.InvestmentDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionsWrapper;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayMvcDTO;
import com.telerik.virtualwallet.models.dtos.user.UserReferDTO;
import com.telerik.virtualwallet.models.dtos.user.UserReferralDisplayDTO;
import com.telerik.virtualwallet.models.dtos.user.UserUpdateMvcDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletMvcDisplayDTO;
import com.telerik.virtualwallet.services.card.CardService;
import com.telerik.virtualwallet.services.jwt.JwtService;
import com.telerik.virtualwallet.services.stock.StockService;
import com.telerik.virtualwallet.services.transaction.TransactionService;
import com.telerik.virtualwallet.services.transaction.TransferService;
import com.telerik.virtualwallet.services.user.UserService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ui/users")
public class UserMvcController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final StockService stockService;
    private final TransferService transferService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final CardService cardService;
    private final CardMapper cardMapper;

    @Autowired
    public UserMvcController(UserService userService, UserMapper userMapper, JwtService jwtService, StockService stockService, TransferService transferService, TransactionService transactionService, TransactionMapper transactionMapper, WalletService walletService, WalletMapper walletMapper, CardService cardService, CardMapper cardMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.stockService = stockService;
        this.transferService = transferService;
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.walletService = walletService;
        this.walletMapper = walletMapper;
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin() {
        return populateIsAdminAttribute();
    }



    @GetMapping("/dashboard")
    public String getOverview(Model model, Authentication authentication) {

        WalletMvcDisplayDTO wallet = walletMapper.walletToMvcDto
                (walletService.getmainWalletByUsername(authentication.getName()));

        BigDecimal allDeposits = transferService.getBalanceChangeForTheCurrentMonthByWalletId(wallet.getId());

        loadDashboardMainWalletStats(model, allDeposits, wallet);

        loadDashboardCardInfo(model, authentication, wallet, allDeposits);

        loadDashboardTransactions(model, authentication);

        loadDashboardInvestments(model, authentication);

        loadDashboardIncomeExpenseChart(model, wallet);

        model.addAttribute("wallet", wallet);

        return "index";
    }

    @GetMapping("/referral-program")
    public String getReferralProgram(Model model, Authentication authentication){

        List<User> referrals = userService.getReferredUsers(authentication.getName());
        List<UserReferralDisplayDTO> users = referrals.stream().map(userMapper::userToUserReferralDTO).toList();

        long verifiedCount = users.stream()
                .filter(user -> user.getVerifiedAt() != null)
                .count();

        model.addAttribute("users", users);
        model.addAttribute("verifiedCount", verifiedCount);

        return "referral-program";
    }

    @GetMapping("/settings")
    public String showUserSettings(Authentication authentication, Model model, HttpServletRequest request) {

        try {
            User user = userService.getByUsername(authentication.getName());

            UserUpdateMvcDTO userUpdate = userMapper.userToUserUpdateMvcDto(user);
            UserDisplayMvcDTO userDisplay = userMapper.userToUserDisplayMvcDTO(user);


            model.addAttribute("userUpdate", userUpdate);
            model.addAttribute("userDisplay", userDisplay);
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("token", jwtService.generateEmailVerificationToken(user.getEmail()));

            return "settings";
        } catch (UnauthorizedOperationException e) {
            return "redirect:/ui/auth/login";
        }
    }

    @PutMapping("/settings")
    public String handleUpdateUser(Authentication authentication, @Valid @ModelAttribute("userUpdate") UserUpdateMvcDTO userUpdateMvcDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/ui/users/settings";
        }

        try {

            User userToUpdate = userMapper.dtoToUser(userUpdateMvcDTO, authentication.getName());

            userService.update(userToUpdate);

            redirectAttributes.addFlashAttribute("successUpdate", true);

            return "redirect:/ui/users/settings";

        } catch (DuplicateEntityException e) {
            bindingResult.reject("duplicateEntityError", e.getMessage());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/ui/users/settings";
        }
    }

    @DeleteMapping
    public String deleteUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication, RedirectAttributes redirectAttributes) {

        userService.delete(authentication.getName());

        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        redirectAttributes.addFlashAttribute("userDeleted", true);
        return "redirect:/ui/auth/login";

    }

    @GetMapping("/transactions")
    public String getTransactions(Authentication authentication, Model model) {

        List<TransactionsWrapper> transactions =
                new ArrayList<>(transactionService.getTransactionsByUsername(authentication.getName())
                        .stream().map(transactionMapper::transactionToTransactionWrapper).toList());


        transactions.addAll(transferService.getAllTransfersToYourWalletsByUsername(authentication.getName())
                .stream().map(transactionMapper::transferToTransactionWrapper).toList());

        model.addAttribute("transactions", transactions);
        model.addAttribute("activeTab", "all");

        return "transaction";
    }

    @GetMapping("/transactions/incoming")
    public String getIncomingTransactions(Authentication authentication, Model model) {

        List<TransactionsWrapper> transactions =
                new ArrayList<>(transactionService.getIncomingTransactionsByUsername(authentication.getName())
                        .stream().map(transactionMapper::transactionToTransactionWrapper).toList());


        transactions.addAll(transferService.getAllTransfersToYourWalletsByUsername(authentication.getName())
                .stream().map(transactionMapper::transferToTransactionWrapper).toList());

        model.addAttribute("transactions", transactions);
        model.addAttribute("activeTab", "in");

        return "transaction";
    }

    @GetMapping("/transactions/outgoing")
    public String getOutgoingTransactions(Authentication authentication, Model model) {

        List<TransactionsWrapper> transactions =
                new ArrayList<>(transactionService.getOutgoingTransactionsByUsername(authentication.getName())
                        .stream().map(transactionMapper::transactionToTransactionWrapper).toList());

        model.addAttribute("transactions", transactions);
        model.addAttribute("activeTab", "out");

        return "transaction";
    }

    @GetMapping("/investments")
    public String getInvestmentsHistory(Authentication authentication, Model model) {

        model.addAttribute("investments", userService.getInvestmentsByUsername(authentication.getName())
                .stream()
                .flatMap(investment -> transactionMapper.investmentToInvestmentDTO(investment).stream())
                .toList());

        model.addAttribute("activeTab", "investment");

        return "investments";
    }


    @GetMapping("/stocks")
    public String getStocksView(Model model, Authentication authentication) {


        Map<String, StockResponse> stocks = stockService.getStockPricesDetailed();


        StockOrderMvcDTO stockOrder = new StockOrderMvcDTO();

        for (Map.Entry<String, StockResponse> entry : stocks.entrySet()) {
            String symbol = entry.getKey();
            StockResponse stockResponse = entry.getValue();

            stockOrder.getSymbols().add(symbol);
            stockOrder.getPrices().add(stockResponse.getValues().get(0).getClose());
            stockOrder.getDirections().add(0);
            stockOrder.getQuantities().add(0.0);
        }


        model.addAttribute("stocks", stocks);
        model.addAttribute("stockOrder", stockOrder);


        model.addAttribute("userStocks", userService.getUserWithStocks(authentication.getName()).getStocks()
                .stream().collect(Collectors.toMap(Stock::getStockSymbol, Stock::getQuantity)));

        return "stocks";
    }

    @PostMapping("/stocks")
    public String handleStockOrder(@Valid @ModelAttribute("stockOrder") StockOrderMvcDTO stockOrder, RedirectAttributes redirectAttributes, Authentication authentication) {

        try {
            userService.processStockOrderMvc(stockOrder, authentication.getName());

            redirectAttributes.addFlashAttribute("orderSuccess", true);

        } catch (InsufficientFundsException | EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("orderFail", e.getMessage());
        }

        return "redirect:/ui/users/stocks";
    }


    public static boolean populateIsAdminAttribute() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> roles = new ArrayList<>();
        if (authentication != null && authentication.isAuthenticated()) {
            roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        }

        return roles.contains("ROLE_ADMIN");
    }

    private void loadDashboardMainWalletStats(Model model, BigDecimal allDeposits, WalletMvcDisplayDTO wallet) {
        BigDecimal balanceChange = allDeposits.add(transactionService
                .getBalanceChangeForTheCurrentMonthByWalletId(wallet.getId()));

        BigDecimal percentageBalanceChange = wallet.getBalance().subtract(balanceChange).compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(100).multiply(balanceChange
                        .divide(wallet.getBalance().subtract(balanceChange), 4, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

        boolean positiveBalanceChange = balanceChange.compareTo(BigDecimal.ZERO) > 0;

        model.addAttribute("percentageBalanceChange", percentageBalanceChange);
        model.addAttribute("positiveBalanceChange", positiveBalanceChange);
    }

    private void loadDashboardIncomeExpenseChart(Model model, WalletMvcDisplayDTO wallet) {
        List<BigDecimal> inTransfers = transactionService
                .getIncomingTransactionsForTheLastYearByWalletId(wallet.getId());

        List<BigDecimal> deposits = transferService
                .getTransfersForTheLastYearByWalletId(wallet.getId());

        List<BigDecimal> income = IntStream.range(0, deposits.size())
                .mapToObj(i -> inTransfers.get(i).add(deposits.get(i)))
                .collect(Collectors.toList());

        List<BigDecimal> expense = transactionService
                .getOutgoingTransactionsForTheLastYearByWalletId(wallet.getId());

        model.addAttribute("income", income);
        model.addAttribute("expense", expense);
        model.addAttribute("incomeSum", income.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        model.addAttribute("expenseSum", expense.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private void loadDashboardCardInfo(Model model, Authentication authentication, WalletMvcDisplayDTO wallet, BigDecimal allDeposits) {

        Card cardOr = cardService.getFirstCardCreatedByUsername(authentication.getName());

        CardDisplayDTO card = new CardDisplayDTO();
        card.setId(-1);

        if(cardOr != null){
            card = cardMapper.cardToCardDisplayDTO(cardOr);
        }


        BigDecimal depositsFromMainCard =  BigDecimal.ZERO;

        if(card.getId() != -1){
            depositsFromMainCard = transferService
                    .getBalanceChangeForTheCurrentMonthByWalletAndCardId(wallet.getId(), card.getId());
        }

        BigDecimal depositedFromMainPercentage = allDeposits.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(100)
                .multiply(depositsFromMainCard.divide(allDeposits, 2, RoundingMode.HALF_UP));


        model.addAttribute("depositsFromMainCard", depositsFromMainCard);
        model.addAttribute("depositedFromMainPercentage", depositedFromMainPercentage);
        model.addAttribute("card", card);
    }

    private void loadDashboardInvestments(Model model, Authentication authentication) {
        model.addAttribute("investments", userService.getInvestmentsByUsername(authentication.getName())
                .stream()
                .flatMap(investment -> transactionMapper.investmentToInvestmentDTO(investment).stream())
                .sorted(Comparator.comparing(InvestmentDTO::getPurchasedAt).reversed())
                .limit(4).toList());
    }

    private void loadDashboardTransactions(Model model, Authentication authentication) {
        List<TransactionsWrapper> transactions =
                new ArrayList<>(transactionService.getTransactionsByUsername(authentication.getName())
                        .stream().map(transactionMapper::transactionToTransactionWrapper).toList());


        transactions.addAll(transferService.getAllTransfersToYourWalletsByUsername(authentication.getName())
                .stream().map(transactionMapper::transferToTransactionWrapper).toList());

        List<TransactionsWrapper> sortedTransactions = transactions.stream()
                .sorted(Comparator.comparing(TransactionsWrapper::getTransactionTime).reversed())
                .limit(6).toList();

        model.addAttribute("transactions", sortedTransactions);
    }


}
