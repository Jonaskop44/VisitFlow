package com.jonas.visitflow.vacationday.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateVacationDayDto {

    @NotNull(message = "Date cannot be empty")
    private LocalDate data;

}
