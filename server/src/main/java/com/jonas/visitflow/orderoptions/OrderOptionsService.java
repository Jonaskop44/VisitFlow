package com.jonas.visitflow.orderoptions;

import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.OrderOptions;
import com.jonas.visitflow.orderoptions.dto.CreateOrderOptionsDto;
import com.jonas.visitflow.orderoptions.dto.OrderOptionsDto;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.OrderOptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderOptionsService {

    private final OrderOptionsRepository orderOptionsRepository;
    private final CompanyRepository companyRepository;


    public List<OrderOptionsDto> getAllOrderOptionsByCompany(UUID companyId, String userId) {
        Company company = companyRepository.findByIdAndUserId(companyId, userId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        List<OrderOptions> orderOptionsList = orderOptionsRepository.findAllByCompany(company);

        return orderOptionsList.stream()
                .map(OrderOptionsDto::fromEntity)
                .toList();
    }

    public OrderOptionsDto createOrderOption(CreateOrderOptionsDto createOrderOptionsDto, UUID companyId, String userId) {
        Company company = companyRepository.findByIdAndUserId(companyId, userId).
                orElseThrow(() -> new NotFoundException("Company not found"));

        OrderOptions orderOptions = OrderOptions.builder()
                .name(createOrderOptionsDto.getName())
                .duration(createOrderOptionsDto.getDuration())
                .price(createOrderOptionsDto.getPrice())
                .company(company)
                .build();

        orderOptions = orderOptionsRepository.save(orderOptions);
        return OrderOptionsDto.fromEntity(orderOptions);
    }

    public OrderOptionsDto updateOrderOption(CreateOrderOptionsDto createOrderOptionsDto, Long orderOptionId, String userId) {
        OrderOptions orderOptions = orderOptionsRepository.findById(orderOptionId).
                orElseThrow(() -> new NotFoundException("Order option not found"));

        if(!orderOptions.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to update this order option");
        }

        orderOptions.setName(createOrderOptionsDto.getName());
        orderOptions.setDuration(createOrderOptionsDto.getDuration());
        orderOptions.setPrice(createOrderOptionsDto.getPrice());

        orderOptions = orderOptionsRepository.save(orderOptions);
        return OrderOptionsDto.fromEntity(orderOptions);
    }

    public OrderOptionsDto deleteOrderOption(Long orderOptionId, String userId) {
        OrderOptions orderOptions = orderOptionsRepository.findById(orderOptionId)
                .orElseThrow(() -> new NotFoundException("Order option not found"));

        if(!orderOptions.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to update this order option");
        }

        orderOptionsRepository.delete(orderOptions);
        return OrderOptionsDto.fromEntity(orderOptions);
    }



}
