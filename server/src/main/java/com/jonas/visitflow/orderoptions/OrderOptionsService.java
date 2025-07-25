package com.jonas.visitflow.orderoptions;

import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.OrderOptions;
import com.jonas.visitflow.orderoptions.dto.CreateOrderOptionsDto;
import com.jonas.visitflow.orderoptions.dto.OrderOptionsDto;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.OrderOptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderOptionsService {

    private final OrderOptionsRepository orderOptionsRepository;
    private final CompanyRepository companyRepository;

    public OrderOptionsDto createOrderOption(CreateOrderOptionsDto createOrderOptionsDto, UUID companyId, String userId) {
        Company company = companyRepository.findByIdAndUserId(companyId, userId).
                orElseThrow(() -> new RuntimeException("Company not found"));

        OrderOptions orderOptions = OrderOptions.builder()
                .name(createOrderOptionsDto.getName())
                .duration(createOrderOptionsDto.getDuration())
                .price(createOrderOptionsDto.getPrice())
                .company(company)
                .build();

        orderOptions = orderOptionsRepository.save(orderOptions);
        return OrderOptionsDto.fromEntity(orderOptions);
    }

}
