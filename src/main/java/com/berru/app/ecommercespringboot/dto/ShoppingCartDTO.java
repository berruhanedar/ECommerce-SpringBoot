package com.berru.app.ecommercespringboot.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShoppingCartDTO {
    private Integer Id;
    private Integer customerId;
    private BigDecimal totalPrice;
}