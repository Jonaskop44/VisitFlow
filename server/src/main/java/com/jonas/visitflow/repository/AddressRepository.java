package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}