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
    public CategoryDTO create(NewCategoryRequestDTO newCategoryRequestDTO) {
        Category parentCategory = newCategoryRequestDTO.getParentCategoryId() != null
                ? categoryRepository.findById(newCategoryRequestDTO.getParentCategoryId()).orElse(null)
                : null;
        Category category = categoryMapper.toCategoryEntity(newCategoryRequestDTO);
        category.setParentCategory(parentCategory);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDTO(savedCategory);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<CategoryDTO> getAllCategoriesPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryDTO> categoryDTOList = categoryPage.getContent().stream()
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
        List<CategoryDTO> categoryTree = buildCategoryTree(categoryDTOList);
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
        List<Category> categoryTree = categoryRepository.findCategoryTreeById(categoryId);
        List<Integer> categoryIds = categoryTree.stream().map(Category::getId).collect(Collectors.toList());
        List<Product> products = productRepository.findByCategoryIdIn(categoryIds);
        return productMapper.toDtoList(products);
    }

    @Transactional
    public Optional<CategoryDTO> updateCategory(Integer id, UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        return categoryRepository.findById(id).map(category -> {
            categoryMapper.updateCategoryFromDTO(updateCategoryRequestDTO, category);
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
                            throw new NotFoundException("Category not found");
                        }
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
