package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderItemRequestDTO {

    @NotNull(message = "Order item ID cannot be null")
    private Integer orderItemId;

    private Integer productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Ordered product price cannot be null")
    @Min(value = 1, message = "Ordered product price must be at least 0.01")
    private BigDecimal orderedProductPrice;
}