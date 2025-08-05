package com.jonas.visitflow.address.dto;

import com.jonas.visitflow.model.Address;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddressDto {
    private Long id;
    private String street;
    private String city;
    private Integer postalCode;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AddressDto fromEntity(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }

}
