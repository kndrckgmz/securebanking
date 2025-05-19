
package com.securebank.service;

import com.securebank.dto.CustomerDto;
import com.securebank.dto.UserDto;
import com.securebank.exception.ResourceNotFoundException;
import com.securebank.model.Customer;
import com.securebank.repository.CustomerRepository;
import com.securebank.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(modelMapper::mapCustomerToDto)
                .collect(Collectors.toList());
    }
    
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return modelMapper.mapCustomerToDto(customer);
    }
    
    public CustomerDto getCustomerByAccountNumber(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with account number: " + accountNumber));
        return modelMapper.mapCustomerToDto(customer);
    }
    
    public UserDto updateCustomer(Long id, UserDto userDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        customer.setName(userDto.getName());
        customer.setPhone(userDto.getPhone());
        customer.setAddress(userDto.getAddress());
        
        Customer updatedCustomer = customerRepository.save(customer);
        return modelMapper.mapUserToDto(updatedCustomer);
    }
}
