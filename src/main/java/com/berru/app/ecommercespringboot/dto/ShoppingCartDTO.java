package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.enums.ShoppingCartStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShoppingCartDTO {
    private Integer id;
    private BigDecimal totalPrice;
    private CustomerDTO customer;
    private List<ShoppingCartItemDTO> items;
    private ShoppingCartStatus status;
}