package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer quantity;
    private String image;
    private Integer categoryId;
}
