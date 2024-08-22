package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;

import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<ProductDTO> create(NewProductRequestDTO newProductRequestDTO) {
        Product product = productMapper.toEntity(newProductRequestDTO);
        Category category = categoryRepository.findById(newProductRequestDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductDTO productDTO = productMapper.toDto(savedProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }


    public ResponseEntity<ProductDTO> getProductById(Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new NotFoundException("Product not found");
        }

        Product product = productOptional.get();
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    public ResponseEntity<ProductDTO> updateProduct(Integer id, UpdateProductRequestDTO updateProductRequestDTO) {
        // Ürünü ID ile bulma
        Optional<Product> existingProductOpt = productRepository.findById(id);

        // Ürün bulunamazsa hata fırlatma
        if (existingProductOpt.isEmpty()) {
            throw new NotFoundException("Product not found");
        }

        Product existingProduct = existingProductOpt.get();

        if (updateProductRequestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateProductRequestDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            existingProduct.setCategory(category);
        }

        productMapper.updateProductFromDto(updateProductRequestDTO, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        return ResponseEntity.ok(productDTO);
    }


    public ResponseEntity<DeleteProductResponseDTO> deleteProduct(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            DeleteProductResponseDTO response = new DeleteProductResponseDTO("Product deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    public ResponseEntity<PaginationResponse<ProductDTO>> listPaginated(int pageNo, int pageSize) {

        List<Product> products = productRepository.findAll();

        int start = Math.min(pageNo * pageSize, products.size());
        int end = Math.min(start + pageSize, products.size());
        List<Product> pagedProducts = products.subList(start, end);

        List<ProductDTO> productDTOList = pagedProducts.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        PaginationResponse<ProductDTO> productPaginationResponse = new PaginationResponse<>();
        productPaginationResponse.setContent(productDTOList);
        productPaginationResponse.setPageNo(pageNo);
        productPaginationResponse.setPageSize(pageSize);
        productPaginationResponse.setTotalElements((long) products.size());
        productPaginationResponse.setTotalPages((int) Math.ceil((double) products.size() / pageSize));
        productPaginationResponse.setLast(end == products.size());

        return ResponseEntity.ok(productPaginationResponse);
    }
}