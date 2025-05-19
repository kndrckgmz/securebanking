
package com.securebank.service;

import com.securebank.dto.BeneficiaryDto;
import com.securebank.dto.BeneficiaryRequest;
import com.securebank.exception.ResourceNotFoundException;
import com.securebank.model.Beneficiary;
import com.securebank.model.Customer;
import com.securebank.repository.BeneficiaryRepository;
import com.securebank.repository.CustomerRepository;
import com.securebank.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public List<BeneficiaryDto> getBeneficiariesByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByCustomer(customer);
        return beneficiaries.stream()
                .map(modelMapper::mapBeneficiaryToDto)
                .collect(Collectors.toList());
    }
    
    public BeneficiaryDto getBeneficiaryById(Long id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + id));
        return modelMapper.mapBeneficiaryToDto(beneficiary);
    }
    
    public BeneficiaryDto createBeneficiary(Long customerId, BeneficiaryRequest beneficiaryRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setName(beneficiaryRequest.getName());
        beneficiary.setBankName(beneficiaryRequest.getBankName());
        beneficiary.setAccountNumber(beneficiaryRequest.getAccountNumber());
        beneficiary.setMaxTransferLimit(beneficiaryRequest.getMaxTransferLimit());
        beneficiary.setCustomer(customer);
        
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        return modelMapper.mapBeneficiaryToDto(savedBeneficiary);
    }
    
    public BeneficiaryDto updateBeneficiary(Long id, BeneficiaryRequest beneficiaryRequest) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + id));
        
        beneficiary.setName(beneficiaryRequest.getName());
        beneficiary.setBankName(beneficiaryRequest.getBankName());
        beneficiary.setAccountNumber(beneficiaryRequest.getAccountNumber());
        beneficiary.setMaxTransferLimit(beneficiaryRequest.getMaxTransferLimit());
        
        Beneficiary updatedBeneficiary = beneficiaryRepository.save(beneficiary);
        return modelMapper.mapBeneficiaryToDto(updatedBeneficiary);
    }
    
    public void deleteBeneficiary(Long id) {
        if (!beneficiaryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Beneficiary not found with id: " + id);
        }
        beneficiaryRepository.deleteById(id);
    }
}
