package com.jonas.visitflow.vacationday.dto;

import com.jonas.visitflow.model.VacationDay;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class VacationDayDto {
    private Long id;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID companyId;

    public static VacationDayDto fromEntity(VacationDay vacationDay) {
        return VacationDayDto.builder()
                .id(vacationDay.getId())
                .date(vacationDay.getDate())
                .createdAt(vacationDay.getCreatedAt())
                .updatedAt(vacationDay.getUpdatedAt())
                .companyId(vacationDay.getCompany().getId())
                .build();
    }
}
