package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.Order;
import com.jonas.visitflow.statistics.dto.MonthlyOrdersRevenueDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCompanyIdAndRequestedDateTimeBetween(UUID companyId, LocalDateTime start, LocalDateTime end);
    List<Order> findByCompanyId(UUID id);
    long countByCompanyAndRequestedDateTimeBetween(Company company, LocalDateTime requestedDateTimeAfter, LocalDateTime requestedDateTimeBefore);
    List<Order> findAllByCompanyAndRequestedDateTimeBetween(Company company, LocalDateTime localDateTime, LocalDateTime localDateTime1);
    Long countByCompany(Company company);
    Long countByCompanyAndCreatedAtBetween(Company company, LocalDateTime localDateTime, LocalDateTime localDateTime1);
    List<Order> findAllByCompanyAndCreatedAtBetweenOrderByCreatedAtAsc(Company company, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);
    default List<Order> findAllByCompanyAndYear(Company company, int year) {
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        return findAllByCompanyAndCreatedAtBetweenOrderByCreatedAtAsc(company, start, end);
    }
}