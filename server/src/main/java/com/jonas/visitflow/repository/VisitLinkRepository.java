package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.VisitLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitLinkRepository extends JpaRepository<VisitLink, Long> {
    Optional<VisitLink> findByToken(String token);
}