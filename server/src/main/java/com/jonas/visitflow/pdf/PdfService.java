package com.jonas.visitflow.pdf;

import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.model.Invoice;
import com.jonas.visitflow.model.PdfDocument;
import com.jonas.visitflow.model.enums.InvoiceStatus;
import com.jonas.visitflow.pdf.dto.PdfDto;
import com.jonas.visitflow.repository.InvoiceRepository;
import com.jonas.visitflow.repository.PdfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final PdfRepository pdfRepository;
    private final InvoiceRepository invoiceRepository;

    @Value("${storage.directory}")
    private String storageDirectory;

    public PdfDto uploadPdf(MultipartFile file, Long invoiceId, String userId) {
        if(file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".pdf")) {
            throw new IllegalArgumentException("Invalid file format");
        }

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("Invoice not found"));

        if(!invoice.getOrder().getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to upload a PDF for this invoice");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            Path path = Paths.get(storageDirectory).resolve(fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save PDF file: " + e.getMessage());
        }

        PdfDocument pdfDocument = PdfDocument.builder()
                .fileName(fileName)
                .invoice(invoice)
                .build();
        pdfDocument = pdfRepository.save(pdfDocument);
        return PdfDto.fromEntity(pdfDocument);
    }

    public Resource downloadPdf(String token) {
        PdfDocument pdfDocument = pdfRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("PDF not found"));

        if(pdfDocument.getInvoice().getStatus() != InvoiceStatus.PAID) {
            throw new UnauthorizedException("The invoice is not paid, download not allowed");
        }

        Path path = Paths.get(storageDirectory).resolve(pdfDocument.getFileName());

        try {
            Resource resource = new UrlResource(path.toUri());
            if(!resource.exists() || !resource.isReadable()) {
                throw new NotFoundException("PDF file not found or not readable");
            }
            return resource;
        } catch (IOException e) {
            throw new RuntimeException("Failed to download PDF file: " + e.getMessage());
        }

    }

}
