package com.berru.app.ecommercespringboot.mapper;


import com.berru.app.ecommercespringboot.dto.NewShoppingCartRequestDTO;
import com.berru.app.ecommercespringboot.dto.ShoppingCartDTO;
import com.berru.app.ecommercespringboot.dto.UpdateShoppingCartRequestDTO;
import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartDTO toDTO(ShoppingCart shoppingCart);

    ShoppingCart toEntity(NewShoppingCartRequestDTO dto);

    void updateShoppingCartFromDTO(UpdateShoppingCartRequestDTO dto, @MappingTarget ShoppingCart shoppingCart);
}
