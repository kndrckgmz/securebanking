
package com.securebank.security;

import com.securebank.model.Beneficiary;
import com.securebank.model.Customer;
import com.securebank.repository.BeneficiaryRepository;
import com.securebank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("securityUtil")
public class SecurityUtil {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    public boolean isCurrentUser(Long userId) {
        UserPrincipal userPrincipal = getCurrentUser();
        if (userPrincipal == null) {
            return false;
        }
        return userPrincipal.getId().equals(userId);
    }
    
    public boolean isCustomerAccountOwner(String accountNumber) {
        UserPrincipal userPrincipal = getCurrentUser();
        if (userPrincipal == null) {
            return false;
        }
        
        Optional<Customer> customerOptional = customerRepository.findById(userPrincipal.getId());
        return customerOptional.isPresent() && customerOptional.get().getAccountNumber().equals(accountNumber);
    }
    
    public boolean isBeneficiaryOwner(Long beneficiaryId) {
        UserPrincipal userPrincipal = getCurrentUser();
        if (userPrincipal == null) {
            return false;
        }
        
        Optional<Beneficiary> beneficiaryOptional = beneficiaryRepository.findById(beneficiaryId);
        return beneficiaryOptional.isPresent() && 
               beneficiaryOptional.get().getCustomer() != null && 
               beneficiaryOptional.get().getCustomer().getId().equals(userPrincipal.getId());
    }
    
    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            return null;
        }
        return (UserPrincipal) authentication.getPrincipal();
    }
}
