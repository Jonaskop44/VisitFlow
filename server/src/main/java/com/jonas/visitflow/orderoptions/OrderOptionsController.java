package com.jonas.visitflow.orderoptions;

import com.jonas.visitflow.model.OrderOptions;
import com.jonas.visitflow.orderoptions.dto.CreateOrderOptionsDto;
import com.jonas.visitflow.orderoptions.dto.OrderOptionsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/order-options")
@RequiredArgsConstructor
public class OrderOptionsController {

    private final OrderOptionsService orderOptionsService;

    @PostMapping("/{companyId}/create")
    public ResponseEntity<OrderOptionsDto> createOrderOption(@PathVariable UUID companyId,
                                                             @RequestBody @Valid CreateOrderOptionsDto createOrderOptionsDto,
                                                             Principal principal) {
        String userId = principal.getName();
        OrderOptionsDto orderOptionsDto = orderOptionsService.createOrderOption(createOrderOptionsDto, companyId, userId);
        return ResponseEntity.ok(orderOptionsDto);
    }

}
