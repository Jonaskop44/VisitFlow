package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
    List<WorkSchedule> findAllByCompany(Company company);

    boolean existsByCompanyAndDayOfWeek(Company company, DayOfWeek dayOfWeek);
}
