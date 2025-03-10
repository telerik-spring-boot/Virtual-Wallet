package com.telerik.virtualwallet.controllers.mvc;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.RegisterDTO;
import com.telerik.virtualwallet.services.email.EmailService;
import com.telerik.virtualwallet.services.jwt.JwtService;
import com.telerik.virtualwallet.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AnonymousMvcController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Autowired
    public AnonymousMvcController(UserService userService, UserMapper userMapper, EmailService emailService, JwtService jwtService){
        this.userService = userService;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }


    @GetMapping("/login")
    public String getLogin(Authentication authentication) {
        if(authentication != null){
            return "redirect:/users/dashboard";
        }

        return "login";
    }

    @GetMapping("/register")
    public String getRegister(Authentication authentication, Model model){
        if(authentication != null){
            return "redirect:/users/dashboard";
        }
        model.addAttribute("register", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDTO registerDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,  HttpServletRequest request){
        model.addAttribute("formSubmitted", true);

        if(bindingResult.hasErrors()){
            return "register";
        }

        try{
            User user = userMapper.dtoToUser(registerDTO);

            userService.create(user);


            // Do not uncomment before production!

//                    String token = jwtService.generateEmailVerificationToken(user.getEmail());
//        String verificationUrl = request.getScheme() + "://" + request.getServerName() + "/auth/verify-email?token=" + token;
//
//
//        String emailContent = "<p>Hello " + user.getUsername() + ",</p>"
//                + "<p>Please click the link below to verify your email:</p>"
//                + "<a href='" + verificationUrl + "'>Verify Email</a>"
//                + "<p>If you did not sign up, you can ignore this email.</p>";
//
//        emailService.send(user.getEmail(), "Verify Your Email", emailContent);

            redirectAttributes.addFlashAttribute("registerSuccess", true);

            return "redirect:/auth/login";

        }  catch (DuplicateEntityException e) {
            if(e.getMessage().contains("username")){
                bindingResult.rejectValue("username", "username.register", e.getMessage());
            }else if(e.getMessage().contains("email")){
                bindingResult.rejectValue("emailAddress", "email.register", e.getMessage());
            }else
                bindingResult.rejectValue("phoneNumber", "phone.register", e.getMessage());

            return "register";
        }
    }

    @GetMapping("/request-password")
    public String getRequestPassword(Authentication authentication){
        if(authentication != null){
            return "redirect:/users/dashboard";
        }
        return "request-password";}

    @GetMapping("/reset-password")
    public String getResetPassword() { return "reset-password";}


    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, RedirectAttributes redirectAttributes) {


        try {
            if (jwtService.isTokenExpired(token)) {
                redirectAttributes.addFlashAttribute("emailFail", true);
                return "redirect:/auth/login";
            }

            String email = jwtService.extractSubject(token);

            userService.verifyEmail(email);

            redirectAttributes.addFlashAttribute("emailSuccess", true);
            return "redirect:/auth/login";
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("emailFail", true);
            return "redirect:/auth/login";
        }

    }
}
