package com.berru.app.ecommercespringboot.mapper;

import com.berru.app.ecommercespringboot.dto.ProductDTO;
import com.berru.app.ecommercespringboot.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category.id", target = "categoryId")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product productDTOToProduct(ProductDTO productDTO);
}
