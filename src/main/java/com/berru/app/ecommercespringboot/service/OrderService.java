package com.berru.app.ecommercespringboot.service;


import com.berru.app.ecommercespringboot.dto.CustomerDTO;
import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderItemRequestDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Order;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.enums.OrderStatus;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.CustomerMapper;
import com.berru.app.ecommercespringboot.mapper.OrderMapper;
import com.berru.app.ecommercespringboot.repository.OrderRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;


    @Transactional
    public OrderDTO placeOrder(PlaceOrderDTO placeOrderDTO) {
        CustomerDTO customerDTO = customerService.getCustomerById(placeOrderDTO.getCustomerId());
        Customer customer = customerMapper.toEntity(customerDTO);

        Order order = orderMapper.toEntity(placeOrderDTO);
        order.setCustomer(customer);
        order.setTotalAmount(placeOrderDTO.getTotalAmount());

        order = orderRepository.save(order);

        List<OrderItem> cartItems = shoppingCartService.getCartItems(placeOrderDTO.getCustomerId());
        validateAndSaveOrderItems(order, cartItems);

        shoppingCartService.deleteShoppingCart(placeOrderDTO.getCustomerId());

        return orderMapper.toDto(order);
    }

    private void validateAndSaveOrderItems(Order order, List<OrderItem> cartItems) {
        cartItems.forEach(item -> {
            orderItemService.checkStockAvailability(item);
            item.setOrder(order);
            orderItemService.addOrderedProducts(item);
        });
    }

    public PaginationResponse<OrderDTO> listAllOrders(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        List<Order> orders = orderRepository.findAll(pageable).getContent();
        long totalElements = orderRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        boolean isLast = pageNo + 1 >= totalPages;

        return PaginationResponse.<OrderDTO>builder()
                .content(orders.stream()
                        .map(orderMapper::toDto)
                        .collect(Collectors.toList()))
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .isLast(isLast)
                .build();
    }

    @Transactional
    public PaginationResponse<OrderDTO> listOrders(int customerId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "orderDate"));
        Page<Order> orderPage = orderRepository.findAllByCustomerIdOrderByOrderDateDesc(customerId, pageable);

        return PaginationResponse.<OrderDTO>builder()
                .content(orderPage.getContent().stream()
                        .map(orderMapper::toDto)
                        .collect(Collectors.toList()))
                .pageNo(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .isLast(orderPage.isLast())
                .build();
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

        Optional.of(order)
                .filter(o -> o.getOrderStatus() != OrderStatus.CANCELLED)
                .ifPresentOrElse(
                        o -> {
                            o.setOrderStatus(OrderStatus.CANCELLED);
                            orderRepository.save(o);

                            order.getOrderItems().forEach(item -> {
                                Product product = item.getProduct();
                                product.setQuantity(product.getQuantity() + item.getQuantity());
                                productRepository.save(product);
                            });
                        },
                        () -> {
                            throw new IllegalArgumentException("Order is already cancelled");
                        }
                );
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

        Optional.of(order)
                .filter(o -> o.getOrderStatus() == OrderStatus.CANCELLED)
                .ifPresentOrElse(o -> {
                    o.setOrderStatus(OrderStatus.ORDERED);
                    orderRepository.save(o);

                    o.getOrderItems().forEach(item -> {
                        Product product = item.getProduct();
                        product.setQuantity(product.getQuantity() + item.getQuantity());
                        productRepository.save(product);
                    });
                }, () -> {
                    throw new IllegalArgumentException("Order is not cancelled, cannot be reactivated.");
                });

        return orderMapper.toDto(order);
    }

    @Transactional
    public OrderDTO markOrderAsDelivered(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        Optional.of(order)
                .filter(o -> o.getOrderStatus() != OrderStatus.DELIVERED)
                .orElseThrow(() -> new IllegalStateException("Order is already marked as delivered."));

        Optional.of(order)
                .filter(o -> o.getOrderStatus() == OrderStatus.SHIPPED)
                .ifPresent(o -> {
                    o.setOrderStatus(OrderStatus.DELIVERED);
                    orderRepository.save(o);
                });

        return orderMapper.toDto(order);
    }
}
