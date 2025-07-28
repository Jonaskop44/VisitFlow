package com.jonas.visitflow.order.dto;

import com.jonas.visitflow.address.dto.AddressDto;
import com.jonas.visitflow.model.Order;
import com.jonas.visitflow.model.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderDto {
    private Long id;
    private LocalDateTime requestedDateTime;
    private String note;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID companyId;
    private Long customerId;
    private AddressDto address;
    private Long productId;

    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .requestedDateTime(order.getRequestedDateTime())
                .note(order.getNote())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .companyId(order.getCompany().getId())
                .customerId(order.getCustomer().getId())
                .address(AddressDto.fromEntity(order.getAddress()))
                .productId(order.getProduct().getId())
                .build();
    }
}
