package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.dto.NewCategoryRequestDTO;
import com.berru.app.ecommercespringboot.dto.CategoryDTO;
import com.berru.app.ecommercespringboot.dto.UpdateCategoryRequestDTO;
import com.berru.app.ecommercespringboot.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}