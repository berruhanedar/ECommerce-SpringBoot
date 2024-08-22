package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid NewProductRequestDTO newProductRequestDTO) {
        ProductDTO productDTO = productService.create(newProductRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<ProductDTO>> listPaginated(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        PaginationResponse<ProductDTO> paginationResponse = productService.listPaginated(pageNo, pageSize);
        return ResponseEntity.ok(paginationResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @RequestBody @Valid UpdateProductRequestDTO updateProductRequestDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, updateProductRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
