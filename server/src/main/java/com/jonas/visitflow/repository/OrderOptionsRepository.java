package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.OrderOptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderOptionsRepository extends JpaRepository<OrderOptions, Long> {
    List<OrderOptions> findByCompanyId(UUID companyId);
    List<OrderOptions> findAllByCompany(Company company);
}
