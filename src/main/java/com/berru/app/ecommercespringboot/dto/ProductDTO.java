package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryDTO> categoryTree;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductAttributeValueDTO> productAttributeValues;
}