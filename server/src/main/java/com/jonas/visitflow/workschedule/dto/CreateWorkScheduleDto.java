package com.jonas.visitflow.workschedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
public class CreateWorkScheduleDto {

    @NotNull(message = "Day of week cannot be blank")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Max orders per day cannot be blank")
    private Integer maxOrdersPerDay;

    @NotNull(message = "Min minutes between orders cannot be blank")
    private Integer minMinutesBetweenOrders;

    @NotNull(message = "Start time cannot be blank")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be blank")
    private LocalTime endTime;
}
