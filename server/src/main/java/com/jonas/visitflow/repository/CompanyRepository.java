package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUserId(String userId);
    List<Company> findAllByUserId(String userId);
    Optional<Company> findByIdAndUserId(Long companyId, String userId);
    boolean existsByDomain(String domain);
}
