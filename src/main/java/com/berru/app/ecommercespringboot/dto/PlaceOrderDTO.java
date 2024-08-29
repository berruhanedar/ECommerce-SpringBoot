package com.berru.app.ecommercespringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDTO {
    private Integer id;
    private Integer customerId;
    private Integer addressId;
    private BigDecimal totalAmount;
}

