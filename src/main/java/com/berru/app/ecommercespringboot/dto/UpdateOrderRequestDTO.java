package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequestDTO {

    @NotNull(message = "Order ID cannot be null")
    @Min(value = 1, message = "Order ID must be greater than 0")
    private Integer orderId;

    private OrderStatus orderStatus;

    private List<UpdateOrderItemRequestDTO> orderItems = new ArrayList<>();
}