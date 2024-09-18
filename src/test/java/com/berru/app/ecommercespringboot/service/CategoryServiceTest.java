package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.CategoryMapper;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;


    @Test
    void whenCreateCalledWithValidRequest_itShouldReturnValidCategoryDTO() {
        NewCategoryRequestDTO requestDTO = new NewCategoryRequestDTO();
        requestDTO.setName("Electronics");
        requestDTO.setParentCategoryId(1);

        Category parentCategory = new Category();
        parentCategory.setId(1);
        parentCategory.setName("Home");

        Category category = new Category();
        category.setName("Electronics");
        category.setParentCategory(parentCategory);

        Category savedCategory = new Category();
        savedCategory.setId(2);
        savedCategory.setName("Electronics");
        savedCategory.setParentCategory(parentCategory);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(2);
        categoryDTO.setName("Electronics");
        categoryDTO.setParentId(1);

        when(categoryRepository.findById(requestDTO.getParentCategoryId())).thenReturn(Optional.of(parentCategory));
        when(categoryMapper.toCategoryEntity(requestDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toCategoryDTO(savedCategory)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.create(requestDTO);

        assertEquals(2, result.getId());
        assertEquals("Electronics", result.getName());
        assertEquals(1, result.getParentId());

        verify(categoryRepository).findById(requestDTO.getParentCategoryId());
        verify(categoryRepository).save(category);
    }

    @Test
    void whenCreateCalledWithNullParentCategory_itShouldCreateIndependentCategory() {
        // Arrange
        NewCategoryRequestDTO requestDTO = new NewCategoryRequestDTO();
        requestDTO.setName("Electronics");
        requestDTO.setParentCategoryId(null); // Parent Category ID null

        Category category = new Category();
        category.setName("Electronics");
        category.setParentCategory(null); // No parent category

        Category savedCategory = new Category();
        savedCategory.setId(2);
        savedCategory.setName("Electronics");
        savedCategory.setParentCategory(null); // No parent category

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(2);
        categoryDTO.setName("Electronics");
        categoryDTO.setParentId(null); // No parent category in DTO

        when(categoryMapper.toCategoryEntity(requestDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toCategoryDTO(savedCategory)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.create(requestDTO);

        assertEquals(2, result.getId());
        assertEquals("Electronics", result.getName());
        assertEquals(null, result.getParentId()); // Parent ID should be null

        verify(categoryRepository).save(category);
    }


    @Test
    void whenGetProductsByValidCategoryId_itShouldReturnProductDTOList() {
        Integer categoryId = 1;

        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        Category subCategory = new Category();
        subCategory.setId(2);
        subCategory.setName("Mobile Phones");

        List<Category> categoryTree = Arrays.asList(category, subCategory);

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("iPhone 13");
        product1.setCategory(subCategory);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Samsung Galaxy S21");
        product2.setCategory(subCategory);

        List<Product> products = Arrays.asList(product1, product2);

        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1);
        productDTO1.setName("iPhone 13");

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(2);
        productDTO2.setName("Samsung Galaxy S21");

        List<ProductDTO> productDTOList = Arrays.asList(productDTO1, productDTO2);

        when(categoryRepository.findCategoryTreeById(categoryId)).thenReturn(categoryTree);
        when(productRepository.findByCategoryIdIn(Arrays.asList(1, 2))).thenReturn(products);
        when(productMapper.toDtoList(products)).thenReturn(productDTOList);

        List<ProductDTO> result = categoryService.getProductsByCategoryId(categoryId);

        assertEquals(2, result.size());
        assertEquals("iPhone 13", result.get(0).getName());
        assertEquals("Samsung Galaxy S21", result.get(1).getName());

        verify(categoryRepository).findCategoryTreeById(categoryId);
        verify(productRepository).findByCategoryIdIn(Arrays.asList(1, 2));
        verify(productMapper).toDtoList(products);
    }

    @Test
    void whenGetProductsByInvalidCategoryId_itShouldReturnEmptyList() {
        Integer categoryId = 999;

        when(categoryRepository.findCategoryTreeById(categoryId)).thenReturn(Collections.emptyList());
        when(productRepository.findByCategoryIdIn(Collections.emptyList())).thenReturn(Collections.emptyList());
        when(productMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ProductDTO> result = categoryService.getProductsByCategoryId(categoryId);

        assertEquals(0, result.size());

        verify(categoryRepository).findCategoryTreeById(categoryId);
        verify(productRepository).findByCategoryIdIn(Collections.emptyList());
        verify(productMapper).toDtoList(Collections.emptyList());
    }

    @Test
    void whenUpdateCategoryCalledWithValidRequest_itShouldReturnUpdatedCategoryDTO() {
        Integer categoryId = 1;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        UpdateCategoryRequestDTO updateCategoryRequestDTO = new UpdateCategoryRequestDTO();
        updateCategoryRequestDTO.setName("Updated Electronics");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("Updated Electronics");

        CategoryDTO updatedCategoryDTO = new CategoryDTO();
        updatedCategoryDTO.setId(categoryId);
        updatedCategoryDTO.setName("Updated Electronics");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(updatedCategory);
        when(categoryMapper.toCategoryDTO(updatedCategory)).thenReturn(updatedCategoryDTO);

        Optional<CategoryDTO> result = categoryService.updateCategory(categoryId, updateCategoryRequestDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Electronics", result.get().getName());

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDTO(updateCategoryRequestDTO, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toCategoryDTO(updatedCategory);
    }


    @Test
    void whenUpdateCategoryCalledWithNonExistentId_itShouldReturnEmptyOptional() {
        Integer nonExistentCategoryId = 999;
        UpdateCategoryRequestDTO updateCategoryRequestDTO = new UpdateCategoryRequestDTO();
        updateCategoryRequestDTO.setName("Updated Category");

        when(categoryRepository.findById(nonExistentCategoryId)).thenReturn(Optional.empty());

        Optional<CategoryDTO> result = categoryService.updateCategory(nonExistentCategoryId, updateCategoryRequestDTO);

        assertFalse(result.isPresent());

        verify(categoryRepository).findById(nonExistentCategoryId);
    }


    @Test
    void whenDeleteCategoryCalledWithValidId_itShouldDeleteCategory() {
        Integer categoryId = 1;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).delete(category);
    }

    @Test
    void whenDeleteCategoryCalledWithNonExistentId_itShouldThrowNotFoundException() {
        Integer nonExistentCategoryId = 999;

        when(categoryRepository.findById(nonExistentCategoryId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.deleteCategory(nonExistentCategoryId);
        });

        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository).findById(nonExistentCategoryId);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void whenGetAllCategoriesPaginatedCalledWithValidPage_itShouldReturnPaginatedCategories() {
        // Arrange
        int pageNo = 0;
        int pageSize = 10;

        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Laptops");

        List<Category> categories = List.of(category1, category2);
        Page<Category> categoryPage = new PageImpl<>(categories, PageRequest.of(pageNo, pageSize), 2);

        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setId(1);
        categoryDTO1.setName("Electronics");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setId(2);
        categoryDTO2.setName("Laptops");

        List<CategoryDTO> categoryDTOList = List.of(categoryDTO1, categoryDTO2);

        // Mocking
        when(categoryRepository.findAll(PageRequest.of(pageNo, pageSize))).thenReturn(categoryPage);
        when(categoryMapper.toCategoryDTO(category1)).thenReturn(categoryDTO1);
        when(categoryMapper.toCategoryDTO(category2)).thenReturn(categoryDTO2);

        // Act
        PaginationResponse<CategoryDTO> result = categoryService.getAllCategoriesPaginated(pageNo, pageSize);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPageNo());
        assertEquals(10, result.getPageSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
    }

}