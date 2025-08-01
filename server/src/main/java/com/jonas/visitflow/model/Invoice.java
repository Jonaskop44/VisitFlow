package com.jonas.visitflow.model;

import com.jonas.visitflow.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "invoices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String stripePaymentId;

    @Column(nullable = true)
    private String stripeSessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    @Column(nullable = true)
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private String token;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PdfDocument> pdfDocuments;

    @PrePersist
    public void prePersist() {
        this.token = UUID.randomUUID().toString();
    }

}
