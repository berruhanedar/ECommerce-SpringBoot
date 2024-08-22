package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * lombok anotasyonu kullan
     * @param categoryService
     */
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

    @GetMapping("/{id}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryId(@PathVariable Integer id) {
        return categoryService.getProductsByCategoryId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Integer id, @RequestBody UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        return categoryService.updateCategory(id, updateCategoryRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteCategoryResponseDTO> deleteCategory(@PathVariable Integer id) {
        return categoryService.deleteCategory(id);
    }
}