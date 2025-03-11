package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.helpers.CardMapper;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.dtos.card.CardCreateDTO;
import com.telerik.virtualwallet.services.card.CardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final CardService cardService;
    private final CardMapper cardMapper;

    @Autowired
    public UserMvcController(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> roles = new ArrayList<>();
        if (authentication != null && authentication.isAuthenticated()) {
            roles =  authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        }

        return roles.contains("ROLE_ADMIN");

    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/dashboard")
    public String getOverview(){
        return "index";
    }

    @GetMapping("/balance")
    public String getCards(){
        return "balance";
    }

    @GetMapping("/cards")
    public String getBalance(){
        return "card";
    }

    @GetMapping("/recipients")
    public String getRecipients(){
        return "recipients";
    }

    @GetMapping("/settings")
    public String getSettings(){
        return "settings";
    }

    @GetMapping("/transactions")
    public String getTransactions(){
        return "transaction";
    }

    @GetMapping("/card-add")
    public String addCard(){
        return "card-add";
    }

    @GetMapping("/deposit")
    public String depositFromCard(){
        return "deposit";
    }

    @GetMapping("/transfer")
    public String createTransfer(){
        return "transfer-make";
    }

    @GetMapping("{username}/cards/new")
    public String showNewCardForm(@PathVariable String username, Model model,
                                  HttpSession session, Authentication authentication){
        if(!authentication.getName().equals(username)){
            return "redirect:/auth/login";
        }

        model.addAttribute("card", new CardCreateDTO());
        return "card-add";
    }

    @PostMapping("{username}/cards/new")
    public String handleNewCardForm(@PathVariable String username,@Valid @ModelAttribute("card") CardCreateDTO cardCreateDTO,
                                    BindingResult bindingResult, HttpSession session, Authentication authentication){

        if(!authentication.getName().equals(username)){
            return "redirect:/auth/login";
        }

        if(bindingResult.hasErrors()){
            return "card-add";
        }

        try{
            Card card = cardMapper.createDtoToCard(cardCreateDTO);

            cardService.addCard(username, card);

            return "redirect:/users/cards";
        }catch (DuplicateEntityException e){
            bindingResult.rejectValue("cardNumber","");

            return "card-add";
        }
    }



    @GetMapping("/stocks")
    public String getStocks(){
        return "stocks";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminPanel(){

        return "admin-panel";
    }
}
