package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Order;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.OrderMapper;
import com.berru.app.ecommercespringboot.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDTO placeOrder(PlaceOrderDTO placeOrderDTO) {
        Order order = orderMapper.toEntity(placeOrderDTO);
        order.setCustomer(new Customer(placeOrderDTO.getCustomerId()));
        order.setTotalAmount(placeOrderDTO.getTotalAmount());
        order = orderRepository.save(order);

        List<OrderItem> cartItems = shoppingCartService.getCartItems(placeOrderDTO.getCustomerId());
        for (OrderItem item : cartItems) {
            item.setOrder(order);
            orderItemService.addOrderedProducts(item);
        }

        shoppingCartService.deleteShoppingCart(placeOrderDTO.getCustomerId());

        return orderMapper.toDto(order);
    }

    public List<OrderDTO> listOrders(int customerId) {
        List<Order> orders = orderRepository.findAllByCustomerIdOrderByOrderDateDesc(customerId);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO getOrderById(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDto(order);
    }
}
