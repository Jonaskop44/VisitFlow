package com.jonas.visitflow.product.dto;

import com.jonas.visitflow.model.Product;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TrimmedProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long duration;

    public static TrimmedProductDto fromEntity(Product product) {
        return TrimmedProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .duration(product.getDuration())
                .build();
    }
}
