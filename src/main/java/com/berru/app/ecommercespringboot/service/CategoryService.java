package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.CategoryDTO;
import com.berru.app.ecommercespringboot.dto.NewCategoryRequestDTO;
import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.dto.UpdateCategoryRequestDTO;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.CategoryMapper;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

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

    /**
     * Creates a new category.
     *
     * @param newCategoryRequestDTO Data Transfer Object containing the category details.
     * @return CategoryDTO containing the details of the created category.
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

    /**
     * Retrieves all categories.
     *
     * @return List of CategoryDTO representing all categories.
     */
    @Transactional
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categoryMapper.toCategoryDTOList(categories);
        return buildCategoryTree(categoryDTOs);
    }

    /**
     * Retrieves products by category ID.
     *
     * @param categoryId The ID of the category.
     * @return List of ProductDTO representing the products in the category.
     */
    @Transactional
    public List<ProductDTO> getProductsByCategoryId(Integer categoryId) {
        List<Integer> categoryIds = getAllCategoryIds(categoryId);
        List<Product> products = productRepository.findByCategoryIdIn(categoryIds);
        return productMapper.toDtoList(products);
    }

    /**
     * Retrieves all category IDs, including subcategories.
     *
     * @param categoryId The ID of the category to retrieve.
     * @return List of Integer representing the category IDs.
     */
    private List<Integer> getAllCategoryIds(Integer categoryId) {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);

        categoryRepository.findByParentCategoryId(categoryId).stream()
                .map(Category::getId)
                .map(this::getAllCategoryIds)
                .forEach(categoryIds::addAll);

        return categoryIds;
    }

    /**
     * Updates a category by its ID.
     *
     * @param id The ID of the category to update.
     * @param updateCategoryRequestDTO Data Transfer Object containing the updated category details.
     * @return Optional containing the updated CategoryDTO, or empty if the category was not found.
     */
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

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to delete.
     * @throws NotFoundException if the category is not found.
     */
    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(
                        categoryRepository::delete,
                        () -> {
                            throw new NotFoundException("Category not found"); }
                );
    }


    /**
     * Builds a category tree structure.
     *
     * @param categories List of CategoryDTO representing all categories.
     * @return List of CategoryDTO representing the root categories with their children.
     */
    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories) {
        return categories.stream()
                .filter(category -> category.getParentId() == null)
                .peek(rootCategory -> populateChildren(rootCategory, categories))
                .collect(Collectors.toList());
    }

    /**
     * Populates the children of a parent category.
     *
     * @param parent The parent category.
     * @param categories List of CategoryDTO representing all categories.
     */
    private void populateChildren(CategoryDTO parent, List<CategoryDTO> categories) {
        List<CategoryDTO> children = categories.stream()
                .filter(category -> parent.getId().equals(category.getParentId()))
                .peek(child -> populateChildren(child, categories))
                .collect(Collectors.toList());

        parent.setChildren(children);
    }

}