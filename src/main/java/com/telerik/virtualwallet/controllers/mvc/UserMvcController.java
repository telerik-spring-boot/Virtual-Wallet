package com.telerik.virtualwallet.controllers.mvc;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {

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

    @GetMapping("/profile")
    public String getProfile(){
        return "profile";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(){
        return "register";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminPanel(){

        return "admin-panel";
    }
}
