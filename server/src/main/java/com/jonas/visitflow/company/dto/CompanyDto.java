package com.jonas.visitflow.company.dto;

import com.jonas.visitflow.address.dto.AddressDto;
import com.jonas.visitflow.model.Company;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CompanyDto {
    private UUID id;
    private String name;
    private String description;
    private String domain;
    private boolean enabled;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AddressDto address;

    public static CompanyDto fromEntity(Company company) {
        return CompanyDto.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .domain(company.getDomain())
                .enabled(company.isEnabled())
                .userId(company.getUserId())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .address(AddressDto.fromEntity(company.getAddress()))
                .build();
    }

}
