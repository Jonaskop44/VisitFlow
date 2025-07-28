package com.jonas.visitflow.order.dto;

import com.jonas.visitflow.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderDto {

    @NotNull(message = "Please provide a valid status")
    private OrderStatus status;

}
