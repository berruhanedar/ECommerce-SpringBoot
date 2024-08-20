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
public class NewProductRequestDTO {
    @NotNull
    @Size(min = 3, max = 30, message = "Product name size should be between 3-30")
    private String name;

    @NotNull
    @DecimalMin(value = "0.00", message = "Price must be at least 0.00")
    private BigDecimal price;

    @NotNull
    @Size(max = 255, message = "Description size should not exceed 255 characters")
    private String description;

    @NotNull
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @Size(max = 255, message = "Image URL size should not exceed 255 characters")
    private String image;

    @NotNull
    private Integer categoryId;
}
