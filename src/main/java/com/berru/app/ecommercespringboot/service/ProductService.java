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
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductDTO create(NewProductRequestDTO newProductRequestDTO) {
        Product product = productMapper.toEntity(newProductRequestDTO);
        Category category = categoryRepository.findById(newProductRequestDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }


    @Transactional(readOnly = true)
    public ProductDTO getProductById(Integer id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Transactional
    public ProductDTO updateProduct(Integer id, UpdateProductRequestDTO updateProductRequestDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (updateProductRequestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateProductRequestDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            existingProduct.setCategory(category);
        }

        productMapper.updateProductFromDto(updateProductRequestDTO, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }


    @Transactional
    public void deleteProduct(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    /**
     * düzelt
     */
    public PaginationResponse<ProductDTO> listPaginated(int pageNo, int pageSize) {
        List<Product> products = productRepository.findAll();
        int start = Math.min(pageNo * pageSize, products.size());
        int end = Math.min(start + pageSize, products.size());
        List<ProductDTO> productDTOList = products.subList(start, end).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        PaginationResponse<ProductDTO> response = new PaginationResponse<>();
        response.setContent(productDTOList);
        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotalElements((long) products.size());
        response.setTotalPages((int) Math.ceil((double) products.size() / pageSize));
        response.setLast(end == products.size());

        return response;
    }
}