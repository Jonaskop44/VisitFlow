package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {

}