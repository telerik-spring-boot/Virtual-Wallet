package com.telerik.virtualwallet.controllers.mvc;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.ContactDTO;
import com.telerik.virtualwallet.models.dtos.RegisterDTO;
import com.telerik.virtualwallet.models.dtos.user.UserPasswordUpdateDTO;
import com.telerik.virtualwallet.models.dtos.user.UserReferDTO;
import com.telerik.virtualwallet.models.dtos.user.UserRetrieveDTO;
import com.telerik.virtualwallet.services.email.EmailService;
import com.telerik.virtualwallet.services.jwt.JwtService;
import com.telerik.virtualwallet.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ui/auth")
public class AnonymousMvcController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Autowired
    public AnonymousMvcController(UserService userService, UserMapper userMapper, EmailService emailService, JwtService jwtService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }


    @GetMapping("/login")
    public String getLogin(Authentication authentication) {
        if (authentication != null) {
            return "redirect:/ui/users/dashboard";
        }

        return "login";
    }

    @GetMapping("/faq")
    public String getFAQ() {
        return "faq";
    }

    @GetMapping("/terms")
    public String getTermsAndConditions() {
        return "terms";
    }

    @GetMapping("/extraChecks")
    public String getLogin(HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(userDetails.getUsername());

        if (!user.getVerification().isEmailVerified()) {
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            return "redirect:/ui/auth/login?error=email";
        }

        if (user.isBlocked()) {
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            return "redirect:/ui/auth/login?error=block";
        }

        user.setLastOnline(LocalDateTime.now());
        userService.update(user);

        return "redirect:/ui/users/dashboard";
    }

    @GetMapping("/register")
    public String getRegister(Authentication authentication, Model model, @RequestParam(required = false) String token, HttpServletRequest request) {

        if (authentication != null) {
            return "redirect:/ui/users/dashboard";
        }


        String username = "";
        if (token != null) {
            if (jwtService.isTokenExpired(token)) {
                return "redirect:/ui/auth/login";
            }

            username = jwtService.extractSubject(token);

        }

        model.addAttribute("referrerUsername", username);
        model.addAttribute("register", new RegisterDTO());

        String queryString = request.getQueryString();
        String currentURI = queryString == null ? request.getRequestURI() : request.getRequestURI() + "?" + queryString;

        model.addAttribute("currentURI", currentURI);

        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDTO registerDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, @ModelAttribute("referrerUsername") String username) {
        model.addAttribute("formSubmitted", true);

        if (bindingResult.hasErrors()) {

            return "register";
        }

        try {
            User user = userMapper.dtoToUser(registerDTO);

            if (!username.isEmpty()) {
                userService.createWithReferral(user, userService.getByUsername(username));
            } else
                userService.create(user);



            String token = jwtService.generateEmailVerificationToken(user.getEmail());
            String verificationUrl = request.getScheme() + "://" + request.getServerName() + "/ui/auth/verify-email?token=" + token;


            String emailContent = "<p>Hello " + user.getUsername() + ",</p>"
                    + "<p>Please click the link below to verify your email:</p>"
                    + "<a href='" + verificationUrl + "'>Verify Email</a>"
                    + "<p>If you did not sign up, you can ignore this email.</p>";

            emailService.send(user.getEmail(), "Verify Your Email", emailContent);

            redirectAttributes.addFlashAttribute("registerSuccess", true);

            return "redirect:/ui/auth/login";

        } catch (DuplicateEntityException e) {
            if (e.getMessage().contains("username")) {
                bindingResult.rejectValue("username", "username.register", e.getMessage());
            } else if (e.getMessage().contains("email")) {
                bindingResult.rejectValue("emailAddress", "email.register", e.getMessage());
            } else
                bindingResult.rejectValue("phoneNumber", "phone.register", e.getMessage());

            return "register";
        }
    }


    @GetMapping("/contact")
    public String getContact(Model model) {

        model.addAttribute("contact", new ContactDTO());
        return "contact";
    }

    @PostMapping("/contact")
    public String handleContact(@Valid @ModelAttribute("contact") ContactDTO contactDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        model.addAttribute("formSubmitted", true);

        if (bindingResult.hasErrors()) {
            return "contact";
        }

        RestTemplate restTemplate = new RestTemplate();
        String formspreeUrl = "https://formspree.io/f/xblggzee";

        Map<String, String> formData = new HashMap<>();

        formData.put("name", contactDTO.getName());
        formData.put("email", contactDTO.getEmail());
        formData.put("message", contactDTO.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(formspreeUrl, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute("success", "Your message has been sent successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to send message. Please try again.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred. Please try again.");
        }

        return "redirect:/ui/auth/contact";
    }


    @GetMapping("/request-password")
    public String getRequestPassword(Authentication authentication, Model model) {
        if (authentication != null) {
            return "redirect:/ui/users/dashboard";
        }

        model.addAttribute("user", new UserRetrieveDTO());
        return "request-password";
    }

    @PostMapping("/request-password")
    public String handlePasswordRetrieval(@Valid @ModelAttribute("user") UserRetrieveDTO userRetrieveDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        model.addAttribute("formSubmitted", true);

        if (bindingResult.hasErrors()) {
            return "request-password";
        }

        try {
            User user = userService.getByUsername(userRetrieveDTO.getUsername());


            String token = jwtService.generateEmailVerificationToken(user.getEmail());
            String verificationUrl = request.getScheme() + "://" + request.getServerName() + "/ui/auth/reset-password?token=" + token;


            String emailContent = "<p>Hello " + user.getUsername() + ",</p>"
                    + "<p>Please click the link below to reset your password:</p>"
                    + "<a href='" + verificationUrl + "'>Reset Password</a>"
                    + "<p>If you did not request password reset, you can ignore this email.</p>";

            emailService.send(user.getEmail(), "Reset Your Password", emailContent);


            redirectAttributes.addFlashAttribute("requestPasswordSuccess", true);

            return "redirect:/ui/auth/login";
        } catch (EntityNotFoundException e) {
            bindingResult.rejectValue("username", "error.retrieve", e.getMessage());
            return "request-password";
        }

    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {


        try {
            if (jwtService.isTokenExpired(token)) {
                redirectAttributes.addFlashAttribute("passwordFail", true);
                return "redirect:/ui/auth/login";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("passwordFail", true);
            return "redirect:/ui/auth/login";
        }

        model.addAttribute("token", token);
        model.addAttribute("user", new UserPasswordUpdateDTO());

        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @Valid @ModelAttribute("user") UserPasswordUpdateDTO userPasswordUpdateDTO,
                                BindingResult bindingResult, HttpServletRequest request,
                                Model model, RedirectAttributes redirectAttributes,
                                HttpServletResponse response, Authentication authentication) {

        model.addAttribute("formSubmitted", true);

        if (bindingResult.hasErrors()) {
            return "reset-password";
        }

        if (!userPasswordUpdateDTO.getPassword().equals(userPasswordUpdateDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.register", "Passwords do not match");
            bindingResult.rejectValue("password", "error.register", "Password do not match");
            return "reset-password";
        }


        try {
            User user = userService.getByEmail(jwtService.extractSubject(token));

            user.setPassword(new BCryptPasswordEncoder().encode(userPasswordUpdateDTO.getPassword()));

            userService.update(user);

            model.addAttribute("currentURI", request.getRequestURI());

            if (authentication != null && authentication.isAuthenticated()) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }

            redirectAttributes.addFlashAttribute("changeSuccess", true);

            return "redirect:/ui/auth/login";

        } catch (Exception e) {
            bindingResult.rejectValue("password", "error.reset", e.getMessage());
            bindingResult.rejectValue("confirmPassword", "error.reset", e.getMessage());
            return "reset-password";
        }

    }


    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, RedirectAttributes redirectAttributes) {


        try {
            if (jwtService.isTokenExpired(token)) {
                redirectAttributes.addFlashAttribute("emailFail", true);
                return "redirect:/ui/auth/login";
            }

            String email = jwtService.extractSubject(token);

            userService.verifyEmail(email);

            redirectAttributes.addFlashAttribute("emailSuccess", true);
            return "redirect:/ui/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("emailFail", true);
            return "redirect:/ui/auth/login";
        }

    }

    @GetMapping("/refer")
    public String getReferralView(Model model) {

        model.addAttribute("user", new UserReferDTO());
        return "refer";
    }

    @PostMapping("/refer")
    public String handleReferral(@Valid @ModelAttribute("user") UserReferDTO userReferDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        model.addAttribute("formSubmitted", true);

        if (bindingResult.hasErrors()) {
            return "refer";
        }

        try {

            String referrerUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            String token = jwtService.generateUsernameReferralToken(referrerUsername);
            String verificationUrl = request.getScheme() + "://" + request.getServerName() + "/ui/auth/register?token=" + token;


            String emailContent = "<p>Hello " + userReferDTO.getEmailAddress() + ",</p>"
                    + "<p>You're being referred by " + referrerUsername + " to join our platform. We're excited to have you on board!</p>"
                    + "<p>To get started, please click the link below to complete your registration:</p>"
                    + "<a href='" + verificationUrl + "'>Register</a>"
                    + "<p>If you did not intend to sign up or received this message by mistake, please ignore this email.</p>"
                    + "<p>Thank you,</p>"
                    + "<p>The YNPay Team</p>";

            emailService.send(userReferDTO.getEmailAddress(), "Referral", emailContent);


            redirectAttributes.addFlashAttribute("userReferralSent", true);

            return "redirect:/ui/users/dashboard";
        } catch (EntityNotFoundException e) {
            bindingResult.rejectValue("emailAddress", "error.retrieve", e.getMessage());
            return "refer";
        }

    }
}
