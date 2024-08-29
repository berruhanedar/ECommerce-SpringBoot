package com.berru.app.ecommercespringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderItemRequestDTO {
    private Integer orderItemId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal orderedProductPrice;
}