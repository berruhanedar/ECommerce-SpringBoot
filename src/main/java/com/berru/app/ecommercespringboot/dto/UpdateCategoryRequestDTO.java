package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequestDTO {
    @NotNull
    @Size(min = 1, max = 100, message = "Category name size should be between 1-100")
    private String name;

    private Integer parentId;
}
