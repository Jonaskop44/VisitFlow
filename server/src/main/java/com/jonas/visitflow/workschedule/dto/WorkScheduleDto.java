package com.jonas.visitflow.workschedule.dto;

import com.jonas.visitflow.model.WorkSchedule;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class WorkScheduleDto {
    private Long id;
    private DayOfWeek dayOfWeek;
    private Integer maxOrdersPerDay;
    private Integer minMinutesBetweenOrders;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID companyId;

    public static WorkScheduleDto fromEntity(WorkSchedule workSchedule) {
        return WorkScheduleDto.builder()
                .id(workSchedule.getId())
                .dayOfWeek(workSchedule.getDayOfWeek())
                .maxOrdersPerDay(workSchedule.getMaxOrdersPerDay())
                .minMinutesBetweenOrders(workSchedule.getMinMinutesBetweenOrders())
                .startTime(workSchedule.getStartTime())
                .endTime(workSchedule.getEndTime())
                .createdAt(workSchedule.getCreatedAt())
                .updatedAt(workSchedule.getUpdatedAt())
                .companyId(workSchedule.getCompany().getId())
                .build();
    }
}
