package com.berru.app.ecommercespringboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryDTO {
    private Integer id;
    private String name;
    private Integer parentId;
    private List<CategoryDTO> children;
}
