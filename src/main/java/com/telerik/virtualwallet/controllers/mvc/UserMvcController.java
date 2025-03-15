package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.exceptions.*;
import com.telerik.virtualwallet.helpers.CardMapper;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.StockData;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.card.CardCreateDTO;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.models.dtos.stock.StockResponse;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayMvcDTO;
import com.telerik.virtualwallet.models.dtos.user.UserUpdateMvcDTO;
import com.telerik.virtualwallet.models.dtos.wallet.CardTransferCreateDTO;
import com.telerik.virtualwallet.services.card.CardService;
import com.telerik.virtualwallet.services.jwt.JwtService;
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

@Controller
@RequestMapping("/ui/users")
public class UserMvcController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final CardService cardService;
    private final CardMapper cardMapper;
    private final WalletService walletService;
    private final StockService stockService;

    @Autowired
    public UserMvcController(UserService userService, UserMapper userMapper, JwtService jwtService, CardService cardService, CardMapper cardMapper, WalletService walletService, StockService stockService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.walletService = walletService;
        this.stockService = stockService;
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

    @GetMapping("/balance")
    public String getBalance() {
        return "balance";
    }

    @GetMapping("/cards")
    public String getCards(Authentication authentication, Model model, HttpServletRequest request) {

        User user = userService.getByUsername(authentication.getName());

        loadUserCardList(model,authentication);
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
            return "redirect:/auth/login";
        }
    }

    @PutMapping("/settings")
    public String handleUpdateUser(Authentication authentication, @Valid @ModelAttribute("userUpdate") UserUpdateMvcDTO userUpdateMvcDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/settings";
        }

        try {

            User userToUpdate = userMapper.dtoToUser(userUpdateMvcDTO, authentication.getName());

            userService.update(userToUpdate);

            redirectAttributes.addFlashAttribute("successUpdate", true);

            return "redirect:/users/settings";

        } catch (DuplicateEntityException e) {
            bindingResult.reject("duplicateEntityError", e.getMessage());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/settings";
        }
    }

    @DeleteMapping
    public String deleteUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication, RedirectAttributes redirectAttributes) {

        userService.delete(authentication.getName());

        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        redirectAttributes.addFlashAttribute("userDeleted", true);
        return "redirect:/auth/login";

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

            return "redirect:/users/dashboard";

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

            return "redirect:/users/cards";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("cardNumber", "card.number", e.getMessage());

            return "card-add";
        } catch (ExpiredCardException e) {
            bindingResult.rejectValue("expiryMonth", "card.month", e.getMessage());
            bindingResult.rejectValue("expiryYear", "card.year", e.getMessage());

            return "card-add";
        }
    }


    @GetMapping("/stocks")
    public String getStocks() {
        return "stocks";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminPanel() {

        return "admin-panel";
    }

}
