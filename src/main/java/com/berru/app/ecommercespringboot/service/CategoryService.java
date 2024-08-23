package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.CategoryMapper;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public CategoryDTO create(NewCategoryRequestDTO newCategoryRequestDTO) {
        Category parentCategory = Optional.ofNullable(newCategoryRequestDTO.getParentCategoryId())
                .flatMap(categoryRepository::findById)
                .orElse(null);

        Category category = Category.builder()
                .name(newCategoryRequestDTO.getName())
                .parentCategory(parentCategory)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDTO(savedCategory);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<CategoryDTO> getAllCategoriesPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // Kategorileri sayfalı olarak al
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        // Kategorileri DTO'ya dönüştür
        List<CategoryDTO> categoryDTOList = categoryPage.getContent().stream()
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());

        // Kategori ağaç yapısını oluştur
        List<CategoryDTO> categoryTree = buildCategoryTree(categoryDTOList);

        // Sayfalama yanıtını oluştur
        return PaginationResponse.<CategoryDTO>builder()
                .content(categoryTree)
                .pageNo(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .isLast(categoryPage.isLast())
                .build();
    }


    @Transactional
    public List<ProductDTO> getProductsByCategoryId(Integer categoryId) {
        List<Integer> categoryIds = getAllCategoryIds(categoryId);
        List<Product> products = productRepository.findByCategoryIdIn(categoryIds);
        return productMapper.toDtoList(products);
    }

    private List<Integer> getAllCategoryIds(Integer categoryId) {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);

        categoryRepository.findByParentCategoryId(categoryId).stream()
                .map(Category::getId)
                .map(this::getAllCategoryIds)
                .forEach(categoryIds::addAll);

        return categoryIds;
    }

    @Transactional
    public Optional<CategoryDTO> updateCategory(Integer id, UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(updateCategoryRequestDTO.getName());
            Category parentCategory = categoryRepository.findById(updateCategoryRequestDTO.getParentCategoryId())
                    .orElse(null);
            category.setParentCategory(parentCategory);
            Category updatedCategory = categoryRepository.save(category);
            return categoryMapper.toCategoryDTO(updatedCategory);
        });
    }

    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(
                        categoryRepository::delete,
                        () -> {
                            throw new NotFoundException("Category not found"); }
                );
    }

    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories) {
        return categories.stream()
                .filter(category -> category.getParentId() == null)
                .peek(rootCategory -> populateChildren(rootCategory, categories))
                .collect(Collectors.toList());
    }

    private void populateChildren(CategoryDTO parent, List<CategoryDTO> categories) {
        List<CategoryDTO> children = categories.stream()
                .filter(category -> parent.getId().equals(category.getParentId()))
                .peek(child -> populateChildren(child, categories))
                .collect(Collectors.toList());

        parent.setChildren(children);
    }

}