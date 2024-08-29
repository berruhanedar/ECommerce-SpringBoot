package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.OrderItemDTO;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.OrderItemMapper;
import com.berru.app.ecommercespringboot.repository.OrderItemRepository;
import com.berru.app.ecommercespringboot.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public void addOrderedProducts(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    public void deleteOrderItem(Integer orderItemId) {
        if (!orderItemRepository.existsById(orderItemId)) {
            throw new ResourceNotFoundException("OrderItem not found with id: " + orderItemId);
        }
        orderItemRepository.deleteById(orderItemId);
    }

    public OrderItemDTO getOrderItemById(Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + orderItemId));
        return orderItemMapper.toDto(orderItem);
    }

    public List<OrderItemDTO> getOrderItemsByOrderId(Integer orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrderItem updateOrderItem(OrderItem updatedOrderItem) {
        return orderItemRepository.findById(updatedOrderItem.getOrderItemId())
                .map(orderItem -> {
                    orderItem.setQuantity(updatedOrderItem.getQuantity());
                    orderItem.setOrderedProductPrice(updatedOrderItem.getOrderedProductPrice());
                    return orderItemRepository.save(orderItem);
                })
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with ID: " + updatedOrderItem.getOrderItemId()));
    }
}
