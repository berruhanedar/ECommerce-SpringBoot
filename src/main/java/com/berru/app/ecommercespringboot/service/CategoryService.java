package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.CategoryDTO;
import com.berru.app.ecommercespringboot.dto.NewCategoryRequestDTO;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.mapper.CategoryMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public ResponseEntity<CategoryDTO> create(NewCategoryRequestDTO newCategoryRequestDTO) {
        Category parentCategory = null;
        if (newCategoryRequestDTO.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(newCategoryRequestDTO.getParentCategoryId()).orElse(null);
        }

        Category category = Category.builder()
                .name(newCategoryRequestDTO.getName())
                .parentCategory(parentCategory)
                .build();

        Category savedCategory = categoryRepository.save(category);
        CategoryDTO categoryDTO = categoryMapper.toCategoryDTO(savedCategory);

        return ResponseEntity.status(201).body(categoryDTO);
    }

    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categoryMapper.toCategoryDTOList(categories);

        List<CategoryDTO> categoryTree = buildCategoryTree(categoryDTOs);

        return ResponseEntity.ok(categoryTree);
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
