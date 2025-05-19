
package com.securebank.controller;

import com.securebank.dto.BeneficiaryDto;
import com.securebank.dto.BeneficiaryRequest;
import com.securebank.service.BeneficiaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isCurrentUser(#customerId)")
    public ResponseEntity<List<BeneficiaryDto>> getBeneficiariesByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiariesByCustomerId(customerId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isBeneficiaryOwner(#id)")
    public ResponseEntity<BeneficiaryDto> getBeneficiaryById(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiaryById(id));
    }

    @PostMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isCurrentUser(#customerId)")
    public ResponseEntity<BeneficiaryDto> createBeneficiary(
            @PathVariable Long customerId,
            @Valid @RequestBody BeneficiaryRequest beneficiaryRequest) {
        return ResponseEntity.ok(beneficiaryService.createBeneficiary(customerId, beneficiaryRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isBeneficiaryOwner(#id)")
    public ResponseEntity<BeneficiaryDto> updateBeneficiary(
            @PathVariable Long id,
            @Valid @RequestBody BeneficiaryRequest beneficiaryRequest) {
        return ResponseEntity.ok(beneficiaryService.updateBeneficiary(id, beneficiaryRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isBeneficiaryOwner(#id)")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.noContent().build();
    }
}
