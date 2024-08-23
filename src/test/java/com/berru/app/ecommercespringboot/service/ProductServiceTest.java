/*package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        ProductDTO response = productService.create(requestDTO);

        assertEquals(productDTO, response);

        verify(categoryRepository).findById(1);
        verify(productMapper).toEntity(requestDTO);
        verify(productRepository).save(product);
        verify(productMapper).toDto(product);
    }

    @Test
    void whenSaveCalledWithInvalidCategoryId_itShouldThrowNotFoundException() {
        NewProductRequestDTO requestDTO = new NewProductRequestDTO();
        requestDTO.setCategoryId(999);

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.create(requestDTO));

        verify(categoryRepository).findById(999);
    }

    @Test
    void whenGetByIdCalledWithValidId_itShouldReturnValidProductDTO() {
        int validProductId = 1;

        Product product = new Product();
        product.setId(validProductId);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setDescription("Test Description");
        product.setQuantity(10);
        product.setImage("test-image.png");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(validProductId);
        productDTO.setName("Test Product");
        productDTO.setPrice(BigDecimal.valueOf(100.00));
        productDTO.setDescription("Test Description");
        productDTO.setQuantity(10);
        productDTO.setImage("test-image.png");

        when(productRepository.findById(validProductId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDTO);

        ProductDTO response = productService.getProductById(validProductId);

        assertEquals(productDTO, response);
        verify(productRepository).findById(validProductId);
        verify(productMapper).toDto(product);
    }

    @Test
    void whenUpdateCalledWithValidRequest_itShouldReturnUpdatedProductDTO() {
        int productId = 1;
        int categoryId = 2;

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Product Name");
        existingProduct.setPrice(BigDecimal.valueOf(100.00));
        existingProduct.setDescription("Old Description");
        existingProduct.setQuantity(10);
        existingProduct.setImage("old-image.png");

        Category category = new Category();
        category.setId(categoryId);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product Name");
        updatedProduct.setPrice(BigDecimal.valueOf(200.00));
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setQuantity(20);
        updatedProduct.setImage("updated-image.png");
        updatedProduct.setCategory(category);

        UpdateProductRequestDTO updateProductRequestDTO = new UpdateProductRequestDTO();
        updateProductRequestDTO.setName("Updated Product Name");
        updateProductRequestDTO.setPrice(BigDecimal.valueOf(200.00));
        updateProductRequestDTO.setDescription("Updated Description");
        updateProductRequestDTO.setQuantity(20);
        updateProductRequestDTO.setImage("updated-image.png");
        updateProductRequestDTO.setCategoryId(categoryId);

        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setId(productId);
        updatedProductDTO.setName("Updated Product Name");
        updatedProductDTO.setPrice(BigDecimal.valueOf(200.00));
        updatedProductDTO.setDescription("Updated Description");
        updatedProductDTO.setQuantity(20);
        updatedProductDTO.setImage("updated-image.png");
        updatedProductDTO.setCategoryId(categoryId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.save(existingProduct)).thenReturn(updatedProduct);
        when(productMapper.toDto(updatedProduct)).thenReturn(updatedProductDTO);

        ProductDTO response = productService.updateProduct(productId, updateProductRequestDTO);

        assertEquals(updatedProductDTO, response);
        verify(productRepository).findById(productId);
        verify(categoryRepository).findById(categoryId);
        verify(productRepository).save(existingProduct);
        verify(productMapper).toDto(updatedProduct);
    }

    @Test
    void whenUpdateCalledWithInvalidProductId_itShouldThrowNotFoundException() {
        int invalidProductId = 1;
        UpdateProductRequestDTO updateProductRequestDTO = new UpdateProductRequestDTO();

        when(productRepository.findById(invalidProductId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(invalidProductId, updateProductRequestDTO));

        verify(productRepository).findById(invalidProductId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void whenUpdateCalledWithInvalidCategoryId_itShouldThrowNotFoundException() {
        int productId = 1;
        int invalidCategoryId = 99;

        Product existingProduct = new Product();
        existingProduct.setId(productId);

        UpdateProductRequestDTO updateProductRequestDTO = new UpdateProductRequestDTO();
        updateProductRequestDTO.setCategoryId(invalidCategoryId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(invalidCategoryId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(productId, updateProductRequestDTO));

        verify(productRepository).findById(productId);
        verify(categoryRepository).findById(invalidCategoryId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void whenDeleteCalledWithValidId_itShouldReturnSuccessMessage() {
        int productId = 1;

        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteProduct(productId);

        verify(productRepository).existsById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void whenDeleteCalledWithInvalidId_itShouldThrowNotFoundException() {
        int invalidProductId = 1;

        when(productRepository.existsById(invalidProductId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(invalidProductId));

        verify(productRepository).existsById(invalidProductId);
        verify(productRepository, never()).deleteById(invalidProductId);
    }

    /*
    @Test
    void whenListCalledWithValidPageRequest_itShouldReturnPaginatedProductDTOs() {
        int pageNo = 0;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setPrice(BigDecimal.valueOf(100.00));
        product1.setDescription("Description 1");
        product1.setQuantity(10);
        product1.setImage("image1.png");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(200.00));
        product2.setDescription("Description 2");
        product2.setQuantity(20);
        product2.setImage("image2.png");

        List<Product> productList = Arrays.asList(product1, product2);
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1);
        productDTO1.setName("Product 1");
        productDTO1.setPrice(BigDecimal.valueOf(100.00));
        productDTO1.setDescription("Description 1");
        productDTO1.setQuantity(10);
        productDTO1.setImage("image1.png");

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(2);
        productDTO2.setName("Product 2");
        productDTO2.setPrice(BigDecimal.valueOf(200.00));
        productDTO2.setDescription("Description 2");
        productDTO2.setQuantity(20);
        productDTO2.setImage("image2.png");

        List<ProductDTO> productDTOList = Arrays.asList(productDTO1, productDTO2);

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toDto(product1)).thenReturn(productDTO1);
        when(productMapper.toDto(product2)).thenReturn(productDTO2);

        PaginationResponse<ProductDTO> response = productService.listPaginated(pageNo, pageSize);

        assertEquals(productDTOList, response.getContent());
        assertEquals(pageNo, response.getPageNo());
        assertEquals(pageSize, response.getPageSize());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(true, response.isLast());

        verify(productRepository).findAll(pageable);
        verify(productMapper).toDto(product1);
        verify(productMapper).toDto(product2);
    }
}
*/