package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.LoginDTO;
import com.telerik.virtualwallet.models.dtos.RegisterDTO;
import com.telerik.virtualwallet.services.email.EmailService;
import com.telerik.virtualwallet.services.jwt.JwtService;
import com.telerik.virtualwallet.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AnonymousController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Autowired
    public AnonymousController(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService userDetailsService, UserService userService, UserMapper userMapper, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginDTO loginDTO) {

        authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());

        String jwtToken = jwtService.generateToken(userDetails.getUsername(), userDetails.getAuthorities());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterDTO registerDTO, HttpServletRequest request) {

        User user = userMapper.dtoToUser(registerDTO);

        userService.create(user);

        // DO NOT UNCOMMENT BEFORE PRODUCTION, SENDS REAL EMAILS

//        String token = jwtService.generateEmailVerificationToken(user.getEmail());
//        String verificationUrl = request.getScheme() + "://" + request.getServerName() + "/api/auth/verify-email?token=" + token;
//
//
//        String emailContent = "<p>Hello " + user.getUsername() + ",</p>"
//                + "<p>Please click the link below to verify your email:</p>"
//                + "<a href='" + verificationUrl + "'>Verify Email</a>"
//                + "<p>If you did not sign up, you can ignore this email.</p>";
//
//        emailService.send(user.getEmail(), "Verify Your Email", emailContent);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {

        String email = jwtService.extractSubject(token);

        if(jwtService.isTokenExpired(token)){
            return ResponseEntity.badRequest().body("Invalid token");
        }

        userService.verifyEmail(email);

        return ResponseEntity.ok("Successfully verified email.");

    }

}
