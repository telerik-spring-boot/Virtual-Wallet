package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.InsufficientFundsException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.Stock;
import com.telerik.virtualwallet.models.StockResponse;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderMvcDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionsWrapper;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayMvcDTO;
import com.telerik.virtualwallet.models.dtos.user.UserUpdateMvcDTO;
import com.telerik.virtualwallet.services.jwt.JwtService;
import com.telerik.virtualwallet.services.stock.StockService;
import com.telerik.virtualwallet.services.transaction.TransactionService;
import com.telerik.virtualwallet.services.transaction.TransferService;
import com.telerik.virtualwallet.services.user.UserService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    public UserMvcController(UserService userService, UserMapper userMapper, JwtService jwtService, StockService stockService, TransferService transferService, TransactionService transactionService, TransactionMapper transactionMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.stockService = stockService;
        this.transferService = transferService;
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin() {
        return populateIsAdminAttribute();

    }

    @GetMapping("/dashboard")
    public String getOverview() {
        return "index";
    }


    @GetMapping("/recipients")
    public String getRecipients() {
        return "recipients";
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

        return "transaction";
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




}
