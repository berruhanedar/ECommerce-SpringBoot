package com.berru.app.ecommercespringboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;

    @NotNull
    @Size(min = 3, max = 30, message = "Product name size should be between 3-30")
    private String productName;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal price;

    @Size(max = 255)
    @NotNull
    private String description;

    @NotNull
    @Min(value = 0)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;




}