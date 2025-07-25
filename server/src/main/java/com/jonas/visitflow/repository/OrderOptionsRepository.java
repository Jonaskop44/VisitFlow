package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Customer;
import com.jonas.visitflow.model.OrderOptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOptionsRepository extends JpaRepository<OrderOptions, Long> {
}
