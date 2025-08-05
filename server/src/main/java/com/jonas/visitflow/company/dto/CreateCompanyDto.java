package com.jonas.visitflow.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Postal code is required")
    private Integer postalCode;

    @NotBlank(message = "Country is required")
    private String country;

}
