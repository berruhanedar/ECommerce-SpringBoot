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
        // DTO'dan Entity'ye dönüşüm
        Product product = productMapper.toEntity(newProductRequestDTO);

        // Kategori ID'sini ayarla (Opsiyonel: Eğer `Category` nesnesi kullanmak istiyorsanız)
        // product.setCategory(categoryRepository.findById(newProductRequestDTO.getCategoryId()).orElse(null));

        // Veritabanına kaydetme
        Product savedProduct = productRepository.save(product);

        // Entity'den DTO'ya dönüşüm
        ProductDTO productDTO = productMapper.toDto(savedProduct);

        return ResponseEntity.ok(productDTO);
    }
}
