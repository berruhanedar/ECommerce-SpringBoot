package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.mapper.CategoryMapper;
import com.berru.app.ecommercespringboot.mapper.OrderMapper;
import com.berru.app.ecommercespringboot.mapper.ProductMapper;
import com.berru.app.ecommercespringboot.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderRepository orderRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;



    @Test
    void placeOrder() {
    }

    @Test
    void listAllOrders() {
    }

    @Test
    void listOrders() {
    }

    @Test
    void getOrderById() {
    }

    @Test
    void cancelOrder() {
    }

    @Test
    void shipOrder() {
    }

    @Test
    void deliverOrder() {
    }
}