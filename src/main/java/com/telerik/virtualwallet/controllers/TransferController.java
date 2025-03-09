package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.helpers.TransactionMapper;
import com.telerik.virtualwallet.models.Transfer;
import com.telerik.virtualwallet.models.dtos.transaction.TransferDisplayDTO;
import com.telerik.virtualwallet.services.transaction.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransferController(TransferService transferService, TransactionMapper transactionMapper) {
        this.transferService = transferService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping("/{transferId}")
    @PreAuthorize("hasRole('ADMIN') OR " +
            "@transferSecurityService.isUserTransferReceiver(#transferId, authentication.name)")
    public ResponseEntity<TransferDisplayDTO> getTransferById(@PathVariable int transferId) {

        Transfer transfer = transferService.getTransferById(transferId);

        return ResponseEntity.ok(transactionMapper.transferToTransferDisplayDTO(transfer));
    }
}
