
package com.securebank.controller;

import com.securebank.dto.TransactionDto;
import com.securebank.dto.TransferRequest;
import com.securebank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isCustomerAccountOwner(#accountNumber)")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('CUSTOMER') and @securityUtil.isCustomerAccountOwner(#transferRequest.fromAccount)")
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransferRequest transferRequest) {
        return ResponseEntity.ok(transactionService.createTransaction(transferRequest));
    }
}
