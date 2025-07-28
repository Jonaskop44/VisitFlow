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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        Invoice invoice = invoiceRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("PDF not found"));

        if(invoice.getStatus() != InvoiceStatus.PAID) {
            throw new UnauthorizedException("The invoice is not paid, download not allowed");
        }

        List<PdfDocument> pdfDocuments = invoice.getPdfDocuments();
        if(pdfDocuments.isEmpty()) {
            throw new NotFoundException("No PDF documents found for this invoice");
        }

        try {
            if(pdfDocuments.size() == 1) {
                PdfDocument pdfDocument = pdfDocuments.getFirst();
                Path filePath = Paths.get(storageDirectory).resolve(pdfDocument.getFileName());
                Resource resource = new UrlResource(filePath.toUri());
                if (!resource.exists() || !resource.isReadable()) {
                    throw new NotFoundException("PDF file not found or not readable");
                }
                return resource;
            } else {
                String zipFileName = "invoice_" + invoice.getId() + ".zip";
                Path zipPath = Paths.get(storageDirectory).resolve(zipFileName);

                try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                    for (PdfDocument pdf : pdfDocuments) {
                        Path filePath = Paths.get(storageDirectory).resolve(pdf.getFileName());
                        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                            continue; // Skip if file does not exist or is not readable
                        }

                        zipOut.putNextEntry(new ZipEntry(pdf.getFileName()));
                        Files.copy(filePath, zipOut);
                        zipOut.closeEntry();
                    }
                }

                Resource zipResource = new UrlResource(zipPath.toUri());
                if (!zipResource.exists() || !zipResource.isReadable()) {
                    throw new NotFoundException("ZIP file not created or not readable");
                }

                return zipResource;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load PDF file: " + e.getMessage());
        }

    }

}
