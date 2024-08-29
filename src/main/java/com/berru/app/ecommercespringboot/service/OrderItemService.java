package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.entity.OrderItem;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public void addOrderedProducts(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    public void deleteOrderItem(Integer orderItemId) {
        if (!orderItemRepository.existsById(orderItemId)) {
            throw new ResourceNotFoundException("OrderItem not found with id: " + orderItemId);
        }
        orderItemRepository.deleteById(orderItemId);
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
