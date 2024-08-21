package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.NewProductRequestDTO;
import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import com.berru.app.ecommercespringboot.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        productMapper = Mockito.mock(ProductMapper.class);
        productService = new ProductService(productRepository, productMapper, categoryRepository);
    }

    @Test
    void whenCreateCalledWithValidRequest_itShouldReturnValidProductDTO() {
        NewProductRequestDTO requestDTO = new NewProductRequestDTO();
        requestDTO.setName("Test Product");
        requestDTO.setPrice(BigDecimal.valueOf(100.00));
        requestDTO.setDescription("Test Description");
        requestDTO.setQuantity(10);
        requestDTO.setImage("test-image.png");
        requestDTO.setCategoryId(1);

        Category category = new Category();
        category.setId(1);

        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setDescription("Test Description");
        product.setQuantity(10);
        product.setImage("test-image.png");
        product.setCategory(category);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Test Product");
        productDTO.setPrice(BigDecimal.valueOf(100.00));
        productDTO.setDescription("Test Description");
        productDTO.setQuantity(10);
        productDTO.setImage("test-image.png");
        productDTO.setCategoryId(1);


        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(requestDTO)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDTO);


        ResponseEntity<ProductDTO> response = productService.create(requestDTO);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productDTO, response.getBody());

        verify(categoryRepository).findById(1);
        verify(productMapper).toEntity(requestDTO);
        verify(productRepository).save(product);
        verify(productMapper).toDto(product);
    }

    @Test
    void whenSaveCalledWithInvalidCategoryId_itShouldThrowNotFoundException() {
        // Arrange
        NewProductRequestDTO requestDTO = new NewProductRequestDTO();
        requestDTO.setName("Test Product");
        requestDTO.setPrice(BigDecimal.valueOf(100.00));
        requestDTO.setDescription("Test Description");
        requestDTO.setQuantity(10);
        requestDTO.setImage("test-image.png");
        requestDTO.setCategoryId(999);  // Invalid category ID

        // Mocking: Category repository should return an empty Optional
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            productService.create(requestDTO);
        });

        // Verify that findById was called
        Mockito.verify(categoryRepository).findById(999);
    }

    @Test
    void getProductById() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    void listPaginated() {
    }
}