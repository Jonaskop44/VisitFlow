package com.jonas.visitflow.orderoptions.dto;

import com.jonas.visitflow.model.OrderOptions;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderOptionsDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long duration;
    private UUID companyId;

    public static OrderOptionsDto fromEntity(OrderOptions orderOptions) {
        return OrderOptionsDto.builder()
                .id(orderOptions.getId())
                .name(orderOptions.getName())
                .price(orderOptions.getPrice())
                .duration(orderOptions.getDuration())
                .companyId(orderOptions.getCompany().getId())
                .build();
    }

}
