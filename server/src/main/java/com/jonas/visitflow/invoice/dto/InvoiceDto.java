package com.jonas.visitflow.invoice.dto;

import com.jonas.visitflow.model.Invoice;
import com.jonas.visitflow.model.enums.InvoiceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InvoiceDto {
    private Long id;
    private String stripePaymentId;
    private InvoiceStatus status;
    private LocalDateTime paidAt;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long orderId;

    public static InvoiceDto fromEntity(Invoice invoice) {
        return InvoiceDto.builder()
                .id(invoice.getId())
                .stripePaymentId(invoice.getStripePaymentId())
                .status(invoice.getStatus())
                .paidAt(invoice.getPaidAt())
                .token(invoice.getToken())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .orderId(invoice.getOrder().getId())
                .build();
    }

}
