package com.jonas.visitflow.customer;

import com.jonas.visitflow.customer.dto.UpdateCustomerDto;
import com.jonas.visitflow.customer.dto.CustomerDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers(Principal principal) {
        String userId = principal.getName();
        List<CustomerDto> customers = customerService.getAllCustomers(userId);
        return ResponseEntity.ok(customers);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id,
                                                      @RequestBody @Valid UpdateCustomerDto customerDto,
                                                      Principal principal) {
        String userId = principal.getName();
        CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDto, userId);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable Long id, Principal principal) {
        String userId = principal.getName();
        CustomerDto deletedCustomer = customerService.deleteCustomer(id, userId);
        return ResponseEntity.ok(deletedCustomer);
    }

}
