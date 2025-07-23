package com.jonas.visitflow.customer;


import com.jonas.visitflow.customer.dto.CreateCustomerDto;
import com.jonas.visitflow.customer.dto.CustomerDto;
import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.model.Customer;
import com.jonas.visitflow.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerDto> getAllCustomers(String userId) {
        return customerRepository.findAllByUserId(userId)
                .stream()
                .map(CustomerDto::fromEntity)
                .toList();
    }

    public CustomerDto createCustomer(@Valid CreateCustomerDto customerDto, String userId) {
        Customer customer = Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .email(customerDto.getEmail())
                .phoneNumber(customerDto.getPhoneNumber())
                .userId(userId)
                .build();

        return CustomerDto.fromEntity(customerRepository.save(customer));
    }

    public CustomerDto updateCustomer(Long id, CreateCustomerDto customerDto, String userId) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        if (!customer.getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to update this customer");
        }

        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());

        return CustomerDto.fromEntity(customerRepository.save(customer));
    }


    public CustomerDto deleteCustomer(Long id, String userId) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        if (!customer.getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to delete this customer");
        }

        customerRepository.delete(customer);
        return CustomerDto.fromEntity(customer);
    }

}
