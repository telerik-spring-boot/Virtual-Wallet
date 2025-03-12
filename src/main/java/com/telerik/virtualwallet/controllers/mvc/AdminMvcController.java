package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.helpers.UserMapper;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionsWrapper;
import com.telerik.virtualwallet.services.admin.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admins")
public class AdminMvcController {

    private final AdminService adminService;
    private final TransactionMapper transactionMapper;
    private final UserMapper userMapper;

    public AdminMvcController(AdminService adminService, UserMapper userMapper, TransactionMapper transactionMapper) {
        this.adminService = adminService;
        this.transactionMapper = transactionMapper;
        this.userMapper =userMapper;
    }

    @GetMapping
    public String getAdminPanel(Model model) {

        model.addAttribute("users", adminService.getAllUsersMvc().stream().map(userMapper::userToUserDisplayMvcDTO).toList());

        List<TransactionsWrapper> transactions = new ArrayList<>(adminService.getAllTransactions().stream().map(transactionMapper::transactionToTransactionWrapper).toList());

        transactions.addAll(adminService.getAllTransfers().stream().map(transactionMapper::transferToTransactionWrapper).toList());

        model.addAttribute("transactions", transactions);

        return "admin-panel";
    }


    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin() {
        return true;
    }
}
