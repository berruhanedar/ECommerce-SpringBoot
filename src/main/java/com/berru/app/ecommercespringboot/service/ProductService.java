package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.NewProductRequestDTO;
import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
