package com.berru.app.ecommercespringboot.controller;


import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid NewProductRequestDTO newProductRequestDTO) {
        return productService.create(newProductRequestDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<ProductDTO>> listPaginated(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return productService.listPaginated(pageNo, pageSize);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @RequestBody @Valid UpdateProductRequestDTO updateProductRequestDTO) {
        return productService.updateProduct(id, updateProductRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteProductResponseDTO> deleteProduct(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }
}