package com.berru.app.ecommercespringboot.mapper;

import com.berru.app.ecommercespringboot.dto.NewProductRequestDTO;
import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.dto.UpdateProductRequestDTO;
import com.berru.app.ecommercespringboot.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    ProductDTO toDto(Product product);

    Product toEntity(NewProductRequestDTO dto);

    void updateProductFromDto(UpdateProductRequestDTO dto, @MappingTarget Product product);

    List<ProductDTO> toDtoList(List<Product> products);

}