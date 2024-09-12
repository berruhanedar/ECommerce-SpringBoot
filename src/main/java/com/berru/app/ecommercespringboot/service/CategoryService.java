package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.CategoryDTO;
import com.berru.app.ecommercespringboot.dto.NewCategoryRequestDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.dto.UpdateCategoryRequestDTO;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.CategoryMapper;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO create(NewCategoryRequestDTO newCategoryRequestDTO) {
        Category parentCategory = newCategoryRequestDTO.getParentCategoryId() != null
                ? categoryRepository.findById(newCategoryRequestDTO.getParentCategoryId()).orElse(null)
                : null;
        Category category = categoryMapper.toCategoryEntity(newCategoryRequestDTO);
        category.setParentCategory(parentCategory);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDTO(savedCategory);
    }

    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public List<ProductDTO> getProductsByCategoryId(Integer categoryId) {
        List<Category> categoryTree = categoryRepository.findCategoryTreeById(categoryId);
        List<Integer> categoryIds = categoryTree.stream()
                .map(Category::getId)
                .collect(Collectors.toList());
        List<Product> products = productRepository.findByCategoryIdIn(categoryIds);
        return productMapper.toDtoList(products);
    }


    @Transactional
    @CachePut(value = "categories", key = "#id")
    public Optional<CategoryDTO> updateCategory(Integer id, UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        return categoryRepository.findById(id).map(category -> {
            categoryMapper.updateCategoryFromDTO(updateCategoryRequestDTO, category);
            Category updatedCategory = categoryRepository.save(category);
            return categoryMapper.toCategoryDTO(updatedCategory);
        });
    }

    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public void deleteCategory(Integer id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(
                        categoryRepository::delete,
                        () -> {
                            throw new NotFoundException("Category not found");
                        }
                );
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoryTree(Integer categoryId) {
        List<Category> categoryTree = categoryRepository.findCategoryTreeById(categoryId);
        List<CategoryDTO> categoryDTOList = categoryTree.stream()
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
        return buildCategoryTree(categoryDTOList, categoryId);
    }


    @Transactional(readOnly = true)
    public PaginationResponse<CategoryDTO> getAllCategoriesPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryDTO> categoryDTOList = categoryPage.getContent().stream()
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());

        List<CategoryDTO> categoryTree = buildCategoryTree(categoryDTOList, null);
        return PaginationResponse.<CategoryDTO>builder()
                .content(categoryTree)
                .pageNo(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .isLast(categoryPage.isLast())
                .build();
    }

    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories, Integer rootCategoryId) {
        return Optional.ofNullable(rootCategoryId)
                .map(id -> categories.stream()
                        .filter(category -> category.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException("Kategori bulunamadı")))
                .map(rootCategory -> {
                    populateChildren(rootCategory, categories);
                    return List.of(rootCategory);
                })
                .orElseGet(() -> categories.stream()
                        .filter(category -> category.getParentId() == null)
                        .peek(category -> populateChildren(category, categories))
                        .collect(Collectors.toList()));
    }

    private void populateChildren(CategoryDTO parent, List<CategoryDTO> categories) {
        parent.setChildren(categories.stream()
                .filter(category -> parent.getId().equals(category.getParentId()))
                .peek(child -> populateChildren(child, categories)) // Recursive call with lambda
                .collect(Collectors.toList()));
    }
}