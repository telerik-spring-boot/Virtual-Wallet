package com.telerik.virtualwallet.controllers.mvc;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.InconsistentOperationException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.helpers.WalletMapper;
import com.telerik.virtualwallet.models.dtos.wallet.WalletCreateDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletMvcDisplayDTO;
import com.telerik.virtualwallet.models.enums.Currency;
import com.telerik.virtualwallet.services.wallet.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.telerik.virtualwallet.controllers.mvc.UserMvcController.populateIsAdminAttribute;

@Controller
@RequestMapping("/ui/wallets")
public class WalletMvcController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletMvcController(WalletService walletService, WalletMapper walletMapper) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin() {
        return populateIsAdminAttribute();

    }

    @GetMapping()
    public String getAllUserWallets(Authentication authentication, Model model) {

        addAllUserWalletsToModel(authentication, model);
        model.addAttribute("loggedUsername", authentication.getName());
        return "wallets";
    }

    @GetMapping("/new")
    public String createNewWalletForm(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "wallet-create";
    }

    @PostMapping("/new")
    public String handleCreateNewWallet(@RequestParam("selectedCurrency") String selectedCurrency,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttributes) {

        try {

            WalletCreateDTO wallet = new WalletCreateDTO(Currency.valueOf(selectedCurrency));
            walletService.createWallet(authentication.getName(), walletMapper.createDtoToWallet(wallet));

            redirectAttributes.addFlashAttribute("creationSuccess", true);

            return "redirect:/ui/wallets";
        } catch (UnauthorizedOperationException e) {
            redirectAttributes.addFlashAttribute("creationErrors", e.getMessage());
            return "redirect:/ui/wallets";
        }

    }

    @PreAuthorize("@walletSecurityService.isUserWalletCreator(#walletId, authentication.name)")
    @PostMapping("/{walletId}/add")
    public String addUserToWallet(@PathVariable int walletId, @RequestParam String username,
                                  RedirectAttributes redirectAttributes) {


        try {
            walletService.addUserToWallet(walletId, username);

            redirectAttributes.addFlashAttribute("addingSuccess", username);
            return "redirect:/ui/wallets";
        } catch (DuplicateEntityException | EntityNotFoundException | UnauthorizedOperationException e) {
            redirectAttributes.addFlashAttribute("addingErrors", e.getMessage());
            return "redirect:/ui/wallets";
        }

    }

    @PreAuthorize("@walletSecurityService.isUserWalletCreator(#walletId, authentication.name)")
    @GetMapping("/{walletId}/remove")
    public String removeUserToWallet(@PathVariable int walletId, @RequestParam String username,
                                     RedirectAttributes redirectAttributes) {

        try {
            walletService.removeUserFromWallet(walletId, username);

            redirectAttributes.addFlashAttribute("removalSuccess", username);
            return "redirect:/ui/wallets";
        } catch (DuplicateEntityException | EntityNotFoundException |
                 UnauthorizedOperationException | InconsistentOperationException e) {
            redirectAttributes.addFlashAttribute("removalErrors", e.getMessage());
            return "redirect:/ui/wallets";
        }
    }

    @GetMapping("/transfer")
    public String createTransferForm(Authentication authentication, Model model)
    {
        addAllUserWalletsToModel(authentication, model);
        return "transfer-make";
    }

    private void addAllUserWalletsToModel(Authentication authentication, Model model) {
        List<WalletMvcDisplayDTO> wallets = walletService.getWalletsByUsername(authentication.getName()).stream()
                .map(walletMapper::walletToMvcDto)
                .toList();

        model.addAttribute("wallets", wallets);
    }
}
