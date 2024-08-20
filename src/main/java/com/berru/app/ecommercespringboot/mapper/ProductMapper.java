package com.berru.app.ecommercespringboot.mapper;

import com.berru.app.ecommercespringboot.dto.NewProductRequestDTO;
import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.dto.UpdateProductRequestDTO;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    ProductDTO toDto(Product product);

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    Product toEntity(NewProductRequestDTO dto);

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    void updateProductFromDto(UpdateProductRequestDTO dto, @MappingTarget Product product);

    List<ProductDTO> toDtoList(List<Product> products);


    @Named("mapCategory")
    default Category mapCategory(Integer categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);  // Set id directly
        return category;
    }
}
