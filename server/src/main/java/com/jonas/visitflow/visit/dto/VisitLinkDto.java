package com.jonas.visitflow.visit.dto;

import com.jonas.visitflow.model.VisitLink;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VisitLinkDto {
    private Long id;
    private String token;
    private String userId;
    private String expiresAt;
    private String createdAt;
    private String updatedAt;
    private Long companyId;

    public static VisitLinkDto fromEntity(VisitLink visitLink) {
        return VisitLinkDto.builder()
                .id(visitLink.getId())
                .token(visitLink.getToken())
                .userId(visitLink.getUserId())
                .expiresAt(visitLink.getExpiresAt().toString())
                .createdAt(visitLink.getCreatedAt().toString())
                .updatedAt(visitLink.getUpdatedAt().toString())
                .companyId(visitLink.getCompany().getId())
                .build();
    }

}
