package com.jonas.visitflow.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateOrderDto {

    //Customer information
    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;

    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

   // Order information
   @NotNull(message = "Requested date and time is required")
   private LocalDateTime requestedDateTime;

   @NotNull(message = "Order option is required")
   private Long productId;

   private String note;

    // Address information
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Postal code is required")
    private Integer postalCode;

    @NotBlank(message = "Country is required")
    private String country;
}
