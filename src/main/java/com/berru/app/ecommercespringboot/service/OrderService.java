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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


    @Transactional
    public OrderDTO placeOrder(PlaceOrderDTO placeOrderDTO) {
        // Müşteriyi çek - check the order
        Customer customer = customerRepository.findById(placeOrderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + placeOrderDTO.getCustomerId()));

        // Adresi çek
        Address address = addressRepository.findById(placeOrderDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + placeOrderDTO.getAddressId()));

        // Alışveriş sepetini çek
        ShoppingCart shoppingCart = shoppingCartRepository.findById(placeOrderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Shopping cart not found for customer ID: " + placeOrderDTO.getCustomerId()));

        // Total fiyatı hesapla (checkout işlemi)
        shoppingCartService.checkoutCart(shoppingCart.getId());
        BigDecimal totalPrice = shoppingCart.getTotalPrice();

        // Sepetteki ürünlerin stok miktarlarını kontrol et - enough stock?
        validateOrderItems(shoppingCart.getItems());

        // Mevcut bakiye total amount için yeterli mi kontrol et - is it valid
        if (customer.getBalance().compareTo(totalPrice) < 0) {
            throw new InsufficientBalanceException("Not enough balance to complete the order.");
        }
        // Ürün miktarlarını ve müşterinin bakiyesini güncelle - process oroders
        updateProductQuantitiesAndCustomerBalance(shoppingCart.getItems(), customer, totalPrice);

        // Siparişi oluştur ve kaydet - ordered
        Order order = Order.createOrder(customer, address, totalPrice, shoppingCart.getItems());
        orderRepository.save(order);

        // Sepeti temizle
        shoppingCartService.deleteShoppingCart(shoppingCart.getId());

        // OrderMapper kullanarak sipariş bilgilerini OrderDTO'ya dönüştür
        return orderMapper.toDto(order);
    }

    // Sepetteki ürünlerin stok miktarlarını kontrol eden yardımcı method
    private void validateOrderItems(List<ShoppingCartItem> items) {
        for (ShoppingCartItem item : items) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                throw new InsufficientQuantityException("Not enough stock for product: " + product.getName());
            }
        }
    }

    // Ürün miktarlarını ve müşterinin bakiyesini güncelleyen yardımcı method
    private void updateProductQuantitiesAndCustomerBalance(List<ShoppingCartItem> items, Customer customer, BigDecimal totalPrice) {
        for (ShoppingCartItem item : items) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }
        // Müşterinin bakiyesini güncelle
        customer.setBalance(customer.getBalance().subtract(totalPrice));
        customerRepository.save(customer);
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
        Order order = orderRepository.findByIdWithOrderItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDto(order);
    }

    @Transactional
    public void cancelOrder(Integer orderId) {
        // Siparişi bul
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Siparişin iptal edilebilir olup olmadığını kontrol et
        if (order.getOrderStatus() == OrderStatus.SHIPPED
                || order.getOrderStatus() == OrderStatus.DELIVERED
                || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("Order cannot be cancelled in its current state.");
        }

        // Siparişi iptal et ve durumu güncelle
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // Ürün stoklarını geri yükle
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            productRepository.save(product);
        }

        // Müşterinin bakiyesini iade et
        Customer customer = order.getCustomer();
        customer.setBalance(customer.getBalance().add(order.getTotalAmount()));
        customerRepository.save(customer);
    }

    @Transactional
    public void shipOrder(Integer orderId) {
        // Siparişi bul
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Siparişin durumunu kontrol et
        if (order.getOrderStatus() != OrderStatus.ORDERED) {
            throw new InvalidOrderStateException("Order cannot be shipped because it is not in ORDERED state.");
        }


        // Siparişi SHIPPED durumuna getir
        order.setOrderStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }

    @Transactional
    public void deliverOrder(Integer orderId) {
        // Siparişi bul
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Siparişin durumunu kontrol et
        if (order.getOrderStatus() != OrderStatus.SHIPPED) {
            throw new InvalidOrderStateException("Order must be in SHIPPED status to be delivered.");
        }

        // Siparişin durumunu DELIVERED olarak güncelle
        order.setOrderStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
    }
}
