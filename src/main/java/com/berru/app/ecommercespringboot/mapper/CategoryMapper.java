package com.berru.app.ecommercespringboot.mapper;

import com.berru.app.ecommercespringboot.dto.CategoryDTO;
import com.berru.app.ecommercespringboot.dto.NewCategoryRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateCategoryRequestDTO;
import com.berru.app.ecommercespringboot.entity.Category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "parentCategory.id", target = "parentId")
    @Mapping(source = "children", target = "children")
    CategoryDTO toCategoryDTO(Category category);

    Category toCategoryEntity(NewCategoryRequestDTO newcategoryDTO);

    void updateCategoryFromDTO(UpdateCategoryRequestDTO updateCategoryRequestDTO, @MappingTarget Category category);

    List<CategoryDTO> toCategoryDTOList(List<Category> categories);
}
