package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByCompanyIdAndRequestedDateTimeBetween(Long companyId, LocalDateTime start, LocalDateTime end);

    List<Visit> findByCompanyId(Long id);
}