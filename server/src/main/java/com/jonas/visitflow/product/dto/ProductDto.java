package com.jonas.visitflow.product.dto;

import com.jonas.visitflow.model.Product;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long duration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID companyId;

    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .duration(product.getDuration())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .companyId(product.getCompany().getId())
                .build();
    }

}
