package com.berru.app.ecommercespringboot.dto;

import lombok.Data;

@Data
public class ProductAttributeValueDTO {
    private Integer id;
    private AttributeDTO attribute;
    private String value;
}

