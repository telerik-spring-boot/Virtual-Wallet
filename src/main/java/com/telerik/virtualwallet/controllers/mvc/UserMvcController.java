package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayMvcDTO;
import com.telerik.virtualwallet.models.dtos.user.UserUpdateDTO;
import com.telerik.virtualwallet.models.dtos.user.UserUpdateMvcDTO;
import com.telerik.virtualwallet.services.jwt.JwtService;
import com.telerik.virtualwallet.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public UserMvcController(UserService userService, UserMapper userMapper, JwtService jwtService){
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
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
    public String showUserSettings(Authentication authentication, Model model, HttpServletRequest request) {

        try {
            User user = userService.getByUsername(authentication.getName());

            UserUpdateMvcDTO userUpdate = userMapper.userToUserUpdateMvcDto(user);
            UserDisplayMvcDTO userDisplay = userMapper.userToUserDisplayMvcDTO(user);


            model.addAttribute("userUpdate", userUpdate);
            model.addAttribute("userDisplay",userDisplay);
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("token", jwtService.generateEmailVerificationToken(user.getEmail()));

            return "settings";
        } catch (UnauthorizedOperationException e) {
            return "redirect:/auth/login";
        }
    }

    @PutMapping("/settings")
    public String handleUpdateUser(Authentication authentication, @Valid @ModelAttribute("userUpdate") UserUpdateMvcDTO userUpdateMvcDTO,  BindingResult bindingResult, RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/settings";
        }

        try {

            User userToUpdate = userMapper.dtoToUser(userUpdateMvcDTO, authentication.getName());

            userService.update(userToUpdate);

            redirectAttributes.addFlashAttribute("successUpdate", true);

            return "redirect:/users/settings";

        } catch (DuplicateEntityException e) {
            bindingResult.reject("duplicateEntityError", e.getMessage());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/settings";
        }
    }

    @DeleteMapping
    public String deleteUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication, RedirectAttributes redirectAttributes){

        userService.delete(authentication.getName());

        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        redirectAttributes.addFlashAttribute("userDeleted", true);
        return "redirect:/auth/login";

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
