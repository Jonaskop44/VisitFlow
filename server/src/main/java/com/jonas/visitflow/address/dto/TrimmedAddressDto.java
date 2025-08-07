package com.jonas.visitflow.address.dto;

import com.jonas.visitflow.model.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrimmedAddressDto {
    private String street;
    private String city;
    private int postalCode;
    private String country;

    public static TrimmedAddressDto fromEntity(Address address) {
        return TrimmedAddressDto.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }
}
