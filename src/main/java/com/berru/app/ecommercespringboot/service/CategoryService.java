package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.*;
import com.berru.app.ecommercespringboot.dto.DeleteCategoryResponseDTO;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.CategoryMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public ResponseEntity<CategoryDTO> getCategoryById(Integer id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            CategoryDTO categoryDTO = categoryMapper.toCategoryDTO(categoryOptional.get());
            return ResponseEntity.ok(categoryDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<CategoryDTO> updateCategory(Integer id, UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(updateCategoryRequestDTO.getName());
            Category parentCategory = categoryRepository.findById(updateCategoryRequestDTO.getParentCategoryId()).orElse(null);
            category.setParentCategory(parentCategory);
            Category updatedCategory = categoryRepository.save(category);
            CategoryDTO categoryDTO = categoryMapper.toCategoryDTO(updatedCategory);
            return ResponseEntity.ok(categoryDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<DeleteCategoryResponseDTO> deleteCategory(Integer id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            // Başarılı yanıt için DeleteCategoryResponseDTO oluşturuluyor
            DeleteCategoryResponseDTO response = new DeleteCategoryResponseDTO();
            response.setMessage("Category deleted successfully");
            response.setId(id); // Kategori ID'si döndürülebilir
//
            return ResponseEntity.ok(response);
        } else {

            throw new NotFoundException("Category not found");
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