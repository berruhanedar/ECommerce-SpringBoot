package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.NewProductRequestDTO;
import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.dto.UpdateProductRequestDTO;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ResponseEntity<ProductDTO> create(NewProductRequestDTO newProductRequestDTO) {
        Product product = productMapper.toEntity(newProductRequestDTO);
        Product savedProduct = productRepository.save(product);
        ProductDTO productDTO = productMapper.toDto(savedProduct);

        return ResponseEntity.ok(productDTO);
    }

    public ResponseEntity<ProductDTO> getProductById(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            ProductDTO productDTO = productMapper.toDto(product.get());
            return ResponseEntity.ok(productDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDto)
                .toList();
        return ResponseEntity.ok(productDTOs);
    }

    public ResponseEntity<ProductDTO> updateProduct(Integer id, UpdateProductRequestDTO updateProductRequestDTO) {
        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            productMapper.updateProductFromDto(updateProductRequestDTO, existingProduct);
            Product updatedProduct = productRepository.save(existingProduct);
            ProductDTO productDTO = productMapper.toDto(updatedProduct);
            return ResponseEntity.ok(productDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteProduct(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}