package com.jonas.visitflow.orderoptions;

import com.jonas.visitflow.model.OrderOptions;
import com.jonas.visitflow.orderoptions.dto.CreateOrderOptionsDto;
import com.jonas.visitflow.orderoptions.dto.OrderOptionsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-options")
@RequiredArgsConstructor
public class OrderOptionsController {

    private final OrderOptionsService orderOptionsService;

    @GetMapping("/{companyId}")
    public ResponseEntity<List<OrderOptionsDto>> getAllOrderOptionsByCompany(@PathVariable UUID companyId, Principal principal) {
        String userId = principal.getName();
        List<OrderOptionsDto> orderOptionsDto = orderOptionsService.getAllOrderOptionsByCompany(companyId, userId);
        return ResponseEntity.ok(orderOptionsDto);
    }

    @PostMapping("/{companyId}/create")
    public ResponseEntity<OrderOptionsDto> createOrderOption(@PathVariable UUID companyId,
                                                             @RequestBody @Valid CreateOrderOptionsDto createOrderOptionsDto,
                                                             Principal principal) {
        String userId = principal.getName();
        OrderOptionsDto orderOptionsDto = orderOptionsService.createOrderOption(createOrderOptionsDto, companyId, userId);
        return ResponseEntity.ok(orderOptionsDto);
    }

    @PatchMapping("/{orderOptionId}/update")
    public ResponseEntity<OrderOptionsDto> updateOrderOption(@PathVariable Long orderOptionId,
                                                              @RequestBody @Valid CreateOrderOptionsDto createOrderOptionsDto,
                                                              Principal principal) {
        String userId = principal.getName();
        OrderOptionsDto orderOptionsDto = orderOptionsService.updateOrderOption(createOrderOptionsDto, orderOptionId, userId);
        return ResponseEntity.ok(orderOptionsDto);
    }

    @DeleteMapping("/{orderOptionId}/delete")
    public ResponseEntity<OrderOptionsDto> deleteOrderOption(@PathVariable Long orderOptionId, Principal principal) {
        String userId = principal.getName();
        OrderOptionsDto orderOptionsDto = orderOptionsService.deleteOrderOption(orderOptionId, userId);
        return ResponseEntity.ok(orderOptionsDto);
    }

}
