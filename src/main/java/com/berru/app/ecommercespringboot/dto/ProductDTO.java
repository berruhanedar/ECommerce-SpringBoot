package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import com.berru.app.ecommercespringboot.entity.Product.Status;

@Data
public class ProductDTO {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer quantity;
    private String image;
    private Integer categoryId;
    private Status status;
}