package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.VacationDay;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VacationDayRepository extends JpaRepository<VacationDay, Long> {
    List<VacationDay> findAllByCompany(Company company);

    boolean existsByDateAndCompany(LocalDate data, Company company);
}
