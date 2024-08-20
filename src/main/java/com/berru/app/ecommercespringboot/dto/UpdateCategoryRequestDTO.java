package com.berru.app.ecommercespringboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequestDTO {
    private String name;
    private Integer parentCategoryId;
}
