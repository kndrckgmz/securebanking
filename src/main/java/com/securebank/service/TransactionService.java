
package com.securebank.service;

import com.securebank.dto.TransactionDto;
import com.securebank.dto.TransferRequest;
import com.securebank.exception.InsufficientBalanceException;
import com.securebank.exception.ResourceNotFoundException;
import com.securebank.model.Customer;
import com.securebank.model.Transaction;
import com.securebank.model.TransactionStatus;
import com.securebank.repository.CustomerRepository;
import com.securebank.repository.TransactionRepository;
import com.securebank.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAllByOrderByDateDesc();
        return transactions.stream()
                .map(modelMapper::mapTransactionToDto)
                .collect(Collectors.toList());
    }
    
    public List<TransactionDto> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findByFromAccountOrToAccountOrderByDateDesc(accountNumber, accountNumber);
        return transactions.stream()
                .map(modelMapper::mapTransactionToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TransactionDto createTransaction(TransferRequest transferRequest) {
        // Find the source customer
        Customer sourceCustomer = customerRepository.findByAccountNumber(transferRequest.getFromAccount())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found: " + transferRequest.getFromAccount()));
        
        // Check if destination account exists
        customerRepository.findByAccountNumber(transferRequest.getToAccount())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found: " + transferRequest.getToAccount()));
        
        // Check if source has sufficient balance
        if (sourceCustomer.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }
        
        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setFromAccount(transferRequest.getFromAccount());
        transaction.setToAccount(transferRequest.getToAccount());
        transaction.setAmount(transferRequest.getAmount());
        transaction.setDescription(transferRequest.getDescription() != null ? 
                transferRequest.getDescription() : "Transfer to " + transferRequest.getToAccount());
        transaction.setDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        
        // Save transaction
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Update balances
        updateBalances(transferRequest);
        
        return modelMapper.mapTransactionToDto(savedTransaction);
    }
    
    @Transactional
    private void updateBalances(TransferRequest transferRequest) {
        // Update source balance
        Customer sourceCustomer = customerRepository.findByAccountNumber(transferRequest.getFromAccount()).get();
        sourceCustomer.setBalance(sourceCustomer.getBalance().subtract(transferRequest.getAmount()));
        customerRepository.save(sourceCustomer);
        
        // Update destination balance
        Customer destinationCustomer = customerRepository.findByAccountNumber(transferRequest.getToAccount()).get();
        destinationCustomer.setBalance(destinationCustomer.getBalance().add(transferRequest.getAmount()));
        customerRepository.save(destinationCustomer);
    }
}
