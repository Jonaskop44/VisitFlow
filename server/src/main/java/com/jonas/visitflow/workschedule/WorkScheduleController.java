package com.jonas.visitflow.workschedule;

import com.jonas.visitflow.workschedule.dto.CreateWorkScheduleDto;
import com.jonas.visitflow.workschedule.dto.WorkScheduleDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/work-schedule")
@RequiredArgsConstructor
public class WorkScheduleController {

    private final WorkScheduleService workScheduleService;

    @PostMapping("/{companyId}/create")
    public ResponseEntity<WorkScheduleDto> createWorkSchedule(
            @PathVariable UUID companyId,
            @RequestBody @Valid CreateWorkScheduleDto workScheduleDto,
            Principal principal
    ) {
        String userId = principal.getName();
        WorkScheduleDto createdWorkSchedule = workScheduleService.createWorkSchedule(workScheduleDto, companyId, userId);
        return ResponseEntity.ok(createdWorkSchedule);
    }

    @GetMapping("/{companyId}/all")
    public ResponseEntity<List<WorkScheduleDto>> getAllWorkSchedules(
            @PathVariable UUID companyId,
            Principal principal
    ) {
        String userId = principal.getName();
        List<WorkScheduleDto> workSchedules = workScheduleService.getAllWorkSchedules(companyId, userId);
        return ResponseEntity.ok(workSchedules);
    }

    @PatchMapping("/{workScheduleId}/update")
    public ResponseEntity<WorkScheduleDto> updateWorkSchedule(
            @PathVariable Long workScheduleId,
            @RequestBody @Valid CreateWorkScheduleDto workScheduleDto,
            Principal principal
    ) {
        String userId = principal.getName();
        WorkScheduleDto updatedWorkSchedule = workScheduleService.updateWorkSchedule(workScheduleId, workScheduleDto, userId);
        return ResponseEntity.ok(updatedWorkSchedule);
    }

    @DeleteMapping("/{workScheduleId}/delete")
    public ResponseEntity<WorkScheduleDto> deleteWorkSchedule(
            @PathVariable Long workScheduleId,
            Principal principal
    ) {
        String userId = principal.getName();
        WorkScheduleDto deletedWorkSchedule = workScheduleService.deleteWorkSchedule(workScheduleId, userId);
        return ResponseEntity.ok(deletedWorkSchedule);
    }


}
