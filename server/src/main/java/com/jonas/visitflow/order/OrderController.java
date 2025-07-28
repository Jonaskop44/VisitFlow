package com.jonas.visitflow.order;

import com.jonas.visitflow.order.dto.CreateOrderDto;
import com.jonas.visitflow.order.dto.OrderDto;
import com.jonas.visitflow.order.dto.UpdateOrderDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{id}/submit")
    public ResponseEntity<OrderDto> createOrder(@PathVariable UUID id, @RequestBody @Valid CreateOrderDto createOrderDto) {
        OrderDto orderDto = orderService.createOrder(createOrderDto, id);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping("/{id}/all")
    public ResponseEntity<List<OrderDto>> getAllOrders(@PathVariable UUID id,
                                                       @RequestParam(required = false)
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                       @RequestParam(required = false)
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                                       Principal principal) {
        String userId = principal.getName();
        List<OrderDto> orders = orderService.getAllOrders(userId, id, start, end);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long id,
                                                      @RequestBody @Valid UpdateOrderDto updateOrderDto,
                                                      Principal principal) {
        String userId = principal.getName();
        OrderDto updatedOrder = orderService.updateOrderStatus(id, updateOrderDto, userId);
        return ResponseEntity.ok(updatedOrder);
    }

}
