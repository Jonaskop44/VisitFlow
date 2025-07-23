    package com.jonas.visitflow.model;

    import jakarta.persistence.*;
    import lombok.*;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.annotation.LastModifiedDate;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;

    import java.time.LocalDateTime;
    import java.util.UUID;

    @Entity
    @EntityListeners(AuditingEntityListener.class)
    @Table(name = "visit_links")
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public final class VisitLink {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String token;

        @Column(nullable = false)
        private String userId;

        @Column(nullable = false)
        private LocalDateTime expiresAt;

        @Column(nullable = false)
        private boolean used = false;

        @CreatedDate
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @LastModifiedDate
        @Column(nullable = false)
        private LocalDateTime updatedAt;

        @PrePersist
        public void prePersist() {
            this.expiresAt = this.createdAt.plusDays(1); // Set expiration to 1 day after creation
            this.token = UUID.randomUUID().toString();
        }

    }
