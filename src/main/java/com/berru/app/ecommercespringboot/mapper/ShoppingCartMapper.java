package com.berru.app.ecommercespringboot.mapper;

import com.berru.app.ecommercespringboot.dto.AddressDTO;
import com.berru.app.ecommercespringboot.dto.CustomerDTO;
import com.berru.app.ecommercespringboot.dto.ShoppingCartDTO;
import com.berru.app.ecommercespringboot.dto.ShoppingCartItemDTO;
import com.berru.app.ecommercespringboot.entity.Address;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import com.berru.app.ecommercespringboot.entity.ShoppingCartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartDTO toDTO(ShoppingCart shoppingCart);

    List<ShoppingCartItemDTO> toItemDTOs(List<ShoppingCartItem> items);

    CustomerDTO toCustomerDTO(Customer customer);
}
