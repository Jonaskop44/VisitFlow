package com.jonas.visitflow.pdf.dto;

import com.jonas.visitflow.model.PdfDocument;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PdfDto {
    private Long id;
    private String fileName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long invoiceId;

    public static PdfDto fromEntity(PdfDocument pdfDocument) {
        return PdfDto.builder()
                .id(pdfDocument.getId())
                .fileName(pdfDocument.getFileName())
                .createdAt(pdfDocument.getCreatedAt())
                .updatedAt(pdfDocument.getUpdatedAt())
                .invoiceId(pdfDocument.getInvoice().getId())
                .build();
    }

}
