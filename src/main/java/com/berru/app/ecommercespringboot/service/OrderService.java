package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderItemRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderRequestDTO;
import com.berru.app.ecommercespringboot.entity.Address;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Order;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.entity.ShoppingCartItem;
import com.berru.app.ecommercespringboot.enums.OrderStatus;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.OrderMapper;
import com.berru.app.ecommercespringboot.repository.AddressRepository;
import com.berru.app.ecommercespringboot.repository.CustomerRepository;
import com.berru.app.ecommercespringboot.repository.OrderRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;


    @Transactional
    public OrderDTO placeOrder(PlaceOrderDTO placeOrderDTO) {

        // 1 check the order
        Customer customer = customerRepository.findById(placeOrderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + placeOrderDTO.getCustomerId()));
        Address address = addressRepository.findById(placeOrderDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + placeOrderDTO.getAddressId()));

        List<ShoppingCartItem> shoppingCartItems = shoppingCartService.getCartItems(placeOrderDTO.getCustomerId());

        //1.2 order total price çekme

        // 2 enough stock ? checkStockAvailability (ORDERITEMSERVICETE)

        // 3 is it valid ? bakiye kontrolu olabilir

        // 4 processing = quantity ve bakiye miktarı düzenlenir

        // 5 status ordered olur


        BigDecimal calculatedTotalPrice = calculateTotalPrice(shoppingCartItems);
        Order order = Order.createOrder(customer, address, calculatedTotalPrice);




        validateOrderItems(order, shoppingCartItems);

        Order savedOrder = orderRepository.save(order);

        saveOrderItems(savedOrder, shoppingCartItems);

        shoppingCartService.deleteShoppingCart(placeOrderDTO.getCustomerId());

        return orderMapper.toDto(savedOrder);


        //status ordered olacak
    }







    private BigDecimal calculateTotalPrice(List<ShoppingCartItem> cartItems) {
        return cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateOrderItems(Order order, List<ShoppingCartItem> cartItems) {
        cartItems.forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);

            orderItemService.checkStockAvailability(orderItem);
        });
    }

    private void saveOrderItems(Order savedOrder, List<ShoppingCartItem> cartItems) {
        cartItems.forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(savedOrder);

            orderItemService.addOrderedProducts(orderItem);
        });
    }


    @Transactional
    public PaginationResponse<OrderDTO> listAllOrders(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Order> orderPage = orderRepository.findAll(pageable);

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

        if (updateOrderRequestDTO.getOrderItems() != null && !updateOrderRequestDTO.getOrderItems().isEmpty()) {
            for (UpdateOrderItemRequestDTO itemDTO : updateOrderRequestDTO.getOrderItems()) {
                OrderItem orderItem = order.getOrderItems().stream()
                        .filter(item -> item.getOrderItemId().equals(itemDTO.getOrderItemId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + itemDTO.getOrderItemId()));

                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDTO.getProductId()));
                orderItem.setProduct(product);

                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setOrderedProductPrice(itemDTO.getOrderedProductPrice());

                orderItemService.checkStockAvailability(orderItem);
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
