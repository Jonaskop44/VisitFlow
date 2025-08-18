package com.jonas.visitflow.statistics;

import com.jonas.visitflow.statistics.dto.MonthlyOrdersRevenueDto;
import com.jonas.visitflow.statistics.dto.SummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/summary/{companyId}")
    public SummaryDto getSummary(@PathVariable UUID companyId, Principal principal) {
        String userId = principal.getName();
        return statisticsService.getSummary(userId, companyId);
    }

    @GetMapping("/orders-revenue/{companyId}")
    public List<MonthlyOrdersRevenueDto> getOrdersRevenue(@PathVariable UUID companyId, Principal principal) {
        String userId = principal.getName();
        return statisticsService.getMonthlyOrdersRevenue(companyId, userId);
    }

}
