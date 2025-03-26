package com.telerik.virtualwallet.controllers.mvc;

import com.telerik.virtualwallet.exceptions.*;
import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.helpers.WalletMapper;
import com.telerik.virtualwallet.models.Transaction;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.Wallet;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionConfirmationMVCCreateDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionMVCIBANCreateDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionMVCUsernamePhoneCreateDTO;
import com.telerik.virtualwallet.models.dtos.transaction.TransactionsWrapper;
import com.telerik.virtualwallet.models.dtos.wallet.WalletCreateDTO;
import com.telerik.virtualwallet.models.dtos.wallet.WalletMvcDisplayDTO;
import com.telerik.virtualwallet.models.enums.Currency;
import com.telerik.virtualwallet.models.enums.TransactionCategoryEnum;
import com.telerik.virtualwallet.services.transaction.TransactionService;
import com.telerik.virtualwallet.services.transaction.TransferService;
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

import java.util.ArrayList;
import java.util.List;

import static com.telerik.virtualwallet.controllers.mvc.UserMvcController.populateIsAdminAttribute;

@Controller
@RequestMapping("/ui/wallets")
public class WalletMvcController {

    public static final String IBAN_WAS_NOT_RECOGNISED = "IBAN was not recognised.";
    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final TransferService transferService;

    @Autowired
    public WalletMvcController(WalletService walletService, WalletMapper walletMapper, UserService userService, TransactionService transactionService, TransactionMapper transactionMapper, TransferService transferService) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;
        this.userService = userService;
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.transferService = transferService;
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
        model.addAttribute("IBANInput", new TransactionMVCIBANCreateDTO());
        addAllUserWalletsToModel(authentication, model);
        return "transfer-make";
    }

    @PostMapping("/transfer/users")
    public String handleTransferFormByUsername(Model model, @RequestParam("walletId") int walletId,
                                               Authentication authentication,
                                               @Valid @ModelAttribute("usernameInput")
                                               TransactionMVCUsernamePhoneCreateDTO dto,
                                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("IBANInput", new TransactionMVCIBANCreateDTO());
            addAllUserWalletsToModel(authentication, model);
            model.addAttribute("userDoesNotExist", dto.getUsernameOrPhone());
            return "transfer-make";
        }

        try {
            User receiver = userService.getByUsernameOrEmailOrPhoneNumberMVC(dto.getUsernameOrPhone());

            Wallet receiverWallet = walletService
                    .getReceiverWalletBySenderWalletIdAndReceiverUsernameMVC(walletId, receiver.getUsername());

            TransactionConfirmationMVCCreateDTO confirmation = transactionMapper
                    .handleConfirmationMVCDTOLogic(walletId, receiverWallet, receiver, dto.getAmount());

            redirectAttributes.addFlashAttribute("walletId", walletId);
            redirectAttributes.addFlashAttribute("confirmation", confirmation);
            redirectAttributes.addFlashAttribute("transactionCategories", TransactionCategoryEnum.values());

            return "redirect:/ui/wallets/transfer/confirmation";

        } catch (EntityNotFoundException e) {

            bindingResult.rejectValue("usernameOrPhone", "card.number", e.getMessage());
            model.addAttribute("IBANInput", new TransactionMVCIBANCreateDTO());
            addAllUserWalletsToModel(authentication, model);

            model.addAttribute("userDoesNotExist", dto.getUsernameOrPhone());

            return "transfer-make";
        }
    }

    @PostMapping("/transfer/wallet")
    public String handleTransferFormByIban(Model model, @RequestParam("walletId") int walletId,
                                           Authentication authentication,
                                           @Valid @ModelAttribute("IBANInput")
                                           TransactionMVCIBANCreateDTO dto,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("usernameInput", new TransactionMVCUsernamePhoneCreateDTO());
            addAllUserWalletsToModel(authentication, model);

            return "transfer-make";
        }

        if (!dto.getIBAN().toUpperCase().contains("IBAN33YNPAY68477732491843244")) {
            bindingResult.rejectValue("IBAN", "card.number", IBAN_WAS_NOT_RECOGNISED);

            model.addAttribute("usernameInput", new TransactionMVCUsernamePhoneCreateDTO());
            addAllUserWalletsToModel(authentication, model);

            model.addAttribute("wrongIBAN", dto.getIBAN());
            model.addAttribute("showIbanTab", true);
            return "transfer-make";
        }
        try {

            String extractedId = dto.getIBAN().replaceFirst("IBAN33YNPAY68477732491843244", "");
            int walletReceiverId = Integer.parseInt(extractedId);

            Wallet receiverWallet = walletService.getWalletById(walletReceiverId);

            TransactionConfirmationMVCCreateDTO confirmation = transactionMapper
                    .handleConfirmationMVCDTOLogic(walletId, receiverWallet, receiverWallet.getCreator(), dto.getAmount());

            redirectAttributes.addFlashAttribute("walletId", walletId);
            redirectAttributes.addFlashAttribute("confirmation", confirmation);
            redirectAttributes.addFlashAttribute("transactionCategories", TransactionCategoryEnum.values());

            return "redirect:/ui/wallets/transfer/confirmation";

        } catch (NumberFormatException e) {

            bindingResult.rejectValue("IBAN", "card.number", IBAN_WAS_NOT_RECOGNISED);

            model.addAttribute("usernameInput", new TransactionMVCUsernamePhoneCreateDTO());
            addAllUserWalletsToModel(authentication, model);

            model.addAttribute("wrongIBAN", dto.getIBAN());
            model.addAttribute("showIbanTab", true);

            return "transfer-make";
        }
    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @GetMapping("/transfer/confirmation")
    public String handleTransferByUsername(Model model, @ModelAttribute("walletId") int walletId,
                                           @ModelAttribute("confirmation")
                                           TransactionConfirmationMVCCreateDTO dto,
                                           @ModelAttribute("transactionCategories")
                                           TransactionCategoryEnum[] categories,
                                           HttpServletRequest request) {

        model.addAttribute("requestURI", request.getRequestURI());

        return "transfer-confirmation";

    }

    @PostMapping("/transfer/confirmation")
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

            Transaction transaction = transactionMapper.mvcDtoToTransaction(confirmation, authentication.getName());

            transactionService.makeTransactionMVC(transaction, confirmation.getReceivedAmount());

            redirectAttributes.addFlashAttribute("transferSuccess", true);

            return "redirect:/ui/users/dashboard";

        } catch (InsufficientFundsException e) {
            bindingResult.rejectValue("sentAmount", "card.number", e.getMessage());

            redirectAttributes.addFlashAttribute("insufficientFunds", true);
            model.addAttribute("usernameInput", new TransactionMVCUsernamePhoneCreateDTO());
            addAllUserWalletsToModel(authentication, model);

            return "transfer-confirmation";
        }

    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @GetMapping("/{walletId}/transactions")
    public String getTransactions(@PathVariable int walletId, Model model) {

        List<TransactionsWrapper> transactions =
                new ArrayList<>(transactionService.getTransactionsByWalletId(walletId)
                        .stream().map(transactionMapper::transactionToTransactionWrapper).toList());


        transactions.addAll(transferService.getAllTransfersByWalletId(walletId)
                .stream().map(transactionMapper::transferToTransactionWrapper).toList());

        model.addAttribute("walletId", walletId);
        model.addAttribute("transactions", transactions);
        model.addAttribute("activeTab", "all");

        return "transaction-wallet";
    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @GetMapping("/{walletId}/transactions/incoming")
    public String getIncomingTransactions(@PathVariable int walletId, Model model) {

        List<TransactionsWrapper> transactions =
                new ArrayList<>(transactionService.getIncomingTransactionsByWalletId(walletId)
                        .stream().map(transactionMapper::transactionToTransactionWrapper).toList());


        transactions.addAll(transferService.getAllTransfersByWalletId(walletId)
                .stream().map(transactionMapper::transferToTransactionWrapper).toList());

        model.addAttribute("walletId", walletId);
        model.addAttribute("transactions", transactions);
        model.addAttribute("activeTab", "in");

        return "transaction-wallet";
    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @GetMapping("/{walletId}/transactions/outgoing")
    public String getOutgoingTransactions(@PathVariable int walletId, Model model) {

        List<TransactionsWrapper> transactions =
                new ArrayList<>(transactionService.getOutgoingTransactionsByWalletId(walletId)
                        .stream().map(transactionMapper::transactionToTransactionWrapper).toList());

        model.addAttribute("walletId", walletId);
        model.addAttribute("transactions", transactions);
        model.addAttribute("activeTab", "out");

        return "transaction-wallet";
    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @GetMapping("/{walletId}/top-up")
    public String topUpWalletFromAnotherWallet(@PathVariable int walletId, Authentication authentication,
                                               Model model, HttpServletRequest request) {

        model.addAttribute("usernameInput", new TransactionMVCUsernamePhoneCreateDTO());
        model.addAttribute("requestURI", request.getRequestURI());
        addAllUserWalletsToModel(authentication, model);
        return "transfer-make-internal";
    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @PostMapping("/{walletId}/top-up")
    public String handleTopUpWalletFromAnotherWallet(Model model, @PathVariable int walletId,
                                                     @RequestParam("senderWalletId") int senderWalletId,
                                                     Authentication authentication,
                                                     @ModelAttribute("usernameInput")
                                                     TransactionMVCUsernamePhoneCreateDTO dto,
                                                     BindingResult bindingResult,
                                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "transfer-make-internal";
        }
        try {
            User receiverUser = userService.getByUsername(authentication.getName());

            Wallet receiverWallet = walletService.getWalletById(walletId);

            TransactionConfirmationMVCCreateDTO confirmation = transactionMapper
                    .handleConfirmationMVCDTOLogic(senderWalletId, receiverWallet, receiverUser, dto.getAmount());


            redirectAttributes.addFlashAttribute("walletId", walletId);
            redirectAttributes.addFlashAttribute("confirmation", confirmation);
            redirectAttributes.addFlashAttribute("transactionCategories", TransactionCategoryEnum.values());

            return "redirect:/ui/wallets/top-up/confirmation";

        } catch (EntityNotFoundException e) {

            bindingResult.rejectValue("usernameOrPhone", "card.number", e.getMessage());

            addAllUserWalletsToModel(authentication, model);

            return "transfer-make-internal";
        }
    }

    @PreAuthorize("@walletSecurityService.isUserWalletHolder(#walletId, authentication.name)")
    @GetMapping("/top-up/confirmation")
    public String handleTopUp(Model model, @ModelAttribute("walletId") int walletId,
                                           @ModelAttribute("confirmation")
                                           TransactionConfirmationMVCCreateDTO dto,
                                           @ModelAttribute("transactionCategories")
                                           TransactionCategoryEnum[] categories,
                                           HttpServletRequest request) {

        model.addAttribute("requestURI", request.getRequestURI());

        return "transfer-confirmation-internal";

    }

    @PostMapping("/top-up/confirmation")
    public String handleTopUpConfirmation(Model model,
                                               Authentication authentication,
                                               @Valid @ModelAttribute("confirmation")
                                               TransactionConfirmationMVCCreateDTO confirmation,
                                               BindingResult bindingResult,
                                               RedirectAttributes redirectAttributes) {

        model.addAttribute("formSubmitted", true);

        if (bindingResult.hasErrors()) {
            model.addAttribute("transactionCategories", TransactionCategoryEnum.values());
            return "transfer-confirmation-internal";
        }
        try {

            Transaction transaction = transactionMapper.mvcDtoToTransaction(confirmation, authentication.getName());

            transactionService.makeTransactionMVC(transaction, confirmation.getReceivedAmount());

            redirectAttributes.addFlashAttribute("transferSuccess", true);

            return "redirect:/ui/wallets";

        } catch (InsufficientFundsException e) {
            bindingResult.rejectValue("sentAmount", "card.number", e.getMessage());

            redirectAttributes.addFlashAttribute("insufficientFunds", true);
            model.addAttribute("usernameInput", new TransactionMVCUsernamePhoneCreateDTO());
            addAllUserWalletsToModel(authentication, model);

            return "transfer-confirmation-internal";
        }

    }

    private void addAllUserWalletsToModel(Authentication authentication, Model model) {
        List<WalletMvcDisplayDTO> wallets = walletService.getWalletsByUsername(authentication.getName()).stream()
                .map(walletMapper::walletToMvcDto)
                .toList();

        model.addAttribute("wallets", wallets);
    }
}
