package com.telerik.virtualwallet.controllers.mvc;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.ExpiredCardException;
import com.telerik.virtualwallet.exceptions.InsufficientFundsException;
import com.telerik.virtualwallet.helpers.CardMapper;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.card.CardCreateDTO;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.models.dtos.wallet.CardTransferCreateDTO;
import com.telerik.virtualwallet.services.card.CardService;
import com.telerik.virtualwallet.services.user.UserService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.telerik.virtualwallet.controllers.mvc.UserMvcController.populateIsAdminAttribute;

@Controller
@RequestMapping("/ui/cards")
public class CardMvcController {

    private final CardService cardService;
    private final CardMapper cardMapper;
    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public CardMvcController(CardService cardService, CardMapper cardMapper, UserService userService, WalletService walletService) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.userService = userService;
        this.walletService = walletService;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin() {
        return populateIsAdminAttribute();

    }

    @GetMapping()
    public String getCards(Authentication authentication, Model model, HttpServletRequest request) {

        User user = userService.getByUsername(authentication.getName());

        loadUserCardList(model, authentication);

        model.addAttribute("user", user);
        model.addAttribute("requestURI", request.getRequestURI());

        return "card";
    }

    @GetMapping("/new")
    public String showNewCardForm(Model model, HttpServletRequest request) {

        model.addAttribute("card", new CardCreateDTO());
        model.addAttribute("requestURI", request.getRequestURI());
        return "card-add";
    }

    @PostMapping("/new")
    public String handleNewCardForm(Model model, @Valid @ModelAttribute("card") CardCreateDTO cardCreateDTO,
                                    BindingResult bindingResult, Authentication authentication,
                                    RedirectAttributes redirectAttributes) {

        model.addAttribute("formSubmitted", true);


        if (bindingResult.hasErrors()) {
            return "card-add";
        }

        try {
            Card card = cardMapper.createDtoToCard(cardCreateDTO);

            cardService.addCard(authentication.getName(), card);

            redirectAttributes.addFlashAttribute("successAdd", true);

            return "redirect:/ui/cards";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("cardNumber", "card.number", e.getMessage());

            return "card-add";
        } catch (ExpiredCardException e) {
            bindingResult.rejectValue("expiryMonth", "card.month", e.getMessage());
            bindingResult.rejectValue("expiryYear", "card.year", e.getMessage());

            return "card-add";
        }
    }

    @PreAuthorize("@cardSecurityService.isUserCardHolder(#cardId, authentication.name)")
    @GetMapping("/{cardId}/delete")
    public String deleteCardById(@PathVariable int cardId, RedirectAttributes redirectAttributes) {

        try {
            cardService.deleteCard(cardId);

            redirectAttributes.addFlashAttribute("successDelete", true);

            return "redirect:/ui/cards";
        } catch (EntityNotFoundException e) {
            return "404";
        }
    }

    @PreAuthorize("@cardSecurityService.isUserCardHolder(#cardId, authentication.name)")
    @GetMapping("/{cardId}/update")
    public String updateCardByForm(@PathVariable int cardId, Model model, HttpServletRequest request) {

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

    @PostMapping("/{cardId}/update")
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

            return "redirect:/ui/cards";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("cardNumber", "card.number", e.getMessage());

            return "card-update";
        } catch (ExpiredCardException e) {
            bindingResult.rejectValue("expiryMonth", "card.month", e.getMessage());
            bindingResult.rejectValue("expiryYear", "card.year", e.getMessage());

            return "card-update";
        }


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
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        model.addAttribute("formSubmitted", true);

        User user = userService.getByUsername(authentication.getName());

        if (bindingResult.hasErrors()) {
            return "deposit";
        }

        try {
            walletService.addFundsToWallet(user.getMainWallet().getId(), cardId, cardTransferCreateDTO.getAmount());

            redirectAttributes.addFlashAttribute("successfulDeposit", true);

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
}
