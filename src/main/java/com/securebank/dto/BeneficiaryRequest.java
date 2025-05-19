
package com.securebank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Bank name is required")
    private String bankName;
    
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    
    @NotNull(message = "Max transfer limit is required")
    @DecimalMin(value = "0.01", message = "Max transfer limit must be greater than 0")
    private BigDecimal maxTransferLimit;
}
