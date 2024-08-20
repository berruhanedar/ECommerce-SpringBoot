package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.CategoryDTO;
import com.berru.app.ecommercespringboot.dto.NewCategoryRequestDTO;
import com.berru.app.ecommercespringboot.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody NewCategoryRequestDTO newCategoryRequestDTO) {
        return categoryService.create(newCategoryRequestDTO);
    }


    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
