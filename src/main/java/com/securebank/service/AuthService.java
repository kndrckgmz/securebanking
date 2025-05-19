
package com.securebank.service;

import com.securebank.dto.AuthResponse;
import com.securebank.dto.LoginRequest;
import com.securebank.dto.RegisterRequest;
import com.securebank.dto.UserDto;
import com.securebank.exception.ResourceAlreadyExistsException;
import com.securebank.model.Admin;
import com.securebank.model.Customer;
import com.securebank.model.User;
import com.securebank.model.UserRole;
import com.securebank.repository.AdminRepository;
import com.securebank.repository.CustomerRepository;
import com.securebank.repository.UserRepository;
import com.securebank.security.JwtTokenProvider;
import com.securebank.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if user exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email is already in use");
        }
        
        User user;
        
        // Create user based on role
        if (registerRequest.getRole() == UserRole.CUSTOMER) {
            // Validate account number
            if (customerRepository.existsByAccountNumber(registerRequest.getAccountNumber())) {
                throw new ResourceAlreadyExistsException("Account number already exists");
            }
            
            Customer customer = new Customer();
            customer.setName(registerRequest.getName());
            customer.setEmail(registerRequest.getEmail());
            customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            customer.setPhone(registerRequest.getPhone());
            customer.setAddress(registerRequest.getAddress());
            customer.setRole(UserRole.CUSTOMER);
            customer.setAccountNumber(registerRequest.getAccountNumber());
            customer.setBalance(BigDecimal.ZERO);
            
            user = customerRepository.save(customer);
        } else if (registerRequest.getRole() == UserRole.ADMIN) {
            // Validate employee id
            if (adminRepository.existsByEmployeeId(registerRequest.getEmployeeId())) {
                throw new ResourceAlreadyExistsException("Employee ID already exists");
            }
            
            Admin admin = new Admin();
            admin.setName(registerRequest.getName());
            admin.setEmail(registerRequest.getEmail());
            admin.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            admin.setPhone(registerRequest.getPhone());
            admin.setAddress(registerRequest.getAddress());
            admin.setRole(UserRole.ADMIN);
            admin.setEmployeeId(registerRequest.getEmployeeId());
            admin.setDepartment(registerRequest.getDepartment());
            
            user = adminRepository.save(admin);
        } else {
            throw new IllegalArgumentException("Invalid user role");
        }
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = tokenProvider.generateToken(authentication);
        
        // Map user to DTO
        UserDto userDto = modelMapper.mapUserToDto(user);
        
        return new AuthResponse(jwt, userDto);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        // Authenticate with username (email) and password
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = tokenProvider.generateToken(authentication);
        
        // Get user information
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        
        if (userOptional.isPresent()) {
            UserDto userDto = modelMapper.mapUserToDto(userOptional.get());
            return new AuthResponse(jwt, userDto);
        }
        
        return new AuthResponse(jwt, null);
    }
}
