package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.enums.ProductStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import lombok.Data;


@Data
public class UpdateProductRequestDTO {

    @NotBlank
    @Size(min = 3, max = 30, message = "Product name size should be between 3-30")
    private String name;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal price;

    @NotBlank
    @Size(max = 255)
    private String description;

    @NotNull
    @Min(value = 0)
    private Integer quantity;

    @Size(max = 255)
    private String image;

    private Integer categoryId;

    private ProductStatus status;
}