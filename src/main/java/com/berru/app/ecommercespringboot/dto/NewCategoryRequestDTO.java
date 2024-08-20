package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCategoryRequestDTO {
    @NotNull
    @Size(min = 1, max = 50, message = "Category name size should be between 1-50")
    private String name;

    private Integer parentCategoryId; // Optional parent category ID
}
