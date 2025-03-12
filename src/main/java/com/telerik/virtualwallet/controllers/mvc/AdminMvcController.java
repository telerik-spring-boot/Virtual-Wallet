package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.services.admin.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admins")
public class AdminMvcController {

    private final AdminService adminService;
    private final TransactionMapper transactionMapper;

    public AdminMvcController(AdminService adminService, TransactionMapper transactionMapper) {
        this.adminService = adminService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping
    public String getAdminPanel(Model model) {

        model.addAttribute("users", adminService.getAllUsersMvc());


        model.addAttribute("transfers", adminService.getAllTransfers().stream().map(transactionMapper::transferToTransferDisplayDTO).toList());

        model.addAttribute("transactions", adminService.getAllTransactions().stream().map(transactionMapper::transactionToTransactionDisplayDTO).toList());

        return "admin-panel";
    }


    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin() {
        return true;
    }
}
