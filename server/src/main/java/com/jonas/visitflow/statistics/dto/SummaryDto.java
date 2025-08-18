package com.jonas.visitflow.statistics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummaryDto {
    private Long totalCustomers;
    private Double customerGrowthRate;
    private Long totalOrders;
    private Double orderGrowthRate;
}
