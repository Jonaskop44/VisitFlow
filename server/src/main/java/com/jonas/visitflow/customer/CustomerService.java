package com.jonas.visitflow.customer;


import com.jonas.visitflow.customer.dto.UpdateCustomerDto;
import com.jonas.visitflow.customer.dto.CustomerDto;
import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.Customer;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public List<CustomerDto> getAllCustomers(String userId) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Company not found for user " + userId));

        List<Customer> customers = customerRepository.findAllByCompany(company);

        return customers.stream()
                .map(CustomerDto::fromEntity)
                .toList();
    }

    public CustomerDto updateCustomer(Long id, UpdateCustomerDto customerDto, String userId) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        if(!customer.getCompany().getUserId().equals(userId)) {
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

        if(!customer.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to update this customer");
        }

        customerRepository.delete(customer);
        return CustomerDto.fromEntity(customer);
    }

}
