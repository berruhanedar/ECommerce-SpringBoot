package com.berru.app.ecommercespringboot.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {

    private Integer orderItemId;
    private Integer productId;
    private Integer orderId;
    private Integer quantity;
    private BigDecimal orderedProductPrice;
}
