package com.berru.app.ecommercespringboot.dto;

import lombok.Data;


@Data
public class AddToCartRequestDTO {
    private Integer customerId;
    private Integer productId;
    private Integer quantity;
}
