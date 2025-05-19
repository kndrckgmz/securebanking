
package com.securebank.controller;

import com.securebank.dto.CustomerDto;
import com.securebank.dto.UserDto;
import com.securebank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isCurrentUser(#id)")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/account/{accountNumber}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isCustomerAccountOwner(#accountNumber)")
    public ResponseEntity<CustomerDto> getCustomerByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(customerService.getCustomerByAccountNumber(accountNumber));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isCurrentUser(#id)")
    public ResponseEntity<UserDto> updateCustomer(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, userDto));
    }
}
