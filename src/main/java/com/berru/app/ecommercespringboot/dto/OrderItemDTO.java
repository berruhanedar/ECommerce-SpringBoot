package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.entity.Order;
import com.berru.app.ecommercespringboot.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {

    private Integer orderItemId;
    private Product product;
    private Integer quantity;
    private BigDecimal price;

    @JsonInclude(JsonInclude.Include.NON_NULL) 
    private Order order;
}
