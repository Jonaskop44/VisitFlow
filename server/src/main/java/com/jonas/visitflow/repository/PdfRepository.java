package com.jonas.visitflow.repository;

import com.jonas.visitflow.model.PdfDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdfRepository extends JpaRepository<PdfDocument, Long> {
}
