
package com.securebank.utils;

import com.securebank.dto.AdminDto;
import com.securebank.dto.BeneficiaryDto;
import com.securebank.dto.CustomerDto;
import com.securebank.dto.TransactionDto;
import com.securebank.dto.UserDto;
import com.securebank.model.Admin;
import com.securebank.model.Beneficiary;
import com.securebank.model.Customer;
import com.securebank.model.Transaction;
import com.securebank.model.User;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public UserDto mapUserToDto(User user) {
        if (user instanceof Customer) {
            return mapCustomerToDto((Customer) user);
        } else if (user instanceof Admin) {
            return mapAdminToDto((Admin) user);
        }
        
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setAddress(user.getAddress());
        userDto.setRole(user.getRole());
        return userDto;
    }
    
    public CustomerDto mapCustomerToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPhone(customer.getPhone());
        customerDto.setAddress(customer.getAddress());
        customerDto.setRole(customer.getRole());
        customerDto.setAccountNumber(customer.getAccountNumber());
        customerDto.setBalance(customer.getBalance());
        return customerDto;
    }
    
    public AdminDto mapAdminToDto(Admin admin) {
        AdminDto adminDto = new AdminDto();
        adminDto.setId(admin.getId());
        adminDto.setName(admin.getName());
        adminDto.setEmail(admin.getEmail());
        adminDto.setPhone(admin.getPhone());
        adminDto.setAddress(admin.getAddress());
        adminDto.setRole(admin.getRole());
        adminDto.setEmployeeId(admin.getEmployeeId());
        adminDto.setDepartment(admin.getDepartment());
        return adminDto;
    }
    
    public TransactionDto mapTransactionToDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setFromAccount(transaction.getFromAccount());
        transactionDto.setToAccount(transaction.getToAccount());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setDate(transaction.getDate());
        transactionDto.setStatus(transaction.getStatus());
        return transactionDto;
    }
    
    public BeneficiaryDto mapBeneficiaryToDto(Beneficiary beneficiary) {
        BeneficiaryDto beneficiaryDto = new BeneficiaryDto();
        beneficiaryDto.setId(beneficiary.getId());
        beneficiaryDto.setName(beneficiary.getName());
        beneficiaryDto.setBankName(beneficiary.getBankName());
        beneficiaryDto.setAccountNumber(beneficiary.getAccountNumber());
        beneficiaryDto.setMaxTransferLimit(beneficiary.getMaxTransferLimit());
        return beneficiaryDto;
    }
}
