package com.jonas.visitflow.statistics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthlyOrdersRevenueDto {
    private Integer year;
    private Integer month;
    private Integer totalOrders;
    private Double totalRevenue;
}
