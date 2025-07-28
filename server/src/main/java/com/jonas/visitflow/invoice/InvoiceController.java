package com.jonas.visitflow.invoice;

import com.jonas.visitflow.invoice.dto.InvoiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private  final InvoiceService invoiceService;

    @PostMapping("/{orderId}/create")
    public ResponseEntity<InvoiceDto> createInvoice(@PathVariable Long orderId,
                                                    Principal principal) {
        String userId = principal.getName();
        InvoiceDto invoiceDto = invoiceService.createInvoice(orderId, userId);
        return ResponseEntity.ok(invoiceDto);
    }

    @GetMapping("/{companyId}/all")
    public ResponseEntity<List<InvoiceDto>> getAllInvoicesByCompanyId(@PathVariable UUID companyId,
                                                                      Principal principal) {
        String userId = principal.getName();
        List<InvoiceDto> invoices = invoiceService.getAllInvoicesByCompanyId(companyId, userId);
        return ResponseEntity.ok(invoices);
    }

}
