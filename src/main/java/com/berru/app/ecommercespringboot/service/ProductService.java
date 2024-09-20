package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.CategoryDTO;
import com.berru.app.ecommercespringboot.dto.NewProductRequestDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.dto.UpdateProductRequestDTO;
import com.berru.app.ecommercespringboot.entity.Category;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.mapper.CategoryMapper;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.CategoryRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.berru.app.ecommercespringboot.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductDTO create(NewProductRequestDTO newProductRequestDTO) {
        Product product = productMapper.toEntity(newProductRequestDTO);
        Category category = categoryRepository.findById(newProductRequestDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(Integer id) {
        ProductDTO productDTO = productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        List<CategoryDTO> categoryTree = getCategoryTreeForProduct(productDTO.getCategoryId());
        productDTO.setCategoryTree(categoryTree);
        return productDTO;
    }

    @Transactional
    @CachePut(value = "products", key = "#id")
    public ProductDTO updateProduct(Integer id, UpdateProductRequestDTO updateProductRequestDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Optional.ofNullable(updateProductRequestDTO.getCategoryId())
                .ifPresent(categoryId -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new NotFoundException("Category not found"));
                    existingProduct.setCategory(category);
                });

        productMapper.updateProductFromDto(updateProductRequestDTO, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Integer id) {
        Optional.of(id)
                .filter(productRepository::existsById)
                .ifPresentOrElse(
                        productId -> productRepository.deleteById(productId),
                        () -> {
                            throw new NotFoundException("Product not found"); }
                );
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#pageNo + '-' + #pageSize")
    public PaginationResponse<ProductDTO> listPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(product -> {
                    ProductDTO productDTO = productMapper.toDto(product);
                    List<CategoryDTO> categoryTree = getCategoryTreeForProduct(product.getCategory().getId());
                    productDTO.setCategoryTree(categoryTree);
                    return productDTO;
                })
                .collect(Collectors.toList());

        return PaginationResponse.<ProductDTO>builder()
                .content(productDTOList)
                .pageNo(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .isLast(productPage.isLast())
                .build();
    }

    private List<CategoryDTO> getCategoryTreeForProduct(Integer categoryId) {
        var categoryPath = categoryMapper.toCategoryDTOList(categoryRepository.findCategoryPath(categoryId));
        for (int i = 1; i < categoryPath.size(); i++) {
            categoryPath.get(i - 1).setChildren(Collections.singletonList(categoryPath.get(i)));
            if (categoryPath.get(i).getId().equals(categoryId)) {
                categoryPath.get(i).setChildren(Collections.emptyList());
                break;
            }
        }
        return categoryPath.isEmpty() ? Collections.emptyList() : Collections.singletonList(categoryPath.get(0));
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> searchProductsByRsql(String query) {
        RSQLParser parser = new RSQLParser();
        Node rootNode = parser.parse(query);

        CustomRsqlVisitor<Product> visitor = new CustomRsqlVisitor<>();
        Specification<Product> spec = rootNode.accept(visitor);

        List<Product> products = productRepository.findAll(spec);

        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }


}