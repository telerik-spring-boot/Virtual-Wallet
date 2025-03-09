package com.telerik.virtualwallet.controllers.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AnonymousMvcController {


    @GetMapping("/login")
    public String getLogin(Authentication authentication) {
        if(authentication != null){
            return "redirect:/users/dashboard";
        }

        return "login";
    }

    @GetMapping("/register")
    public String getRegister(Authentication authentication){
        if(authentication != null){
            return "redirect:/users/dashboard";
        }
        return "register";
    }

    @GetMapping("/request-password")
    public String getRequestPassword(Authentication authentication){
        if(authentication != null){
            return "redirect:/users/dashboard";
        }
        return "request-password";}

    @GetMapping("/reset-password")
    public String getResetPassword() { return "reset-password";}
}
