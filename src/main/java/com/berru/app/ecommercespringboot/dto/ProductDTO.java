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

    @NotNull
    @Size(min = 3, max = 30, message = "Product name size should be between 3-30")
    private String name;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal price;

    @Size(max = 255)
    @NotNull
    private String description;

    @NotNull
    @Min(value = 0)
    private Integer quantity;

    @Size(max = 255)
    private String image;

    private Integer categoryId;
}
