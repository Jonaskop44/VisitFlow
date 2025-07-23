package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
