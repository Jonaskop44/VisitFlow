package com.jonas.visitflow.vacationday;

import com.jonas.visitflow.exception.AlreadyExistsException;
import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.VacationDay;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.VacationDayRepository;
import com.jonas.visitflow.vacationday.dto.CreateVacationDayDto;
import com.jonas.visitflow.vacationday.dto.VacationDayDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VacationDayService {

    private final VacationDayRepository vacationDayRepository;
    private final CompanyRepository companyRepository;

    public VacationDayDto createVacationDay(CreateVacationDayDto dto, UUID companyId, String userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if(!company.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to create a vacation day for this company");
        }

        //Check if the picked date is in the past
        if (dto.getData().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("The selected date cannot be in the past");
        }

        //Check if the picked date is already a vacation day
        if (vacationDayRepository.existsByDateAndCompany(dto.getData(), company)) {
            throw new AlreadyExistsException("The selected date is already a vacation day");
        }

        VacationDay vacationDay = VacationDay.builder()
                .date(dto.getData())
                .company(company)
                .build();

        company.setEnabled(true);
        companyRepository.save(company);

        vacationDay = vacationDayRepository.save(vacationDay);
        return VacationDayDto.fromEntity(vacationDay);
    }

    public List<VacationDayDto> getAllVacationDays(UUID companyId, String userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if(!company.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view vacation days for this company");
        }

        List<VacationDay> vacationDays = vacationDayRepository.findAllByCompany(company);

        if (vacationDays.isEmpty()) {
            throw new NotFoundException("No vacation days found for the company");
        }

        return vacationDays.stream()
                .map(VacationDayDto::fromEntity)
                .toList();
    }

    public VacationDayDto updateVacationDay(Long id, CreateVacationDayDto dto, String userId) {
        VacationDay vacationDay = vacationDayRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vacation day not found"));

        if(!vacationDay.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this vacation day");
        }

        //Check if the picked date is in the past
        if (dto.getData().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("The selected date cannot be in the past");
        }

        //Check if the picked date is already a vacation day
        if (vacationDayRepository.existsByDateAndCompany(dto.getData(), vacationDay.getCompany())) {
            throw new AlreadyExistsException("The selected date is already a vacation day");
        }

        vacationDay.setDate(dto.getData());

        vacationDay = vacationDayRepository.save(vacationDay);
        return VacationDayDto.fromEntity(vacationDay);
    }

    public VacationDayDto deleteVacationDay(Long id, String userId) {
        VacationDay vacationDay = vacationDayRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vacation day not found"));

        if(!vacationDay.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this vacation day");
        }

        vacationDayRepository.delete(vacationDay);

        //Check if the company still has vacation days
        List<VacationDay> vacationDays = vacationDayRepository.findAllByCompany(vacationDay.getCompany());
        if (vacationDays.isEmpty()) {
            vacationDay.getCompany().setEnabled(false);
            companyRepository.save(vacationDay.getCompany());
        }

        return VacationDayDto.fromEntity(vacationDay);
    }

}
