package com.jonas.visitflow.visit.dto;

import com.jonas.visitflow.address.dto.AddressDto;
import com.jonas.visitflow.model.Visit;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VisitDto {
    private Long id;
    private LocalDateTime requestedDateTime;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long customerId;
    private AddressDto address;
    private Long companyId;

    public static VisitDto fromEntity(Visit visit) {
        return VisitDto.builder()
                .id(visit.getId())
                .requestedDateTime(visit.getRequestedDateTime())
                .note(visit.getNote())
                .createdAt(visit.getCreatedAt())
                .updatedAt(visit.getUpdatedAt())
                .customerId(visit.getCustomer().getId())
                .address(AddressDto.fromEntity(visit.getAddress()))
                .companyId(visit.getCompany().getId())
                .build();
    }
}
