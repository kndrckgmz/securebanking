
package com.securebank.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CustomerDto extends UserDto {
    private String accountNumber;
    private BigDecimal balance;
}
