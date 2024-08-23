package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.NewProductRequestDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.dto.UpdateProductRequestDTO;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The ProductService class contains business logic related to products.
 * It provides operations for adding, deleting, updating, and listing products.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    /**
     * Creates and saves a new product.
     *
     * @param newProductRequestDTO DTO containing the details of the new product.
     * @return DTO of the created product.
     * @throws NotFoundException If the category is not found.
     */
    @Transactional
    public ProductDTO create(NewProductRequestDTO newProductRequestDTO) {
        Product product = productMapper.toEntity(newProductRequestDTO);
        Category category = categoryRepository.findById(newProductRequestDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product.
     * @return DTO of the product.
     * @throws NotFoundException If the product is not found.
     */
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Integer id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    /**
     * Updates an existing product with new information.
     *
     * @param id                      The ID of the product to update.
     * @param updateProductRequestDTO DTO containing the updated product information.
     * @return DTO of the updated product.
     * @throws NotFoundException If the product or category is not found.
     */
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

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @throws NotFoundException If the product is not found.
     */
    @Transactional
    public void deleteProduct(Integer id) {
        Optional.of(id)
                .filter(productRepository::existsById)
                .ifPresentOrElse(
                        existingId -> productRepository.deleteById(existingId),
                        () -> {
                            throw new NotFoundException("Product not found");
                        }
                );
    }

    /**
     * Retrieves a paginated list of products.
     *
     * @param pageNo   The page number to retrieve.
     * @param pageSize The number of products per page.
     * @return A pagination response containing the list of product DTOs and pagination details.
     */
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
