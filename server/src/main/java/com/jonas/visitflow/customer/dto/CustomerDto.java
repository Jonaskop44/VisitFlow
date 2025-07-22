package com.jonas.visitflow.customer.dto;

import com.jonas.visitflow.model.Customer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public static CustomerDto fromEntity(Customer customer) {

        return CustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }
}
