package com.jonas.visitflow.workschedule;

import com.jonas.visitflow.exception.AlreadyExistsException;
import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.WorkSchedule;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.WorkScheduleRepository;
import com.jonas.visitflow.workschedule.dto.CreateWorkScheduleDto;
import com.jonas.visitflow.workschedule.dto.WorkScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;
    private final CompanyRepository companyRepository;

    public WorkScheduleDto createWorkSchedule(CreateWorkScheduleDto workScheduleDto, UUID companyId, String userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if(!company.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to create a work schedule for this company");
        }

        //Check if a work schedule already exists for the same day of the week
        if (workScheduleRepository.existsByCompanyAndDayOfWeek(company, workScheduleDto.getDayOfWeek())) {
            throw new AlreadyExistsException("A work schedule already exists for this day of the week");
        }

        WorkSchedule workSchedule = WorkSchedule.builder()
                .company(company)
                .startTime(workScheduleDto.getStartTime())
                .endTime(workScheduleDto.getEndTime())
                .dayOfWeek(workScheduleDto.getDayOfWeek())
                .maxOrdersPerDay(workScheduleDto.getMaxOrdersPerDay())
                .minMinutesBetweenOrders(workScheduleDto.getMinMinutesBetweenOrders())
                .build();

        workSchedule = workScheduleRepository.save(workSchedule);
        return WorkScheduleDto.fromEntity(workSchedule);
    }

    public List<WorkScheduleDto> getAllWorkSchedules(UUID companyId, String userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if(!company.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view work schedules for this company");
        }

        List<WorkSchedule> workSchedules = workScheduleRepository.findAllByCompany(company);

        if (workSchedules.isEmpty()) {
            throw new NotFoundException("No work schedules found for this company");
        }

        return workSchedules.stream()
                .map(WorkScheduleDto::fromEntity)
                .toList();
    }

    public WorkScheduleDto updateWorkSchedule(Long workScheduleId, CreateWorkScheduleDto workScheduleDto, String userId) {
        WorkSchedule workSchedule = workScheduleRepository.findById(workScheduleId)
                .orElseThrow(() -> new NotFoundException("Work schedule not found"));

        if(!workSchedule.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this work schedule");
        }

        boolean existsForSameDay = workScheduleRepository.existsByCompanyAndDayOfWeek(workSchedule.getCompany(), workSchedule.getDayOfWeek());

        if(existsForSameDay && !workSchedule.getDayOfWeek().equals(workScheduleDto.getDayOfWeek())) {
            throw new AlreadyExistsException("A work schedule already exists for this day of the week");
        }

        workSchedule.setStartTime(workScheduleDto.getStartTime());
        workSchedule.setEndTime(workScheduleDto.getEndTime());
        workSchedule.setDayOfWeek(workScheduleDto.getDayOfWeek());
        workSchedule.setMaxOrdersPerDay(workScheduleDto.getMaxOrdersPerDay());
        workSchedule.setMinMinutesBetweenOrders(workScheduleDto.getMinMinutesBetweenOrders());

        workSchedule = workScheduleRepository.save(workSchedule);
        return WorkScheduleDto.fromEntity(workSchedule);
    }

    public WorkScheduleDto deleteWorkSchedule(Long workScheduleId, String userId) {
        WorkSchedule workSchedule = workScheduleRepository.findById(workScheduleId)
                .orElseThrow(() -> new NotFoundException("Work schedule not found"));

        if(!workSchedule.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this work schedule");
        }

        workScheduleRepository.delete(workSchedule);
        return WorkScheduleDto.fromEntity(workSchedule);
    }

}
