package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody NewCategoryRequestDTO newCategoryRequestDTO) {
        CategoryDTO categoryDTO = categoryService.create(newCategoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categoryTree = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryTree);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryId(@PathVariable Integer id) {
        List<ProductDTO> productDTOs = categoryService.getProductsByCategoryId(id);
        return ResponseEntity.ok(productDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Integer id, @RequestBody UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        return categoryService.updateCategory(id, updateCategoryRequestDTO)
                .map(ResponseEntity::ok) // If categoryDTO is present, return 200 OK with categoryDTO
                .orElseGet(() -> ResponseEntity.notFound().build()); // If not present, return 404 Not Found
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}