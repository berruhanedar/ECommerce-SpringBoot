package com.berru.app.ecommercespringboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductRequestDTO {

    private String name;
    private BigDecimal price;
    private String description;
    private Integer quantity;
    private String image;
    private Integer categoryId;
}
