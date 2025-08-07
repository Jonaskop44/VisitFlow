package com.jonas.visitflow.order.dto;

import com.jonas.visitflow.model.Order;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TrimmedOrderDto {
    private LocalDateTime requestedDateTime;

    public static TrimmedOrderDto fromEntity(Order order) {
        return TrimmedOrderDto.builder()
                .requestedDateTime(order.getRequestedDateTime())
                .build();
    }
}
