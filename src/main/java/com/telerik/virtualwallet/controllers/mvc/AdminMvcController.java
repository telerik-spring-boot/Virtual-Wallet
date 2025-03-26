package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.dtos.transaction.InvestmentDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionsWrapper;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayDTO;
import com.telerik.virtualwallet.models.dtos.user.UserDisplayMvcDTO;
import com.telerik.virtualwallet.services.admin.AdminService;
import com.telerik.virtualwallet.services.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/ui/admin")
public class AdminMvcController {

    private final AdminService adminService;
    private final UserService userService;
    private final TransactionMapper transactionMapper;
    private final UserMapper userMapper;

    public AdminMvcController(AdminService adminService, UserMapper userMapper, TransactionMapper transactionMapper, UserService userService) {
        this.adminService = adminService;
        this.transactionMapper = transactionMapper;
        this.userMapper =userMapper;
        this.userService = userService;
    }

    @GetMapping
    public String getAdminPanel(Model model) {

        List<UserDisplayMvcDTO> users = adminService.getAllUsersMvc().stream().map(userMapper::userToUserDisplayMvcDTO).toList();
        model.addAttribute("users", users);

        model.addAttribute("investments", adminService.getAllInvestments().stream()
                .flatMap(investment -> transactionMapper.investmentToInvestmentDTO(investment).stream())
                .toList());

        List<TransactionsWrapper> transactions = new ArrayList<>(adminService.getAllTransactions().stream().map(transactionMapper::transactionToTransactionWrapper).toList());


        transactions.addAll(adminService.getAllTransfers().stream().map(transactionMapper::transferToTransactionWrapper).toList());

        model.addAttribute("transactions", transactions);


        return "admin-panel";
    }


    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin() {
        return true;
    }

    @PutMapping("/users/{userId}/grant")
    public String grantAdminRights(@PathVariable int userId) {


        adminService.giveAdminRights(userId);

        return "redirect:/ui/admin";

    }

    @PutMapping("/users/{userId}/revoke")
    public String revokeAdminRights(@PathVariable int userId) {

        adminService.revokeAdminRights(userId);

        return "redirect:/ui/admin";


    }

    @PutMapping("/users/{userId}/block")
    public String blockUser(@PathVariable int userId) {


        User userToBlock = userService.getById(userId);

        adminService.blockUser(userToBlock);

        return "redirect:/ui/admin";


    }

    @PutMapping("/users/{userId}/unblock")
    public String unblockUser(@PathVariable int userId) {

        User userToBlock = userService.getById(userId);

        adminService.unblockUser(userToBlock);

        return "redirect:/ui/admin";

    }

    @PutMapping("/users/{username}/approve-verification")
    public String approveVerification(@PathVariable String username) {

        adminService.approveUserPictureVerification(username);

        return "redirect:/ui/admin";


    }

    @PutMapping("/users/{username}/reject-verification")
    public String rejectVerification(@PathVariable String username) {

        adminService.rejectUserPictureVerification(username);

        return "redirect:/ui/admin";


    }
}
