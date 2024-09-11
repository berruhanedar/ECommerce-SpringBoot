package com.berru.app.ecommercespringboot.mapper;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderRequestDTO;
import com.berru.app.ecommercespringboot.entity.Order;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(PlaceOrderDTO dto);
    Order toEntity(UpdateOrderRequestDTO dto, @MappingTarget Order order);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderDTO toDto(Order order);
}