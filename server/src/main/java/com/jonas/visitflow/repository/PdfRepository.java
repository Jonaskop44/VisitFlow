package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.PdfDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PdfRepository extends JpaRepository<PdfDocument, Long> {
    Optional<PdfDocument> findByToken(String token);
}
