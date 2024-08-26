package com.berru.app.ecommercespringboot.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShoppingCartDTO {
    private Integer shoppingCartId;
    private Integer userId;
    private BigDecimal totalPrice;
}