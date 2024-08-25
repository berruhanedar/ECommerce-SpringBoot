package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class NewCategoryRequestDTO {

    @NotBlank
    @Size(min = 3, max = 30, message = "Category name size should be between 3-30")
    private String name;

    private Integer parentCategoryId;
}
