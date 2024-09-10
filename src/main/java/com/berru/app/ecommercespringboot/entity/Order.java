package com.berru.app.ecommercespringboot.entity;

import com.berru.app.ecommercespringboot.enums.OrderStatus;
import jakarta.persistence.*;
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
    // AUTO yerine IDENTITY daha uygundur, çünkü genelde IDENTITY ile kimlik üretimi daha verimli olur.
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Lazy yükleme ve zorunluluk şartı
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Lazy yükleme ve zorunluluk şartı
    @JoinColumn(name = "address_id")
    private Address address;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Statik metod, bir Order nesnesi oluşturmak için kullanılıyor.
    public static Order createOrder(Customer customer, Address address, BigDecimal totalAmount, List<ShoppingCartItem> shoppingCartItems) {
        Order order = Order.builder()
                .customer(customer)
                .address(address)
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.ORDERED)
                .build();

        if (order.getOrderItems() == null) {
            order.setOrderItems(new ArrayList<>()); // Eğer null ise yeni bir liste başlat
        }

        // Her bir ShoppingCartItem için OrderItem oluşturuluyor ve Order ile ilişkilendiriliyor
        for (ShoppingCartItem cartItem : shoppingCartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .order(order)
                    .build();
            order.getOrderItems().add(orderItem); // Burada listeye ekleniyor
        }
        return order;
    }
}

