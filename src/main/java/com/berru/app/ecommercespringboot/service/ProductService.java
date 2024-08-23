package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

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

        Optional.ofNullable(updateProductRequestDTO.getCategoryId())
                .ifPresent(categoryId -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new NotFoundException("Category not found"));
                    existingProduct.setCategory(category);
                });

        productMapper.updateProductFromDto(updateProductRequestDTO, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        Optional.of(id)
                .filter(productRepository::existsById)
                .ifPresentOrElse(
                        existingId -> productRepository.deleteById(existingId),
                        () -> { throw new NotFoundException("Product not found"); }
                );
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ProductDTO> listPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return PaginationResponse.<ProductDTO>builder()
                .content(productDTOList)
                .pageNo(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .isLast(productPage.isLast())
                .build();
    }
}
