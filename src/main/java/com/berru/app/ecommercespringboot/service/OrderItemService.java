package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.OrderItemDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderItemRequestDTO;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.entity.ShoppingCartItem;
import com.berru.app.ecommercespringboot.exception.InsufficientQuantityException;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.OrderItemMapper;
import com.berru.app.ecommercespringboot.repository.OrderItemRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductRepository productRepository;





    @Transactional
    public OrderItemDTO getOrderItemById(Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + orderItemId));
        return orderItemMapper.toDto(orderItem);
    }

    @Transactional
    public List<OrderItemDTO> getOrderItemsByOrderId(Integer orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteOrderItem(Integer orderItemId) {
        Optional.of(orderItemId)
                .filter(id -> orderItemRepository.existsById(id))
                .ifPresentOrElse(
                        id -> orderItemRepository.deleteById(id),
                        () -> {
                            throw new ResourceNotFoundException("OrderItem not found with id: " + orderItemId); }
                );
    }
}
