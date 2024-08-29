package com.berru.app.ecommercespringboot.mapper;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderRequestDTO;
import com.berru.app.ecommercespringboot.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(PlaceOrderDTO dto);

    Order toEntity(UpdateOrderRequestDTO dto, @MappingTarget Order order);

    OrderDTO toDto(Order order);

}
