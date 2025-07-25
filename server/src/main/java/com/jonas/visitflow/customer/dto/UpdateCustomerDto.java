package com.jonas.visitflow.customer.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerDto {

    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;

    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

}
