package com.jonas.visitflow.vacationday;

import com.jonas.visitflow.vacationday.dto.CreateVacationDayDto;
import com.jonas.visitflow.vacationday.dto.VacationDayDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vacation-day")
@RequiredArgsConstructor
public class VacationDayController {

    private final VacationDayService vacationDayService;

    @PostMapping("/{companyId}/create")
    public ResponseEntity<VacationDayDto> createVacationDay(
            @PathVariable UUID companyId,
            @RequestBody @Valid CreateVacationDayDto dto,
            Principal principal
    ) {
        String userId = principal.getName();
        VacationDayDto vacationDayDto = vacationDayService.createVacationDay(dto, companyId, userId);
        return ResponseEntity.ok(vacationDayDto);
    }

    @GetMapping("/{companyId}/all")
    public ResponseEntity<List<VacationDayDto>> getAllVacationDays(
            @PathVariable UUID companyId,
            Principal principal
    ) {
        String userId = principal.getName();
        List<VacationDayDto> vacationDays = vacationDayService.getAllVacationDays(companyId, userId);
        return ResponseEntity.ok(vacationDays);
    }

    @PatchMapping("/{vacationDayId}/update")
    public ResponseEntity<VacationDayDto> updateVacationDay(
            @PathVariable Long vacationDayId,
            @RequestBody @Valid CreateVacationDayDto dto,
            Principal principal
    ) {
        String userId = principal.getName();
        VacationDayDto updatedVacationDay = vacationDayService.updateVacationDay(vacationDayId, dto, userId);
        return ResponseEntity.ok(updatedVacationDay);
    }

    @DeleteMapping("/{vacationDayId}/delete")
    public ResponseEntity<VacationDayDto> deleteVacationDay(
            @PathVariable Long vacationDayId,
            Principal principal
    ) {
        String userId = principal.getName();
        VacationDayDto vacationDayDto = vacationDayService.deleteVacationDay(vacationDayId, userId);
        return ResponseEntity.ok(vacationDayDto);
    }

}
