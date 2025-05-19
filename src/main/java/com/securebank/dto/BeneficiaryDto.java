
package com.securebank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryDto {
    private Long id;
    private String name;
    private String bankName;
    private String accountNumber;
    private BigDecimal maxTransferLimit;
}
