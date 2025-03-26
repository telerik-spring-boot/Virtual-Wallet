package com.telerik.virtualwallet.controllers.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/home")
public class HomeMvcController {

    @GetMapping()
    public String getHome(Authentication authentication, Model model) {

        if(authentication != null){
            model.addAttribute("isLoggedIn", true);
        }

        return "index";
    }




}
