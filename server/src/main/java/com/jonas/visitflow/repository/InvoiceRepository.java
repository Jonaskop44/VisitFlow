package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.Customer;
import com.jonas.visitflow.model.Invoice;
import com.jonas.visitflow.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByOrder(Order order);
    List<Invoice> findAllByOrderCompany(Company company);
}
