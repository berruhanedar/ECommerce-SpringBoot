package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.entity.OrderItem;
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
}
