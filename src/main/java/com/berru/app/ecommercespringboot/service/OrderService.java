package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderItemRequestDTO;
import com.berru.app.ecommercespringboot.dto.OrderItemDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Order;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.enums.OrderStatus;
import com.berru.app.ecommercespringboot.exception.InsufficientQuantityException;
import com.berru.app.ecommercespringboot.exception.NotFoundException;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.OrderItemMapper;
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
    private final OrderItemMapper orderItemMapper;

    @Transactional
    public OrderDTO placeOrder(PlaceOrderDTO placeOrderDTO) {
        Order order = createOrderFromDTO(placeOrderDTO);

        List<OrderItem> cartItems = shoppingCartService.getCartItems(placeOrderDTO.getCustomerId());
        validateAndSaveOrderItems(order, cartItems);

        shoppingCartService.deleteShoppingCart(placeOrderDTO.getCustomerId());

        return orderMapper.toDto(order);
    }

    private Order createOrderFromDTO(PlaceOrderDTO placeOrderDTO) {
        Order order = orderMapper.toEntity(placeOrderDTO);
        order.setCustomer(new Customer(placeOrderDTO.getCustomerId()));
        order.setTotalAmount(placeOrderDTO.getTotalAmount());
        return orderRepository.save(order);
    }

    private void validateAndSaveOrderItems(Order order, List<OrderItem> cartItems) {
        for (OrderItem item : cartItems) {
            checkStockAvailability(item);
            item.setOrder(order);
            orderItemService.addOrderedProducts(item);
        }
    }

    private void checkStockAvailability(OrderItem orderItem) {
        Integer productId = orderItem.getProduct().getId();
        Integer orderedQuantity = orderItem.getQuantity();
        Integer availableStock = productRepository.findStockByProductId(productId)
                .orElseThrow(() -> new NotFoundException("Product not found or stock is unavailable"));

        if (orderedQuantity > availableStock) {
            throw new InsufficientQuantityException("Ordered quantity exceeds available stock.");
        }
    }







    @Transactional
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

        if (updateOrderRequestDTO.getOrderStatus() != null) {
            order.setOrderStatus(updateOrderRequestDTO.getOrderStatus());
        }
        if (updateOrderRequestDTO.getTotalAmount() != null) {
            order.setTotalAmount(updateOrderRequestDTO.getTotalAmount());
        }

        if (updateOrderRequestDTO.getOrderItems() != null && !updateOrderRequestDTO.getOrderItems().isEmpty()) {
            for (UpdateOrderItemRequestDTO itemDTO : updateOrderRequestDTO.getOrderItems()) {
                OrderItem orderItem = order.getOrderItems().stream()
                        .filter(item -> item.getOrderItemId().equals(itemDTO.getOrderItemId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + itemDTO.getOrderItemId()));

                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setOrderedProductPrice(itemDTO.getOrderedProductPrice());

                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDTO.getProductId()));
                orderItem.setProduct(product);
            }
        }

        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Transactional
    public OrderDTO reactivateOrder(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getOrderStatus() != OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Order is not cancelled, cannot be reactivated.");
        }

        order.setOrderStatus(OrderStatus.ORDERED);
        orderRepository.save(order);

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        return orderMapper.toDto(order);
    }

    @Transactional
    public OrderDTO markOrderAsDelivered(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Order is already marked as delivered.");
        }

        if (order.getOrderStatus() != OrderStatus.SHIPPED) {
            throw new IllegalArgumentException("Order must be in SHIPPED status to be marked as DELIVERED.");
        }

        order.setOrderStatus(OrderStatus.DELIVERED);
        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }


    public void deleteOrderItemById(Integer orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
    }

    public OrderItemDTO getOrderItemById(Integer orderItemId) {
        return orderItemService.getOrderItemById(orderItemId);
    }

    public List<OrderItemDTO> getOrderItemsByOrderId(Integer orderId) {
        return orderItemService.getOrderItemsByOrderId(orderId);
    }

    public OrderItemDTO updateOrderItem(UpdateOrderItemRequestDTO updateOrderItemRequestDTO) {
        OrderItem updatedOrderItem = new OrderItem();
        updatedOrderItem.setOrderItemId(updateOrderItemRequestDTO.getOrderItemId());
        updatedOrderItem.setQuantity(updateOrderItemRequestDTO.getQuantity());
        updatedOrderItem.setOrderedProductPrice(updateOrderItemRequestDTO.getOrderedProductPrice());

        OrderItem savedOrderItem = orderItemService.updateOrderItem(updatedOrderItem);
        return orderItemMapper.toDto(savedOrderItem);
    }


}
