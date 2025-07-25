package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    List<Company> findAllByUserId(String userId);
    Optional<Company> findByIdAndUserId(UUID companyId, String userId);
    boolean existsByDomain(String domain);
    Optional<Company> findByUserId(String userId);
}
