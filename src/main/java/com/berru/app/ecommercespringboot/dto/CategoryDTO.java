package com.berru.app.ecommercespringboot.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryDTO {

    private Integer id;
    private String name;
    private Integer parentId;
    private List<CategoryDTO> children;
}
