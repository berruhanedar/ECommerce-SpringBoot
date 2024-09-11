package com.berru.app.ecommercespringboot.mapper;

import com.berru.app.ecommercespringboot.dto.OrderItemDTO;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    void updateEntityFromDto(OrderItemDTO orderItemDTO, @MappingTarget OrderItem orderItem);

    OrderItemDTO toOrderItemDto(OrderItem orderItem);

    List<OrderItemDTO> toOrderItemDto(List<OrderItem> orderItems);

    OrderItem toEntity(OrderItemDTO dto);

    List<OrderItem> toOrderItems(List<OrderItemDTO> orderItemDTOs);
}
