package com.berru.app.ecommercespringboot.dto;

import lombok.Data;

@Data
public class DeleteCategoryResponseDTO {
    private Integer id;
    private String name;
    private String message;

    public DeleteCategoryResponseDTO() {
    }

      public DeleteCategoryResponseDTO(String message) {
        this.message = message;
    }

    public DeleteCategoryResponseDTO(Integer id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }
}
