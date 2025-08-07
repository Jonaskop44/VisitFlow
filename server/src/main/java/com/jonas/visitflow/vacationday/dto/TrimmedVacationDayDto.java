package com.jonas.visitflow.vacationday.dto;

import com.jonas.visitflow.model.VacationDay;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TrimmedVacationDayDto {
    private LocalDate date;

    public static TrimmedVacationDayDto fromEntity(VacationDay day) {
        return TrimmedVacationDayDto.builder()
                .date(day.getDate())
                .build();
    }
}
