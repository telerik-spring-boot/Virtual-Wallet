package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.exceptions.*;
import com.telerik.virtualwallet.helpers.CardMapper;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.helpers.WalletMapper;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.Stock;
import com.telerik.virtualwallet.models.StockResponse;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.card.CardCreateDTO;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.models.dtos.stock.StockOrderMvcDTO;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayMvcDTO;
import com.telerik.virtualwallet.models.dtos.user.UserUpdateMvcDTO;
import com.telerik.virtualwallet.models.dtos.wallet.CardTransferCreateDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletMvcDisplayDTO;
import com.telerik.virtualwallet.services.card.CardService;
import com.telerik.virtualwallet.services.jwt.JwtService;
import com.telerik.virtualwallet.services.security.CardSecurityService;
import com.telerik.virtualwallet.services.security.WalletSecurityService;
import com.telerik.virtualwallet.services.stock.StockService;
import com.telerik.virtualwallet.services.user.UserService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final CardService cardService;
    private final CardMapper cardMapper;
    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final StockService stockService;
    private final CardSecurityService cardSecurityService;
    private final WalletSecurityService walletSecurityService;

    @Autowired
    public UserMvcController(UserService userService, UserMapper userMapper, JwtService jwtService, CardService cardService, CardMapper cardMapper, WalletService walletService, WalletMapper walletMapper, StockService stockService, CardSecurityService cardSecurityService, WalletSecurityService walletSecurityService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.walletService = walletService;
        this.walletMapper = walletMapper;
        this.stockService = stockService;
        this.cardSecurityService = cardSecurityService;
        this.walletSecurityService = walletSecurityService;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> roles = new ArrayList<>();
        if (authentication != null && authentication.isAuthenticated()) {
            roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        }

        return roles.contains("ROLE_ADMIN");

    }

    @GetMapping("/dashboard")
    public String getOverview() {
        return "index";
    }

    @GetMapping("/wallets")
    public String getBalance(Authentication authentication, Model model) {

        List<WalletMvcDisplayDTO> wallets = walletService.getWalletsByUsername(authentication.getName()).stream()
                .map(walletMapper::walletToMvcDto)
                .toList();

        model.addAttribute("wallets", wallets);
        model.addAttribute("loggedUsername", authentication.getName());
        return "wallets";
    }

    @GetMapping("/wallets/{walletId}/add/{username}")
    public String addUserToWallet(@PathVariable int walletId, @PathVariable String username,
                                  Authentication authentication, RedirectAttributes redirectAttributes) {

        if (!walletSecurityService.isUserWalletHolder(walletId, authentication.getName())) {
            return "404";
        }

        try {
            walletService.addUserToWallet(walletId, username);

            redirectAttributes.addFlashAttribute("addingSuccess", true);
            return "redirect:/ui/users/wallets";
        } catch (DuplicateEntityException | EntityNotFoundException | UnauthorizedOperationException e) {
            redirectAttributes.addFlashAttribute("addingErrors", e.getMessage());
            return "redirect:/ui/users/wallets";
        }
    }

    @GetMapping("/wallets/{walletId}/remove/{username}")
    public String removeUserToWallet(@PathVariable int walletId, @PathVariable String username,
                                  Authentication authentication, RedirectAttributes redirectAttributes) {

        if (!walletSecurityService.isUserWalletHolder(walletId, authentication.getName())) {
            return "404";
        }

        try {
            walletService.removeUserFromWallet(walletId, username);

            redirectAttributes.addFlashAttribute("removalSuccess", true);
            return "redirect:/ui/users/wallets";
        } catch (DuplicateEntityException | EntityNotFoundException |
                 UnauthorizedOperationException | InconsistentOperationException e) {
            redirectAttributes.addFlashAttribute("removalErrors", e.getMessage());
            return "redirect:/ui/users/wallets";
        }
    }

    @GetMapping("/cards")
    public String getCards(Authentication authentication, Model model, HttpServletRequest request) {

        User user = userService.getByUsername(authentication.getName());

        loadUserCardList(model, authentication);

        model.addAttribute("user", user);
        model.addAttribute("requestURI", request.getRequestURI());

        return "card";
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
    public String getTransactions() {
        return "transaction";
    }

    @GetMapping("/deposit")
    public String depositFromCard(Authentication authentication, Model model, HttpServletRequest request) {

        User user = userService.getByUsername(authentication.getName());

        loadUserCardList(model, authentication);

        model.addAttribute("user", user);
        model.addAttribute("cardTransfer", new CardTransferCreateDTO());
        model.addAttribute("requestURI", request.getRequestURI());

        return "deposit";

    }

    @PostMapping("/deposit")
    public String handleDeposit(Model model, @RequestParam("chosenCardId") int cardId, Authentication authentication,
                                @Valid @ModelAttribute("cardTransfer") CardTransferCreateDTO cardTransferCreateDTO,
                                BindingResult bindingResult) {

        model.addAttribute("formSubmitted", true);

        User user = userService.getByUsername(authentication.getName());

        if (bindingResult.hasErrors()) {
            return "deposit";
        }

        try {
            walletService.addFundsToWallet(user.getMainWallet().getId(), cardId, cardTransferCreateDTO.getAmount());

            return "redirect:/ui/users/dashboard";

        } catch (EntityNotFoundException | InsufficientFundsException e) {
            bindingResult.rejectValue("amount", "card.number", e.getMessage());

            loadUserCardList(model, authentication);

            model.addAttribute("user", user);

            return "deposit";
        }

    }

    private void loadUserCardList(Model model, Authentication authentication) {
        List<CardDisplayDTO> userCards = cardService.getCardsByUsername(authentication.getName()).stream()
                .map(cardMapper::cardToCardDisplayDTO)
                .toList();

        model.addAttribute("cards", userCards);

    }


    @GetMapping("/transfer")
    public String createTransfer() {
        return "transfer-make";
    }

    @GetMapping("/cards/new")
    public String showNewCardForm(Model model, HttpServletRequest request) {

        model.addAttribute("card", new CardCreateDTO());
        model.addAttribute("requestURI", request.getRequestURI());
        return "card-add";
    }

    @PostMapping("/cards/new")
    public String handleNewCardForm(Model model, @Valid @ModelAttribute("card") CardCreateDTO cardCreateDTO,
                                    BindingResult bindingResult, Authentication authentication) {

        model.addAttribute("formSubmitted", true);


        if (bindingResult.hasErrors()) {
            return "card-add";
        }

        try {
            Card card = cardMapper.createDtoToCard(cardCreateDTO);

            cardService.addCard(authentication.getName(), card);

            return "redirect:/ui/users/cards";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("cardNumber", "card.number", e.getMessage());

            return "card-add";
        } catch (ExpiredCardException e) {
            bindingResult.rejectValue("expiryMonth", "card.month", e.getMessage());
            bindingResult.rejectValue("expiryYear", "card.year", e.getMessage());

            return "card-add";
        }
    }

    @GetMapping("/cards/{cardId}/delete")
    public String deleteCardById(@PathVariable int cardId, Authentication authentication, RedirectAttributes redirectAttributes) {

        if (!cardSecurityService.isUserCardHolder(cardId, authentication.getName())) {
            return "404";
        }

        try {
            cardService.deleteCard(cardId);

            redirectAttributes.addFlashAttribute("successDelete", true);

            return "redirect:/ui/users/cards";
        } catch (EntityNotFoundException e) {
            return "404";
        }
    }

    @GetMapping("/cards/{cardId}/update")
    public String updateCardByForm(@PathVariable int cardId, Authentication authentication, Model model, HttpServletRequest request) {

        if (!cardSecurityService.isUserCardHolder(cardId, authentication.getName())) {
            return "404";
        }

        try {

            Card card = cardService.getCardById(cardId);

            model.addAttribute("card", new CardCreateDTO(card.getNumber(), card.getHolder(),
                    card.getCvv(), card.getExpiryMonth(), card.getExpiryYear()));
            model.addAttribute("requestURI", request.getRequestURI());

            return "card-update";
        } catch (EntityNotFoundException e) {
            return "404";
        }

    }

    @PostMapping("/cards/{cardId}/update")
    public String handleUpdateCardByForm(@PathVariable int cardId, @Valid @ModelAttribute("card") CardCreateDTO cardCreateDTO,
                                         BindingResult bindingResult, Model model,
                                         RedirectAttributes redirectAttributes) {

        model.addAttribute("formSubmitted", true);


        if (bindingResult.hasErrors()) {
            return "card-update";
        }


        try {
            Card card = cardMapper.createDtoToCard(cardId, cardCreateDTO);

            cardService.updateCard(card);

            redirectAttributes.addFlashAttribute("successUpdate", true);

            return "redirect:/ui/users/cards";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("cardNumber", "card.number", e.getMessage());

            return "card-update";
        } catch (ExpiredCardException e) {
            bindingResult.rejectValue("expiryMonth", "card.month", e.getMessage());
            bindingResult.rejectValue("expiryYear", "card.year", e.getMessage());

            return "card-update";
        }


    }


    @GetMapping("/stocks")
    public String getStocksView(Model model, Authentication authentication) {


        Map<String, StockResponse> stocks = stockService.getStockPricesDetailed(); // Map<String, StockData>


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

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminPanel() {

        return "admin-panel";
    }


}
