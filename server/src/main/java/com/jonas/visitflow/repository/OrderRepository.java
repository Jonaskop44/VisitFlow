package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCompanyIdAndRequestedDateTimeBetween(UUID companyId, LocalDateTime start, LocalDateTime end);
    List<Order> findByCompanyId(UUID id);
    long countByCompanyAndRequestedDateTimeBetween(Company company, LocalDateTime requestedDateTimeAfter, LocalDateTime requestedDateTimeBefore);
    List<Order> findAllByCompanyAndRequestedDateTimeBetween(Company company, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}