package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.CategoryMapper;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * CategoryDTO dönsün
     * @param newCategoryRequestDTO
     * @return
     */


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


    @Transactional
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categoryMapper.toCategoryDTOList(categories);
        return buildCategoryTree(categoryDTOs);
    }

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
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new NotFoundException("Product not found");
        }
    }


    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories) {
        List<CategoryDTO> rootCategories = categories.stream()
                .filter(category -> category.getParentId() == null)
                .collect(Collectors.toList());

        for (CategoryDTO rootCategory : rootCategories) {
            populateChildren(rootCategory, categories);
        }

        return rootCategories;
    }

    private void populateChildren(CategoryDTO parent, List<CategoryDTO> categories) {
        List<CategoryDTO> children = categories.stream()
                .filter(category -> parent.getId().equals(category.getParentId()))
                .collect(Collectors.toList());

        for (CategoryDTO child : children) {
            populateChildren(child, categories);
        }

        parent.setChildren(children);
    }

}