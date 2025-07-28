package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByOrder(Order order);
    List<Invoice> findAllByOrderCompany(Company company);
    Optional<Invoice> findByToken(String token);
}
