package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByCompany(Company company);
}