package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateProductRequestDTO {

    private Integer id;

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
    // status ekle
}
