package com.berru.app.ecommercespringboot.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShoppingCartDTO {
    private Integer id;
    private Integer customerId;
    private BigDecimal totalPrice;
    private Integer quantity;
    private Integer productId;
}