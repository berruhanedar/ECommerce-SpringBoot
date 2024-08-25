package com.berru.app.ecommercespringboot.dto;

import java.util.List;

import lombok.Data;

@Data
public class CategoryDTO {

    private Integer id;
    private String name;
    private Integer parentId;
    private List<CategoryDTO> children;
}
