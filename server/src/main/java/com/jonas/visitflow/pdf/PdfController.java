package com.jonas.visitflow.pdf;

import com.jonas.visitflow.pdf.dto.PdfDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfService pdfService;

    @PostMapping("/{invoiceId}/upload")
    public ResponseEntity<PdfDto> uploadPdf(@RequestParam("file") MultipartFile file,
                                            @PathVariable Long invoiceId,
                                            Principal principal) {
        String userId = principal.getName();
        PdfDto pdfDto = pdfService.uploadPdf(file, invoiceId, userId);
        return ResponseEntity.ok(pdfDto);
    }

    @GetMapping("/{token}/download")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String token) {
        Resource resource = pdfService.downloadPdf(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
