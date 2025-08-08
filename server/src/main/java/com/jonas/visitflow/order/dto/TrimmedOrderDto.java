package com.jonas.visitflow.order.dto;

import com.jonas.visitflow.model.Order;
import com.jonas.visitflow.product.dto.TrimmedProductDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TrimmedOrderDto {
    private LocalDateTime requestedDateTime;
    private TrimmedProductDto product;

    public static TrimmedOrderDto fromEntity(Order order) {
        return TrimmedOrderDto.builder()
                .requestedDateTime(order.getRequestedDateTime())
                .product(TrimmedProductDto.fromEntity(order.getProduct()))
                .build();
    }
}
