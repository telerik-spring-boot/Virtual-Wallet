package com.telerik.virtualwallet.controllers.mvc;

import com.telerik.virtualwallet.exceptions.*;
import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.helpers.WalletMapper;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionConfirmationMVCCreateDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionMVCUsernamePhoneCreateDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletCreateDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletMvcDisplayDTO;
import com.telerik.virtualwallet.models.enums.Currency;
import com.telerik.virtualwallet.models.enums.TransactionCategoryEnum;
import com.telerik.virtualwallet.services.transaction.TransactionService;
import com.telerik.virtualwallet.services.user.UserService;
import com.telerik.virtualwallet.services.wallet.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.telerik.virtualwallet.controllers.mvc.UserMvcController.populateIsAdminAttribute;

@Controller
@RequestMapping("/ui/wallets")
public class WalletMvcController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public WalletMvcController(WalletService walletService, WalletMapper walletMapper, UserService userService, TransactionService transactionService, TransactionMapper transactionMapper) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;
        this.userService = userService;
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
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
    public String createTransferForm(Authentication authentication, Model model) {
        model.addAttribute("usernameInput", new TransactionMVCUsernamePhoneCreateDTO());
        addAllUserWalletsToModel(authentication, model);
        return "transfer-make";
    }

    @PreAuthorize("@walletSecurityService.isUserWalletCreator(#walletId, authentication.name)")
    @GetMapping("/transfer/by_username/confirmation")
    public String handleTransferByUsername(Model model, @RequestParam("walletId") int walletId,
                                           Authentication authentication,
                                           @Valid @ModelAttribute("usernameInput")
                                           TransactionMVCUsernamePhoneCreateDTO dto,
                                           BindingResult bindingResult,
                                           HttpServletRequest request) {

//        model.addAttribute("formSubmitted", true);

        if (bindingResult.hasErrors()) {
            return "transfer-make";
        }
        try {
            User receiver = userService.getByUsernameOrEmailOrPhoneNumberMVC(dto.getUsernameOrPhone());

            Wallet receiverWallet = walletService
                    .getReceiverWalletBySenderWalletIdAndReceiverUsernameMVC(walletId, receiver.getUsername());

            TransactionConfirmationMVCCreateDTO confirmation = transactionMapper
                    .handleConfirmationMVCDTOLogic(walletId, receiverWallet, receiver, dto.getAmount());

            model.addAttribute("confirmation", confirmation);
            model.addAttribute("transactionCategories", TransactionCategoryEnum.values());
            model.addAttribute("requestURI", request.getRequestURI());

            return "transfer-confirmation";

        } catch (EntityNotFoundException e) {

            bindingResult.rejectValue("usernameOrPhone", "card.number", e.getMessage());

            addAllUserWalletsToModel(authentication, model);

            return "transfer-make";
        }


    }

    @PostMapping("/transfer/by_username/confirmation")
    public String handleTransferByUsernamePost(Model model,
                                               Authentication authentication,
                                               @Valid @ModelAttribute("confirmation")
                                               TransactionConfirmationMVCCreateDTO confirmation,
                                               BindingResult bindingResult,
                                               RedirectAttributes redirectAttributes) {

        model.addAttribute("formSubmitted", true);

        if (bindingResult.hasErrors()) {
            model.addAttribute("transactionCategories", TransactionCategoryEnum.values());
            return "transfer-confirmation";
        }
        try {

            Transaction transaction = transactionMapper.mvcDtoToTransaction(confirmation,authentication.getName());

            transactionService.makeTransactionMVC(transaction, confirmation.getReceivedAmount());

            return "redirect:/ui/users/dashboard";

        } catch (InsufficientFundsException e) {
            bindingResult.rejectValue("sentAmount", "card.number", e.getMessage());

            redirectAttributes.addFlashAttribute("insufficientFunds", true);
            model.addAttribute("usernameInput", new TransactionMVCUsernamePhoneCreateDTO());
            addAllUserWalletsToModel(authentication, model);

            return "transfer-confirmation";
        }

    }

    private void addAllUserWalletsToModel(Authentication authentication, Model model) {
        List<WalletMvcDisplayDTO> wallets = walletService.getWalletsByUsername(authentication.getName()).stream()
                .map(walletMapper::walletToMvcDto)
                .toList();

        model.addAttribute("wallets", wallets);
    }
}
