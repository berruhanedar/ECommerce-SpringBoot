package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderItemRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderRequestDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Order;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.enums.OrderStatus;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.OrderMapper;
import com.berru.app.ecommercespringboot.repository.OrderRepository;

import com.berru.app.ecommercespringboot.repository.ProductRepository;
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
    private final ProductRepository productRepository;

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

    @Transactional
    public void cancelOrder(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Order is already cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }

    @Transactional
    public OrderDTO updateOrder(int orderId, UpdateOrderRequestDTO updateOrderRequestDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Update the order status
        if (updateOrderRequestDTO.getOrderStatus() != null) {
            order.setOrderStatus(updateOrderRequestDTO.getOrderStatus());
        }

        // Update the total amount
        if (updateOrderRequestDTO.getTotalAmount() != null) {
            order.setTotalAmount(updateOrderRequestDTO.getTotalAmount());
        }

        // Update order items
        if (updateOrderRequestDTO.getOrderItems() != null && !updateOrderRequestDTO.getOrderItems().isEmpty()) {
            for (UpdateOrderItemRequestDTO itemDTO : updateOrderRequestDTO.getOrderItems()) {
                OrderItem orderItem = order.getOrderItems().stream()
                        .filter(item -> item.getOrderItemId().equals(itemDTO.getOrderItemId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + itemDTO.getOrderItemId()));

                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setOrderedProductPrice(itemDTO.getOrderedProductPrice());
                orderItem.setProduct(new Product(itemDTO.getProductId())); // Assuming you have a way to fetch the product
            }
        }

        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }



}
