package com.berru.app.ecommercespringboot.controller;


import com.berru.app.ecommercespringboot.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
             return ResponseEntity.ok(new Product());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
               return ResponseEntity.ok(List.of(new Product()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product productDetails) {
            return ResponseEntity.ok(productDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
            return ResponseEntity.noContent().build();
    }
}