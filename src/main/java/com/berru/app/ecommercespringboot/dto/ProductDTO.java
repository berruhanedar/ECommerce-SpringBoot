package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.enums.ProductStatus;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer quantity;
    private String image;
    private Integer categoryId;
    private ProductStatus status;
}