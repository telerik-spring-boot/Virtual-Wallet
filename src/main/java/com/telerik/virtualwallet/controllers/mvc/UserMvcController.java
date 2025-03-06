package com.telerik.virtualwallet.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserMvcController {

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
}
