
package com.securebank.dto;

import com.securebank.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String description;
    private LocalDateTime date;
    private TransactionStatus status;
}
