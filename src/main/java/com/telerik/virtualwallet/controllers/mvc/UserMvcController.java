package com.telerik.virtualwallet.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    @GetMapping("me")
    public String getOverview(){
        return "index";
    }
}
