package com.berru.app.ecommercespringboot.entity;

import com.berru.app.ecommercespringboot.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public static Order createOrder(Customer customer, Address address, BigDecimal totalAmount, List<ShoppingCartItem> shoppingCartItems) {
        Order order = Order.builder()
                .customer(customer)
                .address(address)
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.ORDERED)
                .build();

        if (order.getOrderItems() == null) {
            order.setOrderItems(new ArrayList<>());
        }

        for (ShoppingCartItem cartItem : shoppingCartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .order(order) // Burada Order'ı OrderItem'a set ediyoruz
                    .build();
            order.getOrderItems().add(orderItem);
        }
        return order;
    }
}
