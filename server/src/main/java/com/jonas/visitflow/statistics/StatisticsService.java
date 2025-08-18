package com.jonas.visitflow.statistics;

import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.Order;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.CustomerRepository;
import com.jonas.visitflow.repository.OrderRepository;
import com.jonas.visitflow.statistics.dto.MonthlyOrdersRevenueDto;
import com.jonas.visitflow.statistics.dto.SummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final OrderRepository orderRepository;

    public SummaryDto getSummary(String userId, UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if(!company.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to access this company's statistics");
        }

        Long totalCustomers = customerRepository.countByCompany(company);
        Long totalOrders = orderRepository.countByCompany(company);

        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);

        Long currentMonthCustomers = customerRepository.countByCompanyAndCreatedAtBetween(
                company,
                currentMonth.atDay(1).atStartOfDay(),
                currentMonth.atEndOfMonth().atTime(23, 59, 59)
        );

        Long lastMonthCustomers = customerRepository.countByCompanyAndCreatedAtBetween(
                company,
                lastMonth.atDay(1).atStartOfDay(),
                lastMonth.atEndOfMonth().atTime(23, 59, 59)
        );

        Long currentMonthOrders = orderRepository.countByCompanyAndCreatedAtBetween(
                company,
                currentMonth.atDay(1).atStartOfDay(),
                currentMonth.atEndOfMonth().atTime(23, 59, 59)
        );

        Long lastMonthOrders = orderRepository.countByCompanyAndCreatedAtBetween(
                company,
                lastMonth.atDay(1).atStartOfDay(),
                lastMonth.atEndOfMonth().atTime(23, 59, 59)
        );

        return SummaryDto.builder()
                .totalCustomers(totalCustomers)
                .customerGrowthRate(calcGrowth(currentMonthCustomers, lastMonthCustomers))
                .totalOrders(totalOrders)
                .orderGrowthRate(calcGrowth(currentMonthOrders, lastMonthOrders))
                .build();
    }

    public List<MonthlyOrdersRevenueDto> getMonthlyOrdersRevenue(UUID companyId, String userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if(!company.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to access this company's statistics");
        }

        int currentYear = Year.now().getValue();

        List<Order> orders = orderRepository.findAllByCompanyAndYear(company, currentYear);

        Map<YearMonth, List<Order>> grouped = orders.stream()
                .collect(Collectors.groupingBy(o -> YearMonth.from(o.getCreatedAt())));

        List<MonthlyOrdersRevenueDto> result = new ArrayList<>();
        for(int month = 1; month <= 12; month++) {
            YearMonth ym = YearMonth.of(currentYear, month);
            List<Order> monthOrders = grouped.getOrDefault(ym, Collections.emptyList());

            double revenue = monthOrders.stream()
                    .mapToDouble(o -> o.getProduct().getPrice().doubleValue())
                    .sum();

            result.add(MonthlyOrdersRevenueDto.builder()
                    .year(currentYear)
                    .month(month)
                    .totalOrders(monthOrders.size())
                    .totalRevenue(revenue)
                    .build());
        }
        return result;
    }

    private double calcGrowth(long current, long last) {
        if (last == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        double growth = ((double) (current - last) / last) * 100.0;
        return BigDecimal.valueOf(growth)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
