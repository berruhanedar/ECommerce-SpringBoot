package com.berru.app.ecommercespringboot.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "shopping_cart_id")
    private Integer shoppingCartId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total price")
    private BigDecimal totalPrice;
}
