package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.entity.Address;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Order;
import com.berru.app.ecommercespringboot.entity.OrderItem;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import com.berru.app.ecommercespringboot.entity.ShoppingCartItem;
import com.berru.app.ecommercespringboot.enums.OrderStatus;
import com.berru.app.ecommercespringboot.exception.InsufficientBalanceException;
import com.berru.app.ecommercespringboot.exception.InsufficientQuantityException;
import com.berru.app.ecommercespringboot.exception.InvalidOrderStateException;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.OrderMapper;
import com.berru.app.ecommercespringboot.repository.OrderRepository;
import com.berru.app.ecommercespringboot.repository.AddressRepository;
import com.berru.app.ecommercespringboot.repository.CustomerRepository;
import com.berru.app.ecommercespringboot.repository.ShoppingCartRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import com.berru.app.ecommercespringboot.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public OrderDTO placeOrder(PlaceOrderDTO placeOrderDTO) {
        Customer customer = customerRepository.findById(placeOrderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + placeOrderDTO.getCustomerId()));
        Address address = addressRepository.findById(placeOrderDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + placeOrderDTO.getAddressId()));
        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(placeOrderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Shopping cart not found for customer ID: " + placeOrderDTO.getCustomerId()));

        shoppingCartService.checkoutCart(shoppingCart.getId());
        BigDecimal totalPrice = shoppingCart.getTotalPrice();

        validateOrderItems(shoppingCart.getItems());
        validateCustomerBalance(customer, totalPrice);
        updateProductQuantitiesAndCustomerBalance(shoppingCart.getItems(), customer, totalPrice);

        Order order = Order.createOrder(customer, address, totalPrice, shoppingCart.getItems());
        orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.toDto(order);

        kafkaProducerService.sendMessage("order-topic", orderDTO);

        return orderDTO;
    }


    private void validateOrderItems(List<ShoppingCartItem> items) {
        for (ShoppingCartItem item : items) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                throw new InsufficientQuantityException("Not enough stock for product: " + product.getName());
            }
        }
    }

    private void validateCustomerBalance(Customer customer, BigDecimal totalPrice) {
        if (customer.getBalance().compareTo(totalPrice) < 0) {
            throw new InsufficientBalanceException("Customer balance is insufficient for the order amount.");
        }
    }

    private void updateProductQuantitiesAndCustomerBalance(List<ShoppingCartItem> items, Customer customer, BigDecimal totalPrice) {
        for (ShoppingCartItem item : items) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }
        customer.setBalance(customer.getBalance().subtract(totalPrice));
        customerRepository.save(customer);
    }

    @Transactional
    @Cacheable(value = "orders")
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
    @Cacheable(value = "orders", key = "#customerId")
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
        Order order = orderRepository.findByIdWithOrderItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDto(order);
    }

    @Transactional
    @CacheEvict(value = "orders", key = "#orderId")
    public void cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        if (order.getOrderStatus() == OrderStatus.SHIPPED
                || order.getOrderStatus() == OrderStatus.DELIVERED
                || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("Order cannot be cancelled in its current state.");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            productRepository.save(product);
        }

        Customer customer = order.getCustomer();
        customer.setBalance(customer.getBalance().add(order.getTotalAmount()));
        customerRepository.save(customer);
    }

    @Transactional
    public void shipOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getOrderStatus() != OrderStatus.ORDERED) {
            throw new InvalidOrderStateException("Order cannot be shipped because it is not in ORDERED state.");
        }

        order.setOrderStatus(OrderStatus.SHIPPED);

        orderRepository.save(order);
        }

    @Transactional
    public void deliverOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (order.getOrderStatus() != OrderStatus.SHIPPED) {
            throw new InvalidOrderStateException("Order must be in SHIPPED status to be delivered.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> searchOrdersByRsql(String query) {

        RSQLParser parser = new RSQLParser();
        Node rootNode = parser.parse(query);

        CustomRsqlVisitor<Order> visitor = new CustomRsqlVisitor<>();
        Specification<Order> spec = rootNode.accept(visitor);

        List<Order> orders = orderRepository.findAll(spec);

        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
}