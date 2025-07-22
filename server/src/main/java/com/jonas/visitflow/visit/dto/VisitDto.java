package com.jonas.visitflow.visit.dto;

import com.jonas.visitflow.model.Visit;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VisitDto {
    private Long id;
    private Long customerId;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private LocalDateTime requestedDateTime;
    private String note;

    public static VisitDto fromEntity(Visit visit) {
        return VisitDto.builder()
                .id(visit.getId())
                .customerId(visit.getCustomer().getId())
                .street(visit.getAddress().getStreet())
                .city(visit.getAddress().getCity())
                .postalCode(visit.getAddress().getPostalCode())
                .country(visit.getAddress().getCountry())
                .requestedDateTime(visit.getRequestedDateTime())
                .note(visit.getNote())
                .build();
    }
}
