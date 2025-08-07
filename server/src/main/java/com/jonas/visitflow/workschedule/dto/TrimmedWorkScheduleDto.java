package com.jonas.visitflow.workschedule.dto;

import com.jonas.visitflow.model.WorkSchedule;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
public class TrimmedWorkScheduleDto {
    private DayOfWeek dayOfWeek;
    private int maxOrdersPerDay;
    private int minMinutesBetweenOrders;
    private LocalTime startTime;
    private LocalTime endTime;

    public static TrimmedWorkScheduleDto fromEntity(WorkSchedule ws) {
        return TrimmedWorkScheduleDto.builder()
                .dayOfWeek(ws.getDayOfWeek())
                .maxOrdersPerDay(ws.getMaxOrdersPerDay())
                .minMinutesBetweenOrders(ws.getMinMinutesBetweenOrders())
                .startTime(ws.getStartTime())
                .endTime(ws.getEndTime())
                .build();
    }
}
