package com.jonas.visitflow.pdf;

import com.jonas.visitflow.pdf.dto.PdfDto;
import com.jonas.visitflow.repository.PdfRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final PdfRepository pdfRepository;


}
