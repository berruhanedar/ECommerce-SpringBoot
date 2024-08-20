package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    // Tüm kategorileri getir
    @GetMapping
    public List<Category> getAllCategories() {
        return null;
    }

    // ID'ye göre bir kategori getir
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return null;
    }

    // Yeni bir kategori oluştur
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return null;
    }

    // Mevcut bir kategoriyi güncelle
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category updatedCategory) {
        return null;
    }

    // Bir kategoriyi sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        return null;
    }
}
