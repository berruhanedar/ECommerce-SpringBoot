package com.berru.app.ecommercespringboot.mapper;

import com.berru.app.ecommercespringboot.dto.OrderItemDTO;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemDTO toDto(OrderItem orderItem);

    OrderItem toEntity(OrderItemDTO orderItemDTO);

    void updateEntityFromDto(OrderItemDTO orderItemDTO, @MappingTarget OrderItem orderItem);
}
