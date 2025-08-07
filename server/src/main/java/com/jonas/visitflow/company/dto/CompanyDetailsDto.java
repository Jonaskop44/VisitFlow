package com.jonas.visitflow.company.dto;

import com.jonas.visitflow.address.dto.TrimmedAddressDto;
import com.jonas.visitflow.model.*;
import com.jonas.visitflow.order.dto.TrimmedOrderDto;
import com.jonas.visitflow.product.dto.TrimmedProductDto;
import com.jonas.visitflow.vacationday.dto.TrimmedVacationDayDto;
import com.jonas.visitflow.workschedule.dto.TrimmedWorkScheduleDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class CompanyDetailsDto {
    private UUID id;
    private String name;
    private String description;
    private String domain;
    private TrimmedAddressDto address;
    private List<TrimmedProductDto> products;
    private List<TrimmedWorkScheduleDto> workSchedules;
    private List<TrimmedVacationDayDto> vacationDays;
    private List<TrimmedOrderDto> orders;

    public static CompanyDetailsDto fromEntity(Company company) {
        return CompanyDetailsDto.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .domain(company.getDomain())
                .address(company.getAddress() == null ? null : TrimmedAddressDto.fromEntity(company.getAddress()))
                .products(company.getProducts() == null ? null :
                        company.getProducts().stream()
                                .map(TrimmedProductDto::fromEntity)
                                .collect(Collectors.toList()))
                .workSchedules(company.getWorkSchedules() == null ? null :
                        company.getWorkSchedules().stream()
                                .map(TrimmedWorkScheduleDto::fromEntity)
                                .collect(Collectors.toList()))
                .vacationDays(company.getVacationDays() == null ? null :
                        company.getVacationDays().stream()
                                .map(TrimmedVacationDayDto::fromEntity)
                                .collect(Collectors.toList()))
                .orders(company.getOrders() == null ? null :
                        company.getOrders().stream()
                                .map(TrimmedOrderDto::fromEntity)
                                .collect(Collectors.toList()))
                .build();
    }
}
