package com.jonas.visitflow.company.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCompanyDto {

    // Company information
    @NotBlank(message = "Company name cannot be empty")
    private String name;

    private String description;
    private String domain;

    // Address information
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;

}
